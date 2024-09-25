package com.mercurio.lms.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;

import com.mercurio.lms.annotation.InjectInJersey;

public class InjectInJerseyResolver implements InjectionResolver<InjectInJersey> {
 
	@Context private HttpServletRequest request;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {

    	return BeanUtils.getBean((Class<?>)injectee.getRequiredType(), request);
        
    }
 
    @Override
    public boolean isConstructorParameterIndicator() {
        return false;
    }
 
    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }
}
