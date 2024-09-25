package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;


import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PostoConveniadoDAO extends BaseCrudDao<PostoConveniado, Long> {

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pessoa", FetchMode.JOIN );
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("pessoa", FetchMode.JOIN );
		super.initFindListLazyProperties(lazyFindList);
	}
	
	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("pessoa", FetchMode.JOIN );
		super.initFindLookupLazyProperties(lazyFindLookup);
	}
	
	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pessoa", FetchMode.JOIN );
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PostoConveniado.class;
    }

    
}