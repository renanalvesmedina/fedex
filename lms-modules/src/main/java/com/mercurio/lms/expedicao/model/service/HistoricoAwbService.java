package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.HistoricoAwb;
import com.mercurio.lms.expedicao.model.dao.HistoricoAwbDAO;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.historicoAwbService"
 */
public class HistoricoAwbService extends CrudService<HistoricoAwb, Long> {

	private AwbService awbService;
	private ConfiguracoesFacade configuracoesFacade;
	private UsuarioService usuarioService;
	private IntegracaoJmsService integracaoJmsService;
	
	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	
	/**
	 * Recupera uma instância de <code>HistoricoAwb</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public HistoricoAwb findById(java.lang.Long id) {
		return (HistoricoAwb)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(HistoricoAwb bean) {
		return super.store(bean);
	}

	/**
	 * Busca os registros relacionado ao awb recebido por parametro no mapa.
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		return getHistoricoAwbDAO().findPaginated(criteria, findDef);
	}

	/**
	 * Conta o numero de registros retornados pela pesquisa do historico
	 * relacionado ao awb recebido por parametro no mapa.
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCount(TypedFlatMap criteria) {
		return getHistoricoAwbDAO().getRowCount(criteria);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setHistoricoAwbDAO(HistoricoAwbDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private HistoricoAwbDAO getHistoricoAwbDAO() {
		return (HistoricoAwbDAO) getDao();
	}

	public java.io.Serializable storeHistorico(HistoricoAwb historicoAwb) {
		return super.store(historicoAwb);
	}

	public void executeSendMailHistoricoAwb(HistoricoAwb historicoAwb) {
		Awb awb = awbService.findById(historicoAwb.getAwb().getIdAwb());
		Usuario usuario = usuarioService.findById(historicoAwb.getUsuario().getIdUsuario());

		List contatos = getHistoricoAwbDAO().findEmailsContatosByFiliaisDestinoAWB(awb.getIdAwb());
		if (contatos.isEmpty()) {
			throw new BusinessException("LMS-04200");
		}	

		String[] dsEmails = (String[]) contatos.toArray(new String[]{});
		String strFrom = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		String strSubject = (String) configuracoesFacade.getMensagem("ocorrenciaAwb");

		StringBuilder text = new StringBuilder();
		text.append("LMS - Logistics Management System").append(LINE_SEPARATOR);
		text.append(configuracoesFacade.getMensagem("ocorrenciaAwb")).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		text.append(configuracoesFacade.getMensagem("ciaAerea")).append(": ").append(awb.getCiaFilialMercurio().getEmpresa().getPessoa().getNmPessoa()).append(LINE_SEPARATOR);
		text.append(configuracoesFacade.getMensagem("numeroAWB")).append(": ").append(AwbUtils.formatNrAwb(awb.getNrAwb(), awb.getDvAwb())).append(LINE_SEPARATOR);
		text.append(configuracoesFacade.getMensagem("dataHoraOcorrencia")).append(": ").append(JTFormatUtils.format(historicoAwb.getDhInclusao())).append(LINE_SEPARATOR);
		text.append(configuracoesFacade.getMensagem("usuario")).append(": ");
		if (StringUtils.isNotBlank(usuario.getNrMatricula())) {
			text.append(usuario.getNrMatricula()).append(" - ");
		}
		text.append(usuario.getNmUsuario()).append(LINE_SEPARATOR);

		text.append(configuracoesFacade.getMensagem("descricao")).append(": ").append(historicoAwb.getDsHistoricoAwb()).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		text.append(configuracoesFacade.getMensagem("mailHistoricoAwbText1", new Object[]{configuracoesFacade.getValorParametro("ENDERECO_LMS")})).append(LINE_SEPARATOR);

		sendEmail(dsEmails, strSubject, strFrom, text.toString());
	}

	/*
	 * Métodos privados
	 */
	private void sendEmail(final String[] strEmails, final String strSubject,final String strFrom, final String strText){
		Mail mail = createMail(StringUtils.join(strEmails, ";"), strFrom, strSubject, strText);
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
	
	/*
	 * Getters e setter
	 */

	/**
	 * @param awbService The awbService to set.
	 */
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	/**
	 * @param configuracoesFacade The configuracoesFacade to set.
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * @param usuarioService The usuarioService to set.
	 */
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}
