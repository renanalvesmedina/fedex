package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdAtendimentoFilial;
import com.mercurio.lms.ppd.model.dao.PpdAtendimentoFilialDAO;

public class PpdAtendimentoFilialService extends CrudService<PpdAtendimentoFilial, Long> {		
	
	public PpdAtendimentoFilial findById(Long id) {
		return getDAO().findById(id);
	}
	
	public Serializable store(PpdAtendimentoFilial bean) {
		return super.store(bean);		
	}
	
	public List<PpdAtendimentoFilial> findByIdFilial(Long idFilial) {
		return getDAO().findByIdFilial(idFilial);
	}
	
	public void removeById(Long id) {				
		super.removeById(id);
	}

	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);		
	}		
	
	public ResultSetPage<PpdAtendimentoFilial> findPaginated(PaginatedQuery paginatedQuery) {
		return getDAO().findPaginated(paginatedQuery);
	}
	
	public List<Long> findIdsFiliaisByIdGrupoAtendimento(Long idGrupoAtendimento) {
		Map<String,Object> param = new HashMap<String, Object>();			
		param.put("grupoAtendimento.idGrupoAtendimento", idGrupoAtendimento);
		List<PpdAtendimentoFilial> atendimentoFiliais = this.find(param);
		List<Long> ids = new ArrayList<Long>();
		if(atendimentoFiliais != null && atendimentoFiliais.size() > 0) {			
			for(int i=0;i < atendimentoFiliais.size(); i++) {
				ids.add(atendimentoFiliais.get(i).getFilial().getIdFilial());
			}						
		}
		return ids;
	}
		
	//Get e Set do DAO correspondente a esta service
	public void setDAO(PpdAtendimentoFilialDAO dao) {
		setDao(dao);
	}
	
	private PpdAtendimentoFilialDAO getDAO() {
		return (PpdAtendimentoFilialDAO) getDao();		
	}	
}