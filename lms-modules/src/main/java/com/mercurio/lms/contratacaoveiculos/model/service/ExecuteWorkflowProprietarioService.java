package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.BloqueioMotoristaProp;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
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
public class ExecuteWorkflowProprietarioService extends FluxoContratacaoWorkFlow{

	private static final String LMS_26152 = "LMS-26152";
	public static final String PROPRIETARIO = "identificacao";
	public static final String ANEXO = "anexo";
	public static final String ENDERECO = "novoEndereco";
	public static final String DADOS_BANCARIOS = "dadosBancarios";
	
	private static final String LMS_26134 = "LMS-26134";
	private static final String LMS_26139 = "LMS-26139";
	private static final String LMS_26140 = "LMS-26140";
	private static final String LMS_26141 = "LMS-26141";
	
	
	private static final String PESSOA_ATIVO = "A";
	
	private static final String WORFLOW_APROVADO = "A";
	private static final String WORFLOW_EM_APROVACAO = "E";
		
	private static final String  PROPRIO = "P";
	
	private ProprietarioService proprietarioService;
	private WorkflowPendenciaService workflowPendenciaService;
	private DomainValueService domainValueService;
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

			Proprietario proprietario = proprietarioService.findById(id);
			
			Usuario usuarioAprovador = SessionUtils.getUsuarioLogado();
			proprietario.setUsuarioAprovador(usuarioAprovador);
			proprietario.setTpSituacaoWorkflow(new DomainValue(tpSituacaoAprovacao));
			
			desbloqueiaRegistro(proprietario);
			
