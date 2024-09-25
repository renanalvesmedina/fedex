package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.mercurio.lms.tabelaprecos.model.ReajusteParametroParcelaDTO;

public class CalculoReajusteTabelaPrecoParametrizadoServiceTest {

	@Test
	public void calculaReajusteParcelaShouldReturnInputValueWhenParcelaIsNotFound() {
		CalculoReajusteTabelaPrecoParametrizadoService service = new CalculoReajusteTabelaPrecoParametrizadoService();
		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas();
		BigDecimal valor = new BigDecimal("10");
		Long idParcela = 123L;
		BigDecimal resultadoCalculo = service.calculaReajusteParcela(idParcela, valor, parametrosParcelas);

		Assert.assertTrue(isBigDecimalEqual(resultadoCalculo, valor));
	}

	@Test
	public void calculaReajusteParcelaShouldReturnValorReajustadoWhenParcelaIsFound() {
		CalculoReajusteTabelaPrecoParametrizadoService service = new CalculoReajusteTabelaPrecoParametrizadoService();
		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas();
		BigDecimal valor = new BigDecimal("10");
		Long idParcela = 1L;
		BigDecimal resultadoCalculo = service.calculaReajusteParcela(idParcela, valor, parametrosParcelas);

		Assert.assertTrue( isBigDecimalEqual(resultadoCalculo, new BigDecimal("12")));

	}
	
	@Test
	public void calculaReajusteParcelaFaixaProgressivaShouldReturnValorReajustadoWhenParcelaIsFound() {
		CalculoReajusteTabelaPrecoParametrizadoService service = new CalculoReajusteTabelaPrecoParametrizadoService();
		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas();
		BigDecimal valor = new BigDecimal("10");
		Long idParcela = 2L;
		BigDecimal resultadoCalculo = service.calculaReajusteParcelaFaixaProgressiva(idParcela, valor, parametrosParcelas);

		Assert.assertTrue(isBigDecimalEqual(resultadoCalculo, new BigDecimal("11.5")));
	}
	
	@Test
	public void calculaReajusteParcelaPrecoFreteShouldReturnValorReajustadoWhenParcelaIsFound() {
		CalculoReajusteTabelaPrecoParametrizadoService service = new CalculoReajusteTabelaPrecoParametrizadoService();
		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas();
		BigDecimal valor = new BigDecimal("10");
		Long idParcela = 3L;
		BigDecimal resultadoCalculo = service.calculaReajusteParcelaPrecoFrete(idParcela, valor, parametrosParcelas);

		Assert.assertTrue(isBigDecimalEqual(resultadoCalculo, new BigDecimal("11.5")));
	}

	@Test
	public void calcularReajusteParaOValorShouldReturnValorReajustado() {

		CalculoReajusteTabelaPrecoParametrizadoService service = new CalculoReajusteTabelaPrecoParametrizadoService();
		
		BigDecimal pcParcelaPercent = new BigDecimal("0.10");
		BigDecimal valor = new BigDecimal("10");
		BigDecimal resultado = service.calcularReajusteParaOValor(valor, pcParcelaPercent);
		Assert.assertTrue(isBigDecimalEqual(resultado, new BigDecimal("11")));
	}

	@Test
	public void calcularReajusteParaOValorShouldReturnNullWhenValorIsNull() {

		CalculoReajusteTabelaPrecoParametrizadoService service = new CalculoReajusteTabelaPrecoParametrizadoService();
		
		BigDecimal pcParcelaPercent = new BigDecimal("0.10");
		BigDecimal valor = null;
		BigDecimal resultado = service.calcularReajusteParaOValor(valor, pcParcelaPercent);
		Assert.assertEquals(resultado, null);
	}
	
	private List<ReajusteParametroParcelaDTO> simulateParametrosParcelas() {
		List<ReajusteParametroParcelaDTO> parametroParcela = new ArrayList<ReajusteParametroParcelaDTO>();
		
		ReajusteParametroParcelaDTO dto = new ReajusteParametroParcelaDTO();
		dto.setIdTabelaPrecoParcela(1L);
		dto.setPcParcela(new BigDecimal("20"));
		parametroParcela.add(dto);
		
		dto = new ReajusteParametroParcelaDTO();
		dto.setIdFaixaProgressiva(2L);
		dto.setPcParcela(new BigDecimal("15"));
		parametroParcela.add(dto);
		
		dto = new ReajusteParametroParcelaDTO();
		dto.setIdPrecoFrete(3L);
		dto.setPcParcela(new BigDecimal("15"));
		parametroParcela.add(dto);
		
		return parametroParcela;
	}
	
	private Boolean isBigDecimalEqual(BigDecimal compared, BigDecimal other){
		return compared.compareTo(other) == 0;
	}
}
