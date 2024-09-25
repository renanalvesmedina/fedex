package com.mercurio.lms.seguros.model.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.seguros.model.dao.ProcessoSinistroDAO;
import com.mercurio.lms.util.FormatUtils;

/**
 * 
 * @spring.bean id="lms.seguros.monitoramentoProcessoSinistroService"
 * 
 * @author lalmeida
 *
 */
@Assynchronous
public class MonitoramentoProcessoSinistroService extends CrudService<ProcessoSinistro, Long>{
	private static final String TEXT_HTML = "text/html; charset='utf-8'";
	
	private ProcessoSinistroDAO processoSinistroDAO;
	private ConfiguracoesFacade configuracoesFacade;
	private IntegracaoJmsService integracaoJmsService;
	
	public ProcessoSinistroDAO getProcessoSinistroDAO() {
		return processoSinistroDAO;
	}

	public void setProcessoSinistroDAO(ProcessoSinistroDAO processoSinistroDAO) {
		this.processoSinistroDAO = processoSinistroDAO;
	}
	
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	/**
	 * Batch de monitoramento de processos de sinistro
	 * 
	 * Jira LMS-6159
	 * 
	 */
	@AssynchronousMethod(name = "seguros.monitoramentoProcessoSinistro", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeMonitoramentoProcessoSinistro() {
		executeMonitoramento();
	}
	
	/**
	 * Rotina de execução do batch
	 * 
	 * Jira LMS-6159
	 * 
	 */
	public void executeMonitoramento() {
		List<Object[]> result = this.getProcessoSinistroDAO().findMonitoramentoProcessoSinistro(new String[] { "login", "tp_situacao" });
		processaDadosByUsuario(result, false);
		
		result = this.getProcessoSinistroDAO().findMonitoramentoProcessoSinistro(new String[] { "tp_situacao" });
		processaDadosByUsuario(result, true);
	}

	/**
	 * Rotina auxiliar para processamento dos dados
	 * 
	 * @param dadosProcessoSinistro
	 * @param enviaEmailSetor
	 */
	private void processaDadosByUsuario(List<Object[]> dadosProcessoSinistro, boolean enviaEmailSetor) {
		
		/*
		[0] id_processo_sinistro LONG
		[1] nr_processo_sinistro STRING
		[2] id_usuario LONG
		[3] login STRING
		[4] ds_email STRING
		[5] qt_documentos LONG
		[6] vl_mercadoria BIG_DECIMAL
		[7] vl_prejuizo BIG_DECIMAL
		[8] dt_abertura TIMESTAMP
		[9] dt_ocorrencia TIMESTAMP
		[10] dt_retificacao TIMESTAMP
		[11] dt_filial_rim TIMESTAMP
		[12] cto_sem_rim STRING
		[13] dt_rim_sem_pagto TIMESTAMP
		[14] tp_situacao STRING
		*/
		
		if(dadosProcessoSinistro != null && !dadosProcessoSinistro.isEmpty()) {
		
			List<Map<String, Object>> listDadosProcessoSinistro = new ArrayList<Map<String, Object>>();
			Long idUsuarioAviso = 0L;
			String dsEmail = null;

			for (int i = 0; i < dadosProcessoSinistro.size(); i++) {
				
				final Object[] row = (Object[]) dadosProcessoSinistro.get(i);
				final Long idUsuarioAvisoLoop = (row)[2] == null ? 0L : (Long)(row)[2];
				
				// Se for o primeiro registro
				if(i == 0) {
					// Seta os valores nas variáveis
					idUsuarioAviso = (row)[2] == null ? 0 : (Long)(row)[2];
					dsEmail = (row)[4] == null ? null : (String)(row)[4];
				} else if(!idUsuarioAvisoLoop.equals(idUsuarioAviso) && !enviaEmailSetor) {
					// Se é um usuário dirente do usuário corrente
					if(dsEmail == null) {
						enviaEmailMonitoramento(dsEmail, listDadosProcessoSinistro);
					}
					
					// Carrega as variáveis de acordo com o usuário do loop
					idUsuarioAviso = idUsuarioAvisoLoop;
					dsEmail = (row)[4] == null ? null : (String)(row)[4];
					listDadosProcessoSinistro = new ArrayList<Map<String, Object>>();
				}
				
				Map<String, Object> dadosSinistro = new HashMap<String, Object>();
				dadosSinistro.put("tpSituacao", row[14] == null ? "" : row[14]);
				dadosSinistro.put("nrProcessoSinistro", row[1] == null ? "" : row[1]);
				dadosSinistro.put("qtDocumentos", row[5] == null ? "" : row[5]);
				dadosSinistro.put("vlMercadoria", row[6] == null ? "" : 
					FormatUtils.formatBigDecimalWithPattern((BigDecimal)row[6], "#,###,###,##0.00"));
				dadosSinistro.put("vlPrejuizo", row[7] == null ? "" : 
					FormatUtils.formatBigDecimalWithPattern((BigDecimal)row[7], "#,###,###,##0.00"));
				dadosSinistro.put("dtAbertura", row[8] == null ? "" : new YearMonthDay((Timestamp)row[8]).toString("dd/MM/yyyy"));
				dadosSinistro.put("dtOcorrencia", row[9] == null ? "" : new YearMonthDay((Timestamp)row[9]).toString("dd/MM/yyyy"));
				dadosSinistro.put("dtRetificacao", row[10] == null ? "" : new YearMonthDay((Timestamp)row[10]).toString("dd/MM/yyyy"));
				dadosSinistro.put("dtFilialRIM", row[11] == null ? "" : new YearMonthDay((Timestamp)row[11]).toString("dd/MM/yyyy"));
				dadosSinistro.put("ctoSemRIM", row[12] == null ? "" : getConfiguracoesFacade()
						.getDomainValue("DM_SIM_NAO", String.valueOf(row[12])).getDescriptionAsString());
				dadosSinistro.put("dtRIMSemPagto", row[13] == null ? "" : new YearMonthDay((Timestamp)row[13]).toString("dd/MM/yyyy"));
				
				// Adiciona dados na lista
				listDadosProcessoSinistro.add(dadosSinistro);
				
				// Se for o último registro
				if (i == dadosProcessoSinistro.size() - 1 && dsEmail != null) {
					enviaEmailMonitoramento(dsEmail, listDadosProcessoSinistro);
				}
			}
		}
	}

	/**
	 * Envia um e-amil de monitoramento de processo de sinistro
	 *
	 * Jira LMS-6159
	 *
	 * @param dsEmail
	 * @param listItensEmail
	 */
	private void enviaEmailMonitoramento(final String dsEmail, List<Map<String, Object>> listItensEmail) {

		StringBuilder textoItensEmail = new StringBuilder();

		final String keyTextoAssunto = "LMS-22039";
		final String keyTextoIntroducao = "LMS-22040";
		final String keyTextoCabecalho = "LMS-22041";
		final String keyTextoConclusao = "LMS-22042";
		final String paramRemetente = "REMETENTE_EMAIL_LMS";

		// Monta a lista de itens
		for (Map<String, Object> item : listItensEmail) {
			textoItensEmail
				.append(item.get("tpSituacao")).append("\t")
				.append(item.get("nrProcessoSinistro")).append("\t")
				.append(item.get("qtDocumentos")).append("\t")
				.append(item.get("vlMercadoria")).append("\t")
				.append(item.get("vlPrejuizo")).append("\t")
				.append(item.get("dtAbertura")).append("\t")
				.append(item.get("dtOcorrencia")).append("\t")
				.append(item.get("dtRetificacao")).append("\t")
				.append(item.get("dtFilialRIM")).append("\t")
				.append(item.get("ctoSemRIM")).append("\t")
				.append(item.get("dtRIMSemPagto")).append("\t")
				.append("\n");
		}

		// Monta o corpo do e-mail
		final StringBuilder emailText = new StringBuilder()
			.append(formatEmail(getMessage(keyTextoIntroducao))).append("\n\n") // Introdução
			.append(formatEmail(getMessage(keyTextoCabecalho))).append("\n") // Cabeçalho
			.append(textoItensEmail).append("\n\n") // Itens
			.append(formatEmail(getMessage(keyTextoConclusao))); // Conclusão

		// Envia o e-mail
		Mail mail = createMail(dsEmail, (String)getConfiguracoesFacade().getValorParametro(paramRemetente), getMessage(keyTextoAssunto), emailText.toString());
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		return mail;
	}

	/**
	 * Força a formatação do texto que veio das mensagens da base de dados
	 * 
	 * Jira LMS-6159
	 * 
	 * @param text Texto a ser formatado
	 * @return
	 */
	private String formatEmail(String text) {
		return text.replace("\\r", "\r").replace("\\n", "\n").replace("\\t", "\t");
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}
