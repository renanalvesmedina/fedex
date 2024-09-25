package com.mercurio.lms.tracking.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tracking.model.RadarSolicitacaoUsuario;

public class RadarSolicitacaoUsuarioDAO extends BaseCrudDao<RadarSolicitacaoUsuario, Long> {

	@Override
	public Class getPersistentClass() {
		return RadarSolicitacaoUsuario.class;
	}

	public RadarSolicitacaoUsuario findById(Long id) {
		return (RadarSolicitacaoUsuario) super.findById(id);
	}
	
}
