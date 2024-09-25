package com.mercurio.lms.workflow.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.EventoWorkflow;
import com.mercurio.lms.workflow.model.HistoricoWorkflow;
import com.mercurio.lms.workflow.model.Integrante;
import com.mercurio.lms.workflow.model.Ocorrencia;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.workflow.workflowPendenciaService"
 */
public class WorkflowPendenciaService {

	private FilialService filialService;
	
	private PendenciaService pendenciaService;
	
	private EventoWorkflowService eventoWorkflowService;

	private OcorrenciaService ocorrenciaService;
	
	private HistoricoWorkflowService historicoWorkflowService;
	
	private AcaoService acaoService;
	
	private GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService;
	
	private IntegranteService integranteService;
	
	private DomainValueService domainValueService;
	
	private WorkflowEngine workflowEngine;
	
	private ConfiguracoesFacade configuracoesFacade;

	public GerarEmailMensagemAvisoService getGerarEmailMensagemAvisoService() {
		return gerarEmailMensagemAvisoService;
	}

	public void setGerarEmailMensagemAvisoService(
			GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService) {
		this.gerarEmailMensagemAvisoService = gerarEmailMensagemAvisoService;
	}

	/**
	 * Fecha a pendência mudando a situação dela.
	 * 
	 * @param Long idPendencia
	 * @param String tpSituacaoPendencia
	 * */
	public String executeClosePendencia(Pendencia pendencia) {
		
		this.getPendenciaService().store(pendencia);
		
    	final Ocorrencia ocorrencia = pendencia.getOcorrencia();
    	
    	//Lista das pendencias pendentes da ocorrencia 
    	List pendenciasPendentes = this.getPendenciaService().findPendenciasAbertasByOcorrencia(ocorrencia.getIdOcorrencia());
    	
    	//Lista das pendencias da ocorrencia
    	List<Pendencia> pendencias = this.getPendenciaService().findPendenciasByOcorrencia(ocorrencia.getIdOcorrencia());    	
		
		String executeClasseAcao = null;
    	if (pendenciasPendentes.size() <= 0){
        	//Trocar a situação da ocorrência e salvar-lo
        	ocorrencia.setTpSituacaoOcorrencia(this.getDomainValueService().findDomainValueByValue("DM_STATUS_OCORRENCIA_WORKFLOW","C"));    	
        	this.ocorrenciaService.store(ocorrencia);
        	
    		final EventoWorkflow eventoWorkflow = ocorrencia.getEventoWorkflow();
            	
    		if (eventoWorkflow.getNmClasseAcao() != null) {
    			
    			executeClasseAcao = this.getWorkflowService().executeClasseAcao(pendenciasPendentes, 
    																			pendencias, 
    																			eventoWorkflow.getNmClasseAcao());
    		}
    		
    		//Se a ação é aprovada ou reprovada e se o solicitante tem email,
    		//mandar um email avisando o solicitante que foi fechado a pendencia.
			if (eventoWorkflow.getBlRequerAprovacao().equals(Boolean.TRUE) && 
    			!pendencia.getTpSituacaoPendencia().getValue().equals("C") &&
    			eventoWorkflow.getBlRequerAprovacao().equals(Boolean.TRUE) && 
    			StringUtils.isNotBlank(ocorrencia.getUsuario().getDsEmail())) { 
    			
    			this.getGerarEmailMensagemAvisoService().sendEmailUsuario(ocorrencia.getUsuario().getDsEmail(),
    																getGerarEmailMensagemAvisoService().mountSubjectEmailWorkflow(eventoWorkflow.getTipoEvento().getDsTipoEvento().getValue()),
    																getGerarEmailMensagemAvisoService().mountBodyClosePendenciaEmailWorkflow(pendencias.toArray(new Pendencia[]{})));
    		}
		}
		return executeClasseAcao;
	}
	
	/**
	 * Cancela a pendência mudando a situação dela e chamando a classe de 
	 * ação caso que é a última pendencia.
	 * 
	 * @param Long idPendencia
	 * */
	public String cancelPendencia(Long idPendencia){
		Pendencia pendencia = this.getPendenciaService().findById(idPendencia);
		return cancelPendencia(pendencia);
	}

