package com.mercurio.lms.fretecarreteiroviagem.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ProprietarioRPA;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ProprietarioRpaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.RateioFreteCarreteiroCeService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ReciboAnuarioRfcService;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.EventoWorkflow;
import com.mercurio.lms.workflow.model.Ocorrencia;
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
 * @spring.bean
 * id="lms.indenizacoes.executeWorkflowReciboFreteCarreteiroColetaEntregaService"
 */
public class ExecuteWorkflowReciboComplementarFreteCarreteiroColetaEntregaService {

	private static final String EMITIDO = "EM";
	private static final String MENSAGEM_WORKFLOW_2401 = "LMS-24038";
    private static final String MENSAGEM_WORKFLOW_2402 = "LMS-24041";
	private static final String DOMINIO_RECIBO_FRETE_CARRETEIRO = "DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE";
    private static final String DOMINIO_STATUS_WORKFLOW = "DM_STATUS_WORKFLOW";

    private static final String WORFLOW_APROVADO = "A";
    private static final String WORFLOW_REJEITADO = "R";
    private static final String WORFLOW_EM_APROVACAO = "E";
    
    private static final String EM_APROVACAO = "EA";
    private static final String AGUARDANDO_ASSINATURAS = "AA";
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
	private ReciboAnuarioRfcService reciboAnuarioRfcService;
	private ProprietarioRpaService proprietarioRpaService;

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
            
            Short nrTipoEvento = recibofreteCarreteiro.getPendencia().getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
            
            atualizaSituacaoRecibo(recibofreteCarreteiro, tpSituacaoAprovacao,nrTipoEvento);      

