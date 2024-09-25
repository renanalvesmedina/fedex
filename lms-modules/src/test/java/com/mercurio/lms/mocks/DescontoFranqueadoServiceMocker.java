package com.mercurio.lms.mocks;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.mockito.Mock;

import com.mercurio.lms.franqueados.model.service.DescontoFranqueadoService;

public class DescontoFranqueadoServiceMocker {
	
	@Mock private DescontoFranqueadoService descontoFranqueadoService;
	
	public DescontoFranqueadoServiceMocker() {
		initMocks(this);
	}
	
	public DescontoFranqueadoService mock(){
		return descontoFranqueadoService;
	}

}
