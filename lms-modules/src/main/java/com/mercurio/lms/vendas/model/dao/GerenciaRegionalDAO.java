package com.mercurio.lms.vendas.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.GerenciaRegional;
import com.mercurio.lms.vendas.model.MunicipioRegionalCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class GerenciaRegionalDAO extends BaseCrudDao<GerenciaRegional, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
	protected final Class getPersistentClass() {
		return GerenciaRegional.class;
	}

	/**
	 * Remove todas as rela��es entre GerenciaRegional e Munic�pios, relacionadas atrav�s da MunicipioRegionalCliente
	 * @param regional
	 */
	public void deleteAllMunicipioRegionalClientesByGerenciaRegional(Long idGerenciaRegional) {		
		if(idGerenciaRegional != null) {
			StringBuilder query = new StringBuilder()
			.append(" DELETE ").append(MunicipioRegionalCliente.class.getName())
			.append(" WHERE gerenciaRegional.id = :id");

			getAdsmHibernateTemplate().removeById(query.toString(),idGerenciaRegional);
		}
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