package com.mercurio.lms.configuracoes.model.dao;

public class OneObjectExpectedException extends RuntimeException {

	public OneObjectExpectedException() {
		super();
	}

	public OneObjectExpectedException(String message) {
		super(message);
	}

	public OneObjectExpectedException(String message, Throwable cause) {
		super(message, cause);
	}

	public OneObjectExpectedException(Throwable cause) {
		super(cause);
	}

}
