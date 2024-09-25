package com.mercurio.lms.rest.config;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.model.Imagem;
import com.mercurio.lms.configuracoes.model.service.ImagemService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Public
@Path("/config/imagem")
public class ImagemRest {
	
	@InjectInJersey
	private ImagemService imagemService;
	
	@GET
	@Path("findByChave")
	public Response findByChave(@QueryParam("chave") String chave) {
		Imagem imagem = imagemService.findByChave(chave, Boolean.FALSE);
		return Response.ok(imagem).build();
	}
}
