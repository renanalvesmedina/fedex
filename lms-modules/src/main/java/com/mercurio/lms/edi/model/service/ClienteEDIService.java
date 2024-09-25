package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteEDI;
import com.mercurio.lms.edi.model.dao.ClienteEDIDAO;
import com.mercurio.lms.vendas.model.service.ClienteService;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.clienteEDIService"
 */

public class ClienteEDIService extends CrudService<ClienteEDI, Long> {

	private ClienteService clienteService;
	
	@Override
	public ClienteEDI findById(Long id) {		
		return (ClienteEDI)super.findById(id);
	}
	
	@Override
	public Serializable store(ClienteEDI bean) {
		return super.store(bean);
	}
	
	@Override
	public void removeById(Long id) {		
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
    private ClienteEDIDAO getClienteEDIDAO() {
        return (ClienteEDIDAO) getDao();
    }
    
    public void setClienteEDIDAO(ClienteEDIDAO dao) {
        setDao(dao);
    }	
     
    public ClienteEDI findClienteEDIById(Long id) {		
		return getClienteEDIDAO().findClienteEDIById(id);
	}

	public ResultSetPage<ClienteEDI> findPaginated(PaginatedQuery paginatedQuery) {
		return getClienteEDIDAO().findPaginated(paginatedQuery);
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}  
}
