package com.mercurio.lms.mocks;

import java.math.BigDecimal;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;

public class ServicoAdicionalClienteServiceMocker {
	
	@Mock private ServicoAdicionalClienteService servicoAdicionalClienteService;
	
	public ServicoAdicionalClienteServiceMocker() {
		initMocks(this);
	}
	
	public ServicoAdicionalClienteService mock(){
		return servicoAdicionalClienteService;
	}
	
	public ServicoAdicionalClienteServiceMocker findServicoAdicionalCliente(Integer quantidadeDias, int decursoPrazo, String tpIndicador, int vlValor) {
		ServicoAdicionalCliente servicoAdicionalCliente = new ServicoAdicionalCliente();
		servicoAdicionalCliente.setNrQuantidadeDias(quantidadeDias);
		servicoAdicionalCliente.setNrDecursoPrazo(decursoPrazo);
		servicoAdicionalCliente.setTpIndicador(new DomainValue(tpIndicador));
		servicoAdicionalCliente.setVlValor(new BigDecimal(vlValor));
		when(servicoAdicionalClienteService.findServicoAdicionalCliente(anyLong(), anyLong())).thenReturn(servicoAdicionalCliente);
		return this;
	}
	
	public ServicoAdicionalClienteServiceMocker findServicoAdicionalClienteReturingNull(){
		when(servicoAdicionalClienteService.findServicoAdicionalCliente(anyLong(), anyLong())).thenReturn(null);
		return this;
	}
	
	

}
