package com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.helper;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants.ReciboHelperConstants;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants.ReciboRestConstants;
import com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.dto.ReciboComplementarDTO;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Responsável pela manipulação de validações de dados do rest.
 * 
 */
public class ReciboComplementarRestHelper {

	/**
	 * Classe utilitária não deve ser instanciada.
	 */
	private ReciboComplementarRestHelper() {

	}

	/***
	 *  <p><b>Botão Emitir trazer habilitado nas seguintes situações:</b></p>
	 *<i>	
	 *	<ul>
	 *  <li>Emitido</li>
	 *	<li>Bloqueado</li>
	 *	<li>Pago</li>
	 *	<li>Rejeitado no evento de workflow número 2402</li>
	 *	<li>Aguardando assinaturas</li>
	 *	<li>Aguardando envio JDE</li>
	 *	<li>Enviado JDE.
	 *	</ul>
	 *</i>
	 *	<p><b>Caso o recibo esteja sendo acessado através do Workflow desabilitar o botão.</b></p>
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isHabilitaEmitir(Map<String, Boolean> situacoes) {
		String[] valores = { 
				ReciboHelperConstants.STATUS_EMITIDO.getValue(),
				ReciboHelperConstants.STATUS_BLOQUEADO.getValue(),
				ReciboHelperConstants.STATUS_PAGO.getValue(),
				ReciboHelperConstants.STATUS_ASSINATURAS.getValue(),
				ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue(),
				ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue(),
				};
		boolean isWorkFlow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		
		String[] valoresRejeitado = { 				
				ReciboHelperConstants.EVENTO_2402.getValue(),				
				};
		
		boolean isRejeitado = situacoes.get(ReciboHelperConstants.STATUS_REJEITADO.getValue());
		
		return  !isWorkFlow && (hasKeys(situacoes, valores) || (!isWorkFlow && isRejeitado && hasKeys(situacoes, valoresRejeitado)));
	}

	/**
	 * <p><b>Trazer sempre habilitado, exceto se o Recibo complementado estiver cancelado.</b></p>
	 * 
	 * <p><b>Caso o recibo esteja sendo acessado através do Workflow desabilitar o botão.</b></p>
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
	 * <p><b>Trazer sempre habilitado, exceto se o Recibo complementado estiver cancelado.</b></p>
	 * 
	 * <p><b>Caso o recibo esteja sendo acessado através do Workflow desabilitar o botão.</b></p>
	 * @param situacoes
	 * @return
	 */
	public static Boolean isDesabilitacontroleCarga(Map<String, Boolean> situacoes) {
		String[] valores = { 
				ReciboHelperConstants.STATUS_CANCELADO.getValue(),
				ReciboHelperConstants.WORKFLOW.getValue() 
				};
		return hasKeys(situacoes, valores);
	}	
	
