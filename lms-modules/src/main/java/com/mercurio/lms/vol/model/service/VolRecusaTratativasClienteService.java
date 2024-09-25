package com.mercurio.lms.vol.model.service;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.vol.model.VolContatos;
import com.mercurio.lms.vol.model.VolEmailsRecusa;
import com.mercurio.lms.vol.model.dao.VolRecusasDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volRecusaTratativasClienteService"
 */
public class VolRecusaTratativasClienteService extends CrudService{
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	
	private ParametroGeralService parametroGeralService;
	private ConfiguracoesFacade configuracoesFacade;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private FilialService filialService;
	private VolEmailsRecusaService volEmailsRecusaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private ContatosVolService contatosVolService;
	private IntegracaoJmsService integracaoJmsService;	

	public ContatosVolService getContatosVolService() {
		return contatosVolService;
	}



	public void setContatosVolService(ContatosVolService contatosVolService) {
		this.contatosVolService = contatosVolService;
	}



	public VolEmailsRecusaService getVolEmailsRecusaService() {
		return volEmailsRecusaService;
	}



	public void setVolEmailsRecusaService(
			VolEmailsRecusaService volEmailsRecusaService) {
		this.volEmailsRecusaService = volEmailsRecusaService;
	}


	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}



	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}


	public List findRecusa(Long idRecusa){
		
		return this.getVolRecusasDAO().findRecusa(idRecusa);
	}
		
	
	
	 /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setVolRecusasDAO(VolRecusasDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VolRecusasDAO getVolRecusasDAO() {
        return (VolRecusasDAO) getDao();
    }
    
    /**
     * envia email para o setor de recusa quando a tratativa é feita pelo cliente
     * @param criteria
     */
    public void sendEmailSetorRecusa(TypedFlatMap criteria){
//      Pega a descricao do parametro geral
    	
    	String[] argsAssunto = {criteria.getString("documento")};
    	String assunto = getConfiguracoesFacade().getMensagem("assuntoMailRecusaTratativa", argsAssunto);
        String remetenteLms = (this.getParametroGeralService().findByNomeParametro("REMETENTE_EMAIL_LMS", false)).getDsConteudo();
        String destinatarioEmail = this.getConteudoParametroFilialService().findByNomeParametro(Long.parseLong(criteria.getString("idFilial")), "EMAIL_RECUSA", false).getVlConteudoParametroFilial();    
        String mensagem = "";
    	
    	//Busca a mensagem do "ResourceBundle" afim de tela internacionalizavel...
    	if(!criteria.getString("idRecusa").equals("")) {
    		String[] args = {"<br><br>", criteria.getString("destinatario"), criteria.getString("nrIdentificacao"), criteria.getString("documento"),
    				criteria.getString("dhRecusa"), criteria.getString("motivo"), criteria.getString("observacoes")};
    		mensagem = getConfiguracoesFacade().getMensagem("textoMailRecusaTratativa", args);
    	}
    		
    	sendMail(assunto, remetenteLms, destinatarioEmail, mensagem);	
  }

	private void sendMail(final String assunto, final String remetenteLms, final String destinatarioEmail,final String mensagem) {
		Mail mail = createMail(destinatarioEmail, remetenteLms, assunto, mensagem);
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
	
    public void sendEmailCliente(TypedFlatMap criteria){
    	String[] argsAssunto = {criteria.getString("documento")};
    	String assunto = getConfiguracoesFacade().getMensagem("assuntoMailRecusaTratativa", argsAssunto);
        String remetenteLms = (this.getParametroGeralService().findByNomeParametro("REMETENTE_EMAIL_LMS", false)).getDsConteudo();
        
        //recupera o e-mail do contato
        VolEmailsRecusa volEmailsRecusa = this.getVolEmailsRecusaService().findById(criteria.getLong("emailContato"));        
        VolContatos  volContatos = this.getContatosVolService().findById(volEmailsRecusa.getVolContatos().getIdContato());
        String destinatarioEmail = volContatos.getDsEmail(); 
        	
        //recupera o nome da filial
        List listNmFilial = this.getFilialService().findNmSgFilialByIdFilial(criteria.getLong("idFilial"));
        Iterator it = listNmFilial.iterator();
        Map linha = (Map)it.next();
        String nmFilial = (String)linha.get("pessoa.nmFantasia");
        
        //recupera o telefone da filial  
        TelefoneEndereco telefoneEndereco = this.getTelefoneEnderecoService().findTelefoneByIdPessoa(criteria.getLong("idFilial")); 
        String telefoneFilial = telefoneEndereco.getNrTelefone();
        
        String mensagem = "";
          
    	//Busca a mensagem do "ResourceBundle" afim de tela internacionalizavel...
    	if(!criteria.getString("idRecusa").equals("")) {
    		String[] args = {"<br><br>", criteria.getString("destinatario") + "<br>" , criteria.getString("nrIdentificacao"), criteria.getString("documento"),
    				criteria.getString("dhRecusa"), criteria.getString("motivo"), criteria.getString("observacoes"), nmFilial, telefoneFilial};
    		mensagem = getConfiguracoesFacade().getMensagem("textoMailRecusaCliente", args);
    	}
    		
    	sendMail(assunto, remetenteLms, destinatarioEmail, mensagem);
    			}
    	
    	

	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}



	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}



	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}



	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}



	public FilialService getFilialService() {
		return filialService;
	}



	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public TelefoneEnderecoService getTelefoneEnderecoService() {
		return telefoneEnderecoService;
	}



	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}
