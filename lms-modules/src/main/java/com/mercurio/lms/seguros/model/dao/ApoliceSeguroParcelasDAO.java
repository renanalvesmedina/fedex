package com.mercurio.lms.seguros.model.dao;

import java.util.List;

import org.hibernate.Query;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.seguros.model.ApoliceSeguroParcela;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ApoliceSeguroParcelasDAO extends BaseCrudDao<ApoliceSeguroParcela, Long>{

	@Override
	protected Class getPersistentClass() {
		return ApoliceSeguroParcela.class;
	}

	public List findParcelasByIdApoliceSeguro(Long idApoliceSeguro) {
		SqlTemplate hql = new SqlTemplate();		
   		hql.addInnerJoin(getPersistentClass().getName() , "apoliceSeguroParcela");
   		hql.addInnerJoin("fetch apoliceSeguroParcela.apoliceSeguro","apoliceSeguro");
   		
   		hql.addCriteria("apoliceSeguroParcela.apoliceSeguro.id", "=", idApoliceSeguro);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public Integer getRowCountParcelasByIdApoliceSeguro(Long idApoliceSeguro){
		SqlTemplate hql = new SqlTemplate();	
		hql.addInnerJoin(getPersistentClass().getName() , "apoliceSeguroParcela");
   		hql.addInnerJoin("apoliceSeguroParcela.apoliceSeguro","apoliceSeguro");
   		
   		hql.addCustomCriteria("apoliceSeguroParcela.apoliceSeguro.id = ?");
   		
		Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("select count(*) as rowCount " + 
					hql.toString());
		q.setParameter(0, idApoliceSeguro);
    	return Integer.valueOf( ((Long)q.uniqueResult()).intValue() );
	}
	
}
