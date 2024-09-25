package com.mercurio.lms.workflow.model.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.EmailEventoUsuario;
import com.mercurio.lms.workflow.model.EventoWorkflow;
import com.mercurio.lms.workflow.model.Mensagem;
import com.mercurio.lms.workflow.model.Pendencia;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.workflow.gerarEmailMensagemAvisoService"
 */
public class GerarEmailMensagemAvisoService {
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private static final Short QTD_PENDENCIA_INICIAL = Short.valueOf("1");
	private MensagemService mensagemService;
	private UsuarioService usuarioService;
	private EventoWorkflowService eventoWorkflowService;
	private EmailEventoUsuarioService emailEventoUsuarioService;
	private ConfiguracoesFacade configuracoesFacade;
	private IntegracaoJmsService integracaoJmsService; 
	private Log log = LogFactory.getLog(GerarEmailMensagemAvisoService.class);

	/**
	 * Gera mensagems para os usuarios da filial
	 * 
	 * @param Long idFilial
	 * @param Short nrTipoEvento
	 * @param Long idUsuario
	 * @param Long idPerfil
	 * @param Short qtdPendencias 
	 */
	public void gerarEmailMensagemUsuarioFilial(Long idEventoWorkflow, Long idFilial, String dsTipoEvento, List dssProcesso, String sgFilial) {
		List emailEventosUsuarios = emailEventoUsuarioService.findByEvento(idEventoWorkflow, idFilial);

		String strMensagemTemplate = "{0}: {1} - {2} - {3}: {4}\n";
		StringBuffer strMensagem = new StringBuffer(2000);
		MessageFormat mfMsg = new MessageFormat(strMensagemTemplate);

		// Cria mensagem com todas as pendências
		final String filialLabel = configuracoesFacade.getMensagem("filial"); 
		for(Iterator iter_proc = dssProcesso.iterator(); iter_proc.hasNext();) {
			
			mfMsg.format(new Object[] {configuracoesFacade.getMensagem("evento"),
							dsTipoEvento,
							(String)iter_proc.next(),
							filialLabel,
							sgFilial}, strMensagem, null);
		}

		//Por cada um dos usuarios
		for(Iterator iter = emailEventosUsuarios.iterator(); iter.hasNext();){
			final EmailEventoUsuario emailEventoUsuario = (EmailEventoUsuario)iter.next();
			final Usuario usuario = emailEventoUsuario.getUsuario();
			//Quando tem email, mandar.
			if (StringUtils.isNotBlank(usuario.getDsEmail())) {				
				this.sendEmailUsuario(usuario.getDsEmail(),
						mountSubjectEmailWorkflow(dsTipoEvento),
						strMensagem.toString());
			}
		}
	}	

	/**
	 * Considera a quantidade de pêndencia inicial igual a 1 (um).
	 * Sempre busca os substitutos.
	 * 
	 * @param idFilial
	 * @param nrTipoEvento
	 * @param usuario
	 * @param perfil
	 */
	public void gerarEmailMensagem(Long idFilial, Short nrTipoEvento, Usuario usuario, Perfil perfil, Pendencia... pendencias){				
		gerarEmailMensagem(idFilial, null, nrTipoEvento, usuario, perfil, QTD_PENDENCIA_INICIAL, true, pendencias);
	}

	/**
	 * Gera uma mensagem ou manda um email para o usuario, perfil e substituto, permite informar o numero da pêndencia.
	 * Sempre busca os substitutos.
	 * 
	 * @param Long idFilial
	 * @param Short nrTipoEvento
	 * @param Long idUsuario
	 * @param Long idPerfil 
	 * @param Short qtdPendencias
	 */
	public void gerarEmailMensagem(Long idFilial, Long idEmpresa, Short nrTipoEvento, Usuario usuario, Perfil perfil, Short qtdPendencias, Pendencia... pendencias){				
		gerarEmailMensagem(idFilial, idEmpresa, nrTipoEvento, usuario, perfil, qtdPendencias, true, pendencias);
	}

