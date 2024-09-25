package com.mercurio.lms.rest;

import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

@Provider
public class LmsEventListener implements ApplicationEventListener {
	
	@Override
	public void onEvent(ApplicationEvent event) {
	}
	
	@Override
	public RequestEventListener onRequest(RequestEvent event) {		
		return new LmsRequestEventListener(event);
	}
}
