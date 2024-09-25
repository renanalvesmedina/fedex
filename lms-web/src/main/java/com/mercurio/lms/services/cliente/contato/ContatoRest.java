package com.mercurio.lms.services.cliente.contato;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.dto.ContatoDto;

@Path("/contato")
public class ContatoRest {

	@InjectInJersey UsuarioService usuarioService;
	
	/**
	 * Responsavel por retornar dados do contato do cliente 
	 * @param idCliente
	 * @return
	 */
	@POST
	@Path("findContato")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findContato(Long idCliente) {
		ContatoDto contato = usuarioService.findContato(idCliente);
		return Response.ok(contato).type(MediaType.APPLICATION_JSON).build();
	}
}
