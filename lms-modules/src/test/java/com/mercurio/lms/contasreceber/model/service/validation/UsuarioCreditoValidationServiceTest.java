package com.mercurio.lms.contasreceber.model.service.validation;

import org.testng.annotations.Test;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationInterfaceTest;

public class UsuarioCreditoValidationServiceTest {
	

	protected static final long ID_USUARIO = 1L;
	
	@Test
	public void doValidationShouldNotReturnExceptionWhenInputIsValid() {
		
		UsuarioCreditoValidationService validation = new UsuarioCreditoValidationService();
		
		CreditoBancario creditoBancario = new CreditoBancario();
		creditoBancario.setIdUsuario(1L);
		
		validation.doValidation(creditoBancario);
	}
	
	@Test
	public void doValidationShouldReturnExceptionWhenUsuarioisNull() {
		
		UsuarioCreditoValidationService validation = new UsuarioCreditoValidationService();
		
		CreditoBancario creditoBancario = new CreditoBancario();
		creditoBancario.setIdUsuario(null);
		
		ValidationInterfaceTest.didThrowException(validation,creditoBancario,"LMS-36341");
	}

}
