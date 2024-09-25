package com.mercurio.lms.expedicao.report;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConfiguracoesFacadeImpl;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoPPEPadraoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.DpeService;
import com.mercurio.lms.expedicao.model.service.EmitirDocumentoService;
import com.mercurio.lms.expedicao.model.service.LiberacaoNotaNaturaService;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * @author Claiton Grings
 */
public abstract class EmitirCTOService {
	private EmitirDocumentoService emitirDocumentoService;
	private DevedorDocServService devedorDocServService;
	protected ConhecimentoService conhecimentoService;
	private DpeService dpeService;
	private ConfiguracoesFacade configuracoesFacade;
	private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
	private LiberacaoNotaNaturaService liberacaoNotaNaturaService;
	private DoctoServicoService doctoServicoService;
	private DoctoServicoPPEPadraoService doctoServicoPPEPadraoService;
	private ConfiguracoesFacadeImpl configuracoesFacadeImpl;
	private PessoaService pessoaService;
	private ClienteService clienteService;

	/**
	 * Gera eventos para diversos tipos de conhecimento
	 * 
	 * @param conhecimento
	 * @param idFilial
	 */
	protected void storeEventos(Conhecimento conhecimento, Long idFilial) {
		conhecimentoService.storeEventos(conhecimento, idFilial);
		
		}
	
	protected void updateDadosConsignatario(Conhecimento conhecimento) {
	    if(!BooleanUtils.isTrue(conhecimento.getBlFluxoSubcontratacao())){
	        String cnpjFilialFedex = (String) configuracoesFacadeImpl.getValorParametro(conhecimento.getFilialByIdFilialDestino().getIdFilial(), "CNPJ_FILIAL_FEDEX");
	        if (StringUtils.isNotBlank(cnpjFilialFedex) && conhecimento.getClienteByIdClienteConsignatario() == null) {
	            Pessoa pessoa = pessoaService.findByNrIdentificacao(cnpjFilialFedex);
	            if (pessoa != null) {
	                Cliente cliente = clienteService.findByIdComPessoa(pessoa.getIdPessoa());
	                conhecimento.setClienteByIdClienteConsignatario(cliente);
	            }
	        }
	    }
	}

	/**
	 * Atualiza os dados do documento
	 * 
	 * @param conhecimento
	 * @param impressoraFormulario
	 * @param tpSituacaoConhecimento
	 * @param tpSituacaoAtualizacao
	 * @param nrProximoCodigoBarras Caso passado como parâmetro, é atualizado em conhecimento.nrCodigoBarras
	 * @param semNumeroReservado 
	 */	
	protected void updateDados(Conhecimento conhecimento, ImpressoraFormulario impressoraFormulario, DomainValue tpSituacaoConhecimento, String tpSituacaoAtualizacao, Long nrProximoCodigoBarras, final Boolean semNumeroReservado) {
		String tpDoctoServico = conhecimento.getTpDoctoServico().getValue();
		Long idMunicipioOrigem = conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio();
		Long idMunicipioDestino = conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio();

		Long idPedidoColeta = null;
		if(conhecimento.getPedidoColeta() != null) {
			idPedidoColeta = conhecimento.getPedidoColeta().getIdPedidoColeta();
		}

		/*Obtem o devedor do documento*/
		Cliente clienteDevedor = conhecimento.getDevedorDocServs().get(0).getCliente();

		if (conhecimento.getDtPrevEntrega() == null) {
			Map dpeMap = dpeService.executeCalculoDPE(
					conhecimento.getClienteByIdClienteRemetente(),
					conhecimento.getClienteByIdClienteDestinatario(),
					clienteDevedor,
					conhecimento.getClienteByIdClienteConsignatario(),
					conhecimento.getClienteByIdClienteRedespacho(),
					idPedidoColeta,
					conhecimento.getServico().getIdServico(),
					idMunicipioOrigem,
					conhecimento.getFilialByIdFilialOrigem().getIdFilial(),
					conhecimento.getFilialByIdFilialDestino().getIdFilial(),
					idMunicipioDestino,
					conhecimento.getNrCepColeta(),
					conhecimento.getNrCepEntrega(),
					conhecimento.getDhEmissao() == null ? null : conhecimento.getDhEmissao());
		
			/*Calcula a data do prazo de entrega do conhecimento (PPE)*/
			conhecimento.setDtPrevEntrega((YearMonthDay)dpeMap.get("dtPrazoEntrega"));
			conhecimento.setNrDiasPrevEntrega(((Long)dpeMap.get("nrPrazo")).shortValue());

			// LMS-8779
			doctoServicoPPEPadraoService.updateDoctoServicoPPEPadrao(conhecimento.getIdDoctoServico(),(Long) dpeMap.get("nrDiasColeta"),(Long) dpeMap.get("nrDiasEntrega"),(Long) dpeMap.get("nrDiasTransferencia"));
			
		} else if (conhecimento.getNrDiasPrevEntrega() == null){
			Integer nrDias = doctoServicoService.findQtdeDiasUteisEntregaDocto(conhecimento.getIdDoctoServico(),
					conhecimento.getDtPrevEntrega());
			if (nrDias == null || IntegerUtils.ZERO.equals(nrDias)) {
				conhecimento.setNrDiasPrevEntrega(IntegerUtils.ONE.shortValue());
			} else {
				conhecimento.setNrDiasPrevEntrega(nrDias.shortValue());
		}
		}

		if(!ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(conhecimento.getTpDoctoServico().getValue()) && !ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equalsIgnoreCase(conhecimento.getTpDoctoServico().getValue())){
			conhecimento.setImpressora(impressoraFormulario.getImpressora());
			conhecimento.setNrAidf(impressoraFormulario.getControleFormulario().getNrAidf());
			conhecimento.setNrFormulario(impressoraFormulario.getNrUltimoFormulario());
			if(impressoraFormulario.getControleFormulario().getNrCodigoBarrasInicial() != null){
				conhecimento.setNrCodigoBarras(nrProximoCodigoBarras);
			}
			monitoramentoNotasFiscaisCCTService.executeVincularDocumentoComMonitoramento(conhecimento, "EM");
			
		} else if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(conhecimento.getTpDoctoServico().getValue())){
			// LMS-4715
			conhecimento.setNrCodigoBarras(conhecimento.getIdDoctoServico());
			monitoramentoNotasFiscaisCCTService.executeVincularDocumentoComMonitoramento(conhecimento, "EM");
		}

