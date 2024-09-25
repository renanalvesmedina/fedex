package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ProprietarioRPA;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.CalculoReciboIRRFService;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.EventoWorkflow;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.EventoWorkflowService;
import com.mercurio.lms.workflow.model.service.PendenciaService;
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
 * @spring.bean id="lms.indenizacoes.executeWorkflowReciboFreteCarreteiroColetaEntregaService"
 */
public class ExecuteWorkflowReciboFreteCarreteiroColetaEntregaService {

	private static final String DOMINIO_RECIBO_FRETE_CARRETEIRO = "DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE";
	private static final String DOMINIO_STATUS_WORKFLOW = "DM_STATUS_WORKFLOW";

	private static final String WORFLOW_APROVADO = "A";
	private static final String WORFLOW_REJEITADO = "R";
	private static final String WORFLOW_EM_APROVACAO = "E";
	private static final String EM_APROVACAO = "EA";
	private static final String AGUARDANDO_ENVIO_JDE = "AJ";
	private static final String REJEITADO = "RE";
	private static final String ATIVA_RATEIO_RFC = "ATIVA_RATEIO_RFC";
	
	private ReciboFreteCarreteiroService reciboFreteCarreteitoService;
	private WorkflowPendenciaService workflowPendenciaService;
	private DomainValueService domainValueService;
	private ConfiguracoesFacade configuracoesFacade;
	private ParametroGeralService parametroGeralService;
	private RateioFreteCarreteiroCeService rateioFreteCarreteiroCeService;
	private CalculoReciboIRRFService calculoReciboIRRFService;
	private FilialService filialService;
	private PendenciaService pendenciaService;
	private EventoWorkflowService eventoWorkflowService;
	private ProprietarioRpaService proprietarioRpaService;
	private ReciboAnuarioRfcService reciboAnuarioRfcService;
	
	/**
	 * Executado pela tela de manter ações
	 * 
	 * @param ids
	 * @param situacoes
	 * @return
	 */
	public String executeWorkflow(List<Long> ids, List<String> situacoes) {
		for (int i = 0; i < ids.size(); i++) {

			Long idReciboIndenizacao = (Long) ids.get(i);
			String tpSituacaoAprovacao = (String) situacoes.get(i);

			ReciboFreteCarreteiro recibofreteCarreteiro = reciboFreteCarreteitoService.findById(idReciboIndenizacao);

			recibofreteCarreteiro.setTpSituacaoWorkflow(new DomainValue(tpSituacaoAprovacao));

			recibofreteCarreteiro = atualizaSituacaoRecibo(recibofreteCarreteiro, tpSituacaoAprovacao);

			reciboFreteCarreteitoService.store(recibofreteCarreteiro);
		}

		return null;
	}


	/**
	 * Executado na tela de manter ReciboFreteCarreteiro
	 * 
	 * @param idReciboFreteCarreteiro
	 * @param observacao
	 * @param usuario
	 * @return
	 */
	public ReciboFreteCarreteiro executeWorkflowPendencia(ReciboFreteCarreteiro recibofreteCarreteiro) {    	
    	
    	DomainValue rejeitado = domainValueService.findDomainValueByValue(DOMINIO_STATUS_WORKFLOW, WORFLOW_REJEITADO);
    	//se já possui uma pendencia que esta aguardando aprovação não cria outra.
    	if(recibofreteCarreteiro.getPendencia() == null || (recibofreteCarreteiro.getPendencia() != null && rejeitado.equals(recibofreteCarreteiro.getPendencia().getTpSituacaoPendencia()))){
    		Pendencia pendencia = executeWorkflowPendenciaWorkFlow(recibofreteCarreteiro);
    		
    		recibofreteCarreteiro.setPendencia(pendencia);    	    	
    		recibofreteCarreteiro.setDhEmissao(null);
    		
    		recibofreteCarreteiro = atualizaSituacaoRecibo(recibofreteCarreteiro, WORFLOW_EM_APROVACAO);
    		
        	reciboFreteCarreteitoService.store(recibofreteCarreteiro);
    	}
    	    
    	return recibofreteCarreteiro;
    }
    
	
	private Pendencia executeWorkflowPendenciaWorkFlow(ReciboFreteCarreteiro reciboFreteCarreteiro) {
		Short nrTipoEvento = ConstantesWorkflow.NR2505_APROVACAO_RECIBO_FRETE_CARRETEIRO;
		String mensagemWorkflow = getDsProcessoWorkflowParaReciboFreteCarreteiro(reciboFreteCarreteiro);

		return getPendencia(reciboFreteCarreteiro, nrTipoEvento, mensagemWorkflow);
	}


