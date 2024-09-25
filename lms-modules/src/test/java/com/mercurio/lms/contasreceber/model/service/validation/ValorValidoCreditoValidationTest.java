package com.mercurio.lms.contasreceber.model.service.validation;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationInterfaceTest;

public class ValorValidoCreditoValidationTest {

	@Test
	public void doValidationFilialShouldNotReturnExceptionWhenObjectIsValid() {

		ValorValidoCreditoValidation validation = new ValorValidoCreditoValidation();

		CreditoBancario creditoBancario = new CreditoBancario();
		creditoBancario.setVlCredito(new BigDecimal(1));

		validation.doValidation(creditoBancario);
	}
	@Test
	public void doValidationShouldThrowExceptionWhenVlCreditoIsNotValid() {

		ValorValidoCreditoValidation validation = new ValorValidoCreditoValidation();

		CreditoBancario creditoBancario = new CreditoBancario();
		creditoBancario.setVlCredito(new BigDecimal(-1));

		Assert.assertTrue(ValidationInterfaceTest.didThrowException(validation, creditoBancario, "LMS-36340"));
	}
}
