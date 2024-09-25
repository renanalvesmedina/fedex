package com.mercurio.lms.rest.municipios;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.municipios.model.service.EmpresaService;

@Path("/municipios/empresa")
public class EmpresaRest {
	
	@InjectInJersey
	EmpresaService empresaService;

	@InjectInJersey
	private IntegracaoJwtService integracaoJwtService;
	
	@GET
	@Path("findByUsuarioLogado")
	public Response findByUsuarioLogado() {
		return Response.ok(empresaService.findByUsuarioLogado(null)).build();
	}

	@GET
	@Path("findEmpresaByUsuarioAutenticado")
	public Response findEmpresaByUsuarioAutenticado(@Context HttpHeaders headers) {
		return Response.ok(
					empresaService.findEmpresaByUsuarioAutenticado(
					integracaoJwtService.findAutenticacaoDMN(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))
				)
		).build();
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
}
