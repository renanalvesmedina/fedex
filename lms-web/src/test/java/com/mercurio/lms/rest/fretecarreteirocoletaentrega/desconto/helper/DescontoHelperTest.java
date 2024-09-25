package com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.YearMonthDay;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto.DescontoRfcDTO;

/**
 * @author AlessandroSF
 *
 */
public class DescontoHelperTest {

	private static final BigDecimal R$_1000 = new BigDecimal(1000);
	private static final BigDecimal R$_1001 = new BigDecimal(1001);
	private static final BigDecimal R$_100 = new BigDecimal(100);
	private static final BigDecimal R$_975_30 = new BigDecimal(975.30);
	private static final BigDecimal R$_1000_01 = new BigDecimal(1000.01);
	private static final BigDecimal R$_250 = new BigDecimal(250);

	@BeforeTest
	protected void setup() {
	}
	
	
	private DescontoHelper getDescontoHelper() {
		DescontoHelper descontoHelper = new DescontoHelper();
		ParametroGeralService parametroGerealService = Mockito.mock(ParametroGeralService.class);
		Mockito.when(parametroGerealService.findConteudoByNomeParametro(DescontoHelper.NR_PARCELAS, false)).thenReturn(new BigDecimal(36));
		Mockito.when(parametroGerealService.findConteudoByNomeParametro(DescontoHelper.PERC_MIN_DESCONTO, false)).thenReturn(new BigDecimal(5));
		ConfiguracoesFacade configuracoesFacade = Mockito.mock(ConfiguracoesFacade.class);
		Mockito.when(configuracoesFacade.getMensagem("LMS_25105")).thenReturn("Mensagem");
		
		descontoHelper.setParametroGeralService(parametroGerealService);
		descontoHelper.setConfiguracoesFacade(configuracoesFacade);
		return descontoHelper;
	}
	

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testaEntradaNula() throws Exception {
		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(null);
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaFaltaDeParametros() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaExcessoDeParametros() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setPcDesconto(BigDecimal.TEN);
		entrada.setQtParcelas(10);
		entrada.setVlFixoParcela(BigDecimal.TEN);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaFaltaDeValorTotalCalculoPercentual() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setPcDesconto(BigDecimal.TEN);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaFaltaDeValorTotalCalculoQtdParcelas() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtParcelas(10);
		entrada.setVlFixoParcela(BigDecimal.TEN);

		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertNotNull(saida.getParcelas());
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaFaltaDeValorTotalCalculoValorFixo() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setVlFixoParcela(BigDecimal.TEN);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaCalculoValorFixoMaiorQueDesconto() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setVlFixoParcela(R$_1000);
		entrada.setVlTotalDesconto(BigDecimal.TEN);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaCalculoValorFixoSemIntervalo() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setVlFixoParcela(R$_1000);
		entrada.setVlTotalDesconto(BigDecimal.TEN);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);

	}

	/* fim teste validação entrada */

	@Test
	public void testaCalculoValorFixoOk() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setVlFixoParcela(R$_100);
		entrada.setVlTotalDesconto(R$_1000);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertNotNull(saida.getParcelas());
	}

	@Test
	public void testaCalculoValorFixoOkSize() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setVlFixoParcela(R$_100);
		entrada.setVlTotalDesconto(R$_1000);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 10);
	}

	@Test
	public void testaCalculoValorFixoOkUmaParcela() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setVlFixoParcela(R$_100);
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 1);
	}

	@Test
	public void testaCalculoValorFixoOkUltimaParcelaSaldo() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setVlFixoParcela(R$_100);
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(9).getValor(),
				new BigDecimal(101).setScale(2));
	}

	@Test
	public void testaCalculoValorFixoOkSize2() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setVlFixoParcela(R$_100);
		entrada.setVlTotalDesconto(R$_975_30);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 9);
	}

	@Test
	public void testaCalculoValorFixoOkUltimaParcelaSaldoQuebrado()
			throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setVlFixoParcela(R$_100);
		entrada.setVlTotalDesconto(R$_975_30);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(8).getValor(),
				new BigDecimal(175.30).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoValorFixoOkUltimaParcelaSaldoQuebrado2()
			throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setVlFixoParcela(R$_100);
		entrada.setVlTotalDesconto(R$_1000_01);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(9).getValor(),
				new BigDecimal(100.01).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoValorFixoOkUltimaParcelaSaldoSize()
			throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setVlFixoParcela(R$_100);
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 10);
	}

	@Test
	public void testaCalculoValorFixoOkData() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		YearMonthDay proximo = hoje.plusDays(7);
		YearMonthDay ultimo = hoje.plusDays(63);

		entrada.setVlFixoParcela(R$_100);
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(0).getData(), hoje);
		Assert.assertEquals(saida.getParcelas().get(1).getData(), proximo);
		Assert.assertEquals(saida.getParcelas().get(9).getData(), ultimo);
	}

	@Test
	public void testaCalculoValorFixoNumero() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		entrada.setVlFixoParcela(R$_100);
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getNumeroParcela(),
				Integer.valueOf(1));
		Assert.assertEquals(saida.getParcelas().get(1).getNumeroParcela(),
				Integer.valueOf(2));
		Assert.assertEquals(saida.getParcelas().get(9).getNumeroParcela(),
				Integer.valueOf(10));
	}

	/* fim teste calculo fixo */

	@Test
	public void testaCalculoQtdParcelasOk() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setQtParcelas(10);
		entrada.setVlTotalDesconto(R$_1000);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertNotNull(saida.getParcelas());
	}

	@Test
	public void testaCalculoQtdParcelasOkSize() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setQtParcelas(10);
		entrada.setVlTotalDesconto(R$_1000);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 10);
	}

	@Test
	public void testaCalculoQtdParcelasOkUmaParcela() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setQtParcelas(10);
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 10);
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaCalculoQtdParcelasZero() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setQtParcelas(0);
		entrada.setVlTotalDesconto(R$_100);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaCalculoQtdParcelasNegativo() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setQtParcelas(-1);
		entrada.setVlTotalDesconto(R$_100);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}

	@Test
	public void testaCalculoQtdParcelasOkUltimaParcelaSaldo() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setQtParcelas(10);
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(9).getValor(),
				new BigDecimal(100.10).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoQtdParcelasOkSize2() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setQtParcelas(10);
		entrada.setVlTotalDesconto(R$_975_30);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 10);
	}

	@Test
	public void testaCalculoQtdParcelasOkUltimaParcelaSaldoQuebrado()
			throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setQtParcelas(10);
		entrada.setVlTotalDesconto(R$_975_30);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(9).getValor(),
				new BigDecimal(97.53).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoQtdParcelasOkUltimaParcelaSaldoQuebrado2()
			throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setQtParcelas(10);
		entrada.setVlTotalDesconto(R$_1000_01);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(9).getValor(),
				new BigDecimal(100.01).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoQtdParcelasOkUltimaParcelaSaldoSize()
			throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setQtParcelas(10);
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 10);
	}

	@Test
	public void testaCalculoQtdParcelasOkData() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		YearMonthDay proximo = hoje.plusDays(7);
		YearMonthDay ultimo = hoje.plusDays(63);

		entrada.setQtParcelas(10);
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(0).getData(), hoje);
		Assert.assertEquals(saida.getParcelas().get(1).getData(), proximo);
		Assert.assertEquals(saida.getParcelas().get(9).getData(), ultimo);
	}

	@Test
	public void testaCalculoQtdParcelasNumero() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		entrada.setQtParcelas(10);
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getNumeroParcela(),
				Integer.valueOf(1));
		Assert.assertEquals(saida.getParcelas().get(1).getNumeroParcela(),
				Integer.valueOf(2));
		Assert.assertEquals(saida.getParcelas().get(9).getNumeroParcela(),
				Integer.valueOf(10));
	}

	/* fim calculo quantidade parcelas */

	@Test
	public void testaCalculoPercentualOk() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(10));
		entrada.setVlTotalDesconto(R$_1000);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertNotNull(saida.getParcelas());
	}

	@Test
	public void testaCalculoPercentualOkSize() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(10));
		entrada.setVlTotalDesconto(R$_1000);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 10);
	}

	@Test
	public void testaCalculoPercentualOkUmaParcela() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(10));
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 10);
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaCalculoPercentualZero() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(0));
		entrada.setVlTotalDesconto(R$_100);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaCalculoPercentualNegativo() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(-1));
		entrada.setVlTotalDesconto(R$_100);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}

	@Test
	public void testaCalculoPercentualOkUltimaParcelaSaldo() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(10));
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(9).getValor(),
				new BigDecimal(100.10).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoPercentualOkSize2() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(10));
		entrada.setVlTotalDesconto(R$_975_30);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 10);
	}

	@Test
	public void testaCalculoPercentualOkUltimaParcelaSaldoQuebrado()
			throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(10));
		entrada.setVlTotalDesconto(R$_975_30);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(9).getValor(),
				new BigDecimal(97.53).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoPercentualOk100() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(100));
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 1);
	}

	@Test
	public void testaCalculoPercentualOk100Valor() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(100));
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(0).getValor(),
				R$_100.setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoPercentualOk100Data() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());
		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(100));
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(0).getData(), hoje);
	}

	@Test
	public void testaCalculoPercentualOk50Valor() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(50));
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(0).getValor(),
				new BigDecimal(50).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoPercentualOk50Data() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());
		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(50));
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(0).getData(), hoje);
		Assert.assertEquals(saida.getParcelas().get(1).getData(),
				hoje.plusDays(7));
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaCalculoPercentualOk010Data() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());
		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(0.10));
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(0).getData(), hoje);
		Assert.assertEquals(saida.getParcelas().get(0).getValor(),
				new BigDecimal(50).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoPercentualOk5Data() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());
		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(5));
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(0).getData(), hoje);
		Assert.assertEquals(saida.getParcelas().get(0).getValor(),
				new BigDecimal(5).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoPercentualOk501Data() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());
		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(5.01));
		entrada.setVlTotalDesconto(R$_100);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(0).getValor(),
				new BigDecimal(5.01).setScale(2, RoundingMode.HALF_UP));
	}

	@Test(expectedExceptions = BusinessException.class)
	public void testaCalculoPercentualOk101Data() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());
		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(100.99));
		entrada.setVlTotalDesconto(R$_100);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}

	@Test
	public void testaCalculoPercentualOkUltimaParcelaSaldoQuebrado2()
			throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(10));
		entrada.setVlTotalDesconto(R$_1000_01);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(9).getValor(),
				new BigDecimal(100.01).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testaCalculoPercentualOkUltimaParcelaSaldoSize()
			throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(10));
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().size(), 10);
	}

	@Test
	public void testaCalculoPercentualOkData() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		YearMonthDay proximo = hoje.plusDays(7);
		YearMonthDay ultimo = hoje.plusDays(63);

		entrada.setPcDesconto(new BigDecimal(10));
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);
		Assert.assertEquals(saida.getParcelas().get(0).getData(), hoje);
		Assert.assertEquals(saida.getParcelas().get(1).getData(), proximo);
		Assert.assertEquals(saida.getParcelas().get(9).getData(), ultimo);
	}

	@Test
	public void testaCalculoPercentualNumero() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(7);

		entrada.setPcDesconto(new BigDecimal(10));
		entrada.setVlTotalDesconto(R$_1001);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getNumeroParcela(),Integer.valueOf(1));
		Assert.assertEquals(saida.getParcelas().get(1).getNumeroParcela(),Integer.valueOf(2));
		Assert.assertEquals(saida.getParcelas().get(9).getNumeroParcela(),Integer.valueOf(10));
	}

	/* cenarios gil */

	@Test
	public void testaCenario2IndiceParcelas() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(10);

		entrada.setQtParcelas(5);
		entrada.setVlTotalDesconto(R$_1000);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getNumeroParcela(),Integer.valueOf(1));
		Assert.assertEquals(saida.getParcelas().get(1).getNumeroParcela(),Integer.valueOf(2));
		Assert.assertEquals(saida.getParcelas().get(4).getNumeroParcela(),Integer.valueOf(5));
	}
	
	
	@Test
	public void testaCenario2ValorParcelas() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(10);

		entrada.setQtParcelas(5);
		entrada.setVlTotalDesconto(R$_1000);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getValor(),new BigDecimal(200.00).setScale(2));
		Assert.assertEquals(saida.getParcelas().get(1).getValor(),new BigDecimal(200.00).setScale(2));
		Assert.assertEquals(saida.getParcelas().get(2).getValor(),new BigDecimal(200.00).setScale(2));
		Assert.assertEquals(saida.getParcelas().get(3).getValor(),new BigDecimal(200.00).setScale(2));
		Assert.assertEquals(saida.getParcelas().get(4).getValor(),new BigDecimal(200.00).setScale(2));
		
	}
	
	@Test
	public void testaCenario2DatasParcelas() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.MARCH);
		calendar.set(Calendar.YEAR, 2015);
		
		YearMonthDay data = YearMonthDay.fromDateFields(calendar.getTime());

		entrada.setDtInicioDesconto(data);
		entrada.setQtDias(10);

		entrada.setQtParcelas(5);
		entrada.setVlTotalDesconto(R$_1000);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getData(),data);
		Assert.assertEquals(saida.getParcelas().get(1).getData(),data.plusDays(10));
		Assert.assertEquals(saida.getParcelas().get(2).getData(),data.plusDays(20));
		Assert.assertEquals(saida.getParcelas().get(3).getData(),data.plusDays(30));
		Assert.assertEquals(saida.getParcelas().get(4).getData(),data.plusDays(40));
		
	}
	
	
	@Test
	public void testaCenario3IndiceParcelas() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(10);
		entrada.setVlTotalDesconto(R$_1000);
		
		entrada.setVlFixoParcela(R$_250);
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getNumeroParcela(),Integer.valueOf(1));
		Assert.assertEquals(saida.getParcelas().get(1).getNumeroParcela(),Integer.valueOf(2));
		Assert.assertEquals(saida.getParcelas().get(3).getNumeroParcela(),Integer.valueOf(4));
	}
	
	
	@Test
	public void testaCenario3ValorParcelas() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(10);
		entrada.setVlTotalDesconto(R$_1000);

		entrada.setVlFixoParcela(R$_250);
		
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getValor(),new BigDecimal(250.00).setScale(2));
		Assert.assertEquals(saida.getParcelas().get(1).getValor(),new BigDecimal(250.00).setScale(2));
		Assert.assertEquals(saida.getParcelas().get(2).getValor(),new BigDecimal(250.00).setScale(2));
		Assert.assertEquals(saida.getParcelas().get(3).getValor(),new BigDecimal(250.00).setScale(2));		
		
	}
	
	@Test
	public void testaCenario3DatasParcelas() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.MARCH);
		calendar.set(Calendar.YEAR, 2015);
		
		YearMonthDay data = YearMonthDay.fromDateFields(calendar.getTime());

		entrada.setDtInicioDesconto(data);
		entrada.setQtDias(10);
		entrada.setVlTotalDesconto(R$_1000);

		entrada.setVlFixoParcela(R$_250);
		
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getData(),data);
		Assert.assertEquals(saida.getParcelas().get(1).getData(),data.plusDays(10));
		Assert.assertEquals(saida.getParcelas().get(2).getData(),data.plusDays(20));
		Assert.assertEquals(saida.getParcelas().get(3).getData(),data.plusDays(30));				
	}
	
	@Test
	public void testaCenario4IndiceParcelas() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(10);
		entrada.setVlTotalDesconto(R$_1000);
		
		entrada.setPcDesconto(new BigDecimal(30));
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getNumeroParcela(),Integer.valueOf(1));
		Assert.assertEquals(saida.getParcelas().get(1).getNumeroParcela(),Integer.valueOf(2));
	}
	
	
	@Test
	public void testaCenario4ValorParcelas() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(10);
		entrada.setVlTotalDesconto(R$_1000);

		entrada.setPcDesconto(new BigDecimal(30));
		
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getValor(),new BigDecimal(300.00).setScale(2));
		Assert.assertEquals(saida.getParcelas().get(1).getValor(),new BigDecimal(300.00).setScale(2));
		Assert.assertEquals(saida.getParcelas().get(2).getValor(),new BigDecimal(400.00).setScale(2));
						
		
	}
	
	@Test
	public void testaCenario4DatasParcelas() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.MARCH);
		calendar.set(Calendar.YEAR, 2015);
		
		YearMonthDay data = YearMonthDay.fromDateFields(calendar.getTime());

		entrada.setDtInicioDesconto(data);
		entrada.setQtDias(10);
		entrada.setVlTotalDesconto(R$_1000);

		entrada.setPcDesconto(new BigDecimal(30));
		
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getData(),data);
		Assert.assertEquals(saida.getParcelas().get(1).getData(),data.plusDays(10));
		Assert.assertEquals(saida.getParcelas().get(2).getData(),data.plusDays(20));
					
	}
	
	
	@Test
	public void testa3NumeroParcelas1000() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(10);
		entrada.setVlTotalDesconto(R$_1000);
		entrada.setQtParcelas(3);
		
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getValor(),new BigDecimal(333.33).setScale(2,RoundingMode.HALF_UP));
		Assert.assertEquals(saida.getParcelas().get(1).getValor(),new BigDecimal(333.33).setScale(2,RoundingMode.HALF_UP));
		Assert.assertEquals(saida.getParcelas().get(2).getValor(),new BigDecimal(333.34).setScale(2,RoundingMode.HALF_UP));
		
	}

	
	@Test
	public void testa6NumeroParcelas1000() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		YearMonthDay hoje = YearMonthDay.fromDateFields(new Date());

		entrada.setDtInicioDesconto(hoje);
		entrada.setQtDias(10);
		entrada.setVlTotalDesconto(R$_1000);
		entrada.setQtParcelas(6);
		
		DescontoHelper descontoHelper = getDescontoHelper();
		DescontoRfcDTO saida = descontoHelper.gerarParcelas(entrada);

		Assert.assertEquals(saida.getParcelas().get(0).getValor(),new BigDecimal(166.67).setScale(2,RoundingMode.HALF_UP));
		Assert.assertEquals(saida.getParcelas().get(1).getValor(),new BigDecimal(166.67).setScale(2,RoundingMode.HALF_UP));
		Assert.assertEquals(saida.getParcelas().get(2).getValor(),new BigDecimal(166.67).setScale(2,RoundingMode.HALF_UP));
		Assert.assertEquals(saida.getParcelas().get(3).getValor(),new BigDecimal(166.67).setScale(2,RoundingMode.HALF_UP));
		Assert.assertEquals(saida.getParcelas().get(4).getValor(),new BigDecimal(166.67).setScale(2,RoundingMode.HALF_UP));
		Assert.assertEquals(saida.getParcelas().get(5).getValor(),new BigDecimal(166.65).setScale(2,RoundingMode.HALF_UP));
		
	}
	
	@Test
	public void testaCalculoValorFixoJu1() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setVlTotalDesconto(R$_1000);
		entrada.setVlFixoParcela(new BigDecimal("150.50"));
		entrada.setQtDias(1);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}
	
	@Test
	public void testaDescontoPrioritario() throws Exception {
		DescontoRfcDTO entrada = new DescontoRfcDTO();
		entrada.setDtInicioDesconto(YearMonthDay.fromDateFields(new Date()));
		entrada.setVlTotalDesconto(R$_1000);
		entrada.setQtDias(1);
		entrada.setPrioritario(true);

		DescontoHelper descontoHelper = getDescontoHelper();
		descontoHelper.gerarParcelas(entrada);
	}
	

}
