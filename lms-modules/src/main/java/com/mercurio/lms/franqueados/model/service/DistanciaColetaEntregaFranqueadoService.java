package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.DistanciaColetaEntregaFranqueado;
import com.mercurio.lms.franqueados.model.dao.DistanciaColetaEntregaFranqueadoDAO;

public class DistanciaColetaEntregaFranqueadoService extends CrudService<DistanciaColetaEntregaFranqueado, Long> {

	public List<DistanciaColetaEntregaFranqueado> findDistanciaColetaEntregaFranqueado(YearMonthDay dtIniCompetencia) {
		return getDistanciaColetaEntregaFranqueadoDAO().findDistanciaColetaEntregaFranqueado(dtIniCompetencia); 
	}
	
	
	private DistanciaColetaEntregaFranqueadoDAO getDistanciaColetaEntregaFranqueadoDAO() {
		return (DistanciaColetaEntregaFranqueadoDAO) getDao();
	}
	
	public void setDistanciaColetaEntregaFranqueadoDAO(DistanciaColetaEntregaFranqueadoDAO distanciaColetaEntregaFranqueadoDAO) {
        setDao(distanciaColetaEntregaFranqueadoDAO);
    }

}
