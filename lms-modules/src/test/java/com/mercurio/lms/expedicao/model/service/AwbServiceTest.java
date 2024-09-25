package com.mercurio.lms.expedicao.model.service;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class AwbServiceTest  {
	private AwbService awbService;
	
	@BeforeMethod(alwaysRun = true)
	protected final void prepareBeforeTest() throws Exception {
		awbService = new AwbService();
	}

	@AfterMethod(alwaysRun = true)
	protected final void disposeAfterTest() throws Exception {
	}
	
	@Test
	public void testeCalculoRentabilidade() {
		double percentualRentabilidade = 10.0;
		double valorFreteLiquidoDocumentos = 100.0;
		
		double calculo = awbService.calcularRentabilidade(valorFreteLiquidoDocumentos, percentualRentabilidade);
		
		assertEquals(calculo, 10.0);
	}
	
	@Test
	public void testeCalculoRentabilidade2() {
		double percentualRentabilidade = 10.0;
		double valorFreteLiquidoDocumentos = 325.5;
		
		double calculo = awbService.calcularRentabilidade(valorFreteLiquidoDocumentos, percentualRentabilidade);
		
		assertEquals(calculo, 32.55);
	}
	
	
	@Test
	public void testeCalculoRentabilidadeArredondamentoPraCima() {
		double percentualRentabilidade = 12.0;
		double valorFreteLiquidoDocumentos = 325.52;
		
		double calculo = awbService.calcularRentabilidade(valorFreteLiquidoDocumentos, percentualRentabilidade);
		
		assertEquals(calculo, 39.07);
	}
	
	@Test
	public void testeCalculoRentabilidadeArredondamentoPraCima2() {
		double percentualRentabilidade = 10.0;
		double valorFreteLiquidoDocumentos = 499.99;
		
		double calculo = awbService.calcularRentabilidade(valorFreteLiquidoDocumentos, percentualRentabilidade);
		
		assertEquals(calculo, 50.0);
	}
	
	@Test
	public void testeCalculoRentabilidadeZero() {
		double percentualRentabilidade = 0.0;
		double valorFreteLiquidoDocumentos = 325.52;
		
		double calculo = awbService.calcularRentabilidade(valorFreteLiquidoDocumentos, percentualRentabilidade);
		
		assertEquals(calculo, 0.0);
	}
	
	
	@Test
	public void testeCalculoRentabilidadeZero2() {
		double percentualRentabilidade = 10.0;
		double valorFreteLiquidoDocumentos = 0.0;
		
		double calculo = awbService.calcularRentabilidade(valorFreteLiquidoDocumentos, percentualRentabilidade);
		
		assertEquals(calculo, 0.0);
	}
	
	
}