	public String cancelPendencia(Pendencia pendencia){
		//Se a pendencia não é pendente, lançar uma Exception.
		if (!pendencia.getTpSituacaoPendencia().getValue().equals("E")){
			throw new BusinessException("LMS-39017");
		}		
		//Se a ocorrência não é pendente, lançar uma Exception.
		if (!pendencia.getOcorrencia().getTpSituacaoOcorrencia().getValue().equals("P")){
			throw new BusinessException("LMS-39018");
		}
				
		pendencia.setTpSituacaoPendencia(this.getDomainValueService().findDomainValueByValue("DM_STATUS_ACAO_WORKFLOW","C"));		
		
		//Salvar as ações como 'Cancelada'
		this.getAcaoService().cancelAcoesPendentes(pendencia.getIdPendencia());		
		
		return executeClosePendencia(pendencia);
	}
    
	/**
	 * 
	 * @param idFilial
	 * @param nrTipoEvento
	 * @param idProcesso
	 * @param dsProcesso
	 * @param dhLiberacao
	 * @param nmTabela
	 * @param nmCampo
	 * @param dsVlAntigo
	 * @param dsVlNovo
	 * @param idUsuario
	 * @param dsObservacao
	 * @return
	 */
	public Pendencia generatePendenciaHistorico(PendenciaHistoricoDTO pendenciaHistoricoDTO){
		Pendencia pendencia = this.generatePendencia(pendenciaHistoricoDTO.getIdFilial(), pendenciaHistoricoDTO.getNrTipoEvento(), pendenciaHistoricoDTO.getIdProcesso(), pendenciaHistoricoDTO.getDsProcesso(), pendenciaHistoricoDTO.getDhLiberacao());
		HistoricoWorkflow historicoWorkflow = getHistoricoWorkflow(pendencia, pendenciaHistoricoDTO);
		historicoWorkflowService.store(historicoWorkflow);
		return pendencia;
	}
	
	
	private HistoricoWorkflow getHistoricoWorkflow(Pendencia pendencia, PendenciaHistoricoDTO pendenciaHistoricoDTO) {
		HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow();
		historicoWorkflow.setPendencia(pendencia);
		historicoWorkflow.setIdProcesso(pendenciaHistoricoDTO.getIdProcesso());
		historicoWorkflow.setNmTabela(pendenciaHistoricoDTO.getTabelaHistoricoWorkflow().name());

		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		historicoWorkflow.setUsuario(usuario);

		if(pendenciaHistoricoDTO.getCampoHistoricoWorkflow() != null){
			historicoWorkflow.setTpCampoWorkflow(new DomainValue(pendenciaHistoricoDTO.getCampoHistoricoWorkflow().name()));
		}
		
		historicoWorkflow.setDsVlAntigo(pendenciaHistoricoDTO.getDsVlAntigo());
		historicoWorkflow.setDsVlNovo(pendenciaHistoricoDTO.getDsVlNovo());
		historicoWorkflow.setDhSolicitacao(JTDateTimeUtils.getDataHoraAtual());
		historicoWorkflow.setDsObservacao(pendenciaHistoricoDTO.getDsObservacao());

		return historicoWorkflow;
	}
	
    /**
     * Insere a ocorrencis, pendencia(s) e ações de um evento informado.
     * @param idFilial
     * @param nrTipoEvento
     * @param idProcesso
     * @param dsProcesso
     * @param dhLiberacao
     * @param usuario
     * @param idEmpresa
     * @return
     */
	public Pendencia generatePendencia(Long idFilial, Short nrTipoEvento, Long idProcesso, String dsProcesso, DateTime dhLiberacao, Usuario usuario, Long idEmpresa){
    	List<Long> idsProcesso = new ArrayList<Long>();
    	List<String> dssProcesso = new ArrayList<String>(); 
    	idsProcesso.add(idProcesso);
    	dssProcesso.add(dsProcesso);    	
    	List lstPendencias = generatePendencia(idFilial, nrTipoEvento, idsProcesso, dssProcesso, dhLiberacao, usuario, idEmpresa);
    	
    	return lstPendencias.size() > 0 ? (Pendencia) lstPendencias.get(0) : null;
    }
	
