package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ItemValorFaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorImportacao;

public class CacheValoresFaixaProgressivaTest {

	@Test
	public void shouldAddNewValueAbreviated() {
		CacheValoresFaixaProgressiva cache = new CacheValoresFaixaProgressiva();
		ValorImportacao valor = new ValorImportacao(2,3,"20.0",new TagImportacao(2,2,"[#FRPE_FXP_PSMIN]"));
		cache.inclui(valor, "FRPE_20.0");
		
		ItemValorFaixaProgressivaImportacao item = cache.next();
		assertEquals(item.pesoMinimo(), valor);
	}
	
	@Test
	public void shouldAddNewValueFullAfterAbreviated() {
		CacheValoresFaixaProgressiva cache = new CacheValoresFaixaProgressiva();
		ValorImportacao pesoMinimo = new ValorImportacao(2,3,"20.0",new TagImportacao(2,2,"[#FRPE_FXP_PSMIN]"));
		cache.inclui(pesoMinimo, "FRPE_20.0");
		ValorImportacao valor = new ValorImportacao(2,3,"20.0",new TagImportacao(2,2,"[#FRPE_FXP_VLFXPROG]"));
		cache.inclui(valor, "FRPE_20.0");
		ItemValorFaixaProgressivaImportacao item = cache.next();
		assertEquals(item.pesoMinimo(), pesoMinimo);
		assertEquals(item.valor(), valor);
	}
	
	@Test
	public void shouldAddNewValueAbreviatedAfterFull() {
		CacheValoresFaixaProgressiva cache = new CacheValoresFaixaProgressiva();
		ValorImportacao valor = new ValorImportacao(2,3,"20.0",new TagImportacao(2,2,"[#FRPE_FXP_VLFXPROG]"));
		cache.inclui(valor, "FRPE_20.0");
		ValorImportacao pesoMinimo = new ValorImportacao(2,3,"20.0",new TagImportacao(2,2,"[#FRPE_FXP_PSMIN]"));
		cache.inclui(pesoMinimo, "FRPE_20.0");
		ItemValorFaixaProgressivaImportacao item = cache.next();
		assertEquals(item.pesoMinimo(), pesoMinimo);
		assertEquals(item.valor(), valor);
	}
	
	
	@Test
	public void shouldAddNewValueAbreviatedAfterFullWithVlFxAndPrd() {
		CacheValoresFaixaProgressiva cache = new CacheValoresFaixaProgressiva();
		ValorImportacao valor = new ValorImportacao(2,3,"9.0",new TagImportacao(2,2,"[#FRPE_FXP_VLFXPROG]"));
		cache.inclui(valor, "FRPE_20.0");
		ValorImportacao pesoMinimoVl = new ValorImportacao(2,3,"6.0",new TagImportacao(2,2,"[#FRPE_FXP_PSMIN]"));
		cache.inclui(pesoMinimoVl, "FRPE_20.0");
		ValorImportacao pesoMinimoPr = new ValorImportacao(2,3,"5.0",new TagImportacao(2,2,"[#FRPE_FXP_PSMIN]"));
		cache.inclui(pesoMinimoPr, "FRPE_20.0");
		ValorImportacao prodEsp = new ValorImportacao(2,3,"4.0",new TagImportacao(2,2,"[#FRPE_FXP_PRDESP]"));
		cache.inclui(prodEsp, "FRPE_20.0");
		
		ItemValorFaixaProgressivaImportacao item = cache.next();
		assertEquals(item.pesoMinimo(), pesoMinimoPr);
		assertEquals(item.valor(), prodEsp);
		item = cache.next();
		assertEquals(item.pesoMinimo(), pesoMinimoVl);
		assertEquals(item.valor(), valor);
	}
	
	@Test
	public void shouldAddValuesSuccessfully() {
		CacheValoresFaixaProgressiva cache = new CacheValoresFaixaProgressiva();
		ValorImportacao valor20 = new ValorImportacao(3,5, "1.0", new TagImportacao(1,5,"[#FRPE_FXP_VLFXPROG]"));
		cache.inclui(valor20,  "FRPE_20");
		ValorImportacao valor40 = new ValorImportacao(3,6, "2.0", new TagImportacao(1,6,"[#FRPE_FXP_VLFXPROG]"));
		cache.inclui(valor40,  "FRPE_40");
		ValorImportacao valor60 = new ValorImportacao(3,7, "3.0", new TagImportacao(1,7,"[#FRPE_FXP_VLFXPROG]"));
		cache.inclui(valor60,  "FRPE_60");
		ValorImportacao psMin20 = new ValorImportacao(3,8, "1.5", new TagImportacao(1,8,"[#FRPE_FXP_PSMIN]"));
		cache.inclui(psMin20,  "FRPE_20");
		ValorImportacao psMin40 = new ValorImportacao(3,9, "2.5", new TagImportacao(1,9,"[#FRPE_FXP_PSMIN]"));
		cache.inclui(psMin40,  "FRPE_40");
		ValorImportacao psMin60 = new ValorImportacao(3,10, "3.5", new TagImportacao(1,10,"[#FRPE_FXP_PSMIN]"));
		cache.inclui(psMin60,  "FRPE_60");
		
		ItemValorFaixaProgressivaImportacao item = cache.next();
		assertEquals(item.pesoMinimo(), psMin20);
		assertEquals(item.valor(), valor20);
		item = cache.next();
		assertEquals(item.pesoMinimo(), psMin40);
		assertEquals(item.valor(), valor40);
		item = cache.next();
		assertEquals(item.pesoMinimo(), psMin60);
		assertEquals(item.valor(), valor60);
		
		
	}
	
}
