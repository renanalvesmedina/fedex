package com.mercurio.lms.rest.portaria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.rest.PaginationDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.portaria.model.service.PortariaService;
import com.mercurio.lms.rest.portaria.contract.Portaria;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/portaria")
public class PortariaRest {

	@InjectInJersey
	private PortariaService portariaService;
	
	@POST
	@Path("/filial/")
	public Response findPortariasFilial() {
		List<Map<String, Object>> portarias = portariaService.findByFilial(SessionUtils.getFilialSessao().getIdFilial());
		return Response.ok(wrapResult(convertSaidas(portarias))).build();
	}
	
	private List<Portaria> convertSaidas(List<Map<String, Object>> portarias) {
		List<Portaria> result = new ArrayList<Portaria>();
		for (Map<String, Object> portaria : portarias) {
			result.add(convertDTO(portaria));
		}
		return result;
	}

	private Portaria convertDTO(Map<String, Object> portaria) {
		Portaria dto = new Portaria();
		dto.setId((Long)portaria.get("idPortaria"));
		dto.setDescricao(String.valueOf(portaria.get("dsPortaria")));
		dto.setPadrao((Boolean) portaria.get("blPadraoFilial"));
		return dto;
	}
	
	private PaginationDTO wrapResult(List<Portaria> portarias) {
		PaginationDTO pagination = new PaginationDTO();
		pagination.setList(portarias);
		pagination.setQtRegistros(portarias.size());
		return pagination;
	}
}
