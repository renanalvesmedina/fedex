package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.DescritivoPce;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DescritivoPceDAO extends BaseCrudDao<DescritivoPce, Long>
{

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("ocorrenciaPce",FetchMode.JOIN);
		fetchModes.put("ocorrenciaPce.eventoPce",FetchMode.JOIN);
		fetchModes.put("ocorrenciaPce.eventoPce.processoPce",FetchMode.JOIN);
		fetchModes.put("empresa",FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("ocorrenciaPce",FetchMode.JOIN);
		fetchModes.put("ocorrenciaPce.eventoPce",FetchMode.JOIN);
		fetchModes.put("ocorrenciaPce.eventoPce.processoPce",FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return DescritivoPce.class;
    }

   


}