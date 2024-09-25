package com.mercurio.lms.facade.radar;

import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Celso Martins
 */
public interface DoctoServicoFacade {
	
	TypedFlatMap findDetailImage(TypedFlatMap criteria);
	
	TypedFlatMap reemitirCteRps(TypedFlatMap criteria);
	
}

