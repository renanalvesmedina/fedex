package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.vendas.model.HistoricoAnaliseCredito;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class HistoricoAnaliseCreditoDAO extends BaseCrudDao<HistoricoAnaliseCredito, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return HistoricoAnaliseCredito.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("analiseCreditoCliente", FetchMode.JOIN);
		lazyFindById.put("usuario", FetchMode.JOIN);
	}

	public HistoricoAnaliseCredito findByIdCliente(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "hac");
		dc.createAlias("hac.analiseCreditoCliente", "acc");
		dc.createAlias("acc.cliente", "c");
		dc.add(Restrictions.eq("c.id", idCliente));
		return (HistoricoAnaliseCredito)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public List<HistoricoAnaliseCredito> findByIdAnaliseCreditoCliente(Long idAnaliseCreditoCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "hac");
		dc.createAlias("hac.analiseCreditoCliente", "acc");
		dc.add(Restrictions.eq("acc.id", idAnaliseCreditoCliente));
		dc.addOrder(Order.asc("acc.dhSolicitacao"));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition def) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("hac.id"), "idHistoricoAnaliseCredito")
			.add(Projections.property("hac.dhEvento.value"), "dhEvento")
			.add(Projections.property("hac.tpEvento"), "tpEvento")
			.add(Projections.property("hac.obEvento"), "obEvento")
			.add(Projections.property("u.nmUsuario"), "usuario_nmUsuario");

		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(pl);
		dc.addOrder(Order.desc("dhEvento"));
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	private DetachedCriteria createCriteriaPaginated(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "hac")
			.createAlias("hac.analiseCreditoCliente", "ac")
			.createAlias("hac.usuario", "u");

		Long idAnaliseCreditoCliente = criteria.getLong("analiseCreditoCliente.idAnaliseCreditoCliente");
		if (idAnaliseCreditoCliente != null) {
			dc.add(Restrictions.eq("ac.id", idAnaliseCreditoCliente));
		}
		return dc;
	}
}