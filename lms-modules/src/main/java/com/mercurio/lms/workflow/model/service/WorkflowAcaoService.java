package com.mercurio.lms.workflow.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.Integrante;
import com.mercurio.lms.workflow.model.Ocorrencia;
import com.mercurio.lms.workflow.model.SubstitutoFalta;
import com.mercurio.lms.workflow.model.SubstitutoFaltaAcao;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.workflow.workflowAcaoService"
 */
@Assynchronous
public class WorkflowAcaoService {
	
	private DomainValueService domainValueService;
	
	private AcaoService acaoService;
	
	private PendenciaService pendenciaService;
	
	private SubstitutoFaltaService substitutoFaltaService;
	
	private SubstitutoFaltaAcaoService substitutoFaltaAcaoService;
	
	private GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService;
	
	private ConfiguracoesFacade configuracoesFacade;
	
	private BatchLogger batchLogger;

	public void setBatchLogger(BatchLogger batchLogger) { 
		this.batchLogger = batchLogger; batchLogger.logClass(getClass()); 
	}
	
	/**
	 * Método chamado pelo 'Job Scheduler'.
	 * Caso o tpAcaoAutomatica do EventoWorkflow ele vai:
	 * -Aprovar a ação
	 * -Reprovar a ação
	 * -Tentar achar substitutos falta e inserir registro na tabela substituto_falta_acao
	 * 
	 * Caso a pendência está fechada ele vai fechar a ocorrencia e chamar o método
	 * da classe ação
	 * 
	 * Retorna a mensagem da classe acao chamada. 
	 * 
	 * @return String 
	 * */
	@AssynchronousMethod(name="workflow.AcoesAutomaticas",
			type = BatchType.BATCH_SERVICE,
			feedback = BatchFeedbackType.ON_ERROR)
	public void generateAcoesAutomaticas(){
		//Lista das açoes que tem que virar pendente
		
		final List lstAcoesNaoLiberadas = this.getAcaoService().findAcoesNaoLiberadas();
		batchLogger.info("Inicio de geração de ações automáticas, quantidade de ações não liberadas: "+lstAcoesNaoLiberadas.size());
		
		//Para cada ação não liberada
		for(Iterator iter = lstAcoesNaoLiberadas.iterator(); iter.hasNext();) {
			final Acao acao = (Acao)iter.next();
			
			//setar o blLiberada para 'true'
			acao.setBlLiberada(Boolean.TRUE);
			
			//salvar
			this.getAcaoService().store(acao);
			
			final Integrante integrante = acao.getIntegrante();
			
			//Mandar email para o integrante
			final Ocorrencia ocorrencia = acao.getPendencia().getOcorrencia();
			this.getGerarEmailMensagemAvisoService().gerarEmailMensagem( ocorrencia.getFilial().getIdFilial(), 
																		 ocorrencia.getEventoWorkflow().getTipoEvento().getNrTipoEvento(), 
																		 integrante.getUsuario(), 
																		 integrante.getPerfil(), 
																		 false,
																		 acao.getPendencia());
		}
		
		//Lista das acões pendentes		
		final List lstAcoesPendentes = this.getAcaoService().findAcoesPendente();
		batchLogger.info("Quantidade de ações pendentes: "+lstAcoesPendentes.size());
		
		final String msgAprovado = configuracoesFacade.getMensagem("LMS-39010");
		final String msgReprovado = configuracoesFacade.getMensagem("LMS-39011");

		//Para cada acao pendente
		for(Iterator iter = lstAcoesPendentes.iterator(); iter.hasNext();) {
			
			final Map map = (Map)iter.next();
			final Acao acao = (Acao)map.get("acao");
			final Long idIntegrante = (Long)map.get("idIntegrante");
			final Long idFilial = (Long)map.get("idFilial");
			final Short nrTipoEvento = (Short)map.get("nrTipoEvento");
			final String tpAcaoAutomatica =((DomainValue)map.get("tpAcaoAutomatica")).getValue();
			
			try {
				//Se tem que repassar
				if (tpAcaoAutomatica.equals("RA") ) {
					List lstSubstituto = this.getSubstitutoFaltaService().findSubstitutoFaltaByIntegrante(idIntegrante, acao.getIdAcao());
					
					if (lstSubstituto.size() > 0) {
						this.storeSubstitutoFalta(lstSubstituto, 
												  acao, 
												  idFilial,
												  nrTipoEvento);
					}
					
				} else {

					String obAcao = null;
					//Se tem que aprovar
					if (tpAcaoAutomatica.equals("AP")) {
						obAcao = msgAprovado;
					//Se tem que reprovar
					} else if (tpAcaoAutomatica.equals("RE") ) {
						obAcao = msgReprovado;
					}
					
					String retornoSaveAcao = this.getAcaoService().saveAprovarAcao(acao.getIdAcao(), obAcao);
					if (StringUtils.isNotBlank(retornoSaveAcao)) {
			    		//Mandar um email avisando o solicitante que foi fechado a pendencia.			
		    			final String dsPendencia = (String)map.get("dsPendencia");
						final String dhInclusao = (String)map.get("dhInclusão");
						
						// concatena o texto do email
						StringBuilder dsPendenciaEmail = new StringBuilder(dsPendencia.length()+dhInclusao.length()+4);
						dsPendenciaEmail.append(dsPendencia).append("\n").append(dhInclusao).append("\n").append(retornoSaveAcao);

						final String dsEmail = (String)map.get("dsEmail");
						this.getGerarEmailMensagemAvisoService().sendEmail(dsEmail, dsPendenciaEmail.toString());
					}
				
				}
				
				
			} catch (RuntimeException re) {
				batchLogger.warning("Exceção ao processar ação com id: "+acao.getIdAcao()+" - ("+re.getMessage()+")", re);
			}
		}
		batchLogger.info("Geração de ações automáticas concluída com sucesso.");
	}
	
