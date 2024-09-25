package com.mercurio.lms.edi.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.ItemNotaFiscalApple;
import com.mercurio.lms.edi.model.dao.ItemNotaFiscalAppleDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.notaFiscalAppleService"
 */

public class ItemNotaFiscalAppleService extends CrudService<ItemNotaFiscalApple, Long> {
	private ItemNotaFiscalAppleDAO getItemNotaFiscalAppleDAO() {
        return (ItemNotaFiscalAppleDAO) getDao();
    }
    
    public void setItemNotaFiscalAppleDAO(ItemNotaFiscalAppleDAO dao) {
        setDao(dao);
    }
    
    public ItemNotaFiscalApple find(Long nrNotaFiscal, Long idInformacaoDoctoCliente, String dsDnn) {
    	return getItemNotaFiscalAppleDAO().find(nrNotaFiscal, idInformacaoDoctoCliente, dsDnn);
    }
    
    public Serializable store(ItemNotaFiscalApple bean) {
    	return super.store(bean);
    }
}
