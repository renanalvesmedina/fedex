package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.ReembarqueFranqueado;
import com.mercurio.lms.franqueados.model.dao.ReembarqueFranqueadoDAO;

public class ReembarqueFranqueadoService extends CrudService<ReembarqueFranqueado, Long> {
	
	private ReembarqueFranqueadoDAO getReembarqueFranqueadoDAO() {
		return (ReembarqueFranqueadoDAO) getDao();
	}
	
	public void setReembarqueFranqueadoDAO(ReembarqueFranqueadoDAO reembarqueFranqueadoDAO) {
        setDao(reembarqueFranqueadoDAO);
    }
	
	public ReembarqueFranqueado findReembarqueFranqueadoByDtVigenciaInicioFim(YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia){
		List<ReembarqueFranqueado> reembarqueFranqueadoList = getReembarqueFranqueadoDAO().findReembarqueFranqueadoByDtVigenciaInicioFim(dtIniCompetencia, dtFimCompetencia); 
		
		if (reembarqueFranqueadoList != null && !reembarqueFranqueadoList.isEmpty()){
			return reembarqueFranqueadoList.get(0);
		}
		
		return null;
	}

}
