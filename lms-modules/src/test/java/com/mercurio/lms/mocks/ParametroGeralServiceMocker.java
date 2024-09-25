package com.mercurio.lms.mocks;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;

import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;

public class ParametroGeralServiceMocker {
	
	@Mock private ParametroGeralService parametroGeralService;
	
	public ParametroGeralServiceMocker() {
		initMocks(this);
	}
	
	public ParametroGeralService mock(){
		return parametroGeralService;
	}
	
	public ParametroGeralServiceMocker findByNomeParametro(String parametro, String retorno){
		ParametroGeral pg = new ParametroGeral();
		pg.setNmParametroGeral(parametro);
		pg.setDsConteudo(retorno);
		when(parametroGeralService.findByNomeParametro(parametro)).thenReturn(pg);
		return this;
	}
	
	public ParametroGeralServiceMocker findConteudoByNomeParametro(String parametro, String retorno){
		when(parametroGeralService.findConteudoByNomeParametro(parametro, false)).thenReturn(retorno);
		return this;
	}

}
