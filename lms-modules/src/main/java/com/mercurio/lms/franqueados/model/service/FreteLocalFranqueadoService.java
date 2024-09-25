package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.FreteLocalFranqueado;
import com.mercurio.lms.franqueados.model.dao.FreteLocalFranqueadoDAO;

public class FreteLocalFranqueadoService extends CrudService<FreteLocalFranqueado, Long> {
	
	private FreteLocalFranqueadoDAO getFreteLocalFranqueado() {
		return (FreteLocalFranqueadoDAO) getDao();
	}
	
	public void setFranqueadoDAO(FreteLocalFranqueadoDAO freteLocalFranqueadoDAO) {
        setDao(freteLocalFranqueadoDAO);
    }
	
	public FreteLocalFranqueado findByCompetencia(YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia){
		List<FreteLocalFranqueado> freteLocalFranqueados = getFreteLocalFranqueado().findByCompetencia(dtIniCompetencia, dtFimCompetencia);
		if (freteLocalFranqueados != null && !freteLocalFranqueados.isEmpty()){
			return freteLocalFranqueados.get(0);
		}
		return null;
	}

}
