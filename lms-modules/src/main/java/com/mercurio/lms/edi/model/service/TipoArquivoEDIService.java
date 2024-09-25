package com.mercurio.lms.edi.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.TipoArquivoEDI;
import com.mercurio.lms.edi.model.dao.TipoArquivoEDIDAO;


/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.edi.tipoArquivoEDIService"
 */

public class TipoArquivoEDIService extends CrudService<TipoArquivoEDI, Long> {

	
	public List<TipoArquivoEDI> findListExtesaoArquivo(){
		return getTipoArquivoEDIDAO().findAllEntities();
	}
	
    private TipoArquivoEDIDAO getTipoArquivoEDIDAO() {
        return (TipoArquivoEDIDAO) getDao();
    }
    
    public void setTipoArquivoEDIDAO(TipoArquivoEDIDAO dao) {
        setDao(dao);
    }		
		
	
}
