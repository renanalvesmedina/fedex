package com.mercurio.lms.edi.model.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.LogEDIDetalhe;
import com.mercurio.lms.edi.model.LogEDIVolume;




/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class LogEDIVolumeDAO extends BaseCrudDao<LogEDIVolume, Long>{

	@Override
	public Class getPersistentClass() {		
		return LogEDIVolume.class;
	}
		
	private StringBuilder getSqlFindPaginated(TypedFlatMap criteria, boolean find) {
		StringBuilder query = new StringBuilder();
		
		if(find){
			query.append(" SELECT logEdiDet ");
		}
		query.append(" FROM "+getPersistentClass().getName()+" as logEdiDet where 1=1 ");
			
		
		if(MapUtils.getObject(criteria, "idLogEdiDetalhe") != null) {
			query.append("  and logEdiDet.logEDIDetalhe.idLogEdiDetalhe =:idLogEdiDetalhe ");
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
		return Long.valueOf(getSession().createSQLQuery("select LOG_ARQUIVO_EDI_DET_VOL_SQ.nextval from dual").uniqueResult().toString());
	}

	public List<LogEDIVolume> findByLogEDIDetalhe(LogEDIDetalhe logEDIDetalhe) {
		String hql = "from LogEDIVolume l where l.logEDIDetalhe = :logEDIDetalhe";
		return getAdsmHibernateTemplate().findByNamedParam(hql, "logEDIDetalhe", logEDIDetalhe);
	}	
	
	public LogEDIVolume findByCodigoVolume(LogEDIDetalhe logEDIDetalhe, String codigoVolume){
		String hql = "from LogEDIVolume l where l.logEDIDetalhe = :logEDIDetalhe and l.codigoVolume = :codigoVolume";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("logEDIDetalhe", logEDIDetalhe);
		param.put("codigoVolume", codigoVolume);
		return (LogEDIVolume) getAdsmHibernateTemplate().findUniqueResult(hql,param);
	}
	
}

