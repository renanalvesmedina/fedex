package com.mercurio.lms.rest.municipios;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/municipios/manterPais")
public class PaisRest {
	
	/**
	 * Retorna o pais do usuário logado.
	 * 
	 * @return TypedFlatMap
	 */
	@GET
	@Path("findPaisUsuarioLogado")
	public TypedFlatMap findPaisUsuarioLogado() { 
		Pais pais = SessionUtils.getPaisSessao();
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("idPais", pais.getIdPais());
		result.put("nmPais", pais.getNmPais().getValue());
		result.put("sgPais", pais.getSgPais());
		result.put("cdIso", pais.getCdIso());
		
		return result;
	}	
}