package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.DiaCorteFaturamento;
import com.mercurio.lms.contasreceber.model.dao.DiaCorteFaturamentoDAO;

public class DiaCorteFaturamentoService extends CrudService<DiaCorteFaturamento, Long> {
	
	private DiaCorteFaturamentoDAO diaCorteFaturamentoDAO;
    
	public Serializable store(DiaCorteFaturamento entity) {
		return super.store(entity);
	}
	
	public DiaCorteFaturamento findDiaCorteById(Long id){
		return diaCorteFaturamentoDAO.findDiaCorteById(id);
	}
	
	public List<DiaCorteFaturamento> findAll(TypedFlatMap filtro) {
		return diaCorteFaturamentoDAO.findAll(filtro);
	}
	
	public void setExcecoesClienteFinanceiroDAO(DiaCorteFaturamentoDAO diaCorteFaturamentoDAO) {
		this.diaCorteFaturamentoDAO = diaCorteFaturamentoDAO;
		setDao( diaCorteFaturamentoDAO );
	}
	
}