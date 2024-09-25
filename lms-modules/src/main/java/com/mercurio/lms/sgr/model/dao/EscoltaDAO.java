package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.Escolta;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EscoltaDAO extends BaseCrudDao<Escolta, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return Escolta.class;
    }

   protected void initFindByIdLazyProperties(Map map) {
	   map.put("pessoa",FetchMode.SELECT);
   }
}