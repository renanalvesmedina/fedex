package com.mercurio.lms.rest.municipios;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/municipios/filial")
public class FilialRest {

	@GET
	@Path("findFilialUsuarioLogado")
	public TypedFlatMap findFilialUsuarioLogado() { 
		Filial bean = SessionUtils.getFilialSessao();
		
		TypedFlatMap result = new TypedFlatMap();
		
		result.put("idFilial", bean.getIdFilial());
		result.put("sgFilial", bean.getSgFilial());
		result.put("nmFilial", bean.getPessoa().getNmFantasia());		
		result.put("isMatriz", SessionUtils.isFilialSessaoMatriz());
		
		return result;
	}	
}
