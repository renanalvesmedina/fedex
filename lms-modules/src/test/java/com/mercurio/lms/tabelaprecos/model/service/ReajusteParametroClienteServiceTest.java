package com.mercurio.lms.tabelaprecos.model.service;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.ParametroClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteGeneralidadeCliente;
import com.mercurio.lms.tabelaprecos.model.dao.ParametroReajusteTabelaPrecoDAO;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.dao.GeneralidadeClienteDAO;
import com.mercurio.lms.vendas.model.dao.TaxaClienteDAO;

public class ReajusteParametroClienteServiceTest {

	@Mock private GeneralidadeClienteDAO generalidadeClienteDAO;
	@Mock private TaxaClienteDAO taxaClienteDAO;
	@Mock private ParametroReajusteTabelaPrecoDAO parametroReajusteTabelaPrecoDAO;
	
	ReajusteParametroClienteService service;
	
	@BeforeTest
	public void init(){
		service = new ReajusteParametroClienteService();
		
		MockitoAnnotations.initMocks(this);
		when(generalidadeClienteDAO.findGeneralidadeClienteByIdParamCliente(anyLong())).thenReturn(new ArrayList<GeneralidadeCliente>());
		when(taxaClienteDAO.findTaxaClienteByIdParamCliente(anyLong())).thenReturn(new ArrayList<TaxaCliente>());
		when(parametroReajusteTabelaPrecoDAO.percentuaisToReajusteGeneralidadeCliente(anyLong(), anyLong())).thenReturn(new ArrayList<ReajusteGeneralidadeCliente>());

		service.setGeneralidadeClienteDAO(generalidadeClienteDAO);
		service.setTaxaClienteDAO(taxaClienteDAO);
		service.setParametroReajusteTabelaPrecoDAO(parametroReajusteTabelaPrecoDAO);
	}

	@Test
	public void testCalculoValorGeneralidadeByTpIndicador() {
		Assert.assertEquals(BigDecimal.ZERO, service.calculoValorGeneralidadeByTpIndicador(null, new BigDecimal(100L), new BigDecimal(20L), new BigDecimal(10L)));
		Assert.assertEquals(new BigDecimal(88L), service.calculoValorGeneralidadeByTpIndicador(new DomainValue("D"), new BigDecimal(100L), new BigDecimal(20L), new BigDecimal(10L)));
		Assert.assertEquals(new BigDecimal(132L), service.calculoValorGeneralidadeByTpIndicador(new DomainValue("A"), new BigDecimal(100L), new BigDecimal(20L), new BigDecimal(10L)));
		Assert.assertEquals(new BigDecimal(110L), service.calculoValorGeneralidadeByTpIndicador(new DomainValue("T"), new BigDecimal(100L), new BigDecimal(20L), new BigDecimal(10L)));
		Assert.assertEquals(new BigDecimal(22L), service.calculoValorGeneralidadeByTpIndicador(new DomainValue("V"), new BigDecimal(100L), new BigDecimal(20L), new BigDecimal(10L)));
		Assert.assertEquals(BigDecimal.ZERO, service.calculoValorGeneralidadeByTpIndicador(new DomainValue("Z"), new BigDecimal(100L), new BigDecimal(20L), new BigDecimal(10L)));
	}
	
	@Test
	public void testGetPercentualPedagio() {
		Assert.assertNull(service.getPercentualPedagio(new ParametroCliente(), new ParametroClienteAutomaticoDTO()));
	}
	
