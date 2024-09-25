package com.mercurio.lms.edi.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;

import com.mercurio.lms.edi.model.LogArquivoEDI;

import com.mercurio.lms.edi.model.dao.LogArquivoEDIDAO;


/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
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
