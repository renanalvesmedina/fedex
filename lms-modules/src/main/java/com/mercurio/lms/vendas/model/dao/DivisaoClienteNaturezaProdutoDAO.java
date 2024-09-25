package com.mercurio.lms.vendas.model.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.DivisaoClienteNaturezaProduto;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DivisaoClienteNaturezaProdutoDAO extends BaseCrudDao<DivisaoClienteNaturezaProduto, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DivisaoClienteNaturezaProduto.class;
    }

    public DivisaoClienteNaturezaProduto findById(Long idDivisaoClienteNaturezaProduto) {
		ProjectionList projection = Projections.projectionList()
			.add(Projections.property("idDivisaoClienteNaturezaProduto"), "idDivisaoClienteNaturezaProduto")
			.add(Projections.property("dsNaturezaProdutoCliente"), "dsNaturezaProdutoCliente")
			.add(Projections.property("dc.idDivisaoCliente"), "divisaoCliente.idDivisaoCliente")
			.add(Projections.property("np.idNaturezaProduto"), "naturezaProduto.idNaturezaProduto");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass())
			.setProjection(projection)
			.createAlias("divisaoCliente", "dc")
			.createAlias("naturezaProduto", "np")
			.add(Restrictions.idEq(idDivisaoClienteNaturezaProduto))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));

		return (DivisaoClienteNaturezaProduto)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

    public Integer getRowCountNaturezaProduto(TypedFlatMap criteria) {
		DetachedCriteria dc = this.createQueryNaturezaProduto(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
    public ResultSetPage findPaginatedNaturezaProduto(TypedFlatMap criteria) {
    	DetachedCriteria dc = this.createQueryNaturezaProduto(criteria);
    	dc.addOrder(Order.asc("dsNaturezaProdutoCliente"));

    	FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize()); 
	}
	private DetachedCriteria createQueryNaturezaProduto(TypedFlatMap criteria) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("divisaoCliente", "dc");
		dc.createAlias("naturezaProduto", "np");

		Long idDivisaoCliente = criteria.getLong("divisaoCliente.idDivisaoCliente");
		if(idDivisaoCliente != null) {
			dc.add(Restrictions.eq("dc.idDivisaoCliente", idDivisaoCliente));
		}
		Long idNaturezaProduto = criteria.getLong("naturezaProduto.idNaturezaProduto");
		if(idNaturezaProduto != null) {
			dc.add(Restrictions.eq("np.idNaturezaProduto", idNaturezaProduto));
		}
		String dsNaturezaProdutoCliente = criteria.getString("dsNaturezaProdutoCliente");
		if(StringUtils.isNotBlank(dsNaturezaProdutoCliente)) {
			dc.add(Restrictions.like("dsNaturezaProdutoCliente", dsNaturezaProdutoCliente));
		}
		return dc;
	} 
}