	/**
	 * Considera a quantidade de pendencia inicial igual a 1 (um).
	 * 
	 * @param idFilial
	 * @param nrTipoEvento
	 * @param usuario
	 * @param perfil
	 * @param blBuscarSubstituto
	 */
	public void gerarEmailMensagem(Long idFilial, Short nrTipoEvento, Usuario usuario, Perfil perfil, boolean blBuscarSubstituto, Pendencia... pendencias) {		
		gerarEmailMensagem(idFilial, null, nrTipoEvento, usuario, perfil, QTD_PENDENCIA_INICIAL, blBuscarSubstituto, pendencias);
	}

	/**
	 * Gera a mensagem de email.
	 * 
	 * @param idFilial
	 * @param nrTipoEvento
	 * @param usuario
	 * @param perfil
	 * @param qtdPendencias
	 * @param blBuscarSubstituto
	 */
	public void gerarEmailMensagem(Long idFilial, Long idEmpresa, Short nrTipoEvento, Usuario usuario, Perfil perfil, Short qtdPendencias , boolean blBuscarSubstituto, Pendencia...pendencias) {		
		EventoWorkflow eventoWorkflow = eventoWorkflowService.findByTipoEvento(nrTipoEvento);

		if (eventoWorkflow == null){
			return;
		}
		if(idEmpresa == null) {
			idEmpresa = SessionUtils.getEmpresaSessao().getIdEmpresa();
		}
		
		gerarEmailMensagem(idFilial, idEmpresa, eventoWorkflow, usuario, perfil, qtdPendencias, blBuscarSubstituto, pendencias);
	}

