package com.mercurio.lms.facade.radar;

import com.mercurio.adsm.framework.util.TypedFlatMap;

public interface WhiteListFacade {
	
	TypedFlatMap storeWhiteList(TypedFlatMap criteria);
	
	TypedFlatMap updateStatusWhiteList(TypedFlatMap criteria);
	
	TypedFlatMap executeSendWhiteListStatusAtual(TypedFlatMap criteria);
	
	TypedFlatMap disableWhiteList(TypedFlatMap criteria);
}
