package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.DeParaEDI;
import com.mercurio.lms.edi.model.dao.DeParaEDIDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.deParaEDIService"
 */

public class DeParaEDIService extends CrudService<DeParaEDI, Long> {
	
	@Override
	public DeParaEDI findById(Long id) {		
		return (DeParaEDI)super.findById(id);
	}
	
	@Override
	public Serializable store(DeParaEDI bean) {
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
	
	public ResultSetPage<DeParaEDI> findPaginated(PaginatedQuery paginatedQuery) {
		return getDeParaEDIDAO().findPaginated(paginatedQuery);
	}    
		
    public DeParaEDIDAO getDeParaEDIDAO() {
        return (DeParaEDIDAO) getDao();
    }
    
    public void setDeParaEDIDAO(DeParaEDIDAO dao) {
        setDao(dao);
    }	
}
