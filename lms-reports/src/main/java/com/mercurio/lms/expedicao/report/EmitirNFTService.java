package com.mercurio.lms.expedicao.report;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.configuracoes.model.service.ImpressoraFormularioService;
import com.mercurio.lms.expedicao.model.CalculoNFT;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.dao.EmitirCTRDAO;
import com.mercurio.lms.expedicao.model.service.CalculoTributoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalTransporteService;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 * @spring.bean id="lms.expedicao.emitirNFTService"
 */
public class EmitirNFTService extends EmitirCTOService {
	private GerarNFTService gerarNFFPSService;
	private ImpressoraFormularioService impressoraFormularioService;
	private EmitirCTRDAO emitirCTRDAO;
	private CalculoTributoService calculoTributoService;
	private NotaFiscalTransporteService notaFiscalTransporteService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;

	public GerarNFTService getGerarNFFPSService() {
		return gerarNFFPSService;
	}
	public void setGerarNFFPSService(GerarNFTService gerarNFFPSService) {
		this.gerarNFFPSService = gerarNFFPSService;
	}

	public ImpressoraFormularioService getImpressoraFormularioService() {
		return impressoraFormularioService;
	}
	public void setImpressoraFormularioService(ImpressoraFormularioService impressoraFormularioService) {
		this.impressoraFormularioService = impressoraFormularioService;
	}

	public EmitirCTRDAO getEmitirCTRDAO() {
		return emitirCTRDAO;
	}

	public void setEmitirCTRDAO(EmitirCTRDAO emitirCTRDAO) {
		this.emitirCTRDAO = emitirCTRDAO;
	}

	public void setCalculoTributoService(CalculoTributoService calculoTributoService) {
		this.calculoTributoService = calculoTributoService;
	}

	/**
	 * Valida Impressora do Usuario e Proximo Formulario
	 * @param nrProximoFormulario
	 * @param dsMacAddress
	 */
	public void validateImpressoraFormulario(Long nrProximoFormulario, String dsMacAddress) {
		getEmitirDocumentoService().findImpressoraFormulario(SessionUtils.getFilialSessao().getIdFilial(), dsMacAddress, ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE, nrProximoFormulario, null, Boolean.TRUE);
	}

