package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.testng.annotations.Test;

import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.FaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ItemValorFaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorFaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.providers.ProviderOperadorFaixaProgressiva;

public class OperadorFaixaProgressivaTest {
	
	ProviderOperadorFaixaProgressiva provider = new ProviderOperadorFaixaProgressiva();
	
	@Test
	public void shouldHave3Ranges() {
		OperadorFaixaProgressiva operador = this.provider.operadorCom3FaixasFuncionais();
		List<ComponenteImportacao> resultado = operador.resultadoImportacao();
		FaixaProgressivaImportacao faixa = (FaixaProgressivaImportacao) resultado.get(0);
		assertEquals(faixa.identificacaoFaixa(), "FRPE");
		assertEquals(faixa.valores().size(), 3);
		verificaValoresSucesso(faixa);
	}


	private void verificaValoresSucesso(FaixaProgressivaImportacao faixa) {
		Iterator<ValorFaixaProgressivaImportacao> iterator = faixa.valores().iterator();
		verificaValoresDaFaixaDe20(iterator.next());
		verificaValoresDaFaixaDe40(iterator.next());
		verificaValoresDaFaixaDe60(iterator.next());
	}


	private void verificaValoresDaFaixaDe20(ValorFaixaProgressivaImportacao valorFaixa20) {
		assertEquals(valorFaixa20.identificacao(), "V:20.0");
		assertEquals(valorFaixa20.valores().size(), 3);
	
		Set<ChaveProgressao> keySet = valorFaixa20.valores().keySet();
		List<ChaveProgressao> chaves = new ArrayList<ChaveProgressao>(keySet);
		Collections.sort(chaves);
		
		ItemValorFaixaProgressivaImportacao item = valorFaixa20.valores().get(chaves.get(0));
		assertNotNull(item.pesoMinimo());
		assertEquals(item.pesoMinimo().valorBigDecimal().doubleValue(), 1.6);
		assertNotNull(item.valor());
		assertEquals(item.valor().valorBigDecimal().doubleValue(), 1.1);
		
		item = valorFaixa20.valores().get(chaves.get(1));
		assertNotNull(item.pesoMinimo());
		assertEquals(item.pesoMinimo().valorBigDecimal().doubleValue(), 1.7);
		assertNotNull(item.valor());
		assertEquals(item.valor().valorBigDecimal().doubleValue(), 1.2);
		
		item = valorFaixa20.valores().get(chaves.get(2));
		assertNotNull(item.pesoMinimo());
		assertEquals(item.pesoMinimo().valorBigDecimal().doubleValue(), 1.5);
		assertNotNull(item.valor());
		assertEquals(item.valor().valorBigDecimal().doubleValue(), 1.0);
		
		
	}
	
	private void verificaValoresDaFaixaDe40(ValorFaixaProgressivaImportacao valorFaixa40) {
		assertEquals(valorFaixa40.identificacao(), "V:40.0");
		assertEquals(valorFaixa40.valores().size(), 3);
	
		Set<ChaveProgressao> keySet = valorFaixa40.valores().keySet();
		List<ChaveProgressao> chaves = new ArrayList<ChaveProgressao>(keySet);
		Collections.sort(chaves);
		
		ItemValorFaixaProgressivaImportacao item = valorFaixa40.valores().get(chaves.get(0));
		assertNotNull(item.pesoMinimo());
		assertEquals(item.pesoMinimo().valorBigDecimal().doubleValue(), 2.6);
		assertNotNull(item.valor());
		assertEquals(item.valor().valorBigDecimal().doubleValue(), 2.1);
		
		item = valorFaixa40.valores().get(chaves.get(2));
		assertNotNull(item.pesoMinimo());
		assertEquals(item.pesoMinimo().valorBigDecimal().doubleValue(), 2.5);
		assertNotNull(item.valor());
		assertEquals(item.valor().valorBigDecimal().doubleValue(), 2.0);
		
		item = valorFaixa40.valores().get(chaves.get(1));
		assertNotNull(item.pesoMinimo());
		assertEquals(item.pesoMinimo().valorBigDecimal().doubleValue(), 2.7);
		assertNotNull(item.valor());
		assertEquals(item.valor().valorBigDecimal().doubleValue(), 2.2);
		
		
		
	}
	
