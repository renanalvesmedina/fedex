package com.mercurio.lms.contasreceber.model.service.validation;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationInterfaceTest;

public class CreditoInputValidationTest {

  @Test
  public void doValidationShouldReturnExceptionWhenInputIsNull() {
	  CreditoInputValidation validation = new CreditoInputValidation();
	  CreditoBancario creditoBancario = null;
	  
	  Assert.assertTrue(ValidationInterfaceTest.didThrowException(validation, creditoBancario, "LMS-36337"));
  }
  @Test
  public void doValidationShouldReturnExceptionWhenSgFilialIsNull() {
	  CreditoInputValidation validation = new CreditoInputValidation();
	  CreditoBancario creditoBancario = new CreditoBancario();
	  
	  Assert.assertTrue(ValidationInterfaceTest.didThrowException(validation, creditoBancario, "LMS-36337"));
  }
  @Test
  public void doValidationShouldReturnExceptionWhenSgFilialIsEmpty() {
	  CreditoInputValidation validation = new CreditoInputValidation();
	  CreditoBancario creditoBancario =  getCreditoExemplo();
	  creditoBancario.setSgFilial("");
	  
	  Assert.assertTrue(ValidationInterfaceTest.didThrowException(validation, creditoBancario, "LMS-36337"));
  }

  @Test
  public void doValidationShouldNotReturnExceptionWhenAtributesAreNotEmpty() {
	  CreditoInputValidation validation = new CreditoInputValidation();
	  CreditoBancario creditoBancario = getCreditoExemplo();

	  validation.doValidation(creditoBancario);
  }

  private CreditoBancario getCreditoExemplo(){
	  CreditoBancario creditoBancario = new CreditoBancario();

	  creditoBancario.setSgFilial("a");
	  creditoBancario.setNrBanco("a");
	  creditoBancario.setDtCredito(new org.joda.time.YearMonthDay());
	  creditoBancario.setVlCredito(new BigDecimal(1L));
	  creditoBancario.setTpModalidade("d");
	  creditoBancario.setTpOrigem("s");
	  creditoBancario.setLogin("s");
	  creditoBancario.setIdFilial(1L);
	  creditoBancario.setIdBanco(1L);

	  return creditoBancario;
  }
}
