package com.mercurio.lms.fretecarreteirocoletaentrega.util;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy.TipoParcela;

public class NotaCreditoHelperTest {

	@BeforeTest
	public void setUp() throws Exception {
		
	}
	
	private List<TipoParcela> getParametrosCompleto() {
		List<TipoParcela> identificadores = new ArrayList<TipoParcela>();		
		identificadores.add(TipoParcela.DIARIA);
		identificadores.add(TipoParcela.QUILOMETRAGEM);
		identificadores.add(TipoParcela.FRETE_PESO);
		identificadores.add(TipoParcela.PERCENTUAL_SOBRE_VALOR);
		identificadores.add(TipoParcela.PERCENTUAL_SOBRE_FRETE);
		identificadores.add(TipoParcela.PERCENTUAL_SOBRE_MERCADORIA);
		identificadores.add(TipoParcela.EVENTO);
		identificadores.add(TipoParcela.VOLUME);
		return identificadores;
	}
	
	private ControleCarga getControleCarga(){
		ControleCarga cc = new ControleCarga();
		return cc;
	}
	
	@Test(expectedExceptions=BusinessException.class)
	public void testaControleCargaNulo() {
		NotaCreditoHelper notaCreditoHelper = new NotaCreditoHelper();
		
		List<TipoParcela> identificadores = getParametrosCompleto();
		
		Map<TipoParcela,BigDecimal> valores = notaCreditoHelper.getDadosControleCarga(null,identificadores);
		
		Assert.assertNotNull(valores);
	}

	@Test
	public void testaRetornoNaoNulo() {
		NotaCreditoHelper notaCreditoHelper = new NotaCreditoHelper();
		
		List<TipoParcela> identificadores = getParametrosCompleto();
		
		Map<TipoParcela,BigDecimal> valores = notaCreditoHelper.getDadosControleCarga(getControleCarga(),identificadores);
		
		Assert.assertNotNull(valores);
	}

	

	@Test
	public void testaRetornoNaoVazio() {
		NotaCreditoHelper notaCreditoHelper = new NotaCreditoHelper();
		
		List<TipoParcela> identificadores = getParametrosCompleto();
		
		Map<TipoParcela,BigDecimal> valores = notaCreditoHelper.getDadosControleCarga(getControleCarga(),identificadores);
		
		Assert.assertFalse(valores.size() == 0);
	}
	
	
	@Test
	public void testaRetornoDiaria() {
		NotaCreditoHelper notaCreditoHelper = new NotaCreditoHelper();		
		Assert.assertEquals(BigDecimal.ONE,notaCreditoHelper.getValorDiaria());			
	}
	
	

	
	

}
