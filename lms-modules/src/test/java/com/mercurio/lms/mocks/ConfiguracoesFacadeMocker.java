package com.mercurio.lms.mocks;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;
import org.mockito.Mockito;

import com.mercurio.lms.configuracoes.ConfiguracoesFacade;

public class ConfiguracoesFacadeMocker {
	
	@Mock private ConfiguracoesFacade configuracoesFacade;
	
	public ConfiguracoesFacadeMocker() {
		initMocks(this);
	}
	
	public ConfiguracoesFacade mock(){
		return configuracoesFacade;
	}
	
	public ConfiguracoesFacadeMocker getValorParametro(String parametro, String retorno){
		when(configuracoesFacade.getValorParametro(parametro)).thenReturn(retorno);
		return this;
	}
	
	public ConfiguracoesFacadeMocker getMensagem(String answer){
		when(configuracoesFacade.getMensagem(Mockito.anyString())).thenReturn(answer);
		return this;
	}

}
