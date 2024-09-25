package com.mercurio.lms.services;

import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import com.mercurio.lms.rest.LmsRequestEventListener;

@Provider
public class LmsWSEventListener implements ApplicationEventListener {
	
	@Override
	public void onEvent(ApplicationEvent event) {
	}
	
	@Override
	public RequestEventListener onRequest(RequestEvent event) {		
		return new LmsRequestEventListener(event);
	}
}
