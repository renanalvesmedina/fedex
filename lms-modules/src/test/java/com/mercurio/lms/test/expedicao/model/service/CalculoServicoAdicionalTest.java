package com.mercurio.lms.test.expedicao.model.service;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.math.BigDecimal;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mercurio.lms.expedicao.model.ServicoAdicionalCalculo;
import com.mercurio.lms.expedicao.model.service.CalculoServicoAdicionalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;


public class CalculoServicoAdicionalTest  {
	private CalculoServicoAdicionalService calculoService;
	
	@BeforeMethod(alwaysRun = true)
	protected final void prepareBeforeTest() throws Exception {
		calculoService = new CalculoServicoAdicionalService();
	}

	@AfterMethod(alwaysRun = true)
	protected final void disposeAfterTest() throws Exception {
		
	}
	
	@Test(groups="expedicao")
	public void testSemCalculo() {
		ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();
		calculo.setCdParcela(ConstantesExpedicao.CD_COMPROVANTE_ENTREGA);
		assertNull(calculoService.executeCalculo(null, null, null, calculo));
		calculo.setCdParcela(null);
		assertNull(calculoService.executeCalculo(toBD(100), null, null, calculo));
	}
	
	@Test(groups="expedicao")
	public void testCalculoAgendamento() {			
		String[] cdParcelas = new String[] {ConstantesExpedicao.CD_AGENDAMENTO_COLETA};
		
		for(int x=0; x < cdParcelas.length; x++) {
			ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();
			calculo.setCdParcela(cdParcelas[x]);
			calculo.setVlFrete(toBD(3333.33));
			/* Cálculo sem valor mínimo - arredondamento para baixo (418.332915) */
			assertEquals(calculoService.executeCalculo(
					toBD(12.55), null, null, calculo), toBD(418.33));
			/* Cálculo normal - arredondamento para cima (1666.665) */
			assertEquals(calculoService.executeCalculo(
					toBD(50), toBD(1650), toBD(1000), calculo), toBD(1666.67));
			/* Cálculo com valor mínimo maior que valor calculado */
			assertEquals(calculoService.executeCalculo(
					toBD(12.55), toBD(450.50), toBD(1000), calculo), toBD(450.50));
		}
 	}
	
	@Test(groups="expedicao")
	public void testCalculoPaletizacao() {	
		String[] cdParcelas = new String[] {ConstantesExpedicao.CD_PALETIZACAO};
		
		for(int x=0; x < cdParcelas.length; x++) {
			ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();
			calculo.setCdParcela(cdParcelas[x]);
			/* Cálculo normal */			
			calculo.setQtPaletes(2);			
			assertEquals(calculoService.executeCalculo(
					toBD(66.66), null, null, calculo), toBD(133.32));
			/* Cálculo normal - valor mínimo informado (não deve ser considerado) */
			calculo.setQtPaletes(0);
			assertEquals(calculoService.executeCalculo(
					toBD(500), toBD(1000), toBD(1000), calculo), toBD(0));
			/* Cálculo normal - valor calculado grande */
			calculo.setQtPaletes(999);
			assertEquals(calculoService.executeCalculo(
					toBD(330.35), toBD(1000), toBD(1000), calculo), toBD(330019.65));				
		}
 	}
	
	@Test(groups="expedicao")
	public void testCalculoArmazenagemSemCalculoTonelada() {	
		String[] cdParcelas = new String[] {
				ConstantesExpedicao.CD_ARMAZENAGEM, ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO};
		
		for(int x=0; x < cdParcelas.length; x++) {
			ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();
			calculo.setCdParcela(cdParcelas[x]);
			calculo.setPsReferencia(toBD(66.333, 3));
			calculo.setVlMercadoria(toBD(315.36));
			calculo.setQtDias(11);
			calculo.setBlPagaSeguro(Boolean.TRUE);
			calculo.setIsTpUnidadeMedidoCobrTonelada(false);
			/* Cálculo normal com arredondamento para cima 
			 * armazenagem: 240.78879 + seguro: 69,3792 
			 * TOTAL: 240.79 + 69.38 = 310.17 */		
			assertEquals(calculoService.executeCalculo(
					toBD(0.33), null, toBD(22), calculo), toBD(310.17));
			/* Cálculo normal com arredondamento para baixo 
			 * armazenagem: 233.49216 + seguro: 63.072 
			 * TOTAL: 233.49 + 63.07 = 296.56 */
			assertEquals(calculoService.executeCalculo(
					toBD(0.32), toBD(200), toBD(20), calculo), toBD(296.56));
			
			/* Cálculo normal com valor mínimo
			 * armazenagem: 233.49216 (valor minimo: 500) + seguro: 63.072 
			 * TOTAL: 500 + 63.07 = 563.07 */	
			assertEquals(calculoService.executeCalculo(
					toBD(0.32), toBD(500), toBD(20), calculo), toBD(563.07));
			calculo.setPsReferencia(toBD(39.953, 3));
			calculo.setVlMercadoria(toBD(995.55));
			calculo.setQtDias(9);
			/* Cálculo normal sem seguro
			 * armazenagem: 233.72505 */
			assertEquals(calculoService.executeCalculo(
					toBD(0.65), toBD(100), toBD(0), calculo), toBD(233.73));
			/* Cálculo normal sem seguro com valor mínimo de armazenagem */
			assertEquals(calculoService.executeCalculo(
					toBD(0.65), toBD(300), toBD(0), calculo), toBD(300));
			/* Cálculo zerado */
			assertEquals(calculoService.executeCalculo(
					toBD(0), toBD(0), toBD(0), calculo), BigDecimal.ZERO);

			/* Cálculo normal sem seguro
			 * armazenagem: 233.72505 */
			calculo.setBlPagaSeguro(Boolean.FALSE);
			assertEquals(calculoService.executeCalculo(
					toBD(0.65), toBD(100), toBD(4153), calculo), toBD(233.73));
			assertEquals(calculoService.executeCalculo(
					toBD(0.65), toBD(100), null, calculo), toBD(233.73));
		}
 	}
	
