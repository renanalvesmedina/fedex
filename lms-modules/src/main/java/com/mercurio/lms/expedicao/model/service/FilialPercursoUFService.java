package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.FilialPercursoUF;
import com.mercurio.lms.expedicao.model.dao.FilialPercursoUFDAO;

public class FilialPercursoUFService extends CrudService<FilialPercursoUF, Long> {
	
	
	public void setMonitoramentoDocEletronicoDAO(FilialPercursoUFDAO dao) {
		setDao( dao );
	}

	private FilialPercursoUFDAO getFilialPercursoUFDao() {
		return (FilialPercursoUFDAO) getDao();
	}

	public List<FilialPercursoUF> findFiliaisPercursoByIdFilialOrigemAndIdFilialDestino(Long idFilialOrigem, Long idFilialDestino) {
		return getFilialPercursoUFDao().findFiliaisPercursoByIdFilialOrigemAndIdFilialDestino(idFilialOrigem, idFilialDestino);
	}

}