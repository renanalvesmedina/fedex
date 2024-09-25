package com.mercurio.lms.mocks;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.adsm.framework.util.TypedFlatMap;

public class TypedFlatMapMocker {

	
	@Mock private TypedFlatMap typedFlatMap;
	
	public TypedFlatMapMocker() {
		initMocks(this);
	}
	
	public TypedFlatMap mock(){
		return typedFlatMap;
	}
	
	public TypedFlatMapMocker findLongById(String key, Long retorno){
		when(typedFlatMap.getLong(key)).thenReturn(retorno);
		return this;
	}
}
