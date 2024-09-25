package com.mercurio.lms.mocks;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.lms.vendas.model.service.ParametroClienteService;

public class ParametroClienteServiceMocker {
	
	@Mock private ParametroClienteService parametroClienteService;

	public ParametroClienteServiceMocker() {
		initMocks(this);
	}
	
	public ParametroClienteService mock(){
		return parametroClienteService;
	}
	
	public ParametroClienteServiceMocker existParametroByIdProposta(boolean answer){
		when(parametroClienteService.existParametroByIdProposta(anyLong())).thenReturn(answer);
		return this;
	}
	
	public ParametroClienteServiceMocker existParametroByIdSimulacao(boolean answer){
		when(parametroClienteService.existParametroByIdSimulacao(anyLong())).thenReturn(answer);
		return this;
	}
}
