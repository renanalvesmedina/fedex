package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ExcecoesClienteFinanceiro;
import com.mercurio.lms.contasreceber.model.dao.ExcecoesClienteFinanceiroDAO;

public class ExcecoesClienteFinanceiroService extends CrudService<ExcecoesClienteFinanceiro, Long> {
	
	private ExcecoesClienteFinanceiroDAO excecoesClienteFinanceiroDAO;
    
	public Serializable store(ExcecoesClienteFinanceiro entity) {
		return super.store(entity);
	}
	
	public List<ExcecoesClienteFinanceiro> findAll(TypedFlatMap filtro) {
		return excecoesClienteFinanceiroDAO.findAll(filtro);
	}
	
	public ExcecoesClienteFinanceiro findExcecaoById(Long id) {
		return excecoesClienteFinanceiroDAO.findExcecaoById(id);
	}
	
	public void setExcecoesClienteFinanceiroDAO(ExcecoesClienteFinanceiroDAO excecoesClienteFinanceiroDAO) {
		this.excecoesClienteFinanceiroDAO = excecoesClienteFinanceiroDAO;
		setDao( excecoesClienteFinanceiroDAO );
	}

 }