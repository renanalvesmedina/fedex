package com.mercurio.lms.mocks;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.vendas.model.DivisaoCliente;


public class DoctoServicoServiceMocker {
	
	@Mock private DoctoServicoService doctoServicoService;
	
	public DoctoServicoServiceMocker() {
		initMocks(this);
	}
	
	public DoctoServicoService mock(){
		return doctoServicoService;
	}
	
	public DoctoServicoServiceMocker findDivisaoClienteById(){
		DivisaoCliente divisaoCliente = new DivisaoCliente();
		divisaoCliente.setIdDivisaoCliente(984746L);
		when(doctoServicoService.findDivisaoClienteById(anyLong())).thenReturn(divisaoCliente);
		return this;
	}
	

}