	/***
	 *  <p><b>Caso o recibo esteja sendo acessado com perfil Filial trazer sempre desabilitado, exceto se o recibo estiver nas seguintes situações:</b></p>
	 *<i>	
	 *	<ul>
	 *  <li>Gerado</li>
	 *	<li>Rejeitado no evento de workflow número 2401</li>
	 *	</ul>
	 *</i>
	 *	<p><b>Caso esteja sendo acessado pelo perfil MTZ, trazer sempre habilitado, exceto se o recibo estiver nas seguintes situações:</b></p>
	 *<i>	
	 *	<ul>
	 *  <li>Cancelado</li>
	 *	<li>Pago</li>
	 *	</ul>
	 *</i>
	 *  <p><b>Caso o recibo esteja sendo acessado através de Menu WEB > Workflow desabilitar o botão.</b></p>
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isHabilitaBotaoCancelar(Map<String, Boolean> situacoes) {
		String[] valoresMatriz = { 
				ReciboHelperConstants.STATUS_CANCELADO.getValue(),
				ReciboHelperConstants.STATUS_PAGO.getValue() 
				};
		
		boolean isWorkflow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isfilial = !situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isGerado = situacoes.get(ReciboHelperConstants.STATUS_GERADO.getValue());
		
		return (!hasKeys(situacoes, valoresMatriz) && isMatriz  && !isWorkflow ) ||  isGeradoFilial(isGerado, isfilial) ;
	}	
	
	/***
	 * <i>REGRA COMPARTILHADA POR SALVAR E LIMPAR</i>
	 * 
	 *  <p><b> Se filial Habilitar somente se o recibo estiver nas seguintes situações:</b></p>
	 *<i>	
	 *	<ul>
	 *  <li>Gerado</li>
	 *	<li>Rejeitado</li>
	 *  <li>Aguardando assinaturas</li>
	 *	</ul>
	 *</i>
	 *	<p><b>Caso esteja sendo acessado pelo perfil MTZ, trazer habilitado somente se o recibo estiver:</b></p>
	 *<i>	
	 *	<ul>
	 *  <li>Em aprovação</li>
	 *	</ul>
	 *</i>
	 *  <p><b>Caso o recibo esteja sendo acessado através de Menu WEB > Workflow desabilitar o botão.</b></p>
	 * 
	 * @param situacoes
	 * @return
	 * 
	 * 
	 * 
	 */
	public static Boolean isHabilitaBotaoSalvarLimpar(Map<String, Boolean> situacoes) {
		String[] valoresDesabilitado = {
				ReciboHelperConstants.STATUS_EMITIDO.getValue(),
				ReciboHelperConstants.STATUS_GERADO.getValue(),
				ReciboHelperConstants.STATUS_REJEITADO.getValue(),
				ReciboHelperConstants.STATUS_ASSINATURAS.getValue()
				};

		boolean isWorkFlow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isFilial = !isMatriz;
		boolean isEmAprovacao = situacoes.get(ReciboHelperConstants.STATUS_EM_APROVACAO.getValue());
		boolean isAguardandoEnvioJDE = situacoes.get(ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue());

		if(isWorkFlow && isEmAprovacao && isMatriz){
			return true;
		}
		
		if(isMatriz && isAguardandoEnvioJDE){
			return true;
		}
		
		return isFilial && !isWorkFlow && hasKeys(situacoes, valoresDesabilitado);
	}	
	
	/**
	 *  <p><b> Se filial Habilitar somente se o recibo estiver nas seguintes situações:</b></p>
	 *<i>	
	 *	<ul>
	 *  <li>Gerado</li>
	 *	<li>Rejeitado no Workflow número 2401</li>
	 *	</ul>
	 *</i>
	 *	<p><b>Caso o recibo esteja sendo acessado com perfil MTZ, trazer sempre desabilitado</b></p>
	 *</i>
	 *  <p><b>Caso o recibo esteja sendo acessado através de Menu WEB > Workflow trazer habilitado somente se o recibo estiver na seguinte situação:</b></p>
	 *  <ul>
	 *  <li>Gerado</li>
	 *	<li>Em aprovação do workflow 2401</li>
	 *	</ul>
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isHabilitaNFCarreteiro(Map<String, Boolean> situacoes) {
			
		boolean isGerado = situacoes.get(ReciboHelperConstants.STATUS_GERADO.getValue());
		boolean isWorkflow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isEmAprovacao = situacoes.get(ReciboHelperConstants.STATUS_EM_APROVACAO.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isEvento2401 = situacoes.get(ReciboHelperConstants.EVENTO_2401.getValue());
		boolean isFilial = !isMatriz;
		boolean isRejeitado = situacoes.get(ReciboHelperConstants.STATUS_REJEITADO.getValue());
		
		return isGeradoFilial(isGerado, isFilial) || isFilialRejeitado2401(isEvento2401, isFilial, isRejeitado) || isMatriz2401Aprovacao(isWorkflow, isEmAprovacao, isMatriz, isEvento2401);
	}

	/**
	 * @param isWorkflow
	 * @param isEmAprovacao
	 * @param isMatriz
	 * @param isEvento2401
	 * @return
	 */
	private static boolean isMatriz2401Aprovacao(boolean isWorkflow,
			boolean isEmAprovacao, boolean isMatriz, boolean isEvento2401) {
		return isMatriz && isEmAprovacao && isWorkflow && isEvento2401;
	}

	/**
	 * @param isEvento2401
	 * @param isFilial
	 * @param isRejeitado
	 * @return
	 */
	private static boolean isFilialRejeitado2401(boolean isEvento2401,
			boolean isFilial, boolean isRejeitado) {
		return isFilial && isRejeitado && isEvento2401;
	}

