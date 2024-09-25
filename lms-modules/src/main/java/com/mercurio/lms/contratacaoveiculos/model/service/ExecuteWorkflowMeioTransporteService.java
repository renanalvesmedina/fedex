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
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
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
 * @spring.bean id="lms.indenizacoes.executeWorkflowProprietarioService"
 */
public class ExecuteWorkflowMeioTransporteService extends FluxoContratacaoWorkFlow{
	
	
	private static final String LMS_26144 = "LMS-26144";	
	private static final String LMS_26150 = "LMS-26150";
	
	private MeioTransporteService meioTransporteService;
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

			MeioTransporte meioTransporte = meioTransporteService.findById(id);
			desbloqueiaRegistro(meioTransporte);
			
			Usuario usuarioAprovador = SessionUtils.getUsuarioLogado();
			meioTransporte.setUsuarioAprovador(usuarioAprovador);
			meioTransporte.setTpSituacaoWorkflow(new DomainValue(tpSituacaoAprovacao));
			meioTransporteService.storeWorkFlow(meioTransporte);
		}

		return null;
	}

	/**
	 * Executado na tela de manter Meio Transporte
	 * 
	 * 
	 * 
	 * @param meioTransporte
	 * @param isCadastroNovo 
	 * @return
	 */
	public void executeWorkflowPendencia(MeioTransporte meioTransporte, boolean isCadastroNovo) {
			
		if(!isLiberaValidacao()){
			return;
		}
		
		MeioTransporte meioTransporteWorkflow = meioTransporteService.findById(meioTransporte.getIdMeioTransporte());
		
		if(meioTransporteWorkflow == null){
			return;
		}
		
		verificarTipoOperacao(meioTransporte.getTpOperacao());
		
		executeWorkflowPendenciaWorkFlow(meioTransporteWorkflow,isCadastroNovo);    		
		
	}

	
	private void executeWorkflowPendenciaWorkFlow(MeioTransporte meioTransporte, boolean isCadastroNovo) {
		Pendencia pendencia = meioTransporte.getPendencia();
		
		if( pendencia != null && (ConstantesWorkflow.EM_APROVACAO).equals(pendencia.getTpSituacaoPendencia().getValue())){
			return;		
		}else{
			
			boolean isColeta = TIPO_OPERACAO_COLETA_ENTREGA.equals(meioTransporte.getTpOperacao().getValue());
			
			boolean isNovo = gerarNovaPendencia(pendencia, isCadastroNovo);
			
			
			bloqueiaRegistro(meioTransporte,isNovo);				

			
			Short nrTipoEvento =  getNrTipoEvento(isColeta,isNovo);
			
			String mensagemWorkflow = getDsProcessoWorkflowParaReciboFreteCarreteiro(meioTransporte,isNovo);
			
			pendencia = workflowPendenciaService.generatePendencia(meioTransporte.getFilial().getIdFilial(), nrTipoEvento, meioTransporte.getIdMeioTransporte(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
			
			meioTransporte.setPendencia(pendencia);    	    
	    	
	    	meioTransporteService.storeWorkFlow(meioTransporte);			
		}
	}

	
	private String getDsProcessoWorkflowParaReciboFreteCarreteiro(MeioTransporte meioTransporte, boolean isNovo) {
		return getMensagemWorkflow(meioTransporte, isNovo);
		
	}
	
	private String getMensagemWorkflow(MeioTransporte meioTransporte, boolean isNovo) {
		if(isNovo){
			return getMensagem(LMS_26150,meioTransporte);		
		}else{
			return getMensagem(LMS_26144,meioTransporte);			
		}
	}
	
	private String getMensagem(String chave, MeioTransporte meioTransporte) {
		return configuracoesFacade.getMensagem(chave, getParametros(meioTransporte).toArray());
	}

	private Short getNrTipoEvento(boolean isColeta, boolean isNovo) {
		if(isColeta){			
			return getEventoColeta(isNovo);
		}else{
			return getEventoViagem(isNovo);
		}
	}

	private Short getEventoViagem(boolean isNovo) {
		if(isNovo){			
			return ConstantesWorkflow.NR2615_APROVACAO_CADASTRO_MEIO_TRANSPORTE_VI;
		}else{
			return ConstantesWorkflow.NR2621_ALTERACAO_CADASTRO_MEIO_TRANSPORTE_VI;								
		}
	}

	private Short getEventoColeta(boolean isNovo) {
		if(isNovo){
			return ConstantesWorkflow.NR2612_APROVACAO_CADASTRO_MEIO_TRANSPORTE_CE;
		}else{
			return ConstantesWorkflow.NR2618_ALTERACAO_CADASTRO_MEIO_TRANSPORTE_CE;									
		}
	}

	private List<String> getParametros(MeioTransporte meioTransporte) {
		List<String> parametros = new ArrayList<String>();			
		parametros.add(meioTransporte.getNrFrota());
		parametros.add(meioTransporte.getNrIdentificador());
		return parametros;
	}
	
	private void desbloqueiaRegistro(MeioTransporte meioTransporte) {
		Map<String, Object> dados = new HashMap<String, Object>();
		dados.put("idMeioTransporte", String.valueOf(meioTransporte.getIdMeioTransporte()));
		@SuppressWarnings("rawtypes")
		Map bloqueios = bloqueioMotoristaPropService.findDadosBloqueioMeioTransporte(dados);
		if(!bloqueios.isEmpty()){
			BloqueioMotoristaProp bloqueio = new BloqueioMotoristaProp();
			bloqueio.setIdBloqueioMotoristaProp(Long.valueOf(bloqueios.get("idBloqueioMotoristaProp").toString()));
			bloqueio.setDhVigenciaInicial((DateTime) bloqueios.get("dhVigenciaInicial"));
			bloqueio.setDhVigenciaFinal(JTDateTimeUtils.getDataHoraAtual());
			bloqueio.setMeioTransporte(meioTransporte);
			bloqueio.setObBloqueioMotoristaProp("Desbloqueio via workflow");
			bloqueioMotoristaPropService.storeBloqueio(bloqueio);			
		}
	}
	
	private boolean bloqueiaRegistro(MeioTransporte meioTransporte, boolean isNovo) {
		if(isNovo){
			BloqueioMotoristaProp bloqueio = new BloqueioMotoristaProp();
			bloqueio.setDhVigenciaInicial(JTDateTimeUtils.getDataHoraAtual());
			bloqueio.setMeioTransporte(meioTransporte);
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

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setBloqueioMotoristaPropService(
			BloqueioMotoristaPropService bloqueioMotoristaPropService) {
		this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
	}

}