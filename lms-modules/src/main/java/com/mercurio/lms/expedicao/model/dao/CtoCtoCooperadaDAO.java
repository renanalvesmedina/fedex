package com.mercurio.lms.expedicao.model.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.CtoCtoCooperada;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CtoCtoCooperadaDAO extends BaseCrudDao<CtoCtoCooperada, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<CtoCtoCooperada> getPersistentClass() {
		return CtoCtoCooperada.class;
	}

	public Integer getRowCountByIdFilialNrCooperada(Long idFilial, Integer nrCtoCooperada) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cto")
			.setProjection(Projections.count("cto.id"))
			.add(Restrictions.eq("cto.nrCtoCooperada", nrCtoCooperada))
			.add(Restrictions.eq("cto.filialByIdFilial.id", idFilial));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public CtoCtoCooperada findByIdFilialNrCooperada(Long idFilial, Integer nrCtoCooperada) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ccc");

		dc.add(Restrictions.eq("ccc.filialByIdFilial.id", idFilial));
		dc.add(Restrictions.eq("ccc.nrCtoCooperada", nrCtoCooperada));

		return (CtoCtoCooperada)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * verifica se existe algum conhecimento da cooperada para o documento de servico 
	 * @param idDoctoServico
	 * @return
	 */
	public boolean findCoopByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("1");
		hql.addFrom(CtoCtoCooperada.class.getName()+ " ccc " +
				"left outer join ccc.conhecimento co");
		hql.addCriteria("co.idDoctoServico","=", idDoctoServico);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).size()>0;
	}

}