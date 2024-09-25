package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.LayoutEDI;
import com.mercurio.lms.edi.model.dao.LayoutEDIDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.layoutEDIService"
 */

public class LayoutEDIService extends CrudService<LayoutEDI, Long> {

	
	@Override
	public LayoutEDI findById(Long id) {		
		return getLayoutEDIDAO().findById(id);
	}
	
	@Override
	public Serializable store(LayoutEDI bean) {
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
	
    private LayoutEDIDAO getLayoutEDIDAO() {
        return (LayoutEDIDAO) getDao();
    }
    
    public void setLayoutEDIDAO(LayoutEDIDAO dao) {
        setDao(dao);
    }	
	
	public ResultSetPage<LayoutEDI> findPaginated(PaginatedQuery paginatedQuery) {
		return getLayoutEDIDAO().findPaginated(paginatedQuery);
	}    
	
	@Override
	public LayoutEDI beforeStore(LayoutEDI bean) {
		if(getLayoutEDIDAO().find(bean).isEmpty()){
			return bean;			
		}else{
			throw new BusinessException("Registro já existente!");
		}		
	}
    
	public String findTipoArquivo(Long idLayoutEdi){
		return getLayoutEDIDAO().findTipoArquivo(idLayoutEdi);
}
    
}
