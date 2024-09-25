package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.vendas.model.CampanhaMarketing;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CampanhaMarketingDAO extends BaseCrudDao<CampanhaMarketing, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CampanhaMarketing.class;
    }    

    public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {    
    	StringBuilder query = new StringBuilder()
			.append(" new Map(im.idCampanhaMarketing as idCampanhaMarketing").append(",")
			.append(" "+PropertyVarcharI18nProjection.createProjection("im.dsCampanhaMarketing")+" as dsCampanhaMarketing").append(",")
			.append("im.tpSituacao as tpSituacao)");

		SqlTemplate hql = montaHQL(criteria);
		hql.addProjection(query.toString());

		hql.addCriteria("im.tpSituacao","=",MapUtilsPlus.getString(criteria,"tpSituacao",null));
	    hql.addOrderBy(" "+PropertyVarcharI18nProjection.createProjection("im.dsCampanhaMarketing")+"");

    	return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
    }
    
    public Integer getRowCount(Map criteria) {    
    	SqlTemplate hql = montaHQL(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
    }
    
    private SqlTemplate montaHQL(Map criteria) {
    	SqlTemplate hql = new SqlTemplate();
        hql.addFrom(getPersistentClass().getCanonicalName() + " as im join im.empresa as e ");        
        hql.addCriteria("e.idEmpresa","=",MapUtilsPlus.getLong(criteria,"ID_EMPRESA_LOG_USER",null));
 
        if (!MapUtilsPlus.getString(criteria,"dsCampanhaMarketing","").equals("")) {
        	hql.addCriteria("lower("+PropertyVarcharI18nProjection.createProjection("im.dsCampanhaMarketing")+")","like",
        				MapUtilsPlus.getString(criteria,"dsCampanhaMarketing","").toLowerCase());
        }
    	return hql;
    }
}