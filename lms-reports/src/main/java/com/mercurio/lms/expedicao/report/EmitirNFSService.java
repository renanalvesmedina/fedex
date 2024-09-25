package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.configuracoes.model.service.ImpressoraFormularioService;
import com.mercurio.lms.expedicao.model.CalculoNFServico;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.service.CalculoTributoService;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.EmitirDocumentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoService;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.tributos.model.service.ImpostoCalculadoService;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.service.CotacaoService;

/**
 * @author Claiton Grings
 * @spring.bean id="lms.expedicao.emitirNFSService"
 */
public class EmitirNFSService {
	private NotaFiscalServicoService notaFiscalServicoService;
	private GerarNFSService gerarNFSService;
	private ImpressoraFormularioService impressoraFormularioService;
	private EmitirDocumentoService emitirDocumentoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosService;
	private CotacaoService cotacaoService;
	private CalculoTributoService calculoTributoService;
	private ImpostoCalculadoService impostoCalculadoService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private DevedorDocServService devedorDocServService;

	public NotaFiscalServicoService getNotaFiscalServicoService() {
		return notaFiscalServicoService;
	}
	public void setNotaFiscalServicoService(NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}
	public GerarNFSService getGerarNFSService() {
		return gerarNFSService;
	}
	public void setGerarNFSService(GerarNFSService gerarNFSService) {
		this.gerarNFSService = gerarNFSService;
	}
	public ImpressoraFormularioService getImpressoraFormularioService() {
		return impressoraFormularioService;
	}
	public void setImpressoraFormularioService(ImpressoraFormularioService impressoraFormularioService) {
		this.impressoraFormularioService = impressoraFormularioService;
	}
	public EmitirDocumentoService getEmitirDocumentoService() {
		return emitirDocumentoService;
	}
	public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}
	public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosService() {
		return incluirEventosService;
	}
	public void setIncluirEventosService(IncluirEventosRastreabilidadeInternacionalService incluirEventosService) {
		this.incluirEventosService = incluirEventosService;
	}

	
	
	public Long storeNFS(NotaFiscalServico notaFiscalServico, Long idFilial, String tpOperacaoEmissao, Long nrProximoFormulario,
			String dsMacAddress ) {
		//LMS-3715
		notaFiscalServico.setVlImpostoPesoDeclarado(BigDecimal.ZERO);
		notaFiscalServico.setVlIcmsSubstituicaoTributariaPesoDeclarado(null);
		
		List<Cotacao> cotacoes = notaFiscalServico.getCotacoes();
		if(cotacoes != null) {
			Cotacao cotacao = cotacaoService.findById(cotacoes.remove(0).getIdCotacao());
			cotacoes.add(cotacao);
		}
		// Calcula tributos na emissao
		if(ConstantesExpedicao.CD_EMISSAO.equals(tpOperacaoEmissao)) {
			CalculoNFServico calculoNFServico = ExpedicaoUtils.getCalculoNFSInSession();
			calculoTributoService.calculaTributos(calculoNFServico);
			if( calculoNFServico.getImpostosCalculados() != null){
				impostoCalculadoService.storeAll(calculoNFServico.getImpostosCalculados());
			}
			// Atribui Dados a Nota Fiscal de Servico
			CalculoFreteUtils.copyResult(notaFiscalServico, calculoNFServico);
			// Salva NFS na Sessao
			ExpedicaoUtils.setNFSInSession(notaFiscalServico);
		}
		Long toReturn = this.executeEmitirNFS(notaFiscalServico, idFilial, tpOperacaoEmissao, nrProximoFormulario, dsMacAddress);
		//gera o monitoramento eletronico
		if( ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equalsIgnoreCase(notaFiscalServico.getTpNotaFiscalServico().getValue()) ){		
			notaFiscalEletronicaService.storeNotaFiscalServicoEletronica( notaFiscalServico );
	}
		return toReturn;

	}

	public NotaFiscalServico executeReemitirNFS(Long idNotaFiscalServico, Long idFilial, Long nrProximoFormulario, String dsMacAddress) {
		NotaFiscalServico notaFiscalServico = notaFiscalServicoService.findById(idNotaFiscalServico);
		this.executeEmitirNFS(notaFiscalServico, idFilial, ConstantesExpedicao.CD_REEMISSAO, nrProximoFormulario, dsMacAddress);
		return notaFiscalServico;
	}
	
		
	public Long executeEmitirNFS(
			NotaFiscalServico notaFiscalServico,
			Long idFilial,
			String tpOperacaoEmissao,
			Long nrProximoFormulario,
			String dsMacAddress) {

		/** Busca e Valida Formulario da Impressora */
		ImpressoraFormulario impressoraFormulario = null;
		if( ConstantesExpedicao.NOTA_FISCAL_SERVICO.equalsIgnoreCase(notaFiscalServico.getTpNotaFiscalServico().getValue()) ){
			impressoraFormulario = getEmitirDocumentoService().findImpressoraFormulario(idFilial, dsMacAddress,
					ConstantesExpedicao.NOTA_FISCAL_SERVICO, nrProximoFormulario, null, Boolean.FALSE);
		getEmitirDocumentoService().findProximoFormulario(impressoraFormulario);
		}

		/** Valida REEMISSAO */
		if(ConstantesExpedicao.CD_REEMISSAO.equals(tpOperacaoEmissao)) {
			if(!ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO.equals(notaFiscalServico.getTpSituacaoNf().getValue())) {
				throw new BusinessException("LMS-04121");
			}
			int equalsDate = JTDateTimeUtils.getDataAtual().compareTo(notaFiscalServico.getDhInclusao().toYearMonthDay());
			if(equalsDate != 0) {
				throw new BusinessException("LMS-04122");
			}
		}
		if(ConstantesExpedicao.CD_EMISSAO.equals(tpOperacaoEmissao)) {
			/** Salva EMISSAO */
			updateDados(notaFiscalServico, impressoraFormulario);
		}
		
		/** Grava Eventos somente para notas que NÃO sejam NFS ou NSE*/
		if( !ConstantesExpedicao.NOTA_FISCAL_SERVICO.equalsIgnoreCase(notaFiscalServico.getTpNotaFiscalServico().getValue())
				&& !ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equalsIgnoreCase(notaFiscalServico.getTpNotaFiscalServico().getValue())){
			storeEventos(notaFiscalServico, idFilial);
		}
		/** Monta e retorna form p/ Impressão */
		return notaFiscalServico.getIdDoctoServico();
	}

	private void updateDados(NotaFiscalServico notaFiscalServico, ImpressoraFormulario impressoraFormulario) {
		
		if( impressoraFormulario != null ){
		notaFiscalServico.setNrAidf(impressoraFormulario.getControleFormulario().getNrAidf());
		notaFiscalServico.setNrFormulario(IntegerUtils.getInteger(impressoraFormulario.getNrUltimoFormulario()));
		}
		
		List<DevedorDocServ> devedoresDoctoServico = devedorDocServService.findDevedoresByDoctoServico(notaFiscalServico.getIdDoctoServico());
		
		for (DevedorDocServ devedorDocServ : devedoresDoctoServico) {
			devedorDocServ.getCliente().setDtUltimoMovimento(JTDateTimeUtils.getDataAtual());
		}

		if (!ConstantesExpedicao.NOTA_FISCAL_SERVICO_SITUACAO_GERADA.equals( notaFiscalServico.getTpSituacaoNf().getValue())) {
		List<ImpostoServico> impostosServico = notaFiscalServico.getImpostoServicos();
		if (impostosServico != null) {
			for (ImpostoServico impostoServico : impostosServico) {
				impostoServico.setNotaFiscalServico(notaFiscalServico);
			}
		}

		/** Gera numero da Nota Fiscal */
		getEmitirDocumentoService().generateProximoNumero(notaFiscalServico);
		} else {
			notaFiscalServico.setTpSituacaoNf(new DomainValue(ConstantesExpedicao.NOTA_FISCAL_SERVICO_SITUACAO_EMITIDA));
		}
		notaFiscalServicoService.store(notaFiscalServico);
	}

	protected void storeEventos(NotaFiscalServico notaFiscalServico, Long idFilial) {
		/** Documento emitido */
		String tpDoctoServico = notaFiscalServico.getTpDocumentoServico().getValue();
		if(ConstantesExpedicao.NOTA_FISCAL_SERVICO.equals(tpDoctoServico)) {
			return;
		}

		String nrCtrc = ConhecimentoUtils.formatConhecimento(
				notaFiscalServico.getFilialByIdFilialOrigem().getSgFilial(),
				notaFiscalServico.getNrDoctoServico());

		Short cdEvento = ConstantesSim.EVENTO_DOCUMENTO_EMITIDO;
		getIncluirEventosService().generateEventoDocumento(
				cdEvento, notaFiscalServico.getIdDoctoServico(), idFilial, nrCtrc, null, null, null, tpDoctoServico);
	}
	
	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}
	
	public void setCalculoTributoService(CalculoTributoService calculoTributoService) {
		this.calculoTributoService = calculoTributoService;
	}
	
	public void setImpostoCalculadoService(
			ImpostoCalculadoService impostoCalculadoService) {
		this.impostoCalculadoService = impostoCalculadoService;
	}
	
	public void setNotaFiscalEletronicaService(
			NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}
	
	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}
	
}