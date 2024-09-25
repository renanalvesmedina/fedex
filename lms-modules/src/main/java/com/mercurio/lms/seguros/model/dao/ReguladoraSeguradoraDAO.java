package com.mercurio.lms.seguros.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.seguros.model.ReguladoraSeguradora;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReguladoraSeguradoraDAO extends BaseCrudDao<ReguladoraSeguradora, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ReguladoraSeguradora.class;
    }

	protected void initFindListLazyProperties(Map map) {
		map.put("seguradora",FetchMode.JOIN);
		map.put("seguradora.pessoa",FetchMode.JOIN);
	}
    
    protected void initFindByIdLazyProperties(Map map) {
        map.put("reguladoraSeguro",FetchMode.JOIN);
        map.put("reguladoraSeguro.pessoa",FetchMode.JOIN);
        map.put("seguradora",FetchMode.JOIN);
        map.put("seguradora.pessoa",FetchMode.JOIN);
    }
}