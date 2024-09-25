package com.mercurio.lms.portaria.model.service.utils;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import com.mercurio.adsm.framework.model.RecursoMensagem;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * LMSA-1002 - Classe utilitária para geração e envio de e-mail de pré-alerta de
 * {@link Manifesto} aéreo do processo "Informar Saída na Portaria".
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class EmailPreAlertaManifestoBuilder {

	/**
	 * Builder para geração e envio de e-mail de pré-alerta de {@link Manifesto}
	 * aéreo.
	 * 
	 * @return
	 */
	public static EmailPreAlertaManifestoBuilder newEmailPreAlertaManifestoBuilder() {
		return new EmailPreAlertaManifestoBuilder();
	}

	private ConfiguracoesFacade configuracoesFacade;
	private IntegracaoJmsService integracaoJmsService;

	private Manifesto manifesto;
	private List<DoctoServico> doctoServicos;

	private EmailPreAlertaManifestoBuilder() {}

	/**
	 * Injeta serviço necessário para busca de {@link ParametroGeral} e
	 * {@link RecursoMensagem}.
	 * 
	 * @param configuracoesFacade
	 * @return
	 */
	public EmailPreAlertaManifestoBuilder setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
		return this;
	}

	/**
	 * Injeta serviço necessário para envio de e-mails utilizando fila de
	 * integração JMS {@code mail-sender-service.Mail}.
	 * 
	 * @param integracaoJmsService
	 * @return
	 */
	public EmailPreAlertaManifestoBuilder setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
		return this;
	}

	/**
	 * Prepara e envia e-mail de pré-alerta para determinado {@link Manifesto},
	 * listando {@link DoctoServico}s relacionados.
	 * 
	 * @param manifesto
	 * @param doctoServicos
	 */
	public void sendPreAlertaManifesto(Manifesto manifesto, List<DoctoServico> doctoServicos) {
		this.manifesto = manifesto;
		this.doctoServicos = doctoServicos;

		Set<String> emails = getEmailContatos(manifesto.getFilialByIdFilialDestino().getPessoa());
		if (emails.isEmpty()) {
			return;
		}

		integracaoJmsService.storeMessage(
				integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, createEmail(emails)));
	}

	private Set<String> getEmailContatos(Pessoa pessoa) {
		Set<String> emails = new HashSet<String>();
		@SuppressWarnings("unchecked")
		List<Contato> contatos = pessoa.getContatosByIdPessoaContatado();
		for (Contato contato : contatos) {
			if (validarContatoAereoEmail(contato)) {
				emails.add(StringUtils.trim(contato.getDsEmail()));
			}
		}
		return emails;
	}

	private boolean validarContatoAereoEmail(Contato contato) {
		String tpContato = contato.getTpContato().getValue();
		return ConstantesPortaria.TP_CONTATO_AEREO.equals(tpContato) && !StringUtils.isBlank(contato.getDsEmail());
	}

	private Mail createEmail(Set<String> emails) {
		Mail mail = new Mail();
		mail.setFrom(getMailFrom());
		mail.setTo(getMailTo(emails));
		mail.setSubject(getMailSubject());
		mail.setBody(getMailBody());
		return mail;
	}

	private String getMailFrom() {
		return (String) configuracoesFacade.getValorParametro(ConstantesPortaria.PARAMETRO_REMETENTE_EMAIL_LMS);
	}

	private String getMailTo(Set<String> emails) {
		return StringUtils.join(emails.toArray(), ";");
	}

	private String getMailSubject() {
		return new StringBuilder()
				.append(configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_PRE_ALERTA_EMBARQUE))
				.append(" - ")
				.append(configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_REFERENTE_MANIFESTO))
				.append(" ")
				.append(FormatUtils.formatSgFilialWithLong(
						manifesto.getFilialByIdFilialOrigem().getSgFilial(),
						manifesto.getManifestoViagemNacional().getNrManifestoOrigem().longValue()))
				.toString();
	}

	private String getMailBody() {
		StringBuilder stringBuilder = new StringBuilder()
				.append(configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_LOGISTICS_MANAGEMENT_SYSTEM))
				.append(ConstantesPortaria.LINE_SEPARATOR)
				.append(configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_PRE_ALERTA_EMBARQUE))
				.append(ConstantesPortaria.LINE_SEPARATOR)
				.append(configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_DATA))
				.append(": ")
				.append(JTDateTimeUtils.formatDateYearMonthDayToString(JTDateTimeUtils.getDataAtual()))
				.append(ConstantesPortaria.LINE_SEPARATOR)
				.append(ConstantesPortaria.LINE_SEPARATOR)
				.append(configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_TEXTO_EMAIL_MANIFESTO, new String[] {
					manifesto.getFilialByIdFilialOrigem().getSgFilial(),
					FormatUtils.formatLongWithZeros(manifesto.getManifestoViagemNacional().getNrManifestoOrigem().longValue()),
					manifesto.getFilialByIdFilialDestino().getSgFilial()
				}))
				.append(ConstantesPortaria.LINE_SEPARATOR)
				.append(ConstantesPortaria.LINE_SEPARATOR)
				.append(String.format(
						"%-16s  %-10s  %-8s  %s",
						configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_DOCUMENTO) + ":",
						configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_DPE) + ":",
						configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_VOLUMES) + ":",
						configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_SERVICO) + ":"))
				.append(ConstantesPortaria.LINE_SEPARATOR);
		for (DoctoServico doctoServico : doctoServicos) {
			stringBuilder
					.append(getDoctoServicoString(doctoServico))
					.append(ConstantesPortaria.LINE_SEPARATOR);
		}
		return stringBuilder
				.append(ConstantesPortaria.LINE_SEPARATOR)
				.append(ConstantesPortaria.LINE_SEPARATOR)
				.append(configuracoesFacade.getMensagem(ConstantesPortaria.MENSAGEM_AVISO_EMAIL_MANIFESTO))
				.toString();
	}

	private String getDoctoServicoString(DoctoServico doctoServico) {
		String nrDocumento = FormatUtils.formatSgFilialWithLong(
				doctoServico.getFilialByIdFilialOrigem().getSgFilial(), doctoServico.getNrDoctoServico());
		String sgFilialDestino = doctoServico.getFilialByIdFilialDestino().getSgFilial();
		String dtPrevEntrega = JTDateTimeUtils.formatDateYearMonthDayToString(doctoServico.getDtPrevEntrega());
		String psReal = FormatUtils.formatDecimal("##,###.000", doctoServico.getPsReal(), true);
		String dsServico = doctoServico.getServico() != null
				? doctoServico.getServico().getDsServico().getValue() : "";
		return String.format(
				"%-16s  %-10s  %-8s  %s", nrDocumento + " " + sgFilialDestino, dtPrevEntrega, psReal, dsServico);
	}

}
