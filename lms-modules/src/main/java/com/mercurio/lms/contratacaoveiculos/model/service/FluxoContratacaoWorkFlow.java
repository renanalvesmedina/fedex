package com.mercurio.lms.contratacaoveiculos.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

public abstract class FluxoContratacaoWorkFlow {

	private static final String LMS_26158 = "LMS-26158";
	protected static final String TIPO_OPERACAO_COLETA_ENTREGA = "CE";
	private static final String PARAMETRO_FILIAL = "VALIDA_CONTRATO_MT";
	private static final String SIM = "S";
	
	private ConteudoParametroFilialService conteudoParametroFilialService;
	

	protected boolean isLiberaValidacao() {
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		 if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			 return true;
		 }
		return false;
	}
	
	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	
	protected boolean gerarNovaPendencia(Pendencia pendencia, boolean isCadastroNovo) {
		if(isCadastroNovo){
			return true;
		}
		if (pendencia != null && (ConstantesWorkflow.REPROVADO).equals(pendencia.getTpSituacaoPendencia().getValue())) {
			return true;
		}
		return false;
	}
	
	/**
	 * <b>Valida se possui tipo operacao</b>
	 * @param tpOperacao
	 */
	protected void verificarTipoOperacao(DomainValue tpOperacao) {
		if(tpOperacao == null){
			throw new BusinessException(LMS_26158);
		}		
	}
}
