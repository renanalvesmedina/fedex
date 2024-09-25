package com.mercurio.lms.mocks;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;

import org.mockito.Mock;

import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;

public class TabelaDivisaoClienteServiceMocker {
	
	@Mock private TabelaDivisaoClienteService tabelaDivisaoClienteService;

	
	public TabelaDivisaoClienteServiceMocker() {
		initMocks(this);
	}
	
	public TabelaDivisaoClienteService mock(){
		return tabelaDivisaoClienteService;
	}
	
	public TabelaDivisaoClienteServiceMocker findByDivisaoCliente(){
		TabelaDivisaoCliente tabelaDivisaoCliente = new TabelaDivisaoCliente();
		tabelaDivisaoCliente.setIdTabelaDivisaoCliente(123456L);
		when(tabelaDivisaoClienteService.findByDivisaoCliente(anyLong())).thenReturn(Arrays.asList(tabelaDivisaoCliente));
		return this;
	}
	
	
}
