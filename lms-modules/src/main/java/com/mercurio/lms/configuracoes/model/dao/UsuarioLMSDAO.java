package com.mercurio.lms.configuracoes.model.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ClienteUsuario;
import com.mercurio.lms.configuracoes.model.EmpresaUsuario;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.FilialUsuario;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.RegionalUsuario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.param.ConsultarUsuarioLMSParam;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.GrupoClassificacaoFilial;
import com.mercurio.lms.municipios.model.RegionalFilial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 * 
 * @author juliosce
 */
public class UsuarioLMSDAO extends BaseCrudDao<UsuarioLMS, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return UsuarioLMS.class;
	}
	
	/**
	 * Verifica se já exite um usuarioLMS na base conforme o valor do parametro
	 * idUsuarioLMS.
	 * 
	 * @param idUsuarioLMS
	 * @return boolean
	 */
	public boolean jaExisteUsuarioLMS(Long idUsuarioLMS) {
		final String usuarioLMS = "select count(usuario.idUsuario) from " + UsuarioLMS.class.getName()
							+ " as usuario where usuario.id = ?";
		
		Long resultado = (Long) getAdsmHibernateTemplate().findUniqueResult(usuarioLMS, new Object[]{idUsuarioLMS});
		
		return (resultado==1);
	}

	/**
	 * Retorna o número de registro na busca da lookup, este médo só sera
	 * chamado se o usuário logado for administrador da filial.
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountGerenteFilial(Map criteria) {
		String usuarioADSMAdmimFilialHql = " count( f.id ) as quantidade ";
		SqlTemplate hql = montaHqlPaginateGerenteFilial( usuarioADSMAdmimFilialHql, criteria);
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult( hql.getSql(), hql.getCriteria());

		return Integer.valueOf(result.intValue());
	}

	/**
	 * Retorna o número de registro na busca na tela Pesquisar Login, este médo 
	 * só será chamado se o usuário logado for administrador da filial.
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountPesquisaLogin(Map criteria) {

		String usuarioADSMAdmimFilialHql = " count( f.id ) as quantidade ";
		SqlTemplate hql = montaHqlPaginatePesquisaLogin( usuarioADSMAdmimFilialHql, criteria);

		Long result = (Long) (Long) getAdsmHibernateTemplate().findUniqueResult( hql.getSql(), hql.getCriteria());
		return Integer.valueOf(result.intValue());
	}
	
	
	/**
	 * Retorna o número de registro existente na View Usuario.
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountUsuario(Map criteria) {
		String usuarioHql = "count( us.id ) as quantidade ";

		SqlTemplate hql = montaHqlPaginateUsuario(usuarioHql, criteria);

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		return Integer.valueOf(result.intValue());
	}

	public List<Map<String, Object>> findUsuarioLmsSuggest(String value, Integer limiteRegistros) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		String sql = getSqlFindUsuarioLmsSuggest(value, limiteRegistros, parametersValues);
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql, parametersValues, getConfigureSqlQueryFindUsuarioLmsSuggest());
	}
	
	private String getSqlFindUsuarioLmsSuggest(String value, Integer limiteRegistros, Map<String, Object> parametersValues){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ul.id_usuario as idUsuario, ua.nr_matricula as nrMatricula, ua.login as login, ua.nm_usuario as nmUsuario ");
		sql.append("FROM usuario_lms ul, usuario_adsm ua ");
		sql.append("WHERE ");
		sql.append("ul.id_usuario = ua.id_usuario ");
		sql.append("AND (lower(ua.nr_matricula) like :nrMatricula OR lower(ua.nm_usuario) like :nmUsuario OR lower(ua.login) like :login) ");
		
		if(limiteRegistros != null){
			sql.append("AND rownum <= :limite ");
			parametersValues.put("limite", limiteRegistros);
		}
		
		sql.append("ORDER BY ua.nm_usuario ");
		
		parametersValues.put("nrMatricula",  value.toLowerCase() + "%");
		parametersValues.put("nmUsuario",  value.toLowerCase() + "%");
		parametersValues.put("login",  value.toLowerCase() + "%");

		return sql.toString();
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryFindUsuarioLmsSuggest(){
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idUsuario", Hibernate.LONG);
				sqlQuery.addScalar("nrMatricula", Hibernate.STRING);
				sqlQuery.addScalar("login", Hibernate.STRING);
				sqlQuery.addScalar("nmUsuario", Hibernate.STRING);
			}
		};
		return csq;
	}
	
	/**
	 * Busca todos os usuários ADSM que são funcionários subordiandos ao usuário
	 * logado na sessão.
	 * 
	 * @param criteria
	 * @return List
	 */
	public List findLookupGerenteFilial(TypedFlatMap criteria) {
		String usuarioADSMAdmimFilialHql = " f.usuario ";
		
		SqlTemplate hql = montaHqlLookupGerenteFilial(usuarioADSMAdmimFilialHql, criteria);
		
		return getAdsmHibernateTemplate().find( hql.getSql(), hql.getCriteria() );
	}

	/**
	 * Metodo responsável por montar o template da string HQL que será executada
	 * pelo hibernate. Será usado pela lookup quando o usuário logado for administrador da
	 * filial
	 * 
	 * @param projection
	 * @param criteria
	 * @return SqlTemplate
	 */
	private SqlTemplate montaHqlLookupGerenteFilial(String projection, Map criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addInnerJoin(Funcionario.class.getName(), "f");
		hql.addInnerJoin("f.usuario", "us");
		hql.addInnerJoin("f.filial", "fil");

		hql.addCriteria(" us.tpCategoriaUsuario", "!=", "C");
		hql.addCriteria(" us.idUsuario", "!=", Long.valueOf(criteria.get("idUsuarioGerente").toString()));
		hql.addCriteria(" fil.idFilial", "=", Long.valueOf(criteria.get("idFilial").toString()));

		final String login = (String) criteria.get("login");
		if (StringUtils.isNotBlank(login)) {
			hql.addCriteria("lower(us.login)", " LIKE ", login.toUpperCase().trim() );
		}

		hql.addProjection(projection);

		return hql;
	}
	
	/**
	 * Faz a busca para lookup Login, considerando que o usuário logado é
	 * administrador da filial, retornando todos os seus subordinados menos ele e aqueles
	 * que forem administradores de filiais.
	 * 
	 * @param criteria
	 * @param findDef
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedGerenteFilial(Map criteria, FindDefinition findDef) {
		String usuarioADSMAdmimFilialHql = " new Map( f.usuario.nrMatricula as dsMatricula, " +
										   " f.usuario.nrMatricula as nrMatricula, " +
										   " f.usuario.nrMatricula as idMatricula, " +
										   " f.usuario.nmUsuario as nmUsuario, " +
										   " f.usuario.idUsuario as idUsuario, " +
										   " f.usuario.tpCategoriaUsuario as tpCategoriaUsuario," +
										   " f.usuario.login as login, " +
										   " f.usuario.blAdminCliente as blAdminCliente ) ";
		
		SqlTemplate hql = montaHqlPaginateGerenteFilial(usuarioADSMAdmimFilialHql, criteria);

		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),
														findDef.getCurrentPage(), 
														findDef.getPageSize(),
														hql.getCriteria());
	}
	
	/**
	 * Faz a busca usuarios ADSM para a tela Pesquisar Login, considerando que o 
	 * usuário logado é administrador da filial, retornando todos os seus subordinados menos ele.
	 * 
	 * @param criteria
	 * @param findDef
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedPesquisaLogin(Map criteria, FindDefinition findDef) {

		String usuarioADSMAdmimFilialHql = " new Map(usuario.nrMatricula as dsMatricula," +
													" usuario.nrMatricula as idMatricula," +
													" usuario.nmUsuario as nmUsuario," +
													" usuario.idUsuario as idUsuario," +
													" usuario.tpCategoriaUsuario as tpCategoriaUsuario," + 
													" usuario.login as login)";

		SqlTemplate hql = montaHqlPaginatePesquisaLogin(usuarioADSMAdmimFilialHql, criteria);

		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),
														findDef.getCurrentPage(), 
														findDef.getPageSize(),
														hql.getCriteria());
	}
	
	/**
	 * Retorna todos os usuário da View Usuario, com as restrições enviadas da
	 * tela.
	 * 
	 * @param criteria
	 * @param findDef
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedUsuario(Map criteria,	FindDefinition findDef) {
		String usuarioHql = " new Map( uadsm.nrMatricula as dsMatricula," +
									" uadsm.nrMatricula as idMatricula, " +
									" uadsm.nmUsuario as nmUsuario, " +
									" us.idUsuario as idUsuario," +
									" uadsm.tpCategoriaUsuario as tpCategoriaUsuario," +
									" uadsm.login as login," +
									" us.blAdminCliente as blAdminCliente )";
		
		SqlTemplate hql = montaHqlPaginateUsuario(usuarioHql, criteria);

		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),
														findDef.getCurrentPage(), 
														findDef.getPageSize(),
														hql.getCriteria());
	}
	
	/**
	 * Metodo responsável por montar o template da string HQL que será executada
	 * pelo hibernate. Será usado quando o usuário logado for administrador da
	 * filial
	 * 
	 * @param projection
	 * @param criteria
	 * @return SqlTemplate
	 */
	private SqlTemplate montaHqlPaginateGerenteFilial(String projection, Map criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addInnerJoin(Funcionario.class.getName(), "f");
		hql.addInnerJoin("f.usuario", "us");
		hql.addInnerJoin("f.filial", "fil");

		hql.addCriteria(" us.tpCategoriaUsuario", "!=", "C");
		hql.addCriteria(" us.idUsuario", "!=", criteria.get("idUsuarioGerente"));
		hql.addCriteria(" fil.idFilial", "=", criteria.get("idFilial"));
		
		final Map usuario = (Map) criteria.get("usuario");
		if (usuario != null) {
			final String idUsuario = (String) usuario.get("idUsuario");
			if (StringUtils.isNotBlank(idUsuario)) {
				hql.addCriteria("us.idUsuario", "=", Long.valueOf(idUsuario));
			}
			final String blAdminClienteStr = (String) usuario.get("blAdminCliente");
			if (blAdminClienteStr != null) {
				if (blAdminClienteStr.equals("N") || blAdminClienteStr.equals("false")) {
					hql.addCriteria("us.blAdminCliente", "=", Boolean.FALSE);
				} else if (blAdminClienteStr.equals("S") || blAdminClienteStr.equals("true")) {
					hql.addCriteria("us.blAdminCliente", "=", Boolean.TRUE);
				}
			}
		}
		hql.addProjection(projection);
		hql.addOrderBy(" us.nmUsuario asc ");
		return hql;
	}
	
	/**
	 * Metodo responsável por montar o template da string HQL que será executada
	 * pelo hibernate. Será usado quando o usuário logado for administrador da
	 * filial. Esta query é usada na tela Pesquisar Login
	 * 
	 * @param projection
	 * @param criteria
	 * @return SqlTemplate
	 */
	private SqlTemplate montaHqlPaginatePesquisaLogin(String projection, Map criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addInnerJoin(Funcionario.class.getName(), "f");
		hql.addLeftOuterJoin("f.usuario", "us");
		hql.addLeftOuterJoin("f.filial", "fil");
		
		hql.addCriteria(" us.tpCategoriaUsuario", "!=", "C");
		hql.addCriteria(" us.idUsuario", "!=", Long.valueOf(criteria.get("idUsuarioGerente").toString()));
		hql.addCriteria(" fil.idFilial", "=", Long.valueOf(criteria.get("idFilial").toString()));

		if (StringUtils.isNotBlank(criteria.get("nrMatricula.chapa").toString())) {
			hql.addCriteria("us.nrMatricula", "=", criteria.get("nrMatricula.chapa").toString().replaceAll("%", ""));
		}
		hql.addCriteria("us.tpCategoriaUsuario", "=", criteria.get("tpCategoriaUsuario"));

		final String login = (String) criteria.get("login");
		if (StringUtils.isNotBlank(login)) {
			hql.addCriteria("lower(us.login)", "like", login.toLowerCase().trim());
		}
		hql.addProjection(projection);
		hql.addOrderBy("us.nmUsuario asc ");
		return hql;
	}
	

	/**
	 * Metodo responsável por montar o template da string HQL que será executada
	 * pelo hibernate. Será usado quando o usuário logado for um usuário que não
	 * seja administrador da filial.
	 * 
	 * @param projection
	 * @param criteria
	 * @return SqlTemplate
	 */
	private SqlTemplate montaHqlPaginateUsuario(String projection, Map criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(UsuarioLMS.class.getName()+ " as us join us.usuarioADSM as uadsm ");

		final Map usuario = (Map) criteria.get("usuario");
		if (usuario != null) {
			final String idUsuario = (String) usuario.get("idUsuario");
			if (StringUtils.isNotBlank(idUsuario)) {
				hql.addCriteria("us.idUsuario", "=", Long.valueOf(idUsuario));
			}
			final String blAdminClienteStr = (String) usuario.get("blAdminCliente");
			if (blAdminClienteStr != null) {
				if (blAdminClienteStr.equals("N") || blAdminClienteStr.equals("false")) {
					hql.addCriteria("us.blAdminCliente", "=", Boolean.FALSE);
				} else if (blAdminClienteStr.equals("S") || blAdminClienteStr.equals("true")) {
					hql.addCriteria("us.blAdminCliente", "=", Boolean.TRUE);
				}
			}
		}
		hql.addProjection(projection);
		hql.addOrderBy(" uadsm.nmUsuario asc ");
		return hql;
	}

	/**
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountUsuarioADSM(Map criteria) {
		SqlTemplate hql = montaHQLFindPaginatedUsuarioAdsm(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
	}

	/**
	 * 
	 * @param criteria
	 * @param findDef
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedUsuarioADSM(Map criteria, FindDefinition findDef) {

		SqlTemplate hql = montaHQLFindPaginatedUsuarioAdsm(criteria);

		hql.addProjection(" new Map(us.nrMatricula as dsMatricula," +
									" us.nrMatricula as idMatricula, "+
									" us.nmUsuario as nmUsuario," +
									" us.idUsuario as idUsuario," + 
									" us.login as login," +
									" us.tpCategoriaUsuario as tpCategoriaUsuario) ");

		hql.addOrderBy(" us.nmUsuario asc ");

		return getAdsmHibernateTemplate().findPaginated(hql.getSql(true), 
														findDef.getCurrentPage(), 
														findDef.getPageSize(),
														hql.getCriteria());
	}

	/**
	 * Monta as restrições da pesquisa de usuários ADSM da lookup Login Usuário ADSM na tela manter usuário LMS
	 * @param criteria
	 * @return SqlTemplate
	 */
	private SqlTemplate montaHQLFindPaginatedUsuarioAdsm(Map criteria) {
		SqlTemplate hql = new SqlTemplate();
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		if (usuarioSessao.getBlAdminFilial() != null
				&& usuarioSessao.getBlAdminFilial() == true) {
			hql.addFrom(Funcionario.class.getName() + " as f join f.usuario as us join f.filial as f ");
			hql.addCriteria(" us.tpCategoriaUsuario", "!=", "C");
			hql.addCriteria(" us.idUsuario", "!=", Long.valueOf(criteria.get("idUsuarioGerente").toString()));
			hql.addCriteria(" f.idFilial", "=", Long.valueOf(criteria.get("idFilial").toString()));			
		}else{
			hql.addFrom(UsuarioADSM.class.getName() + " as us ");
			hql.addCriteria("us.tpCategoriaUsuario", "=", criteria.get("tpCategoriaUsuario"));			
		}
		if ((criteria.get("nrMatricula") != null) && (!StringUtils.isBlank(criteria.get("nrMatricula").toString()))) {
			hql.addCriteria("us.nrMatricula", "like", criteria.get("nrMatricula").toString().trim());
		}

		final Object blAtivoStr = criteria.get("blAtivo");
		hql.addCriteria("us.blAtivo", "=", blAtivoStr);

		if (StringUtils.isNotBlank(criteria.get("login").toString())) {
			hql.addCriteria("upper(us.login)", "like", criteria.get("login").toString().toUpperCase().trim());
		}

		final String nmUsuario = (String) criteria.get("nmUsuario");
		if (StringUtils.isNotBlank(nmUsuario)) {
			hql.addCriteria("upper(us.nmUsuario)", "like", nmUsuario.toUpperCase());
		}
	
		final String localeStr = (String) criteria.get("locale");
		if (StringUtils.isNotBlank(localeStr)) {
			hql.addCriteria("us.locale", "=", parseLocale(localeStr) );
		}

		return hql;
	}

	private Locale parseLocale(final String localeStr) {
		return new Locale( localeStr.toString() );
	}
	
	public UsuarioLMS findByIdWithAutoridade(Long id) {

		final String hql = "from " + UsuarioLMS.class.getName() + " as us "+
						" join fetch us.autoridade as aut " +
						" left join fetch aut.adminSistemas as admin " +
						" WHERE us.idUsuario = ?";

		return (UsuarioLMS)getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{id});
	}

	/**
	 * Este método verifica se já existe um usuarioLMS setado como administrador
	 * do cliente recebido por parametro e que não seja ele mesmo.
	 * 
	 * @param idCliente
	 * @return Boolean
	 */
	public boolean findExisteUmUsuarioAdminDoCliente(Long idCliente, Long idUsuario) {
		if (idCliente == null || idUsuario == null) {
			throw new IllegalArgumentException("idCliente and idUsuario are required");
		}
		final String hql = " SELECT ulms" +
					 " FROM "+UsuarioLMS.class.getName()+" as ulms " +
					 	" JOIN ulms.cliente as cli " +
					 " WHERE cli.idCliente = ? "+
					 " AND ulms.idUsuario != ? ";
		
		UsuarioLMS usuarioLMS = (UsuarioLMS) getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{idCliente, idUsuario});
		if (usuarioLMS != null) {
			Boolean blAdminCliente = usuarioLMS.getBlAdminCliente();
			if (blAdminCliente != null) {
				return blAdminCliente;
			} else {
				throw new IllegalStateException("Attribute blAdminCliente cannot be null on entity UsuarioLMS");
			}
		} else {
			return false;
		}
	}

	/**
	 * Retira a permisão de administrador do cliente de todos os usuarioLMS que
	 * são administrador do cliente corrente.
	 * 
	 * @param idCliente
	 */
	public void removerAdminSobreEsteCliente(Long idCliente) {
		String hql = " FROM "+UsuarioLMS.class.getName()+" as ulms " +
					 " JOIN ulms.cliente as c " +
					 " WHERE c.idCliente = ? ";
		List<UsuarioLMS> resultado = (List<UsuarioLMS>) getAdsmHibernateTemplate().find(hql, new Object[]{idCliente});
		if (resultado != null) {
			for (UsuarioLMS usuario : resultado) {
				if (usuario.getBlAdminCliente()) { // verifica se o usuario é administrador do cliente
					usuario.setBlAdminCliente(false);
					getAdsmHibernateTemplate().save(usuario);
				}
			}
		}
	}

	public UsuarioLMS findById(Long id) {
		final String hql =  "select ulms" +
		" from "+UsuarioLMS.class.getName()+" as ulms" +
			" join fetch ulms.usuarioADSM as us "+
		" where ulms.id = ?";

		UsuarioLMS usuarioLMS = (UsuarioLMS) getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{id});
		final Filial filial = usuarioLMS.getFilial();
		if (filial != null) {
			Hibernate.initialize(filial);
			Hibernate.initialize(filial.getPessoa());
		}
		final Cliente cliente = usuarioLMS.getCliente();
		if (cliente != null) {
			Hibernate.initialize(cliente);
			Hibernate.initialize(cliente.getPessoa());
		}
		return usuarioLMS;
	}
	
	/**
	 * Faz a busca dos dados da tela manterUsuarioLMS, este método é chamado na
	 * edição de um registro.
	 * 
	 * @param id
	 * @return Map
	 */
	public Map findById2(Long id) {

		final String hql =  "select ulms" +
							" from "+UsuarioLMS.class.getName()+" as ulms" +
							" join fetch ulms.usuarioADSM as us "+
							" left join fetch ulms.empresaPadrao as ep" +
							" left join fetch ep.pessoa as p" +
							" where ulms.id = ?";

		UsuarioLMS usuarioLMS = (UsuarioLMS) getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{id});
		if (usuarioLMS == null) {
			return new HashMap();
		} else {
			Map newResultado = new HashMap();

			Map nrMatricula = new HashMap();
			UsuarioADSM usuarioADSM = usuarioLMS.getUsuarioADSM();

			nrMatricula.put("idUsuario", usuarioADSM.getIdUsuario().toString());
			nrMatricula.put("login", usuarioADSM.getLogin());
			nrMatricula.put("nmUsuario", usuarioADSM.getNmUsuario());

			newResultado.put("usuarioADSM", nrMatricula);
			newResultado.put("idUsuarioLMS", usuarioLMS.getIdUsuario().toString());
			Cliente cliente = usuarioLMS.getCliente();
			if (cliente != null) {
				newResultado.put("idCliente", cliente.getIdCliente().toString());
				final Pessoa pessoa = cliente.getPessoa();
				newResultado.put("nmCliente", pessoa.getNmPessoa());
				newResultado.put("nrIdentificacao", pessoa.getNrIdentificacao());
				newResultado.put("tpIdentificacao", pessoa.getTpIdentificacao());
			}

			Filial filial = usuarioLMS.getFilial();
			if (filial != null) {
				newResultado.put("idFilial", filial.getIdFilial().toString());
				newResultado.put("nmFantasiaFilial", filial.getPessoa().getNmFantasia());
				newResultado.put("sgFilial", filial.getSgFilial());
			}

			newResultado.put("blIrrestritoCliente", usuarioLMS.getBlIrrestritoCliente());
			newResultado.put("blAdminCliente", usuarioLMS.getBlAdminCliente());
			newResultado.put("blAdminFilial", usuarioLMS.getBlAdminFilial());
			newResultado.put("tpCategoriaUsuario", usuarioADSM.getTpCategoriaUsuario().getValue());
			newResultado.put("clienteUsuario", findClientesUsuario(id));

			return newResultado;
		}

	}

	/**
	 * Retorna todos os clientes associados a um UsuarioLMS. São os clientes que
	 * o usuario tem acesso.
	 * 
	 * @param id
	 * @return List
	 */
	public List<ClienteUsuario> findClientesUsuario(Long idUsuario) {
		final String hql = "SELECT cliu " +
					" FROM "+ClienteUsuario.class.getName() + " as cliu " +
					" JOIN cliu.usuarioLMS as ulms " +
					" JOIN fetch cliu.cliente as cli" +
					" JOIN fetch cli.pessoa as p " +
					" WHERE ulms.id = ?";

		List find = (List) getAdsmHibernateTemplate().find(hql,	new Object[] {idUsuario});
		if (find == null) {
			find = Collections.EMPTY_LIST;
		}
		return find;
	}

	public Integer getRowCount(Map criteria) {
		SqlTemplate hql = montaHQLFindPaginatedUsuarioLMS(criteria, false);
		hql.addProjection(" count(*) as quantidade ");
		return getAdsmHibernateTemplate().getRowCountForQuery( hql.getSql(), hql.getCriteria());
	}

	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {

		SqlTemplate hql = montaHQLFindPaginatedUsuarioLMS(criteria, false);

		hql.addProjection(" new Map(us.nrMatricula as dsMatricula," +
									" us.login as login," +
									" us.nrMatricula as idMatricula," +
									" us.nrMatricula as nrMatricula," +
									" us.nmUsuario as nmUsuario," +
									" us.idUsuario as idUsuario ) ");

		hql.addOrderBy(" us.nmUsuario asc ");

		return getAdsmHibernateTemplate().findPaginated(hql.getSql(true),
														findDef.getCurrentPage(), 
														findDef.getPageSize(),
														hql.getCriteria());
	}

	/**
	 * Monta a String HQl da busca usada na tela de listagem, além disso este
	 * método é chamado pelo método findById(), getRowCount() entre outros.
	 * 
	 * @param criteria
	 * @param isJoinEmpresaPadrao
	 * @return SqlTemplate
	 */
	private SqlTemplate montaHQLFindPaginatedUsuarioLMS(Map criteria, boolean isJoinEmpresaPadrao) {
		SqlTemplate hql = new SqlTemplate();

		String from = UsuarioLMS.class.getName()+ " as ulms join ulms.usuarioADSM as us ";
		if (isJoinEmpresaPadrao) {
			from += " left join fetch ulms.empresaPadrao as ep join fetch ep.pessoa as p ";
		}
		hql.addFrom(from);

		hql.addCriteria("ulms.idUsuario", "=", criteria.get("hidUsuarioADSM"), Long.class);
		
		final String nrMatricula = (String) criteria.get("nrMatricula");
		if (StringUtils.isNotBlank(nrMatricula)) {
			hql.addCriteria("us.nrMatricula", "like", nrMatricula);
		}
		
		final String nmUsuario = (String)criteria.get("nmUsuario");
		if (StringUtils.isNotBlank(nmUsuario)) {
			hql.addCriteria("lower(us.nmUsuario)", "like", nmUsuario.toLowerCase());
		}
	
		final String login = (String) criteria.get("login");
		if (StringUtils.isNotBlank(login)) {
			hql.addCriteria("lower(us.login)", "like", login.toLowerCase());
		}
		
		final String idUsuarioStr = (String) criteria.get("idUsuario");
		if (StringUtils.isNotBlank(idUsuarioStr.trim())) {
			hql.addCriteria("ulms.idUsuario", "=", idUsuarioStr, Long.class);
		}

		return hql;

	}
	
	/**
	 * Retorna a lista de usuarioLMS baseado nos filtros informado dentro do objeto 'ConsultarUsuarioLMSParam'
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/09/2006
	 * 
	 * @param ConsultarUsuarioLMSParam cup
	 * @return List
	 */
	public List findLookupSistema(ConsultarUsuarioLMSParam cup){
		SqlTemplate hql = mountHqlFindUsuarioLMSSistema(cup);
		
		hql.addProjection("new Map(uadsm.nrMatricula as nrMatricula, " +
								  " uadsm.login as login, " +
								  " uadsm.nmUsuario as nmUsuario, " +
								  " uadsm.idUsuario as idUsuario)");
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public List findLookupSistemaMonitoramento(ConsultarUsuarioLMSParam cup){
		SqlTemplate hql = this.mountHqlFindUsuarioLMSSistemaMonitoramento(cup);
		
		hql.addProjection("new Map(uadsm.nrMatricula as nrMatricula, " +
								  " uadsm.login as login, " +
								  " uadsm.nmUsuario as nmUsuario, " +
								  " uadsm.idUsuario as idUsuario)");
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	private SqlTemplate mountHqlFindUsuarioLMSSistemaMonitoramento(ConsultarUsuarioLMSParam cup) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addInnerJoin(UsuarioLMS.class.getName(), "ulms");
		hql.addInnerJoin("ulms.usuarioADSM", "uadsm");
		
		hql.addCriteria("ulms.id", "=", cup.getIdUsuario());
		
		if (StringUtils.isNotBlank(cup.getNmUsuario())) {
			hql.addCriteria("lower(uadsm.nmUsuario)", "like", cup.getNmUsuario().toLowerCase());
		}
		
		if(StringUtils.isNotBlank(cup.getNrMatricula())){
			hql.addCriteria("lower(uadsm.nrMatricula)", "like", cup.getNrMatricula().toLowerCase());
		}
				
		if (StringUtils.isNotBlank(cup.getLogin())) {
			hql.addCriteria("lower(uadsm.login)", "like", cup.getLogin().toLowerCase());
		}
		
		if (StringUtils.isNotBlank(cup.getTpCategoriaUsuario())) {
			hql.addCriteria("uadsm.tpCategoriaUsuario", "=", new DomainValue(cup.getTpCategoriaUsuario()));
		}
		
		hql.addOrderBy("uadsm.nrMatricula");
		hql.addOrderBy("uadsm.login");
		
		return hql;
	}
	
	/**
	 * Retorna a lista de usuarioLMS paginado baseado nos filtros informado dentro do objeto 'ConsultarUsuarioLMSParam'
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/09/2006
	 * 
	 * @param ConsultarUsuarioLMSParam cup
	 * @return ResultSetPage
	 */	
	public ResultSetPage findPaginatedSistema(ConsultarUsuarioLMSParam cup, FindDefinition findDef){
		SqlTemplate hql = mountHqlFindUsuarioLMSSistema(cup);
		
		hql.addProjection("new Map(uadsm.nrMatricula as nrMatricula, " +
						  " uadsm.login as login, " +
						  " uadsm.nmUsuario as nmUsuario, " +
						  " uadsm.idUsuario as idUsuario)");
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), 
														findDef.getCurrentPage(), 
														findDef.getPageSize(), 
														hql.getCriteria());
	}
	
	/**
	 * Retorna o número de usuarios baseado nos filtros informado dentro do objeto 'ConsultarUsuarioLMSParam'
	 * 
	 * author Mickaël Jalbert
	 * @since 21/09/2006
	 * 
	 * @param ConsultarUsuarioLMSParam cup
	 * @return Integer
	 */	
	public Integer getRowCountSistema(ConsultarUsuarioLMSParam cup){
		SqlTemplate hql = mountHqlFindUsuarioLMSSistema(cup);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
	}		
	
	private SqlTemplate mountHqlFindUsuarioLMSSistema(ConsultarUsuarioLMSParam cup) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addInnerJoin(UsuarioLMS.class.getName(), "ulms");
		hql.addInnerJoin("ulms.usuarioADSM", "uadsm");
		
		hql.addCriteria("ulms.id", "=", cup.getIdUsuario());
		
		if (StringUtils.isNotBlank(cup.getNmUsuario())) {
			hql.addCriteria("lower(uadsm.nmUsuario)", "like", cup.getNmUsuario().toLowerCase());
		}
		
		if(StringUtils.isNotBlank(cup.getNrMatricula())){
			hql.addCriteria("lower(uadsm.nrMatricula)", "like", cup.getNrMatricula().toLowerCase());
		}
				
		if (StringUtils.isNotBlank(cup.getLogin())) {
			hql.addCriteria("lower(uadsm.login)", "like", cup.getLogin().toLowerCase());
		}
		
		hql.addCriteria("uadsm.tpCategoriaUsuario", "=", new DomainValue(cup.getTpCategoriaUsuario()));

		hql.addOrderBy("uadsm.nrMatricula");
		hql.addOrderBy("uadsm.login");
		
		return hql;
	}

	/**
	 * 
	 * @param criteria
	 * @return List
	 */
	// TODO Algum dia reescrever esse método porque do jeito que está 
	// tá ALTAMENTE propenso a erros devido a mudanças no código.
	public List findLookupFilial(Map criteria) {
		StringBuffer sb = new StringBuffer(500);
		
		sb.append("SELECT DISTINCT new map(filial.idFilial as idFilial, ")
			.append("pessoa.tpIdentificacao as pessoa_tpIdentificacao, ")
			.append("pessoa.nrIdentificacao as pessoa_nrIdentificacao, ")	
			.append("pessoa.nmFantasia as pessoa_nmFantasia, ")
			.append("filial.sgFilial as sgFilial, ") 
			.append("filial.sgFilial || ' - ' || pessoa.nmFantasia as siglaNomeFilial, ")
			.append("moeda.sgMoeda || ' ' || moeda.dsSimbolo as moeda_siglaSimbolo, ")
			.append("empresa.idEmpresa as empresa_idEmpresa, ")
			.append("empresa.tpEmpresa as empresa_tpEmpresa, ")
			.append("empresaPessoa.nmPessoa as empresa_pessoa_nmPessoa, ")
			.append("empresaPessoa.nrIdentificacao as empresa_pessoa_nrIdentificacao, ")
			.append("empresaPessoa.tpIdentificacao as empresa_pessoa_tpIdentificacao, ")
			
			.append("lastHistoricoFilial.tpFilial as lastHistoricoFilial_tpFilial , ")
			.append("lastHistoricoFilial.dtRealOperacaoInicial as lastHistoricoFilial_dtRealOperacaoInicial, ") 
			.append("lastHistoricoFilial.dtRealOperacaoFinal as lastHistoricoFilial_dtRealOperacaoFinal) ");
		
		if( criteria.get("usuario") != null ){
			sb.append("FROM ").append(Filial.class.getName()).append(" as filial, ")
				.append(EmpresaUsuario.class.getName()).append(" as empresaUsuario ")
				.append(" JOIN empresaUsuario.usuario as usuario ")
				.append(" JOIN empresaUsuario.empresa as empresaUs ");
		}else{
			sb.append("FROM ").append(Filial.class.getName()).append(" as filial ");
		}		
		sb.append("inner join filial.pessoa as pessoa ")
			.append("left join filial.moeda as moeda ")
			.append("inner join filial.empresa as empresa ")
			.append("inner join empresa.pessoa as empresaPessoa ");
		
		String regionaisSelecionadas = (String)criteria.get("regionaisSelecionadas");
		if( StringUtils.isNotBlank(regionaisSelecionadas) ){
			sb.append("left join filial.regionalFiliais as regionalFiliais ")
			  .append("left join regionalFiliais.regional as regional ");			
		}
		
		sb.append("inner join filial.historicoFiliais as lastHistoricoFilial ")
			.append("WHERE lastHistoricoFilial.id = ")
			.append("(select max(hf2.idHistoricoFilial) from com.mercurio.lms.municipios.model.HistoricoFilial as hf2 ")
			.append(" WHERE hf2.filial = lastHistoricoFilial.filial) ");
		
		Map<String, Object> namedParams = new HashMap<String, Object>();
		String filiaisSelecionadas = (String)criteria.get( "filiaisSelecionadas" );
		
		/*Filtro para Regionais*/
		if (regionaisSelecionadas != null && !StringUtils.isBlank( regionaisSelecionadas )) {		
		
			if( filiaisSelecionadas!= null && !StringUtils.isBlank(filiaisSelecionadas) ){
				addHql(sb, " ( regional.id in(:idRegional)" );
			}else{
				addHql(sb, " regional.id in(:idRegional)" );
			} 
			String[] fs = regionaisSelecionadas.split(",");
			Long[] regionalIds = new Long[fs.length];
			for(int idx = 0; idx<fs.length; idx++) {
				regionalIds[idx] = Long.valueOf(fs[idx]);
			}
			namedParams.put("idRegional", regionalIds);
		} 

		/*Filtro para Filiais*/
		if( StringUtils.isNotBlank(filiaisSelecionadas) ){
			if (StringUtils.isNotBlank( regionaisSelecionadas )) {
				sb.append( " OR filial.idFilial in(:idFilial) )" );
			}else{
				addHql(sb, " filial.idFilial in(:idFilial)");
			}
			String[] fs = filiaisSelecionadas.split(",");
			Long[] filialIds = new Long[fs.length];
			for(int idx = 0; idx<fs.length; idx++) {
				filialIds[idx] = Long.valueOf(fs[idx]);
			}
			namedParams.put("idFilial", filialIds);
		}
		if (criteria.get("usuario") != null){
			Usuario usuario = (Usuario)criteria.get("usuario");
			addHql(sb, " usuario.idUsuario=:idUsuario");
			namedParams.put("idUsuario", usuario.getIdUsuario()); 
			addHql(sb, " empresaUs.idEmpresa=empresa.idEmpresa");
		}
		
		/*Filtro sigla da filial*/
		addHql(sb, " lower(filial.sgFilial) like lower(:sgFilial)");
		namedParams.put("sgFilial", criteria.get("sgFilial")); 
		
		String flagType = ((String)criteria.get("flagType") == null) ? "" : (String)criteria.get("flagType");
		String flag = ((String)criteria.get("flag") == null) ? "" : (String)criteria.get("flag");
		String[] types = getTypes(flagType,flag);
		addHql(sb, "lastHistoricoFilial.tpFilial in(:inFilial)");
		namedParams.put("inFilial", types);

		/*Filtro para empresa*/
		final String idEmpresaStr = (String) criteria.get("empresa.idEmpresa");
		if (StringUtils.isNotBlank(idEmpresaStr)) {
			addHql(sb,"empresa.idEmpresa = :idEmpresa");
			namedParams.put("idEmpresa", Long.valueOf(idEmpresaStr));
		}
		
		/*Filtro para tipo de empresa*/
		if (criteria.get("empresa") != null){
			Map mapEmpresa = (Map)criteria.get("empresa");
			Object tpEmpresa = mapEmpresa.get("tpEmpresa");
			if (tpEmpresa != null) {
				addHql(sb,"empresa.tpEmpresa = :tpEmp");
				namedParams.put("tpEmp", tpEmpresa);
			}
		}

		return getAdsmHibernateTemplate().findByNamedParam(sb.toString(), namedParams);
		
	}
	
	/**
	 * 
	 * @param criteria
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedEmpresa( Map criteria, FindDefinition findDef) {
		final String hql = "from " + Empresa.class.getName() + " as e" +
							" inner join fetch e.pessoa as p"+
							" inner join fetch e.usuariosEmpresa as ue"+
							" inner join fetch ue.usuario as us"+
							" where us.idUsuario = :idUsuario";

		ResultSetPage l = getAdsmHibernateTemplate().findPaginated(hql, 
																   findDef.getCurrentPage(),
																   findDef.getPageSize(), criteria );
		
		return l;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountEmpresa( Map criteria) {
		final String hql = " select count(e) as quantidade "+
							" from " + Empresa.class.getName() + " as e"+
							" inner join e.pessoa as p"+
							" inner join e.usuariosEmpresa as ue"+
							" inner join ue.usuario as us"+
							" where us.idUsuario = :idUsuario";

		return getAdsmHibernateTemplate().getRowCountForQuery( hql, criteria );
	}
	
	/**
	 * 
	 * @param criteria
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedFilial(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = new SqlTemplate();
		
		sql = createHqlPaginatedFilial(criteria );
		
		sql.addOrderBy("filial.sgFilial");
		sql.addOrderBy("pessoa.nmFantasia");
		
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),
														findDef.getCurrentPage(),
														findDef.getPageSize(),
														sql.getCriteria());
	}
	
	private SqlTemplate getProjectionFilial(){
		SqlTemplate sql = new SqlTemplate();

		// Projections
		sql.addProjection(" DISTINCT new map( filial.idFilial","idFilial");
		sql.addProjection("pessoa.tpIdentificacao","pessoa_tpIdentificacao");
		sql.addProjection("pessoa.nrIdentificacao","pessoa_nrIdentificacao");
		sql.addProjection("moeda.sgMoeda || ' ' || moeda.dsSimbolo","moeda_siglaSimbolo");
		sql.addProjection("pessoa.nmFantasia","pessoa_nmFantasia");
		sql.addProjection("filial.sgFilial","sgFilial");
		sql.addProjection("filialByIdFilialResponsavel.sgFilial","filialByIdFilialResponsavel_sgFilial"); 
		sql.addProjection("responsavelPessoa.nmFantasia","filialByIdFilialResponsavel_pessoa_nmFantasia");
		sql.addProjection("filialByIdFilialResponsavalAwb.sgFilial","filialByIdFilialResponsavalAwb_sgFilial");
		sql.addProjection("responsavalAwbPessoa.nmFantasia","filialByIdFilialResponsavalAwb_pessoa_nmFantasia");
		sql.addProjection("lastHistoricoFilial.tpFilial","lastHistoricoFilial_tpFilial");
		sql.addProjection("lastHistoricoFilial.dtRealOperacaoInicial","lastHistoricoFilial_dtRealOperacaoInicial"); 
		sql.addProjection("lastHistoricoFilial.dtRealOperacaoFinal","lastHistoricoFilial_dtRealOperacaoFinal");
		sql.addProjection("filial.dtImplantacaoLMS","dtImplantacaoLMS");
		sql.addProjection("empresa.idEmpresa","empresa_idEmpresa");
		sql.addProjection("empresa.tpEmpresa","empresa_tpEmpresa");
		sql.addProjection("empresaPessoa.nmPessoa","empresa_pessoa_nmPessoa");
		sql.addProjection("empresaPessoa.nrIdentificacao","empresa_pessoa_nrIdentificacao");
		sql.addProjection("empresaPessoa.tpIdentificacao","empresa_pessoa_tpIdentificacao)");
		
		return sql;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountFilial(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
			
		sql = createHqlPaginatedFilial(criteria );

		return Integer.valueOf( getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()).size());
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * TODO Reescrever, altamente sucetivel a erros a forma que está escrito.
	 */
	private SqlTemplate createHqlPaginatedFilial(TypedFlatMap criteria ) {
		SqlTemplate sql = getProjectionFilial();
		
		//From,
		StringBuffer froms = new StringBuffer();
		if( criteria.get("usuario") != null ){
			froms.append( Filial.class.getName() ).append(" as filial, ").append(EmpresaUsuario.class.getName()).append(" as empresaUsuario ");
		}else{
			froms.append( Filial.class.getName() ).append(" as filial ");
		}
		froms.append("inner join filial.pessoa as pessoa ")
		.append("inner join filial.empresa as empresa ")
		.append("inner join empresa.pessoa as empresaPessoa ") 
		.append("left join filial.filialByIdFilialResponsavel as filialByIdFilialResponsavel ")
		.append("left join filial.filialByIdFilialResponsavalAwb as filialByIdFilialResponsavalAwb ")
		.append("left join filialByIdFilialResponsavel.pessoa as responsavelPessoa ")
		.append("left join filialByIdFilialResponsavalAwb.pessoa as responsavalAwbPessoa ")
		.append("left join filial.moeda as moeda ");

		if (criteria.getLong("pessoa.enderecoPessoas.municipio.unidadeFederativa.idUnidadeFederativa") != null)
			froms.append("inner join pessoa.enderecoPessoas as enderecoPessoas ")
					.append("inner join enderecoPessoas.municipio as municipio ")
					.append("inner join municipio.unidadeFederativa as unidadeFederativa ");
		
		final String regionaisSelecionadas = criteria.getString("regionaisSelecionadas");
		if (criteria.getLong("regionalFiliais.regional.idRegional") != null || 
				StringUtils.isNotBlank(regionaisSelecionadas)) {
			froms.append("left join filial.regionalFiliais as regionalFiliais ")
				.append("left join regionalFiliais.regional as regional ");
		}

		froms.append("left join filialByIdFilialResponsavalAwb.pessoa as responsavalAwbPessoa ")
			.append("left join filial.historicoFiliais as lastHistoricoFilial ");
		
		sql.addFrom(froms.toString());

		//Critérios
		final String filtraLastHistorico = "lastHistoricoFilial.id = (select max(hf2.idHistoricoFilial)" +
											" from com.mercurio.lms.municipios.model.HistoricoFilial as hf2" +
											" where hf2.filial = lastHistoricoFilial.filial) ";
		sql.addCustomCriteria(filtraLastHistorico);
		
		if (criteria.getLong("pessoa.enderecoPessoas.municipio.unidadeFederativa.idUnidadeFederativa") != null) {
			final String filtraEnderecoPessoa = "(enderecoPessoas.id = (SELECT max(EP.id) FROM " + EnderecoPessoa.class.getName() + " as EP " +
																		" WHERE EP.pessoa.idPessoa = enderecoPessoas.pessoa.idPessoa" +
																		"	 AND EP.dtVigenciaInicial <= ?" +
																		"	 AND (EP.dtVigenciaFinal IS NULL OR EP.dtVigenciaFinal >= ?)))"; 
			sql.addCustomCriteria(filtraEnderecoPessoa);
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			
			sql.addCriteria("unidadeFederativa.idUnidadeFederativa","=",criteria.getLong("pessoa.enderecoPessoas.municipio.unidadeFederativa.idUnidadeFederativa"));
		}
		
		if (criteria.getLong("regionalFiliais.regional.idRegional") != null ) {
			sql.addCustomCriteria(new StringBuffer("regionalFiliais.id = (SELECT max(RE.id) FROM ").append(RegionalFilial.class.getName()).append(" as RE WHERE RE.id = regionalFiliais.id AND RE.dtVigenciaInicial <= ? AND (RE.dtVigenciaFinal IS NULL OR RE.dtVigenciaFinal >= ?)) ").toString());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			
			sql.addCriteria("regional.idRegional","=",criteria.getLong("regionalFiliais.regional.idRegional"));
		}
		
		if (criteria.getLong("idFilial") != null) {
			sql.addCriteria("filial.idFilial","=",criteria.getLong("idFilial"));
			return sql;
		}

		sql.addCriteria("filial.idFilial","=",criteria.getLong("idProcessoWorkflow"));
			
		if (criteria.getLong("empresa.idEmpresa") != null)
			sql.addCriteria("empresa.idEmpresa","=",criteria.getLong("empresa.idEmpresa"));

		if (StringUtils.isNotBlank(criteria.getString("empresa.tpEmpresa")))
			sql.addCriteria("empresa.tpEmpresa","=",criteria.getString("empresa.tpEmpresa"));
 
		if (StringUtils.isNotBlank(criteria.getString("sgFilial"))) {
			sql.addCustomCriteria("lower(filial.sgFilial) LIKE lower(?)");
			sql.addCriteriaValue(criteria.getString("sgFilial"));
		}
		
		if (criteria.getYearMonthDay("dtImplantacaoLMS") != null)
			sql.addCriteria("filial.dtImplantacaoLMS","=",criteria.getYearMonthDay("dtImplantacaoLMS"));

		if (StringUtils.isNotBlank(criteria.getString("pessoa.tpIdentificacao")))
			sql.addCriteria("pessoa.tpIdentificacao","=",criteria.getString("pessoa.tpIdentificacao"));
		
		if (StringUtils.isNotBlank(criteria.getString("pessoa.nrIdentificacao"))) {
			sql.addCustomCriteria("lower(pessoa.nrIdentificacao) LIKE lower(?)");
			sql.addCriteriaValue(criteria.getString("pessoa.nrIdentificacao"));
		}

		if (StringUtils.isNotBlank(criteria.getString("pessoa.nmFantasia"))) {
			sql.addCustomCriteria("lower(pessoa.nmFantasia) LIKE lower(?)");
			sql.addCriteriaValue(criteria.getString("pessoa.nmFantasia"));
		}

		if (StringUtils.isNotBlank(criteria.getString("pessoa.nmPessoa"))) {
			sql.addCustomCriteria("lower(pessoa.nmPessoa) LIKE lower(?)");
			sql.addCriteriaValue(criteria.getString("pessoa.nmPessoa"));
		}

		if (criteria.getYearMonthDay("historicoFiliais.vigenteEm") != null) {
			sql.addCustomCriteria("lastHistoricoFilial.dtRealOperacaoInicial <= ? AND (lastHistoricoFilial.dtRealOperacaoFinal is null OR lastHistoricoFilial.dtRealOperacaoFinal >= ?)");
			sql.addCriteriaValue(criteria.getYearMonthDay("historicoFiliais.vigenteEm"));
			sql.addCriteriaValue(criteria.getYearMonthDay("historicoFiliais.vigenteEm"));
		}

		if (StringUtils.isNotBlank(criteria.getString("historicoFiliais.tpFilial")))
			sql.addCriteria("lastHistoricoFilial.tpFilial","=",criteria.getString("historicoFiliais.tpFilial"));

		if (criteria.getYearMonthDay("historicoFiliais.dtRealOperacaoInicial") != null)
			sql.addCriteria("lastHistoricoFilial.dtRealOperacaoInicial",">=",criteria.getYearMonthDay("historicoFiliais.dtRealOperacaoInicial"));

		if (criteria.getYearMonthDay("historicoFiliais.dtRealOperacaoFinal") != null)
			sql.addCriteria("lastHistoricoFilial.dtRealOperacaoFinal","<=",criteria.getYearMonthDay("historicoFiliais.dtRealOperacaoFinal"));

		if (criteria.getLong("filialByIdFilialResponsavel.idFilial") != null)
			sql.addCriteria("filialByIdFilialResponsavel.id","=",criteria.getLong("filialByIdFilialResponsavel.idFilial"));

		if (criteria.getLong("filialByIdFilialResponsavalAwb.idFilial") != null)
			sql.addCriteria("filialByIdFilialResponsavalAwb.id","=",criteria.getLong("filialByIdFilialResponsavalAwb.idFilial"));

		if (criteria.getLong("grupoClassificacaoFiliais.divisaoGrupoClassificacao.idDivisaoGrupoClassificacao") != null) {
			sql.addCustomCriteria(new StringBuffer("filial.id in (Select distinct(GC.filial.idFilial) from ")
						.append(GrupoClassificacaoFilial.class.getName()).append(" AS GC where GC.divisaoGrupoClassificacao.id = ?)").toString());
			sql.addCriteriaValue(criteria.getLong("grupoClassificacaoFiliais.divisaoGrupoClassificacao.idDivisaoGrupoClassificacao"));
		}

		if (criteria.getLong("grupoClassificacaoFiliais.divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao") != null) {
			sql.addCustomCriteria(new StringBuffer("filial.id in (Select distinct(GC2.filial.idFilial) from ")
						.append(GrupoClassificacaoFilial.class.getName()).append(" GC2 where GC2.divisaoGrupoClassificacao.grupoClassificacao.id = ?)").toString());
			sql.addCriteriaValue(criteria.getLong("grupoClassificacaoFiliais.divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao"));
		}

		if (StringUtils.isNotBlank(criteria.getString("obFilial"))) {
			String[] tp = ((String) criteria.get("obFilial")).split(":");
			String[] tpFilial = getTypes(tp[0], tp[1]);
			StringBuffer criterios = new StringBuffer();
			char token = ' ';
			for(int x = 0; x < tpFilial.length; x++) {
				criterios.append(token).append("?");
				token = ',';
				sql.addCriteriaValue(tpFilial[x]);
			}
			sql.addCustomCriteria(new StringBuffer("lastHistoricoFilial.tpFilial in (").append(criterios).append(") ").toString());
		} 
		
		final String filiaisSelecionadas = criteria.getString("filiaisSelecionadas");
		if( StringUtils.isNotBlank(regionaisSelecionadas ) ){
			if( StringUtils.isNotBlank(filiaisSelecionadas ) ){
				sql.addCustomCriteria( " ( regional.idRegional in( "+regionaisSelecionadas+" )");
			} else {
				sql.addCustomCriteria( " ( regional.idRegional in( "+regionaisSelecionadas+" )");
			}
		}
		
		if( StringUtils.isNotBlank(filiaisSelecionadas ) ){
			if( StringUtils.isNotBlank(regionaisSelecionadas ) ){
				sql.add( " OR filial.idFilial in ("+ filiaisSelecionadas + ") )");
			}else{
				sql.add( " filial.idFilial in ("+ filiaisSelecionadas + " )");
			}
		}
		
		if (criteria.get("usuario") != null){
			Usuario usuario = (Usuario)criteria.get("usuario");
			sql.addCriteria("empresaUsuario.usuario.idUsuario","=", usuario.getIdUsuario());
			sql.add(" AND empresaUsuario.empresa.idEmpresa = empresa.idEmpresa");
		}					
		return sql;
	}

	/**
	 * 
	 * @param sb
	 * @param hql
	 * @return
	 */
	private StringBuffer addHql(StringBuffer sb, String hql) {
		if (sb.length() == 0)
			sb.append("where ");
		else
			sb.append(" and ");
		return sb.append(hql);
	}
	
	/**
	 * 
	 * @param typeRecebido
	 * @param flag
	 * @return
	 */
	public String[] getTypes(String typeRecebido, String flag) {
		if (typeRecebido.equals("FI") || (typeRecebido.equals("FR") && flag.equals("C")))
			return new String[] { "FI","FR" };
		else if (typeRecebido.equals("LO"))
			return new String[] { "FI", "FR" };
		else
			return new String[] { "FI", "FR", "LO", "MA", "PA", "SU", "LG", "OP" }; 
	}
	
	public void removeClienteUsuario(List<Long> ids) {
		getAdsmHibernateTemplate().removeByIds("delete from " + ClienteUsuario.class.getName() + " as cu " +
											" where cu.usuarioLMS.id in ( :id )", ids);
	}

	public void removeEmpresaUsuario(List<Long> ids) {

		getAdsmHibernateTemplate().removeByIds(
				"delete from " + FilialUsuario.class.getName()
						+ " as fu where fu.empresaUsuario.id in ( select eu from "
						+ EmpresaUsuario.class.getName()
						+ " eu where eu.usuario.id in ( :id ) )", ids);
		getAdsmHibernateTemplate().removeByIds(
				"delete from " + RegionalUsuario.class.getName()
						+ " as ru where ru.empresaUsuario.id in ( select eu from "
						+ EmpresaUsuario.class.getName()
						+ " eu where eu.usuario.id in ( :id ) )", ids);
		getAdsmHibernateTemplate().removeByIds(
				"delete from " + EmpresaUsuario.class.getName()
						+ " as eu where eu.usuario.id in ( :id )", ids);
		getAdsmHibernateTemplate().flush();
	}

	@Override
	public int removeByIds(List ids) {
		removeEmpresaUsuario(ids);
		removeClienteUsuario(ids);
		return super.removeByIds(ids);
	}

	public void updateBlTermoComp(Long idUsuario, boolean blTermoComp) {
		getAdsmHibernateTemplate().bulkUpdate("update "+UsuarioLMS.class.getName()+" ulms " +
												" set ulms.blTermoComp = ? where ulms.id = ?",
												new Object[] {blTermoComp, idUsuario});
	}
}