            reciboFreteCarreteitoService.store(recibofreteCarreteiro);
        }

        return null;
    }

    private void atualizaSituacaoRecibo(ReciboFreteCarreteiro recibofreteCarreteiro,String tpSituacaoAprovacao, Short nrTipoEvento) {
    	if (WORFLOW_REJEITADO.equals(tpSituacaoAprovacao)) {
            recibofreteCarreteiro.setTpSituacaoWorkflow(new DomainValue(WORFLOW_REJEITADO));
            recibofreteCarreteiro.setTpSituacaoRecibo(domainValueService.findDomainValueByValue(DOMINIO_RECIBO_FRETE_CARRETEIRO, REJEITADO));
        } else if (WORFLOW_APROVADO.equals(tpSituacaoAprovacao)) {
        	atualizaStatusRecibo(recibofreteCarreteiro, nrTipoEvento);
        } else if (WORFLOW_EM_APROVACAO.equals(tpSituacaoAprovacao)) {
            recibofreteCarreteiro.setTpSituacaoWorkflow(new DomainValue(WORFLOW_EM_APROVACAO));
            recibofreteCarreteiro.setTpSituacaoRecibo(domainValueService.findDomainValueByValue(DOMINIO_RECIBO_FRETE_CARRETEIRO, EM_APROVACAO));
        }
	}

	private void atualizaStatusRecibo(ReciboFreteCarreteiro recibofreteCarreteiro, Short nrTipoEvento) {
		if(ConstantesWorkflow.NR2401_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_ANALISTAS.equals(nrTipoEvento)){
			recibofreteCarreteiro.setTpSituacaoWorkflow(new DomainValue(WORFLOW_APROVADO));
		    recibofreteCarreteiro.setTpSituacaoRecibo(domainValueService.findDomainValueByValue(DOMINIO_RECIBO_FRETE_CARRETEIRO, AGUARDANDO_ASSINATURAS));
		}
		if(ConstantesWorkflow.NR2402_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_COORDENACAO.equals(nrTipoEvento)){
			
			
			DomainValue aguardandoEnvioJDE = domainValueService.findDomainValueByValue(DOMINIO_RECIBO_FRETE_CARRETEIRO, AGUARDANDO_ENVIO_JDE);
			
			if(aguardandoEnvioJDE.equals(recibofreteCarreteiro.getTpSituacaoRecibo())){
				return;
			}
			
			if(reciboAnuarioRfcService.hasAnuarioVinculado(recibofreteCarreteiro)){
				return;
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
		    		rateioFreteCarreteiroCeService.execute(recibofreteCarreteiro.getReciboComplementado().getIdReciboFreteCarreteiro());
		    	}
		    }
		}
	}

	/**
     * Executado na tela de manter ReciboComplementarFreteCarreteiro
     *
     * @param recibofreteCarreteiro
     * @return ReciboFreteCarreteiro
     */
    public ReciboFreteCarreteiro executeWorkflowPendencia(ReciboFreteCarreteiro recibofreteCarreteiro) {
        //se já possui uma pendencia que esta aguardando aprovação não cria outra.
        if (gerarPendencia(recibofreteCarreteiro)) {
        	
        	DomainValue emitido = domainValueService.findDomainValueByValue(DOMINIO_RECIBO_FRETE_CARRETEIRO, EMITIDO);
        	
        	boolean statusEmitido = emitido.equals(recibofreteCarreteiro.getTpSituacaoRecibo());
        	
            Short nrTipoEventoNext = null;
            String mensagem = "";
            String tpSituacaoAprovacao = WORFLOW_EM_APROVACAO;
            
            boolean isEventoRejeitado2402 = isEvento2402(recibofreteCarreteiro);
            
        	if(statusEmitido || isEventoRejeitado2402){
            	nrTipoEventoNext = ConstantesWorkflow.NR2402_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_COORDENACAO;            	
            	mensagem = getDsProcessoWorkflowParaReciboFreteCarreteiro(recibofreteCarreteiro,MENSAGEM_WORKFLOW_2402);
            	
            	executeWorkflowPendenciaWorkFlow(recibofreteCarreteiro, mensagem, nrTipoEventoNext);
			} else {
            	nrTipoEventoNext = ConstantesWorkflow.NR2401_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_ANALISTAS;            	
            	mensagem = getDsProcessoWorkflowParaReciboFreteCarreteiro(recibofreteCarreteiro,MENSAGEM_WORKFLOW_2401);
            	
            	if(SessionUtils.isFilialSessaoMatriz()){
            		tpSituacaoAprovacao = WORFLOW_APROVADO;        		        		
    				executeWorkflowPendenciaMatriz(recibofreteCarreteiro, mensagem, nrTipoEventoNext);    				
            	} else {
            		executeWorkflowPendenciaWorkFlow(recibofreteCarreteiro, mensagem, nrTipoEventoNext);
            	}
            }
        	
    		atualizaSituacaoRecibo(recibofreteCarreteiro, tpSituacaoAprovacao, nrTipoEventoNext);        	
        	reciboFreteCarreteitoService.store(recibofreteCarreteiro);        	
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
	
    

	/**
	 * Gera um workflow quando o recibo complementar é inserido pela matriz.
	 * 
	 * @param recibofreteCarreteiro
	 * @param mensagem
	 * @param nrTipoEventoNext
	 */
	private void executeWorkflowPendenciaMatriz(
			ReciboFreteCarreteiro recibofreteCarreteiro, String mensagem,
			Short nrTipoEventoNext) {
		EventoWorkflow eventoWorkflow = workflowPendenciaService.getEventoWorkflowService().findByTipoEvento(nrTipoEventoNext);
		        		
		Ocorrencia ocorrencia = new Ocorrencia();
		ocorrencia.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		ocorrencia.setEventoWorkflow(eventoWorkflow);
		ocorrencia.setFilial(recibofreteCarreteiro.getFilial());
		ocorrencia.setTpSituacaoOcorrencia(this.domainValueService.findDomainValueByValue("DM_STATUS_OCORRENCIA_WORKFLOW","C"));
		ocorrencia.setUsuario(SessionUtils.getUsuarioLogado());
		        		
		Pendencia pendencia = new Pendencia();
		pendencia.setIdProcesso(recibofreteCarreteiro.getIdReciboFreteCarreteiro());
		pendencia.setDsPendencia(mensagem);
		pendencia.setOcorrencia(ocorrencia);
		pendencia.setTpSituacaoPendencia(this.domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW","A"));
		    		        		
		workflowPendenciaService.getOcorrenciaService().store(ocorrencia);		
		workflowPendenciaService.getPendenciaService().store(pendencia);
		
		recibofreteCarreteiro.setPendencia(pendencia);
	}

	private boolean isEvento2402(ReciboFreteCarreteiro recibofreteCarreteiro) {
		boolean isEnventoRejeitado2402 = false ;
		
		if(recibofreteCarreteiro.getPendencia() != null){
			
			Short nrTipoEvento = recibofreteCarreteiro.getPendencia().getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
			
			if(ConstantesWorkflow.NR2402_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_COORDENACAO.equals(nrTipoEvento)){
				isEnventoRejeitado2402 = true;
			}
		}
		return isEnventoRejeitado2402;
	}
    
  
	private boolean gerarPendencia(ReciboFreteCarreteiro recibofreteCarreteiro) {
		 DomainValue rejeitado = domainValueService.findDomainValueByValue(DOMINIO_STATUS_WORKFLOW, WORFLOW_REJEITADO);
		 DomainValue emitido = domainValueService.findDomainValueByValue(DOMINIO_RECIBO_FRETE_CARRETEIRO, EMITIDO);
		 
		 if( recibofreteCarreteiro.getPendencia() != null){
			 Pendencia p = workflowPendenciaService.getPendenciaService().findById(recibofreteCarreteiro.getPendencia().getIdPendencia());
			 recibofreteCarreteiro.setPendencia(p);
		 }
		 
		return recibofreteCarreteiro.getPendencia() == null ||
				(recibofreteCarreteiro.getPendencia() != null && rejeitado.equals(recibofreteCarreteiro.getPendencia().getTpSituacaoPendencia()) || 
				(emitido.equals(recibofreteCarreteiro.getTpSituacaoRecibo())));
	}
    

    private void executeWorkflowPendenciaWorkFlow(ReciboFreteCarreteiro reciboFreteCarreteiro,String mensagemWorkflow,Short nrTipoEvento) {
    	Pendencia p = workflowPendenciaService.generatePendencia(reciboFreteCarreteiro.getFilial().getIdFilial(), nrTipoEvento, reciboFreteCarreteiro.getIdReciboFreteCarreteiro(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
    	reciboFreteCarreteiro.setPendencia(p);
    }
    
    private String getDsProcessoWorkflowParaReciboFreteCarreteiro(ReciboFreteCarreteiro recibofreteCarreteiro, String mensagem) {
        List<String> parametros = new ArrayList<String>();
        
        ReciboFreteCarreteiro rfcComplementado = reciboFreteCarreteitoService.findByIdComplementar(recibofreteCarreteiro.getIdReciboFreteCarreteiro());
        
        parametros.add(rfcComplementado.getReciboComplementado().getFilial().getSgFilial());
        parametros.add(String.valueOf(rfcComplementado.getReciboComplementado().getNrReciboFreteCarreteiro()));        
        parametros.add(rfcComplementado.getReciboComplementado().getFilial().getSgFilial());
     
        String nrRecibo = recibofreteCarreteiro.getNrReciboFreteCarreteiro() == null ? "" : recibofreteCarreteiro.getNrReciboFreteCarreteiro().toString();
		parametros.add(nrRecibo);
        
        String obs = recibofreteCarreteiro.getObReciboFreteCarreteiro() != null ? recibofreteCarreteiro.getObReciboFreteCarreteiro(): "";
        
		parametros.add(obs);

        return configuracoesFacade.getMensagem(mensagem, parametros.toArray());        	
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

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setRateioFreteCarreteiroCeService(RateioFreteCarreteiroCeService rateioFreteCarreteiroCeService) {
		this.rateioFreteCarreteiroCeService = rateioFreteCarreteiroCeService;
	}

	public void setCalculoReciboIRRFService(CalculoReciboIRRFService calculoReciboIRRFService) {
		this.calculoReciboIRRFService = calculoReciboIRRFService;
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
