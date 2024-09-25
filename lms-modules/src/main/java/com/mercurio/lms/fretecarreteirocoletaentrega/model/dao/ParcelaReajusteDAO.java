package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaReajuste;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParcelaReajusteDAO extends BaseCrudDao<ParcelaReajuste, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ParcelaReajuste.class;
    }

   


}