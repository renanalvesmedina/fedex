package com.mercurio.lms.edi.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.edi.model.dao.NotaFiscalEdiItemDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.notaFiscalEDIItemService"
 */

public class NotaFiscalEDIItemService extends CrudService<NotaFiscalEdiItem, Long> {
	@Override
	public Serializable store(NotaFiscalEdiItem bean) {
		if(bean.getIdNotaFiscalEdiItem() == null){
			bean.setIdNotaFiscalEdiItem(getNotaFiscalEdiItemDAO().findSequence());
		}
		return super.store(bean);
	}
	private NotaFiscalEdiItemDAO getNotaFiscalEdiItemDAO() {
        return (NotaFiscalEdiItemDAO) getDao();
    }
    
    public void setNotaFiscalEdiItemDAO(NotaFiscalEdiItemDAO dao) {
        setDao(dao);
    }
}
