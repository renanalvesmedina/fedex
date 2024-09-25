package com.mercurio.lms.vendas.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.GerenciaRegional;
import com.mercurio.lms.vendas.model.MunicipioRegionalCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MunicipioRegionalClienteDAO extends BaseCrudDao<MunicipioRegionalCliente, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MunicipioRegionalCliente.class;
    }

    /**
	 * Remove todas os itens relacionados ao cliente informado.
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
    	StringBuilder hql = new StringBuilder()
    	.append(" DELETE ").append(getPersistentClass().getName())
    	.append(" WHERE gerenciaRegional.id = ( ")
    	.append("       SELECT gr.id ")
    	.append("       FROM ").append(GerenciaRegional.class.getName()).append(" gr ")
    	.append("       WHERE gr.cliente.id = :id) ");

    	getAdsmHibernateTemplate().removeById(hql.toString(), idCliente);
    }
}