package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.utils.ConstantesEventosNotaCredito;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 */
public class ExecuteWorkflowCalculoNotaCreditoPadraoService extends ExecuteWorkflowNotaCreditoPadraoCommonService {

	private static final String PARAMETRO_VALOR_MAXIMO_PERMITIDO = "VALOR_MAXIMO_NOTA_CR";
	
	/**
	 * Executado pela tela de manter ações
	 * 
	 * @param ids
	 * @param situacoes
	 * 
	 * @return String
	 */
	public String executeWorkflow(List<Long> ids, List<String> situacoes) {
		for (int i = 0; i < ids.size(); i++) {
			executeWorkflow((Long) ids.get(i), (String) situacoes.get(i));
		}

		return null;
	}

	/**
	 * Gera uma pendência de aprovação para a nota de crédito o valor total da
	 * nota é maior que o informado no parâmetro.
	 * 
	 * @param notacredito
	 */
	public boolean isGeneratePendencia(NotaCredito notaCredito) {
		
		/*
		 * Verifica se nota de credito ja foi emitida.
		 */
		if(notaCredito.getDhEmissao() != null){
			return false;
		}
		
		/*
		 * Verifica se existe um workflow em aprovação para valor de desconto/acréscimo.
		 */
		if(notaCredito.getTpSituacaoAprovacao() != null && "S".equals(notaCredito.getTpSituacaoAprovacao().getValue())){
			return true;
		}
		
		/*
		 * Verifica se existe uma pendencia já aprovada para emissão da nota de crédito.
		 */
		if(notaCredito.getPendencia() != null && ConstantesWorkflow.APROVADO.equals(notaCredito.getPendencia().getTpSituacaoPendencia().getValue())){
			return false;
		}
		
		/*
		 * Matriz não gera workflow, assim como se o valor estiver dentro do permitido.
		 */
		if(SessionUtils.isFilialSessaoMatriz() || notaCredito.getVlTotal().compareTo(getValorMaximoPermitido()) < 0){
			return false;
		}
		
		doGeneratePendencia(notaCredito);
		
		return true;
    }
	
	/**
	 * @param notaCredito
	 */
	private void doGeneratePendencia(NotaCredito notaCredito) {						
		Pendencia pendencia = getWorkflowPendenciaService().generatePendencia(
				SessionUtils.getFilialSessao().getIdFilial(), 
				ConstantesWorkflow.NR2509_NC_VL_MAIOR_PARAMETRO,
				notaCredito.getIdNotaCredito(), 
				getMensagemPendencia("aprovarNotaCreditoValorSuperiorMax", notaCredito.getFilial().getSgFilial(), notaCredito.getNrNotaCredito()), 
				JTDateTimeUtils.getDataHoraAtual(), 
				SessionUtils.getUsuarioLogado(), 
				SessionUtils.getEmpresaSessao().getIdEmpresa());
		
		notaCredito.setPendencia(pendencia);		
				
		getNotaCreditoPadraoService().store(notaCredito);
		
		String eventoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_VALOR_EXCEDENTE;
		
		registrarEventoNotaCredito(notaCredito, eventoNotaCredito, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_SOLICITADO);
	}
	
	/**
	 * Ação executada a partir da tela ações do workflow. 
	 * 
	 * @param idNotaCredito
	 * @param tpSituacao
	 * 
	 */
	private void executeWorkflow(Long idNotaCredito, String tpSituacao) {
		String eventoFluxoNotaCredito = null;		
		
		if (tpSituacao.equalsIgnoreCase(ConstantesWorkflow.APROVADO)) {
			eventoFluxoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_APROVADO;
		} else if (tpSituacao.equalsIgnoreCase(ConstantesWorkflow.REPROVADO)) {
			eventoFluxoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_REPROVADO;
		}
		
		registrarEventoNotaCredito(getNotaCreditoPadraoService().findById(idNotaCredito), ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_VALOR_EXCEDENTE, eventoFluxoNotaCredito);
	}	
	
	/**
	 * Retorna o valor máximo permitido para emissão da nota de crédito.
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal getValorMaximoPermitido() {
		BigDecimal valorParametro = (BigDecimal) getConfiguracoesFacade().getValorParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_VALOR_MAXIMO_PERMITIDO);

		if (valorParametro == null || BigDecimal.ZERO.compareTo(valorParametro) >= 0) {
			valorParametro = BigDecimal.ZERO;
		}

		return valorParametro;
	}
}