package com.mercurio.lms.facade.radar;

import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Adércio Reinan
 */
public interface UsuarioFacade {

	public TypedFlatMap findUsuarioClienteByLogin(TypedFlatMap map);

	String findRecursosMensagem(TypedFlatMap map);
}
