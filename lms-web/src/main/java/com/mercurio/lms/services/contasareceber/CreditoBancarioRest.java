package com.mercurio.lms.services.contasareceber;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.model.service.CreditoBancarioService;

@Path("/contasareceber/creditoBancario") 
public class CreditoBancarioRest {

	@InjectInJersey	
	private CreditoBancarioService creditoBancarioService;
	
	@POST
	@Path("executeIncluirCreditoBancario")
	public Response executeIncluirCreditoBancario(CreditoBancario creditoBancario){
		
		return  Response.ok(
				creditoBancarioService.executeIncluirCreditoBancario(creditoBancario)
				).build();
	}
	
}