	/**
	 * @param isGerado
	 * @param isFilial
	 * @return
	 */
	private static boolean isGeradoFilial(boolean isGerado, boolean isFilial) {
		return isFilial && isGerado;
	}
	
	/**
	 *  <p><b> Se filial Habilitar somente se o recibo estiver nas seguintes situações:</b></p>
	 *<i>	
	 *	<ul>
	 *  <li>Gerado</li>
	 *	<li>Rejeitado no evento 2401</li>
	 *	</ul>
	 *</i>
	 *	<p><b>Caso o recibo esteja sendo acessado com perfil MTZ, trazer sempre desabilitado</b></p>
	 *</i>
	 *  <p><b>Caso o recibo esteja sendo acessado através de Menu WEB > Workflow trazer habilitado somente se o recibo estiver na seguinte situação:</b></p>
	 *  <ul>  
	 *	<li>Em aprovação</li>
	 *	</ul>
	 * 
	 * @param situacoes
	 * @return
	 */
	public static Boolean isHabilitaObservacao(Map<String, Boolean> situacoes) {

		boolean isWorkFlow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isAprovacao = situacoes.get(ReciboHelperConstants.STATUS_EM_APROVACAO.getValue());
		boolean isGerado = situacoes.get(ReciboHelperConstants.STATUS_GERADO.getValue());
		boolean isEvento2401 = situacoes.get(ReciboHelperConstants.EVENTO_2401.getValue());
		boolean isRejeitado = situacoes.get(ReciboHelperConstants.STATUS_REJEITADO.getValue());
		boolean isFilial = !isMatriz; 
		
		boolean isFilialGerado = isFilial && isGerado;
		boolean isFilialRejeitadoEvento2401 = isFilial && isRejeitado && isEvento2401;			
		boolean isMatrizWorkflowEmAprovacao = isMatriz && isWorkFlow && isAprovacao;
		
		return isFilialGerado || isFilialRejeitadoEvento2401 || isMatrizWorkflowEmAprovacao;
	}

	/***
	 *  <p><b>Caso o recibo esteja sendo acessado com perfil Filial trazer sempre desabilitado, exceto se o recibo estiver nas seguintes situações:</b></p>
	 *<i>	
	 *	<ul>
	 *  <li>Gerado</li>
	 *	<li>Rejeitado no evento de workflow número 2401</li>
	 *	</ul>
	 *</i>
	 *	<p><b>Caso esteja sendo acessado pelo perfil MTZ, trazer sempre desabilitado</b></p>
	 *</i>
	 *  <p><b>Caso a tela esteja sendo acessada pela MTZ e a pendência de workflow esteja com status “Em aprovação”, trazer habilitada.</b></p>
	 * 
	 * @param situacoes
	 * @return
	 */	
	public static Boolean isHabilitaTxtMoeda(Map<String, Boolean> situacoes) {
		boolean isEmAprovacao = situacoes.get(ReciboHelperConstants.STATUS_EM_APROVACAO.getValue());
		boolean isGerado = situacoes.get(ReciboHelperConstants.STATUS_GERADO.getValue());
		boolean is2401 = situacoes.get(ReciboHelperConstants.EVENTO_2401.getValue());
		boolean isWorkflow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isFilial = !situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isRejeitado = situacoes.get(ReciboHelperConstants.STATUS_REJEITADO.getValue());
		
		boolean isFilialGerado = isFilial && isGerado;
		boolean isFilialRejeitadoEvento2401 = isFilial && isRejeitado &&  is2401;
		boolean isMatrizWorkflowStatusAprovacao = isMatriz && isWorkflow && isEmAprovacao;
		
		return  isFilialGerado || isFilialRejeitadoEvento2401 || isMatrizWorkflowStatusAprovacao;
	}

