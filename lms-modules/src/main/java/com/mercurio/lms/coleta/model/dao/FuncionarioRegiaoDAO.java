package com.mercurio.lms.coleta.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.coleta.model.FuncionarioRegiao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FuncionarioRegiaoDAO extends BaseCrudDao<FuncionarioRegiao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FuncionarioRegiao.class;
    }

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("usuario", FetchMode.JOIN);
		lazyFindById.put("regiaoColetaEntregaFil", FetchMode.JOIN);
		lazyFindById.put("regiaoColetaEntregaFil.filial", FetchMode.JOIN);
		lazyFindById.put("regiaoColetaEntregaFil.filial.pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("usuario", FetchMode.JOIN);
		lazyFindPaginated.put("regiaoColetaEntregaFil", FetchMode.JOIN);
	}

}