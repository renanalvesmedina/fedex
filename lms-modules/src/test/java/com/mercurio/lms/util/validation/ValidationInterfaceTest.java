package com.mercurio.lms.util.validation;

import com.mercurio.adsm.framework.BusinessException;

public class ValidationInterfaceTest {
  
	public static <T> Boolean didThrowException(ValidationInterface<T> validation, T model, String errorCode){
		try{
			validation.doValidation(model);

		}catch(BusinessException e){
			return errorCode.equals(e.getMessageKey());
		}
		return false;
	}
}
