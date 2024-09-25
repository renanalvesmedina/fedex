package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.FaturaAnexo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FaturaAnexoDAO extends BaseCrudDao<FaturaAnexo, Long>{

	@Override
	protected Class getPersistentClass() {		
		return FaturaAnexo.class;
	}
	public List<FaturaAnexo> findAllFaturaAnexoById(Long idFatura){
		SqlTemplate hql = new SqlTemplate(); 
		hql.addFrom(getPersistentClass().getName(), "faturaAnexo");
		hql.addCriteria("faturaAnexo.fatura.id", "=", idFatura);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()); 
	}
	
	public List<FaturaAnexo> findFaturaAnexosByIdFatura(Long idFatura) {
		SqlTemplate hql = getSqlTemplateFilterFatura(idFatura);
   		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}	

	private SqlTemplate getSqlTemplateFilterFatura(Long idFatura) {
		SqlTemplate hql = new SqlTemplate();		
   		hql.addInnerJoin(getPersistentClass().getName() , "faturaAnexo");
   		hql.addInnerJoin("fetch faturaAnexo.usuario","usuario");
   		
   		hql.addCriteria("faturaAnexo.fatura.id", "=", idFatura);
		return hql;
	}		
}