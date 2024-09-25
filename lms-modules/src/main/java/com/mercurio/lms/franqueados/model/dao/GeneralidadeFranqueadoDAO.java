package com.mercurio.lms.franqueados.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.GeneralidadeFranqueado;

public class GeneralidadeFranqueadoDAO extends BaseCrudDao<GeneralidadeFranqueado, Long> {

	@Override
	protected Class<GeneralidadeFranqueado> getPersistentClass() {
		return GeneralidadeFranqueado.class;
	}
}
