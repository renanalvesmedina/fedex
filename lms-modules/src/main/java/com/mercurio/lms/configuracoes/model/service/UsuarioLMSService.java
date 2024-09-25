package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ClienteUsuario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.dao.UsuarioLMSDAO;
import com.mercurio.lms.configuracoes.model.param.ConsultarUsuarioLMSParam;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de servi�o para CRUD:
 * 
 * @author juliosce
 */
public class UsuarioLMSService extends CrudService<UsuarioLMS, Long> {

	private ClienteUsuarioService clienteUsuarioService;

	private UsuarioADSMService usuarioADSMService;

	private FilialUsuarioService filialUsuarioService;
	
	private EmpresaService empresaService;
	
	private RegionalFilialService regionalFilialService;
	
	
	
	public UsuarioLMS findById(java.lang.Long id) {
		return (UsuarioLMS) getUsuarioLMSDAO().findById(id);
	}


	public List<Map<String, Object>> findUsuarioLmsSuggest(String value, Integer limiteRegistros) {
		return getUsuarioLMSDAO().findUsuarioLmsSuggest(value, limiteRegistros);
	}
	
	public List findLookup(TypedFlatMap criteria) {
		Usuario usuarioSessao = SessionContext.getUser();
		if (usuarioSessao.getBlAdminFilial() != null
				&& usuarioSessao.getBlAdminFilial() == true) {
			criteria.put("idUsuarioGerente", usuarioSessao.getIdUsuario());
			criteria.put("idFilial", usuarioSessao.getFilial().getIdFilial());
			return findLookupGerenteFilial(criteria);
		} else {
			return this.usuarioADSMService.findLookup(criteria);
		}
	}

	/**
	 * Retira a permis�o de administrador do cliente de todos os usuarioLMS que
	 * s�o administrador do cliente corrente.
	 * 
	 * @param idCliente
	 */
	public void removerAdminSobreEsteCliente(Long idCliente) {
		getUsuarioLMSDAO().removerAdminSobreEsteCliente(idCliente);
	}
	
