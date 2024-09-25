package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.OcorrenciaCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaClienteDAO extends BaseCrudDao<OcorrenciaCliente, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return OcorrenciaCliente.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("ocorrenciaEntrega", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("ocorrenciaEntrega", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}

	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
    	StringBuilder hql = new StringBuilder()
    	.append(" DELETE ").append(getPersistentClass().getName())
    	.append(" WHERE cliente.id = :id");

    	getAdsmHibernateTemplate().removeById(hql.toString(), idCliente);
    }
}