			if(WORFLOW_APROVADO.equals(tpSituacaoAprovacao)){					
				proprietario.setTpSituacao(new DomainValue(PESSOA_ATIVO));
				proprietarioService.storeProprietarioCommon(proprietario, false);
			}
		}

		return null;
	}


	private Map executeWorkflowPendencia(Proprietario proprietario,Map<String,Boolean> alteracoes,boolean isCadastroNovo) {    	
		Pendencia pendencia = executeWorkflowPendenciaWorkFlow(proprietario,alteracoes,isCadastroNovo);
		proprietario.setPendencia(pendencia);    	    
		proprietario.setTpSituacaoWorkflow(new DomainValue(WORFLOW_EM_APROVACAO));
    	
    	return proprietarioService.store(proprietario);
    }
    
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Pendencia executeWorkflowPendenciaWorkFlow(Proprietario proprietario,Map<String,Boolean> alteracoes, boolean isCadastroNovo) {
		
		boolean isColeta = TIPO_OPERACAO_COLETA_ENTREGA.equals(proprietario.getTpOperacao().getValue());
		
		boolean isNovo = gerarNovaPendencia(proprietario.getPendencia(), isCadastroNovo);
		
		Short nrTipoEvento = getNrTipoEvento(isColeta, isNovo) ;
			
		Pendencia pendencia = null;
		
		String mensagemWorkflow = "";
		
		if(!isNovo){
			
			mensagemWorkflow = getDsProcessoWorkflowParaReciboFreteCarreteiro(alteracoes,proprietario,false); 
			
			if(proprietario.getPendencia() != null){
				Map criteria = new HashMap<String, Object>();
				criteria.put("idPendencia", proprietario.getPendencia().getIdPendencia());
				criteria.put("tpSituacaoPendencia", WORFLOW_EM_APROVACAO);
				List<Pendencia> pendenciaAux = workflowPendenciaService.getPendenciaService().find(criteria);
				
				if(!pendenciaAux.isEmpty()){
					if(!mensagemWorkflow.equals(pendenciaAux.get(0).getDsPendencia())){
						mensagemWorkflow = pendenciaAux.get(0).getDsPendencia() + " " + mensagemWorkflow;					
					}
					workflowPendenciaService.cancelPendencia(proprietario.getPendencia().getIdPendencia());			

					nrTipoEvento = proprietario.getPendencia().getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
				}		
			}
			
			
		}else{
			mensagemWorkflow = getDsProcessoWorkflowParaReciboFreteCarreteiro(alteracoes,proprietario,true);
		}
		
		pendencia = workflowPendenciaService.generatePendencia(proprietario.getFilial().getIdFilial(), nrTipoEvento, proprietario.getIdProprietario(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
		return pendencia;
	}
	
	
	private Short getNrTipoEvento(boolean isColeta, boolean isNovo) {
		if(isColeta){			
			return getEventoColeta(isNovo);
		}else{
			return getEventoViagem(isNovo);
		}
	}
	

	private Short getEventoColeta(boolean isNovo) {
		if(isNovo){
			return ConstantesWorkflow.NR2609_APROVACAO_CADASTRO_PROPRIETARIO_CE;
		}else{
			return ConstantesWorkflow.NR2616_ALTERACAO_CADASTRO_PROPRIETARIO_CE;									
		}
	}

	private Short getEventoViagem(boolean isNovo) {
		if(isNovo){			
			return ConstantesWorkflow.NR2613_APROVACAO_CADASTRO_PROPRIETARIO_VI;
		}else{
			return ConstantesWorkflow.NR2619_ALTERACAO_CADASTRO_PROPRIETARIO_VI;								
		}
	}
	
	
	
	private String getDsProcessoWorkflowParaReciboFreteCarreteiro(Map<String, Boolean> alteracoes, Proprietario proprietario,boolean proprietarioNovo) {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append(ajustaMensagem(PROPRIETARIO, proprietarioNovo ? LMS_26152 : LMS_26139,alteracoes, proprietario));
		mensagem.append(ajustaMensagem(ANEXO, LMS_26140,alteracoes, proprietario));
		mensagem.append(ajustaMensagem(ENDERECO, LMS_26141,alteracoes, proprietario));
		mensagem.append(ajustaMensagem(DADOS_BANCARIOS, LMS_26134,alteracoes, proprietario));
		
		return mensagem.toString();
	}

	

	private Object ajustaMensagem(String chave, String lms, Map<String, Boolean> alteracoes, Proprietario proprietario) {
		if(verificaAlteracao(alteracoes, chave)){
			return configuracoesFacade.getMensagem(lms, getParametrosProprietario(proprietario).toArray());
		}
		return "";
	}

	private List<String> getParametrosProprietario(Proprietario proprietario) {
		List<String> parametros = new ArrayList<String>();			
		parametros.add(proprietario.getPessoa().getNrIdentificacao());
		parametros.add(proprietario.getPessoa().getNmPessoa());
		return parametros;
	}


	private boolean verificaAlteracao(Map<String, Boolean> alteracoes,String chave) {
		return alteracoes.containsKey(chave) && BooleanUtils.isTrue(alteracoes.get(chave));
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
	
	public ProprietarioService getProprietarioService() {
		return proprietarioService;
	}


	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}

	public BloqueioMotoristaPropService getBloqueioMotoristaPropService() {
		return bloqueioMotoristaPropService;
	}

	public void setBloqueioMotoristaPropService(
			BloqueioMotoristaPropService bloqueioMotoristaPropService) {
		this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
	}

	/**
	 * Executado na tela de manter Proprietario
	 * 
	 * <i>Alterações: deve conter chave [{@link #PROPRIETARIO},{@link #ANEXO},{@link #ENDERECO},{@link #DADOS_BANCARIOS}] e bolleano correspondente se há alteração;</i>
	 * 
	 * @param proprietario
	 * @param alteracoes
	 * @return
	 */
	public void executeWorkflowPendencia(Pessoa pessoa, String chave,boolean isCadastroNovo) {
			
		Proprietario proprietario = proprietarioService.findById(pessoa.getIdPessoa());
		
		if(proprietario == null){
			return;
		}
		
		verificarTipoOperacao(proprietario.getTpOperacao());
		
		if(isGeraWorflow(proprietario, SessionUtils.isFilialSessaoMatriz())){
			
			bloqueiaRegistro(proprietario,isCadastroNovo);
    		
    		Map<String, Boolean> alteracoes = new HashMap<String, Boolean>();
    		alteracoes.put(chave, true);
    		
    		executeWorkflowPendencia(proprietario, alteracoes,isCadastroNovo);    		
    	}
		
	}
	
	private void desbloqueiaRegistro(Proprietario proprietario) {
		Map<String, Object> dados = new HashMap<String, Object>();
		dados.put("idProprietario", String.valueOf(proprietario.getIdProprietario()));
		@SuppressWarnings("rawtypes")
		Map bloqueios = bloqueioMotoristaPropService.findDadosBloqueioProprietario(dados);
		if(!bloqueios.isEmpty()){
			BloqueioMotoristaProp bloqueio = new BloqueioMotoristaProp();
			bloqueio.setIdBloqueioMotoristaProp(Long.valueOf(bloqueios.get("idBloqueioMotoristaProp").toString()));
			bloqueio.setDhVigenciaInicial((DateTime) bloqueios.get("dhVigenciaInicial"));
			bloqueio.setDhVigenciaFinal(JTDateTimeUtils.getDataHoraAtual());
			bloqueio.setProprietario(proprietario);
			bloqueio.setObBloqueioMotoristaProp("Desbloqueio via workflow");
			bloqueioMotoristaPropService.storeBloqueio(bloqueio);			
		}
	}
	
	private boolean bloqueiaRegistro(Proprietario proprietario, boolean isCadastroNovo) {
		if(isCadastroNovo){
			BloqueioMotoristaProp bloqueio = new BloqueioMotoristaProp();
			bloqueio.setDhVigenciaInicial(JTDateTimeUtils.getDataHoraAtual());
			bloqueio.setProprietario(proprietario);
			bloqueio.setObBloqueioMotoristaProp("Bloqueado, aguardando aprovação workflow.");
			bloqueioMotoristaPropService.storeBloqueio(bloqueio);			
		}
		return false;
	}
	
	public  Boolean isGeraWorflow(Proprietario proprietario, Boolean isMatriz) {
		boolean isProprio =  PROPRIO.equals(proprietario.getTpProprietario().getValue());
		
		if(isMatriz || isProprio || !isLiberaValidacao()){
			return false;
		}
		return true;
	}
}