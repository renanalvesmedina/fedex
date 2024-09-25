package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdNaturezaProduto;
import com.mercurio.lms.ppd.model.dao.PpdNaturezaProdutoDAO;

public class PpdNaturezaProdutoService extends CrudService<PpdNaturezaProduto, Long> {		
	
	public PpdNaturezaProduto findById(Long id) {
		return getDAO().findById(id); 
	}
	
	public List findCombo(Map<String,Object> criteria) {
		List<String> order = new ArrayList<String>();
		order.add("dsNaturezaProduto:asc");
		return getDAO().findListByCriteria(criteria, order);
	}
	
	public Serializable store(PpdNaturezaProduto bean) {
		return super.store(bean);		
	}	
	
	public void removeById(Long id) {				
		super.removeById(id);
	}

	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);		
	}		
	
	public ResultSetPage<PpdNaturezaProduto> findPaginated(PaginatedQuery paginatedQuery) {
		return getDAO().findPaginated(paginatedQuery);
	}
	
	public List<Map<String, Object>> findLookup(String dsNaturezaProduto) {
		return getDAO().findLookup(dsNaturezaProduto);
	}
	
	//Get e Set do DAO correspondente a esta service
	public void setDAO(PpdNaturezaProdutoDAO dao) {
		setDao(dao);
	}
	
	private PpdNaturezaProdutoDAO getDAO() {
		return (PpdNaturezaProdutoDAO) getDao();		
	}	
}