package com.mercurio.lms.sim.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.LayoutEDI;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.sim.model.dao.FaseProcessoDAO;

public class FaseProcessoService extends CrudService<FaseProcesso, Long> {
	
	@Override
	public Serializable store(FaseProcesso bean) {
		return super.store(bean);
	}
	
	@Override
	public FaseProcesso beforeStore(FaseProcesso bean) {
		if(getFaseProcessoDAO().validateBean(bean)) {
			return bean;
		} else {
			throw new BusinessException("LMS-10079");
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
	
	@Override
	public FaseProcesso findById(Long id) {		
		return getFaseProcessoDAO().findById(id);
	}
	
	public List<FaseProcesso> findByCdFaseProcesso(FaseProcesso fp) {
		return  getFaseProcessoDAO().find(fp);
	}
	
	public FaseProcesso findByIdDoctoServico(Long idDoctoServico) {
		return getFaseProcessoDAO().findByIdDoctoServico(idDoctoServico);
	}
	
	private FaseProcessoDAO getFaseProcessoDAO() {
		return (FaseProcessoDAO) getDao();
	}
	
	public void setFaseProcessoDAO(FaseProcessoDAO dao) {
		setDao(dao);
	}
	
}
