package com.mercurio.lms.fretecarreteirocoletaentrega.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoCalcPadrao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcValores;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFreteCarreteiroCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.CalculoTabelaFreteCarreteiroCeService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.CalculoTabelaFreteCarreteiroCeService.ParcelaNotaCreditoCalcPadrao;

public class CalculoTabelaFreteCarreteiroCeDiariaTest {

	@BeforeTest
	public void setUp() throws Exception {

	}

	@Test
	public void testaCalculoDiariaComMenosDe6HorasPagaMeiaDiaria() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = null;
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(0, 0, 0, 5, 59, 00);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(false);
		parametros.setIdFilial(1L);
		parametros.setNotas(new ArrayList<Map<String, Object>>());
		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0.5));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testaCalculoDiariaComMaisDe6HorasPagaUmaDiaria() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = null;
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(6, 0, 0, 12, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(false);
		parametros.setIdFilial(1L);
		parametros.setNotas(new ArrayList<Map<String, Object>>());
		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(1));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testaCalculoDiariaComMaisDe6HorasPagaUmaDiariaMaisDeumControleDeCargo() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "6565656";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(0, 0, 0, 5, 59, 59);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		/* controle carga 2 */

		ControleCarga controleCarga2 = getControleCarga(6, 0, 0, 12, 0, 0);

		controlesCarga.add(controleCarga2);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(false);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();

		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(1.0));

		notas.add(item);

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testaCalculoDiariaComMaisDe6HorasPagaUmaDiariaDoisControleDeCargo() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "6565656,6565657";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(0, 0, 0, 5, 59, 59);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		/* controle carga 2 */

		ControleCarga controleCarga2 = getControleCarga(6, 0, 0, 12, 0, 0);

		controlesCarga.add(controleCarga2);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(false);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();
		Map<String, Object> item2 = new HashMap<String, Object>();

		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(0.5));

		item2.put("nr_nota_credito", "6565657");
		item2.put("qt_total", new BigDecimal(0.5));

		notas.add(item);
		notas.add(item2);

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarUmaDiariaComControleCarga6Hora() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 14, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(false);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(1));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarZeroDiariaComDoisControleCarga() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "6565656";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 14, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		/* controle carga 2 */

		ControleCarga controleCarga2 = getControleCarga(20, 0, 0, 21, 0, 0);

		controlesCarga.add(controleCarga2);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(false);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();

		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(1));

		notas.add(item);

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarZeroDiariaComTresControleCarga() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "6565656,6565657";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 14, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		/* controle carga 2 */

		ControleCarga controleCarga2 = getControleCarga(20, 0, 0, 21, 0, 0);

		controlesCarga.add(controleCarga2);

		ControleCarga controleCarga3 = getControleCarga(22, 0, 0, 23, 0, 0);

		controlesCarga.add(controleCarga3);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(false);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();
		Map<String, Object> item2 = new HashMap<String, Object>();

		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(1));

		item2.put("nr_nota_credito", "6565657");
		item2.put("qt_total", new BigDecimal(0));

		notas.add(item);
		notas.add(item2);

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarmeiaDiariaComControleCargaMenorQueHoras() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 10, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(false);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0.5));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarZeroDiariaComControleCargaJaPago() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "6565656,6565657";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 10, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		/* controle carga 2 */

		ControleCarga controleCarga2 = getControleCarga(11, 0, 0, 19, 0, 0);

		controlesCarga.add(controleCarga2);

		ControleCarga controleCarga3 = getControleCarga(20, 0, 0, 23, 0, 0);

		controlesCarga.add(controleCarga3);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(false);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();
		Map<String, Object> item2 = new HashMap<String, Object>();

		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(0.5));

		item2.put("nr_nota_credito", "6565657");
		item2.put("qt_total", new BigDecimal(0.5));

		notas.add(item);
		notas.add(item2);

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarZeroDiariaComControleCargaJaPago2() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 13, 0, 0, 0);
		ControleCarga controleCarga1 = getControleCarga(14, 0, 0, 17, 0, 0, 0);
		ControleCarga controleCarga2 = getControleCarga(17, 0, 0, 21, 0, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		controlesCarga.add(controleCarga1);

		controlesCarga.add(controleCarga2);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(true);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(0.5));

		Map<String, Object> item2 = new HashMap<String, Object>();
		item2.put("nr_nota_credito", "6565656");
		item2.put("qt_total", new BigDecimal(0.5));

		notas.add(item);
		notas.add(item2);

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));
		parametros.setDiariaPaga(true);

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal().intValue(), 0);
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarMeiaDiariaComControleCargaComExcedente() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 13, 0, 0, 0);
		ControleCarga controleCarga1 = getControleCarga(14, 0, 0, 17, 0, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		controlesCarga.add(controleCarga1);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(true);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(0.5));

		notas.add(item);

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0.5));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarMeiaDiariaComControleCargaMenorQue6HorasComExcedente() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 13, 0, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(true);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0.5));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	/***
	 * Exemplo 2: Valor da Diária: R$ 300,00 Neste caso, deve-se pagar a diária
	 * completa e o excedente, pois ultrapassou de 12 horas. Saída Chegada Qtd
	 * horas Valor Pago 21/01/16 08hs 21/01/16 14hs 06hs R$ 300,00
	 * --excedenteExemplo2_0 21/01/16 15hs 21/01/16 21hs 06hs R$ 0,00
	 * --excedenteExemplo2_1 21/01/16 22hs 21/01/16 23hs 01hs R$ 0
	 * --excedenteExemplo2_2
	 */

	@Test
	public void testeDevePagarZeroDiariaComControleCargaMenorQue18HorasComExcedente2() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 14, 0, 0, 0);
		ControleCarga controleCarga1 = getControleCarga(15, 0, 0, 21, 0, 0, 0);
		ControleCarga controleCarga2 = getControleCarga(22, 0, 0, 23, 0, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		controlesCarga.add(controleCarga1);

		controlesCarga.add(controleCarga2);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(true);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(1));

		Map<String, Object> item2 = new HashMap<String, Object>();
		item2.put("nr_nota_credito", "6565656");
		item2.put("qt_total", new BigDecimal(0));

		notas.add(item);
		notas.add(item2);

		parametros.setNotas(notas);
		parametros.setDiariaPaga(true);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarZeroDiariaComControleCargaMenorQue18HorasComExcedente() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 14, 0, 0, 0);
		ControleCarga controleCarga1 = getControleCarga(15, 0, 0, 21, 0, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		controlesCarga.add(controleCarga1);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(true);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(1));

		notas.add(item);

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));
		parametros.setDiariaPaga(true);

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal().intValue(), 0);
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagar1DiariaComControleCargaComExcedente() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 14, 0, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(true);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal().intValue(), 1);
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarMeiaDiariaComControleCargaMaior18ComExcedente() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 13, 0, 0, 0);
		ControleCarga controleCarga1 = getControleCarga(14, 0, 0, 22, 0, 0, 0);
		ControleCarga controleCarga2 = getControleCarga(23, 0, 0, 4, 0, 0, 1);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);
		controlesCarga.add(controleCarga1);
		controlesCarga.add(controleCarga2);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(true);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(0.5));

		Map<String, Object> item2 = new HashMap<String, Object>();
		item2.put("nr_nota_credito", "6565656");
		item2.put("qt_total", new BigDecimal(0.5));

		notas.add(item);
		notas.add(item2);

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0.5));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarZeroDiariaComControleCargaMenor18ComExcedente() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 13, 0, 0, 0);
		ControleCarga controleCarga1 = getControleCarga(14, 0, 0, 22, 0, 0, 0);
		ControleCarga controleCarga2 = getControleCarga(23, 0, 0, 0, 0, 0, 1);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);
		controlesCarga.add(controleCarga1);
		controlesCarga.add(controleCarga2);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(true);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("nr_nota_credito", "6565656");
		item.put("qt_total", new BigDecimal(0.5));

		Map<String, Object> item2 = new HashMap<String, Object>();
		item2.put("nr_nota_credito", "6565656");
		item2.put("qt_total", new BigDecimal(0.5));

		notas.add(item);
		notas.add(item2);

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));
		parametros.setDiariaPaga(true);

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal().intValue(), 0);
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	@Test
	public void testeDevePagarMeiaDiariaComControleCargaMenor6ComExcedente() {

		CalculoTabelaFreteCarreteiroCeService calculo = new CalculoTabelaFreteCarreteiroCeService();

		String notaDiaria = "";
		// add se a nota pagou meia diaria ou diaria completa
		ParametrosCalculoDiariaNotaCreditoPadrao parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
		NotaCredito notaCredito = new NotaCredito();
		TabelaFcValores tabela = new TabelaFcValores();

		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabela.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		tabela.setVlDiaria(new BigDecimal(300));

		ControleCarga controleCarga = getControleCarga(8, 0, 0, 13, 0, 0, 0);

		List<ControleCarga> controlesCarga = new ArrayList<ControleCarga>();

		controlesCarga.add(controleCarga);

		parametros.setControlesCargaNoPeriodo(controlesCarga);

		parametros.setFilialPagaDiariaExcedente(true);
		parametros.setIdFilial(1L);

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();

		parametros.setNotas(notas);

		parametros.setParametro(new BigDecimal(6));

//		NotaCreditoCalcPadrao parcela = calculo.calculaParcelaDiaria(tabela, notaCredito, notaDiaria, parametros);
//
//		Assert.assertNotNull(parcela);
//		Assert.assertEquals(parcela.getTpValor().getValue(), "DIA");
//		Assert.assertEquals(parcela.getQtTotal(), new BigDecimal(0.5));
//		Assert.assertEquals(parcela.getVlValor(), new BigDecimal(300));

	}

	private ControleCarga getControleCarga(int hora, int minuto, int segundo, int horaC, int minutoC, int segundoC) {
		ControleCarga controleCarga = new ControleCarga();

		DateTime dataSaida = getDateTime(hora, minuto, segundo);
		DateTime dataChegada = getDateTime(horaC, minutoC, segundoC);

		controleCarga.setDhSaidaColetaEntrega(dataSaida);
		controleCarga.setDhChegadaColetaEntrega(dataChegada);
		return controleCarga;
	}

	private ControleCarga getControleCarga(int hora, int minuto, int segundo, int horaC, int minutoC, int segundoC, int dias) {
		ControleCarga controleCarga = new ControleCarga();

		DateTime dataSaida = getDateTime(hora, minuto, segundo);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, horaC);
		c.set(Calendar.MINUTE, minutoC);
		c.set(Calendar.SECOND, segundoC);
		c.add(Calendar.DATE, dias);

		DateTime dataChegada = new DateTime(c.getTimeInMillis());

		controleCarga.setDhSaidaColetaEntrega(dataSaida);
		controleCarga.setDhChegadaColetaEntrega(dataChegada);
		return controleCarga;
	}

	private DateTime getDateTime(int HOUR_OF_DAY, int MINUTE, int SECOND) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
		c.set(Calendar.MINUTE, MINUTE);
		c.set(Calendar.SECOND, SECOND);
		return new DateTime(c.getTimeInMillis());
	}

}
