package com.mercurio.lms.rest;

import com.mercurio.adsm.framework.security.model.service.AuthenticationService;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	private static final String BEARER = "Bearer ";

	@Context private HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext requestContext)throws IOException {
		
		boolean authorized = false;
		String token = request.getHeader(ConstantesAmbiente.TOKEN);
		if (requestContext.getUriInfo().getMatchedResources().iterator().hasNext()) {
			
			Object resource = requestContext.getUriInfo().getMatchedResources().iterator().next();
			if (resource.getClass().isAnnotationPresent(Public.class) || "OPTIONS".equals(requestContext.getMethod())) {
				authorized = true;
			}else if(token != null){
				IntegracaoJwtService integracaoJwtService = new IntegracaoJwtService();
				authorized = integracaoJwtService.validarToken(request.getHeader("Authorization").replace(BEARER, ""));
				integracaoJwtService = null;
			} else {
				AuthenticationService authenticationService = (AuthenticationService) BeanUtils.getBean("adsm.security.authenticationService", request);
				if (authenticationService.isAuthenticated()) {
					authorized = true;
				}
			}
		}

		if (!authorized) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}

	}
