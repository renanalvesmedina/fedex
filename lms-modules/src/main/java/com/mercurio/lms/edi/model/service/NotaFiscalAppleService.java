package com.mercurio.lms.edi.model.service;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.NotaFiscalApple;
import com.mercurio.lms.edi.model.dao.NotaFiscalAppleDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.notaFiscalAppleService"
 */

public class NotaFiscalAppleService extends CrudService<NotaFiscalApple, Long> {
	private NotaFiscalAppleDAO getNotaFiscalAppleDAO() {
        return (NotaFiscalAppleDAO) getDao();
    }
    
    public void setNotaFiscalAppleDAO(NotaFiscalAppleDAO dao) {
        setDao(dao);
    }
    
    public Serializable store(NotaFiscalApple bean) {
    	return super.store(bean);
    }
    
    public NotaFiscalApple find(Long nrNotaFiscal, String nrSerie, YearMonthDay dtEmissao) {
    	return this.getNotaFiscalAppleDAO().find(nrNotaFiscal, nrSerie, dtEmissao);
    }
}
