package com.mercurio.lms.facade.radar.impl;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.facade.radar.RecursoMensagemFacade;

/**
 * @author Expedito Neto
 * @spring.bean id="lms.radar.recursoMensagemFacade"
 */
@ServiceSecurity
public class RecursoMensagemFacadeImpl implements RecursoMensagemFacade{
	
	private RecursoMensagemService recursoMensagemService;

	@Override
	@MethodSecurity
	(
		processGroup = "radar.recursoMensagem", 
		processName = "findRecursoMensagemByChave", authenticationRequired=false
	)
	public TypedFlatMap findRecursoMensagemByChave(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		String chave = criteria.getString("chave");
		String mensagem = recursoMensagemService.findByChave(chave);
		map.put("mensagem", mensagem);
		return map;
	}

	public void setRecursoMensagemService(RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}
	
	

}
