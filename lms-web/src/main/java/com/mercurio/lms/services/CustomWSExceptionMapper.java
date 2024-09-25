package com.mercurio.lms.services;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class CustomWSExceptionMapper extends Exception implements ExceptionMapper<Exception> {

	private static final Logger LOGGER = LogManager.getLogger(CustomWSExceptionMapper.class);

	public CustomWSExceptionMapper() {
		super("Erro não tratado");
	}

	@Override
	public Response toResponse(Exception exception) {
		LOGGER.error(exception.getMessage(), exception);
		
		return Response.status(getStatusCode(exception))
			.entity(getEntity(exception)).build();
	}

	private int getStatusCode(Throwable exception) {
	    if (exception instanceof WebApplicationException) {
	        return ((WebApplicationException)exception).getResponse().getStatus();
	    }
	
	    return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
	}

	private Map<String, String> getEntity(Throwable exception) {
	  //  StringWriter errorMsg = new StringWriter();
	    //exception.printStackTrace(new PrintWriter(errorMsg));
	    Map<String, String> retorno = new HashMap<>();
	    retorno.put("erro", exception.getMessage()) ;
	    return retorno;
	}

}
