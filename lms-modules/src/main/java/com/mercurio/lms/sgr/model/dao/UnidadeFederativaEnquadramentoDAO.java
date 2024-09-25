package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.UnidadeFederativaEnquadramento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class UnidadeFederativaEnquadramentoDAO extends BaseCrudDao<UnidadeFederativaEnquadramento, Long> {

/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return UnidadeFederativaEnquadramento.class;
    }

    protected void initFindListLazyProperties(Map lazyFindList) {
    	lazyFindList.put("unidadeFederativa", FetchMode.JOIN );
		super.initFindListLazyProperties(lazyFindList);
	}

}