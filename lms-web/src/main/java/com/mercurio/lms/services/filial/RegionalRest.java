package com.mercurio.lms.services.filial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.service.RegionalService;

@Path("/regional")
public class RegionalRest {

	@InjectInJersey RegionalService regionalService;
	
	@InjectInJersey UsuarioService usuarioService;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findRegionaisUsuario")
	public Response findFiliaisUsuario(String login) {
		List<String> regionaisList = new ArrayList<String>();
		if(login != null) {
			Usuario user =  usuarioService.findUsuarioByLogin(login);
			List<Map<String, Object>> regionais = regionalService.findRegionaisByUsuario(user);
			for (Map<String, Object> regional : regionais) {
				regionaisList.add(regional.get("sgRegional").toString());
			}
		}
		return Response.ok(regionaisList).build();
	}
	
}
