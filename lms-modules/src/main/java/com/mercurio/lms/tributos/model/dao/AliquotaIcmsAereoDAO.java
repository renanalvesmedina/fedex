package com.mercurio.lms.tributos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tributos.model.AliquotaIcmsAereo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AliquotaIcmsAereoDAO extends BaseCrudDao<AliquotaIcmsAereo, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AliquotaIcmsAereo.class;
    }

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("unidadeFederativa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("unidadeFederativa", FetchMode.JOIN);
	}
   
    /**
     * Retorna a AliquotaIcmsAereo vigente por unidade federativa.
     * 
     * @author Mickaël Jalbert
     * @since 26/05/2006
     * 
     * @param Long idUnidadeFederativa
     * @param YearMonthDay dtVigencia
     * 
     * @return AliquotaIcmsAereo
     * */
    public AliquotaIcmsAereo findVigenteByUF(Long idUnidadeFederativa, YearMonthDay dtVigencia){
    	SqlTemplate hql = new SqlTemplate();
    	SqlTemplate hqlSub = new SqlTemplate();
    	
    	hqlSub.addProjection("MAX(aliSub.dtInicioVigencia)");
    	hqlSub.addFrom(AliquotaIcmsAereo.class.getName(), "aliSub");
    	hqlSub.addCriteria("aliSub.unidadeFederativa.id", "=", idUnidadeFederativa);
    	hqlSub.addCriteria("aliSub.dtInicioVigencia", "<=", dtVigencia);

    	
    	hql.addProjection("ali");
    	hql.addFrom(AliquotaIcmsAereo.class.getName(), "ali");
    	hql.addCriteria("ali.unidadeFederativa.id", "=", idUnidadeFederativa);
    	
    	hql.addCustomCriteria("ali.dtInicioVigencia = (" + hqlSub.getSql() + ")");
    	hql.addCriteriaValue(hqlSub.getCriteria());
    	
    	List lstAliquotas = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (lstAliquotas.size() == 1){
    		return (AliquotaIcmsAereo) lstAliquotas.get(0);
    	} else {
    		return null;
    	}
    }
    

}