	@Deprecated
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}

	@Deprecated
	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}

	public ResultSetPage findPaginatedUsuarioADSM(Map criteria) {
		Usuario usuarioSessao = SessionContext.getUser();
		if (usuarioSessao.getBlAdminFilial() != null
				&& usuarioSessao.getBlAdminFilial() == true) {
			criteria.put("idUsuarioGerente", usuarioSessao.getIdUsuario());
			criteria.put("idFilial", usuarioSessao.getFilial().getIdFilial());
			return getUsuarioLMSDAO().findPaginatedUsuarioADSM(criteria, FindDefinition.createFindDefinition(criteria));
		}else{
			return getUsuarioADSMService().findPaginated(criteria);
		}
	}
	
	public Integer getRowCountUsuarioADSM(Map criteria) {
		Usuario usuarioSessao = SessionContext.getUser();
		if (usuarioSessao.getBlAdminFilial() != null
				&& usuarioSessao.getBlAdminFilial() == true) {
			criteria.put("idUsuarioGerente", usuarioSessao.getIdUsuario());
			criteria.put("idFilial", usuarioSessao.getFilial().getIdFilial());
			return getUsuarioLMSDAO().getRowCountUsuarioADSM(criteria);
		}else{
			return getUsuarioADSMService().getRowCount(criteria);
		}
	}
		  
	/**
	 * Busca usu�rios ADSM via DAO do usu�rio LMS pois faz join com a classe
	 * Funcionario.
	 * 
	 * @param criteria
	 * @return List
	 */
	public List findLookupGerenteFilial(TypedFlatMap criteria) {
		return getUsuarioLMSDAO().findLookupGerenteFilial(criteria);
	}

	/**
	 * Retorna o n�mero de registro na busca da listagem de Usu�rios LMS, este m�do s� sera
	 * chamado se o usu�rio logado for administrador da filial.
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountADSM(Map criteria) {
		Usuario usuarioSessao = SessionContext.getUser();
		if (usuarioSessao.getBlAdminFilial() != null
				&& usuarioSessao.getBlAdminFilial() == true) {
			criteria.put("idUsuarioGerente", usuarioSessao.getIdUsuario());
			criteria.put("idFilial", usuarioSessao.getFilial().getIdFilial());
			return getUsuarioLMSDAO().getRowCountGerenteFilial(criteria);
		} else {
			return getUsuarioLMSDAO().getRowCountUsuario(criteria);
		}
	}

	/**
	 * Retorna o n�mero de registro na busca da tela de pesquisar login, 
	 * este m�todo s� ser� chamado se o usu�rio logado for administrador da filial.
	 * Chamado pela action: com.mercurio.lms.seguranca.action.ManterUsuarioADSMAction
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountPesquisaLogin(Map criteria) {
		return getUsuarioLMSDAO().getRowCountPesquisaLogin(criteria);
	}
	
	/**
	 * Faz a busca para a listagem de Usu�rios LMS, considerando que o usu�rio logado �
	 * administrador da filial, retornando todos os seus subordinados menos ele.
	 * 
	 * @param criteria
	 * @param findDef
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedADSM(Map criteria) {
		Usuario usuarioSessao = SessionContext.getUser();
		if (usuarioSessao.getBlAdminFilial() != null
				&& usuarioSessao.getBlAdminFilial() == true) {
			criteria.put("idUsuarioGerente", usuarioSessao.getIdUsuario());
			criteria.put("idFilial", usuarioSessao.getFilial().getIdFilial());
			return getUsuarioLMSDAO().findPaginatedGerenteFilial(criteria,
					FindDefinition.createFindDefinition(criteria));
		} else {
			return getUsuarioLMSDAO().findPaginatedUsuario(criteria,
					FindDefinition.createFindDefinition(criteria));
		}
	}

	/**
	 * Faz a busca de usuarios para a listagem da tela Pesquisar Login, considerando que o usu�rio logado �
	 * administrador da filial, retornando todos os seus subordinados menos ele.
	 * Chamado pela action: com.mercurio.lms.seguranca.action.ManterUsuarioADSMAction
	 * 
	 * @param criteria
	 * @param findDef
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedPesquisaLogin(Map criteria) {
		return getUsuarioLMSDAO().findPaginatedPesquisaLogin(criteria,
					FindDefinition.createFindDefinition(criteria));
	}	
	
	public void setUsuarioLMSDAO(UsuarioLMSDAO dao) {
		setDao( dao );
	}

	public UsuarioLMSDAO getUsuarioLMSDAO() {
		return (UsuarioLMSDAO) getDao();
	}

	public void setClienteUsuarioService( ClienteUsuarioService clienteUsuarioService) {
		this.clienteUsuarioService = clienteUsuarioService;
	}

	/**
	 * Verifica se j� existe um usu�rio LMS cadastrado na base com o Id recebido
	 * por parametro.
	 * 
	 * @param idUsuarioLMS
	 * @return boolean
	 */
	public boolean findJaExisteUsuarioLMS(Long idUsuarioLMS) {
		return getUsuarioLMSDAO().jaExisteUsuarioLMS(idUsuarioLMS);
	}
	
	/**
	 * 
	 * @param criteria
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedEmpresa(Map criteria) {
	  	Usuario usuarioSessao = SessionContext.getUser();      	
    	if (usuarioSessao.getBlAdminFilial() != null && usuarioSessao.getBlAdminFilial() == true) {
    		criteria.put("idUsuario", usuarioSessao.getIdUsuario() );
    		return getUsuarioLMSDAO().findPaginatedEmpresa( criteria, FindDefinition.createFindDefinition(criteria) );
		}else{
			return getEmpresaService().findPaginated( criteria );
		}
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountEmpresa( Map criteria) {
	  	Usuario usuarioSessao = SessionContext.getUser();      	
    	if (usuarioSessao.getBlAdminFilial() != null && usuarioSessao.getBlAdminFilial() == true) {
    		criteria.put("idUsuario", usuarioSessao.getIdUsuario() );
    		return getUsuarioLMSDAO().getRowCountEmpresa( criteria );
		}else{
			return getEmpresaService().getRowCount( criteria );
		}		
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountFilial(TypedFlatMap criteria) {
	  	Usuario usuarioSessao = SessionContext.getUser();      	
    	if (usuarioSessao.getBlAdminFilial() != null && usuarioSessao.getBlAdminFilial() == true) {
    		if( criteria.get("empresa.idEmpresa") != null ){
    			Empresa empresa = getEmpresaService().findById( Long.valueOf( criteria.get("empresa.idEmpresa").toString() ) );
        		criteria.put("empresa", empresa );
    		}
    		criteria.put("usuario", usuarioSessao );
    		
		}		
		return getUsuarioLMSDAO().getRowCountFilial( criteria );
	}
	
	/**
	 * 
	 * @param criteria
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedFilial( TypedFlatMap criteria) {

	  	Usuario usuarioSessao = SessionContext.getUser();      	
    	if (usuarioSessao.getBlAdminFilial() != null && usuarioSessao.getBlAdminFilial() == true) {
    		if( criteria.get("empresa.idEmpresa") != null ){
    			Empresa empresa = getEmpresaService().findById( Long.valueOf( criteria.get("empresa.idEmpresa").toString() ) );
        		criteria.put("empresa", empresa );
    		}
    		criteria.put("usuario", usuarioSessao );
    		
		}
    	    	
		ResultSetPage rsPage = getUsuarioLMSDAO().findPaginatedFilial( criteria, FindDefinition.createFindDefinition(criteria) );
		
		List newList = new ArrayList();
		
		for (Iterator ie = rsPage.getList().iterator(); ie.hasNext();) {
			Map result = (Map)ie.next();
			TypedFlatMap newResult = new TypedFlatMap();
			for (Iterator ie2 = result.keySet().iterator(); ie2.hasNext();) {
				String key = (String)ie2.next();
				newResult.put(key.replace('_','.'),result.get(key));
			}
			if (StringUtils.isNotBlank(newResult.getString("pessoa.nrIdentificacao")))
				newResult.put("pessoa.nrIdentificacao",FormatUtils.formatIdentificacao(newResult.getDomainValue("pessoa.tpIdentificacao").getValue(),newResult.getString("pessoa.nrIdentificacao")));
			
			if (StringUtils.isNotBlank(newResult.getString("empresa.pessoa.nrIdentificacao")))
				newResult.put("empresa.pessoa.nrIdentificacao",FormatUtils.formatIdentificacao(newResult.getDomainValue("empresa.pessoa.tpIdentificacao").getValue(),newResult.getString("empresa.pessoa.nrIdentificacao")));
			
			newList.add(newResult);
			Regional regional = regionalFilialService.findLastRegionalVigente(newResult.getLong("idFilial"));
			if (regional != null) {
				newResult.put("lastRegional.dsRegional",regional.getDsRegional());
				newResult.put("lastRegional.idRegional",regional.getIdRegional());
				newResult.put("lastRegional.sgRegional",regional.getSgRegional());
			}
		}
		rsPage.setList(newList);
		return rsPage;
		
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public List findLookupFilial(Map map) {
      	Usuario usuarioSessao = SessionContext.getUser();
      	List rs =null;
    	if (usuarioSessao.getBlAdminFilial() != null && usuarioSessao.getBlAdminFilial() == true) {
    		map.put("usuario", usuarioSessao );    		
    		rs = getUsuarioLMSDAO().findLookupFilial( map ); //SessionUtils.getFiliaisUsuarioLogado();
		} else {   	
			rs =  getUsuarioLMSDAO().findLookupFilial( map );
		}
    	if( rs != null ){
    		return formataRetorno( rs );
    	}
    	return rs;
	}
	
    /**
     * 
     * @param rs
     * @return List
     */
    private List formataRetorno( List rs ){
    	for(int x = 0; x < rs.size(); x++) {
			Map oldMap = (Map)rs.get(x);
			Set set = oldMap.keySet();
			Map newMap = new HashMap();
			
			oldMap.put("empresa_pessoa_nrIdentificacao",
						FormatUtils.formatIdentificacao(
													((DomainValue)oldMap.get("empresa_pessoa_tpIdentificacao")).getValue(),
													(String)oldMap.get("empresa_pessoa_nrIdentificacao")
						)
			);
			
			for (Iterator ie = set.iterator(); ie.hasNext();) {				
				String key = ((String)ie.next());
				ReflectionUtils.setNestedBeanPropertyValue(newMap,key.replace('_','.'),ReflectionUtils.getNestedBeanPropertyValue(oldMap,key));
			}
			
			Regional regional = regionalFilialService.findLastRegionalVigente((Long)newMap.get("idFilial"));
			if (regional != null) {
				Map lastRegional = new HashMap();
				lastRegional.put("dsRegional",regional.getDsRegional());
				lastRegional.put("idRegional",regional.getIdRegional());
				lastRegional.put("sgNmFull",  new StringBuffer(regional.getSgRegional()).append(" - ").append(regional.getDsRegional()).toString());
				lastRegional.put("sgRegional",regional.getSgRegional());
				newMap.put("lastRegional",lastRegional);
			}
			rs.set(x,newMap);
			if (x == 1)
				break;
    	}
    	return rs;    	
    }

	/**
	 * Salva um UsuarioLMS
	 * 
	 * @param usuarioLMS
	 * @param clientesUsuario
	 * @return Serializable
	 */
	public java.io.Serializable store(UsuarioLMS usuarioLMS,
							  List<ClienteUsuario> clientesUsuario,
							  boolean excluirAdminDoCliente) {

		if (usuarioLMS == null) {
			throw new IllegalArgumentException("usuarioLMS cannot be null");
		}
		if (clientesUsuario == null) {
			throw new IllegalArgumentException("clientesUsuario cannot be null");
		}

		// deve existir a associa��o entre UsuarioLMS e UsuarioADSM
		final UsuarioADSM usuarioADSM = usuarioLMS.getUsuarioADSM();
		final Long idUsuarioADSM = usuarioADSM.getIdUsuario();
		if (usuarioADSM == null || idUsuarioADSM == null) {
			throw new IllegalArgumentException("usuarioLMS must be associated with an UsuarioADMS");
		}
		
		// Define o id do UsuarioLMS como sendo o ID do UsuarioADSM
		// que � lado mais forte da associa��o e sempre dever� 
		// existir prioritariamente a inser��o de UsuarioLMS
		usuarioLMS.setIdUsuario(idUsuarioADSM);

		final boolean jaExisteUsuarioLMS = findJaExisteUsuarioLMS(idUsuarioADSM);
		
		final Cliente cliente = usuarioLMS.getCliente();
		if( cliente != null ) {

			// Se foi atribu�do a administra��o do cliente associado ao usu�rio corrente e o cliente associado j�
			// possui um administrador e o usu�rio optou por substituir o administrador anterior pelo usu�rio corrente.
			// chama o m�todo getService().getUsuarioLMSDAO().removerAdminSobreEsteCliente() que ir� realizar este procedimento.
			if( usuarioLMS.getBlAdminCliente() ) {
				// consulta novamente na base se existeAdminProCliente, outro usuario pode ter alterado
				// s� consulta se o usuario confirmou a pergunta de substituicao
				if (excluirAdminDoCliente) {
					final Long idCliente = cliente.getIdCliente();
					boolean existeAdminCliente = findExisteUmUsuarioAdminDoCliente(idCliente, idUsuarioADSM);
					if (existeAdminCliente) {
						this.removerAdminSobreEsteCliente( idCliente );
					}
				}
			}			
		}
		
		// verifica se o usu�rio � administrador de uma filial
		final Filial filial = usuarioLMS.getFilial();
		if(  !usuarioLMS.getBlAdminFilial() ) {
			usuarioLMS.setFilial( null );
		}
		
		if (jaExisteUsuarioLMS) {
			// Apaga todos os clientes do usuario. 
			// para permitir sua inclusao logo abaixo
			List<Long> ids = new ArrayList<Long>(1);
			ids.add(idUsuarioADSM);
			getUsuarioLMSDAO().removeClienteUsuario(ids);
		} else {
			// Caso n�o exista um UsuarioLMS salvo
			// seta para null o ID para que o Hibernate possa 
			// buscar a partir da associa��o one-to-one de UsuarioADSM o Id 
			// da associa��o, usado apenas na inser��o de um novo registro
			usuarioLMS.setIdUsuario(null);
			Long id = (Long) super.store(usuarioLMS);
			usuarioLMS = this.get(id);
		}
		
		// Grava os clientes usuario se o campo blIrrestritoCliente
		// n�o estiver checked.
		if ( usuarioLMS.getBlIrrestritoCliente() == false ) {
			for(ClienteUsuario clienteUsuario : clientesUsuario) {
				clienteUsuario.setUsuarioLMS(usuarioLMS);
				clienteUsuarioService.store(clienteUsuario);
			}
		}

		return super.store(usuarioLMS);
	}

	/**
	 * M�todo utilizado por em qto somente em ManterUsuarioLMSEmpresa, devido �
	 * regras peculiares
	 * 
	 * @param usuarioLMS
	 * @return
	 */
	public java.io.Serializable storeUsuarioLMS(UsuarioLMS usuarioLMS) {
		return super.store(usuarioLMS);
	}

	public void removeById(Long idUsuario) {
		List<Long> ids = new ArrayList(1);
		ids.add(idUsuario);
		removeByIds(ids);
	}

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getUsuarioLMSDAO().removeByIds(ids);
	}

	/**
	 * @param usuarioADSMService The usuarioADSMService to set.
	 */
	public void setUsuarioADSMService(UsuarioADSMService usuarioADSMService) {
		this.usuarioADSMService = usuarioADSMService;
	}

	/**
	 * @return Returns the usuarioADSMService.
	 */
	public UsuarioADSMService getUsuarioADSMService() {
		return usuarioADSMService;
	}

	/**
	 * Verifica se o usu�rio corrente da sess�o � administrador da filial
	 * @return boolean
	 */
	public boolean findEhAdminFilial(){
		Usuario usuarioSessao = SessionContext.getUser();
		if (usuarioSessao.getBlAdminFilial() != null
				&& usuarioSessao.getBlAdminFilial() == true) {
			return usuarioSessao.getBlAdminFilial();
		}
		return false;
	}
	
	/**
	 * Retorna a lista de usuarioLMS baseado nos filtros informado dentro do objeto 'ConsultarUsuarioLMSParam'
	 * 
	 * @author Micka�l Jalbert
	 * @since 21/09/2006
	 * 
	 * @param ConsultarUsuarioLMSParam cup
	 * @return List
	 */
	public List findLookupSistema(ConsultarUsuarioLMSParam cup){
		return getUsuarioLMSDAO().findLookupSistema(cup);
	}
	
	public List findLookupSistemaMonitoramento(ConsultarUsuarioLMSParam cup){
		return getUsuarioLMSDAO().findLookupSistemaMonitoramento(cup);
	}
	
	/**
	 * Retorna a lista de usuarioLMS paginado baseado nos filtros informado dentro do objeto 'ConsultarUsuarioLMSParam'
	 * 
	 * author Micka�l Jalbert
	 * @since 21/09/2006
	 * 
	 * @param ConsultarUsuarioLMSParam cup
	 * @return ResultSetPage
	 */	
	public ResultSetPage findPaginatedSistema(ConsultarUsuarioLMSParam cup, FindDefinition findDef){
		return getUsuarioLMSDAO().findPaginatedSistema(cup, findDef);
	}
	
	/**
	 * Retorna o n�mero de usuarios baseado nos filtros informado dentro do objeto 'ConsultarUsuarioLMSParam'
	 * 
	 * author Micka�l Jalbert
	 * @since 21/09/2006
	 * 
	 * @param ConsultarUsuarioLMSParam cup
	 * @return Integer
	 */	
	public Integer getRowCountSistema(ConsultarUsuarioLMSParam cup){
		return getUsuarioLMSDAO().getRowCountSistema(cup);
	}
	
	/**
	 * @return Returns the filialUsuarioService.
	 */
	public FilialUsuarioService getFilialUsuarioService() {
		return filialUsuarioService;
	}

	/**
	 * @param filialUsuarioService The filialUsuarioService to set.
	 */
	public void setFilialUsuarioService(FilialUsuarioService filialUsuarioService) {
		this.filialUsuarioService = filialUsuarioService;
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
	 * @return Returns the regionalFilialService.
	 */
	public RegionalFilialService getRegionalFilialService() {
		return regionalFilialService;
	}

	/**
	 * @param regionalFilialService The regionalFilialService to set.
	 */
	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}
	
	public List<ClienteUsuario> findClientesUsuario(Long idUsuario) {
		return getUsuarioLMSDAO().findClientesUsuario(idUsuario);
	}
	
	public boolean findExisteUmUsuarioAdminDoCliente(Long idCliente, Long idUsuario) {
		return getUsuarioLMSDAO().findExisteUmUsuarioAdminDoCliente(idCliente, idUsuario);
	}

	public void updateBlTermoComp(Long idUsuario, boolean blTermoComp) {
		getUsuarioLMSDAO().updateBlTermoComp(idUsuario, blTermoComp);
	}
}