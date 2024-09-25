package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteEDIFilialCobranca;
import com.mercurio.lms.edi.model.dao.ClienteEDIFilialCobrancaDAO;


/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.edi.clienteEDIFilialCobrancaService"
 */

public class ClienteEDIFilialCobrancaService extends CrudService<ClienteEDIFilialCobranca, Long> {

	
	@Override
	public ClienteEDIFilialCobranca findById(Long id) {		
		return (ClienteEDIFilialCobranca)super.findById(id);
	}
	
	@Override
	public Serializable store(ClienteEDIFilialCobranca bean) {
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
     * Pagina��o da grid
     * 
     * @param paginatedQuery
     * @return
     */
	public ResultSetPage<ClienteEDIFilialCobranca> findPaginated(PaginatedQuery paginatedQuery) {
		return getClienteEDIFilialCobrancaDAO().findPaginated(paginatedQuery);
	}           
	       		        
    public ClienteEDIFilialCobrancaDAO getClienteEDIFilialCobrancaDAO() {
        return (ClienteEDIFilialCobrancaDAO) getDao();
    }
    
    public void setClienteEDIFilialCobrancaDAO(ClienteEDIFilialCobrancaDAO dao) {
        setDao(dao);
    }	
	 
}
