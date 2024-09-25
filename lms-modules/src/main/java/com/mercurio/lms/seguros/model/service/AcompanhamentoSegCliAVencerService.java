package com.mercurio.lms.seguros.model.service;

import java.util.ArrayList;
import java.util.List;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.SeguroCliente;
import com.mercurio.lms.vendas.model.dao.SeguroClienteDAO;

/** 
 * @spring.bean id="lms.seguros.acompanhamentoSegCliAVencerService"
 */
@Assynchronous
public class AcompanhamentoSegCliAVencerService extends CrudService<SeguroCliente, Long>{
	
	private static final String TEXT_PLAIN = "text/plain; charset='utf-8'";
	private ConfiguracoesFacade configuracoesFacade;
	private UsuarioService usuarioService;
	private IntegracaoJmsService integracaoJmsService;
	
	/**
	 * Batch de acompanhamento de seguros de cliente a vencer
	 * 
	 * Jira LMS-6150
	 */
	@AssynchronousMethod(name = "seguroCliente.executeAcompanhamentoSegCliAVencer", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeAcompanhamentoSegCliAVencer() {
		executeGeracaoAcompanhamento();
	}

	public void executeGeracaoAcompanhamento() {
		List<Object[]> result = this.getSeguroClienteDAO().findApolicesAVencer();
		
		if(result != null && !result.isEmpty()) {
		
			List<SeguroCliente> apolicesAVencer = new ArrayList<SeguroCliente>();
			Long idUsuarioAviso = 0L;
			String dsEmail = null;
			
			for (int i = 0; i < result.size(); i++) {
				
				final Object[] row = (Object[]) result.get(i);
				final Long idUsuarioAvisoLoop = (row)[1] == null ? 0 : (Long)(row)[1];
				
				// Se for o primeiro registro
				if(i == 0) {
					// Seta os valores nas variáveis
					idUsuarioAviso = (row)[1] == null ? 0 : (Long)(row)[1];
					dsEmail = (row)[2] == null ? null : (String)(row)[2];
				}
				// Se é um usuário dirente do usuário corrente
				else if(!idUsuarioAvisoLoop.equals(idUsuarioAviso)) {
					enviaEmailAcompanhamento(idUsuarioAviso, dsEmail, apolicesAVencer);
					
					// Carrega as variáveis de acordo com o usuário do loop
					idUsuarioAviso = idUsuarioAvisoLoop;
					dsEmail = (row)[2] == null ? null : (String)(row)[2];
					apolicesAVencer = new ArrayList<SeguroCliente>();
				}			
				
				// Busca informações do seguro
				final Long idSeguroCliente = (row)[0] == null ? null : (Long)(row)[0];
				// Adiciona seguro na lista
				apolicesAVencer.add((SeguroCliente)this.findById(idSeguroCliente));
				
				// Se for o último registro
				if (i == result.size() - 1) {
					enviaEmailAcompanhamento(idUsuarioAviso, dsEmail, apolicesAVencer);
				}
			}
		}
	}

	/**
	 * Atualiza nos seguros de cliente a informação de data e hora de envio do aviso
	 * 
	 * Jira LMS-6150
	 * 
	 * @param idUsuarioAviso
	 * @param segurosCliente
	 */
	private void atualizaDhEnvioAviso(Long idUsuarioAviso, List<SeguroCliente> segurosCliente) {
		for (SeguroCliente seguroCliente : segurosCliente) {
			seguroCliente.setDhEnvioAviso(JTDateTimeUtils.getDataAtual().toDateTimeAtCurrentTime());
			
			if(idUsuarioAviso != null)
				seguroCliente.setUsuarioAviso(getUsuarioService().findById(idUsuarioAviso));
			
			this.store(seguroCliente);
		}
	}
	
	/**
	 * Envia e-mail com seguros de cliente à vencer para seu responsável
	 * 
	 * Jira LMS-6150
	 * 
	 * @param idUsuarioAviso
	 * @param dsEmail
	 * @param segurosCliente
	 */
	private void enviaEmailAcompanhamento(final Long idUsuarioAviso, final String dsEmail, List<SeguroCliente> segurosCliente) {

		// Atualiza a data de envio do aviso
		atualizaDhEnvioAviso(idUsuarioAviso, segurosCliente);
		
		StringBuilder itensEmail = new StringBuilder();
		
		// Monta a lista de itens
		for (SeguroCliente seguroCliente : segurosCliente) {
			itensEmail.append(FormatUtils.formatIdentificacao(seguroCliente.getCliente().getPessoa())).append("\t")
				.append(seguroCliente.getTipoSeguro().getSgTipo()).append("\t")
				.append(seguroCliente.getTpModal().getDescription()).append("\t")
				.append(seguroCliente.getTpAbrangencia().getDescription()).append("\t")
				.append(JTDateTimeUtils.formatDateYearMonthDayToString(seguroCliente.getDtVigenciaInicial())).append("\t")
				.append(JTDateTimeUtils.formatDateYearMonthDayToString(seguroCliente.getDtVigenciaFinal())).append("\t")
				.append(seguroCliente.getCliente().getPessoa().getNmPessoa()).append("\t")
				.append("\n");
		}
		
		// Monta o corpo do e-mail
		final StringBuilder emailText = new StringBuilder()
			.append(formatEmail(configuracoesFacade.getMensagem("LMS-22028"))).append("\n\n") // Introdução
			.append(formatEmail(configuracoesFacade.getMensagem("LMS-22029"))).append("\n") // Cabeçalho
			.append(itensEmail).append("\n\n") // Itens
			.append(formatEmail(configuracoesFacade.getMensagem("LMS-22030"))); // Conclusão
		
		// Envia o e-mail
		Mail mail = createMail(dsEmail, (String)configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS"), configuracoesFacade.getMensagem("LMS-22027"), emailText.toString());
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_PLAIN);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		return mail;
	}
	
	/**
	 * Força a formatação do texto que veio das mensagens da base de dados
	 * @param text Texto a ser formatado
	 * @return
	 */
	public String formatEmail(String text) {
		return text.replace("\\r", "\r").replace("\\n", "\n").replace("\\t", "\t");
	}
	
	public SeguroClienteDAO getSeguroClienteDAO() {
		return (SeguroClienteDAO)this.getDao();
	}

	public void setSeguroClienteDAO(SeguroClienteDAO dao) {
		this.setDao(dao);
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}
