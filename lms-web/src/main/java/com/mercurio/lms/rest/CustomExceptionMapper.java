package com.mercurio.lms.rest;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.mercurio.adsm.framework.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.adsm.core.cache.RecursoMensagemCache;
import com.mercurio.adsm.core.model.executor.ServiceResult;
import org.springframework.dao.DataIntegrityViolationException;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger LOGGER = LogManager.getLogger(CustomExceptionMapper.class);

	@Override
	public Response toResponse(Exception e) {

		if(e instanceof BusinessException || e instanceof DataIntegrityViolationException) {
			LOGGER.warn(e.getMessage());
		} else {
			LOGGER.error(e.getMessage(), e);
		}

		Map<String, String> map = toMap(e);
		return Response.ok(map).type(MediaType.APPLICATION_JSON).build();
	}

	public Map<String, String> toMap(Exception e) {
		Map<String, String> map = new HashMap<String, String>();
		
		ADSMException exception = ServiceResult.convertThrowable(e);
	    
		String exceptionMessage = exception.getMessage();
        
	    String msgKey = null;
    	
	    Object[] msgArgs = new Object[0];
        
       	msgKey = exception.getMessageKey();
       	msgArgs = exception.getMessageArguments();

        if (StringUtils.isNotBlank(msgKey)) {
        	String mensagem = RecursoMensagemCache.getMessage(msgKey, LocaleContextHolder.getLocale().toString());
        	if (mensagem == null) {
	        	
	        	exceptionMessage = "Mensagem não cadastrada para o código: " + msgKey;
	        			
	        } else {
	        	exceptionMessage = MessageFormat.format(mensagem, msgArgs);
	        	
	        }
        	map.put("key", msgKey);
        }
		map.put("error", exceptionMessage);
		return map;
	}
	
}
