package com.mercurio.lms.services.vendas;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ControleValidacaoService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.vendas.model.service.ClienteService;

import br.com.tntbrasil.integracao.domains.vendas.ListaClientesBloqueadoTributacao;

@Path("/vendas/cliente")
public class ClienteServiceRest {

	@InjectInJersey
	ClienteService clienteService;
	@InjectInJersey
	ParametroGeralService parametroGeralService;

	@InjectInJersey
	ControleValidacaoService controleValidacaoService; 
	
	@POST
	@Path("updateClientesBloqueadosPorTributacao")
	public Response updateClientesBloqueadosPorTributacao(ListaClientesBloqueadoTributacao lista) {
		controleValidacaoService.updateByNrIdentificacaoBloqueado(lista);
		return Response.ok().build();
	}

}
