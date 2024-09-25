package com.mercurio.lms.tributos.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.ObrigacaoAcessoriaIssMun;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ObrigacaoAcessoriaIssMunDAO extends BaseCrudDao<ObrigacaoAcessoriaIssMun, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ObrigacaoAcessoriaIssMun.class;
    }

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("municipio", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("municipio", FetchMode.JOIN);
	}    

}