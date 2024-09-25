package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;

import com.mercurio.adsm.core.util.ServerInfo;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.AutoridadeService;
import com.mercurio.adsm.framework.security.model.service.AuthenticationService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EmpresaUsuario;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.dao.UsuarioDAO;
import com.mercurio.lms.municipios.dto.ContatoDto;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 */
public class UsuarioServiceImpl extends CrudService implements UsuarioService {

	private AutoridadeService autoridadeService;
	private ClienteUsuarioService clienteUsuarioService;
	private EmpresaUsuarioService empresaUsuarioService;
	
	private SessionContentLoaderService sessionContentLoaderService;
	private FuncionarioService funcionarioService;
	private UsuarioLMSService usuarioLmsService;
	private ConfiguracoesFacade configuracoesFacade;
	private AuthenticationService authenticationService;
	
	private EmpresaService empresaService;
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade){
		this.configuracoesFacade = configuracoesFacade;
	}

	private FilialService filialService;

	/**
	 * Recupera uma instância de <code>Usuario</code> a partir do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	
	public Usuario findById(java.lang.Long id) {
		return (Usuario) getUsuarioDAO().findById(id);
	}
	
    /**
     * Obtém o usuário sem fazer fetch em relacionamentos.
     * 
     * @param id
     * @return
     */
    public Usuario get(Long id) {
		return getUsuarioDAO().get(id);
	}	
	
	/**
	 * Retorna a lista de usuarios do perfil informado filtrando pela filial.
	 * 
	 * @param idPerfil
	 * @return List lista de usuarios
	 */
	public List findUsuariosByPerfil(Long idPerfil, Long idFilial, Long idEmpresa) {
		return this.getUsuarioDAO().findUsuariosByPerfil(idPerfil, idFilial, idEmpresa);
	}

	/**
	 * Retorna o usuario substituto do usuario informado filtrando
	 * pela filial.
	 * 
	 * author Mickaël Jalbert
	 * @since 01/02/2007
	 * 
	 * @param idUsuario
	 * @param idFilial
	 * @param idEmpresa
	 * @return lista de usuarios
	 */
	public List<Usuario> findSubstitutoByUsuario(Long idUsuario, Long idFilial, Long idEmpresa) {
		List lstUsuarios = new ArrayList();
		
		lstUsuarios.add(this.get(idUsuario));
		
		return this.getUsuarioDAO().findSubstitutoByUsuarios(lstUsuarios, null, idFilial, idEmpresa);
	}
	
	/**
	 * Retorna o usuario substituto do usuario ou do perfil informado filtrando
	 * pela filial.
	 * 
	 * author Mickaël Jalbert
	 * @since 23/09/2006
	 * 
	 * @param idsUsuario
	 * @param idPerfil
	 * @param idFilial
	 * @param idEmpresa
	 * @return List lista de usuarios
	 */
	public List<Usuario> findSubstitutoByUsuarios(List<Usuario> idsUsuario, Long idPerfil, Long idFilial, Long idEmpresa) {
		return this.getUsuarioDAO().findSubstitutoByUsuarios(idsUsuario,idPerfil, idFilial, idEmpresa);
	}
	
	/**
	 * Retorna o usuario substituto do perfil informado filtrando
	 * pela filial.
	 * 
	 * @author Mickaël Jalbert
	 * @since 01/02/2007
	 * 
	 * @param idPerfil
	 * @param idFilial
	 * @param idEmpresa
	 * @return lista de usuarios
	 */
	public List<Usuario> findSubstitutoByPerfil(Long idPerfil, Long idFilial, Long idEmpresa) {
		return this.getUsuarioDAO().findSubstitutoByUsuarios(null, idPerfil, idFilial, idEmpresa);
	}		
	
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}
	
	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}
	
	/**
	 * Esse metodo apenas delega a modificacao para UsuarioLMS. 
	 * Usuario ADSM nao deve ser modificado via Usuario/UsuarioLMS, 
	 * e sim utilizando as services do ADSM.
	 */
	@Deprecated
	public void removeById(java.lang.Long id) {
		
		this.usuarioLmsService.removeById(id);
	}

	/**
	 * Esse metodo apenas delega a modificacao para UsuarioLMS. 
	 * Usuario ADSM nao deve ser modificado via Usuario/UsuarioLMS, 
	 * e sim utilizando as services do ADSM.
	 *
	 *
	 */
    @ParametrizedAttribute(type = java.lang.Long.class)
	@Deprecated
	public void removeByIds(List ids) {
		
		this.usuarioLmsService.removeByIds(ids);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param dao
	 */
	public void setUsuarioDAO(UsuarioDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private UsuarioDAO getUsuarioDAO() {
		return (UsuarioDAO) getDao();
	}

	public Boolean verificaAcessoFilialRegionalUsuarioLogado(Long idFilial) {

		Filial filial = getFilialService().findById(idFilial);
		return Boolean.valueOf(SessionUtils.isFilialAllowedByUsuario(filial)); 

	}

	public Boolean validateAcessoFilialRegionalUsuario(Long idFilial) {
		return this.verificaAcessoFilialRegionalUsuarioLogado(idFilial);
	}

	public Boolean validateAcessoFilialRegionalUsuario(Long idFilial,
			Long idUsuario) {
		return getUsuarioDAO().verificaAcessoFilialRegionalUsuarioLogado(
				idUsuario, idFilial);
	}
	
	
	public com.mercurio.adsm.framework.security.model.Usuario findByIdAtLogon(Long id) {
		return this.findById(id);
	}

    /**
     * FindLookupUsuarioFuncionario (Generica) Busca as informacoes do usuario/funcionario conforme parametros informados
     * @param idUsuario
     * @param nrMatricula 
     * @param idFilial
     * @param cdFuncao
     * @param cdCargo
     * @param cdSetor
     * @param joinFuncionario
     * @return
     */
    public List<Map<String, Object>> findLookupUsuarioFuncionario(Long idUsuario, String nrMatricula, Long idFilial, String cdFuncao, String cdCargo, String cdSetor, boolean joinFuncionario){    	
    	return findLookupUsuarioFuncionario(idUsuario, null, nrMatricula, idFilial, cdFuncao, cdCargo, cdSetor, null, joinFuncionario);
    }
    
    /**
     * FindLookupUsuarioFuncionario (Generica) Busca as informacoes do usuario/funcionario conforme parametros informados
     * @param idUsuario
     * @param login
     * @param nrMatricula 
     * @param idFilial
     * @param cdFuncao
     * @param cdCargo
     * @param cdSetor
     * @param nrCpf
     * @param joinFuncionario
     * @return
     */
    public List<Map<String, Object>> findLookupUsuarioFuncionario(Long idUsuario, String login, String nrMatricula, Long idFilial, String cdFuncao, String cdCargo, String cdSetor, String nrCpf, boolean joinFuncionario){
    	String[] tpsituacoes = FuncionarioService.SITUACAO_INVALIDA_FUNCIONARIO.split(",");

    	String[] cdFuncoes = cdFuncao == null || cdFuncao.isEmpty() ? null : new String[]{cdFuncao};
    	return getUsuarioDAO().findLookupUsuarioFuncionario(
    			idUsuario, login, nrMatricula, idFilial, cdFuncoes , cdCargo, cdSetor, nrCpf, tpsituacoes, joinFuncionario);
    }
    
    
    @Override
    public List findSuggestUsuarioPromotor(String nrMatricula, String nmFuncionario) {
    	return getUsuarioDAO().findSuggestUsuarioFuncionario(nrMatricula, nmFuncionario);
    }
    
    public List<Map<String, Object>> findLookupMotoristaInstrutor(String nrMatricula){
    	String[] tpsituacoes = FuncionarioService.SITUACAO_INVALIDA_FUNCIONARIO.split(",");
    	return getUsuarioDAO().findLookupMotoristaInstrutor(nrMatricula, tpsituacoes);
    }
    
    public List<Map<String, Object>> findLookupUsuarioPromotor(Long idUsuario, String login, String nrMatricula, Long idFilial, String cdSetor, String nrCpf, boolean joinFuncionario){
    	String[] tpsituacoes = FuncionarioService.SITUACAO_INVALIDA_FUNCIONARIO.split(",");
    	return getUsuarioDAO().findLookupUsuarioFuncionario(
    			idUsuario, login, nrMatricula, idFilial, findCodigoPromotor(), null, cdSetor, nrCpf, tpsituacoes, joinFuncionario);
    }
    
    public List<Map<String, Object>> findLookupUsuario(TypedFlatMap parametros){
    	return findLookupUsuarioFuncionario(parametros.getLong("idUsuario"), 
											null,
											    			parametros.getString("nrMatricula"),
											    			parametros.getLong("idFilial"),
											    			parametros.getString("cdFuncao"),
											    			parametros.getString("codFuncao.cargo.codigo"),
											    			parametros.getString("cdSetor"),
							    			parametros.getString("pessoa.nrIdentificacao"),
							    			true);
    }
    
    public List<Map<String, Object>> findLookupMotoristaInstrutor(TypedFlatMap parametros){
    	return findLookupMotoristaInstrutor(parametros.getString("nrMatricula"));
    }
    /**
     * FindLookupFuncionarioPromotor Busca as informacoes do usuario/funcionario promotor conforme matrícula
     * @param nrMatricula 
     * @return
     */
    public List findLookupFuncionarioPromotor(String nrMatricula){
    	String[] tpsituacoes = FuncionarioService.SITUACAO_INVALIDA_FUNCIONARIO.split(",");
    	return getUsuarioDAO().findLookupFuncionarioPromotor(nrMatricula, tpsituacoes);
    }
  
	/**
	 * Andresa Vargas
	 */
	public ResultSetPage findPaginatedCustom(Long idFilial, 
									   String nrMatricula,
									   String nmFuncionario,
									   String cdCargo,
									   String cdFuncao,
									   String tpSituacaoFuncionario,
									   String cdSetor,
									   String nrCpf,
									   boolean joinFuncionario,
									   FindDefinition findDef) {
		String[] cdFuncoes = cdFuncao == null || cdFuncao.isEmpty() ? null : new String[]{cdFuncao};
		//LMS 7128
		if("025".equals(cdCargo) && cdFuncao.contains("025.0002,025.04")){
	 		cdFuncoes = new String[2];  
			cdFuncoes = cdFuncao.split(","); 	
		}
		
		
		
		ResultSetPage rs = getUsuarioDAO().findPaginatedCustom(idFilial, nrMatricula, nmFuncionario, cdCargo, cdFuncoes , tpSituacaoFuncionario, cdSetor, nrCpf, joinFuncionario, findDef);
		return rs;
	}
	
	/**
	 * Andresa Vargas
	 */
	public Integer getRowCountCustom(Long idFilial, 
							   String nrMatricula,
							   String nmFuncionario,
							   String cdCargo,
							   String cdFuncao,
							   String tpSituacaoFuncionario,
							   String cdSetor,
							   String nrCpf,
							   boolean joinFuncionario){
		String[] cdFuncoes = cdFuncao == null || cdFuncao.isEmpty() ? null : new String[]{cdFuncao};

		return getUsuarioDAO().getRowCountCustom(idFilial, nrMatricula, nmFuncionario, cdCargo, cdFuncoes, tpSituacaoFuncionario, cdSetor, nrCpf, joinFuncionario);
	}


	public ResultSetPage findPaginatedPromotor(Long idFilial, 
									   String nrMatricula,
									   String nmFuncionario,									   
									   String tpSituacaoFuncionario,
									   String cdSetor,
									   String nrCpf,
									   boolean joinFuncionario,
									   FindDefinition findDef) {
		ResultSetPage rs = getUsuarioDAO().findPaginatedCustom(idFilial, nrMatricula, nmFuncionario, null, findCodigoPromotor(), tpSituacaoFuncionario, cdSetor, nrCpf, joinFuncionario, findDef);
		return rs;
	}
	
	/**
	 * Andresa Vargas
	 */
	public Integer getRowCountPromotor(Long idFilial, 
							   String nrMatricula,
							   String nmFuncionario,							   
							   String tpSituacaoFuncionario,
							   String cdSetor,
							   String nrCpf,
							   boolean joinFuncionario){
		return getUsuarioDAO().getRowCountCustom(idFilial, nrMatricula, nmFuncionario, null, findCodigoPromotor(), tpSituacaoFuncionario, cdSetor, nrCpf, joinFuncionario);
	}
	
	/**
	 * Hector Junior
	 */
	public ResultSetPage findPaginatedPromotor(Long idFilial, 
									   String nrMatricula,
									   String nmFuncionario,
									   String cdCargo,
									   String cdFuncao,
									   String tpSituacaoFuncionario,
									   FindDefinition findDef) {
		
		if ( ( (nmFuncionario == null) || (nmFuncionario.length() == 0) ) 
				&& (idFilial == null) ){
			throw new BusinessException("LMS-09119");
		}
		ResultSetPage rs = getUsuarioDAO().findPaginatedPromotor(idFilial, nrMatricula, nmFuncionario, cdCargo, cdFuncao, tpSituacaoFuncionario, findDef, findCodigoPromotor());
		return rs;
	}
	
	/**
	 * Hector Junior
	 * @return String formatada
	 */
	private String[] findCodigoPromotor(){
		
		String cdPromotor = (String)configuracoesFacade.getValorParametro("CD_PROMOTOR");
		String cdPromotor2 = (String)configuracoesFacade.getValorParametro("CD_PROMOTOR_2");
		if(!StringUtils.isBlank(cdPromotor2)){
			cdPromotor = StringUtils.isBlank(cdPromotor) ? cdPromotor2 : (cdPromotor + ";" + cdPromotor2);
		}
		
		if( cdPromotor != null ){
			return cdPromotor.split(";");
		}
		
		return new String[]{};
	}
	
	/**
	 * Hector Junior
	 */
	public Integer getRowCountPromotor(Long idFilial, 
							   String nrMatricula,
							   String nmFuncionario,
							   String cdCargo,
							   String cdFuncao,
							   String tpSituacaoFuncionario){
		return getUsuarioDAO().getRowCountPromotor(idFilial, nrMatricula, nmFuncionario, cdCargo, cdFuncao, tpSituacaoFuncionario, findCodigoPromotor());
	}

	
	public AutoridadeService getAutoridadeService() {
		return autoridadeService;
	}

	public void setAutoridadeService(AutoridadeService autoridadeService) {
		this.autoridadeService = autoridadeService;
	}


	public ClienteUsuarioService getClienteUsuarioService() {
		return clienteUsuarioService;
	}

	public void setClienteUsuarioService(ClienteUsuarioService clienteUsuarioService) {
		this.clienteUsuarioService = clienteUsuarioService;
	}

	public EmpresaUsuarioService getEmpresaUsuarioService() {
		return empresaUsuarioService;
	}

	public void setEmpresaUsuarioService(EmpresaUsuarioService empresaUsuarioService) {
		this.empresaUsuarioService = empresaUsuarioService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public TypedFlatMap findMeuPerfil() {
		
		TypedFlatMap tfm = new TypedFlatMap();

		Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
		Usuario usuarioBanco = get(usuarioLogado.getIdUsuario());
		tfm.put("locale", usuarioBanco.getLocale().toString());
		
		final Empresa empresaPadrao = usuarioBanco.getEmpresaPadrao();
		if (empresaPadrao != null) {
			tfm.put("empresaPadrao.idEmpresa", empresaPadrao.getIdEmpresa());
			final List<EmpresaUsuario> empresasUsuario = usuarioBanco.getEmpresaUsuario();
			Hibernate.initialize(empresasUsuario);
			for(EmpresaUsuario eu : empresasUsuario) {
				if (eu.getEmpresa().getIdEmpresa().equals(empresaPadrao.getIdEmpresa())) {
					final Filial filialPadrao = eu.getFilialPadrao();
					final Pessoa filialPessoa = filialPadrao.getPessoa();
					Hibernate.initialize(filialPadrao);
					Hibernate.initialize(filialPessoa);
					tfm.put("filialPadrao.idFilial", filialPadrao.getIdFilial());
					tfm.put("filialPadrao.sgFilial", filialPadrao.getSgFilial());
					tfm.put("filialPadrao.pessoa.nmFantasia", filialPessoa.getNmFantasia());
					break;
				}
			}
		}
		
		Empresa e = SessionUtils.getEmpresaSessao();
		tfm.put("empresaLogado.idEmpresa", e.getIdEmpresa());
		Filial f = SessionUtils.getFilialSessao();
		tfm.put("filialLogado.idFilial", f.getIdFilial());
		tfm.put("filialLogado.sgFilial", f.getSgFilial());
		tfm.put("filialLogado.pessoa.nmFantasia", f.getPessoa().getNmFantasia());
		tfm.put("usuarioLogado.usuarioClienteNaoAdmin", !usuarioLogado.isBlAdminCliente() && ("C".equals(usuarioLogado.getTpCategoriaUsuario().getValue()) || usuarioLogado.getCliente() != null));

		return tfm;
	}

	public void loadMeuPerfil(TypedFlatMap m) {
		
		Long idEmpresa = m.getLong("empresaLogado.idEmpresa");
		Empresa empresa = empresaService.findEmpresaLogadoById(idEmpresa);
		SessionContext.set(SessionKey.EMPRESA_KEY, empresa);

		Long idFilial = m.getLong("filialLogado.idFilial");
		Filial filial = filialService.findFilialLogadoById(idFilial);
		SessionContext.set(SessionKey.FILIAL_KEY, filial);
		
		Usuario usuario = SessionUtils.getUsuarioLogado();
		this.sessionContentLoaderService.executeForEmpresaFilial(usuario, empresa, filial);
	}
	
	public void storeMeuPerfil(TypedFlatMap m) {
		
		Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
		
		Long idEmpresa = m.getLong("empresaPadrao.idEmpresa");
		Empresa e = new Empresa();
		e.setIdEmpresa(idEmpresa);
		
		EmpresaUsuario empresaUsuario = this.empresaUsuarioService.findByEmpresaUsuario(e, usuarioLogado);
		
		getUsuarioDAO().storeMeuPerfil(m, empresaUsuario);
		
		final String password = m.getString("dsSenha");
		if (StringUtils.isNotBlank(password)) {
			this.authenticationService.changePassword(password);
		}
		
		this.sessionContentLoaderService.execute();
	}

	public void setSessionContentLoaderService(SessionContentLoaderService sessionContentLoaderService) {
		
		this.sessionContentLoaderService = sessionContentLoaderService;
	}


	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}


	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}
	
	public void setUsuarioLmsService(UsuarioLMSService usuarioLmsService) {
		this.usuarioLmsService = usuarioLmsService;
	}

	public Map findDadosUsuarioLogado() {
		Map map = new HashMap();
		
		Usuario u = SessionUtils.getUsuarioLogado();
		Empresa empresa = SessionUtils.getEmpresaSessao();
		Filial filial = SessionUtils.getFilialSessao();
		
		map.put("nomeUsuario", u.getNmUsuario());
		String strNomeEmpresa = empresa.getPessoa().getNmPessoa(); 
		if (StringUtils.isBlank(strNomeEmpresa)) {
			strNomeEmpresa = "Razão social da empresa é vazio";
		}
		map.put("nomeEmpresa", strNomeEmpresa);
		String strNomeFilial = filial.getPessoa().getNmFantasia(); 
		if (StringUtils.isBlank(strNomeFilial)) {
			strNomeFilial = filial.getPessoa().getNmPessoa();
		}
		map.put("nomeFilial", strNomeFilial);

		map.put("idUsuario", u.getIdUsuario());
		map.put("idEmpresa", empresa.getIdEmpresa());
		map.put("idFilial", filial.getIdFilial());
		map.put("blSorter", filial.getBlSorter());
		return map;
	}
	
	public Map findDadosUsuarioLogadoSWT() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Usuario usuario = SessionUtils.getUsuarioLogado();
		Empresa empresa = SessionUtils.getEmpresaSessao();
		Filial filial = SessionUtils.getFilialSessao();
		
		map.put("idUsuario", usuario.getIdUsuario());
		map.put("nmUsuario", usuario.getNmUsuario());
		
		map.put("idEmpresa", empresa.getIdEmpresa());
		map.put("nrIdentificacaoFormatadoEmpresa", FormatUtils.formatIdentificacao(empresa.getPessoa()));
		map.put("nrIdentificacaoEmpresa", empresa.getPessoa().getNrIdentificacao());
		map.put("nmFantasiaEmpresa", empresa.getPessoa().getNmPessoa());
		map.put("tpEmpresa", empresa.getTpEmpresa().getValue());		
				
		map.put("idFilial", filial.getIdFilial());
		map.put("idsFiliais", SessionUtils.getIdsFiliaisUsuarioLogado());
		map.put("sgFilial", filial.getSgFilial());
		map.put("nmFilial", filial.getPessoa().getNmPessoa());
		map.put("nmFantasiaFilial", filial.getPessoa().getNmFantasia());
		map.put("blFilialMatriz", SessionUtils.isFilialSessaoMatriz());
		map.put("lmsImplantadoFilial", filialService.lmsImplantadoFilial(filial));
		map.put("mwwImplantadoFilial", filial.getBlColetorDadoScan());
		
		//Propriedades para dar suporte aos mecanismos do JodaTime
		map.put("locale", usuario.getLocale());
		map.put("timezoneregion", filial.getDsTimezone());
		
		// mapeamento de codigo para siglas de filiais
		map.put("filialMapping", filialService.findFilialMapping());
		
		//FIXME idPostoAvancado e dsPostoAvancado está "chumbado" temporariamente conf. já estava no GT3. 
		//idPostoAvancado e dsPostoAvancado está associado ao usuário/empresa, 
		//Hoje está "chumbado" devido não saber quando o usuário está trabalhando no cliente ou na própria mercúrio.
		//Situação esta, está por resolver ainda com o grupo responsável
		//-----------------------------------------------------------------
		map.put("idPostoAvancado", Long.valueOf(2L)); 
		map.put("dsPostoAvancado", "Posto Porto Alegre (FIXME)");
		//-----------------------------------------------------------------

		String serverInfo = ServerInfo.getServerInfo();

		map.put("oracle.ons.instancename", serverInfo); 
		return map;
	}
	
	public void setEmpresaService(EmpresaService empresaService) {

		this.empresaService = empresaService;
	}
	
	/**
     * @see com.mercurio.lms.configuracoes.model.service.UsuarioService#findUsuarioByLogin(String)
     */
	public Usuario findUsuarioByLogin(String login){
		
		List result = this.getUsuarioDAO().findUsuarioByLogin(login);
		
		//Decorrente de poder SEMPRE HAVER SOMENTE UM usuario com o login espeficado... 
		if (!result.isEmpty()) {
			return (Usuario) result.get(0);
		}
		
		return null;
	}

	/**
	 * @see com.mercurio.lms.configuracoes.model.service.UsuarioService#findUsuarioByLogin(String)
	 */
	public Usuario findUsuarioByLoginIdFedex(String loginIdFedex){

		List result = this.getUsuarioDAO().findUsuarioByLoginIdFedex(loginIdFedex);

		//Decorrente de poder SEMPRE HAVER SOMENTE UM usuario com o login espeficado...
		if (!result.isEmpty()) {
			return (Usuario) result.get(0);
		}

		return null;
	}
	
	/**
	 * @see com.mercurio.lms.configuracoes.model.service.UsuarioService#findUsuarioClienteByLogin(String) 
     */
	public Object[] findUsuarioClienteByLogin(String login){
		return this.getUsuarioDAO().findUsuarioClienteByLogin(login);
	}

	public Boolean findUsuarioHasPerfil(Long idUsuario, String dsPerfil){
		return getUsuarioDAO().findUsuarioHasPerfil(idUsuario, dsPerfil);
	}

	public Boolean findUsuarioHasPerfil(Long idUsuario, Long idPerfil){
		return getUsuarioDAO().findUsuarioHasPerfil(idUsuario, idPerfil);
	}

	/**
	 * Retorna o usuario atraves da matricula.
	 * 
	 * Utilizado pela integração.
	 */
	public Usuario findByNrMatricula(String nrMatricula) {
		return getUsuarioDAO().findByNrMatricula(nrMatricula);
	}

	public List findLookupByNrMatricula(String nrMatricula){
		return getUsuarioDAO().findLookupByNrMatricula(nrMatricula);
	}
	
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return this.configuracoesFacade;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@Override
	public ContatoDto findContato(Long idCliente){
		return getUsuarioDAO().findContato(idCliente);
	}
	
}
