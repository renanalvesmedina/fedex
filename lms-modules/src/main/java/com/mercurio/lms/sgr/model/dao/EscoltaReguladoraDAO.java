package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.EscoltaReguladora;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EscoltaReguladoraDAO extends BaseCrudDao<EscoltaReguladora, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return EscoltaReguladora.class;
    }

   
    protected void initFindByIdLazyProperties(Map map) {
        map.put("reguladoraSeguro",FetchMode.JOIN);
        map.put("reguladoraSeguro.pessoa",FetchMode.JOIN);
        map.put("escolta",FetchMode.JOIN);
        map.put("escolta.pessoa",FetchMode.JOIN);
    }
}