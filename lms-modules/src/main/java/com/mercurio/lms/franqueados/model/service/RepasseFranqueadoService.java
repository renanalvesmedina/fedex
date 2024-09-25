package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.RepasseFranqueado;
import com.mercurio.lms.franqueados.model.dao.RepasseFranqueadoDAO;

public class RepasseFranqueadoService extends CrudService<RepasseFranqueado, Long> {

	private RepasseFranqueadoDAO getRepasseFranqueadoDAO() {
		return (RepasseFranqueadoDAO) getDao();
	}

	public void setRepasseFranqueadoDAO(RepasseFranqueadoDAO repasseFranqueadoDAO) {
		setDao(repasseFranqueadoDAO);
	}

	public RepasseFranqueado findRepasseFranqueadoByCompetencia(YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia) {
		List<RepasseFranqueado> repasseFranqueados = getRepasseFranqueadoDAO().findRepasseFranqueado(dtIniCompetencia, dtFimCompetencia);
		
		if (repasseFranqueados != null && !repasseFranqueados.isEmpty()){
			return repasseFranqueados.get(0);
		}	
		
		return null;				
	}
	
	public RepasseFranqueado findRepasseFranqueadoJdbc(YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia) {
		List<RepasseFranqueado> repasseFranqueados = getRepasseFranqueadoDAO().findRepasseFranqueado(dtIniCompetencia, dtFimCompetencia);
		
		if (repasseFranqueados != null && !repasseFranqueados.isEmpty()){
			return repasseFranqueados.get(0);
		}	
		
		return null;				
	}
}
