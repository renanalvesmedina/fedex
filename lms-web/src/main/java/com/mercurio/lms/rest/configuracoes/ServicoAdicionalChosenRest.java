package com.mercurio.lms.rest.configuracoes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.configuracoes.model.service.ServicoAdicionalService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;

@Path("/configuracoes/servicoAdicionalChosen")
public class ServicoAdicionalChosenRest extends BaseRest {

	@InjectInJersey
	private ServicoAdicionalService servicoAdicionalService;
		
	@GET
	@Path("/find")
	public Response find() {
		List<ServicoAdicional> listServicosAdicionais = servicoAdicionalService.findServicosAdicionaisAtivos(null);
		List<ServicoAdicionalChosenDTO> listDto = new ArrayList<ServicoAdicionalChosenDTO>();
		
		for(ServicoAdicional servico : listServicosAdicionais) {
			listDto.add(new ServicoAdicionalChosenDTO(servico.getIdServicoAdicional(), servico.getDsServicoAdicional().toString()));
		}
		
		return Response.ok(listDto).build();
	}

}
