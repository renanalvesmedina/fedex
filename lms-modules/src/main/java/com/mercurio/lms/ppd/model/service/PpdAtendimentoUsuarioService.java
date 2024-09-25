package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdAtendimentoUsuario;
import com.mercurio.lms.ppd.model.dao.PpdAtendimentoUsuarioDAO;

public class PpdAtendimentoUsuarioService extends CrudService<PpdAtendimentoUsuario, Long> {		
	
	public PpdAtendimentoUsuario findById(Long id) {
		return getDAO().findById(id);
	}
	
	public Serializable store(PpdAtendimentoUsuario bean) {
		return super.store(bean);		
	}
	
	public List<PpdAtendimentoUsuario> findByIdGrupoAtendimento(Long idGrupoAtendimento) {
		return getDAO().findByIdGrupoAtendimento(idGrupoAtendimento);
	}
	
	public void removeById(Long id) {				
		super.removeById(id);
	}
	
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);		
	}		
	
	public ResultSetPage<PpdAtendimentoUsuario> findPaginated(PaginatedQuery paginatedQuery) {
		return getDAO().findPaginated(paginatedQuery);
	}
	
	
	//Get e Set do DAO correspondente a esta service 
	public void setDAO(PpdAtendimentoUsuarioDAO dao) {
		setDao(dao);
	}
	
	private PpdAtendimentoUsuarioDAO getDAO() {
		return (PpdAtendimentoUsuarioDAO) getDao();		
	}	
}