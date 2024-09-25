package com.mercurio.lms.expedicao.model.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.EqualsVarcharI18n;
import com.mercurio.lms.expedicao.model.TipoCusto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoCustoDAO extends BaseCrudDao<TipoCusto, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<TipoCusto> getPersistentClass() {
		return TipoCusto.class;
	}

	public TipoCusto findByDsTipoCusto(String dsTipoCusto) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tc")
			.setProjection(Projections.alias(Projections.property("tc.idTipoCusto"), "idTipoCusto"))
			.add(EqualsVarcharI18n.eq("tc.dsTipoCusto", dsTipoCusto, LocaleContextHolder.getLocale()))
			.setResultTransformer(new AliasToBeanResultTransformer(getPersistentClass()));

		return (TipoCusto) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}