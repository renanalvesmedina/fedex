package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.FilialReajuste;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FilialReajusteDAO extends BaseCrudDao<FilialReajuste, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FilialReajuste.class;
    }

    protected void initFindByIdLazyProperties(Map fetchModes) {
    	fetchModes.put("filial",FetchMode.JOIN);
    	fetchModes.put("filial.pessoa",FetchMode.JOIN);
    	super.initFindByIdLazyProperties(fetchModes);
    }
   


}