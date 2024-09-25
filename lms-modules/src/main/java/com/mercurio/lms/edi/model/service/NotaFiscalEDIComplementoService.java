package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.NotaFiscalEdiComplemento;
import com.mercurio.lms.edi.model.dao.NotaFiscalEdiComplementoDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.notaFiscalEDIComplementoService"
 */

public class NotaFiscalEDIComplementoService extends CrudService<NotaFiscalEdiComplemento, Long> {

	@Override
	public Serializable store(NotaFiscalEdiComplemento bean) {
		if(bean.getIdNotaFiscalEdiComplemento() == null){
			bean.setIdNotaFiscalEdiComplemento(getNotaFiscalEdiComplementoDAO().findSequence());
		}
		return super.store(bean);
	}

	public NotaFiscalEdiComplemento findByIdInformacaoDocClienteAndIdNotaFiscalEdi(Long idInformacaoDoctoCliente,Long idNotaFiscalEdi) {
		return getNotaFiscalEdiComplementoDAO().findByIdInformacaoDocClienteAndIdNotaFiscalEdi(idInformacaoDoctoCliente, idNotaFiscalEdi);
	}
	
	public NotaFiscalEdiComplemento findByIdInformacaoDocClienteAndValorCompl(Long idInformacaoDoctoCliente, String valorComplemento) {
		return getNotaFiscalEdiComplementoDAO().findByIdInformacaoDocClienteAndValorCompl(idInformacaoDoctoCliente, valorComplemento);
	}
	
	private NotaFiscalEdiComplementoDAO getNotaFiscalEdiComplementoDAO() {
        return (NotaFiscalEdiComplementoDAO) getDao();
    }
    
	public List findDadosComplementos(List listIdNotaFiscalEdi) {
		return getNotaFiscalEdiComplementoDAO().findDadosComplementos(listIdNotaFiscalEdi);
	}
	
    public void setNotaFiscalEdiComplementoDAO(NotaFiscalEdiComplementoDAO dao) {
        setDao(dao);
    }
    
    // LMSA-6520
    public void removeByIdNotaFiscalEdi(Long id) {
        List<Long> ids = new ArrayList<Long>();
        ids.add(id);
        getNotaFiscalEdiComplementoDAO().removeByIdNotaFiscalEdi(ids);
    }
    
    
}