    /**
     * Insere a ocorrencis, pendencia(s) e ações de um evento informado.
     * 
     * NÃO UTILIZAR POIS ESTE SERVIÇO UTILIZA DADOS DA SESSÃO QUE NÃO DEVERIAM SER 
     * ACESSADOS NA CAMADA DE SERVIÇOS!!!
     * 
     * @param Long idFilial
     * @param Short nrTipoEvento
     * @param List idsProcesso
     * @param List dssProcesso
     * @param Date dtLiberacao
     * */
	@Deprecated
    public Pendencia generatePendencia(Long idFilial, Short nrTipoEvento, Long idProcesso, String dsProcesso, DateTime dhLiberacao){    	   
    	return generatePendencia(idFilial, nrTipoEvento, idProcesso, dsProcesso, dhLiberacao, 
    			SessionUtils.getUsuarioLogado(), SessionUtils.getEmpresaSessao().getIdEmpresa());
    }
    
    /**
     * Insere a ocorrencis, pendencia(s) e ações de um evento informado.
     * 
     * @param Long idFilial
     * @param Short nrTipoEvento
     * @param List idsProcesso
     * @param List dssProcesso
     * @param Date dtLiberacao
     * */
    public List generatePendencia(Long idFilial, Short nrTipoEvento, List idsProcesso, List dssProcesso, DateTime dhLiberacao, Usuario usuario, Long idEmpresa) {
    	List<Pendencia> pendencias = new ArrayList();
    	
    	//Buscar a Filial
    	Filial filial = this.getFilialService().findById(idFilial);
    	
    	EventoWorkflow eventoWorkflow = getEventoWorkflow(nrTipoEvento);
    	
    	if ("A".equals(eventoWorkflow.getTipoEvento().getTpSituacao().getValue())){
    	
    	Ocorrencia ocorrencia = geraOcorencia(filial, usuario, eventoWorkflow);
    	
    	//Por cada processo
    	Integrante primeiroIntegrante = null;
    	for(int i=0; i< idsProcesso.size(); i++){
    		Long idProcesso = (Long)idsProcesso.get(i);
    		String dsProcesso = null;
    		if (dssProcesso != null) {
    			dsProcesso = (String)dssProcesso.get(i);
    		}
    		
    		Pendencia pendencia = geraPendencia(idProcesso, dsProcesso, ocorrencia);
    		pendencias.add(pendencia);
    		
	
	    		
    		List integrantes = this.getIntegranteService().findIntegrantesByComite(eventoWorkflow.getComite().getIdComite(), 
    																				idFilial, 
    																				idEmpresa);
    		
    		// Quando não existe nenhum integrante, lançar uma business exception
    		if (integrantes.size() <= 0){
    			throw new BusinessException("LMS-39023", new Object[]{eventoWorkflow.getComite().getNmComite()});
    		}
    		
    		//Por cada integrante
    		for(Iterator iter = integrantes.iterator(); iter.hasNext();){
    			Integrante integrante = (Integrante)iter.next();   			
    			
    			geraAcao(dhLiberacao, primeiroIntegrante, pendencia, integrante);
    			
    			// deve atribuir o primerio integrante apenas após montar o objeto Acao.
    			if (primeiroIntegrante == null){
    				primeiroIntegrante = integrante;
    			} 
    			
    		}
    	}

    	if(primeiroIntegrante == null){
    		throw new IllegalStateException("Deve sempre existir um primeiro integrante");
    	}
    	
    	
    	// Enviar email somente quando ação estiver liberada
    	if (JTDateTimeUtils.getDataHoraAtual().isAfter(dhLiberacao)){
	    	//Mandar email para o primeiro integrante do comité
	    	this.gerarEmailMensagemAvisoService.gerarEmailMensagem(idFilial, 
	    															idEmpresa,
	    															nrTipoEvento, 
	    															primeiroIntegrante.getUsuario(), 
	    															primeiroIntegrante.getPerfil(), 
	    															Short.valueOf((short)idsProcesso.size()),
	    															pendencias.toArray(new Pendencia[]{}));
	    	
	    	//Mandar email para o os usuarios da Cópia de Email
	    	this.gerarEmailMensagemAvisoService.gerarEmailMensagemUsuarioFilial(eventoWorkflow.getIdEventoWorkflow(), 
	    																		idFilial, 
	    																		eventoWorkflow.getTipoEvento().getDsTipoEvento().getValue(), 
	    																		dssProcesso, 
	    																		filial.getSgFilial());
    	}
    	}

    	return pendencias;
    }
    
