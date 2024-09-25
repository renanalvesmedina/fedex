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
 *- Salvar: Caso o recibo esteja sendo acessado com perfil filial e pelo Menu WEB > Frete Carreteiro Viagem > Recibos > Manter Recibos Complementares ou Menu WEB > Frete Carreteiro Coleta/Entrega > Notas e Recibos > Manter Recibos Coleta/Entrega > Botão Recibo Complementar:
Habilitar somente se o recibo estiver nas seguintes situações:
•	Gerado;
•	Rejeitado;
•	Aguardando assinaturas.
•	Emitido.


Caso esteja sendo acessado com perfil MTZ, trazer sempre desabilitado.

Caso o recibo esteja sendo acessado com perfil da filial e através de Menu WEB > Workflow > Cadastros Gerais > Manter Ações > aba Detalhamento > Visualizar processo, trazer sempre desabilitado.

Caso esteja sendo acessado com perfil MTZ, trazer habilitado somente se o recibo estiver:
•	 Em aprovação.

 * 
 * @author AlessandroSF
 *
 */
public class ReciboComplementarHelperBotaoSalvarTest  {


	    @BeforeTest
	    protected void setup(){
	    }
	
		@Test
		public void isHabilitaBotaoSalvarLimparFilialGerado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_GERADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizWorkflow() throws Exception {
			Boolean matriz = true;
			Boolean workflow = true;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_APROVACAO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		
		@Test
		public void isHabilitaBotaoSalvarLimparFilialRejeitado2404() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),true);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparFilialRejeitado2401() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),true);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparFilialRejeitado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizGerado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_GERADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizCancelado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_CANCELADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizPago() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_PAGO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		
	
		@Test
		public void isHabilitaBotaoSalvarLimparFilialEmitido() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EMITIDO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparFilialBloqueado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_BLOQUEADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparFilialPago() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_PAGO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		
		@Test
		public void isHabilitaBotaoSalvarLimparFilialAguardandoAssinaturas() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ASSINATURAS.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparFilialAguardandoEnvioJDE() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparFilialEnviadoJDE() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizEmitido() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EMITIDO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizBloqueado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_BLOQUEADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizRejeitado2404() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),true);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizRejeitado() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizAguardandoAssinaturas() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ASSINATURAS.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizAguardandoEnvioJDE() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparMatrizEnviadoJDE() throws Exception {
			Boolean matriz = true;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		
		@Test
		public void isHabilitaBotaoSalvarLimparWorkflowFilial() throws Exception {
			Boolean matriz = false;
			Boolean workflow = true;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_APROVACAO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparWorkflowMatriz() throws Exception {
			Boolean matriz = false;
			Boolean workflow = true;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_APROVACAO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparStatusCancelado() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_CANCELADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		@Test
		public void isHabilitaBotaoSalvarLimparStatusEmRelacaoPagamento() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_EM_RELACAO_PAGAMENTO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),false);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertFalse(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
		@Test
		public void isHabilitaBotaoSalvarLimparStatusRejeitado2401() throws Exception {
			Boolean matriz = false;
			Boolean workflow = false;
						 		
			String tpSituacaoRecibo = ReciboHelperConstants.STATUS_REJEITADO.getValue();
			
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);			
			situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(),true);
			situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(),false);
			
			Assert.assertTrue(ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		}
		
}
