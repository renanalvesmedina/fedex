package com.mercurio.lms.mocks;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;

public class UnidadeFederativaServiceMocker {
	
	@Mock private UnidadeFederativaService service;
	
	public UnidadeFederativaServiceMocker() {
		initMocks(this);
	}
	
	public UnidadeFederativaService mock(){
		return service;
	}
	
	public UnidadeFederativaServiceMocker findById(Long idUf, String returnedUf){
		UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
		unidadeFederativa.setSgUnidadeFederativa(returnedUf);
		
		when(service.findById(idUf)).thenReturn(unidadeFederativa);
		return this;
	}
	
	public UnidadeFederativaServiceMocker findById(Long idUf, Long idPais){
		UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
		Pais pais = new Pais();
		
		pais.setIdPais(idPais);
		unidadeFederativa.setPais(pais);
		
		when(service.findById(idUf)).thenReturn(unidadeFederativa);
		
		return this;
	}
}
