/**
 * 
 */
package com.mercurio.lms.seguranca.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ClienteUsuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSClienteService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * @author juliosce
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguranca.manterUsuarioLMSClienteAction"
 */
public class ManterUsuarioLMSClienteAction extends CrudAction {
	
	private UsuarioLMSClienteService usuarioLMSClienteService ;

	private ManterUsuarioADSMAction manterUsuarioADSMAction;
	
	private ManterUsuarioLMSAction manterUsuarioLMSAction;
	
	private PerfilUsuarioService perfilUsuarioService;
	
	private UsuarioADSMService usuarioADSMService;
	
	/**
	 * 
	 * @param map
	 * @return List
	 */
	public List findLookupClientesAtivos( TypedFlatMap map ){
		return usuarioLMSClienteService.findLookupClientesAtivos( map );
	}
	
	public String findLocaleByUsuarioLogado(){
		return getUsuarioLMSClienteService().findLocaleUsuarioLogado();
	}
	
	/**
	 * 
	 * @param map
	 * @return List
	 */
	public List findLookupPerfil( TypedFlatMap map ){
		// Faço pesquisa primeiro com parâmetro extato
		List list = getUsuarioLMSClienteService().findLookupPerfil( map );
		
		if (list != null) {
			if (list.size() <= 0 ) {
				
				// Se não encontrou algo com parâmetro exato informado, adiciono %				
				map.put("dsPerfil",map.get("dsPerfil")+"%");
				return getUsuarioLMSClienteService().findLookupPerfil( map );
				
			} else {
				
				// Se Encontrou algo com parâmetro exato informado, retorna o próprio
				return list;
				
			}
		}
		return list;
	}
	
	public void removeById(Long id) {		
		getUsuarioLMSClienteService().removeById(id);
	}

