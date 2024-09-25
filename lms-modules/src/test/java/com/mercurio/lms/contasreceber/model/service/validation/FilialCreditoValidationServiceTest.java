package com.mercurio.lms.contasreceber.model.service.validation;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationInterfaceTest;

public class FilialCreditoValidationServiceTest {

	@Test
	public void doValidationFilialShouldNotReturnExceptionWhenObjectIsValid() {
		
		FilialCreditoValidationService validation = new FilialCreditoValidationService();
		
		CreditoBancario creditoBancario = new CreditoBancario();
		creditoBancario.setIdFilial(1L);
		
		validation.doValidation(creditoBancario);
	}
	@Test
	public void doValidationShouldThrowExceptionWhenFilialIsNull() {
		
		FilialCreditoValidationService validation = new FilialCreditoValidationService();
		
		CreditoBancario creditoBancario = new CreditoBancario();
		creditoBancario.setIdFilial(null);
		
		Assert.assertTrue(ValidationInterfaceTest.didThrowException(validation, creditoBancario, "LMS-36275"));
	}
}
