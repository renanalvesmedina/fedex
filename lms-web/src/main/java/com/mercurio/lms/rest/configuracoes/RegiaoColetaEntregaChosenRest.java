package com.mercurio.lms.rest.configuracoes;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.coleta.model.service.FuncionarioRegiaoService;
import com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil;

@Path("/configuracoes/regiaoColetaEntregaChosen")
public class RegiaoColetaEntregaChosenRest extends BaseRest {

	@InjectInJersey
	private FuncionarioRegiaoService funcionarioRegiaoService;
		
	@GET
	@Path("/find")
	public Response find() {
		List<RegiaoColetaEntregaFil> regioes = this.funcionarioRegiaoService.findRegiaoColetaEntregaByFilial(null);
		List<RegiaoColetaEntregaChosenDTO> listDto = new ArrayList<RegiaoColetaEntregaChosenDTO>();
		
		for(RegiaoColetaEntregaFil regiao : regioes) {
			listDto.add(new RegiaoColetaEntregaChosenDTO(regiao.getIdRegiaoColetaEntregaFil(), regiao.getDsRegiaoColetaEntregaFil()));
		}
		
		return Response.ok(listDto).build();
	}

}
