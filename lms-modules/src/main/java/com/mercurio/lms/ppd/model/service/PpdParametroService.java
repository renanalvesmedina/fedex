package com.mercurio.lms.ppd.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;

public class PpdParametroService {
	private ConfiguracoesFacade configuracoesFacade;

	public Object getValorParametro(String nomeParametro) {
		Object valor;
		try {
			return configuracoesFacade.getValorParametro(nomeParametro);
		} catch(BusinessException e) {
			throw e;
		}	
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}
