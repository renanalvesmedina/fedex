package com.mercurio.lms.edi.model.dao;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteEDIFilialCobranca;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class ClienteEDIFilialCobrancaDAO extends BaseCrudDao<ClienteEDIFilialCobranca, Long>{

	@Override
	public Class getPersistentClass() {		
		return ClienteEDIFilialCobranca.class;
	}
	
	public ResultSetPage<ClienteEDIFilialCobranca> findPaginated(PaginatedQuery paginatedQuery) {
		
		StringBuilder query = new StringBuilder()
		.append("from ClienteEDIFilialCobranca as cobranca ")
		.append(" join fetch cobranca.cliente as cli ")
		.append(" join fetch cobranca.clienteCobranca as clientecobranca ")
		.append(" join fetch clientecobranca.pessoa as pessoacobranca ")
		.append("where 1=1 ");
	
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idClienteEDI") != null) {
			query.append("  and cli.id =:idClienteEDI ");
		}
		if(MapUtils.getObject(criteria, "idCliente") != null) {
			query.append("  and clientecobranca.id =:idCliente ");
		}
	
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}	
}
