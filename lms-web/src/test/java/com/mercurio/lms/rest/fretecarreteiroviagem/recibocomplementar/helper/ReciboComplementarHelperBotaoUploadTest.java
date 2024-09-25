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
 Caso o recibo esteja sendo acessado com perfil filial e pelo Menu WEB > Frete Carreteiro Viagem > Recibos > Manter Recibos Complementares ou Menu WEB > Frete Carreteiro Coleta/Entrega > Notas e Recibos > Manter Recibos Coleta/Entrega > Botão Recibo Complementar:
Habilitar somente se o recibo estiver nas seguintes situações:
•	Gerado;
•	Rejeitado no workflow 2401.
•	Emitido;

Caso esteja sendo acessado com perfil MTZ, trazer sempre desabilitado.

Caso o recibo esteja sendo acessado através de Menu WEB > Workflow > Cadastros Gerais > Manter Ações > aba Detalhamento > Visualizar processo, pela filial, trazer sempre desabilitado. Caso a tela esteja sendo acessada pela MTZ e a pendência de workflow esteja com status “Em aprovação”, trazer habilitada.

 * 
 * @author AlessandroSF
 *
 */
public class ReciboComplementarHelperBotaoUploadTest  {


	    @BeforeTest
	    protected void setup(){
	    }
	
		@Test
		public void isDesabilitaObservacaoFilialGerado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_GERADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoMatrizWorkflow() throws Exception {
			Boolean matriz = true;
			Boolean workflow = true;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_APROVACAO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		
		@Test
		public void isDesabilitaObservacaoFilialRejeitado2404() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),true);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoFilialRejeitado2401() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),true);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoFilialRejeitado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		
		
		@Test
		public void isDesabilitaObservacaoMatrizGerado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_GERADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoMatrizCancelado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_CANCELADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoMatrizPago() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_PAGO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		
	
		@Test
		public void isDesabilitaObservacaoFilialEmitido() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EMITIDO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoFilialBloqueado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_BLOQUEADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoFilialPago() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_PAGO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		
		@Test
		public void isDesabilitaObservacaoFilialAguardandoAssinaturas() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ASSINATURAS.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoFilialAguardandoEnvioJDE() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoFilialEnviadoJDE() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoMatrizEmitido() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EMITIDO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoMatrizBloqueado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_BLOQUEADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		
		@Test
		public void isDesabilitaObservacaoMatrizRejeitado2404() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),true);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoMatrizRejeitado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoMatrizAguardandoAssinaturas() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ASSINATURAS.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoMatrizAguardandoEnvioJDE() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoMatrizEnviadoJDE() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		
		@Test
		public void isDesabilitaObservacaoWorkflowFilial() throws Exception {
			Boolean matriz = false;
			Boolean workflow = true;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_APROVACAO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoWorkflowMatriz() throws Exception {
			Boolean matriz = false;
			Boolean workflow = true;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_APROVACAO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoStatusCancelado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_CANCELADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		@Test
		public void isDesabilitaObservacaoStatusEmRelacaoPagamento() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_RELACAO_PAGAMENTO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
		@Test
		public void isDesabilitaObservacaoStatusRejeitado2401() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),true);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		}
		
}
