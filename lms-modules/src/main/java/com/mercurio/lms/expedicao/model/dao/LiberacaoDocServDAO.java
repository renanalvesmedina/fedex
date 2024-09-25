package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.LiberacaoDocServ;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LiberacaoDocServDAO extends BaseCrudDao<LiberacaoDocServ, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return LiberacaoDocServ.class;
    }

	public List findIdsByIdDoctoServico(Long idDoctoServico) {
		String sql = "select pojo.idLiberacaoDocServ " +
			"from "+ LiberacaoDocServ.class.getName() + " as  pojo " +
			"join pojo.doctoServico as ds " +
			"where ds.idDoctoServico = :idDoctoServico ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idDoctoServico", idDoctoServico);
	}

	public List getLiberacaoDocServByIdCotacao(Long idCotacao) {
		DetachedCriteria dc =  DetachedCriteria.forClass(LiberacaoDocServ.class)
			.add(Restrictions.eq("cotacao.id", idCotacao));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List getLiberacaoDocServByIdDocServ(Long idDocServ) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(LiberacaoDocServ.class)
			.add(Restrictions.eq("doctoServico.id", idDocServ))
			.addOrder(Order.asc("id"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
}