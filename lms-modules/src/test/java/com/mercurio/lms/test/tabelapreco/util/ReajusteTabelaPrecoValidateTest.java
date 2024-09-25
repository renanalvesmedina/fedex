package com.mercurio.lms.test.tabelapreco.util;

import java.math.BigDecimal;
import java.util.HashMap;

import org.joda.time.YearMonthDay;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPreco;
import com.mercurio.lms.tabelaprecos.util.ReajusteTabelaPrecoValidate;

public class ReajusteTabelaPrecoValidateTest {
	
	ReajusteTabelaPrecoValidate validate;
	
	@BeforeTest
	public void init(){
		validate = new ReajusteTabelaPrecoValidate();
	}
	
	@Test()
	public void testConsultaVazia(){
		HashMap<String, Object> criteria = new HashMap<String, Object>();
		Assert.assertFalse( validate.isConsultaValida(criteria));
	}
	
	@Test()
	public void testConsultaNula(){
		Assert.assertFalse(validate.isConsultaValida(null));
	}
	
	@Test()
	public void testConsultaValida(){
		HashMap<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("tabelaBase.idTabelaPreco", "t");
		Assert.assertTrue(validate.isConsultaValida(criteria));
	}
	
	@Test
	public void testValidateInsertTipoTabela(){
		Assert.assertTrue(validate.validateInsertTipoTabela(null, "T"));
	}
	
	@Test()
	public void testValidateInsertTipoTabelaInvalidTabReferencial(){
		Assert.assertFalse(validate.validateInsertTipoTabela(Long.MIN_VALUE, "T"));
	}
	
	@Test(expectedExceptions=BusinessException.class)
	public void testValidateInsertTipoTabelaInexistTabPreco(){
		validate.validateInsertTipoTabela(null, "D");
	}
	
	@Test
	public void testValidateInsertTabelaPreco(){
		Assert.assertTrue(validate.validateInsertTabelaPreco(null, null, null));
		Assert.assertTrue(validate.validateInsertTabelaPreco(null, null, "T"));
		Assert.assertTrue(validate.validateInsertTabelaPreco(null, null, "D"));
	}
	
	@Test(expectedExceptions=BusinessException.class)
	public void testValidateInsertTabelaPrecoInvalidExistTabReferencial(){
		Assert.assertTrue(validate.validateInsertTabelaPreco(new ReajusteTabelaPreco(), Long.MIN_VALUE, "T"));
	}
	
	@Test
	public void testTabReferencial(){
		Assert.assertTrue(validate.isTabelaReferencial("T"));
		Assert.assertTrue(validate.isTabelaReferencial("M"));
		Assert.assertTrue(validate.isTabelaReferencial("A"));
		Assert.assertFalse(validate.isTabelaReferencial("X"));
		Assert.assertFalse(validate.isTabelaReferencial(null));
	}
	
	@Test
	public void testDatasValidasStore(){
		Assert.assertTrue(validate.validateFieldStore(createReajuste(new YearMonthDay(2014,12,25), null)));
		Assert.assertFalse(validate.validateFieldStore(createReajuste(null, new YearMonthDay(2014,12,25))));
	}
	
	private ReajusteTabelaPreco createReajuste(YearMonthDay vigenciaInicial, YearMonthDay vigenciaFinal){
		ReajusteTabelaPreco reajuste = new ReajusteTabelaPreco();
		reajuste.setPercentualReajusteGeral(BigDecimal.ONE);
		reajuste.setIdTabelaBase(Long.MIN_VALUE);
		reajuste.setTipo("");
		reajuste.setSubTipo("");
		reajuste.setVersao("");
		reajuste.setDtVigenciaInicial(vigenciaInicial);
		reajuste.setDtVigenciaFinal(vigenciaFinal);
		
		return reajuste;
	}
	
	

}
