package com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.helper;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants.ReciboHelperConstants;

/**
 * <b>Classe que testa a classe utilitaria ReciboComplementarHelper</b>
 * <i>Separada por habilita��o dos botoes, para ficar mais simples o entendimento</i>
 * <p><b>REGRA:</b></p> 
Campo obrigat�rio. Caso o recibo esteja sendo acessado com perfil filial e pelo Menu WEB > Frete Carreteiro Viagem > Recibos > Manter Recibos Complementares ou Menu WEB > Frete Carreteiro Coleta/Entrega > Notas e Recibos > Manter Recibos Coleta/Entrega > Bot�o Recibo Complementar:
Habilitar somente se o recibo estiver nas seguintes situa��es:
�	Gerado;
�	Rejeitado no Workflow n�mero 2401.

Caso o recibo esteja sendo acessado com perfil MTZ, trazer sempre desabilitado.

Caso o recibo esteja sendo acessado com perfil filial atrav�s de Menu WEB > Workflow > Cadastros Gerais > Manter A��es > aba Detalhamento > Visualizar processo, trazer sempre desabilitado.

Caso esteja sendo acessado com perfil MTZ, trazer habilitado somente se o recibo estiver na seguinte situa��o:
�	Em aprova��o do workflow 2401.


 * 
 * @author AlessandroSF
 *
 */
public class ReciboComplementarHelperBotaoNotaFiscalTest  {


	    @BeforeTest
	    protected void setup(){
	    }
	
		@Test
		public void isHabilitaNFCarreteiroFilialGerado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_GERADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroMatrizWorkflow() throws Exception {
			Boolean matriz = true;
			Boolean workflow = true;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_APROVACAO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		
		@Test
		public void isHabilitaNFCarreteiroFilialRejeitado2404() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),true);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroFilialRejeitado2401() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),true);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroFilialRejeitado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		
		
		@Test
		public void isHabilitaNFCarreteiroMatrizGerado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_GERADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroMatrizCancelado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_CANCELADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroMatrizPago() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_PAGO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		
	
		@Test
		public void isHabilitaNFCarreteiroFilialEmitido() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EMITIDO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroFilialBloqueado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_BLOQUEADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroFilialPago() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_PAGO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		
		@Test
		public void isHabilitaNFCarreteiroFilialAguardandoAssinaturas() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ASSINATURAS.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroFilialAguardandoEnvioJDE() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroFilialEnviadoJDE() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroMatrizEmitido() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EMITIDO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroMatrizBloqueado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_BLOQUEADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		
		@Test
		public void isHabilitaNFCarreteiroMatrizRejeitado2404() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),true);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroMatrizRejeitado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroMatrizAguardandoAssinaturas() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ASSINATURAS.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroMatrizAguardandoEnvioJDE() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroMatrizEnviadoJDE() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		
		@Test
		public void isHabilitaNFCarreteiroWorkflowFilial() throws Exception {
			Boolean matriz = false;
			Boolean workflow = true;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_APROVACAO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroWorkflowMatriz() throws Exception {
			Boolean matriz = false;
			Boolean workflow = true;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_APROVACAO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroStatusCancelado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_CANCELADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		@Test
		public void isHabilitaNFCarreteiroStatusEmRelacaoPagamento() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_RELACAO_PAGAMENTO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
		@Test
		public void isHabilitaNFCarreteiroStatusRejeitado2401() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),true);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		}
		
}
