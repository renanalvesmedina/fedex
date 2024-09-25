/**
 * 
 */
package com.mercurio.lms.configuracoes.swt.action;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.ShutdownService;

/**
 * @author Anibal de Deus
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.swt.manterShutdownAction"
 */

public class ManterShutdownAction extends CrudAction {
	private ShutdownService shutdownService; 
	
	public Map findScheduledSystemStop(Map data) {
		Map map = new HashMap();
		map.put("horario", getShutdownService().getScheduledSystemStop());
		map.put("mensagem", getShutdownService().getScheduledMessage()); 
		return map;
	}

	public void storeScheduledSystemStop(Map data) {
		DateTime time = (DateTime)data.get("horario");
		String mensagem = (String)data.get("mensagem"); 
		getShutdownService().setScheduledSystemStop(time, mensagem);
	}
	
		
	public void setShutdownService(ShutdownService shutdownService) {
		this.shutdownService = shutdownService;
	}

	public ShutdownService getShutdownService() {
		return shutdownService;
	}
}