	/**
	 * @param reciboFreteCarreteiro
	 * @param nrTipoEvento
	 * @param mensagemWorkflow
	 * @return
	 */
	private Pendencia getPendencia(ReciboFreteCarreteiro reciboFreteCarreteiro, Short nrTipoEvento, String mensagemWorkflow) {
		Pendencia pendencia = null;
		try{
			pendencia = workflowPendenciaService.generatePendencia(reciboFreteCarreteiro.getFilial().getIdFilial(), nrTipoEvento, reciboFreteCarreteiro.getIdReciboFreteCarreteiro(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual(),SessionUtils.getUsuarioLogado(), SessionUtils.getEmpresaSessao().getIdEmpresa());
		}catch(Exception e){		
			
			EventoWorkflow eventoWorkflow = eventoWorkflowService.findByTipoEvento(nrTipoEvento);
			
			List<Pendencia> pendencias = pendenciaService.findPendenciaByEvento(reciboFreteCarreteiro.getIdReciboFreteCarreteiro(), eventoWorkflow.getIdEventoWorkflow(), WORFLOW_EM_APROVACAO);
			if(pendencias.isEmpty()){
				pendencia = workflowPendenciaService.generatePendenciaSemEmail(reciboFreteCarreteiro.getFilial(), SessionUtils.getUsuarioLogado(), SessionUtils.getEmpresaSessao(), nrTipoEvento, reciboFreteCarreteiro.getIdReciboFreteCarreteiro(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
			}else{
				pendencia = pendencias.get(0);
			}
			
		}
		return pendencia;
	}
	
	private String getDsProcessoWorkflowParaReciboFreteCarreteiro(ReciboFreteCarreteiro recibofreteCarreteiro) {
		List<String> parametros = new ArrayList<String>();
		
		if(recibofreteCarreteiro.getFilial().getSgFilial() == null){
			 Filial filial = filialService.findById(recibofreteCarreteiro.getFilial().getIdFilial());
			 parametros.add(filial.getSgFilial());
		}else{
			parametros.add(recibofreteCarreteiro.getFilial().getSgFilial());
		}
		
		parametros.add(recibofreteCarreteiro.getNrReciboFreteCarreteiro().toString());
		
		String obs = recibofreteCarreteiro.getObReciboFreteCarreteiro() != null ? recibofreteCarreteiro.getObReciboFreteCarreteiro(): "";
		
		parametros.add(obs);
		
		return configuracoesFacade.getMensagem("LMS-25078", parametros.toArray());
		
	}
	
	private ReciboFreteCarreteiro atualizaSituacaoRecibo(ReciboFreteCarreteiro recibofreteCarreteiro,String tpSituacaoAprovacao) {
		if (WORFLOW_REJEITADO.equals(tpSituacaoAprovacao)) {
			recibofreteCarreteiro.setTpSituacaoWorkflow(new DomainValue(WORFLOW_REJEITADO));
			recibofreteCarreteiro.setTpSituacaoRecibo(domainValueService.findDomainValueByValue(DOMINIO_RECIBO_FRETE_CARRETEIRO, REJEITADO));
		} else if (WORFLOW_APROVADO.equals(tpSituacaoAprovacao)) {
			DomainValue aguardandoEnvioJDE = domainValueService.findDomainValueByValue(DOMINIO_RECIBO_FRETE_CARRETEIRO, AGUARDANDO_ENVIO_JDE);
			
			if(aguardandoEnvioJDE.equals(recibofreteCarreteiro.getTpSituacaoRecibo())){
				return recibofreteCarreteiro;
			}
			
			if(reciboAnuarioRfcService.hasAnuarioVinculado(recibofreteCarreteiro)){
				return recibofreteCarreteiro;
			}
			
			recibofreteCarreteiro.setTpSituacaoWorkflow(new DomainValue(WORFLOW_APROVADO));
			recibofreteCarreteiro.setTpSituacaoRecibo(aguardandoEnvioJDE);
			
			recibofreteCarreteiro.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
			
			recibofreteCarreteiro = reciboFreteCarreteitoService.aplicaInss(recibofreteCarreteiro);

			calculoReciboIRRFService.executeCalcularReciboIRRF(recibofreteCarreteiro, Boolean.TRUE);

			gerarRPA(recibofreteCarreteiro);
			
			Object parametro = parametroGeralService.findConteudoByNomeParametro(ATIVA_RATEIO_RFC, false);
			    
		    if(parametro!= null){
		    	if("S".equalsIgnoreCase(String.valueOf(parametro))){
		    		rateioFreteCarreteiroCeService.execute(recibofreteCarreteiro.getIdReciboFreteCarreteiro());
		    	}
		    }
		} else if (WORFLOW_EM_APROVACAO.equals(tpSituacaoAprovacao)) {
			recibofreteCarreteiro.setTpSituacaoWorkflow(new DomainValue(WORFLOW_EM_APROVACAO));
			recibofreteCarreteiro.setTpSituacaoRecibo(domainValueService.findDomainValueByValue(DOMINIO_RECIBO_FRETE_CARRETEIRO, EM_APROVACAO));
		}
		return recibofreteCarreteiro;
	}


	/**
	 * @param recibofreteCarreteiro
	 * @param idProprietario
	 * @param numero
	 */
	private void gerarRPA(ReciboFreteCarreteiro recibofreteCarreteiro) {
		if(proprietarioRpaService.isGerarRPA(recibofreteCarreteiro.getIdReciboFreteCarreteiro())){
			Long idProprietario = recibofreteCarreteiro.getProprietario().getIdProprietario();
			Long numero = proprietarioRpaService.findProximoNumero(idProprietario);
			if(numero == null){
				numero = 0L;
			}
			
			ProprietarioRPA rpa =  new ProprietarioRPA();
			rpa.setIdProprietario(idProprietario);
			rpa.setIdReciboFreteCarreteiro(recibofreteCarreteiro.getIdReciboFreteCarreteiro());
			rpa.setNrRPA(numero+1);
			rpa.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());
			proprietarioRpaService.store(rpa);
		}
	}
	

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public ReciboFreteCarreteiroService getReciboFreteCarreteitoService() {
		return reciboFreteCarreteitoService;
	}

	public void setReciboFreteCarreteitoService(ReciboFreteCarreteiroService reciboFreteCarreteitoService) {
		this.reciboFreteCarreteitoService = reciboFreteCarreteitoService;
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


	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}


	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}


	public RateioFreteCarreteiroCeService getRateioFreteCarreteiroCeService() {
		return rateioFreteCarreteiroCeService;
	}


	public void setRateioFreteCarreteiroCeService(RateioFreteCarreteiroCeService rateioFreteCarreteiroCeService) {
		this.rateioFreteCarreteiroCeService = rateioFreteCarreteiroCeService;
	}

	public void setCalculoReciboIRRFService(CalculoReciboIRRFService calculoReciboIRRFService) {
		this.calculoReciboIRRFService = calculoReciboIRRFService;
	}


	public FilialService getFilialService() {
		return filialService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


	public PendenciaService getPendenciaService() {
		return pendenciaService;
	}


	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}


	public EventoWorkflowService getEventoWorkflowService() {
		return eventoWorkflowService;
	}


	public void setEventoWorkflowService(EventoWorkflowService eventoWorkflowService) {
		this.eventoWorkflowService = eventoWorkflowService;
	}


	public ProprietarioRpaService getProprietarioRpaService() {
		return proprietarioRpaService;
	}


	public void setProprietarioRpaService(ProprietarioRpaService proprietarioRpaService) {
		this.proprietarioRpaService = proprietarioRpaService;
	}


	public ReciboAnuarioRfcService getReciboAnuarioRfcService() {
		return reciboAnuarioRfcService;
	}


	public void setReciboAnuarioRfcService(ReciboAnuarioRfcService reciboAnuarioRfcService) {
		this.reciboAnuarioRfcService = reciboAnuarioRfcService;
	}
}