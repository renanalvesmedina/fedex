package com.mercurio.lms.edi.model.dao;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteEDI;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class ClienteEDIDAO extends BaseCrudDao<ClienteEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return ClienteEDI.class;
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<ClienteEDI> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("select clienteedi ")
			.append("from " + getPersistentClass().getName() + " as clienteedi ")
			.append(" , Cliente as cliente ")
			.append("where cliente.idCliente = clienteedi.idClienteEDI ");
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idCliente") != null) {
			query.append("  and cliente.id = :idCliente ");
		}
		
		query.append("  order by cliente.pessoa.nmPessoa asc ");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}

	public ClienteEDI findClienteEDIById(Long id) {
		
		DetachedCriteria dc = createDetachedCriteria()
		.add(Restrictions.eq("id", id));
				
		return (ClienteEDI)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
}
