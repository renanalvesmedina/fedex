package com.mercurio.lms.edi.model.dao;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.DeParaEDI;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DeParaEDIDAO extends BaseCrudDao<DeParaEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return DeParaEDI.class;
	}

	public ResultSetPage<DeParaEDI> findPaginated(PaginatedQuery paginatedQuery) {
		
		StringBuilder query = new StringBuilder()
		.append(" from DeParaEDI as dep ")
		.append(" where 1=1 ");
	
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "nmDeParaEDI") != null) {
			query.append("  and dep.nmDeParaEDI like :nmDeParaEDI ");
		}
		
		query.append("order by dep.nmDeParaEDI ");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}	
}
