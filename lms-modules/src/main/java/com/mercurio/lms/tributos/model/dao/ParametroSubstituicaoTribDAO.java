package com.mercurio.lms.tributos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tributos.model.ParametroSubstituicaoTrib;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParametroSubstituicaoTribDAO extends BaseCrudDao<ParametroSubstituicaoTrib, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ParametroSubstituicaoTrib.class;
    }

    /**
     * Método que inicializa os relacionamentos do pojo
     */
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("unidadeFederativa", FetchMode.JOIN);
	} 
	
    public ParametroSubstituicaoTrib findVigenteByUf(Long idUf, YearMonthDay dtVigencia){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("pst");
    	
    	hql.addInnerJoin(ParametroSubstituicaoTrib.class.getName(), "pst");
    	
    	hql.addCriteria("pst.unidadeFederativa.id", "=", idUf);
    	JTVigenciaUtils.getHqlVigenciaNotNull(hql, "pst.dtVigenciaInicial", "pst.dtVigenciaFinal", dtVigencia);
    	
    	List lstParametro = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (lstParametro.size() == 1) {
    		return (ParametroSubstituicaoTrib) lstParametro.get(0);
    	} else {
    		return null;
    	}
    }	
    
	/**
	 * Método responsável por buscar ParametroSubstituicaoTrib que estejam no mesmo intervalo de vigência
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 30/05/2006
	 * 
	 * @param vigenciaInicial
	 * @param vigenciaFinal
	 * @return List 
	 */
    public List findParametroSubstituicaoTribByVigenciaEquals(YearMonthDay vigenciaInicial
    		, YearMonthDay vigenciaFinal
    				, Long idUnidadeFederativa
    						, Long idParametroSubstituicaoTrib){
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("pst");
    	
    	hql.addFrom(getPersistentClass().getName() + " pst ");
    	
    	/** Criteria para buscar registros no mesmo intervalo de vigência */ 
    	hql.addCustomCriteria("( (? between pst.dtVigenciaInicial and pst.dtVigenciaFinal) " +
    						  " OR (? between pst.dtVigenciaInicial and pst.dtVigenciaFinal) " +
    						  " OR (? < pst.dtVigenciaInicial  AND ? > pst.dtVigenciaFinal) )");
    	
    	hql.addCriteriaValue(vigenciaInicial);
    	hql.addCriteriaValue(JTDateTimeUtils.maxYmd(vigenciaFinal));
    	hql.addCriteriaValue(vigenciaInicial);
    	hql.addCriteriaValue(JTDateTimeUtils.maxYmd(vigenciaFinal));
    	
    	hql.addCriteria("pst.unidadeFederativa.idUnidadeFederativa", "=", idUnidadeFederativa);
    	hql.addCriteria("pst.idParametroSubstituicaoTrib", "<>", idParametroSubstituicaoTrib);
    	
    	List lst = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	return lst;
    }
    
}