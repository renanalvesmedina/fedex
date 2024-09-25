package com.mercurio.lms.seguros.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.seguros.model.TipoSeguro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoSeguroDAO extends BaseCrudDao<TipoSeguro, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoSeguro.class;
    }

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("seguroTipoSinistros", FetchMode.SELECT);
	}
	
	/**
	 * Método que valida se as combos de modal e abrangência estão preenchidas, para preencher a combo de tipo de seguro 
	 * @param sgModal
	 * @param sgAbrangencia
	 * @return List com resultado da consulta
	 */
	public List findComboByTipoSeguro(String sgModal, String sgAbrangencia, String tpSituacao){
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( TS.sgTipo as sgTipo, ")
    				.append(" TS.idTipoSeguro as idTipoSeguro, TS.tpSituacao as tpSituacao )")
    				.toString()
    		);
    	
    	sql.addFrom(TipoSeguro.class.getName(), "TS");
    	
    	if(StringUtils.isNotBlank(sgModal)) {
    		sql.addCriteria("TS.tpModal", "=", sgModal);
    	}
    	
    	if(StringUtils.isNotBlank(sgAbrangencia)) {
    		sql.addCriteria("TS.tpAbrangencia", "=", sgAbrangencia);
    	}
        
        if(tpSituacao != null && StringUtils.isNotBlank(tpSituacao)){
            sql.addCriteria("TS.tpSituacao", "=", tpSituacao);
        }
    	
    	sql.addOrderBy("TS.sgTipo", "ASC");
    	
    	List list = getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
		
    	return list;
    }
	
}