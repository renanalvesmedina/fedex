package com.mercurio.lms.sgr.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.ExigenciaFaixaValor;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ExigenciaFaixaValorDAO extends BaseCrudDao<ExigenciaFaixaValor, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ExigenciaFaixaValor.class;
    }

	@SuppressWarnings("unchecked")
	protected void initFindByIdLazyProperties(Map map) {
		map.put("faixaDeValor", FetchMode.JOIN);
		map.put("faixaDeValor.enquadramentoRegra", FetchMode.JOIN);
		map.put("faixaDeValor.enquadramentoRegra.moeda", FetchMode.JOIN);
		map.put("exigenciaGerRisco", FetchMode.JOIN);
		map.put("exigenciaGerRisco.tipoExigenciaGerRisco", FetchMode.JOIN);
		// LMS-6848
		map.put("filialInicio", FetchMode.JOIN);
	}

	@SuppressWarnings("unchecked")
	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("faixaDeValor", FetchMode.JOIN);
		map.put("faixaDeValor.enquadramentoRegra", FetchMode.JOIN);
		map.put("faixaDeValor.enquadramentoRegra.moeda", FetchMode.JOIN);
		map.put("exigenciaGerRisco", FetchMode.JOIN);
		map.put("exigenciaGerRisco.tipoExigenciaGerRisco", FetchMode.JOIN);
		// LMS-6848
		map.put("filialInicio", FetchMode.JOIN);
	}    
    
    /**
     * 
     * @param idFaixaDeValor
     * @return
     */
    public List findByFaixaDeValor(Long idFaixaDeValor) {
    	DetachedCriteria dc = DetachedCriteria.forClass(ExigenciaFaixaValor.class, "efv")
    		.createAlias("efv.exigenciaGerRisco", "egr")
    		.createAlias("egr.tipoExigenciaGerRisco", "tegr")
    		.add(Restrictions.eq("efv.faixaDeValor.id", idFaixaDeValor));

    	List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
   		return result;
    }
}