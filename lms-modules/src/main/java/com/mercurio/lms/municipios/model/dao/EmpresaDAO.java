package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import com.mercurio.lms.configuracoes.model.Pessoa;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureRestrictionsBuilder;
import com.mercurio.adsm.framework.model.hibernate.RestrictionBuilder;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class EmpresaDAO extends BaseCrudDao<Empresa, Long> {
	private ConfigureRestrictionsBuilder rbFilial;

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Empresa.class;
	}

	/* (não-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindPaginatedLazyProperties(java.util.Map)
	 */
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pessoa",FetchMode.JOIN);
	}
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("pessoa",FetchMode.JOIN);		
	}
	
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("pessoa", FetchMode.JOIN);
	}
	
	protected void initDao() throws Exception {
		super.initDao();
		rbFilial = new ConfigureRestrictionsBuilder() {
			public void configure(RestrictionsBuilder rb) {
				List list = new ArrayList(1);
				list.add("tpEmpresa");
				RestrictionBuilder restrict = new RestrictionBuilder() {
						public void buildRestriction(DetachedCriteria dc, String ownerProperty, Map values) {
							if (((DomainValue)values.get("tpEmpresa")).getValue().length() == 2)
								dc.add(Restrictions.in("tpEmpresa",new String[]{"M","P"}));
							else
								dc.add(Restrictions.eq("tpEmpresa",values.get("tpEmpresa")));
						}
				};
				rb.addCustomRestrictionBuilder(list, restrict);
			}
		};
	}
	
	public ResultSetPage findPaginatedFilial(Map criteria, FindDefinition findDef) {
		RestrictionsBuilder restrictions = new RestrictionsBuilder(getPersistentClass(), false);
		return super.findPaginated(criteria, findDef, restrictions, rbFilial);
	}
	
	
	/**
	 * Finder para combo de Empresas.<BR>
	 * Quando contida chave 'tpEmpresa', consulta as empresas filtrando pelo seu tipo.<BR>
	 * @param map
	 * @return nome e identificador da empresa: map -> { pessoa = { nmPessoa }, idEmpresa }
	 */
	public List findComboEmpresa(Map map){
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("pessoa","p");
		dc.setProjection(
				Projections.projectionList()
					.add(Projections.property("tpSituacao"),"tpSituacao")
					.add(Projections.property("p.nmPessoa"),"pessoa_nmPessoa")
					.add(Projections.property("p.nrIdentificacao"),"nrIdentificacao")
					.add(Projections.property("p.tpIdentificacao"),"tpIdentificacao")
					.add(Projections.property("idEmpresa"),"idEmpresa")
				);
		if (map.containsKey("tpEmpresa")){
			dc.add(Restrictions.eq("tpEmpresa", (String)map.get("tpEmpresa")));	
		}
		dc.addOrder(Order.asc("p.nmPessoa"));
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}
	
	public List findLookupByCriteria(Map map) {
		return super.findLookupByCriteria(map);
	}

	public List findLookupFilial(Map map) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.createAlias("pessoa", "p");

		String tpEmpresa = MapUtils.getString(map, "tpEmpresa");
		if(StringUtils.isBlank(tpEmpresa))
			dc.add(Restrictions.in("tpEmpresa", new String[]{"M", "P"}));
		else
			dc.add(Restrictions.eq("tpEmpresa", tpEmpresa));

		dc.add(Restrictions.eq("p.nrIdentificacao", MapUtils.getString(MapUtils.getMap(map, "pessoa"), "nrIdentificacao")));

		String tpSituacao = MapUtils.getString(map, "tpSituacao");
		if(StringUtils.isNotBlank(tpSituacao))
			dc.add(Restrictions.eq("tpSituacao", tpSituacao));

		return findByDetachedCriteria(dc);
	}

	public Integer getRowCountFilial(Map criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setProjection(Projections.rowCount());
		FindDefinition fd = new FindDefinition(dc, null, null);
		RestrictionsBuilder rb = new RestrictionsBuilder(getPersistentClass(), false);
		Integer i = getRowCount(criteria,fd,rb, rbFilial);
		return i;
	}
	
	/**
	 * Verifica a existencia da especialização com mesmo Numero e Tipo de Identificacao, exceto a mesma.
	 * @param map
	 * @return a existência de uma especialização
	 */
	public boolean verificaExistenciaEspecializacao(Empresa empresa){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.createAlias("pessoa", "pessoa_");
		dc.add(Restrictions.eq("pessoa_.nrIdentificacao", empresa.getPessoa().getNrIdentificacao()));
		dc.add(Restrictions.eq("pessoa_.tpIdentificacao", empresa.getPessoa().getTpIdentificacao().getValue()));
		dc.setProjection(Projections.rowCount());
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);	
	}

	public List findByTpEmpresaTpSituacao(String tpEmpresa, String tpSituacao) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("e.idEmpresa"), "idEmpresa")
			.add(Projections.property("p.nmPessoa"), "pessoa_nmPessoa")
			.add(Projections.property("p.nrIdentificacao"), "pessoa_nrIdentificacao");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "e")
			.setProjection(pl)
			.createAlias("e.pessoa", "p");

		if(StringUtils.isNotBlank(tpEmpresa)) {
			dc.add(Restrictions.eq("e.tpEmpresa", tpEmpresa));
		}

		if(StringUtils.isNotBlank(tpSituacao)) {
			dc.add(Restrictions.eq("e.tpSituacao", tpSituacao));
		}
		dc.addOrder(Order.asc("p.nmPessoa"));
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
	}

	/**
	 * 
	 * @param criteria
	 * @param findDef
	 * @return ResultSetPage
	 */
	public ResultSetPage findEmpresasByUsuario(Map criteria, FindDefinition findDef) {
		StringBuffer sb = new StringBuffer()
		.append(" select new " + Empresa.class.getName() + 
				"(e.idEmpresa, e.tpEmpresa, p.nmPessoa, p.nmFantasia, p.tpIdentificacao, p.nrIdentificacao, e.tpSituacao, e.sgEmpresa)")
		.append(" from " + Empresa.class.getName() + " as e")
		.append(" inner join fetch e.pessoa as p")
		.append(" where e.usuariosEmpresa.usuario.idUsuario = :idUsuario");

		return getAdsmHibernateTemplate().findPaginated(sb.toString(), findDef.getCurrentPage(), findDef.getPageSize(), criteria );
	}
	
	/**
	 * 
	 * @param usuario
	 * @return List
	 */
	public List<Empresa> findEmpresasByUsuario(Usuario usuario) {
		StringBuffer sb = new StringBuffer()
		.append(" select new " + Empresa.class.getName() + 
				"(e.idEmpresa, e.tpEmpresa, p.nmPessoa, p.nmFantasia, p.tpIdentificacao, p.nrIdentificacao, e.tpSituacao, e.sgEmpresa)")
		.append(" from " + Empresa.class.getName() + " as e")
		.append(" join e.pessoa as p")
		.append(" join e.usuariosEmpresa ue ")
		.append(" join ue.usuario u ")
		.append(" where u.idUsuario = ?");
		return getAdsmHibernateTemplate().find(sb.toString(), usuario.getIdUsuario());
	}

	public List<Empresa> findEmpresaByUsuario(Usuario usuario, String nrIdentificacao) {

		StringBuffer sb = new StringBuffer()
		.append(" select new " + Empresa.class.getName() + 
				"(e.idEmpresa, e.tpEmpresa, p.nmPessoa, p.nmFantasia, p.tpIdentificacao, p.nrIdentificacao, e.tpSituacao, e.sgEmpresa)")
		.append(" from " + Empresa.class.getName() + " as e")
		.append(" inner join fetch e.pessoa as p")
		.append(" where e.usuariosEmpresa.usuario.idUsuario = ?")
		.append(" AND p.nrIdentificacao = ?");
		String[] restricao = {usuario.getIdUsuario().toString(), nrIdentificacao};

		return getAdsmHibernateTemplate().find(sb.toString(), restricao );
	}


	public Empresa findEmpresaLogadoById(Long id) {
		
		StringBuffer sb = new StringBuffer()
		.append(" select e")
		.append(" from " + getPersistentClass().getName() + " as e")
		.append(" join fetch e.pessoa ")
		.append(" where e.idEmpresa = ?");
		
		return (Empresa) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[] { id });
	}
	
	public Map findDadosEmpresaById(Long id) {
		
		StringBuffer sb = new StringBuffer()
		.append(" select new Map (e.idEmpresa as idEmpresa, p.nmPessoa as nmPessoa, p.tpIdentificacao as tpIdentificacao, p.nrIdentificacao as nrIdentificacao)")
		.append(" from " + Empresa.class.getName() + " as e")
		.append(" inner join e.pessoa as p")
		.append(" where e.idEmpresa = ? ");
		
		return (Map) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[] { id });
	}
	
	public Empresa findEmpresaPadraoByUsuario(Usuario usuario) {
		StringBuffer sb = new StringBuffer()
		.append(" select u.empresaPadrao")
		.append(" from " + Usuario.class.getName() + " as u")
		.append(" join fetch u.empresaPadrao.pessoa ")
		.append(" where u.idUsuario = ?");

		return (Empresa) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[] { usuario.getIdUsuario() });
	}

	public List findByUsuarioLogado(TypedFlatMap m) {
		Usuario u = SessionUtils.getUsuarioLogado();

		StringBuffer sb = new StringBuffer()
		.append("select e.idEmpresa, e.pessoa.nmPessoa, e.pessoa.nrIdentificacao")
		.append(" from ")
		.append(Empresa.class.getName()).append(" as e")
		.append(" inner join e.usuariosEmpresa as ue")
		.append(" where ue.usuario.idUsuario = ?");

		return getAdsmHibernateTemplate().find(sb.toString(), u.getIdUsuario());
	}

	public List findEmpresaByUsuarioAutenticado(AutenticacaoDMN autenticacaoDMN) {
		StringBuffer sb = new StringBuffer()
		.append("select e.idEmpresa, e.pessoa.nmPessoa, e.pessoa.nrIdentificacao")
		.append(" from ")
		.append(Empresa.class.getName()).append(" as e")
		.append(" inner join e.usuariosEmpresa as ue")
		.append(" where ue.usuario.idUsuario = ?");

		return getAdsmHibernateTemplate().find(sb.toString(), autenticacaoDMN.getIdUsuario());
	}

	public Empresa findEmpresa(String nrIdentificacao) {
		StringBuilder hql = new StringBuilder();

		hql.append(" select e"); 
		hql.append(" from ").append(getPersistentClass().getName()).append(" as e");
		hql.append(" inner join fetch e.pessoa as p");
		hql.append(" where p.nrIdentificacao = ?");

		String[] paramValues = {nrIdentificacao};

		return (Empresa)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), paramValues);
	}
	
	/**
	 * Retorna 'true' se a pessoa informada é uma empresa ativa senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isEmpresa(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("count(em.id)");

		hql.addInnerJoin(Empresa.class.getName(), "em");

		hql.addCriteria("em.id", "=", idPessoa);
		hql.addCriteria("em.tpSituacao", "=", "A");

		List<Long> lstEmpresa = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		return (lstEmpresa.get(0).longValue() > 0);
	}

	/**
	 * Retorna o id da empresa da filial passada por parâmetro
	 * @param idFilial
	 * @return idEmpresa
	 */
	public Long findIdEmpresaByIdFilial(Long idFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(Filial.class, "f");
		dc.setProjection(Projections.property("f.empresa.id"));
		dc.add(Restrictions.eq("f.id", idFilial));

		return (Long)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT p.id_pessoa, ");
		sql.append("       p.nr_identificacao, ");
		sql.append("       p.nm_pessoa, ");
		sql.append("       p.nm_fantasia, ");
		sql.append("       m.nm_municipio, ");
		sql.append("       uf.sg_unidade_federativa ");
		
		sql.append("  FROM pessoa p ");
		sql.append("       inner join empresa e on e.id_empresa = p.id_pessoa ");
		sql.append("       left join endereco_pessoa ep on ep.id_endereco_pessoa = p.id_endereco_pessoa ");
		sql.append("       left join municipio m on m.id_municipio = ep.id_municipio ");
		sql.append("       left join unidade_federativa uf on uf.id_unidade_federativa = m.id_unidade_federativa ");
		
		sql.append(" where 1 = 1 ");
		
		if (filter.containsKey("nrIdentificacao")) {
			String operador = "=";
			if (filter.get("nrIdentificacao").toString().length() == 8) {
				operador = "like";
				filter.put("nrIdentificacao", filter.get("nrIdentificacao") + "%");
		}
			sql.append(" and ").append(" p.nr_identificacao " + operador + " :nrIdentificacao ");
		}
		
		if (filter.containsKey("nmPessoa")) {
			sql.append(" and lower(p.nm_pessoa) like lower(:nmPessoa) ");
			filter.put("nmPessoa", filter.get("nmPessoa") + "%");
		}
		
		if (filter.containsKey("tpEmpresa")) {
			sql.append(" and e.tp_empresa = :tpEmpresa ");
		}
		return new ResponseSuggest(sql.toString(), filter);
			}
	public boolean isEmpresaParceira(Long idProprietario){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("count(em.id)");

		hql.addInnerJoin(Empresa.class.getName(), "em");

		hql.addCriteria("em.id", "=", idProprietario);
		hql.addCriteria("em.tpEmpresa", "=", "P");

		List<Long> lstEmpresa = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		return (lstEmpresa.get(0).longValue() > 0);
	}

	public Empresa findEmpresaByProprietario(Proprietario proprietario) {
		String hql = "select e from Empresa e where e.id = :idEmpresa ";
		
		List<Empresa> empresas = getAdsmHibernateTemplate().findByNamedParam(hql, "idEmpresa", proprietario.getIdProprietario());
		if (empresas != null && empresas.size() >0){
			return empresas.get(0);
		}
		return null;
	}
	
}
