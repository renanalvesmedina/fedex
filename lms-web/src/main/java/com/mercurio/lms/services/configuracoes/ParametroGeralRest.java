package com.mercurio.lms.services.configuracoes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;

/**
 * Rest responsável pelo controle da tela manter contato pessoa.
 * 
 */
@Path("/configuracoes/parametro/geral")
public class ParametroGeralRest {

	@InjectInJersey
	private ParametroGeralService parametroGeralService;

	@GET
	@Path("/buscarpornome/{nomeParametro}")
	public Response findById(@PathParam("nomeParametro") String nomeParametro) {
		String dsConteudo = parametroGeralService.findByNomeParametro(nomeParametro, false).getDsConteudo();
		return Response.ok(dsConteudo).build();
	}

}