package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class ProdutoEspecificoDAO extends BaseCrudDao<ProdutoEspecifico, Long>
{

    @Override
	public List findListByCriteria(Map criterions) {
	   	if (criterions == null) criterions = new HashMap(1);
   		List order = new ArrayList(1);
   		order.add("nrTarifaEspecifica");
       return super.findListByCriteria(criterions, order);
   }


	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    @Override
	protected final Class getPersistentClass() {
        return ProdutoEspecifico.class;
    }

	public List findAllAtivo() {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pe")
			.add(Restrictions.eq("pe.tpSituacao", "A"))
			.setProjection(Projections
						.projectionList()
							.add(Projections.property("pe.nrTarifaEspecifica"), "nrTarifaEspecifica")
							.add(Projections.property("pe.idProdutoEspecifico"), "idProdutoEspecifico")
							.add(Projections.property("pe.dsProdutoEspecifico"), "dsProdutoEspecifico"))
			.addOrder(Order.asc("pe.nrTarifaEspecifica"))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}

	public ProdutoEspecifico findByNrTarifa(Short nrTarifa) {
		String queryString = "from ProdutoEspecifico pe where pe.nrTarifaEspecifica = ?";
		return (ProdutoEspecifico)getAdsmHibernateTemplate().findUniqueResult(queryString, new Object[]{nrTarifa});
	}




}