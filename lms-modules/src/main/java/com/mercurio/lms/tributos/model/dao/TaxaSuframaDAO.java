package com.mercurio.lms.tributos.model.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureRestrictionsBuilder;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.TaxaSuframa;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TaxaSuframaDAO extends BaseCrudDao<TaxaSuframa, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TaxaSuframa.class;
    }

    public Integer getRowCount(Map criteria, FindDefinition findDef, RestrictionsBuilder rb, ConfigureRestrictionsBuilder crb) {
        
    	TypedFlatMap map = new TypedFlatMap();
    	
    	map.putAll(criteria);
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("count(ts.idTaxaSuframa)");
    	sql.addFrom(TaxaSuframa.class.getName()+" ts ");
    	sql.addFrom(DomainValue.class.getName()+" dv join dv.domain as do ");     	
    	sql.addJoin("ts.tpIndicadorCalculo","dv.value"); 
    	sql.addCriteria("do.name","=","DM_INDICADOR_CALCULO_SUFRAMA");

    	sql.addCriteria("ts.tpIndicadorCalculo","like",(String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"tpIndicadorCalculo"));
    	
		if (StringUtils.isNotBlank(map.getString("dtVigencia"))){
			sql.addCustomCriteria("( ? BETWEEN cast(ts.dtVigenciaInicial as date) AND cast(ts.dtVigenciaFinal as date))",map.getYearMonthDay("dtVigencia"));
		}
		if (StringUtils.isNotBlank((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlLiquidoInicial"))){			
			sql.addCriteria("ts.vlLiquido",">=",
					ReflectionUtils.getConverterInstance().convert((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlLiquidoInicial"),BigDecimal.class));					
		}
		if (StringUtils.isNotBlank((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlLiquidoFinal"))){			
			sql.addCriteria("ts.vlLiquido","<=",
					ReflectionUtils.getConverterInstance().convert((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlLiquidoFinal"),BigDecimal.class));			
		}
		if (StringUtils.isNotBlank((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlMercadoria"))){
			sql.addCustomCriteria(" ? BETWEEN ts.vlMercadoriaInicial AND ts.vlMercadoriaFinal",
					ReflectionUtils.getConverterInstance().convert((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlMercadoria"),BigDecimal.class));
		}	    	

        Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
        return result.intValue();
    }
    
    public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
        
    	TypedFlatMap map = new TypedFlatMap();
    	
    	map.putAll(criteria);    	
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("new Map(ts.idTaxaSuframa as idTaxaSuframa, " +
    					  "ts.dtVigenciaInicial as dtVigenciaInicial," +
    					  "ts.dtVigenciaFinal as dtVigenciaFinal," +
    					  "ts.vlMercadoriaInicial as vlMercadoriaInicial," +
    					  "ts.vlMercadoriaFinal as vlMercadoriaFinal," +    					  
    					  "ts.vlMercadoriaInicial || ' até ' || ts.vlMercadoriaFinal as vlMercadoria," +
    					  "ts.vlLiquido as vlLiquido," +
    					  "dv.description as tpIndicadorCalculo)");
    	sql.addFrom(TaxaSuframa.class.getName()+" ts ");
    	sql.addFrom(DomainValue.class.getName()+" dv join dv.domain as do ");     	
    	sql.addJoin("ts.tpIndicadorCalculo","dv.value"); 
    	sql.addCriteria("do.name","=","DM_INDICADOR_CALCULO_SUFRAMA");
   
    	sql.addCriteria("ts.tpIndicadorCalculo","like",(String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"tpIndicadorCalculo"));
    	
		if (StringUtils.isNotBlank(map.getString("dtVigencia"))){
			sql.addCustomCriteria("( ? BETWEEN cast(ts.dtVigenciaInicial as date) AND cast(ts.dtVigenciaFinal as date))", map.getYearMonthDay("dtVigencia"));
		}
		if (StringUtils.isNotBlank((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlLiquidoInicial"))){			
			sql.addCriteria("ts.vlLiquido",">=",
					ReflectionUtils.getConverterInstance().convert((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlLiquidoInicial"),BigDecimal.class));					
		}
		if (StringUtils.isNotBlank((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlLiquidoFinal"))){			
			sql.addCriteria("ts.vlLiquido","<=",
					ReflectionUtils.getConverterInstance().convert((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlLiquidoFinal"),BigDecimal.class));			
		}
		if (StringUtils.isNotBlank((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlMercadoria"))){
			sql.addCustomCriteria(" ? BETWEEN ts.vlMercadoriaInicial AND ts.vlMercadoriaFinal",
					ReflectionUtils.getConverterInstance().convert((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"vlMercadoria"),BigDecimal.class));
		}	    	

    	sql.addOrderBy("ts.vlMercadoriaInicial");                
        sql.addOrderBy(OrderVarcharI18n.hqlOrder("dv.description",LocaleContextHolder.getLocale()));
        sql.addOrderBy("ts.vlLiquido");
    	sql.addOrderBy("ts.dtVigenciaFinal");    	
    	
        ResultSetPage rsp = this.getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
        return rsp;
    }
    
	/**
	 * Busca todas as Taxas Suframa que estejam vigentes
	 * 
	 * @param ts Taxa Suframa
	 * @return lista contendo todas as Taxas Suframa vigentes
	 */
	public List findTaxasSuframaVigente(TaxaSuframa ts){
		
        DetachedCriteria dc = DetachedCriteria.forClass(TaxaSuframa.class);
        
        dc.setProjection(Projections.rowCount());
        
        if( ts.getIdTaxaSuframa() != null ){
            dc.add(Restrictions.ne("idTaxaSuframa",ts.getIdTaxaSuframa()));
        }            
        
        dc = JTVigenciaUtils.getDetachedVigencia(dc,ts.getDtVigenciaInicial(), ts.getDtVigenciaFinal());

		return findByDetachedCriteria(dc);        
		
	}

    /**
     * Busca as Taxas Suframa que possuam conflito de vigências ou de valores de mercadoria
     * @param ts Taxa Suframa a ser salva
     * @return Lista contendo todas as Taxas Suframa retornadas da pesquisa
     */
    public List findTaxasSuframaConflitantes(TaxaSuframa ts) {
        
        DetachedCriteria dc = createDetachedCriteria();
        
        if( ts.getIdTaxaSuframa() != null ){
            dc.add(Restrictions.ne("idTaxaSuframa",ts.getIdTaxaSuframa()));
        }            
        
        dc = JTVigenciaUtils.getDetachedVigencia(dc,ts.getDtVigenciaInicial(), ts.getDtVigenciaFinal());
        
        dc.add(
                Restrictions.or(
                                  Restrictions.and(Restrictions.le("vlMercadoriaInicial",ts.getVlMercadoriaInicial()),
                                                   Restrictions.ge("vlMercadoriaFinal",ts.getVlMercadoriaInicial())),
                                  Restrictions.and(Restrictions.le("vlMercadoriaInicial",ts.getVlMercadoriaFinal()),
                                                   Restrictions.ge("vlMercadoriaFinal",ts.getVlMercadoriaFinal()))
                                ));
        
        
        return findByDetachedCriteria(dc);
        
    }
}