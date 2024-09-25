package com.mercurio.lms.edi.model.dao;


import java.util.List;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.LogEDIComplemento;
import com.mercurio.lms.edi.model.LogEDIDetalhe;




/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class LogEDIComplementoDAO extends BaseCrudDao<LogEDIComplemento, Long>{

	@Override
	public Class getPersistentClass() {		
		return LogEDIComplemento.class;
	}
	
	@SuppressWarnings("unchecked")
	public String findVlComplementoByNmComplmentoEChaveNfe(String nomeComplemento, String chaveNfe) {
		StringBuilder hql = new StringBuilder("select lec.valorComplemento from ");
		hql.append(getPersistentClass().getName()).append(" lec ");
		hql.append("inner join lec.logEDIDetalhe led ");
		hql.append("where ");
		hql.append("lec.nomeComplemento = ? ");
		hql.append("and led.chaveNfe = ? ");
		hql.append("order by lec.dtLog desc");
		
		List<String> valoresComplemento = getHibernateTemplate().find(hql.toString(), new Object[]{nomeComplemento, chaveNfe});
		if(valoresComplemento != null && !valoresComplemento.isEmpty()){
			return valoresComplemento.get(0);
		} else {
			return null;
		}
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
		return Long.valueOf(getSession().createSQLQuery("select LOG_ARQUIVO_EDI_DET_COMPL_SQ.nextval from dual").uniqueResult().toString());
	}

	public List<LogEDIComplemento> findByLogEDIDetalhe(LogEDIDetalhe logEDIDetalhe) {
		String hql = "from LogEDIComplemento l where l.logEDIDetalhe = :logEDIDetalhe";
		return getAdsmHibernateTemplate().findByNamedParam(hql, "logEDIDetalhe", logEDIDetalhe);
		
	}
}