	/**
	 * Emite NFT/NFE - Emitir Remitir CTRC NFT
	 * 
	 * @param conhecimento
	 * @param impressoraFormulario
	 * @param tpOperacaoEmissao
	 * @param reentrega
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String storeEmitirNotaFiscal(Long idConhecimento, ImpressoraFormulario impressoraFormulario, String tpOperacaoEmissao,
			Boolean semNumeroReservado){
		
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		
		Conhecimento conhecimento = getEmitirCTRDAO().findConhecimentoById(idConhecimento, idFilial);
		
		/*Se o CTRC for Reentrega atualiza a emissao com a data atual*/				
		if(BooleanUtils.isTrue(semNumeroReservado) || ConstantesExpedicao.CONHECIMENTO_REENTREGA.equals(conhecimento.getTpConhecimento().getValue())){
			conhecimento.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());			
		}
		
		/*Valida Reemissao*/
		if(ConstantesExpedicao.CD_REEMISSAO.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_REEMISSAO_NFT.equals(tpOperacaoEmissao)) {
			if(!"E".equals(conhecimento.getTpSituacaoConhecimento().getValue())) {
				throw new BusinessException("LMS-04121");
			}
			int equalsDate = JTDateTimeUtils.getDataAtual().compareTo(conhecimento.getDhInclusao().toYearMonthDay());
			if(equalsDate != 0) {
				throw new BusinessException("LMS-04111");
			}
		}		
		Boolean aferido = conhecimento.getBlPesoAferido() != null && conhecimento.getBlPesoAferido();
		/*Atualiza Dados e Eventos*/
		if(ConstantesExpedicao.CD_EMISSAO.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_EMISSAO_NFT.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_GERACAO_CTE.equals(tpOperacaoEmissao)) {
			updateDadosConsignatario(conhecimento);
			if((aferido && conhecimento.getNrConhecimento() > 0) || (aferido && semNumeroReservado)) {
				updateDados(conhecimento, impressoraFormulario, new DomainValue("E"), null, null, semNumeroReservado);
			}else if (conhecimento.getBlPesoAferido() == null || !conhecimento.getBlPesoAferido() || conhecimento.getNrConhecimento() <= 0) {
				updateDados(conhecimento, impressoraFormulario, new DomainValue("C"), null, null, semNumeroReservado);
			}
			
			conhecimento.setBlExecutarVerificacaoDocumentoManifestado(Boolean.TRUE);
			storeEventos(conhecimento, idFilial);
		}
		
		String retorno = "";
		if(!"C".equals(conhecimento.getTpSituacaoConhecimento().getValue())){
		if (ConstantesExpedicao.CD_REEMISSAO_NTE.equals(tpOperacaoEmissao)) {
			MonitoramentoDocEletronico mde = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(idConhecimento);
			retorno = mde == null ? "" : mde.getIdMonitoramentoDocEletronic().toString();
			
			//Nota eletrônica é gerada quando selecionado Geração de CT-e/RPS-t na combo da tela
		} else if(ConstantesExpedicao.CD_GERACAO_CTE.equals(tpOperacaoEmissao) && ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue())){
			retorno = notaFiscalEletronicaService.storeNotaFiscalTransporteEletronica(conhecimento);
		}else if( ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue()) ){
			retorno = getGerarNFFPSService().generateNFT(conhecimento);
		}
		}
		return retorno;
	}
	
	public String executeEmitirNFT(
			Long idConhecimento,
			String tpOperacaoEmissao,
			Long nrProximoFormulario,
			String dsMacAddress){

		/*Busca Filial, Conhecimento e Proximo Formulario*/
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		Conhecimento conhecimento = getEmitirCTRDAO().findConhecimentoById(idConhecimento, idFilial);

		/*NFT utiliza o tipo de formulario  NFS*/
		ImpressoraFormulario impressoraFormulario = getEmitirDocumentoService().findImpressoraFormulario(SessionUtils.getFilialSessao().getIdFilial(), dsMacAddress, ConstantesExpedicao.NOTA_FISCAL_SERVICO, nrProximoFormulario,null, Boolean.TRUE);
		getEmitirDocumentoService().findProximoFormulario(impressoraFormulario);

		/*Valida Reemissao*/
		if(ConstantesExpedicao.CD_REEMISSAO.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_REEMISSAO_NFT.equals(tpOperacaoEmissao)) {
			if(!"E".equals(conhecimento.getTpSituacaoConhecimento().getValue())) {
				throw new BusinessException("LMS-04121");
			}
			int equalsDate = JTDateTimeUtils.getDataAtual().compareTo(conhecimento.getDhInclusao().toYearMonthDay());
			if(equalsDate != 0) {
				throw new BusinessException("LMS-04122");
			}
			if(CompareUtils.ne(conhecimento.getNrFormulario(), nrProximoFormulario)) {
				throw new BusinessException("LMS-04123");
			}
		}

		/*Atualiza Dados e Eventos*/
		if(ConstantesExpedicao.CD_EMISSAO.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_EMISSAO_NFT.equals(tpOperacaoEmissao)) {
			updateDados(conhecimento, impressoraFormulario, new DomainValue("E"), null, null, false);
			storeEventos(conhecimento, idFilial);
		}

		return getGerarNFFPSService().generateNFT(conhecimento);
	}

	/**
	 * @param conhecimento
	 * @param nrProximoFormulario
	 * @param dsMacAddress
	 * @return
	 */
	public String storeAndPrint(Conhecimento conhecimento, Long nrProximoFormulario, String dsMacAddress) {
		/** Valida Impressora do Usuario e Proximo Formulario antes de alteracoes */
		this.validateImpressoraFormulario(nrProximoFormulario, dsMacAddress);

		/** Seta Local de Entrega */
		notaFiscalTransporteService.setLocalEntregaConhecimentoNFT(conhecimento);

		/** Calcula Tributos */
		CalculoNFT calculoNFT = ExpedicaoUtils.getCalculoNFTInSession();
		calculoTributoService.calculaTributos(calculoNFT);
		CalculoFreteUtils.copyResult(conhecimento, calculoNFT);

		/** Grava Conhecimento */
		notaFiscalTransporteService.storeConhecimentoNFT(conhecimento, ConstantesExpedicao.TIPO_ISS);
		/** Rotina de Emissao Matricial da Nota Fiscal de Transporte */
		return this.executeEmitirNFT(conhecimento.getIdDoctoServico(), ConstantesExpedicao.CD_EMISSAO, nrProximoFormulario, dsMacAddress);
	}
	public void setNotaFiscalTransporteService(
			NotaFiscalTransporteService notaFiscalTransporteService) {
		this.notaFiscalTransporteService = notaFiscalTransporteService;
	}
	public void setNotaFiscalEletronicaService(
			NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

}