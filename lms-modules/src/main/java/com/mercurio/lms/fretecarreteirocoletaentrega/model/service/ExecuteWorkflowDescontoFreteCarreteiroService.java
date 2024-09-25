package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DescontoRfc;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 */
public class ExecuteWorkflowDescontoFreteCarreteiroService {

	private static final String PROGRAMADO = "P";

	private static final String DOMINIO_STATUS_WORKFLOW = "DM_STATUS_WORKFLOW";

	private static final String WORFLOW_REJEITADO = "R";
	private static final String WORFLOW_EM_APROVACAO = "E";

	private static final String INATIVO = "I";

	private static final Object WORFLOW_APROVADO = "A";
	
	private DescontoRfcService descontoRfcService;
	private WorkflowPendenciaService workflowPendenciaService;
	private DomainValueService domainValueService;
	private ConfiguracoesFacade configuracoesFacade;
	
	/**
	 * Executado pela tela de manter ações
	 * 
	 * @param ids
	 * @param situacoes
	 * @return
	 */
	public String executeWorkflow(List<Long> ids, List<String> situacoes) {
		for (int i = 0; i < ids.size(); i++) {

			Long id = (Long) ids.get(i);
			String tpSituacaoAprovacao = (String) situacoes.get(i);

			DescontoRfc desconto = descontoRfcService.findById(id);

			desconto.setTpSituacaoPendencia(new DomainValue(tpSituacaoAprovacao));
		
			if (WORFLOW_APROVADO.equals(tpSituacaoAprovacao)) {
				DomainValue tpSituacao = new DomainValue(PROGRAMADO);
				
				desconto.setTpSituacao(tpSituacao); 
			}

			descontoRfcService.store(desconto);
		}

		return null;
	}


	/**
	 * Executado na tela de manter DescontoRfc
	 * 
	 * @param idDescontoRfc
	 * @param observacao
	 * @param usuario
	 * @return
	 */
	public DescontoRfc executeWorkflowPendencia(DescontoRfc descontoRfc) {    	
    	
    	DomainValue rejeitado = domainValueService.findDomainValueByValue(DOMINIO_STATUS_WORKFLOW, WORFLOW_REJEITADO);
    	//se já possui uma pendencia que esta aguardando aprovação não cria outra.
    	if(descontoRfc.getPendencia() == null || (descontoRfc.getPendencia() != null && rejeitado.equals(descontoRfc.getPendencia().getTpSituacaoPendencia()))){
    		Pendencia pendencia = executeWorkflowPendenciaWorkFlow(descontoRfc);
    		
    		descontoRfc.setPendencia(pendencia);  
    		
    		DomainValue tpSituacao = new DomainValue(INATIVO);
			
    		descontoRfc.setTpSituacao(tpSituacao); 
    		
    		descontoRfc.setTpSituacaoPendencia(new DomainValue(WORFLOW_EM_APROVACAO));
    		
        	descontoRfcService.store(descontoRfc);
    	}
    	    
    	return descontoRfc;
    }
    
	
	private Pendencia executeWorkflowPendenciaWorkFlow(DescontoRfc reciboFreteCarreteiro) {
		Short nrTipoEvento = ConstantesWorkflow.NR2506_APROVACAO_DESCONTO_FRETE_CARRETEIRO;
		Pendencia pendencia = null;
		String mensagemWorkflow = getDsProcessoWorkflowParaDescontoRfc(reciboFreteCarreteiro);

		pendencia = workflowPendenciaService.generatePendencia(reciboFreteCarreteiro.getFilial().getIdFilial(), nrTipoEvento, reciboFreteCarreteiro.getIdDescontoRfc(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
		return pendencia;
	}
	
	private String getDsProcessoWorkflowParaDescontoRfc(DescontoRfc descontoRfc) {
		List<String> parametros = new ArrayList<String>();
		
		parametros.add(descontoRfc.getProprietario().getPessoa().getNrIdentificacao());
		parametros.add(descontoRfc.getProprietario().getPessoa().getNmPessoa());
		
		return configuracoesFacade.getMensagem("LMS-25083", parametros.toArray());
		
	}
	

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setDescontoRfcService(DescontoRfcService descontoRfcService) {
		this.descontoRfcService = descontoRfcService;
	}
}