package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.DeParaDetalheEDI;
import com.mercurio.lms.edi.model.dao.DeParaDetalheEDIDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.deParaDetalheEDIService"
 */

public class DeParaDetalheEDIService extends CrudService<DeParaDetalheEDI, Long> {
	
	@Override
	public DeParaDetalheEDI findById(Long id) {		
		return (DeParaDetalheEDI)super.findById(id);
	}
	
	@Override
	public Serializable store(DeParaDetalheEDI bean) {
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
	
	public ResultSetPage<DeParaDetalheEDI> findPaginated(PaginatedQuery paginatedQuery) {
		return getDeParaDetalheEDIDAO().findPaginated(paginatedQuery);
	}    
	
	public DeParaDetalheEDI findByIdDeParaAndDe(Long idDePara, String de){
		return getDeParaDetalheEDIDAO().findByIdDeParaAndDe(idDePara, de);
	}
	
    public DeParaDetalheEDIDAO getDeParaDetalheEDIDAO() {
        return (DeParaDetalheEDIDAO) getDao();
    }
    
    public void setDeParaDetalheEDIDAO(DeParaDetalheEDIDAO dao) {
        setDao(dao);
    }	
}