	@Test
	public void testIndicadorPedagioCliente() {
		Assert.assertTrue(service.isIndicadorPedagioParamCliente(new DomainValue("O")));
		Assert.assertTrue(service.isIndicadorPedagioParamCliente(new DomainValue("X")));
		Assert.assertTrue(service.isIndicadorPedagioParamCliente(new DomainValue("Q")));
		Assert.assertFalse(service.isIndicadorPedagioParamCliente(null));
		Assert.assertFalse(service.isIndicadorPedagioParamCliente(new DomainValue("P")));
	}
	
	
	@Test
	public void testCalculoPedagioTabPreco() {
		Assert.assertTrue(service.isCalculoPedagioTabPreco("P"));
		Assert.assertTrue(service.isCalculoPedagioTabPreco("F"));
		Assert.assertTrue(service.isCalculoPedagioTabPreco("D"));
		Assert.assertTrue(service.isCalculoPedagioTabPreco("X"));
		Assert.assertFalse(service.isCalculoPedagioTabPreco(null));
		Assert.assertFalse(service.isCalculoPedagioTabPreco("Q"));
	}
	
	@Test
	public void testGetIndicadorPedagioParamCliente(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		percentuais.setPercentualPedagioFracao(BigDecimal.TEN);
		percentuais.setPercentualPedagioFaixaPeso(BigDecimal.ONE);
		percentuais.setPercentualPedagioPostoFracao(BigDecimal.ZERO);
		
		Assert.assertEquals(BigDecimal.TEN, service.getIndicadorPedagioParamCliente("Q", percentuais));
		Assert.assertEquals(BigDecimal.ONE, service.getIndicadorPedagioParamCliente("X", percentuais));
		Assert.assertEquals(BigDecimal.ZERO, service.getIndicadorPedagioParamCliente("O", percentuais));
	}
	
	@Test
	public void testGetCalculoPedagioTabPreco(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		percentuais.setTpPedagioPadrao("P");
		percentuais.setPercentualPedagioPostoFracao(BigDecimal.TEN);
		Assert.assertEquals(BigDecimal.TEN, service.getCalculoPedagioTabPreco(percentuais));
		
		percentuais = new ParametroClienteAutomaticoDTO();
		percentuais.setTpPedagioPadrao("F");
		percentuais.setPercentualPedagioFracao(BigDecimal.TEN);
		Assert.assertEquals(BigDecimal.TEN, service.getCalculoPedagioTabPreco(percentuais));
		
		percentuais = new ParametroClienteAutomaticoDTO();
		percentuais.setTpPedagioPadrao("D");
		percentuais.setPercentualPedagioDocumento(BigDecimal.TEN);
		Assert.assertEquals(BigDecimal.TEN, service.getCalculoPedagioTabPreco(percentuais));
		
		percentuais = new ParametroClienteAutomaticoDTO();
		percentuais.setTpPedagioPadrao("X");
		percentuais.setPercentualPedagioFaixaPeso(BigDecimal.TEN);
		Assert.assertEquals(BigDecimal.TEN, service.getCalculoPedagioTabPreco(percentuais));
	}
	
	@Test
	public void calculaValorReajuste() {
		BigDecimal percentual = new BigDecimal("10");
		BigDecimal valor = new BigDecimal("10");
		BigDecimal valorReajuste = service.calculaValorReajuste(valor, percentual);
		Assert.assertTrue(isBigDecimalEqual(valorReajuste, new BigDecimal("11")));
	}
	@Test
	public void calculaValorReajusteComPrecisao() {
		BigDecimal percentual = new BigDecimal("30");
		BigDecimal valor = new BigDecimal("0.54");
		BigDecimal valorReajuste = service.calculaValorReajuste(valor, percentual);
		Assert.assertTrue(isBigDecimalEqual(valorReajuste, new BigDecimal("0.702")));
	}

	@Test
	public void calculaValorReajusteShouldDiminuirValorWhenPercentualIsNegativo() {
		BigDecimal percentual = new BigDecimal("-10");
		BigDecimal valor = new BigDecimal("10");
		BigDecimal valorReajuste = service.calculaValorReajuste(valor, percentual);
		Assert.assertTrue(isBigDecimalEqual(valorReajuste, new BigDecimal("9")));
	}
	
