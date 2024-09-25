package com.mercurio.lms.configuracoes.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EmpresaUsuario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.municipios.model.Empresa;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class EmpresaUsuarioDAO extends BaseCrudDao<EmpresaUsuario, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return EmpresaUsuario.class;
	}

	public EmpresaUsuario findById(Long id) {

		if (id == null) {
			throw new IllegalArgumentException("id cannot be null");
		}
		
		final String hql = "from " + getPersistentClass().getName() + " as p " +
			"join fetch p.usuario as user " +
			"join fetch user.empresaPadrao as empresaPadrao " +
			"join fetch p.empresa as empresa " +
			"join fetch p.filialPadrao as filialPadrao " +
			"join fetch filialPadrao.pessoa as pessoaFilialPadrao " +
			"join fetch empresa.pessoa as pessoa " +
			"WHERE p.idEmpresaUsuario = ?";

		return (EmpresaUsuario) getAdsmHibernateTemplate().findUniqueResult(hql, new Object[] { id });
	}
	
	public EmpresaUsuario findByIdUsuarioUsingEmpresaPadrao(Long id) {
		final String hql = "from " + getPersistentClass().getName() + " as p " +
				"join fetch p.usuario as user " +
				"join fetch p.empresa as empresa " +
				"join fetch empresa.pessoa as pessoa " +
				"left join fetch user.empresaPadrao as empresaPadrao " +
			"WHERE user.idUsuario = ? " +
				"and user.empresaPadrao.id != null " +
				"and user.empresaPadrao.id = empresa.id";

		return  (EmpresaUsuario) getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{id});
	}

	public List findByIdUsuario(Long id) {
		final String hql = "from " + getPersistentClass().getName() + " as p " +
				"join fetch p.usuario as user " +
				"join fetch p.empresa as empresa " +
				"join fetch empresa.pessoa as pessoa " +
				"left join fetch p.regionalUsuario as ru " +
				"left join fetch ru.regional as r " +
				"left join fetch p.filiaisUsuario as fu " +
				"left join fetch fu.filial " +
			"WHERE user.idUsuario = ?";

		return getAdsmHibernateTemplate().find(hql, id);
	}

	/**
	 * Busca Empresa Padrão através de um UsuarioLMS.
	 * 
	 * @param id
	 * @return
	 */
	public Empresa findByIdUsuarioEmpresaPadrao(java.lang.Long id) {
		final String hql = "from " + getPersistentClass().getName() + " as p " +
				"join fetch p.usuario as user " +
				"join fetch p.empresa as empresa " +
				"join fetch empresa.pessoa as pessoa " +
				"left join fetch user.empresaPadrao as empresaPadrao " +
			"WHERE user.idUsuario = ? " +
				"and user.empresaPadrao.id != null " +
				"and user.empresaPadrao.id = empresa.id";

		EmpresaUsuario empUsuario = (EmpresaUsuario) getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{id});

		if (empUsuario != null) {
			Usuario usuario = empUsuario.getUsuario();
			if( usuario != null ){
				return usuario.getEmpresaPadrao();
			}
		}
		
		return null;
	}

	public boolean isIrrestritoFilial(Usuario usuario, Empresa empresa) {

		StringBuffer sb = new StringBuffer("select eu.blIrrestritoFilial");
		sb.append(" from " + getPersistentClass().getName() + " as eu ");
		sb.append(" where eu.usuario.idUsuario = ? and eu.empresa.idEmpresa = ?");
		
		Object resultado =getAdsmHibernateTemplate().findUniqueResult( sb.toString(),
				new Object[] { usuario.getIdUsuario(), empresa.getIdEmpresa() });
		if( resultado != null ){
			return ((Boolean)resultado).booleanValue();
		}
		return false;
	}

	public EmpresaUsuario findByEmpresaUsuario(Empresa empresa, Usuario usuario) {

		StringBuffer sb = new StringBuffer("select eu");
		sb.append(" from " + getPersistentClass().getName() + " as eu ");
		sb.append(" where eu.usuario.idUsuario = ? and eu.empresa.idEmpresa = ?");

		return (EmpresaUsuario) getAdsmHibernateTemplate().findUniqueResult(sb.toString(),
						new Object[] { usuario.getIdUsuario(), empresa.getIdEmpresa() });
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria,
			FindDefinition findDef) {

		SqlTemplate hql = montaHQLFindPaginatedUsuarioLMSEmpresa(criteria);

		hql.addProjection(" new Map("
				+ "empresa.tpEmpresa  as tpEmpresa, pessoa.tpIdentificacao as tpIdentificacao,		"
				+ "pessoa.nrIdentificacao as nrIdentificacao, pessoa.nmPessoa	as nmPessoa,   	    "
				+ "empresaUsuario.idEmpresaUsuario 		as idEmpresaUsuario, "
				+ "concat( filial.sgFilial, ' - ', pessoaFilial.nmFantasia ) as nmFilialPadrao " + ") ");

		hql.addOrderBy(" pessoa.nmPessoa asc ");

		return getAdsmHibernateTemplate().findPaginated(hql.getSql(true),
				findDef.getCurrentPage(), findDef.getPageSize(),
				hql.getCriteria());

	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate hql = montaHQLFindPaginatedUsuarioLMSEmpresa(criteria);
		hql.addProjection(" count(*) as quantidade ");
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		return result.intValue();
	}

	private SqlTemplate montaHQLFindPaginatedUsuarioLMSEmpresa(
			TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(EmpresaUsuario.class.getName() + " as empresaUsuario "
				+ " join empresaUsuario.empresa as empresa "
				+ " join empresa.pessoa 		  as pessoa  "
				+ " join empresaUsuario.usuario as usuario "
				+ " left join empresaUsuario.filialPadrao as filial "
				+ " left join filial.pessoa as pessoaFilial ");

		if (MapUtilsPlus.getLong(criteria, "idUsuarioLMS", null) != null)
			hql.addCriteria("usuario.idUsuario", "=", MapUtilsPlus.getLong(
					criteria, "idUsuarioLMS", null));

		if (MapUtilsPlus.getString(criteria,
				"empresaByIdEmpresaCadastrada.idEmpresa", null) != null)
			hql.addCriteria("empresa.idEmpresa", "=", MapUtilsPlus.getLong(
					criteria, "empresaByIdEmpresaCadastrada.idEmpresa", null));

		return hql;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("empresa", FetchMode.JOIN);
		lazyFindById.put("empresa.pessoa", FetchMode.JOIN);
		lazyFindById.put("usuario", FetchMode.JOIN);
		lazyFindById.put("usuario.empresaPadrao", FetchMode.JOIN);
		lazyFindById.put("usuario.empresa", FetchMode.JOIN);
		lazyFindById.put("filialPadrao", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	private List mountHqlFindPaginatedFilialRegional(final Long id) {

		List retorno = getAdsmHibernateTemplate().executeFind(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						StringBuilder sb = new StringBuilder();
						sb.append("select new Map(filUser.idFilialUsuario as idFilialRegional, concat(fil.sgFilial,' - ',pes.nmFantasia) as nmFilialRegional, filUser.blAprovaWorkflow as blAprovaWorkflow, 'F' as tipoReg) ");
						sb.append(" from " + getPersistentClass().getName()+ " as eu ");
						sb.append("   join eu.filiaisUsuario as filUser ");
						sb.append("   join filUser.filial as fil ");
						sb.append("   join fil.pessoa as pes ");
						sb.append(" where eu.idEmpresaUsuario = :id ");

						StringBuilder sb2 = new StringBuilder();
						sb2.append("select new Map(re.idRegionalUsuario as idFilialRegional, rel.sgRegional as nmFilialRegional, re.blAprovaWorkflow as blAprovaWorkflow, 'R' as tipoReg) ");
						sb2.append("from " + getPersistentClass().getName()	+ " as eu ");
						sb2.append("   join eu.regionalUsuario as re ");
						sb2.append("   join re.regional as rel ");
						sb2.append("where eu.idEmpresaUsuario = :id ");

						Query query = getSession().createQuery(sb.toString());
						query.setLong("id", id.intValue());

						List retorno = query.list();
						if (retorno == null)
							retorno = new ArrayList();

						query = getSession().createQuery(sb2.toString());
						query.setLong("id", id.intValue());
						retorno.addAll(query.list());

						return retorno;

					}

				});

		return (retorno == null) ? Collections.emptyList() : retorno;

	}

	public ResultSetPage findPaginatedFilialRegional(Long idEmpresaUsuario,
			FindDefinition findDef) {

		List listQuery = mountHqlFindPaginatedFilialRegional(idEmpresaUsuario);

		int sizeQuery = listQuery.size();

		int endPos = findDef.getPageSize().intValue()
				* findDef.getCurrentPage().intValue();

		int initPos = findDef.getPageSize().intValue()
				* (findDef.getCurrentPage().intValue() - 1);

		List subList = listQuery.subList(initPos,
				(endPos > sizeQuery) ? sizeQuery : endPos);

		return new ResultSetPage(findDef.getCurrentPage(), findDef
				.getCurrentPage().intValue() > 1, sizeQuery > endPos, subList);

	}

	public Integer getRowCountFilialRegional(Long idEmpresaUsuario) {

		return Integer.valueOf(
				mountHqlFindPaginatedFilialRegional(idEmpresaUsuario).size());

	}

}