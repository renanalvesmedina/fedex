package com.mercurio.lms.carregamento.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.EstoqueDispQtdeHist;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class EstoqueDispQtdeHistDAO extends BaseCrudDao<EstoqueDispQtdeHist, Long> {

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    protected final Class getPersistentClass() {
        return EstoqueDispQtdeHist.class;
    }
}