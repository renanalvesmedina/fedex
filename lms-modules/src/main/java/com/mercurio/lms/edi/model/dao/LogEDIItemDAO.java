package com.mercurio.lms.edi.model.dao;


import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.LogEDIItem;




/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class LogEDIItemDAO extends BaseCrudDao<LogEDIItem, Long>{

	@Override
	public Class getPersistentClass() {		
		return LogEDIItem.class;
	}
		
	private StringBuilder getSqlFindPaginated(TypedFlatMap criteria, boolean find) {
		StringBuilder query = new StringBuilder();
		
		if(find){
			query.append(" SELECT logEdiDet ");
		}
		query.append(" FROM "+getPersistentClass().getName()+" as logEdiDet where 1=1 ");
			
		
		if(MapUtils.getObject(criteria, "idLogEdiDetalhe") != null) {
			query.append("  and logEdiDet.logEDIDetalhe.idLogEdiDetalhe = :idLogEdiDetalhe ");
		}	
		
		return query;
	} 
	@SuppressWarnings("unchecked")
	public ResultSetPage findPaginatedByIdLogDetalhe(TypedFlatMap criteria, FindDefinition findDef) {
		StringBuilder query = this.getSqlFindPaginated(criteria,true);		
		String sql = query.toString();
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql, findDef.getCurrentPage(), findDef.getPageSize(),criteria); 
		return rsp;
	}
	public Integer getRowCountByIdLogDetalhe(TypedFlatMap criteria) {
		StringBuilder query = this.getSqlFindPaginated(criteria,false);
		return getAdsmHibernateTemplate().getRowCountForQuery(query.toString(),criteria); 
	}
	
	public Long findSequence(){		
		return Long.valueOf(getSession().createSQLQuery("select LOG_ARQUIVO_EDI_DET_ITEM_SQ.nextval from dual").uniqueResult().toString());
	}	
	
}

