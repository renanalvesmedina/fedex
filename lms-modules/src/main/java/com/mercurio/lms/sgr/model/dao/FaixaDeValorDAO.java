package com.mercurio.lms.sgr.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.FaixaDeValor;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FaixaDeValorDAO extends BaseCrudDao<FaixaDeValor, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FaixaDeValor.class;
    }

   	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("enquadramentoRegra", FetchMode.JOIN);   		
		fetchModes.put("enquadramentoRegra.moeda", FetchMode.JOIN);
	}
    protected void initFindPaginatedLazyProperties(Map fetchModes) 
    {
		fetchModes.put("enquadramentoRegra", FetchMode.JOIN);
		fetchModes.put("enquadramentoRegra.moeda", FetchMode.JOIN);
    }    
   	
   	public void evictFaixaDeValor(FaixaDeValor faixaDeValor) {
		getAdsmHibernateTemplate().evict(faixaDeValor);
	}

   	
   	/**
	 * Identifica a faixa de valor de um enquadramento de regra.
	 * 
	 * @param idEnquadramentoRegra (required)
	 * @param valor (required)
	 * @param blExclusivaAeroporto
	 * @return
	 */
	public FaixaDeValor findByEnquadramentoRegraAndValor(
			Long idEnquadramentoRegra, BigDecimal valor, Boolean blExclusivaAeroporto) {
    	DetachedCriteria dc = DetachedCriteria.forClass(FaixaDeValor.class, "fv")
    		.createAlias("fv.enquadramentoRegra", "er")
			.add(Restrictions.eq("er.id", idEnquadramentoRegra))
			.add(Restrictions.le("fv.vlLimiteMinimo", valor))
			.add(Restrictions.or(Restrictions.isNull("fv.vlLimiteMaximo"), Restrictions.ge("fv.vlLimiteMaximo", valor)));
		// LMS-6850 - filtro para faixa exclusiva aeroporto
		if (blExclusivaAeroporto != null && !blExclusivaAeroporto) {
			dc.add(Restrictions.eq("fv.blExclusivaAeroporto", Boolean.FALSE));
		}

    	List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (result.isEmpty())
    		return null;
    	else
    		return (FaixaDeValor)result.get(0);
    }


    /**
	 * Identifica a faixa de valor de um enquadramento de regra.
	 * 
	 * @param idEnquadramentoRegra (required)
	 * @param valor (required)
	 * @return
	 */
    public FaixaDeValor findFaixaDeValorByIdEnquadramentoRegraByValor(Long idEnquadramentoRegra, BigDecimal valor) {
    	StringBuffer sql = new StringBuffer()
    	.append("select fv ")
    	.append("from ")
    	.append(FaixaDeValor.class.getName()).append(" fv ")
    	.append("inner join fv.enquadramentoRegra as er ")
    	.append("inner join fv.exigenciaFaixaValors as efv ")
    	.append("where ")
    	.append("er.id = ? ")
    	.append("and fv.vlLimiteMinimo <= ? ")
		.append("and (fv.vlLimiteMaximo is null or fv.vlLimiteMaximo >= ?) ");

    	List param = new ArrayList();
    	param.add(idEnquadramentoRegra);
    	param.add(valor);
    	param.add(valor);

    	List result = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    	if (result.isEmpty())
    		return null;
    	else
    		return (FaixaDeValor)result.get(0);
    }
}