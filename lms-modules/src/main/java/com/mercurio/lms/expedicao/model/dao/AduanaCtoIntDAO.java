package com.mercurio.lms.expedicao.model.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.AduanaCtoInt;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class AduanaCtoIntDAO extends BaseCrudDao<AduanaCtoInt, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return AduanaCtoInt.class;
	}
	
	public java.util.List findByIdCtoInternacional(java.lang.Long idCtoInternacional){
		ProjectionList pl = Projections.projectionList();

		pl.add(Projections.property("idAduanaCtoInt"), "idAduanaCtoInt")
		.add(Projections.property("acipp.idPontoParada"), "pontoParada.idPontoParada")
		.add(Projections.property("acipp.nmPontoParada"), "pontoParada.nmPontoParada")
		;

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "aci")
		.setProjection(pl)
		.createAlias("aci.pontoParada", "acipp")
		.createAlias("aci.ctoInternacional", "acici")
		.add(Restrictions.eq("acici.id", idCtoInternacional))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		;

		return findByDetachedCriteria(dc);
	}
	
	public void removeByIdCtoInternacional(Long id, Boolean isFlushSession){
		StringBuilder hql = new StringBuilder()
		.append("delete	").append(getPersistentClass().getName()).append("\n")
		.append("where	ctoInternacional = :id")
		;

		CtoInternacional ctoInternacional = new CtoInternacional();
		ctoInternacional.setIdDoctoServico(id);

		getAdsmHibernateTemplate().removeById(hql.toString(), ctoInternacional);

		if(isFlushSession.booleanValue())
			getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}

}