package com.mercurio.lms.carregamento.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OperadoraCartaoPedagioDAO extends BaseCrudDao<OperadoraCartaoPedagio, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return OperadoraCartaoPedagio.class;
    }
    
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("pessoa", FetchMode.JOIN);
    	super.initFindByIdLazyProperties(lazyFindById);
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("pessoa", FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(lazyFindPaginated);
    }
}