package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteiroviagem.model.CriterioAplicSimulacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CriterioAplicSimulacaoDAO extends BaseCrudDao<CriterioAplicSimulacao, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return CriterioAplicSimulacao.class;
    }

   


}