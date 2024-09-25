package com.mercurio.lms.util.validation;


public interface ValidationInterface<T> {

    void doValidation(T model);

    ValidationInterface<T> setNextValidation(ValidationInterface<T> validation);

	
}
