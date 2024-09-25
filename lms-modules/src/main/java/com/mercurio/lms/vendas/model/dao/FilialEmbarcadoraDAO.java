package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToNestedBeanResultTransformer;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.FilialEmbarcadora;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FilialEmbarcadoraDAO extends BaseCrudDao<FilialEmbarcadora, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return FilialEmbarcadora.class;
	}

	private ProjectionList createProjectionList() {
		return Projections.projectionList()
			.add(Projections.property("idFilialEmbarcadora"), "idFilialEmbarcadora")
			.add(Projections.property("c.idCliente"), "cliente.idCliente")
			.add(Projections.property("cp.nrIdentificacao"), "cliente.pessoa.nrIdentificacao")
			.add(Projections.property("cp.tpIdentificacao"), "cliente.pessoa.tpIdentificacao")
			.add(Projections.property("cp.nmPessoa"), "cliente.pessoa.nmPessoa")
			.add(Projections.property("f.idFilial"), "filial.idFilial")
			.add(Projections.property("f.sgFilial"), "filial.sgFilial")
			.add(Projections.property("fp.nmFantasia"), "filial.pessoa.nmFantasia");
	}

	public FilialEmbarcadora findById(Long id) {
		DetachedCriteria dc = createDetachedCriteria()
			.setProjection(createProjectionList())
			.createAlias("cliente", "c")
			.createAlias("c.pessoa", "cp")
			.createAlias("filial", "f")
			.createAlias("f.pessoa", "fp")
			.add(Restrictions.eq("id", id));
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		return (FilialEmbarcadora) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	private DetachedCriteria createPagidated(TypedFlatMap criteria) {
		DetachedCriteria dc = createDetachedCriteria()
			.createAlias("cliente", "c")
			.createAlias("c.pessoa", "cp")
			.createAlias("filial", "f")
			.createAlias("f.pessoa", "fp")
			.addOrder(Order.asc("cp.nmPessoa"))
			.addOrder(Order.asc("fp.nmFantasia"));
		/** Restrições */
		Long idCliente = criteria.getLong("cliente.idCliente");
		if(idCliente != null) {
			dc.add(Restrictions.eq("c.id", idCliente));
		}
		Long idFilial = criteria.getLong("filial.idFilial");
		if(idFilial != null) {
			dc.add(Restrictions.eq("f.id", idFilial));
		}
		return dc;
	}

	public List findComboFiliais(Long idCliente){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("fe");
		
		sql.addInnerJoin(FilialEmbarcadora.class.getName(),"fe");
		sql.addInnerJoin("fetch fe.filial", "f");
		sql.addInnerJoin("fe.cliente","c");
		
		sql.addCriteria("c.idCliente", "=", idCliente);
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		DetachedCriteria dc = createPagidated(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		DetachedCriteria dc = createPagidated(criteria);
		dc.setProjection(createProjectionList());

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}

	/**
	 * Remove todas as entidade através do id do Cliente.
	 * @param idCliente
	 */
	public void removeByCliente(Long idCliente) {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from ").append(getPersistentClass().getName()).append(" as fe ");
		sql.append(" where fe.cliente.id = ?");

    	List<Long> paramValues = new ArrayList<Long>(1);
    	paramValues.add(idCliente);
		super.executeHql(sql.toString(), paramValues);
	}
}