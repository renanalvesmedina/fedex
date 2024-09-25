package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.SerasaCliente;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class SerasaClienteDAO extends BaseCrudDao<SerasaCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return SerasaCliente.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("historicoAnaliseCredito", FetchMode.JOIN);
	}

	public SerasaCliente findByIdHistoricoAnaliseCredito(Long idHistoricoAnaliseCredito) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "sc");
		dc.createAlias("sc.historicoAnaliseCredito", "hac");
		dc.add(Restrictions.eq("hac.id", idHistoricoAnaliseCredito));
		return (SerasaCliente)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public SerasaCliente findByIdAnaliseCreditoCliente(Long idAnaliseCreditoCliente) {
		//Busca ultimo historico relacionado a Analise de Credito
		DetachedCriteria dcMax = DetachedCriteria.forClass(getPersistentClass(), "scm");
		dcMax.setProjection(Projections.max("hacm.dhEvento.value"));
		dcMax.createAlias("scm.historicoAnaliseCredito", "hacm");
		dcMax.createAlias("hacm.analiseCreditoCliente", "accm");
		dcMax.add(Restrictions.eqProperty("accm.id", "acc.id"));
		dcMax.add(Restrictions.eq("acc.id", idAnaliseCreditoCliente));

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "sc");
		dc.createAlias("sc.historicoAnaliseCredito", "hac");
		dc.createAlias("hac.analiseCreditoCliente", "acc");
		dc.add(Subqueries.propertyEq("hac.dhEvento.value", dcMax));
		return (SerasaCliente)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
}