package com.mercurio.lms.carregamento.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.EstoqueDispQtdeHist;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplica��o atrav�s do suporte
 * ao Hibernate em conjunto com o Spring. N�o inserir documenta��o ap�s ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class EstoqueDispQtdeHistDAO extends BaseCrudDao<EstoqueDispQtdeHist, Long> {

    /**
     * Nome da classe que o DAO � respons�vel por persistir.
     */
    protected final Class getPersistentClass() {
        return EstoqueDispQtdeHist.class;
    }
}