package com.mercurio.lms.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.mercurio.lms.util.ValidateUtils;

public class ValidateUtilsTest {
	
	@Test
	public void testValidateCpfOrCnpjWithNull(){
		Assert.assertFalse(ValidateUtils.validateCpfOrCnpj(null));
	}
	
	@Test
	public void testValidateCpfWithValidCpf(){
		Assert.assertTrue(ValidateUtils.validateCpfOrCnpj("065.424.935-06"));
	}
	
	@Test
	public void testValidateCpfWithNotValidCpf(){
		Assert.assertFalse(ValidateUtils.validateCpfOrCnpj("165.111.235-98"));
	}
	
	@Test
	public void testValidateCnpjWithValid(){
		Assert.assertTrue(ValidateUtils.validateCpfOrCnpj("69.560.951/0001-11"));
	}
	
	@Test
	public void testValidateCnpjWithNotValid(){
		Assert.assertFalse(ValidateUtils.validateCpfOrCnpj("99.233.111/0101-15"));
	}
	
	@Test
	public void testValidatePisWithNotValid(){
		Assert.assertFalse(ValidateUtils.validatePis("325.50325.50-5"));
	}
	
	@Test
	public void testValidatePisWithNotValidWithoutMask(){
		Assert.assertFalse(ValidateUtils.validatePis("32550325505"));
	}
	
	@Test
	public void testValidatePisWithValid(){
		Assert.assertTrue(ValidateUtils.validatePis("577.18577.18-7"));
	}
	
	@Test
	public void testValidatePisWithValidWithoutMask(){
		Assert.assertTrue(ValidateUtils.validatePis("57718577187"));
	}
}