    /**
     * Insere a ocorrencis, pendencia(s) e ações de um evento informado.
     * NÃO UTILIZAR POIS ESTE SERVIÇO UTILIZA DADOS DA SESSÃO QUE NÃO DEVERIAM SER 
     * ACESSADOS NA CAMADA DE SERVIÇOS!!!
     * 
     * @param Long idFilial
     * @param Short nrTipoEvento
     * @param List idsProcesso
     * @param List dssProcesso
     * @param Date dtLiberacao
     * */
    @Deprecated
    public List generatePendencia(Long idFilial, Short nrTipoEvento, List idsProcesso, List dssProcesso, DateTime dhLiberacao){
    	return generatePendencia(idFilial, nrTipoEvento, idsProcesso, dssProcesso, dhLiberacao, 
    			SessionUtils.getUsuarioLogado(), SessionUtils.getEmpresaSessao().getIdEmpresa());
    }
    
    
    /**
     * Cria uma nova instância de ocorrencia populada seguindo as 
     * especificações da regra 1.
     * @param filial
     * @param eventoWorkflow
     * @param usuario
     * @return
     */
    private Ocorrencia mountOcorrencia(Filial filial, EventoWorkflow eventoWorkflow, Usuario usuario){
    	Ocorrencia ocorrencia = new Ocorrencia();
    	ocorrencia.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
    	ocorrencia.setEventoWorkflow(eventoWorkflow);
    	ocorrencia.setFilial(filial);
    	ocorrencia.setTpSituacaoOcorrencia(this.domainValueService.findDomainValueByValue("DM_STATUS_OCORRENCIA_WORKFLOW","P"));
    	ocorrencia.setUsuario(usuario);

    	return ocorrencia;
    }
    