	/***
	 *  <p><b>Caso o recibo esteja sendo acessado com perfil Filial trazer sempre desabilitado, exceto se o recibo estiver nas seguintes situações:</b></p>
	 *<i>	
	 *	<ul>
	 *  <li>Gerado</li>
	 *  <li>Emitido</li>
	 *	<li>Rejeitado</li>
	 *	<li>Aguardando assinaturas</li>
	 *	</ul>
	 *</i>
	 *	<p><b>Caso esteja sendo acessado pelo perfil MTZ, trazer sempre desabilitado</b></p>
	 *</i>
	 *  <p><b>Caso o recibo esteja sendo acessado através de Menu WEB > Workflow desabilitar o botão.</b></p>
	 * 
	 * @param situacoes
	 * @return
	 */	
	public static Boolean isHabilitaAnexos(Map<String, Boolean> situacoes) {
		String[] valores = { 
				ReciboHelperConstants.STATUS_EMITIDO.getValue(),
				ReciboHelperConstants.STATUS_GERADO.getValue(),
				ReciboHelperConstants.STATUS_REJEITADO.getValue(),
				ReciboHelperConstants.STATUS_ASSINATURAS.getValue(),
				};
		
		boolean isWorkflow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isFilial = !situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		
		return isFilial && !isWorkflow && hasKeys(situacoes, valores);
	}
	
