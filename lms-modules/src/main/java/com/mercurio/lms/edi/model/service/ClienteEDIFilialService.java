package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteEDIFilial;
import com.mercurio.lms.edi.model.dao.ClienteEDIFilialDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.clienteEDIFilialService"
 */

public class ClienteEDIFilialService extends CrudService<ClienteEDIFilial, Long> {

	
	@Override
	public ClienteEDIFilial findById(Long id) {		
		return (ClienteEDIFilial)super.findById(id);
	}
	
	@Override
	public Serializable store(ClienteEDIFilial bean) {
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
	
    /**
     * Obtem todos os clientes filiais relacionados a Cliente EDI
     * 
     * @param id
     * @return
     */
    public List<ClienteEDIFilial> findFiliaisById(Long id){
    	return getClienteEDIFilialDAO().findFiliaisById(id);
    }    	
	
    /**
     * Paginação da grid
     * 
     * @param paginatedQuery
     * @return
     */
	public ResultSetPage<ClienteEDIFilial> findPaginated(PaginatedQuery paginatedQuery) {
		return getClienteEDIFilialDAO().findPaginated(paginatedQuery);
	}        
        
    public ClienteEDIFilialDAO getClienteEDIFilialDAO() {
        return (ClienteEDIFilialDAO) getDao();
    }
    
    public void setClienteEDIFilialDAO(ClienteEDIFilialDAO dao) {
        setDao(dao);
    }	
	 
}
