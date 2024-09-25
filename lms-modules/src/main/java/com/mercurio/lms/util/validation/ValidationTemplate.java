package com.mercurio.lms.util.validation;

import com.mercurio.adsm.framework.BusinessException;

public abstract class ValidationTemplate<T> implements ValidationInterface<T> {

	private final String exceptionMessage;
	private Object[] messageParameters;
    protected abstract boolean theValidation(T model);
    private ValidationInterface<T> nextValidation;

    public ValidationTemplate(String validationMessage) {
    	this.exceptionMessage = validationMessage;
    }
    public ValidationTemplate(String validationMessage, Object[] parameters) {
    	this.exceptionMessage = validationMessage;
    	messageParameters = parameters;
    }
    
    @Override
    public void doValidation(T model) {

        if (theValidation(model)) {
        	if(nextValidation != null){
        		nextValidation.doValidation(model);
        	}
            return;
        }

        if(messageParameters != null){
        	throw new BusinessException(exceptionMessage, messageParameters);
        }
        throw new BusinessException(exceptionMessage);
    }

    @Override
    public ValidationInterface<T> setNextValidation(ValidationInterface<T> nextValidation) {
        this.nextValidation = nextValidation;
        return nextValidation;
    }
	public void setMessageParameters(Object[] messageParameters) {
		this.messageParameters = messageParameters;
	}
    

}
