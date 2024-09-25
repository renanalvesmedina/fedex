package com.mercurio.lms.pendencia.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.pendencia.model.UfDispositivoLegal;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class UfDispositivoLegalDAO extends BaseCrudDao<UfDispositivoLegal, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return UfDispositivoLegal.class;
    }

    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("unidadeFederativa", FetchMode.JOIN);
    }
    
    protected void initFindByIdLazyProperties(Map map) 
    {
    	map.put("unidadeFederativa", FetchMode.JOIN);
    }
 

}