package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.PaisEnquadramento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PaisEnquadramentoDAO extends BaseCrudDao<PaisEnquadramento, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return PaisEnquadramento.class;
    }

    protected void initFindListLazyProperties(Map lazyFindList) {
    	lazyFindList.put("pais", FetchMode.JOIN );
		super.initFindListLazyProperties(lazyFindList);
	}
}