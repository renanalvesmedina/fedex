package com.mercurio.lms.pendencia.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.pendencia.model.FaseOcorrencia;
import com.mercurio.lms.pendencia.model.dao.FaseOcorrenciaDAO;

public class FaseOcorrenciaService extends CrudService<FaseOcorrencia, Long> {
	
	@Override
	public Serializable store(FaseOcorrencia bean) {
		return super.store(bean);
	}
	
	@Override
	public FaseOcorrencia beforeStore(FaseOcorrencia bean) {
		if(getFaseOcorrenciaDAO().find(bean).isEmpty()){
			return bean;			
		}else{
			throw new BusinessException("LMS-17047");
		}		
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
	@Override
	public void removeById(Long id) {		
		super.removeById(id);
	}
	
	public ResultSetPage<FaseOcorrencia> findPaginated(PaginatedQuery paginatedQuery) {
		return getFaseOcorrenciaDAO().findPaginated(paginatedQuery);
	} 
	
	@Override
	public FaseOcorrencia findById(Long id) {		
		return getFaseOcorrenciaDAO().findById(id);
	}
	
	private FaseOcorrenciaDAO getFaseOcorrenciaDAO() {
		return (FaseOcorrenciaDAO) getDao();
	}
	
	public void setFaseOcorrenciaDAO(FaseOcorrenciaDAO dao) {
		setDao(dao);
	}
}