	/**
	 * 
	 *
	**/
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getUsuarioLMSClienteService().removeByIds(ids);
	}
	
	/**
	 * 
	 * @param map
	 * @return List
	 */
	public ResultSetPage findPaginatedUsuarioLmsCliente( TypedFlatMap map ){
		return getUsuarioLMSClienteService().findPaginatedUsuarioLmsCliente( map );
	}
		
	/**
	 * 
	 * @param map
	 * @return Integer
	 */
	public Integer getRowCountUsuarioLmsCliente( TypedFlatMap map ){		
		return getUsuarioLMSClienteService().getRowCountUsuarioLmsCliente( map );
	}

	/**
	 *
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedCliente(TypedFlatMap criteria) {
		return getUsuarioLMSClienteService().findPaginatedCliente( criteria );
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public Integer getRowCountPaginatedCliente( Map map ){		
		return getUsuarioLMSClienteService().getRowCountPaginatedCliente( map );
	}
	
	/**
	 * @return Returns the usuarioLMSClienteService.
	 */
	public UsuarioLMSClienteService getUsuarioLMSClienteService() {
		return usuarioLMSClienteService;
	}

	/**
	 * @param usuarioLMSClienteService The usuarioLMSClienteService to set.
	 */
	public void setUsuarioLMSClienteService(
			UsuarioLMSClienteService usuarioLMSClienteService) {
		this.usuarioLMSClienteService = usuarioLMSClienteService;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Map findById(java.lang.Long id) {
		TypedFlatMap retorno = new TypedFlatMap();
		Map usuarioLMS = getManterUsuarioLMSAction().findById( id );

		// FIXME usando findByIdAtLogon pq o findById retorna um Map horrivel
		UsuarioADSM usuarioADSM = getUsuarioADSMService().findByIdAtLogon( id );
		retorno.put("idUsuario", usuarioADSM.getIdUsuario());
		retorno.put("nrMatricula", usuarioADSM.getNrMatricula() );
		retorno.put("blAtivo", usuarioADSM.isBlAtivo());
		retorno.put("blAtivoVerify", usuarioADSM.isBlAtivo());			
		retorno.put("blAdministrador", usuarioADSM.isAdminSistema());
		retorno.put("login", usuarioADSM.getLogin());
		retorno.put("nmUsuario", usuarioADSM.getNmUsuario());
		retorno.put("tpCategoriaUsuario", usuarioADSM.getTpCategoriaUsuario().getValue());
		retorno.put("dsEmail", usuarioADSM.getDsEmail());
		retorno.put("nrDdd", usuarioADSM.getNrDdd());
		retorno.put("nrFone", usuarioADSM.getNrFone());
		retorno.put( "locale", usuarioADSM.getLocale().toString());				
		retorno.put("dsSenhaTemporaria", usuarioADSM.getDsSenhaTemporaria());
		retorno.put("dsSenhaVerify", usuarioADSM.getDsSenhaTemporaria());
		retorno.put("dsSenha", usuarioADSM.getDsSenha());
		retorno.put("dtUltimaTrocaSenha", usuarioADSM.getDtUltimaTrocaSenha());
		retorno.put("dhCadastro", usuarioADSM.getDhCadastro());				
		
		List perfisUsuario = getPerfilUsuarioService().findByIdUsuarioPerfilUsuario( 
				Long.valueOf( usuarioLMS.get("idUsuarioLMS").toString() ) );
		List perfis = new ArrayList();
		if( perfisUsuario != null ){
			for (Iterator iter = perfisUsuario.iterator(); iter.hasNext();) {
				PerfilUsuario element = (PerfilUsuario) iter.next();
				if(  element.getPerfil() != null ){
					HashMap mapa = new HashMap(2);		
					mapa.put("idPerfilUsuario", element.getIdPerfilUsuario() );
					mapa.put("idPerfil", element.getPerfil().getIdPerfil());
					mapa.put("dsPerfil", element.getPerfil().getDsPerfil() );
					
					perfis.add( mapa );
				}				
			}
		}		
		retorno.putAll( usuarioLMS );
		retorno.put("acessoPerfil", perfis );
		return retorno;
	}
	
	/**
	 * Persiste os dados do formulário de cadastro na base de dados.
	 * @param map
	 * @return Serializable
	 */
	public Serializable store(TypedFlatMap map) {
		Long idCliente = map.getLong("clienteDoUsuario.idCliente");
		
		UsuarioADSM uadsm = new UsuarioADSM();
		uadsm.setIdUsuario(map.getLong("idUsuario"));
		uadsm.setNmUsuario( map.getString("nmUsuario") );
		uadsm.setBlAtivo( map.getBoolean("blAtivo", Boolean.FALSE) );
		uadsm.setLogin(map.getString("login") );
		uadsm.setNmUsuario( map.getString("nmUsuario") );
		uadsm.setDsEmail( map.getString("dsEmail") );
		uadsm.setNrDdd( map.getString("nrDdd") );
		uadsm.setNrFone( map.getString("nrFone") );
		uadsm.setLocale( map.getLocale("locale") );
		uadsm.setDsSenhaTemporaria( map.getString("dsSenhaTemporaria") );
		uadsm.setDsSenhaVerify( map.getString("dsSenhaVerify") );
		uadsm.setDtUltimaTrocaSenha( map.getYearMonthDay("dtUltimaTrocaSenha") );
		uadsm.setDhCadastro( JTDateTimeUtils.getDataHoraAtual() );		
		uadsm.setDsSenha( map.getString("dsSenha") );
		uadsm.setUrlSistema( map.getString("urlSistema") );
		
		// monta lista de ids do PerfilUsuario
		List<TypedFlatMap> perfisUsuarioTela = (List<TypedFlatMap>)map.get("acessoPerfil");
		List<PerfilUsuario> perfisUsuario = new ArrayList<PerfilUsuario>();
		if (perfisUsuarioTela != null) {
			for (Iterator<TypedFlatMap> it = perfisUsuarioTela.iterator(); it.hasNext(); ) {
				TypedFlatMap map1 = it.next();
				PerfilUsuario perfilUsuario = new PerfilUsuario();
				perfilUsuario.setIdPerfilUsuario(map1.getLong("idPerfilUsuario"));
				Perfil perfil = new Perfil();
				perfil.setIdPerfil( map1.getLong("idPerfil") );
				perfilUsuario.setPerfil( perfil );
				perfisUsuario.add(perfilUsuario);
			}
		}
		
		// monata lista de ClienteUsuario
		List<TypedFlatMap> clientesUsuarioTela = (List<TypedFlatMap>)map.get("clienteUsuario");
		List<ClienteUsuario> clientesUsuario = ManterUsuarioLMSAction.getClientesUsuario(clientesUsuarioTela);
		
		return getUsuarioLMSClienteService().store( idCliente,
													uadsm,
													perfisUsuario,
													clientesUsuario);
	}

	/**
	 * Verifica se o usuaário logado possui cliente associado, se sim retorna id e nome deste usuário.
	 * @return TypedFlatMap
	 */
	public TypedFlatMap findClienteAssociadoByUsuarioSessao(){
		TypedFlatMap retorno = new TypedFlatMap();
		Cliente cliente = getUsuarioLMSClienteService().findClienteAssociadoByUsuarioSessao();
		if( cliente != null ){
			retorno.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao( 
					cliente.getPessoa().getTpIdentificacao(),cliente.getPessoa().getNrIdentificacao() ) );
			retorno.put("pessoa.nmPessoa", cliente.getPessoa().getNmPessoa() );
			retorno.put("idCliente", cliente.getIdCliente().toString() );
		}
		
		return retorno;
	}

	/**
	 * Carrega informações do usuário logado no sistema da sessão do mesmo.
	 * 
	 * @param data
	 * @return
	 */
	public TypedFlatMap carregaDadosSessao( TypedFlatMap data ){
		return getUsuarioADSMService().findCarregaDadosSessao( data );
	}

	/**
	 * Retorna o tamanho mínimo que a senha do UsuarioADSM deverá ter.
	 * @return int
	 */
	public int findTamanoMinimoSenhaUsuarioADSM( ){
		return getUsuarioADSMService().findTamanoMinimoSenha();
		
	}
	
	/**
	 * @return Returns the manterUsuarioADSMAction.
	 */
	public ManterUsuarioADSMAction getManterUsuarioADSMAction() {
		return manterUsuarioADSMAction;
	}

	/**
	 * @param manterUsuarioADSMAction The manterUsuarioADSMAction to set.
	 */
	public void setManterUsuarioADSMAction(
			ManterUsuarioADSMAction manterUsuarioADSMAction) {
		this.manterUsuarioADSMAction = manterUsuarioADSMAction;
	}

	/**
	 * @return Returns the manterUsuarioLMSAction.
	 */
	public ManterUsuarioLMSAction getManterUsuarioLMSAction() {
		return manterUsuarioLMSAction;
	}

	/**
	 * @param manterUsuarioLMSAction The manterUsuarioLMSAction to set.
	 */
	public void setManterUsuarioLMSAction(
			ManterUsuarioLMSAction manterUsuarioLMSAction) {
		this.manterUsuarioLMSAction = manterUsuarioLMSAction;
	}

	/**
	 * @return Returns the manterPerfilUsuarioAction.
	 */
	public PerfilUsuarioService getPerfilUsuarioService() {
		return perfilUsuarioService;
	}

	/**
	 * @param manterPerfilUsuarioAction The manterPerfilUsuarioAction to set.
	 */
	public void setPerfilUsuarioService(
			PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
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
}
