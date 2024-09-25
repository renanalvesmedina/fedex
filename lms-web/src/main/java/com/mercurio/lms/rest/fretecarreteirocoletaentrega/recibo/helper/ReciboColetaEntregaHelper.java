package com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.helper;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants.ReciboHelperConstants;

/**
 * Responsável pela manipulação dos dados do rest.
 * 
 */
public class ReciboColetaEntregaHelper {

	/**
	 * Classe utilitária não deve ser instanciada.
	 */
	private ReciboColetaEntregaHelper() {

	}

	/**
	 * Trazer sempre habilitado, exceto se o Recibo complementado estiver
	 * cancelado.
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isDesabilitaOcorrencias(Map<String, Boolean> situacoes) {
		String[] valores = { 
				ReciboHelperConstants.STATUS_CANCELADO.getValue(),
				ReciboHelperConstants.WORKFLOW.getValue() 
				};
		return hasKeys(situacoes, valores);
	}

	/**
	 * Se a tela estiver sendo acessada pelo Menu Frete Carreteiro Coleta/Entrega e filial, 
	 * Somente habilitar se a situação do recibo for Aguardando Envio JDE (AJ) ou Enviado JDE (EJ) ou Pago (PA), 
	 * que serão as situações em que a filial poderá gerar um complementar .
	 * 
	 * Se a tela estiver sendo acessada pelo Menu Frete Carreteiro Coleta/Entrega e matriz,
	 * Trazer sempre habilitado, exceto se o recibo estiver cancelado (CA ).
	 * 
	 * Se a tela estiver sendo acessada pelo menu Workflow: Trazer sempre desabilitado.
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isDesabilitaBotaoComplementar(
			Map<String, Boolean> situacoes) {
		String[] valores = { 
				ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue(),
				ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue(),
				ReciboHelperConstants.STATUS_PAGO.getValue()
				};
		
		
		boolean isCancelado = situacoes.get( ReciboHelperConstants.STATUS_CANCELADO.getValue());
		boolean isWorkFlow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isFilial = !isMatriz;
		
		return isWorkFlow || (isMatriz && isCancelado)  || (isFilial && !hasKeys(situacoes, valores) );
	}

	/***
	 * Desabilitado se a situação do recibo for CA,BL, Workflow
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isHabilitaEmitir(Map<String, Boolean> situacoes) {
		String[] valores = { 
				ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue(),
				ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue(),
				ReciboHelperConstants.STATUS_PAGO.getValue(),
				};
		return hasKeys(situacoes, valores);
	}

	/**
	 * Quando a tela for acessada pela MTZ > Workflow > trazer habilitado 
	 * somente se a situação do recibo for “Em aprovação” (RECIBO_FRETE_CARRETEIRO.TP_SITUACAO_RECIBO = ‘EA’)
	 * 
	 * Quando a tela for acessada pela MTZ > Manter Recibos de Coleta/Entrega,  trazer sempre desabilitado. 
	 * 
	 * Quando a tela for acessada pela filial > Workflow trazer SEMPRE desabilitado .
	 * 
	 * Quando a tela for acessada pela filial > Manter Recibos de Coleta/Entrega, 
	 * trazer habilitado somente de o recibo estiver Emitido ou Rejeitado (RECIBO_FRETE_CARRETEIRO.TP_SITUACAO_RECIBO IN (‘EM’,’RE ’).
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isHabilitaBotaoSalvar(Map<String, Boolean> situacoes) {		
		return Boolean.TRUE;
	}

	/***	
	 * - Quando a tela for acessada pela filial, trazer o campo sempre desabilitado. 
	 * - Quando a tela for acessada pela MTZ e através do menu WEB > Workflow  trazer sempre desabilitado.
	 * - Quando a tela for acessada pela MTZ e através do menu WEB  Manter Recibos de Coleta/Entrega trazer 
	 * 	 habilitado se a situação do recibo for Emitido (RECIBO_FRETE_CARRETEIRO.TP_SITUACAO_RECIBO = ‘EM ’). 
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isHabilitaBotaoCancelar(Map<String, Boolean> situacoes) {
		
		boolean isRejeitado = situacoes.get(ReciboHelperConstants.STATUS_REJEITADO.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());		
		return isRejeitado && isMatriz ;
	}

	/**
	 * - Quando a tela for acessada pela filial > Manter Recibos de Coleta/Entrega, trazer o campo habilitado somente de o recibo estiver Emitido ou Rejeitado
	 * - Quando a tela for acessada pela filial > Workflow, trazer SEMPRE desabilitado .
	 * - Quando a tela for acessada pela MTZ trazer sempre desabilitado .
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isDesabilitaAnexos(Map<String, Boolean> situacoes) {
		
		boolean isWorkflow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		
		String[] valoresDesabilitado = {
				ReciboHelperConstants.STATUS_EM_APROVACAO.getValue(),
				ReciboHelperConstants.STATUS_REJEITADO.getValue() 
				};
		
		
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isFilial = !isMatriz;
		
		return isWorkflow || (!hasKeys(situacoes, valoresDesabilitado) && isFilial);
	}

	/***
	 * 
	 * - Quando a tela for acessada pela filial trazer SEMPRE desabilitado .
	 * - Quando a tela for acessada pela MTZ  > Manter Recibos de Coleta/Entrega, trazer SEMPRE desabilitada
	 * - Quando a tela for acessada pela MTZ  > Workflow, trazer habilitado somente se a situação do recibo for “Em aprovação” 
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isHabilitaDataProgramada(	Map<String, Boolean> situacoes) {
		boolean isWorkFlow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isEmAprovacao = situacoes.get(ReciboHelperConstants.STATUS_EM_APROVACAO.getValue());
		
		return isWorkFlow && isMatriz && isEmAprovacao;
	}

	/***
	 * - Quando a tela for acessada pela filial > Workflow, trazer SEMPRE desabilitado .
	 * - Quando a tela for acessada pela filial > Manter Recibos de Coleta/Entrega, trazer habilitado somente de o recibo estiver Emitido ou Rejeitado 
	 * - Quando a tela for acessada pela MTZ, trazer sempre desabilitado, independemente por qual menu tenha sido acessada .
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isDesabilitaObservacao(Map<String, Boolean> situacoes) {
		String[] valoresDesabilitado = {
				ReciboHelperConstants.STATUS_EMITIDO.getValue(),
				ReciboHelperConstants.STATUS_REJEITADO.getValue() 
				};

		boolean isWorkFlow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		
		return isMatriz || isWorkFlow || !hasKeys(situacoes, valoresDesabilitado);
	}

	/**
	 * - Quando a tela for acessada pela MTZ e através do > Workflow, trazer habilitado somente se a situação do recibo for Em aprovação (‘EA’).  
	 * - Quando a tela for acessada pela filial > Workflow, trazer SEMPRE desabilitado .
	 * - Quando a tela for acessada pela MTZ e através do > Manter Recibos de Coleta/Entrega trazer habilitado somente se o recibo estiver Emitido ou Rejeitado .
	 * - Quando a tela for acessada pela filial > Manter Recibos de Coleta/Entrega,trazer habilitado somente de o recibo estiver Emitido ou Rejeitado 
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isHabilitaNFCarreteiro(Map<String, Boolean> situacoes) {
		String[] valores = { 
				ReciboHelperConstants.STATUS_EMITIDO.getValue(),
				ReciboHelperConstants.STATUS_REJEITADO.getValue()
				 };
		
		boolean isWorkflow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isEmAprovacao = situacoes.get(ReciboHelperConstants.STATUS_EM_APROVACAO.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		
		return   (isMatriz && isEmAprovacao && isWorkflow) ||  hasKeys(situacoes, valores);
	}

	/**
	 * Metodo que verifica se as chaves estão nas situações
	 * 
	 * @param situacoes
	 * @param valores
	 * @return
	 */
	public static boolean hasKeys(Map<String, Boolean> situacoes,
			String[] valores) {
		for (String string : valores) {
			if (situacoes.get(string)) {
				return true;
			}
		}
		
		return false;
	}