    /**
     * Cria uma nova instância de pendencia populada seguindo as 
     * especificações da regra 1.
     * 
     * @param Long idProcesso
     * @param String dsProcesso
     * @param Ocorrencia ocorrencia
     * @return Pendencia
     * */
    private Pendencia mountPendencia(Long idProcesso, String dsProcesso, Ocorrencia ocorrencia){
    	Pendencia pendencia = new Pendencia();
    	pendencia.setDsPendencia(dsProcesso);
    	pendencia.setIdProcesso(idProcesso);
    	pendencia.setOcorrencia(ocorrencia);
    	pendencia.setTpSituacaoPendencia(this.domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW","E"));
    	return pendencia;
    }
    
    /**
     * Cria uma nova instância de ação populada seguindo as 
     * especificações da regra 1.
     * 
     * @param Long idProcesso
     * @param EventoWorkflow eventoWorkflow
     * @return Acao
     * */
    private Acao mountAcao(Integrante integrante, Pendencia pendencia, boolean blPrimeiro, DateTime dhLiberacao){
    	Acao acao = new Acao();
    	acao.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
    	acao.setIntegrante(integrante);
    	acao.setNrOrdemAprovacao(integrante.getNrOrdemAprovacao());
    	acao.setPendencia(pendencia);
    	acao.setTpSituacaoAcao(this.domainValueService.findDomainValueByValue("DM_STATUS_ACAO_WORKFLOW","E"));
    	
    	//Quand é a primeira ação
    	if (blPrimeiro) {
    		acao.setDhLiberacao(dhLiberacao);
    	}
    	
    	/*
    	 * Liberar só quando é a primeira ação e que a data de Liberação é 
    	 * anterior a data atual.
    	 * */
    	if (blPrimeiro && JTDateTimeUtils.getDataHoraAtual().isAfter(dhLiberacao)){
    		acao.setBlLiberada(Boolean.valueOf(blPrimeiro));
    	} else {
    		acao.setBlLiberada(Boolean.FALSE);
    	}
    	return acao;
    }

    public Pendencia generatePendenciaSemEmail(Filial filial, Usuario usuario, Empresa empresa, Short nrTipoEvento, Long idProcesso, String dsProcesso,DateTime dhLiberacao) {
		EventoWorkflow eventoWorkflow = getEventoWorkflow(nrTipoEvento);

		if ("A".equals(eventoWorkflow.getTipoEvento().getTpSituacao().getValue())) {

			Ocorrencia ocorrencia = geraOcorencia(filial, usuario, eventoWorkflow);

			Integrante primeiroIntegrante = null;

			Pendencia pendencia = geraPendencia(idProcesso, dsProcesso, ocorrencia);

			List integrantes = getIntegrantes(filial, empresa, eventoWorkflow);

			for (Iterator iter = integrantes.iterator(); iter.hasNext();) {
				Integrante integrante = (Integrante) iter.next();

				geraAcao(dhLiberacao, primeiroIntegrante, pendencia, integrante);

				if (primeiroIntegrante == null) {
					primeiroIntegrante = integrante;
				}

			}

			if (primeiroIntegrante == null) {
				throw new IllegalStateException("Deve sempre existir um primeiro integrante");
			}

			return pendencia;
		}
		return null;

    	
	}

	/**
	 * @param dhLiberacao
	 * @param primeiroIntegrante
	 * @param pendencia
	 * @param integrante
	 */
	private void geraAcao(DateTime dhLiberacao, Integrante primeiroIntegrante, Pendencia pendencia, Integrante integrante) {
		Acao acao = this.mountAcao(integrante, pendencia, primeiroIntegrante == null, dhLiberacao);
		this.getAcaoService().store(acao);
	}

	/**
	 * @param idProcesso
	 * @param dsProcesso
	 * @param ocorrencia
	 * @return
	 */
	private Pendencia geraPendencia(Long idProcesso, String dsProcesso, Ocorrencia ocorrencia) {
		Pendencia pendencia = mountPendencia(idProcesso, dsProcesso, ocorrencia);
		this.getPendenciaService().store(pendencia);
		return pendencia;
	}

	/**
	 * @param filial
	 * @param usuario
	 * @param eventoWorkflow
	 * @return
	 */
	private Ocorrencia geraOcorencia(Filial filial, Usuario usuario, EventoWorkflow eventoWorkflow) {
		Ocorrencia ocorrencia = this.mountOcorrencia(filial, eventoWorkflow, usuario);
		this.getOcorrenciaService().store(ocorrencia);
		return ocorrencia;
	}

	/**
	 * @param nrTipoEvento
	 * @return
	 * @throws BusinessException
	 */
	private EventoWorkflow getEventoWorkflow(Short nrTipoEvento) throws BusinessException {
		if (nrTipoEvento == null) {
			throw new BusinessException("LMS-39005", new Object[] { "" });
		}

		EventoWorkflow eventoWorkflow = this.getEventoWorkflowService().findByTipoEvento(nrTipoEvento);
		return eventoWorkflow;
	}

	/**
	 * @param filial
	 * @param empresa
	 * @param eventoWorkflow
	 * @return
	 * @throws BusinessException
	 */
	private List getIntegrantes(Filial filial, Empresa empresa, EventoWorkflow eventoWorkflow) throws BusinessException {
		List integrantes = this.getIntegranteService().findIntegrantesByComite(eventoWorkflow.getComite().getIdComite(), filial.getIdFilial(),	empresa.getIdEmpresa());

		// Quando não existe nenhum integrante, lançar uma business
		// exception
		if (integrantes.size() <= 0) {
			throw new BusinessException("LMS-39023", new Object[] { eventoWorkflow.getComite().getNmComite() });
		}
		return integrantes;
	}

	public PendenciaService getPendenciaService() {
		return pendenciaService;
	}

	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public EventoWorkflowService getEventoWorkflowService() {
		return eventoWorkflowService;
	}

	public void setEventoWorkflowService(EventoWorkflowService eventoWorkflowService) {
		this.eventoWorkflowService = eventoWorkflowService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public IntegranteService getIntegranteService() {
		return integranteService;
	}

	public void setIntegranteService(IntegranteService integranteService) {
		this.integranteService = integranteService;
	}

	public OcorrenciaService getOcorrenciaService() {
		return ocorrenciaService;
	}

	public void setOcorrenciaService(OcorrenciaService ocorrenciaService) {
		this.ocorrenciaService = ocorrenciaService;
	}

	public AcaoService getAcaoService() {
		return acaoService;
	}

	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}   

	public WorkflowEngine getWorkflowService() {
		return workflowEngine;
	}

	public void setWorkflowService(WorkflowEngine workflowEngine) {
		this.workflowEngine = workflowEngine;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public HistoricoWorkflowService getHistoricoWorkflowService() {
		return historicoWorkflowService;
	}

	public void setHistoricoWorkflowService(HistoricoWorkflowService historicoWorkflowService) {
		this.historicoWorkflowService = historicoWorkflowService;
	}
   }
