package com.mercurio.lms.edi.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;

import com.mercurio.lms.edi.model.LogArquivoEDI;

import com.mercurio.lms.edi.model.dao.LogArquivoEDIDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.logArquivoEDIService"
 */

public class LogArquivoEDIService extends CrudService<LogArquivoEDI, Long> {

	
	@Override
	public LogArquivoEDI findById(Long id) {		
		return (LogArquivoEDI)super.findById(id);
	}
	
	
	
	@Override
	public Serializable store(LogArquivoEDI bean) {
		return super.store(bean);
	}
	
	private LogArquivoEDIDAO getLogArquivoEDIDAO() {
        return (LogArquivoEDIDAO) getDao();
    }
    
    public void setLogArquivoEDIDAO(LogArquivoEDIDAO dao) {
        setDao(dao);
    }	
	
}