	/**
	 * Gera uma mensagem ou manda um email para o usuario, perfil e substituto.
	 * 
	 * @param Long idFilial
	 * @param Short nrTipoEvento
	 * @param Long idUsuario
	 * @param Long idPerfil
	 * @param Short qtdPendencias
	 * @param boolean blBuscarSubstituto
	 */
	public void gerarEmailMensagem(Long idFilial, 
			Long idEmpresa,
			EventoWorkflow eventoWorkflow, 
			Usuario usuario, 
			Perfil perfil, 
			Short qtdPendencias, 
			boolean blBuscarSubstituto,
			Pendencia... pendencias
	) {
		final String tpAlerta = eventoWorkflow.getTpAlerta().getValue();
		List<Usuario> listUsuarios = new ArrayList<Usuario>();

		if (perfil != null && perfil.getIdPerfil() != null) {
			listUsuarios = usuarioService.findUsuariosByPerfil(
					perfil.getIdPerfil(), 
					idFilial,
					SessionUtils.getEmpresaSessao().getIdEmpresa()
			);
		} else if (usuario != null && usuario.getIdUsuario() != null) {
			listUsuarios.add(usuarioService.findById(usuario.getIdUsuario()));
		} else {
			throw new BusinessException("LMS-39007");
		}

		if (listUsuarios.size() <= 0){
			return;			
		}

		if (blBuscarSubstituto) {
			Long idPerfil = null;
			if (perfil != null) {
				idPerfil = perfil.getIdPerfil();
			} // caso o idperfil seja nulo irá gerar erro na rotina a seguir. 
			List<Usuario> usuariosSubstituto = usuarioService.findSubstitutoByUsuarios(listUsuarios, idPerfil, idFilial, idEmpresa);
			
			if (usuariosSubstituto.size() > 0){
				listUsuarios.addAll(usuariosSubstituto);
			}
		}

		if (tpAlerta.equals("E")) { //Se é mandar email
			// Caso exista o perfil, os emails devem ser enviados sem duplicações.
			if (perfil != null && perfil.getIdPerfil() != null) {
				sendEmailByPerfil(listUsuarios, eventoWorkflow, pendencias);			
			// Caso não seja perfil, manda para cada integrante do comitê.
			}else {
				sendEmailToUsuarios(listUsuarios, eventoWorkflow, pendencias);	
			}
		} else if (tpAlerta.equals("M")) { //Se é gravar uma mensagem
			for(Iterator<Usuario> iter = listUsuarios.iterator(); iter.hasNext();){
				Usuario usuarioMsg = iter.next();
				storeMensagem(usuarioMsg, qtdPendencias);
			}						
		}
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 24/07/2007
	 *
	 * @param qtdPendencias
	 * @param listUsuarios
	 *
	 */
	private void sendEmailToUsuarios(List<Usuario> listUsuarios, EventoWorkflow eventoWorkflow, Pendencia[] pendencias) {
		for(Iterator<Usuario> iter = listUsuarios.iterator(); iter.hasNext();){
			Usuario usuarioEmail = iter.next();
			// Chama a rotina de envio de e-mail somente quando o e-mail do usuário estiver cadastrado.
			if (StringUtils.isNotBlank(usuarioEmail.getDsEmail())) {
				sendEmailUsuario(
						usuarioEmail.getDsEmail(), 
						mountSubjectEmailWorkflow(eventoWorkflow.getTipoEvento().getDsTipoEvento().getValue()), 
						mountBodyAprovacaoEmailWorkflow(pendencias));
			}
		}
	}

	/**
	 * Quando o integrante for perfil, não envia email duplicado para usuários que tenham o mesmo email.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 24/07/2007
	 *
	 * @param qtdPendencias
	 * @param emails
	 *
	 */
	private void sendEmailByPerfil(List listUsuarios, EventoWorkflow eventoWorkflow, Pendencia... pendencias) {
		String ultimoEmailEnviado = "";
		String emailAtual;

		// Itera os usuarios.
		for(Iterator<Usuario> iter = listUsuarios.iterator(); iter.hasNext();){
			final Usuario user = iter.next();
			emailAtual = user.getDsEmail();
			if (StringUtils.isBlank(emailAtual)) {
				log.warn("Email não enviado para o usuário: "+user.getNmUsuario()+", campo DsEmail estava vazio!");
			} else {
				// Caso o email anterior seja diferente do atual.
				if (!ultimoEmailEnviado.equals(emailAtual)) {
					// Caso o email não seja nulo nem '', envia o email.
					if (StringUtils.isNotBlank(emailAtual)) {
						String dsTipoEvento = eventoWorkflow.getTipoEvento().getDsTipoEvento().getValue();
						sendEmailUsuario(
								emailAtual, 
								mountSubjectEmailWorkflow(dsTipoEvento), 
								mountBodyAprovacaoEmailWorkflow(pendencias));
					}
					// O email anterior recebe o valor do email atual.
					ultimoEmailEnviado = emailAtual;
				}
			}
		}
	}

	public String mountSubjectEmailWorkflow(String dsTipoEvento){
		return new StringBuilder()
			.append(configuracoesFacade.getMensagem("SUBJECT_MAIL_WORKFLOW"))
			.append(" ")
			.append(dsTipoEvento)
			.toString();
	}

	/**
	 * Monta o corpo do email de workflow.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 25/07/2007
	 *
	 * @param pendencias
	 * @param dsTipoEvento
	 * @param nmSolicitante
	 * @param idPendencia
	 * @param dsPendencia
	 * @return
	 *
	 */
	private String mountBodyAprovacaoEmailWorkflow(Pendencia... pendencias){

		StringBuilder body = new StringBuilder()
		.append(configuracoesFacade.getMensagem("LMS-39007").replaceFirst("&pendencias", pendencias.length+""))
		.append("<BR>");

		String mensagem = configuracoesFacade.getMensagem("BODY_MAIL_WORKFLOW");

		// Itera as pendencias para montar o corpo do e-mail.
		for (Pendencia pendencia : pendencias) {
			body.append(
					MessageFormat.format(
						mensagem, 
						new Object[]{
								pendencia.getOcorrencia().getEventoWorkflow().getTipoEvento().getDsTipoEvento(), 
								pendencia.getOcorrencia().getUsuario().getNmUsuario(),
								pendencia.getIdProcesso().toString(),
								pendencia.getDsPendencia()}));
		}
		mensagemAutomatica(body);

		return body.toString();
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 27/07/2007
	 *
	 * @param body
	 *
	 */
	private void mensagemAutomatica(StringBuilder body) {
		body.append("<BR>");
		body.append("<BR><b><i>");
		body.append(configuracoesFacade.getMensagem("LMS-39022"));
	}

	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 27/07/2007 
	 *
	 * @return
	 *
	 */
	public String mountBodyClosePendenciaEmailWorkflow(Pendencia... pendencias) {

		StringBuilder body = new StringBuilder()
			.append(configuracoesFacade.getMensagem("LMS-39021"));

		// Itera as pendencias para montar o corpo do e-mail.
		for (Pendencia pendencia : pendencias) {
			if(StringUtils.isNotBlank(pendencia.getDsPendencia())) {
				body.append(pendencia.getDsPendencia().trim().concat("\n"));
			}
			body.append(configuracoesFacade.getMensagem("situacao"))
			.append(": ")
			.append(pendencia.getTpSituacaoPendencia().getDescription());
		}
		mensagemAutomatica(body);

		return body.toString().replaceAll("\\\\n","<BR>").replaceAll("\\n", "<BR>");
	}

	/**
	 * Manda um email informando do número de pendencia que ele tem 
	 * para o usuario informado.
	 * 
	 * @param Usuario usuario
	 * @param Short qtdPendencias
	 */
	public void sendEmailUsuario(String email, String subject, String text){
		sendEmail(email, subject, configuracoesFacade.getMensagem("FROM_MAIL_WORKFLOW"), text);
	}

	/**
	 * Manda um email informando que a pendência foi fechada para o solicitante do evento.
	 * 
	 * @param Usuario usuario
	 * @param Short qtdPendencias
	 * @deprecated see sendEmail(String strEmail, Short nrTipoEvento, String strText)
	 */
	public void sendEmail(String strEmail, String strText) {
		sendEmail(strEmail, configuracoesFacade.getMensagem("SUBJECT_MAIL_WORKFLOW"), configuracoesFacade.getMensagem("FROM_MAIL_WORKFLOW"), strText);
	}	

	public void sendEmail(String strEmail, Short nrTipoEvento, String strText) {
		EventoWorkflow eventoWorkflow = eventoWorkflowService.findByTipoEvento(nrTipoEvento);
		String dsTipoEvento = "";
		if (eventoWorkflow != null) {
			dsTipoEvento = eventoWorkflow.getTipoEvento().getDsTipoEvento().getValue();
		}
		sendEmail(strEmail, mountSubjectEmailWorkflow(dsTipoEvento), configuracoesFacade.getMensagem("FROM_MAIL_WORKFLOW"), strText);
	}	

	/**
	 * Manda um email.
	 * 
	 * @param String strEmail
	 * @param String strSubject
	 * @param String strFrom
	 * @param String strText
	 */
	public void sendEmail(final String strEmail,final  String strSubject,final  String strFrom,final  String strText){

		Mail mail = createMail(strEmail, strFrom, strSubject, strText.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		
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
	 * Insere ou atualiza a mensagem que informa quantas pendencias o
	 * usuário informado tem.
	 * 
	 * @param Usuario usuario
	 * @param Short qtdPendencias
	 */
	private void storeMensagem(Usuario usuario, Short qtdPendencias){
		Mensagem mensagem = new Mensagem();
		mensagem.setNrPendencia(qtdPendencias);
		mensagem.setUsuario(usuario);
		mensagemService.storeMensagemWithPendencia(mensagem);
	}

	/**
	 * Retorna a mensagem com o número de pendencia que o usuario informado tem.
	 * 
	 * @return String mensagem
	 */
	public String findMensagem() {
		Mensagem mensagem = mensagemService.findByUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		if (mensagem == null) {
			return null;
		}

		String strMensagem = configuracoesFacade.getMensagem("LMS-39007");		
		strMensagem = strMensagem.replaceAll("(&pendencias)",String.valueOf(mensagem.getNrPendencia().toString()));
		strMensagem += configuracoesFacade.getMensagem("LMS-39008");
		mensagemService.removeById(mensagem.getIdMensagem());
		return strMensagem;
	}

	public void setEventoWorkflowService(EventoWorkflowService eventoWorkflowService) {
		this.eventoWorkflowService = eventoWorkflowService;
	}

	public void setMensagemService(MensagemService mensagemService) {
		this.mensagemService = mensagemService;
	}

	public void setEmailEventoUsuarioService(EmailEventoUsuarioService emailEventoUsuarioService) {
		this.emailEventoUsuarioService = emailEventoUsuarioService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setUsurioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}
