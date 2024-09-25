package com.mercurio.lms.vendas.model.dao;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.ObservacaoConhecimento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ObservacaoConhecimentoDAO extends BaseCrudDao<ObservacaoConhecimento, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ObservacaoConhecimento.class;
    }

	public ResultSetPage findPaginated(TypedFlatMap map,FindDefinition fd) {
		SqlTemplate sql = getFrom(map);

		StringBuilder hql = new StringBuilder();
		hql.append("		new Map( \n");
		hql.append("      	obs.idObservacaoConhecimento as idObservacaoConhecimento, ");
		hql.append("      	obs.dsObservacaoConhecimento as dsObservacaoConhecimento, ");
		hql.append("      	obs.tpSituacao as tpSituacao ");		
		hql.append(")");
		
		sql.addProjection(hql.toString());
		
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), fd.getCurrentPage(), fd.getPageSize(), sql.getCriteria());
	}    

	public Integer getRowCount(TypedFlatMap map) {
		
		SqlTemplate sql = getFrom(map);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

    private SqlTemplate getFrom(TypedFlatMap map){ 

    	SqlTemplate sql = new SqlTemplate();
    	StringBuilder hql = new StringBuilder();
		
		hql.append(getPersistentClass().getName()).append(" as obs \n");
		
		sql.addFrom(hql.toString());
		
		
    	String dsObservacaoConhecimento = map.getString("dsObservacaoConhecimento");
    	if (StringUtils.isNotEmpty(dsObservacaoConhecimento)) {
    		sql.addCriteria("lower(obs.dsObservacaoConhecimento)","like",dsObservacaoConhecimento.toLowerCase()); 
    	}

		String tpSituacao = map.getString("tpSituacao");
		if (StringUtils.isNotEmpty(tpSituacao)) {
			sql.addCriteria("obs.tpSituacao","=",tpSituacao); 
		}

		sql.addOrderBy("obs.dsObservacaoConhecimento");

		return sql;
    }

    
}