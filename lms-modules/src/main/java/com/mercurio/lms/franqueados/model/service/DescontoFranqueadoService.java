package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.DescontoFranqueado;
import com.mercurio.lms.franqueados.model.dao.DescontoFranqueadoDAO;

public class DescontoFranqueadoService extends CrudService<DescontoFranqueado, Long> {
	
	private DescontoFranqueadoDAO getDescontoFranqueadoDAO() {
		return (DescontoFranqueadoDAO) getDao();
	}
	
	public void setDescontoFranqueadoDAO(DescontoFranqueadoDAO descontoFranqueadoDAO) {
        setDao(descontoFranqueadoDAO);
    }
	
	public List<DescontoFranqueado> findDescontoFranqueadoByCompetencia(YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia){
		return getDescontoFranqueadoDAO().findDescontoFranqueadoByCompetencia(dtIniCompetencia, dtFimCompetencia);
	}
}