	/***
	 *  <p><b>Trazer sempre desabilitado, exceto se o usuário estiver logado como MTZ  > Workflow e situação:</b></p>
	 *<i>	
	 *	<ul>
	 *  <li>Em aprovação no workflow 2402.</li>
	 *	</ul>
	 *</i>
	 * @param situacoes
	 * @return
	 */	
	public static Boolean isHabilitaDataProgramada(	Map<String, Boolean> situacoes) {
		boolean isWorkFlow = situacoes.get(ReciboHelperConstants.WORKFLOW.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		boolean isEmAprovacao = situacoes.get(ReciboHelperConstants.STATUS_EM_APROVACAO.getValue());
		boolean isEvento2402 = situacoes.get(ReciboHelperConstants.EVENTO_2402.getValue());
		
		return isWorkFlow && isMatriz && isEmAprovacao && isEvento2402;
	}
	
	/**
	 * Metodo que verifica se as chaves estão nas situações
	 * 
	 * @param situacoes
	 * @param valores
	 * @return
	 */
	private static boolean hasKeys(Map<String, Boolean> situacoes,
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

	/**
	 * Validação da data de pagamento e do valorBruto
	 * @param ReciboComplementarDTO reciboComplementarDTO 
	 * @param reciboFreteCarreteiro
	 */
	public static void validaReciboComplementar(
			ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {
		validaValorBruto(reciboFreteCarreteiro);
		validaDataPagamentoReal(reciboFreteCarreteiro);
		validaDataProgramada(reciboFreteCarreteiro);
		validaReciboComplementado(reciboComplementarDTO, reciboFreteCarreteiro);
	}

	private static void validaDataPagamentoReal(ReciboFreteCarreteiro reciboFreteCarreteiro) {
		YearMonthDay dtPagto = reciboFreteCarreteiro.getDtPagtoReal();
		if (dtPagto != null) {
			YearMonthDay dtBase = JTDateTimeUtils.getDataAtual();
			dtBase = dtBase.minusMonths(1);

			if (dtPagto.isBefore(dtBase)) {
				throw new BusinessException("LMS-24012");
			}
		}
	}

	private static void validaValorBruto(ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if (CompareUtils.le(reciboFreteCarreteiro.getVlBruto(), BigDecimalUtils.ZERO)){
			throw new BusinessException("LMS-24011");			
		}
	}
		
	private static void validaDataProgramada(ReciboFreteCarreteiro reciboFreteCarreteiro) {
		YearMonthDay dtPagto = reciboFreteCarreteiro.getDtProgramadaPagto();
		DateTime dtEmissao = reciboFreteCarreteiro.getDhEmissao();
		
		if (dtPagto != null && dtEmissao != null) {
			DateTime dtimePagto = JTDateTimeUtils.yearMonthDayToDateTime(dtPagto);
			if(dtEmissao.isAfter(dtimePagto)){
				throw new BusinessException("LMS-25080");
			}

		}
	}

	public static Boolean bloqueiaViagem(Map<String, Boolean> situacoes) {
		boolean isGerado = situacoes.get(ReciboHelperConstants.STATUS_GERADO.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		return !isGeradoFilial(isGerado, isMatriz);
	}

	public static Boolean isCancelado(Map<String, Boolean> situacoes) {
		return situacoes.get(ReciboHelperConstants.STATUS_CANCELADO.getValue());
	}

	public static Boolean isDesabilitaEmitirViagem(Map<String, Boolean> situacoes) {
		boolean isGerado = situacoes.get(ReciboHelperConstants.STATUS_GERADO.getValue());
		boolean isEmitido = situacoes.get(ReciboHelperConstants.STATUS_EMITIDO.getValue());
		boolean isMatriz = situacoes.get(ReciboHelperConstants.MATRIZ.getValue());
		return !(isMatriz && (isEmitido || isGerado));
	}
	
	/**
	 * Valida se a filial logada e o tipo de recibo são validos para a criacao
	 * 
	 * @param findReciboSuggest
	 */
	private static void validaReciboComplementado(ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {		
		String sgFilialLogado = SessionUtils.getFilialSessao().getSgFilial();
		String sgRecibo = reciboFreteCarreteiro.getReciboComplementado().getFilial().getSgFilial();		
		String tpReciboFreteCarreteiro = reciboFreteCarreteiro.getTpReciboFreteCarreteiro().getValue(); 
		
		boolean isMatriz = ReciboRestConstants.MTZ.getValue().equals(sgFilialLogado);
		boolean isColetaEntrega = "C".equals(tpReciboFreteCarreteiro);
				
		if(!isColetaEntrega && !isMatriz){
			throw new BusinessException(ReciboRestConstants.LMS_24044.getValue()); 
		}
		
		if(!isMatriz && !sgFilialLogado.equals(sgRecibo)){
			throw new BusinessException(ReciboRestConstants.LMS_24043.getValue()); 
		}
		
		if(isColetaEntrega){
			validaSituacaoReciboOriginal(reciboComplementarDTO, isMatriz);			
			validaAlteracaoReciboComplementarJDE(reciboComplementarDTO, isMatriz);
		}
	}
	
	/**
	 * Apenas deve permitir a alteração do status pela matriz de um recibo
	 * complementar para Aguardando Envio JDE, Enviado JDE ou Pago, quando
	 * esteja em situação igual à Aguardando Envio JDE.
	 * 
	 * @param reciboComplementarDTO
	 * @param isMatriz
	 */
	private static void validaAlteracaoReciboComplementarJDE(ReciboComplementarDTO reciboComplementarDTO, boolean isMatriz){
		/*
		 * Logo se o recibo complementar é novo não é necessário validar a situação
		 */
		if(reciboComplementarDTO.getBtSituacao() == null || reciboComplementarDTO.getIdReciboFreteCarreteiro() == null){
			return;
		}
				
		if(!reciboComplementarDTO.getBtSituacao()){
			Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(reciboComplementarDTO.getTpSituacaoRecibo().getValue(), false, isMatriz);
			boolean	isAguardandoEnvioJDE = situacoes.get(ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue());
			boolean	isEnviadoJDE = situacoes.get(ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue());
			boolean	isPago = situacoes.get(ReciboHelperConstants.STATUS_PAGO.getValue());
			
			if(!isAguardandoEnvioJDE && !isEnviadoJDE && !isPago){
				throw new BusinessException("LMS-24049");
			}
		}
	}
	
	/**
	 * Não deve permitir incluir novo Recibo Complementar para os Recibos
	 * Originais com situação diferente de: Aguardando Envio JDE, Enviado JDE e
	 * Pago.
	 * 
	 * @param reciboComplementarDTO
	 * @param isMatriz
	 */
	private static void validaSituacaoReciboOriginal(ReciboComplementarDTO reciboComplementarDTO, boolean isMatriz) {
		Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(reciboComplementarDTO.getReciboComplementado().getTpSituacaoRecibo().getValue(), false, isMatriz);
		boolean	isAguardandoEnvioJDE = situacoes.get(ReciboHelperConstants.STATUS_AGUARDANDO_ENVIO_JDE.getValue());
		boolean	isEnviadoJDE = situacoes.get(ReciboHelperConstants.STATUS_ENVIADO_JDE.getValue());
		boolean	isPago = situacoes.get(ReciboHelperConstants.STATUS_PAGO.getValue());
		
		if(!isAguardandoEnvioJDE && !isEnviadoJDE && !isPago){
			throw new BusinessException(ReciboRestConstants.LMS_24046.getValue());
		}
	}
}