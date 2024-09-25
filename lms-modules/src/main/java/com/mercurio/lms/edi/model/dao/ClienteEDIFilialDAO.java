package com.mercurio.lms.edi.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteEDIFilial;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class ClienteEDIFilialDAO extends BaseCrudDao<ClienteEDIFilial, Long>{

	@Override
	public Class getPersistentClass() {		
		return ClienteEDIFilial.class;
	}
	
	public List<ClienteEDIFilial> findFiliaisById(Long id) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(ClienteEDIFilial.class)
		.setFetchMode("filial", FetchMode.JOIN)		
		.setFetchMode("filial.pessoa", FetchMode.JOIN)				
		.add(Restrictions.eq("idClienteEDIFilial", id));
				
		return findByDetachedCriteria(dc);
	}
	
	public void executeRemoveById(Long id){
		
		StringBuilder hql = new StringBuilder();
		hql.append("delete from ClienteEDIFilial where idClienteEDIFilial = ").append(id);
		
		executeHql(hql.toString(), new ArrayList());
	}

	public ResultSetPage<ClienteEDIFilial> findPaginated(PaginatedQuery paginatedQuery) {
		
		StringBuilder query = new StringBuilder()
			.append("from ClienteEDIFilial as clifilial ")
			.append(" join fetch clifilial.cliente as cli ")
			.append(" join fetch clifilial.clienteFilial as clifi ")
			.append(" join fetch clifi.pessoa as pes ")
			.append(" join fetch clifilial.filial as fil ")
			.append(" join fetch fil.pessoa as pesfil ")
			.append("where 1=1 ");
	
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idClienteEDI") != null) {
			query.append("  and cli.id =:idClienteEDI ");
		}
		if(MapUtils.getObject(criteria, "idCliente") != null) {
			query.append("  and clifi.id =:idCliente ");
		}
		if(MapUtils.getObject(criteria, "idFilial") != null) {
			query.append("  and fil.id =:idFilial ");
		}		
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
	
}
