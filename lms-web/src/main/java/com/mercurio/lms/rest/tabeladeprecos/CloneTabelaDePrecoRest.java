package com.mercurio.lms.rest.tabeladeprecos;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.tabelaprecos.model.CloneTabelaPrecoDTO;
import com.mercurio.lms.tabelaprecos.model.service.ReajusteTabelaPrecoFacade;

@Path("/tabeladeprecos/clonetabeladepreco")
public class CloneTabelaDePrecoRest {
	@InjectInJersey ReajusteTabelaPrecoFacade reajusteTabelaPrecoFacade;

	
	@POST
	@Path("clonarTabelaPrecoETarifaPrecoRotas")
	public Boolean clonarTabelaPrecoETarifaPrecoRotas(CloneTabelaPrecoDTO cloneTabelaPreco) throws Throwable{
		return reajusteTabelaPrecoFacade.executeClonarTabelaPrecoETarifaPrecoRotas(cloneTabelaPreco);
	}

	@POST
	@Path("executeClonarTabelaPreco")
	public Boolean executeClonarTabelaPreco(CloneTabelaPrecoDTO cloneTabelaPreco) throws Exception{
		return reajusteTabelaPrecoFacade.executeClonarTabelaPreco(cloneTabelaPreco);
	}

	@POST
	@Path("executeClonarTarifaPrecoRotas")
	public Boolean executeClonarTarifaPrecoRotas(CloneTabelaPrecoDTO cloneTabelaPreco) throws Exception{
		return reajusteTabelaPrecoFacade.executeClonarTarifaPrecoRotas(cloneTabelaPreco);
	}
	
}
