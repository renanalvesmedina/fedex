package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.Safx2009;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class Safx2009DAO extends BaseCrudDao<com.mercurio.lms.expedicao.model.Safx2009, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Safx2009.class;
	}
	
	public List findDescricaoByCodObservacao(String codObservacao){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("descricao");		
		hql.addFrom(Safx2009.class.getName() + " saf ");
		hql.addCriteria("saf.codObservacao","like",codObservacao+"%");
		return (List) getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
}