	@Test(groups="expedicao")
	public void testCalculoArmazenagemComCalculoTonelada() {	
		String[] cdParcelas = new String[] {
				ConstantesExpedicao.CD_ARMAZENAGEM, ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO};
		
		for(int x=0; x < cdParcelas.length; x++) {
			ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();
			calculo.setCdParcela(cdParcelas[x]);
			calculo.setPsReferencia(toBD(66.333, 3));
			calculo.setVlMercadoria(toBD(315.36));
			calculo.setQtDias(11);
			calculo.setBlPagaSeguro(Boolean.TRUE);
			calculo.setIsTpUnidadeMedidoCobrTonelada(true);
			/*	CALCULOS COM SEGURO */
			/* Cálculo com vl_minimo null 
			 * armazenagem: 3,63 + seguro: 69.38 
			 * TOTAL: 3,63 + 69.38 = 310.17 */		
			assertEquals(calculoService.executeCalculo(
					toBD(0.33), null, toBD(22), calculo), toBD(73.01));
			/* Cálculo com valor minimo */
			assertEquals(calculoService.executeCalculo(
					toBD(0.32), toBD(200), toBD(20), calculo), toBD(263.07));
			
			/*	CALCULOS SEM SEGURO */
			calculo.setBlPagaSeguro(Boolean.FALSE);
			/* Cálculo com vL_minimo null*/
			assertEquals(calculoService.executeCalculo(
					toBD(0.33), null, toBD(22), calculo), toBD(3.63));
			/* Cálculo com valor minimo */
			assertEquals(calculoService.executeCalculo(
					toBD(0.65), toBD(100), toBD(4153), calculo), toBD(100.00));
			/* Cálculo com pc_seguro null */
			assertEquals(calculoService.executeCalculo(
					toBD(0.65), toBD(100), null, calculo), toBD(100.00));
		}
 	}
	
	@Test(groups="expedicao")
	public void testCalculoEscolta() {	
		String[] cdParcelas = new String[] {ConstantesExpedicao.CD_ESCOLTA};
		
		for(int x=0; x < cdParcelas.length; x++) {
			ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();			
			calculo.setCdParcela(cdParcelas[x]);
			calculo.setQtSegurancasAdicionais(1);
			calculo.setQtKmRodados(102);
			/* Cálculo normal com 1 segurança adicional */
			assertEquals(calculoService.executeCalculo(
					toBD(5.98), null, toBD(0.99), calculo), toBD(710.94));
			/* Cálculo normal com 2 segurança adicional */
			calculo.setQtSegurancasAdicionais(2);		
			assertEquals(calculoService.executeCalculo(
					toBD(5.98), null, toBD(0.99), calculo), toBD(811.92));
			/* Cálculo normal com 3 seguranças adicionais */
			calculo.setQtKmRodados(50000);
			calculo.setQtSegurancasAdicionais(3);
			assertEquals(calculoService.executeCalculo(
					toBD(50.98), null, toBD(10.99), calculo), toBD(4197500));
			/* Cálculo normal sem seguranças adicionais */
			calculo.setQtKmRodados(100);
			calculo.setQtSegurancasAdicionais(0);
			assertEquals(calculoService.executeCalculo(
					toBD(5.66), null, null, calculo), toBD(566));
		}
 	}
	
