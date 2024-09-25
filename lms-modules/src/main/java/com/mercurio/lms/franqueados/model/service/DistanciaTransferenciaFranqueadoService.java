package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.DistanciaTransferenciaFranqueado;
import com.mercurio.lms.franqueados.model.dao.DistanciaTransferenciaFranqueadoDAO;

public class DistanciaTransferenciaFranqueadoService extends CrudService<DistanciaTransferenciaFranqueado, Long> {
	
	public List<DistanciaTransferenciaFranqueado> findDistanciaTransferenciaFranqueado(YearMonthDay dtIniCompetencia) {
		return getDistanciaTransferenciaFranqueadoDAO().findDistanciaTransferenciaFranqueado(dtIniCompetencia);
	}
	
	
	private DistanciaTransferenciaFranqueadoDAO getDistanciaTransferenciaFranqueadoDAO() {
		return (DistanciaTransferenciaFranqueadoDAO) getDao();
	}
	
	public void setDistanciaTransferenciaFranqueadoDAO(DistanciaTransferenciaFranqueadoDAO distanciaTransferenciaFranqueadoDAO) {
        setDao(distanciaTransferenciaFranqueadoDAO);
    }

}
