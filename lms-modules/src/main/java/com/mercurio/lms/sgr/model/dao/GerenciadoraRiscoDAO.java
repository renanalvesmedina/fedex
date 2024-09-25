package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.GerenciadoraRisco;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class GerenciadoraRiscoDAO extends BaseCrudDao<GerenciadoraRisco, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return GerenciadoraRisco.class;
    }

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pessoa", FetchMode.JOIN);
	}

   


}