package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
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
public class ExecuteWorkflowNotaCreditoPadraoService extends ExecuteWorkflowNotaCreditoPadraoCommonService {
	
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
	 * Gera uma pendência de aprovação para a nota de crédito quando um valor de
	 * acrescimo ou desconto é informado, caso matriz apenas o aprova.
	 * 
	 * @param notacredito
	 */
	public void generatePendencia(NotaCredito notaCredito) {		
		if(notaCredito.getTpSituacaoAprovacao() != null || (notaCredito.getVlAcrescimoSugerido() == null && notaCredito.getVlDescontoSugerido() == null)){
			return;
		}
		
		if(!SessionUtils.isFilialSessaoMatriz()){
			doGeneratePendencia(notaCredito);
		} else {
			executeWorkflow(notaCredito.getIdNotaCredito(), ConstantesWorkflow.APROVADO);
		}
    }
	
	/**
	 * @param notaCredito
	 */
	private void doGeneratePendencia(NotaCredito notaCredito) {		
		Short nrEvento = null;
		String eventoNotaCredito = null;
		String tipoMensagem = null;
		
		if (notaCredito.getVlAcrescimoSugerido() != null) {
			tipoMensagem = "aprovarAcrescimoAtribuidoNotaCredito";
			nrEvento = ConstantesWorkflow.NR2508_ACRE_NOTCRE;
			
			eventoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_ACRESCIMO;
		} else {
			tipoMensagem = "aprovarDescontoAtribuidoNotaCredito";
			nrEvento = ConstantesWorkflow.NR2507_DESC_NOTCRE;
			
			eventoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_DESCONTO;
		}
						
		Pendencia pendencia = getWorkflowPendenciaService().generatePendencia(
				SessionUtils.getFilialSessao().getIdFilial(), 
				nrEvento, 
				notaCredito.getIdNotaCredito(), 
				getMensagemPendencia(tipoMensagem, SessionUtils.getFilialSessao().getSgFilial(), notaCredito.getNrNotaCredito()), 
				JTDateTimeUtils.getDataHoraAtual(), 
				SessionUtils.getUsuarioLogado(), 
				SessionUtils.getEmpresaSessao().getIdEmpresa());
		
		notaCredito.setPendencia(pendencia);
		notaCredito.setTpSituacaoAprovacao(new DomainValue("S"));
				
		getNotaCreditoPadraoService().store(notaCredito);
				
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
		String eventoNotaCredito = null;
		String eventoFluxoNotaCredito = null;
		
		NotaCredito notaCredito = getNotaCreditoPadraoService().findById(idNotaCredito);
		
		if (notaCredito.getVlAcrescimoSugerido() != null) {
			eventoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_ACRESCIMO;
		} else if (notaCredito.getVlDescontoSugerido() != null) {
			eventoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_DESCONTO;
		}
				
		if (tpSituacao.equalsIgnoreCase(ConstantesWorkflow.APROVADO)) {
			setNotaCreditoAprovada(notaCredito);			
			eventoFluxoNotaCredito =  ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_APROVADO;
		} else if (tpSituacao.equalsIgnoreCase(ConstantesWorkflow.REPROVADO)) {
			setNotaCreditoReprovada(notaCredito);			
			eventoFluxoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_REPROVADO;
		}
		
		BigDecimal vlTotal = notaCredito.getVlTotal();
		
		if (notaCredito.getVlAcrescimoSugerido() != null) {
			vlTotal = vlTotal.add(notaCredito.getVlAcrescimoSugerido());
		} else if (notaCredito.getVlDescontoSugerido() != null) {
			vlTotal = vlTotal.subtract(notaCredito.getVlDescontoSugerido());
		}

		notaCredito.setVlTotal(vlTotal);
		
		getNotaCreditoPadraoService().store(notaCredito);
		
		registrarEventoNotaCredito(notaCredito, eventoNotaCredito, eventoFluxoNotaCredito);
	}
	
	/**
	 * @param notaCredito
	 */
	private void setNotaCreditoAprovada(NotaCredito notaCredito) {
		notaCredito.setTpSituacaoAprovacao(new DomainValue(ConstantesWorkflow.APROVADO));
		notaCredito.setVlAcrescimo(notaCredito.getVlAcrescimoSugerido());
		notaCredito.setVlDesconto(notaCredito.getVlDescontoSugerido());
		notaCredito.setPendencia(null);
	}

	/**
	 * @param notaCredito
	 */
	private void setNotaCreditoReprovada(NotaCredito notaCredito) {
		notaCredito.setTpSituacaoAprovacao(null);
		notaCredito.setVlAcrescimoSugerido(null);
		notaCredito.setVlDescontoSugerido(null);
		notaCredito.setPendencia(null);		
	}
}