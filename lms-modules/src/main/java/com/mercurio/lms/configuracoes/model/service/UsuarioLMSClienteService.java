/**
 * 
 */
package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ClienteUsuario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.dao.UsuarioLMSClienteDAO;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ManterClienteService;

/**
 * @author juliosce
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * @spring.bean id="lms.configuracoes.usuarioLMSClienteService" 
 */
public class UsuarioLMSClienteService {
	
	private ClienteUsuarioService clienteUsuarioService;
	
	private UsuarioLMSClienteDAO usuarioLMSClienteDAO;
	
	private UsuarioADSMService usuarioADSMService;
	
	private PerfilUsuarioService perfilUsuarioService;
	
	private UsuarioLMSService usuarioLMSService;

	private EmpresaService empresaService;
	
	private FilialService filialService;
	
	private EmpresaUsuarioService empresaUsuarioService;

	private ManterClienteService manterClienteService;
	
	/**
	 * Verifica se o usuário logado possui cliente associado, se sim retorna este usuário.
	 * @return TypedFlatMap
	 */
	public Cliente findClienteAssociadoByUsuarioSessao(){
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		return getUsuarioLMSClienteDAO().findClienteAssociadoByUsuarioSessao( usuarioSessao.getIdUsuario() );
	}
	
	/**
	 * 
	 */
	public Integer getRowCount(Map criteria) {
		return getUsuarioLMSClienteDAO().getRowCountAdminCliente(new TypedFlatMap(criteria));
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		criteria.put("pessoa.nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));

		ResultSetPage rsp = this.getUsuarioLMSClienteDAO().findPaginatedAdminCliente(criteria, FindDefinition.createFindDefinition(criteria));

		List list = new ArrayList();
		if( rsp != null ){
			list = rsp.getList();
		}

		String nrIdentificacaoFormatado = "";
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Map cliente = (Map) it.next();
			Map pessoa = (Map) cliente.get("pessoa");
			nrIdentificacao = (String)pessoa.get("nrIdentificacao");
			if(!StringUtils.isBlank(nrIdentificacao)) {
				nrIdentificacaoFormatado = FormatUtils.formatIdentificacao((String)((Map)pessoa.get("tpIdentificacao")).get("value"), nrIdentificacao);
			} else {
				nrIdentificacaoFormatado = "";
			}
			pessoa.put("nrIdentificacaoFormatado", nrIdentificacaoFormatado);
		}
		return rsp;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void removeById(java.lang.Long id) {
		List perfisUsuario = getPerfilUsuarioService().findByIdUsuarioPerfilUsuario( id );
		if( perfisUsuario != null ){
			for (Iterator p = perfisUsuario.iterator(); p.hasNext();) {
				PerfilUsuario pu = (PerfilUsuario) p.next();
				getPerfilUsuarioService().removeById( pu.getIdPerfilUsuario() );
			}
		}
		
		getUsuarioLMSService().removeById(id);
		
		getUsuarioADSMService().removeById(id);
		
	}

	/**
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long element = (Long) iter.next();
			removeById( element );
		}
	}
	
	/**
	 * Faz uma chamada para o método findLookupClientesAtivos da classe ClienteService.
	 * @param map
	 * @return List
	 */
	public List findLookupClientesAtivos( TypedFlatMap map ){
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		return getUsuarioLMSClienteDAO().findLookupClientesAtivos( map, usuarioSessao );
	}

	/**
	 * 
	 * @param map
	 * @return List
	 */
	public List findLookupPerfil( TypedFlatMap map ){
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		return getUsuarioLMSClienteDAO().findLookupPerfil( map, usuarioSessao );
	}
	
	/**
	 * Retorna o Locale do usuário logado.
	 * @return Locale
	 */
	public String findLocaleUsuarioLogado(){
		return SessionUtils.getUsuarioLogado().getLocale().getLanguage()+"_"+
			SessionUtils.getUsuarioLogado().getLocale().getCountry();
	}
	
	/**
	 * 
	 * @param map
	 * @return List
	 */
	public ResultSetPage findPaginatedUsuarioLmsCliente( TypedFlatMap map ){		
		return getUsuarioLMSClienteDAO().findPaginatedUsuarioLmsCliente( map, 
				FindDefinition.createFindDefinition(map));
	}
	
	/**
	 * 
	 * @param map
	 * @return Integer
	 */
	public Integer getRowCountUsuarioLmsCliente( TypedFlatMap map ){		
		return getUsuarioLMSClienteDAO().getRowCountUsuarioLmsCliente( map );
	}

	/**
	 *
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedCliente(TypedFlatMap criteria) {
		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if(!StringUtils.isBlank(nrIdentificacao)) {
			criteria.put("pessoa.nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		}

		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		ResultSetPage rsp;
		if(!usuarioSessao.isBlAdminCliente()) {
			rsp = this.manterClienteService.findPaginated( criteria );
		} else {
			rsp = getUsuarioLMSClienteDAO().findPaginatedAdminCliente(criteria, FindDefinition.createFindDefinition(criteria));
		}

		List list = new ArrayList();
		if( rsp != null ){
			list = rsp.getList();
		}

		String nrIdentificacaoFormatado = "";
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Map cliente = (Map) it.next();
			Map pessoa = (Map) cliente.get("pessoa");
			if (null == pessoa)
				continue;
			nrIdentificacao = (String)pessoa.get("nrIdentificacao");
			if(!StringUtils.isBlank(nrIdentificacao)) {
				nrIdentificacaoFormatado = FormatUtils.formatIdentificacao((String)((Map)pessoa.get("tpIdentificacao")).get("value"), nrIdentificacao);
			} else {
				nrIdentificacaoFormatado = "";
			}
			pessoa.put("nrIdentificacaoFormatado", nrIdentificacaoFormatado);
		}
		return rsp;		
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public Integer getRowCountPaginatedCliente( Map criteria ){		
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		if( !usuarioSessao.isBlAdminCliente() ){
			return this.manterClienteService.getRowCount( new TypedFlatMap(criteria)  );
		}
		return getUsuarioLMSClienteDAO().getRowCountAdminCliente( new TypedFlatMap(criteria) );		
	}
	
	/**
	 * Persiste os dados do formulário de cadastro na base de dados.
	 * @param map
	 * @return Serializable
	 */
	public java.io.Serializable store(Long idCliente, 
							  UsuarioADSM usuario,
							  List<PerfilUsuario> perfisUsuario,
							  List<ClienteUsuario> clientesUsuario
							  ) {
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		Cliente cliente = this.usuarioLMSClienteDAO.get( Cliente.class, idCliente );

		// Sempre define o tipo do usuario igual ao usuario que esta 
		// logado no momento
		usuario.setAdminSistema( Boolean.FALSE ); // usuario salvo por este método nunca pode ser admin
		usuario.setTpCategoriaUsuario( usuarioSessao.getTpCategoriaUsuario() );
		usuario.setUsuarioCadastro( this.usuarioLMSClienteDAO.get( UsuarioADSM.class, usuarioSessao.getIdUsuario() ));

		Long idUsuarioADSM = (Long)getUsuarioADSMService().store( usuario );
		
		//***** UsuarioLSM ****//
		UsuarioLMS usuarioLMS = this.usuarioLMSClienteDAO.get( UsuarioLMS.class, idUsuarioADSM );
		if( usuarioLMS == null){
			usuarioLMS = new UsuarioLMS();
			usuarioLMS.setIdUsuario( idUsuarioADSM );
		}		
		usuarioLMS.setCliente( cliente );
		usuarioLMS.setBlIrrestritoCliente( false );
		usuarioLMS.setBlAdminCliente( false );
		usuarioLMS.setBlAdminFilial( false );
		usuarioLMS.setEmpresaPadrao( getEmpresaService().findEmpresaPadraoByUsuario( usuarioSessao ) );
		usuarioLMS.setUsuarioADSM( usuario );

		//******Perfil*********//
		mergePerfis( perfisUsuario, idUsuarioADSM );

		// Clientes do Usuario
		for(ClienteUsuario clienteUsuario : clientesUsuario) {
			clienteUsuario.setUsuarioLMS( usuarioLMS );
		}
		
		return usuarioLMSService.store( usuarioLMS, clientesUsuario, false );
	}

	/**
	 * Faz um merge entre os perfis que já estão armazenados na base e os que o usuário enviou da tela, removendo
	 * os que estão na base e não estão na listbox, adicionando os que estão na listbox e não estão na base e ignorando
	 * os que estão em ambas listbox e base de dados.
	 * 
	 * @param perfisUsuario
	 * @param perfisDaBase
	 */
	private void mergePerfis( List<PerfilUsuario> perfisUsuario, Long idUsuario ){
		List<PerfilUsuario> perfisDaBase = getPerfilUsuarioService().findByIdUsuarioPerfilUsuario( idUsuario );
		if (perfisUsuario != null) {
			if( perfisDaBase != null && perfisDaBase.size() > 0 ){
				for (Iterator<PerfilUsuario> iterator = perfisDaBase.iterator(); iterator.hasNext();) {
					boolean naoExiste = true;
					PerfilUsuario element = iterator.next();
					for (Iterator<PerfilUsuario> it = perfisUsuario.iterator(); it.hasNext();) {
						PerfilUsuario perfilUsuario = it.next();
						if( element.getIdPerfilUsuario().equals( perfilUsuario.getIdPerfilUsuario() ) ) {
							naoExiste = false;
							it.remove();
							break;
						}
					}
					if( naoExiste ){
						getPerfilUsuarioService().removeById( element.getIdPerfilUsuario() );
					}
				}
			}
			for (PerfilUsuario perfilUsuario : perfisUsuario) {
				Usuario usuario = new Usuario();
				usuario.setIdUsuario( idUsuario );
				perfilUsuario.setUsuario( usuario );
				getPerfilUsuarioService().store( perfilUsuario );
			}			
		} else if( perfisDaBase != null && perfisDaBase.size() > 0) {
			// caso não tenha nenhum perfil escolhido remove todos os perfis antigos do usuario que 
			// existiam no banco
			for (PerfilUsuario perfilUsuario : perfisDaBase) {
				getPerfilUsuarioService().removeById( perfilUsuario.getIdPerfilUsuario() );
			}
		}
	}
	
	/**
	 * @return Returns the clienteUsuarioService.
	 */
	public ClienteUsuarioService getClienteUsuarioService() {
		return clienteUsuarioService;
	}

	/**
	 * @param clienteUsuarioService The clienteUsuarioService to set.
	 */
	public void setClienteUsuarioService(ClienteUsuarioService clienteUsuarioService) {
		this.clienteUsuarioService = clienteUsuarioService;
	}

	public void setManterClienteService(ManterClienteService mcs) {
		this.manterClienteService = mcs;
	}

	/**
	 * @return Returns the usuarioLMSClienteDAO.
	 */
	public UsuarioLMSClienteDAO getUsuarioLMSClienteDAO() {
		return usuarioLMSClienteDAO;
	}

	/**
	 * @param usuarioLMSClienteDAO The usuarioLMSClienteDAO to set.
	 */
	public void setUsuarioLMSClienteDAO(UsuarioLMSClienteDAO usuarioLMSClienteDAO) {
		this.usuarioLMSClienteDAO = usuarioLMSClienteDAO;
	}

	/**
	 * @return Returns the usuarioADSMService.
	 */
	public UsuarioADSMService getUsuarioADSMService() {
		return usuarioADSMService;
	}


	/**
	 * @param usuarioADSMService The usuarioADSMService to set.
	 */
	public void setUsuarioADSMService(UsuarioADSMService usuarioADSMService) {
		this.usuarioADSMService = usuarioADSMService;
	}


	/**
	 * @return Returns the perfilUsuarioService.
	 */
	public PerfilUsuarioService getPerfilUsuarioService() {
		return perfilUsuarioService;
	}


	/**
	 * @param perfilUsuarioService The perfilUsuarioService to set.
	 */
	public void setPerfilUsuarioService(PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}

	/**
	 * @return Returns the usuarioLMSService.
	 */
	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	/**
	 * @param usuarioLMSService The usuarioLMSService to set.
	 */
	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	/**
	 * @return Returns the empresaService.
	 */
	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	/**
	 * @param empresaService The empresaService to set.
	 */
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	/**
	 * @return Returns the empresaUsuarioService.
	 */
	public EmpresaUsuarioService getEmpresaUsuarioService() {
		return empresaUsuarioService;
	}

	/**
	 * @param empresaUsuarioService The empresaUsuarioService to set.
	 */
	public void setEmpresaUsuarioService(EmpresaUsuarioService empresaUsuarioService) {
		this.empresaUsuarioService = empresaUsuarioService;
	}

	/**
	 * @return Returns the filialService.
	 */
	public FilialService getFilialService() {
		return filialService;
	}

	/**
	 * @param filialService The filialService to set.
	 */
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

}
