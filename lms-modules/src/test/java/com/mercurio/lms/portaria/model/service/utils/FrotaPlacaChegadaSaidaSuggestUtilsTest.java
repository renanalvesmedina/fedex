package com.mercurio.lms.portaria.model.service.utils;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;


public class FrotaPlacaChegadaSaidaSuggestUtilsTest {
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testNotNumericParameterShouldThrowException() {
		FrotaPlacaChegadaSaidaSuggestUtils.completarNumeroFrotaComZeros("A1215");
	}
	
	@Test
	public void testReturnSizeShouldBeEqualsToNumberOfCharacters() {
		String frota = FrotaPlacaChegadaSaidaSuggestUtils.completarNumeroFrotaComZeros("123");
		Assert.assertEquals(frota.length(), FrotaPlacaChegadaSaidaSuggestUtils.QUANTIDADE_CARACTERES_NR_FROTA);
	}
	
	@Test
	public void testReturnShouldBeNumeric() {
		String frota = FrotaPlacaChegadaSaidaSuggestUtils.completarNumeroFrotaComZeros("15514");
		Assert.assertTrue(StringUtils.isNumeric(frota));
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testParameterGreaterThanMaxNumberShouldThrowException() {
		FrotaPlacaChegadaSaidaSuggestUtils.completarNumeroFrotaComZeros("0000000000");
	}
	
	@Test
	public void testReturnShouldContainParemeter() {
		String frota = "123";
		String frotaComZeros = FrotaPlacaChegadaSaidaSuggestUtils.completarNumeroFrotaComZeros(frota);
		Assert.assertTrue(frotaComZeros.contains(frota));
	}
}