	@Test(groups="expedicao")
	public void testCalculoVeiculoDedicado() {
		String[] cdParcelas = new String[] {
				ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO, 
				ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_TOCO,
				ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_VAN};
		
		for(int x=0; x < cdParcelas.length; x++) {
			ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();			
			calculo.setCdParcela(cdParcelas[x]);								
			/* Cálculo normal sem km adicional (até 100km) */
			calculo.setQtKmRodados(100);
			assertEquals(calculoService.executeCalculo(
					toBD(550.55), null, null, calculo), toBD(550.55));			
			/* Cálculo normal sem km adicional (mais de 100km) */
			calculo.setQtKmRodados(1000);
			assertEquals(calculoService.executeCalculo(
					toBD(550.55), null, null, calculo), toBD(550.55));
			/* Cálculo normal com km adicional (mais de 100km) */
			calculo.setQtKmRodados(150);					
			assertEquals(calculoService.executeCalculo(
					toBD(501.39), null, toBD(2.66), calculo), toBD(634.39));		
			/* Cálculo apenas com km adicional */		
			calculo.setQtKmRodados(199);
			assertEquals(calculoService.executeCalculo(
					toBD(0), null, toBD(10), calculo), toBD(990));
		}
 	}

	@Test(groups="expedicao")
	public void testCalculoEstadia() {	
		String[] cdParcelas = new String[] {
				ConstantesExpedicao.CD_ESTADIA_VEICULO, 
				ConstantesExpedicao.CD_ESTADIA_CARRETA,
				ConstantesExpedicao.CD_ESTADIA_CONJUNTO,
				ConstantesExpedicao.CD_ESTADIA_TRUCK};
		
		for(int x=0; x < cdParcelas.length; x++) {
			ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();			
			calculo.setCdParcela(cdParcelas[x]);
			/* Cálculo normal */
			calculo.setQtDias(5);
			assertEquals(calculoService.executeCalculo(
					toBD(11), null, null, calculo), toBD(55));	
			/* Cálculo zerado */
			calculo.setQtDias(0);
			assertEquals(calculoService.executeCalculo(
					toBD(0), null, null, calculo), toBD(0));
			/* Cálculo normal valor calculado grande */
			calculo.setQtDias(200);
			assertEquals(calculoService.executeCalculo(
					toBD(9999.99), null, null, calculo), toBD(1999998));
		}
 	}
	
	@Test(groups="expedicao")
	public void testCalculoCapatazia() {	
		String[] cdParcelas = new String[] {ConstantesExpedicao.CD_CAPATAZIA};
		
		for(int x=0; x < cdParcelas.length; x++) {
			ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();			
			calculo.setCdParcela(cdParcelas[x]);		
			calculo.setVlCusto(toBD(55.55));
			/* Cálculo normal - arredondamento para cima
			 * 55.55 + 20% (11.6655) = 67.22 */
			assertEquals(calculoService.executeCalculo(
					toBD(21), null, null, calculo), toBD(67.22));
			/* Cálculo normal - arredondamento para baixo
			 * 55.55 + 19% (10.5545) = 66.10 */
			assertEquals(calculoService.executeCalculo(
					toBD(19), null, null, calculo), toBD(66.10));						
			/* Cálculo com percentual de acréscimo zerado */
			assertEquals(calculoService.executeCalculo(
					toBD(0), null, null, calculo), toBD(55.55));
			/* Cálculo zerado */
			calculo.setVlCusto(toBD(0));			
			assertEquals(calculoService.executeCalculo(
					toBD(20), null, null, calculo), toBD(0));
		}		
 	}
	
	@Test(groups="expedicao")
	public void testCalculoOperacoesLogisticas() {
		String[] cdParcelas = new String[] {
				ConstantesExpedicao.CD_GESTAO_OPERACOES_LOGISTICAS,
				ConstantesExpedicao.CD_GESTAO_OPERACOES_LOGISTICAS_NO_CLIENTE};
		
		for(int x=0; x < cdParcelas.length; x++) {
			ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();			
			calculo.setCdParcela(cdParcelas[x]);							
			/* Cálculo normal */
			calculo.setVlNegociado(toBD(100.33));		
			assertEquals(calculoService.executeCalculo(
					null, null, null, calculo), toBD(100.33));
			/* Cálculo zerado */			
			calculo.setVlNegociado(toBD(0));			
			assertEquals(calculoService.executeCalculo(
					null, null, null, calculo), toBD(0));
		}
 	}
	
	private BigDecimal toBD(double valor) {
		return toBD(valor, 2);
	}
	
	private BigDecimal toBD(double valor, int scale) {
		return new BigDecimal(valor).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
}
