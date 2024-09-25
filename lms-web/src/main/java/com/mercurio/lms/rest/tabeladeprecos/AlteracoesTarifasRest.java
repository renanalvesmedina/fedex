package com.mercurio.lms.rest.tabeladeprecos;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
 
@Path("/tabeladeprecos/alteracoesTarifas") 
public class AlteracoesTarifasRest extends BaseRest {
	@InjectInJersey 
	TabelaPrecoService tabelaPrecoService;
	
	
	@POST
	@Path("find")
    public Response find(TypedFlatMap criteria) {
		String reportLocator = tabelaPrecoService.findRelatorioAlteracoesTarifas(criteria);
		
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("reportLocator", reportLocator);
		return Response.ok(retorno).build();
    }
	
} 
