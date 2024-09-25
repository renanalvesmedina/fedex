package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.PostoControle;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PostoControleDAO extends BaseCrudDao<PostoControle, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return PostoControle.class;
    }

    protected void initFindByIdLazyProperties(Map map) {
    	map.put("municipio", FetchMode.JOIN);
		map.put("rodovia", FetchMode.JOIN);
		map.put("reguladoraSeguro", FetchMode.JOIN);
		map.put("reguladoraSeguro.pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("municipio", FetchMode.JOIN);
		map.put("municipio.unidadeFederativa", FetchMode.JOIN);
		map.put("rodovia", FetchMode.JOIN);
		map.put("reguladoraSeguro.pessoa", FetchMode.JOIN);
	}

}