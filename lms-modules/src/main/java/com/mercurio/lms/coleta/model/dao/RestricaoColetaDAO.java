package com.mercurio.lms.coleta.model.dao;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.RestricaoColeta;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RestricaoColetaDAO extends BaseCrudDao<RestricaoColeta, Long>
{
	private Logger log = LogManager.getLogger(this.getClass());

    protected void initFindByIdLazyProperties(Map map) 
    {
    	map.put("pais", FetchMode.JOIN);
    	map.put("produtoProibido", FetchMode.JOIN);
    	map.put("servico", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map map) 
    {
    	map.put("pais", FetchMode.JOIN);
    	map.put("produtoProibido", FetchMode.JOIN);
    	map.put("servico", FetchMode.JOIN);
    }
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RestricaoColeta.class;
    }
    
    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
    	Object[] obj = createHqlPaginated(criteria);
    	return getAdsmHibernateTemplate().findPaginated((String)obj[0],findDef.getCurrentPage(),findDef.getPageSize(),(Object[])obj[1]);
    }
    
	public Integer getRowCount(TypedFlatMap criteria) {
		Object[] obj = createHqlPaginated(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery((String)obj[0],(Object[])obj[1]);
	}


    private Object[] createHqlPaginated(TypedFlatMap criteria) {
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(RestricaoColeta.class.getName() + " as RC " +
    					"left join fetch RC.servico as SE " + 
    					"left join fetch RC.pais as PA " +
    					"left join fetch RC.produtoProibido as PP ");
    	try {
	    	sql.addCriteria("SE.idServico", "=", criteria.getLong("servico.idServico"));
	    	sql.addCriteria("PA.idPais", "=", criteria.getLong("pais.idPais"));
	    	sql.addCriteria("PP.idProdutoProibido", "=", criteria.getLong("produtoProibido.idProdutoProibido"));	    	
	    	if (!StringUtils.isBlank(criteria.getString("psMaximoVolume"))) {
	    		sql.addCriteria("RC.psMaximoVolume", "=", criteria.getBigDecimal("psMaximoVolume"), BigDecimal.class);
	    	}	
	    	sql.addOrderBy(OrderVarcharI18n.hqlOrder("SE.dsServico", LocaleContextHolder.getLocale())); 
    		    	
    	}catch (Exception e) {
			log.error(e);
		}
		return new Object[]{sql.getSql(),sql.getCriteria()};
	}
}
