package com.mercurio.lms.mocks;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.Proposta;
import com.mercurio.lms.vendas.model.service.PropostaService;

public class PropostaServiceMocker {
	
	@Mock private PropostaService propostaService;
	
	public PropostaServiceMocker() {
		initMocks(this);
	}
	
	public PropostaService mock(){
		return propostaService;
	}
	
	public PropostaServiceMocker findByIdSimulacao(){
		when(propostaService.findByIdSimulacao(anyLong())).thenReturn(createProposta());
		return this;
	}
	
	public PropostaServiceMocker findByIdSimulacaoReturningNull(){
		when(propostaService.findByIdSimulacao(anyLong())).thenReturn(null);
		return this;
	}
	
	private Proposta createProposta(){
		Proposta proposta = new Proposta();
		proposta.setIdProposta(123456L);
		proposta.setParametroCliente(new ParametroCliente());
		return proposta;
	}

}
