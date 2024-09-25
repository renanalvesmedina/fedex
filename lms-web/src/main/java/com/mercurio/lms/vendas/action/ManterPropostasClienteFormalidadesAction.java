/**
 * 
 */
package com.mercurio.lms.vendas.action;

import java.math.BigDecimal;
import java.text.MessageFormat;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DiaFaturamento;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.HistoricoEfetivacao;
import com.mercurio.lms.vendas.model.PrazoVencimento;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.service.DiaFaturamentoService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.model.service.HistoricoEfetivacaoService;
import com.mercurio.lms.vendas.model.service.MotivoReprovacaoService;
import com.mercurio.lms.vendas.model.service.PrazoVencimentoService;
import com.mercurio.lms.vendas.model.service.SimulacaoAnexoService;
import com.mercurio.lms.vendas.model.service.SimulacaoService;
import com.mercurio.lms.vendas.util.SimulacaoUtils;
import com.mercurio.lms.workflow.model.service.GerarEmailMensagemAvisoService;

/**
 * @author Luis Carlos Poletto
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.manterPropostasClienteFormalidadesAction"
 */
public class ManterPropostasClienteFormalidadesAction extends CrudAction {

	private static final String ID_SIMULACAO = "idSimulacao";

	private DiaFaturamentoService diaFaturamentoService;
	private PrazoVencimentoService prazoVencimentoService;
	private DivisaoClienteService divisaoClienteService;

	private HistoricoEfetivacaoService historicoEfetivacaoService;
	private ConfiguracoesFacade configuracoesFacade;
	private UsuarioLMSService usuarioLMSService;
	private SimulacaoService simulacaoService;
	private DomainValueService domainValueService;
	// LMS-6172
	private SimulacaoAnexoService simulacaoAnexoService;
	
	private GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService;
	private MotivoReprovacaoService motivoReprovacaoService;

	
	public TypedFlatMap findDadosSessao() {
		Filial filial = SessionUtils.getFilialSessao();
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		TypedFlatMap result = new TypedFlatMap();

		if(simulacao != null){
				
			/** Regra para buscar Periodicidade cadastrada no Cliente */
			DivisaoCliente divisaoCliente = simulacao.getDivisaoCliente();
			Servico servico = simulacao.getServico();
			if (simulacao.getTpPeriodicidadeFaturamento() == null) {
				// pré condições para obter o dia de faturamento
				if (divisaoCliente == null) {
					throw new IllegalStateException(configuracoesFacade.getMensagem("manterPropostaClienteDivisaoClienteNula"));
				}
				if (servico == null) {
					throw new IllegalStateException(configuracoesFacade.getMensagem("manterPropostaClienteServicoNulo"));
				}
				DiaFaturamento diaFaturamento = diaFaturamentoService.findDiaFaturamento(divisaoCliente.getIdDivisaoCliente(), servico.getTpModal().getValue());
				if (diaFaturamento != null) {
					simulacao.setTpPeriodicidadeFaturamento(diaFaturamento.getTpPeriodicidade());
				} else {
					// TODO ajustar para ser um erro de negócio BussinesException, é uma falha cadastral e gerava um NullPointerException.
					if (divisaoCliente.getIdDivisaoCliente() != null) {
						divisaoCliente = this.divisaoClienteService.findById(divisaoCliente.getIdDivisaoCliente());
					}
					Object[] arguments = new Object[] {
							divisaoCliente.getDsDivisaoCliente(),
							servico.getTpModal().getDescriptionAsString()
					};
					throw new IllegalStateException(MessageFormat.format(
							configuracoesFacade.getMensagem("manterPropostaClienteDiaFaturamentoNaoCadastrado"), arguments));
				}
			}
			result.put("tpPeriodicidadeFaturamento", simulacao.getTpPeriodicidadeFaturamento().getValue());

			/** Regra para buscar PrazoPagamento cadastrada no Cliente */
			if (simulacao.getNrDiasPrazoPagamento() == null) {
				PrazoVencimento prazoVencimento = prazoVencimentoService.findPrazoVencimento(divisaoCliente.getIdDivisaoCliente(), servico.getTpModal().getValue());
				if (prazoVencimento != null) {
					simulacao.setNrDiasPrazoPagamento(prazoVencimento.getNrPrazoPagamento());
				}
			}
			result.put("nrDiasPrazoPagamento", simulacao.getNrDiasPrazoPagamento());

			result.put("dtValidadeProposta", simulacao.getDtValidadeProposta());
			result.put("dtAceiteCliente", simulacao.getDtAceiteCliente());
			result.put("dtTabelaVigenciaInicial", simulacao.getDtTabelaVigenciaInicial());

			if (simulacao.getTpSituacaoAprovacao() != null) {
				result.put("tpSituacaoAprovacao.description", simulacao.getTpSituacaoAprovacao().getDescription());
				result.put("tpSituacaoAprovacao.value", simulacao.getTpSituacaoAprovacao().getValue());
			}

			if (simulacao.getUsuarioByIdUsuarioAprovou() != null) {
				result.put("usuarioByIdUsuarioAprovou.nrMatricula", simulacao.getUsuarioByIdUsuarioAprovou().getNrMatricula());
				result.put("usuarioByIdUsuarioAprovou.nmUsuario", simulacao.getUsuarioByIdUsuarioAprovou().getNmUsuario());
			}
			result.put("dtAprovacao", simulacao.getDtAprovacao());
			result.put("dtEmissaoTabela", simulacao.getDtEmissaoTabela());

			if (simulacao.getFilial() != null) {
				result.put("filial.idFilial", simulacao.getFilial().getIdFilial());
			}

			result.put("filialSessao.idFilial", filial.getIdFilial());
				
			result.put("dtEfetivacao", simulacao.getDtEfetivacao());
			result.put("simulacao.idSimulacao", simulacao.getIdSimulacao());

			if (simulacao.getPendenciaAprovacao() != null) {
				Long idPendencia = simulacao.getPendenciaAprovacao().getIdPendencia();
				result.put("pendencia.idPendencia", idPendencia);
			}
			result.put("obProposta", simulacao.getObProposta());
		
			
			//Inicio LMS-6190
			if(simulacao.getUsuarioByIdUsuarioEfetivou() != null){
				result.put("usuarioByIdUsuarioEfetivou.nrMatricula", simulacao.getUsuarioByIdUsuarioEfetivou().getNrMatricula());
				result.put("usuarioByIdUsuarioEfetivou.nmUsuario", simulacao.getUsuarioByIdUsuarioEfetivou().getNmUsuario());
			}
			
			result.put("isFilialMatriz",isFilialMatriz());
			
			result.put("blEfetivada",isEfetivada(simulacao));
			
			HistoricoEfetivacao historicoEfetivacao = historicoEfetivacaoService.findLastHistoricoByIdSimulacao(simulacao.getIdSimulacao());
			
			if(historicoEfetivacao != null){
				result.put("dhSolicitacao", historicoEfetivacao.getDhSolicitacao());
				result.put("dhReprovacao", historicoEfetivacao.getDhReprovacao());
				result.put("dsMotivo", historicoEfetivacao.getDsMotivo());
			}
			//Fim LMS-6190
			
			SimulacaoUtils.setSimulacaoInSession(simulacao);
		}
		return result;
	}

