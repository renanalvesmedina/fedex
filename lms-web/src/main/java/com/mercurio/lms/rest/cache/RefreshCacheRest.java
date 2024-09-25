package com.mercurio.lms.rest.cache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.facade.cache.RefreshCacheFacade;
import javax.ws.rs.core.Response;

@Public
@Path("/refreshCacheRest")
public class RefreshCacheRest {
	
	@InjectInJersey RefreshCacheFacade refreshCacheFacade;

	@GET
	@Path("refreshParametroGeral")
	public Response refreshparametroGeral(){
		refreshCacheFacade.refreshParametroGeral();
		return Response.ok("OK").build();
	}

	@GET
	@Path("refreshImagem")
	public Response refreshImagem(){
		refreshCacheFacade.refreshImagem();
		return Response.ok("OK").build();
	}
	
	@GET
	@Path("refreshDomainValue")
	public Response refreshDomainValue(){
		refreshCacheFacade.refreshDomainValue();
		return Response.ok("OK").build();
	}
	
	@GET
	@Path("refreshRecursoMensagem")
	public Response refreshRecursoMensagem(){
		refreshCacheFacade.refreshRecursoMensagem();
		return Response.ok("OK").build();
	}
	
	@GET
	@Path("refreshMenu")
	public Response refreshMenu(){
		refreshCacheFacade.refreshMenu();
		return Response.ok("OK").build();
	}
	
	public void setRefreshCacheFacade(RefreshCacheFacade refreshCacheFacade) {
		this.refreshCacheFacade = refreshCacheFacade;
	}
}
