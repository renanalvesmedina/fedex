package com.mercurio.lms.services.sgr;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.pgr.retornosmp.RetornoEventosSmpDto;
import br.com.tntbrasil.integracao.domains.pgr.retornosmp.RetornoSmpDto;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.sgr.model.service.GerarEnviarSMPService;

@Path("/sgr/retornoSmpRest") 
public class RetornoSmpRest {
	
	@InjectInJersey
	private GerarEnviarSMPService gerarEnviarSMPService;
	
	
	@POST
	@Path("storeRetornoSmpRest")
	public Response storeRetornoSmpRest(RetornoSmpDto retornoSmpDto){
		gerarEnviarSMPService.storeSMPRetornoInsere(retornoSmpDto);
		return Response.ok("OK").build();
	}
	
	@POST
	@Path("storeRetornoSmpCancelamentoRest")
	public Response storeRetornoSmpCancelamentoRest(RetornoSmpDto retornoSmpDto){
		gerarEnviarSMPService.storeSMPRetornoCancela(retornoSmpDto);
		return Response.ok("OK").build();
	}

	@POST
	@Path("storeRetornoSmpFinalizacaoRest")
	public Response storeRetornoSmpFinalizacaoRest(RetornoSmpDto retornoSmpDto){
		gerarEnviarSMPService.storeSMPRetornoFinaliza(retornoSmpDto);
		return Response.ok("OK").build();
	}
	
	@POST
	@Path("storeRetornoSmpEventosRest")
	public Response storeRetornoSmpEventosRest(RetornoEventosSmpDto retornoEventosSmpDto){
		gerarEnviarSMPService.storeSMPRetornoEventos(retornoEventosSmpDto);
		return Response.ok("OK").build();
	}
	
	public GerarEnviarSMPService getGerarEnviarSMPService() {
		return gerarEnviarSMPService;
	}

	public void setGerarEnviarSMPService(GerarEnviarSMPService gerarEnviarSMPService) {
		this.gerarEnviarSMPService = gerarEnviarSMPService;
	}

}
