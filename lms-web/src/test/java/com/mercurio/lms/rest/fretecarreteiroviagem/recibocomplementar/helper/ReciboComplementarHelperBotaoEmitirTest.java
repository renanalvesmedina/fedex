package com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.helper;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants.ReciboHelperConstants;

/**
 * <b>Classe que testa a classe utilitaria ReciboComplementarHelper</b>
 * <i>Separada por habilitação dos botoes, para ficar mais simples o entendimento</i>
 * <p><b>REGRA:</b></p> 
 * <p>Caso o recibo esteja sendo acessado com perfil filial e pelo Menu WEB > Frete Carreteiro Viagem > Recibos > Manter Recibos Complementares ou Menu WEB > Frete Carreteiro Coleta/Entrega > Notas e Recibos > Manter Recibos Coleta/Entrega > Botão Recibo Complementar, trazer habilitado nas seguintes situações:
 * <br>Emitido;
 * <br>Bloqueado;
 * <br>Pago;
 * <br>Rejeitado no evento de workflow número 2402;
 * <br>Aguardando assinaturas;
 * <br>Enviado JDE.
 * <p>Caso o recibo esteja sendo acessado através de Menu WEB > Workflow > Cadastros Gerais > Manter Ações > aba Detalhamento > Visualizar processo, desabilitar o botão .</p>
 * </p>
 * 
 * @author AlessandroSF
 *
 */
public class ReciboComplementarHelperBotaoEmitirTest  {


	    @BeforeTest
	    protected void setup(){
	    }
	
	    
		@Test
		public void isHabilidaEmitirFilialEmitido() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EMITIDO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirFilialBloqueado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_BLOQUEADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirFilialPago() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_PAGO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirFilialRejeitado2404() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),true);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirFilialRejeitado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirFilialAguardandoAssinaturas() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ASSINATURAS.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirFilialAguardandoEnvioJDE() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirFilialEnviadoJDE() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		
		/*Matriz*/
		
		@Test
		public void isHabilidaEmitirMatrizEmitido() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EMITIDO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirMatrizBloqueado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_BLOQUEADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirMatrizPago() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_PAGO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirMatrizRejeitado2404() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),true);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirMatrizRejeitado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirMatrizAguardandoAssinaturas() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ASSINATURAS.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirMatrizAguardandoEnvioJDE() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirMatrizEnviadoJDE() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		
		@Test
		public void isHabilidaEmitirWorkflow() throws Exception {
			Boolean matriz = false;
			Boolean workflow = true;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_APROVACAO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirStatusCancelado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_CANCELADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}

		@Test
		public void isHabilidaEmitirStatusEmRelacaoPagamento() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_RELACAO_PAGAMENTO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}
		
		@Test
		public void isHabilidaEmitirStatusRejeitado2401() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),true);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));
		}

		

		
}
