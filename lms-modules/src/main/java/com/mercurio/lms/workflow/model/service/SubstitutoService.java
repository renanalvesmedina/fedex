package com.mercurio.lms.workflow.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.workflow.model.Substituto;
import com.mercurio.lms.workflow.model.dao.SubstitutoDAO;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import org.apache.commons.lang.StringUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.workflow.substitutoService"
 */
public class SubstitutoService extends CrudService<Substituto, Long> {

	private static final String TEXT_HTML= "text/html; charset='utf-8'";

	private ConfiguracoesFacade configuracoesFacade;
	private ParametroGeralService parametroGeralService;
	private UsuarioService usuarioService;
	private IntegracaoJmsService integracaoJmsService;
	
	/**
	 * Recupera uma inst�ncia de <code>Substituto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public Substituto findById(java.lang.Long id) {
        return (Substituto)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Substituto bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setSubstitutoDAO(SubstitutoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private SubstitutoDAO getSubstitutoDAO() {
        return (SubstitutoDAO) getDao();
    }
    
    protected Substituto beforeStore(Substituto bean) {
        
        Substituto substituto = (Substituto)bean;
        
        List substitutos = this.getSubstitutoDAO().findSubstitutos(substituto);
        
        if( !substitutos.isEmpty() ){
            throw new BusinessException("LMS-39004");
        }    
        
        if(bean.getUsuarioByIdUsuarioSubstituido() != null) {        
	        // LMS-7781 - Valida se o usu�rio substitu�do est� substituindo outro usu�rio no per�odo idicado
	        if(this.getSubstitutoDAO().validateSubstitutoEstaComoSubstitutoDeOutroUsuario(bean)) {
	        	throw new BusinessException("LMS-39025", new String[] { bean.getDtSubstituicaoInicial().toString("dd/MM/yyyy") ,bean.getDtSubstituicaoFinal().toString("dd/MM/yyyy") });
	        }        
        }
        
        // LMS-7781 - Valida se o usu�rio substituto est� ausente no per�odo indicado
        if(this.getSubstitutoDAO().validatePeriodoDeAusenciaSubstituto(bean)) {
        	throw new BusinessException("LMS-39024", new String[] { bean.getDtSubstituicaoInicial().toString("dd/MM/yyyy") ,bean.getDtSubstituicaoFinal().toString("dd/MM/yyyy") });
        }
        
        this.getSubstitutoDAO().getAdsmHibernateTemplate().equals(substituto);
        substituto = null;
        
        if(bean.getUsuarioByIdUsuarioSubstituido() != null) {
        	sendEmailSubstituicaoUsuario(bean);
        }
        
        return super.beforeStore(bean);
    }
    
    /**
     * M�todo respons�vel por buscar os substitutos de acordo com os filtros
     * @param criteria
     * @return ResultSetPage
     */
    public ResultSetPage findPaginated(TypedFlatMap criteria){
    	return getSubstitutoDAO().findPaginated(criteria);
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	return getSubstitutoDAO().getRowCount(criteria);
    }   
    
    /**
     * Envia e-mail para o usu�rio (substituto) informando que ele foi indicado como usu�rio substituto e uma c�pia para o usu�rio substitu�do
     * @param substituto
     */
    public void sendEmailSubstituicaoUsuario(Substituto substituto) {    	
    	Usuario usuarioSubstituto = usuarioService.findById(substituto.getUsuarioByIdUsuarioSubstituto().getIdUsuario());
    	Usuario usuarioSubstituido = usuarioService.findById(substituto.getUsuarioByIdUsuarioSubstituido().getIdUsuario());
    	
		if(StringUtils.isBlank(usuarioSubstituto.getDsEmail())) {
			throw new BusinessException("LMS-39026");
		}

    	String destinatarioEmail = usuarioSubstituto.getDsEmail().toString();
    	String remetenteLms = (this.getParametroGeralService().findByNomeParametro("REMETENTE_EMAIL_LMS", false)).getDsConteudo();
    	String assunto = getConfiguracoesFacade().getMensagem("assuntoEmailSubstituto");
    	String strCc = usuarioSubstituido.getDsEmail().toString();
    	String[] args = {usuarioSubstituido.getNmUsuario().toString(), substituto.getDtSubstituicaoInicial().toString("dd/MM/yyyy"), substituto.getDtSubstituicaoFinal().toString("dd/MM/yyyy")};
    	String mensagem = getConfiguracoesFacade().getMensagem("textoMailSubstituicaoUsuario", args);
    	
    	sendEmail(destinatarioEmail, remetenteLms, assunto, strCc, mensagem);
    }
    
    /**
     * 
     * @param strEmail
     * @param strSubject
     * @param strFrom
     * @param strCc
     * @param strText
     */
	private void sendEmail(final String destinatarioEmail, final String remetenteLms , final  String assunto, final String strCc, final String mensagem) {

		Mail mail = createMail(destinatarioEmail, strCc, remetenteLms, assunto, mensagem);
		
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}
	
	private Mail createMail(String strTo, String strCc, String strFrom, String strSubject, String body) {

		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setCc(strCc);
		mail.setSubject(strSubject);
		mail.setBody(body);

		return mail;
	}
	
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
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