	/**
	 * Por cada substituto_falta que não tem substituto_falta_acao
	 * cadastrar um substituto_falta_acao e mandar um email ou cadastrar mensagem
	 * 
	 * @param List lstSubstituto
	 * @param Acao acao
	 * @param Long idFilial
	 * @param Short nrTipoEvento
	 * */
	private void storeSubstitutoFalta(List lstSubstituto, Acao acao, Long idFilial, Short nrTipoEvento){
		//Por cada substituto falta livre
		for(Iterator iter = lstSubstituto.iterator(); iter.hasNext();){
			SubstitutoFalta substitutoFalta = (SubstitutoFalta)iter.next();
			
			//Preparar o substituto_falta_acao
			SubstitutoFaltaAcao substitutoFaltaAcao = new SubstitutoFaltaAcao();
			substitutoFaltaAcao.setAcao(acao);
			substitutoFaltaAcao.setSubstitutoFalta(substitutoFalta);
			
			//Salvar o substituto_falta_acao
			this.getSubstitutoFaltaAcaoService().store(substitutoFaltaAcao);
			
			//Mandar email
			this.getGerarEmailMensagemAvisoService().gerarEmailMensagem(idFilial, 
																		nrTipoEvento, 
																		substitutoFalta.getUsuario(), 
																		substitutoFalta.getPerfil(), 
																		false,
																		acao.getPendencia());
		}
	}

	public AcaoService getAcaoService() {
		return acaoService;
	}

	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public GerarEmailMensagemAvisoService getGerarEmailMensagemAvisoService() {
		return gerarEmailMensagemAvisoService;
	}

	public void setGerarEmailMensagemAvisoService(
			GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService) {
		this.gerarEmailMensagemAvisoService = gerarEmailMensagemAvisoService;
	}

	public PendenciaService getPendenciaService() {
		return pendenciaService;
	}

	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public SubstitutoFaltaAcaoService getSubstitutoFaltaAcaoService() {
		return substitutoFaltaAcaoService;
	}

	public void setSubstitutoFaltaAcaoService(
			SubstitutoFaltaAcaoService substitutoFaltaAcaoService) {
		this.substitutoFaltaAcaoService = substitutoFaltaAcaoService;
	}

	public SubstitutoFaltaService getSubstitutoFaltaService() {
		return substitutoFaltaService;
	}

	public void setSubstitutoFaltaService(
			SubstitutoFaltaService substitutoFaltaService) {
		this.substitutoFaltaService = substitutoFaltaService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
}