package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.TrechoCtoInt;
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
public class TrechoCtoIntDAO extends BaseCrudDao<TrechoCtoInt, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return TrechoCtoInt.class;
	}
	
	public List findByIdCtoInternacional(Long idCtoInternacional){

		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("idTrechoCtoInt"), "idTrechoCtoInt")
		.add(Projections.property("vlFreteRemetente"), "vlFreteRemetente")
		.add(Projections.property("vlFreteDestinatario"), "vlFreteDestinatario")
		.add(Projections.property("ctitfi.dsTramoFreteInternacional"), "tramoFreteInternacional.dsTramoFreteInternacional")
		.add(Projections.property("ctitfi.blCruze"), "tramoFreteInternacional.blCruze")
		;

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tci")
		.createAlias("tci.tramoFreteInternacional", "ctitfi")
		.add(Restrictions.eq("tci.ctoInternacional.id", idCtoInternacional))
		.setProjection(pl)
		.addOrder(Order.asc("ctitfi.nrTramoFreteInternacional"))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()))
		;

		return findByDetachedCriteria(dc);
	}

	public void removeByIdCtoInternacional(Long id, Boolean isFlushSession){
		StringBuilder hql = new StringBuilder()
		.append("delete	").append(getPersistentClass().getName()).append("\n")
		.append("where	ctoInternacional = :id")
		;
		CtoInternacional crt = new CtoInternacional();
		crt.setIdDoctoServico(id);

		getAdsmHibernateTemplate().removeById(hql.toString(), crt);

		if(isFlushSession.booleanValue()) getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}
}