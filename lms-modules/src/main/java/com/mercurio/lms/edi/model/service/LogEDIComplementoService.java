package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.LogEDIComplemento;
import com.mercurio.lms.edi.model.LogEDIDetalhe;
import com.mercurio.lms.edi.model.dao.LogEDIComplementoDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.logEDIComplementoService"
 */

public class LogEDIComplementoService extends CrudService<LogEDIComplemento, Long> {

	
	public String findVlComplementoByNmComplmentoEChaveNfe(String nomeComplemento, String chaveNfe){
		return getLogEDIComplementoDAO().findVlComplementoByNmComplmentoEChaveNfe(nomeComplemento, chaveNfe);
	}
	
	@Override
	public LogEDIComplemento findById(Long id) {		
		return (LogEDIComplemento)super.findById(id);
	}
	
	public ResultSetPage findPaginatedByIdLogDetalhe(TypedFlatMap criteria) {	
		ResultSetPage rsp = getLogEDIComplementoDAO().findPaginatedByIdLogDetalhe(criteria, FindDefinition.createFindDefinition(criteria));
		return rsp;
	}
	public Integer getRowCountByIdLogDetalhe(TypedFlatMap criteria) {	
		return getLogEDIComplementoDAO().getRowCountByIdLogDetalhe(criteria);
	}
	
	@Override
	public Serializable store(LogEDIComplemento bean) {
		if(bean.getIdComplemento() == null){
			bean.setIdComplemento(getLogEDIComplementoDAO().findSequence());
		}
		return super.store(bean);
	}
	
	private LogEDIComplementoDAO getLogEDIComplementoDAO() {
        return (LogEDIComplementoDAO) getDao();
    }
    
    public void setLogEDIComplementoDAO(LogEDIComplementoDAO dao) {
        setDao(dao);
    }	
    
    public List<LogEDIComplemento> findByLogEDIDetalhe(LogEDIDetalhe logEDIDetalhe){
    	return getLogEDIComplementoDAO().findByLogEDIDetalhe(logEDIDetalhe);
    }
}
