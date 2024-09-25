package com.mercurio.lms.workflow.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.workflow.model.Ocorrencia;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaDAO extends BaseCrudDao<Ocorrencia, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return Ocorrencia.class;
    }

   
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("eventoWorkflow",FetchMode.JOIN);
    }


}