package com.mercurio.lms.contasreceber.model.service.validation;

import org.testng.annotations.Test;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationInterfaceTest;

public class BancoCreditoValidationServiceTest {
	@Test
	public void doValidationShouldNotReturnExceptionWhenInputIsValid() {
		
		BancoCreditoValidationService validation = new BancoCreditoValidationService();
		
		CreditoBancario creditoBancario = new CreditoBancario();
		creditoBancario.setIdBanco(1L);
		
		validation.doValidation(creditoBancario);
	}
	
	@Test
	public void doValidationShouldReturnExceptionWhenBancoIsNull() {
		
		BancoCreditoValidationService validation = new BancoCreditoValidationService();
		
		CreditoBancario creditoBancario = new CreditoBancario();
		creditoBancario.setIdBanco(null);
		
		ValidationInterfaceTest.didThrowException(validation,creditoBancario,"LMS-36338");
	}
}