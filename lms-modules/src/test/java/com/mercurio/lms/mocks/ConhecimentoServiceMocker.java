package com.mercurio.lms.mocks;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;

public class ConhecimentoServiceMocker {
	
	@Mock private ConhecimentoService service;
	
	public ConhecimentoServiceMocker() {
		initMocks(this);
	}
	
	public ConhecimentoService mock(){
		return service;
	}
	
	public ConhecimentoServiceMocker findById(Long id, Conhecimento returnedConhecimento){
		when(service.findById(id)).thenReturn(returnedConhecimento);
		return this;
	}
}
