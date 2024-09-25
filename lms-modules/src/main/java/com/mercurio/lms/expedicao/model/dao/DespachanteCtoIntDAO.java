package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.DespachanteCtoInt;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplica��o atrav�s do suporte
 * ao Hibernate em conjunto com o Spring. N�o inserir documenta��o ap�s ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class DespachanteCtoIntDAO extends BaseCrudDao<DespachanteCtoInt, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
	protected final Class getPersistentClass() {
		return DespachanteCtoInt.class;
	}
	
	public List findByIdCtoInternacional(Long idCtoInternacional){

		ProjectionList pl = Projections.projectionList();

		pl.add(Projections.property("idDespachanteCtoInt"), "idDespachanteCtoInt")
		.add(Projections.property("dcid.idDespachante"), "despachante.idDespachante")
		;

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "dci")
		.setProjection(pl)
		.createAlias("dci.despachante", "dcid")
		.add(Restrictions.eq("dci.ctoInternacional.id", idCtoInternacional))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		;

		return findByDetachedCriteria(dc);
	}

}