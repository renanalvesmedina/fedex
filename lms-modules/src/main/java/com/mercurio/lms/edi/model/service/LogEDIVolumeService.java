package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.LogEDIDetalhe;
import com.mercurio.lms.edi.model.LogEDIVolume;
import com.mercurio.lms.edi.model.dao.LogEDIVolumeDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.logEDIVolumeService"
 */

public class LogEDIVolumeService extends CrudService<LogEDIVolume, Long> {

	

	@Override
	public LogEDIVolume findById(Long id) {		
		return (LogEDIVolume)super.findById(id);
	}
	
	public ResultSetPage findPaginatedByIdLogDetalhe(TypedFlatMap criteria) {	
		ResultSetPage rsp = getLogEDIVolumeDAO().findPaginatedByIdLogDetalhe(criteria, FindDefinition.createFindDefinition(criteria));
		return rsp;
	}
	public Integer getRowCountByIdLogDetalhe(TypedFlatMap criteria) {	
		return getLogEDIVolumeDAO().getRowCountByIdLogDetalhe(criteria);
	}
	
	@Override
	public Serializable store(LogEDIVolume bean) {
		if(bean.getIdVolume() == null){
			bean.setIdVolume(getLogEDIVolumeDAO().findSequence());
		}
		return super.store(bean);
	}
	
	private LogEDIVolumeDAO getLogEDIVolumeDAO() {
        return (LogEDIVolumeDAO) getDao();
    }
    
    public void setLogEDIItemDAO(LogEDIVolumeDAO dao) {
        setDao(dao);
    }
    
    
    public List<LogEDIVolume> findByLogEDIDetalhe(LogEDIDetalhe logEDIDetalhe){
    	return getLogEDIVolumeDAO().findByLogEDIDetalhe(logEDIDetalhe);
    }

	public LogEDIVolume findByCodigoVolume(LogEDIDetalhe logEDIDetalhe,
			String codigoVolume) {
		return getLogEDIVolumeDAO().findByCodigoVolume(logEDIDetalhe, codigoVolume);
	}
}
