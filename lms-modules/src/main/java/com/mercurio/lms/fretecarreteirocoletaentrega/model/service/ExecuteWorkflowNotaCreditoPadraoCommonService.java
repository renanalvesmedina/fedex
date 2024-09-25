package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 */
public class ExecuteWorkflowNotaCreditoPadraoCommonService {

	private EventoNotaCreditoService eventoNotaCreditoService;
	private ConfiguracoesFacade configuracoesFacade;
		
	private NotaCreditoPadraoService notaCreditoPadraoService;
	
	private WorkflowPendenciaService workflowPendenciaService;
	private ParametroGeralService parametroGeralService;	
	
	/**
	 * Cria mensagem para pendência de acordo com o tipo de valor.
	 * 
	 * @param tipoMensagem
	 * @param nrNotaCredito
	 * 
	 * @return String
	 */
	protected String getMensagemPendencia(String tipoMensagem, String sgFilial, Long nrNotaCredito){
		Object[] params = new String[3];
		params[0] = configuracoesFacade.getMensagem(tipoMensagem);
		params[1] = sgFilial;
		params[2] = new DecimalFormat("0000000000").format(nrNotaCredito);
		
		return String.format("%s. %s %s", params);
	}		

	/**
	 * Registra um evento da nota de crédito.
	 * 
	 * @param notaCredito
	 * @param eventoNotaCredito
	 */
	protected void registrarEventoNotaCredito(NotaCredito notaCredito, String eventoNotaCredito, String eventoFluxoNotaCredito) {
		if(StringUtils.isBlank(eventoNotaCredito)){
			return;
		}
		
		eventoNotaCreditoService.storeEventoNotaCredito(notaCredito, eventoNotaCredito, eventoFluxoNotaCredito);
	}	

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setEventoNotaCreditoService(EventoNotaCreditoService eventoNotaCreditoService) {
		this.eventoNotaCreditoService = eventoNotaCreditoService;
	}

	public NotaCreditoPadraoService getNotaCreditoPadraoService() {
		return notaCreditoPadraoService;
	}

	public void setNotaCreditoPadraoService(
			NotaCreditoPadraoService notaCreditoPadraoService) {
		this.notaCreditoPadraoService = notaCreditoPadraoService;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public EventoNotaCreditoService getEventoNotaCreditoService() {
		return eventoNotaCreditoService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
}