package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.CptTipoValor;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CptTipoValorDAO extends BaseCrudDao<CptTipoValor, Long> {

	protected final Class getPersistentClass() {
		return CptTipoValor.class;
	}

	
	public List findListTipoValor() {
    	
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("cpt.idCptTipoValor"), "idCptTipoValor")
			.add(Projections.property("cpt.dsTipoValor"), "dsTipoValor");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cpt")
			.setProjection(pl)
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP).addOrder(Order.asc("cpt.dsTipoValor"));
	
		return super.findByDetachedCriteria(dc);		
	}
	
	

}