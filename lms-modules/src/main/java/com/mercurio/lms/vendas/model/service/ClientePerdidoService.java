package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.ClientePerdido;
import com.mercurio.lms.vendas.model.dao.ClientePerdidoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.clientePerdidoService"
 */
public class ClientePerdidoService extends CrudService<ClientePerdido, Long>{
	
	
	
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		return getClientePerdidoDAO().findPaginatedCustom(criteria, FindDefinition.createFindDefinition(criteria));
	}

	
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getClientePerdidoDAO().getRowCount(criteria);
	}

	public Serializable store(ClientePerdido bean) {
		return super.store(bean);
	}

	@Override
	protected void removeById(Long id) {
		// TODO Auto-generated method stub
		super.removeById(id);
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		// TODO Auto-generated method stub
		super.removeByIds(ids);
	}
	
	private final ClientePerdidoDAO getClientePerdidoDAO() {
		return (ClientePerdidoDAO) getDao();
	}
	public void setClientePerdidoDAO(ClientePerdidoDAO dao) {
		setDao( dao );
	}

}