	private void verificaValoresDaFaixaDe60(ValorFaixaProgressivaImportacao valorFaixa60) {
		assertEquals(valorFaixa60.identificacao(), "V:60.0");
		assertEquals(valorFaixa60.valores().size(), 3);
	
		Set<ChaveProgressao> keySet = valorFaixa60.valores().keySet();
		List<ChaveProgressao> chaves = new ArrayList<ChaveProgressao>(keySet);
		Collections.sort(chaves);
		
		ItemValorFaixaProgressivaImportacao item = valorFaixa60.valores().get(chaves.get(0));
		assertNotNull(item.pesoMinimo());
		assertEquals(item.pesoMinimo().valorBigDecimal().doubleValue(), 3.6);
		assertNotNull(item.valor());
		assertEquals(item.valor().valorBigDecimal().doubleValue(), 3.1);
		
		item = valorFaixa60.valores().get(chaves.get(2));
		assertNotNull(item.pesoMinimo());
		assertEquals(item.pesoMinimo().valorBigDecimal().doubleValue(), 3.5);
		assertNotNull(item.valor());
		assertEquals(item.valor().valorBigDecimal().doubleValue(), 3.0);
		
		item = valorFaixa60.valores().get(chaves.get(1));
		assertNotNull(item.pesoMinimo());
		assertEquals(item.pesoMinimo().valorBigDecimal().doubleValue(), 3.7);
		assertNotNull(item.valor());
		assertEquals(item.valor().valorBigDecimal().doubleValue(), 3.2);
		
	}
	
	@Test
	public void shouldHaveJustOneRange() {
		OperadorFaixaProgressiva operador = this.provider.operadorCom1FaixaFuncional();
		List<ComponenteImportacao> resultado = operador.resultadoImportacao();
		FaixaProgressivaImportacao faixa = (FaixaProgressivaImportacao) resultado.get(0);
		assertEquals(faixa.identificacaoFaixa(), "FRPE");
		assertEquals(faixa.valores().size(), 6);
		Iterator<ValorFaixaProgressivaImportacao> iterator = faixa.valores().iterator();
		this.verificaValoresVLFXP25(iterator.next());
		this.verificaValoresVLFXP50(iterator.next());

		this.verificaValoresPRDESP7(iterator.next());
		this.verificaValoresPRDESP8(iterator.next());
		this.verificaValoresPRDESP9(iterator.next());
		this.verificaValoresPRDESP10(iterator.next());
	}


	private void verificaValoresPRDESP10(ValorFaixaProgressivaImportacao valor) {
		verificaValores1FaixaFuncional(valor, "P:10", 0.1d);
	}
	
	private void verificaValoresPRDESP7(ValorFaixaProgressivaImportacao valor) {
		verificaValores1FaixaFuncional(valor, "P:7", 0.2d);
	}

	private void verificaValoresPRDESP8(ValorFaixaProgressivaImportacao valor) {
		verificaValores1FaixaFuncional(valor, "P:8", 0.3d);
	}

	private void verificaValoresPRDESP9(ValorFaixaProgressivaImportacao valor) {
		verificaValores1FaixaFuncional(valor, "P:9", 0.4d);
	}

	private void verificaValoresVLFXP25(ValorFaixaProgressivaImportacao valor) {
		verificaValores1FaixaFuncional(valor, "V:25.5", 14.97d);
	}

	private void verificaValoresVLFXP50(ValorFaixaProgressivaImportacao valor) {
		verificaValores1FaixaFuncional(valor, "V:50.5", 13.19d);
	}

	private void verificaValores1FaixaFuncional(ValorFaixaProgressivaImportacao valor, String identificacaoEsperada, Double valorEsperado) {
		assertEquals(valor.identificacao(), identificacaoEsperada);
		assertEquals(valor.valores().size(), 1);
		Set<ChaveProgressao> keySet = valor.valores().keySet();
		List<ChaveProgressao> chaves = new ArrayList<ChaveProgressao>(keySet);
		Collections.sort(chaves);
		ItemValorFaixaProgressivaImportacao item = valor.valores().get(chaves.get(0));
		assertNull(item.pesoMinimo());
		assertNull(item.fatorMultiplicacao());
		assertNotNull(item.valor());
		assertEquals(item.valor().valorBigDecimal().doubleValue(), valorEsperado);
	}

}
