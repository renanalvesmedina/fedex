package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.ValorCampoComplementar;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ValorCampoComplementarDAO extends BaseCrudDao<ValorCampoComplementar, Long>
{
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ValorCampoComplementar.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("campoComplementar", FetchMode.JOIN);
    }
    
   protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
	   lazyFindPaginated.put("campoComplementar", FetchMode.JOIN);
   }


}