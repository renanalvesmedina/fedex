package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.vendas.model.TipoVisita;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoVisitaDAO extends BaseCrudDao<TipoVisita, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoVisita.class;
    }

    public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {    
    	StringBuilder query = new StringBuilder()
    		.append(" new Map(v.idTipoVisita as idTipoVisita").append(",")
    		.append(" "+PropertyVarcharI18nProjection.createProjection("v.dsTipoVisita")+" as dsTipoVisita").append(",")
    		.append("v.tpSituacao as tpSituacao)");

    	SqlTemplate hql = montaHQL(criteria);
    	hql.addProjection(query.toString());

    	hql.addCriteria("v.tpSituacao","=",MapUtilsPlus.getString(criteria,"tpSituacao",null));
        hql.addOrderBy(" "+PropertyVarcharI18nProjection.createProjection("v.dsTipoVisita")+" ");
    	return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
    }

    public Integer getRowCount(Map criteria) {    
    	SqlTemplate hql = montaHQL(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
    }

    private SqlTemplate montaHQL(Map criteria) {
        SqlTemplate hql = new SqlTemplate();
        hql.addFrom(getPersistentClass().getCanonicalName() + " as  v join v.empresa as e ");        
        hql.addCriteria("e.idEmpresa","=",MapUtilsPlus.getLong(criteria,"ID_EMPRESA_LOG_USER",null));
 
        if (!MapUtilsPlus.getString(criteria,"dsTipoVisita","").equals("")) {
        	hql.addCriteria("lower("+PropertyVarcharI18nProjection.createProjection("v.dsTipoVisita")+")","like",
        				MapUtilsPlus.getString(criteria,"dsTipoVisita","").toLowerCase());
        }
    	return hql;
    }
}