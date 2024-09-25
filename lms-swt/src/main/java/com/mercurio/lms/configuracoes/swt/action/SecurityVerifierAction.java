package com.mercurio.lms.configuracoes.swt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.security.Resource;
import com.mercurio.adsm.framework.security.model.service.AuthorizationService;

/**
 * @spring.bean id="lms.configuracoes.swt.securityVerifierAction"
 */

public class SecurityVerifierAction extends CrudAction {
	private AuthorizationService authorizationService;
	
	public AuthorizationService getAuthorizationService() {
		return authorizationService;
	}

	public void setAuthorizationService(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	/**
	 * Params must contain:
	 *  -> List<String> itens where the String is the CdResource
	 */
	public Map verifyItens(Map params) {
		HashMap<String, Byte> retorno = new HashMap<String, Byte>();
		List<String> itens = (List<String>)params.get("itens");
		if (itens != null) 
			for (String item : itens) {
				retorno.put(item, getAuthorizationService().getPermissionByResource(new Resource(item)));
			}
		return retorno;
	}
}
