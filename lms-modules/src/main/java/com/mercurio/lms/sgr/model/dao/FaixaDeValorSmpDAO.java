package com.mercurio.lms.sgr.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.FaixaDeValorSmp;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FaixaDeValorSmpDAO extends BaseCrudDao<FaixaDeValorSmp, Long>{
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("exigenciaGerRisco", FetchMode.JOIN);
		lazyFindById.put("exigenciaGerRisco.tipoExigenciaGerRisco", FetchMode.JOIN);
		lazyFindById.put("filialInicio", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class<FaixaDeValorSmp> getPersistentClass() {
        return FaixaDeValorSmp.class;
    }

    /**
     * Persiste uma coleção de FaixaDeValorSmp
     * @param FaixaDeValorSmp
     * @author RomuloP
     */
    public void storeAll(List<FaixaDeValorSmp> faixaDeValorSmp) {
    	getAdsmHibernateTemplate().saveOrUpdateAll(faixaDeValorSmp);
    }
    
    
	
	/**
	 * Remove a faixa de valor SMP pelo id da SMP
	 * @param idSolicMonitPreventivo
	 */
	public void removeByIdSolicMonitPreventivo(Long idSolicMonitPreventivo) {
		String s = "delete from " + FaixaDeValorSmp.class.getName() + " as es " +
			" where es.id in (select esmp.id from " + 
			FaixaDeValorSmp.class.getName()+ 
			" esmp where esmp.solicMonitPreventivo.id = ?) ";
		
		getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s).setParameter(0, idSolicMonitPreventivo).executeUpdate();
	}

}