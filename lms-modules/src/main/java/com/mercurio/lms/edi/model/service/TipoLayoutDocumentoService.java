package com.mercurio.lms.edi.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.TipoLayoutDocumento;
import com.mercurio.lms.edi.model.dao.TipoLayoutDocumentoDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.tipoLayoutDocumentoService"
 */
public class TipoLayoutDocumentoService extends CrudService<TipoLayoutDocumento, Long> {
	
	public List<TipoLayoutDocumento> findListTpLayoutDocumento(){
		return getTipoLayoutDocumentoDAO().findAllEntities();
	}
			
    private TipoLayoutDocumentoDAO getTipoLayoutDocumentoDAO() {
        return (TipoLayoutDocumentoDAO) getDao();
    }
    
    public void setTipoLayoutDocumentoDAO(TipoLayoutDocumentoDAO dao) {
        setDao(dao);
    }		
	
}
