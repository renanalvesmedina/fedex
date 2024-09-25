package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.ReajusteParametroParcelaDTO;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;

public class CalculoSimulacaoNovosPrecosParametrizadoServiceTest {

	@Test
	public void executeReajusteGeneralidadeShouldReturnParcelaComValorReajustado(){
		CalculoSimulacaoNovosPrecosParametrizadoService service = createService();
		
		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas(new BigDecimal("15"), new BigDecimal("15"));
		List<TabelaPrecoParcela> tabelaPrecoParcela = simulateTabelaPrecoParcela();
		List<TabelaPrecoParcela> resultadoReajusteGeneralidade = service.executeReajusteGeneralidade(tabelaPrecoParcela, null, parametrosParcelas, true);

		Assert.assertTrue(resultadoReajusteGeneralidade.size() > 0);
		TabelaPrecoParcela parcelaResult = resultadoReajusteGeneralidade.get(0);
		BigDecimal vlGeneralidadeResult = parcelaResult.getGeneralidade().getVlGeneralidade();
		Assert.assertTrue(isBigDecimalEqual(vlGeneralidadeResult, new BigDecimal("11.5")));
	}
	
	@Test
	public void executeReajusteGeneralidadeShouldReturnParcelaSemReajuste(){
		CalculoSimulacaoNovosPrecosParametrizadoService service = createService();
		
		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas(new BigDecimal("15"), new BigDecimal("15"));
		List<TabelaPrecoParcela> tabelaPrecoParcela = simulateTabelaPrecoParcela();
		List<TabelaPrecoParcela> resultadoReajusteGeneralidade = service.executeReajusteGeneralidade(tabelaPrecoParcela, null, parametrosParcelas, false);

		Assert.assertTrue(resultadoReajusteGeneralidade.size() > 0);
		TabelaPrecoParcela parcelaResult = resultadoReajusteGeneralidade.get(0);
		BigDecimal vlGeneralidadeResult = parcelaResult.getGeneralidade().getVlGeneralidade();
		Assert.assertTrue(isBigDecimalEqual(vlGeneralidadeResult, new BigDecimal("10")));
	}
	
	@Test
	public void executeReajusteGeneralidadeShouldReturnParcelaComValorReajustadoIgualWhenReajusteTemValorZero(){
		CalculoSimulacaoNovosPrecosParametrizadoService service = createService();
		
		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas(new BigDecimal("0"), new BigDecimal("0"));
		List<TabelaPrecoParcela> tabelaPrecoParcela = simulateTabelaPrecoParcela();
		List<TabelaPrecoParcela> resultadoReajusteGeneralidade = service.executeReajusteGeneralidade(tabelaPrecoParcela, null, parametrosParcelas, false);

		Assert.assertTrue(resultadoReajusteGeneralidade.size() > 0);
		TabelaPrecoParcela parcelaResult = resultadoReajusteGeneralidade.get(0);
		BigDecimal vlGeneralidadeResult = parcelaResult.getGeneralidade().getVlGeneralidade();
		Assert.assertTrue(isBigDecimalEqual(vlGeneralidadeResult, new BigDecimal("10")));
	}
	
	@Test
	public void executeReajusteValorTaxaShouldReturnParcelaComValorReajustado(){
		
		CalculoSimulacaoNovosPrecosParametrizadoService service = createService();

		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas(new BigDecimal("15"), new BigDecimal("15"));
		List<TabelaPrecoParcela> tabelaPrecoParcela = simulateTabelaPrecoParcelaTaxa();
		
		List<TabelaPrecoParcela> resultReajusteValorTaxa = service.executeReajusteValorTaxa(tabelaPrecoParcela, null, parametrosParcelas, true);

		Assert.assertTrue(resultReajusteValorTaxa.size() > 0);
		TabelaPrecoParcela parcelaResult = resultReajusteValorTaxa.get(0);
		BigDecimal vlResult = parcelaResult.getValorTaxa().getVlTaxa();
		Assert.assertTrue(isBigDecimalEqual(vlResult, new BigDecimal("11.5")));
	}
	
	@Test
	public void executeReajusteValorTaxaShouldReturnParcelaComValorReajustadoIgualWhenReajusteTemValorZero(){
		
		CalculoSimulacaoNovosPrecosParametrizadoService service = createService();

		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas(new BigDecimal("0"), new BigDecimal("0"));
		List<TabelaPrecoParcela> tabelaPrecoParcela = simulateTabelaPrecoParcelaTaxa();
		
		List<TabelaPrecoParcela> resultReajusteValorTaxa = service.executeReajusteValorTaxa(tabelaPrecoParcela, null, parametrosParcelas, true);

		Assert.assertTrue(resultReajusteValorTaxa.size() > 0);
		TabelaPrecoParcela parcelaResult = resultReajusteValorTaxa.get(0);
		BigDecimal vlResult = parcelaResult.getValorTaxa().getVlTaxa();
		Assert.assertTrue(isBigDecimalEqual(vlResult, new BigDecimal("10")));
		
	}

