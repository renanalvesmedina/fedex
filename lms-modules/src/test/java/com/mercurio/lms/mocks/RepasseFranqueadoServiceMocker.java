package com.mercurio.lms.mocks;

import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.lms.franqueados.model.service.RepasseFranqueadoService;

public class RepasseFranqueadoServiceMocker {
	
	@Mock private RepasseFranqueadoService repasseFranqueadoService;
	
	public RepasseFranqueadoServiceMocker() {
		initMocks(this);
	}
	
	public RepasseFranqueadoService mock(){
		return repasseFranqueadoService;
	}
	
	

}
