package com.mercurio.lms.mww.action;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;

/**
 * Classe utilizada para retornar as mensagens do Banco para o MWW/GM
 * @author rtavares
 * @spring.bean id="lms.mww.messageAction"
 */
public class MessageAction {
	private RecursoMensagemService recursoMensagemService;
	
	/**
	 * Método que recebe um map contendo a chave da mensagem a ser carregado do banco
	 * e os parametros a serem substituídos separados por ';'
	 * @param parametros
	 * @return Mensagem com a chave concatenada
	 */
	public Map getMensagem(Map parametros) {		
		String messageKey = (String) parametros.get("messageKey"); 
		String argumentos = (String) parametros.get("arguments");
		String arguments[] = null;
		if(argumentos != null) {
			arguments = argumentos.split(";");
		}
		String mensagem = recursoMensagemService.findByChave(messageKey, arguments);
		Map map = new HashMap();
		map.put("message", messageKey + " - " + mensagem);
		return map;
	}
	
	/**
	 * Método que recebe um map contendo a chave da mensagem a ser carregado do banco
	 * e os parametros a serem substituídos separados por ';'
	 * @param parametros
	 * @return Mensagem sem a chave concatenada
	 */
	public Map getMensagemSemChave(Map parametros) {		
		String messageKey = (String) parametros.get("messageKey"); 
		String argumentos = (String) parametros.get("arguments");
		String arguments[] = null;
		if(argumentos != null) {
			arguments = argumentos.split(";");
		}
		String mensagem = recursoMensagemService.findByChave(messageKey, arguments);
		Map map = new HashMap();
		map.put("message", mensagem);
		return map;
	}

	public RecursoMensagemService getRecursoMensagemService() {
		return recursoMensagemService;
	}

	public void setRecursoMensagemService(
			RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}

}
