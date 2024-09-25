package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.EscoltaOperadoraMct;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EscoltaOperadoraMctDAO extends BaseCrudDao<EscoltaOperadoraMct, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EscoltaOperadoraMct.class;
    }

	protected void initFindByIdLazyProperties(Map  map) {
		map.put("escolta",FetchMode.JOIN);
		map.put("escolta.pessoa",FetchMode.JOIN);
		map.put("operadoraMct",FetchMode.JOIN);
		map.put("operadoraMct.pessoa",FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("escolta",FetchMode.JOIN);
		map.put("escolta.pessoa",FetchMode.JOIN);
		map.put("operadoraMct",FetchMode.JOIN);
		map.put("operadoraMct.pessoa",FetchMode.JOIN);		
	}

   


}