package com.mercurio.lms.configuracoes.model.service.event;

import com.mercurio.adsm.framework.security.model.service.event.AuthenticationEvent;
import com.mercurio.adsm.framework.security.model.service.event.AuthenticationListener;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.service.SessionContentLoaderService;

public class SessionContentLoaderAuthenticationListener implements AuthenticationListener {
	
	private SessionContentLoaderService sessionContentLoaderService;

	public void afterLogin(AuthenticationEvent ev) {

		sessionContentLoaderService.execute();
	}

	public void beforeLogout() {

		SessionContext.removeAll();
	}
	
	public void setSessionContentLoaderService(SessionContentLoaderService sessionContentLoaderService) {

		this.sessionContentLoaderService = sessionContentLoaderService;
	}
}