package com.mercurio.lms.mocks;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class ClienteServiceMocker {
	
	@Mock private ClienteService service;
	
	public ClienteServiceMocker() {
		initMocks(this);
	}
	
	public ClienteService mock(){
		return service;
	}
	
	public ClienteServiceMocker findByIdComPessoa(Long id, Cliente cliente) {
		when(service.findByIdComPessoa(id)).thenReturn(cliente);
		return this;
	}
	
	public ClienteServiceMocker findById(Cliente cliente) {
		when(service.findById(anyLong())).thenReturn(cliente);
		return this;
	}
	
	
}
