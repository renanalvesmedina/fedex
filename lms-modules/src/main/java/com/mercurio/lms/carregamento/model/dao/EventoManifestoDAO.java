package com.mercurio.lms.carregamento.model.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.EventoManifesto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoManifestoDAO extends BaseCrudDao<EventoManifesto, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return EventoManifesto.class;
	}

	/**
	 * Método que deleta todos os Eventos de Manifesto pelo ID do Manifesto
	 * @param idManifesto
	 */
	public void removeByIdManifesto(Long idManifesto) {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from ").append(EventoManifesto.class.getName()).append(" as em ")
			.append(" where ")
			.append( "em.manifesto.id = :id");

		getAdsmHibernateTemplate().removeById(sql.toString(), idManifesto);
	}

	public List findEventoManifesto(Long idManifesto, String tpEventoManifesto) {
		DetachedCriteria dc = DetachedCriteria.forClass(EventoManifesto.class, "em")
		.add(Restrictions.eq("em.manifesto.id", idManifesto));

		if(StringUtils.isNotBlank(tpEventoManifesto)) {
			dc.add(Restrictions.eq("em.tpEventoManifesto", tpEventoManifesto));
		}
		return super.findByDetachedCriteria(dc);
	}

	/**
	 * Busca um EventoManifesto a partir do idManifesto, idFilial e tpEventoManifesto.
	 * Caso encontrar mais de um registro, uma NonUniqueResultException é disparada. 
	 * @param idManifesto
	 * @param idFilial
	 * @param tpEventoManifesto
	 * @return
	 */
	public EventoManifesto findEventoManifestoByIdManifestoIdFilialTpEventoManifesto(Long idManifesto, Long idFilial, String tpEventoManifesto) {
		DetachedCriteria dc = DetachedCriteria.forClass(EventoManifesto.class, "em");
		dc.add(Restrictions.eq("em.manifesto.id", idManifesto));
		dc.add(Restrictions.eq("em.filial.id", idFilial));
		dc.add(Restrictions.eq("em.tpEventoManifesto", tpEventoManifesto));
		return (EventoManifesto)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public EventoManifesto findUltimoEventoManifesto(Long idManifesto) {
		DetachedCriteria dc = DetachedCriteria.forClass(EventoManifesto.class, "em")
		.add(Restrictions.eq("em.manifesto.id", idManifesto))
		.setProjection(Projections.max("em.dhEvento.value"));
		return (EventoManifesto)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}