package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.CampoLayoutEDI;
import com.mercurio.lms.edi.model.dao.CampoLayoutEDIDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.campoLayoutEDIService"
 */

public class CampoLayoutEDIService extends CrudService<CampoLayoutEDI, Long> {

	
	@Override
	public CampoLayoutEDI findById(Long id) {		
		return (CampoLayoutEDI)super.findById(id);
	}
	
	@Override
	public Serializable store(CampoLayoutEDI bean) {
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
	
	public CampoLayoutEDIDAO getCampoLayoutEDIDAO() {
        return (CampoLayoutEDIDAO) getDao();
    }
    
    public void setCampoLayoutEDIDAO(CampoLayoutEDIDAO dao) {
        setDao(dao);
    }	
	
	public ResultSetPage<CampoLayoutEDI> findPaginated(PaginatedQuery paginatedQuery) {
		return getCampoLayoutEDIDAO().findPaginated(paginatedQuery);
	}    
	
}
