package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.BloqueioMotoristaProp;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
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
 * @spring.bean id="lms.indenizacoes.executeWorkflowMotoristaService"
 */
public class ExecuteWorkflowMotoristaService extends FluxoContratacaoWorkFlow{

	private static final String LMS_26143 = "LMS-26143";
	private static final String LMS_26149 = "LMS-26149";
	private static final String PESSOA_ATIVO = "A";
	
	private MotoristaService motoristaService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private BloqueioMotoristaPropService bloqueioMotoristaPropService;
	


	/**
	 * Executado pela tela de manter ações
	 * 
	 * @param ids
	 * @param situacoes
	 * @return
	 */
	public String executeWorkflow(List<Long> ids, List<String> situacoes) {
		for (int i = 0; i < ids.size(); i++) {
			Long id = ids.get(i);
			String tpSituacaoAprovacao = situacoes.get(i);

			Motorista motorista = motoristaService.findById(id);
			
			desbloqueiaRegistro(motorista);		
			
			if(ConstantesWorkflow.APROVADO.equals(tpSituacaoAprovacao)){
				motorista.setTpSituacao(new DomainValue(PESSOA_ATIVO));
			}		
			
			Usuario usuarioAprovador = SessionUtils.getUsuarioLogado();
			motorista.setUsuarioAprovador(usuarioAprovador);
			motorista.setTpSituacaoWorkflow(new DomainValue(tpSituacaoAprovacao));
			
			if(ConstantesWorkflow.REPROVADO.equals(tpSituacaoAprovacao)){
				motoristaService.storeWorkflow(motorista);
			}else{
				motoristaService.store(motorista);
			}
		}

		return null;
	}

	/**
	 * Executado na tela de manter Motorista
	 * @param isNovo 
	 * 
	 * 
	 * 
	 * @param proprietario
	 * @param alteracoes
	 * @return
	 */
	public void executeWorkflowPendencia(Motorista motorista, boolean isNovo) {
			
		Motorista motoristaWorkflow = motoristaService.findById(motorista.getIdMotorista());
		
		if(motoristaWorkflow == null){
			return;
		}
		
		verificarTipoOperacao(motorista.getTpOperacao());
		
		
		executeWorkflowPendenciaWorkFlow(motoristaWorkflow,isNovo);    		
	}
	

	private void executeWorkflowPendenciaWorkFlow(Motorista motorista, boolean isCadastroNovo) {
		
		if(!isLiberaValidacao()){
			return;
		}
		
		Pendencia pendencia = motorista.getPendencia();
		
		if( pendencia != null && (ConstantesWorkflow.EM_APROVACAO).equals(pendencia.getTpSituacaoPendencia().getValue())){
			return;		
		}else{
			boolean isColeta = TIPO_OPERACAO_COLETA_ENTREGA.equals(motorista.getTpOperacao().getValue());
			
			boolean isNovo = gerarNovaPendencia(pendencia, isCadastroNovo);
			
			bloqueiaRegistro(motorista,isNovo);
			
			Short nrTipoEvento =  getNrTipoEvento(isColeta,isNovo);
			
			String mensagemWorkflow = getDsProcessoWorkflowParaReciboFreteCarreteiro(motorista,isNovo);
			
			pendencia = workflowPendenciaService.generatePendencia(motorista.getFilial().getIdFilial(), nrTipoEvento, motorista.getIdMotorista(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
			
			motorista.setPendencia(pendencia);    	    
			
	    	motoristaService.store(motorista);			
		}
	}

	private Short getNrTipoEvento(boolean isColeta,boolean isNovo) {
		if(isColeta){			
			return getEventoColeta(isNovo);
		}else{
			return getEventoViagem(isNovo);
		}
	}

	private Short getEventoViagem(boolean isNovo) {
		if(isNovo){			
			return ConstantesWorkflow.NR2614_APROVACAO_CADASTRO_MOTORISTA_VI;
		}else{
			return ConstantesWorkflow.NR2620_ALTERACAO_CADASTRO_MOTORISTA_VI;								
		}
	}

	private Short getEventoColeta(boolean isNovo) {
		if(isNovo){
			return ConstantesWorkflow.NR2611_APROVACAO_CADASTRO_MOTORISTA_CE;
		}else{
			return ConstantesWorkflow.NR2617_ALTERACAO_CADASTRO_MOTORISTA_CE;									
		}
	}

	private String getDsProcessoWorkflowParaReciboFreteCarreteiro(Motorista motorista, boolean isNovo) {
		return getMensagemWorkflow(motorista,isNovo);
	}

	private String getMensagemWorkflow(Motorista motorista, boolean isNovo) {
		if(isNovo){
			return getMensagem(LMS_26149,motorista);		
		}else{
			return getMensagem(LMS_26143,motorista);			
		}
	}

	private String getMensagem(String chave, Motorista motorista) {
		return configuracoesFacade.getMensagem(chave, getParametros(motorista).toArray());
	}

	private List<String> getParametros(Motorista motorista) {
		List<String> parametros = new ArrayList<String>();			
		parametros.add(motorista.getPessoa().getNrIdentificacao());
		parametros.add(motorista.getPessoa().getNmPessoa());
		return parametros;
	}
	
	private void desbloqueiaRegistro(Motorista motorista) {
		Map<String, Object> dados = new HashMap<String, Object>();
		dados.put("idMotorista",String.valueOf(motorista.getIdMotorista()));
		@SuppressWarnings("rawtypes")
		Map bloqueios = bloqueioMotoristaPropService.findDadosBloqueioMotorista(dados);
		if(!bloqueios.isEmpty()){
			BloqueioMotoristaProp bloqueio = new BloqueioMotoristaProp();
			bloqueio.setIdBloqueioMotoristaProp(Long.valueOf(bloqueios.get("idBloqueioMotoristaProp").toString()));
			bloqueio.setDhVigenciaInicial((DateTime) bloqueios.get("dhVigenciaInicial"));
			bloqueio.setDhVigenciaFinal(JTDateTimeUtils.getDataHoraAtual());
			bloqueio.setMotorista(motorista);
			bloqueio.setObBloqueioMotoristaProp("Desbloqueio via workflow");
			bloqueioMotoristaPropService.storeBloqueio(bloqueio);			
		}
	}
	
	private boolean bloqueiaRegistro(Motorista motorista, boolean isNovo) {
		if(isNovo){
			BloqueioMotoristaProp bloqueio = new BloqueioMotoristaProp();
			bloqueio.setDhVigenciaInicial(JTDateTimeUtils.getDataHoraAtual());
			bloqueio.setMotorista(motorista);
			bloqueio.setObBloqueioMotoristaProp("Bloqueado, aguardando aprovação workflow.");
			bloqueioMotoristaPropService.storeBloqueio(bloqueio);			
		}
		return false;
	}


	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}
	
	public void setBloqueioMotoristaPropService(
			BloqueioMotoristaPropService bloqueioMotoristaPropService) {
		this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
	}
}