	public static Map<String, Boolean> getSituacoes(String tpSituacaoRecibo, Boolean workflow, Boolean matriz) {
		Map<String, Boolean> situacoes = new HashMap<String, Boolean>();

		situacoes.put(ReciboHelperConstants.STATUS_CANCELADO.getValue(), ReciboHelperConstants.STATUS_CANCELADO.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_PAGO.getValue(), ReciboHelperConstants.STATUS_PAGO.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue(), ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_LIBERADO.getValue(), ReciboHelperConstants.STATUS_LIBERADO.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue(), ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_REJEITADO.getValue(), ReciboHelperConstants.STATUS_REJEITADO.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_EM_APROVACAO.getValue(), ReciboHelperConstants.STATUS_EM_APROVACAO.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_EMITIDO.getValue(), ReciboHelperConstants.STATUS_EMITIDO.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_GERADO.getValue(), ReciboHelperConstants.STATUS_GERADO.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_ASSINATURAS.getValue(), ReciboHelperConstants.STATUS_ASSINATURAS.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_BLOQUEADO.getValue(), ReciboHelperConstants.STATUS_BLOQUEADO.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.STATUS_EM_RELACAO_PAGAMENTO.getValue(), ReciboHelperConstants.STATUS_EM_RELACAO_PAGAMENTO.getValue().equals(tpSituacaoRecibo));
		situacoes.put(ReciboHelperConstants.WORKFLOW.getValue(), workflow);
		situacoes.put(ReciboHelperConstants.MATRIZ.getValue(), matriz);
		return situacoes;
	}

	public static Boolean isDesabilitaRim(Map<String, Boolean> situacoes) {
		String[] valores = { 
				ReciboHelperConstants.STATUS_CANCELADO.getValue()				
				};
		return hasKeys(situacoes, valores);
	}

	public static Boolean isDesabilitaNotasCredito(Map<String, Boolean> situacoes) {
		String[] valores = { 
				ReciboHelperConstants.STATUS_CANCELADO.getValue()				
				};
		return hasKeys(situacoes, valores);
	}

	public static Boolean isHabilitaComboSituacao(Map<String, Boolean> situacoes) {
		String[] valores = { 
				ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue(),
				ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue()
				 };
		
		boolean isWorkflow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		
		return   (isMatriz &&  !isWorkflow) && hasKeys(situacoes, valores);
	}
}