package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdGrupoAtendimento;
import com.mercurio.lms.ppd.model.dao.PpdGrupoAtendimentoDAO;

public class PpdGrupoAtendimentoService extends CrudService<PpdGrupoAtendimento, Long> {			
	public PpdGrupoAtendimento findById(Long id) {
		return getDAO().findById(id);
	}
	
	public Serializable store(PpdGrupoAtendimento bean) {
		return super.store(bean);		
	}	
		
	public void removeById(Long id) {				
		super.removeById(id);
	}
	
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);		
	}		
	
	public ResultSetPage<PpdGrupoAtendimento> findPaginated(PaginatedQuery paginatedQuery) {
		return getDAO().findPaginated(paginatedQuery);
	}
	
	public List<Map<String, Object>> findLookup(String dsGrupoAtendimento) {
		return getDAO().findLookup(dsGrupoAtendimento);
	}
	
	//Get e Set do DAO correspondente a esta service
	public void setDAO(PpdGrupoAtendimentoDAO dao) {
		setDao(dao);
	}
	
	private PpdGrupoAtendimentoDAO getDAO() {
		return (PpdGrupoAtendimentoDAO) getDao();		
	}	
}