	@Test
	public void calculaValorReajusteShouldReturnNullWhenValorIsNull() {
		BigDecimal percentual = new BigDecimal("10");
		BigDecimal valor = null;
		BigDecimal valorReajuste = service.calculaValorReajuste(valor, percentual);
		Assert.assertTrue(valorReajuste == null);
	}
	
	@Test
	public void calculaValorReajusteShouldReturnValorWhenPercentualIsNull() {
		BigDecimal percentual = null;
		BigDecimal valor = new BigDecimal("10");
		BigDecimal valorReajuste = service.calculaValorReajuste(valor, percentual);
		Assert.assertTrue(isBigDecimalEqual(valorReajuste, valor));
	}
	
	
	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoAdValorem(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentualAdvalorem = new BigDecimal("10");
		percentuais.setPercentualAdvalorem(percentualAdvalorem);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valorAdvalorem = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteAdvalorem(valorAdvalorem, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlAdvalorem(), 
				service.calculaValorReajuste(valorAdvalorem, percentualAdvalorem)));
		
	}
	private ParametroCliente getExemploParametroClienteAdvalorem(BigDecimal valorAdvalorem, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlAdvalorem(valorAdvalorem);
		parametroCliente.setTpIndicadorAdvalorem(new DomainValue("V"));
		return parametroCliente;
	}
	
	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoAdValorem2(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentualAdvalorem = new BigDecimal("10");
		percentuais.setPercentualAdvalorem2(percentualAdvalorem);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valorAdvalorem = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteAdvalorem2(valorAdvalorem, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlAdvalorem2(), 
				service.calculaValorReajuste(valorAdvalorem, percentualAdvalorem)));
		
	}
	private ParametroCliente getExemploParametroClienteAdvalorem2(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlAdvalorem2(valor);
		parametroCliente.setTpIndicadorAdvalorem2(new DomainValue("V"));
		return parametroCliente;
	}

	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoFretePeso(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentualAdvalorem = new BigDecimal("10");
		percentuais.setPercentualFretePeso(percentualAdvalorem);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valorAdvalorem = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteFretePeso(valorAdvalorem, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlFretePeso(), 
				service.calculaValorReajuste(valorAdvalorem, percentualAdvalorem)));
		
	}
	private ParametroCliente getExemploParametroClienteFretePeso(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlFretePeso(valor);
		parametroCliente.setTpIndicadorFretePeso(new DomainValue("V"));
		return parametroCliente;
	}

	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoMinFretePeso(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentualAdvalorem = new BigDecimal("10");
		percentuais.setPercentualMinFretePeso(percentualAdvalorem);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valorAdvalorem = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteMinFretePeso(valorAdvalorem, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlMinFretePeso(), 
				service.calculaValorReajuste(valorAdvalorem, percentualAdvalorem)));
		
	}
	private ParametroCliente getExemploParametroClienteMinFretePeso(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlMinFretePeso(valor);
		parametroCliente.setTpIndicadorMinFretePeso(new DomainValue("V"));
		return parametroCliente;
	}

	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoMinFreteQuilo(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentualAdvalorem = new BigDecimal("10");
		percentuais.setPercentualMinFreteQuilo(percentualAdvalorem);
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valorAdvalorem = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteMinFreteQuilo(valorAdvalorem, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlMinimoFreteQuilo(), 
				service.calculaValorReajuste(valorAdvalorem, percentualAdvalorem)));
		
	}
	private ParametroCliente getExemploParametroClienteMinFreteQuilo(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlMinimoFreteQuilo(valor);
		return parametroCliente;
	}

	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoMinGris(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentualAdvalorem = new BigDecimal("10");
		percentuais.setPercentualMinGris(percentualAdvalorem);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valorAdvalorem = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteMinGris(valorAdvalorem, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlMinimoGris(), 
				service.calculaValorReajuste(valorAdvalorem, percentualAdvalorem)));
		
	}
	private ParametroCliente getExemploParametroClienteMinGris(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlMinimoGris(valor);
		parametroCliente.setTpIndicadorMinimoGris(new DomainValue("V"));
		return parametroCliente;
	}

	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoMinProgr(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentualAdvalorem = new BigDecimal("10");
		percentuais.setPercentualMinProgr(percentualAdvalorem);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valorAdvalorem = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteMinProgr(valorAdvalorem, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlPercMinimoProgr(), 
				service.calculaValorReajuste(valorAdvalorem, percentualAdvalorem)));
		
	}
	private ParametroCliente getExemploParametroClienteMinProgr(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlPercMinimoProgr(valor);
		parametroCliente.setTpIndicadorPercMinimoProgr(new DomainValue("V")); 
		return parametroCliente;
	}

	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoMinTDE(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentual = new BigDecimal("10");
		percentuais.setPercentualMinTDE(percentual);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valor = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteMinTDE(valor, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlMinimoTde(), 
				service.calculaValorReajuste(valor, percentual)));
		
	}
	private ParametroCliente getExemploParametroClienteMinTDE(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlMinimoTde(valor);
		parametroCliente.setTpIndicadorMinimoTde(new DomainValue("V"));
		return parametroCliente;
	}

	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoMinTRT(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentual = new BigDecimal("10");
		percentuais.setPercentualMinTRT(percentual);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valor = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteMinTRT(valor, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlMinimoTrt(), 
				service.calculaValorReajuste(valor, percentual)));
		
	}
	private ParametroCliente getExemploParametroClienteMinTRT(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlMinimoTrt(valor);
		parametroCliente.setTpIndicadorMinimoTrt(new DomainValue("V"));
		return parametroCliente;
	}

	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoTarifaMinima(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentual = new BigDecimal("10");
		percentuais.setPercentualTarifaMinima(percentual);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valor = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteTarifaMinima(valor, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlTarifaMinima(), 
				service.calculaValorReajuste(valor, percentual)));
		
	}
	private ParametroCliente getExemploParametroClienteTarifaMinima(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlTarifaMinima(valor);
		parametroCliente.setTpTarifaMinima(new DomainValue("V"));
		return parametroCliente;
	}

	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoTDE(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentual = new BigDecimal("10");
		percentuais.setPercentualTDE(percentual);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valor = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteTDE(valor, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlPercentualTde(), 
				service.calculaValorReajuste(valor, percentual)));
		
	}
	private ParametroCliente getExemploParametroClienteTDE(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlPercentualTde(valor);
		parametroCliente.setTpIndicadorPercentualTde(new DomainValue("V"));
		return parametroCliente;
	}

	@Test
	public void reajustarParametroClienteShouldReturnValorCalculadoTRT(){
		ParametroClienteAutomaticoDTO percentuais = new ParametroClienteAutomaticoDTO();
		BigDecimal percentual = new BigDecimal("10");
		percentuais.setPercentualTRT(percentual);
		
		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>();
		BigDecimal valor = new BigDecimal("10");
		parametrosCliente.add(getExemploParametroClienteTRT(valor, 1L));
		
		List<ParametroCliente> parametroClienteReajustado = service.reajustarParametroCliente(parametrosCliente, percentuais, "A");
		
		Assert.assertTrue(isBigDecimalEqual(findParametro(1L, parametroClienteReajustado).getVlPercentualTrt(), 
				service.calculaValorReajuste(valor, percentual)));
		
	}
	private ParametroCliente getExemploParametroClienteTRT(BigDecimal valor, Long idParcela) {
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(idParcela);
		parametroCliente.setVlPercentualTrt(valor);
		parametroCliente.setTpIndicadorPercentualTrt(new DomainValue("V"));
		return parametroCliente;
	}
	
	private ParametroCliente findParametro(Long l, List<ParametroCliente> parametros) {
		for(ParametroCliente param : parametros){
			if(param.getIdParametroCliente().equals(l)){
				return param;
			}
		}
		return null;
	}
	
	private Boolean isBigDecimalEqual(BigDecimal compared, BigDecimal other){
		return compared.compareTo(other) == 0;
	}
}
