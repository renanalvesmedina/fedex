package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.LimiteParticipacaoFranqueado;
import com.mercurio.lms.franqueados.model.dao.LimiteParticipacaoFranqueadoDAO;

public class LimiteParticipacaoFranqueadoService extends CrudService<LimiteParticipacaoFranqueado, Long> {
	
	private LimiteParticipacaoFranqueadoDAO limiteParticipacaoFranqueadoDAO;

	public List<LimiteParticipacaoFranqueado> findLimiteParticipacao(YearMonthDay dtInicioCompetencia) {
		return limiteParticipacaoFranqueadoDAO.findLimiteParticipacao(dtInicioCompetencia);
	}

	public void setLimiteParticipacaoFranqueadoDAO(
			LimiteParticipacaoFranqueadoDAO limiteParticipacaoFranqueadoDAO) {
		this.limiteParticipacaoFranqueadoDAO = limiteParticipacaoFranqueadoDAO;
	}
	
}