	private String isEfetivada(Simulacao simulacao) {
		if (simulacao.getTpSituacaoAprovacao() != null && "F".equals(simulacao.getTpSituacaoAprovacao().getValue())) {
			return "S";
		} else {
			return "N";
		}
	}

	private String isFilialMatriz() {
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		BigDecimal filialMtz = (BigDecimal)configuracoesFacade.getValorParametro("ID_EMPRESA_MERCURIO");
		
		if((filialMtz != null && filialUsuarioLogado != null && filialUsuarioLogado.getIdFilial() != null) 
				&& filialMtz.toString().equals(filialUsuarioLogado.getIdFilial().toString())){
			return "S";
		} else {
			return "N";
		}
	}

	public TypedFlatMap aprovacaoProposta() {
		return getSimulacaoService().executeAprovacaoProposta();
	}

	public void executeReprovarEfetivacao(TypedFlatMap parameters) {
		getSimulacaoService().executeReprovarEfetivacao(parameters);
	}

	/**
	 * LMS-6190
	 * Atualiza simulação para status 'M' e insere registro de histórico de efetivação
	 * @param map
	 * @return
	 */
	public TypedFlatMap solicitarEfetivacaoProposta(TypedFlatMap map){
		return simulacaoService.executeSolicitarEfetivacaoProposta(map);
	}
	
	/**
	 * @return Returns the simulacaoService.
	 */
	public SimulacaoService getSimulacaoService() {
		return simulacaoService;
	}

	/**
	 * @param simulacaoService The simulacaoService to set.
	 */
	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}
	
	public void storeEfetivacaoProposta(TypedFlatMap map){
		Long idSimulacao = map.getLong(ID_SIMULACAO);
		Long idDivisaoCliente = map.getLong("idDivisaoCliente");
		YearMonthDay dtInicioVigencia = map.getYearMonthDay("dtInicioVigencia");
		// LMS-6168
		Boolean blConfirmaEfetivarProposta = map.getBoolean("blConfirmaEfetivarProposta");

		simulacaoService.storeEfetivacaoProposta(idSimulacao, idDivisaoCliente, dtInicioVigencia,
				blConfirmaEfetivarProposta);
		
		Simulacao atualizada = simulacaoService.findById(idSimulacao);
		SimulacaoUtils.setSimulacaoInSession(atualizada);
	}

	/**
	 * @return Returns the domainValueService.
	 */
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	/**
	 * @param domainValueService The domainValueService to set.
	 */
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setDiaFaturamentoService(DiaFaturamentoService diaFaturamentoService) {
		this.diaFaturamentoService = diaFaturamentoService;
	}

	public void setPrazoVencimentoService(
			PrazoVencimentoService prazoVencimentoService) {
		this.prazoVencimentoService = prazoVencimentoService;
	}

	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public HistoricoEfetivacaoService getHistoricoEfetivacaoService() {
		return historicoEfetivacaoService;
}

	public void setHistoricoEfetivacaoService(HistoricoEfetivacaoService historicoEfetivacaoService) {
		this.historicoEfetivacaoService = historicoEfetivacaoService;
	}
	
	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public GerarEmailMensagemAvisoService getGerarEmailMensagemAvisoService() {
		return gerarEmailMensagemAvisoService;
	}

	public void setGerarEmailMensagemAvisoService(GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService) {
		this.gerarEmailMensagemAvisoService = gerarEmailMensagemAvisoService;
	}

	public SimulacaoAnexoService getSimulacaoAnexoService() {
		return simulacaoAnexoService;
	}

	public void setSimulacaoAnexoService(SimulacaoAnexoService simulacaoAnexoService) {
		this.simulacaoAnexoService = simulacaoAnexoService;
	}

	public MotivoReprovacaoService getMotivoReprovacaoService() {
		return motivoReprovacaoService;
	}

	public void setMotivoReprovacaoService(MotivoReprovacaoService motivoReprovacaoService) {
		this.motivoReprovacaoService = motivoReprovacaoService;
	}


}
