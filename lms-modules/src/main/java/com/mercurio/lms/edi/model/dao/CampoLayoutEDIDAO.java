package com.mercurio.lms.edi.model.dao;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.CampoLayoutEDI;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CampoLayoutEDIDAO extends BaseCrudDao<CampoLayoutEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return CampoLayoutEDI.class;
	}
		
	@SuppressWarnings("unchecked")
	public ResultSetPage<CampoLayoutEDI> findPaginated(PaginatedQuery paginatedQuery) {
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		
		StringBuilder hql = new StringBuilder();
		hql.append("from CampoLayoutEDI where 1=1 ");
		
		if(MapUtils.getString(criteria, "nomeCampo") != null){
			hql.append(" and nomeCampo like :nomeCampo ");
		}
		if(MapUtils.getString(criteria, "descricaoCampo") != null){
			hql.append(" and descricaoCampo like :descricaoCampo ");
		} 		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery,hql.toString());		
	}
	
}
