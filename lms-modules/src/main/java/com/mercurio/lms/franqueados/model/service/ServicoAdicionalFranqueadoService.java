package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.ServicoAdicionalFranqueado;
import com.mercurio.lms.franqueados.model.dao.ServicoAdicionalFranqueadoDAO;

public class ServicoAdicionalFranqueadoService extends CrudService<ServicoAdicionalFranqueado, Long> {
	
	private ServicoAdicionalFranqueadoDAO getServicoAdicionalFranqueadoDAO() {
		return (ServicoAdicionalFranqueadoDAO) getDao();
	}
	
	public void setServicoAdicionalFranqueadoDAO(ServicoAdicionalFranqueadoDAO servicoAdicionalFranqueadoDAO) {
        setDao(servicoAdicionalFranqueadoDAO);
    }

	public ServicoAdicionalFranqueado findByIdServicoAdicional(Long idServicoAdicional, YearMonthDay dtInicioCompetencia) {
		return getServicoAdicionalFranqueadoDAO().findByIdServicoAdicional(idServicoAdicional, dtInicioCompetencia);
	}

	public List<ServicoAdicionalFranqueado> findByIdServicoAdicional(YearMonthDay dtInicioCompetencia) {
		return getServicoAdicionalFranqueadoDAO().findByIdServicoAdicional(dtInicioCompetencia);
	}
}
