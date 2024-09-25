package com.mercurio.lms.facade.radar.impl;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.facade.radar.UsuarioFacade;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;

/**
 * @author Adércio Reinan
 * @spring.bean id="lms.radar.usuarioFacade"
 */
@ServiceSecurity
public class UsuarioFacadeImpl implements UsuarioFacade{
	
	private UsuarioService usuarioService;
	private ConfiguracoesFacade configuracoesFacade;

	@Override
	@MethodSecurity(processGroup = "radar.usuario", processName = "findUsuarioClienteByLogin", authenticationRequired=false)
	public TypedFlatMap findUsuarioClienteByLogin(TypedFlatMap map) {
		String login = map.getString("login");
		Object[] obj = usuarioService.findUsuarioClienteByLogin(login);
		return parseResultToMapUsuario(obj);
	}

	private TypedFlatMap parseResultToMapUsuario(Object[] obj) {
		TypedFlatMap retorno = null;
		if(obj != null){
			int i = 0;
			retorno = new TypedFlatMap();
			retorno.put("login", obj[i++]);
			retorno.put("nmUsuario", obj[i++]);
			retorno.put("idUsuarioCliente", obj[i++]);
			retorno.put("idUsuario", obj[i++]);
			retorno.put("tpCategoriaUsuario", obj[i++]);
			retorno.put("dsEmail", obj[i++]);
			retorno.put("dddTelefone", obj[i++]);
			retorno.put("nrTelefone", obj[i++]);
		}
		return retorno;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.usuario", processName = "findRecursosMensagem", authenticationRequired=false)
	public String findRecursosMensagem(TypedFlatMap map) {
		String chave = map.getString("chave");
		return configuracoesFacade.getMensagem(chave);
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

}
