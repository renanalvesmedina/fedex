package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.ClienteEnquadramento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ClienteEnquadramentoDAO extends BaseCrudDao<ClienteEnquadramento, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ClienteEnquadramento.class;
    }

    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("cliente", FetchMode.SELECT);   	
    	lazyFindById.put("cliente.pessoa", FetchMode.SELECT);   	
		super.initFindByIdLazyProperties(lazyFindById);
	}


	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("cliente", FetchMode.SELECT);   	
		lazyFindList.put("cliente.pessoa", FetchMode.SELECT);   	
		super.initFindListLazyProperties(lazyFindList);
	}

}