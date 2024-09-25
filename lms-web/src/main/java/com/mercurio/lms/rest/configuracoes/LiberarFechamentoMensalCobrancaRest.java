package com.mercurio.lms.rest.configuracoes;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.FechamentoMensalCobrancaService;
import com.mercurio.lms.rest.configuracoes.dto.LiberarFechamentoMensalCobrancaDTO;

@Path("/configuracoes/liberarFechamentoMensalCobranca")
public class LiberarFechamentoMensalCobrancaRest extends BaseRest {
	
	@InjectInJersey
	private FechamentoMensalCobrancaService fechamentoMensalCobrancaService;
	
	@GET
	@Path("findCompetencia")
	public LiberarFechamentoMensalCobrancaDTO findCompetencia(){
		Boolean isFechamentoHabilitado = fechamentoMensalCobrancaService.isFechamentoHabilitado();
		String competencia = fechamentoMensalCobrancaService.getCompetencia();
		return new LiberarFechamentoMensalCobrancaDTO(isFechamentoHabilitado, competencia);
	}
	
	@POST
	@Path("liberarFechamento")
	public LiberarFechamentoMensalCobrancaDTO liberarFechamento(){
		fechamentoMensalCobrancaService.executeLiberarFechamento();
		return findCompetencia();
	}
	
}
