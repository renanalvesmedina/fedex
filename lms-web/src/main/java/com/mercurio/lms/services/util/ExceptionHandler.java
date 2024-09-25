package com.mercurio.lms.services.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;

public class ExceptionHandler {
	
	public static String getErrorMessage(ConfiguracoesFacade configuracoesFacade, Exception exception) {
		
		if (exception instanceof ADSMException){
			ADSMException e = (ADSMException) exception;
			
			if (StringUtils.isNotBlank(e.getMessage())) {
				return findErrorMessage(configuracoesFacade, e.getMessage(), e.getMessageArguments());
			} else {
				return e.getClass().toString();
			}
		}
			
		return exception.getMessage();
	}
			
	private static String findErrorMessage(
			ConfiguracoesFacade configuracoesFacade, String exceptionMessage, Object[] argumentos) {
		
		final String regex = ".*\\=(LMSA{0,1}\\-\\d+).*";
		Matcher matcher = Pattern.compile(regex).matcher(exceptionMessage);
		
        if (matcher.matches()) {
        	return configuracoesFacade.getMensagem(matcher.group(1), argumentos);
        }
        
        return exceptionMessage;
	}
}