		conhecimento.setTpSituacaoConhecimento(tpSituacaoConhecimento);
		conhecimento.setTpSituacaoAtualizacao(tpSituacaoAtualizacao);

		if(ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(tpDoctoServico) || ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDoctoServico)) {

			/*Gera o numero o conhecimento quando o mesmo for de reentrega, complemento de frete ou complemento de icms*/ 
			if(semNumeroReservado && conhecimento.getNrConhecimento() < 0){
				/* Gerar próximo número */
				conhecimento.setGenerateUniqueNumber(true);
				emitirDocumentoService.generateProximoNumero(conhecimento);
			} 

				if(!"P".equals(conhecimento.getTpSituacaoConhecimento().getValue()) && conhecimento.getNrConhecimento().intValue() < 0) {
					Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
					Long nrConhecimento = configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_CONHECIMENTO", true);
				Integer dvConhecimento = ConhecimentoUtils.getDigitoVerificador(nrConhecimento);
				conhecimento.setDvConhecimento(dvConhecimento);
				}

			/*Cotacao utiliza numero do Conhecimento*/
			if(impressoraFormulario != null){
			conhecimento.setNrSeloFiscal(impressoraFormulario.getNrUltimoSeloFiscal());
			conhecimento.setDsSerieSeloFiscal(impressoraFormulario.getCdSerie());
			}

		}

		if(ConstantesExpedicao.CALCULO_COTACAO.equals(conhecimento.getTpCalculoPreco().getValue())) {
			List<Cotacao> cotacoes = conhecimento.getCotacoes();
			if(cotacoes != null) {
				String nrConhecimento = ConhecimentoUtils.formatConhecimento(
					conhecimento.getFilialByIdFilialOrigem().getSgFilial(),
					conhecimento.getNrConhecimento(),
						conhecimento.getDvConhecimento());
				
				for (Cotacao cotacao : cotacoes) {
					cotacao.setNrDocumentoCotacao(nrConhecimento);
				}
			}
		}
		
		/*Grava Conhecimento*/
		conhecimentoService.store(conhecimento);

		liberacaoNotaNaturaService.atualizaTerraNaturaEmitido(conhecimento.getIdDoctoServico());

		List<DevedorDocServ> devedores = conhecimento.getDevedorDocServs();
		for(Iterator<DevedorDocServ> it=devedores.iterator(); it.hasNext();) {

			DevedorDocServ devedorDocServ = (DevedorDocServ) it.next();
			devedorDocServ.getCliente().setDtUltimoMovimento(JTDateTimeUtils.getDataAtual());
			devedorDocServService.store(devedorDocServ);
		}
	}

	public void setLiberacaoNotaNaturaService(LiberacaoNotaNaturaService liberacaoNotaNaturaService) {
		this.liberacaoNotaNaturaService = liberacaoNotaNaturaService;
	}
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}
	public void setDpeService(DpeService dpeService) {
		this.dpeService = dpeService;
	}
	public EmitirDocumentoService getEmitirDocumentoService() {
		return emitirDocumentoService;
	}

	/**
	 * @param doctoServicoService
	 *            the doctoServicoService to set
	 */
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
}
	
	public void setMonitoramentoNotasFiscaisCCTService(MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
}

	public void setDoctoServicoPPEPadraoService(
			DoctoServicoPPEPadraoService doctoServicoPPEPadraoService) {
		this.doctoServicoPPEPadraoService = doctoServicoPPEPadraoService;
	}

	public void setConfiguracoesFacadeImpl(ConfiguracoesFacadeImpl configuracoesFacadeImpl) {
		this.configuracoesFacadeImpl = configuracoesFacadeImpl;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
}
