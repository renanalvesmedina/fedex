package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.TramoFreteInternacional;
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
public class TramoFreteInternacionalDAO extends BaseCrudDao<TramoFreteInternacional, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return TramoFreteInternacional.class;
	} 

	public List findTramosCtoInternacional(Long idFilialOrigem
										   ,Long idFilialDestino
										   ,Long idClienteRemetente){

		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("dsTramoFreteInternacional"), "dsTramoFreteInternacional")
		.add(Projections.property("idTramoFreteInternacional"), "idTramoFreteInternacional")
		.add(Projections.property("pcFrete"), "pcFrete")
		.add(Projections.property("blCruze"), "blCruze")
		.add(Projections.property("blTramoOrigem"), "blTramoOrigem")
		.add(Projections.property("tfidfi.pcFreteOrigem"), "distrFreteInternacional.pcFreteOrigem")
		.add(Projections.property("tfidfi.pcFreteExterno"), "distrFreteInternacional.pcFreteExterno")
		.add(Projections.property("tfidfi.pcFreteDestino"), "distrFreteInternacional.pcFreteDestino")
		;

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tfi")
		.createAlias("tfi.distrFreteInternacional", "tfidfi")

		.add(Restrictions.or(Restrictions.eq("tfi.cliente.id", idClienteRemetente)
						    ,Restrictions.isNull("tfi.cliente.id")))
		.add(Restrictions.eq("tfidfi.filialOrigem.id", idFilialOrigem))
		.add(Restrictions.eq("tfidfi.filialDestino.id", idFilialDestino))

		.setProjection(pl)
		.addOrder(Order.asc("tfi.nrTramoFreteInternacional"))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()))
		;

		return findByDetachedCriteria(dc);
	}

}