	@Test
	public void executeReajusteValorTaxaShouldReturnParcelaSemReajuste(){
		CalculoSimulacaoNovosPrecosParametrizadoService service = createService();
		
		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas(new BigDecimal("15"), new BigDecimal("15"));
		List<TabelaPrecoParcela> tabelaPrecoParcela = simulateTabelaPrecoParcelaTaxa();
		List<TabelaPrecoParcela> resultReajusteValorTaxa = service.executeReajusteValorTaxa(tabelaPrecoParcela, null, parametrosParcelas, false);

		Assert.assertTrue(resultReajusteValorTaxa.size() > 0);
		TabelaPrecoParcela parcelaResult = resultReajusteValorTaxa.get(0);
		BigDecimal vlResult = parcelaResult.getValorTaxa().getVlTaxa();
		Assert.assertTrue(isBigDecimalEqual(vlResult, new BigDecimal("10")));
	}

	
	@Test
	public void executeReajusteValorTaxaShouldReturnParcelaComValorReajustadoIgualWhenNaoEncontraParametro(){
		
		CalculoSimulacaoNovosPrecosParametrizadoService service = createService();

		List<ReajusteParametroParcelaDTO> parametrosParcelas = simulateParametrosParcelas(new BigDecimal("15"), new BigDecimal("15"));
		List<TabelaPrecoParcela> tabelaPrecoParcela = simulateTabelaPrecoParcelaTaxaDiferente();
		
		List<TabelaPrecoParcela> resultReajusteValorTaxa = service.executeReajusteValorTaxa(tabelaPrecoParcela, null, parametrosParcelas, true);

		Assert.assertTrue(resultReajusteValorTaxa.size() > 0);
		TabelaPrecoParcela parcelaResult = resultReajusteValorTaxa.get(0);
		BigDecimal vlResult = parcelaResult.getValorTaxa().getVlTaxa();
		Assert.assertTrue(isBigDecimalEqual(vlResult, new BigDecimal("10")));
		
	}

	private CalculoSimulacaoNovosPrecosParametrizadoService createService() {
		CalculoSimulacaoNovosPrecosParametrizadoService service = new CalculoSimulacaoNovosPrecosParametrizadoService();
		service.setCalculoReajusteTabelaPrecoParametrizadoService(new CalculoReajusteTabelaPrecoParametrizadoService());
		service.setCalculoSimulacaoNovosPrecosService(new CalculoSimulacaoNovosPrecosService());
		return service;
	}

	private List<ReajusteParametroParcelaDTO> simulateParametrosParcelas(BigDecimal percentual, BigDecimal percentualMinimo) {
		List<ReajusteParametroParcelaDTO> parcelas = new ArrayList<ReajusteParametroParcelaDTO>();
		ReajusteParametroParcelaDTO parcela = new ReajusteParametroParcelaDTO();
		parcela.setIdTabelaPrecoParcela(1L);
		parcela.setPcParcela(percentual);
		parcela.setPcMinParcela(percentualMinimo);
		parcelas.add(parcela);
		
		return parcelas;
	}

	private List<TabelaPrecoParcela> simulateTabelaPrecoParcelaTaxa() {
		List<TabelaPrecoParcela> parcelas = new ArrayList<TabelaPrecoParcela>();
		
		TabelaPrecoParcela parcela = new TabelaPrecoParcela();
		
		ValorTaxa taxa = new ValorTaxa();
		taxa.setIdValorTaxa(1L);
		taxa.setVlTaxa(new BigDecimal("10"));
		parcela.setValorTaxa(taxa);
		
		ParcelaPreco preco = new ParcelaPreco();
		preco.setIdParcelaPreco(1L);
		parcela.setParcelaPreco(preco);
		
		parcelas.add(parcela);
		
		return parcelas;
	}

	private List<TabelaPrecoParcela> simulateTabelaPrecoParcelaTaxaDiferente() {
		List<TabelaPrecoParcela> parcelas = new ArrayList<TabelaPrecoParcela>();
		
		TabelaPrecoParcela parcela = new TabelaPrecoParcela();
		
		ValorTaxa taxa = new ValorTaxa();
		taxa.setIdValorTaxa(2L);
		taxa.setVlTaxa(new BigDecimal("10"));
		parcela.setValorTaxa(taxa);
		
		ParcelaPreco preco = new ParcelaPreco();
		preco.setIdParcelaPreco(2L);
		parcela.setParcelaPreco(preco);
		
		parcelas.add(parcela);
		
		return parcelas;
	}
	
	private List<TabelaPrecoParcela> simulateTabelaPrecoParcela() {
		List<TabelaPrecoParcela> parcelas = new ArrayList<TabelaPrecoParcela>();
		
		TabelaPrecoParcela parcela = new TabelaPrecoParcela();
		Generalidade generalidade = new Generalidade();
		generalidade.setIdGeneralidade(1L);
		generalidade.setVlGeneralidade(new BigDecimal("10"));
		generalidade.setVlMinimo(new BigDecimal("10"));
		parcela.setGeneralidade(generalidade);

		ParcelaPreco preco = new ParcelaPreco();
		preco.setIdParcelaPreco(1L);
		parcela.setParcelaPreco(preco);
		
		parcelas.add(parcela);
		
		return parcelas;
	}

	private Boolean isBigDecimalEqual(BigDecimal compared, BigDecimal other){
		return compared.compareTo(other) == 0;
	}
}
