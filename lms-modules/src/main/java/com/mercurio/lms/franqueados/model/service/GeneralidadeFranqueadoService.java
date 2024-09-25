package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.model.GeneralidadeFranqueado;
import com.mercurio.lms.franqueados.model.dao.GeneralidadeFranqueadoDAO;
import com.mercurio.lms.franqueados.model.dao.GeneralidadeFranqueadoJdbcDAO;

public class GeneralidadeFranqueadoService extends CrudService<GeneralidadeFranqueado, Long> {
	
	private GeneralidadeFranqueadoJdbcDAO generalidadeFranqueadoJdbcDAO;

	public List<TypedFlatMap> findParcelaParticipacao(YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal, String tpFrete, Long idDoctoServico){
		return generalidadeFranqueadoJdbcDAO.findParcelaParticipacao(dataVigenciaInicial, dataVigenciaFinal, tpFrete, idDoctoServico);
	}

	public List<TypedFlatMap> findParcelaParticipacao(YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal){
		return generalidadeFranqueadoJdbcDAO.findParcelaParticipacao(dataVigenciaInicial, dataVigenciaFinal);
	}
	public void setGeneralidadeFranqueadoJdbcDAO(
			GeneralidadeFranqueadoJdbcDAO generalidadeFranqueadoJdbcDAO) {
		this.generalidadeFranqueadoJdbcDAO = generalidadeFranqueadoJdbcDAO;
	}
	
	public void setGeneralidadeFranqueadoDAO(GeneralidadeFranqueadoDAO generalidadeFranqueadoDAO) {
		setDao(generalidadeFranqueadoDAO);
	}

}
