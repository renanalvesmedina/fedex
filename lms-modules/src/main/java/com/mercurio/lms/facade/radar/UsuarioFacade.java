package com.mercurio.lms.facade.radar;

import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Ad�rcio Reinan
 */
public interface UsuarioFacade {

	public TypedFlatMap findUsuarioClienteByLogin(TypedFlatMap map);

	String findRecursosMensagem(TypedFlatMap map);
}
