package com.mercurio.lms.configuracoes.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.TipoPrecificacaoServicoAdic;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoPrecificacaoServicoAdicDAO extends BaseCrudDao<TipoPrecificacaoServicoAdic, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoPrecificacaoServicoAdic.class;
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("servicoAdicional",FetchMode.JOIN);
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("servicoAdicional",FetchMode.JOIN);
    }


}