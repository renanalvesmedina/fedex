package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteLayoutEDI;
import com.mercurio.lms.edi.model.TipoTransmissaoEDI;
import com.mercurio.lms.edi.model.dao.ClienteLayoutEDIDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.clienteLayoutEDIService"
 */

public class ClienteLayoutEDIService extends CrudService<ClienteLayoutEDI, Long> {

	
	@Override
	public ClienteLayoutEDI findById(Long id) {		
		return (ClienteLayoutEDI)super.findById(id);
	}
	

	public ClienteLayoutEDI findByIdCliente(Long id) {		
		return getClienteLayoutEDIDAO().findByIdClienteEdi(id);
	}
	
	@Override
	public Serializable store(ClienteLayoutEDI bean) {
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
	
    private ClienteLayoutEDIDAO getClienteLayoutEDIDAO() {
        return (ClienteLayoutEDIDAO) getDao();
    }
    
    public void setClienteLayoutEDIDAO(ClienteLayoutEDIDAO dao) {
        setDao(dao);
    }	
	
	public ResultSetPage<ClienteLayoutEDI> findPaginated(PaginatedQuery paginatedQuery) {
		return getClienteLayoutEDIDAO().findPaginated(paginatedQuery);
	}

	public List<TipoTransmissaoEDI> findListTpTransmissao() {		
		return getClienteLayoutEDIDAO().findListTpTransmissao();
	}
	
	public List<ClienteLayoutEDI> findClientesComAlteracaoLayout(){
		return getClienteLayoutEDIDAO().findClientesComAlteracaoLayout();
	}
		
	public List<ClienteLayoutEDI> findClientesLayoutPorTipo(){
		return getClienteLayoutEDIDAO().findClientesLayoutPorTipo(); 
}
	
	public List<ClienteLayoutEDI> findPorTipo(Map params){
		if(params != null && !params.isEmpty()){
			return getClienteLayoutEDIDAO().findPorTipo(params); 
		} else {
			return getClienteLayoutEDIDAO().findPorTipo(null);
	}
	
}
	
}
