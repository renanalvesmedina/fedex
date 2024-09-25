package com.mercurio.lms.franqueados.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.AnexoLancamentoFranqueado;
import com.mercurio.lms.franqueados.model.dao.AnexoLancamentoFranqueadoDAO;

public class AnexoLancamentoFranqueadoService  extends CrudService<AnexoLancamentoFranqueado, Long> {

	
	public AnexoLancamentoFranqueadoDAO getAnexoLancamentoFranqueadoDAO() {
		return (AnexoLancamentoFranqueadoDAO) getDao();
	}

	public void setAnexoLancamentoFranqueadoDAO(AnexoLancamentoFranqueadoDAO anexoLancamentoFranqueado) {
		setDao(anexoLancamentoFranqueado);
	}
	
	public AnexoLancamentoFranqueado findById(Long id) {
        return getAnexoLancamentoFranqueadoDAO().findById(id);
    }
	
	public List findByLancamentoFrq(Long idLancamento) {
        return getAnexoLancamentoFranqueadoDAO().findByLancamentoFrq(idLancamento);
    }
	
	public void removeByIds(List ids){
		super.removeByIds(ids);
	}
	
	@Override
	public Serializable store(AnexoLancamentoFranqueado anexoLancamentoFranqueado){
		return super.store(anexoLancamentoFranqueado);
	}
}
