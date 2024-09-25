package com.mercurio.lms.seguros.model.dao;

import java.util.List;

import org.hibernate.Query;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.seguros.model.ApoliceSeguroAnexo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ApoliceSeguroAnexoDAO  extends BaseCrudDao<ApoliceSeguroAnexo, Long>{

	@Override
	protected Class getPersistentClass() {
		return ApoliceSeguroAnexo.class;
	}
	
	public List<ApoliceSeguroAnexo> findAnexosByIdApoliceSeguro(Long idApoliceSeguro) {
		SqlTemplate hql = new SqlTemplate();		
   		hql.addInnerJoin(getPersistentClass().getName() , "apoliceSeguroAnexo");
   		hql.addInnerJoin("fetch apoliceSeguroAnexo.usuario","usuario");
   		hql.addInnerJoin("fetch apoliceSeguroAnexo.apoliceSeguro","apoliceSeguro");
   		
   		hql.addCriteria("apoliceSeguroAnexo.apoliceSeguro.idApoliceSeguro", "=", idApoliceSeguro);
   		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public Integer getRowCountAnexosByIdApoliceSeguro(Long idApoliceSeguro){
		SqlTemplate hql = new SqlTemplate();		
   		hql.addInnerJoin(getPersistentClass().getName() , "apoliceSeguroAnexo");
   		hql.addInnerJoin("apoliceSeguroAnexo.usuario","usuario");
   		hql.addInnerJoin("apoliceSeguroAnexo.apoliceSeguro","apoliceSeguro");
   		
   		hql.addCustomCriteria("apoliceSeguroAnexo.apoliceSeguro.idApoliceSeguro = ? ");
		
		Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("select count(*) as rowCount " + 
				hql.toString());
		q.setParameter(0, idApoliceSeguro);
		return Integer.valueOf( ((Long)q.uniqueResult()).intValue() );
	}
	
}
