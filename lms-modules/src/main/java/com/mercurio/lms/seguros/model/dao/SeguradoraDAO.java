package com.mercurio.lms.seguros.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.seguros.model.Seguradora;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SeguradoraDAO extends BaseCrudDao<Seguradora, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Seguradora.class;
    }

    protected void initFindByIdLazyProperties(Map map) {
    	map.put("pessoa", FetchMode.JOIN);
    }
    
    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("pessoa", FetchMode.JOIN);
    }

	protected void initFindListLazyProperties(Map map) {
		map.put("pessoa", FetchMode.JOIN);
	}
}