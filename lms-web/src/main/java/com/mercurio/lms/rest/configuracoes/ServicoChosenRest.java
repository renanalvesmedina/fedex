package com.mercurio.lms.rest.configuracoes;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ServicoService;

@Path("/configuracoes/servicoChosen")
public class ServicoChosenRest extends BaseRest {

	@InjectInJersey
	private ServicoService servicoService;
		
	@GET
	@Path("/find")
	public Response find() {
		List<Servico> list = servicoService.findChosen();
		List<ServicoChosenDTO> listDto = new ArrayList<ServicoChosenDTO>();
		
		for(Servico servico : list) {
			listDto.add(new ServicoChosenDTO(servico.getIdServico(), servico.getDsServico().toString()));
		}
		
		return Response.ok(listDto).build();
	}

}
