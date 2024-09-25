package com.mercurio.lms.expedicao.model.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.batch.job.JobData;
import com.mercurio.adsm.batch.job.JobSessionData;
import com.mercurio.adsm.batch.job.JobSessionHolder;
import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.ConnectorIntegrationCTE;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;


/**
 * @author JonasFE
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.integracaoNDDigitalService"
 */
public class ProcessamentoRetornoCTEService {
	private static final String TEXT_HTML = "text/html; charset='utf-8'";
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private ProcessamentoRetornoCTEItemV103Service processamentoRetornoCTEItemV103Service;
	private ProcessamentoRetornoMDFeItemV100Service processamentoRetornoMDFeItemV100Service;
	private ConfiguracoesFacade configuracoesFacade;
	private UsuarioADSMService usuarioADSMService;
	private BatchLogger batchLogger;
	private ProcessamentoRetornoCTEItemV200Service processamentoRetornoCTEItemV200Service;
	private IntegracaoJmsService integracaoJmsService;
	
	public void executeProcessamentoRetornosCTE(){
		final StringBuilder strText = new StringBuilder();
		
		List<ConnectorIntegrationCTE> retornos = integracaoNDDigitalService.findRetornosPendentes();
		
		for (ConnectorIntegrationCTE connectorIntegration : retornos) {
            String xml;
            try {
                xml = new String(connectorIntegration.getDocumentData(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                xml = String.valueOf(connectorIntegration.getDocumentData());
            }
            
            try{
            	switch (connectorIntegration.getKind()) {
            		case 0:
        	            if (xml.contains("<chCTe>")) {
	            			//Retorno de emissão
	            			processamentoRetornoCTEItemV103Service.executeLeituraRetornoEmissao(connectorIntegration, xml);
        	            } else if (xml.contains("<chMDFe>")){
        	            	//Retorno de emissão
	                		processamentoRetornoMDFeItemV100Service.executeLeituraRetornoEmissao(connectorIntegration, xml);
        	            }	
            			break;
            		case 1:
            			//Retorno de Cancelamento
            			processamentoRetornoCTEItemV103Service.executeLeituraRetornoCancelamento(connectorIntegration, xml);
            			break;
            		case 2:
            			//Retorno de Inutilização
            			processamentoRetornoCTEItemV103Service.executeLeituraRetornoInutilizacao(connectorIntegration, xml);
            			break;
            		case 7:
            			// LMS-4210
            			if (xml.contains("<chMDFe>")) {
            				//Retorno de Encerramento/Cancelamento
            				processamentoRetornoMDFeItemV100Service.executeLeituraRetornoEvento(connectorIntegration, xml);
            			}
            			// LMS-4210
            			else if (xml.contains("<chCTe>")) {
            				// LMS-5904 - Retorno de evento de carta de correção eletrônica
            				if (xml.contains("<tpEvento>110110</tpEvento>")) {
            					processamentoRetornoCTEItemV200Service.executeLeituraRetornoCartaCorrecaoEletronica(
            							connectorIntegration, xml);
            				} else {
                				//Retorno de Cancelamento
                				processamentoRetornoCTEItemV103Service.executeLeituraRetornoCancelamento(
                						connectorIntegration, xml);
            				}
            			}
                		break;	
            		default:
            			integracaoNDDigitalService.removeByIdConnectorIntegration(connectorIntegration);
            			break;
	            	}
            } catch (Exception e) {
            	strText.append("ConnectorIntegrationCTEID:"+connectorIntegration.getConnectorIntegrationCTEID());
            	Writer w = new StringWriter();
            	PrintWriter pw = new PrintWriter(w);
            	e.printStackTrace(pw);
            	strText.append(" PrintStackTrace:"+w.toString());
    		} 
        }
		
		if(strText.length() > 0){
			StringBuilder strSubject = new StringBuilder("Erros no Processamento Retorno CT-e/MDF-e");
			String strTo = null;
			
			JobSessionData jsd = JobSessionHolder.getJobSessionData();
			JobData jobData = jsd.getJobData();
			
			final UsuarioADSM usuario = usuarioADSMService.findUsuarioADSMByLogin(jobData.getUsername());
			
			if (usuario != null) {
				strTo = usuario.getDsEmail();
				if(strTo != null){
					generateEmailProcessamentoRetornos(strTo, strSubject, strText);
				}else{
					batchLogger.info("Email not found for User: " + jobData.getUsername());
				}
			} else {
				batchLogger.info("User not found with login: " + jobData.getUsername());
			}
		}
	}
	
	private void generateEmailProcessamentoRetornos(final String strTo, final StringBuilder strSubject, final StringBuilder strText) {
		final String strFrom = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		Mail mail = new Mail();
		
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject.toString());
		mail.setBody(strText.toString());
		
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	public void setIntegracaoNDDigitalService(IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}

	public void setProcessamentoRetornoCTEItemV103Service(ProcessamentoRetornoCTEItemV103Service processamentoRetornoCTEItemV103Service) {
		this.processamentoRetornoCTEItemV103Service = processamentoRetornoCTEItemV103Service;
	}

	public void setProcessamentoRetornoMDFeItemV100Service(
            ProcessamentoRetornoMDFeItemV100Service processamentoRetornoMDFeItemV100Service) {
        this.processamentoRetornoMDFeItemV100Service = processamentoRetornoMDFeItemV100Service;
    }
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setUsuarioADSMService(UsuarioADSMService usuarioADSMService) {
		this.usuarioADSMService = usuarioADSMService;
	}
	
	public void setBatchLogger(BatchLogger batchLogger){
		batchLogger.logClass(this.getClass());
		this.batchLogger = batchLogger;
	}
	
	public void setProcessamentoRetornoCTEItemV200Service(
			ProcessamentoRetornoCTEItemV200Service processamentoRetornoCTEItemV200Service) {
		this.processamentoRetornoCTEItemV200Service = processamentoRetornoCTEItemV200Service;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}
