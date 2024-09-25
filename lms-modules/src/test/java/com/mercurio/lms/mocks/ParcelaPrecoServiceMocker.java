package com.mercurio.lms.mocks;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;

public class ParcelaPrecoServiceMocker {
	
	@Mock private ParcelaPrecoService parcelaPrecoService;
	
	public ParcelaPrecoServiceMocker() {
		initMocks(this);
	}
	
	public ParcelaPrecoService mock(){
		return parcelaPrecoService;
	}
	
	public ParcelaPrecoServiceMocker findByCdParcelaPreco(String cdParcelaPreco, long idParcelaPreco){
		ParcelaPreco parcelaPreco = new ParcelaPreco();
		parcelaPreco.setIdParcelaPreco(123456L);
		when(parcelaPrecoService.findByCdParcelaPreco(cdParcelaPreco)).thenReturn(parcelaPreco);
		return this;
	}
	
}
