package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.MunicipioNaoAtendidoFranqueado;
import com.mercurio.lms.franqueados.model.dao.MunicipioNaoAtendidoFranqueadoDAO;
import com.mercurio.lms.franqueados.model.dao.MunicipioNaoAtendidoFranqueadoJdbcDAO;

public class MunicipioNaoAtendidoFranqueadoService extends CrudService<MunicipioNaoAtendidoFranqueado, Long> {
	
	private MunicipioNaoAtendidoFranqueadoJdbcDAO municipioNaoAtendidoFranqueadoJdbcDAO;
	
	private MunicipioNaoAtendidoFranqueadoDAO getMunicipioNaoAtendidoFranqueadoDAO() {
		return (MunicipioNaoAtendidoFranqueadoDAO) getDao();
	}
	
	public void setMunicipioNaoAtendidoFranqueadoDAO(MunicipioNaoAtendidoFranqueadoDAO municipioNaoAtendidoFranqueadoDAO) {
        setDao(municipioNaoAtendidoFranqueadoDAO);
    }

	public MunicipioNaoAtendidoFranqueado findMunicipioVigenciaByIdFranquia(Long idFranquia, Long idMunicipio, YearMonthDay dtInicioCompetencia) {
		return getMunicipioNaoAtendidoFranqueadoDAO().findMunicipioVigenciaByIdFranquia(idFranquia, idMunicipio, dtInicioCompetencia);
	}
	
	public MunicipioNaoAtendidoFranqueado findMunicipioByJdbc(Long idFranquia, Long idMunicipio, YearMonthDay dtInicioCompetencia) {
		return municipioNaoAtendidoFranqueadoJdbcDAO.findMunicipioByJdbc(idFranquia, idMunicipio, dtInicioCompetencia);
	}

	public List<MunicipioNaoAtendidoFranqueado> findMunicipioByJdbc(Long idFranquia, YearMonthDay dtInicioCompetencia) {
		return municipioNaoAtendidoFranqueadoJdbcDAO.findMunicipioByJdbc(idFranquia, dtInicioCompetencia);
	}

	public void setMunicipioNaoAtendidoFranqueadoJdbcDAO(MunicipioNaoAtendidoFranqueadoJdbcDAO municipioNaoAtendidoFranqueadoJdbcDAO) {
		this.municipioNaoAtendidoFranqueadoJdbcDAO = municipioNaoAtendidoFranqueadoJdbcDAO;
	}
	
}
