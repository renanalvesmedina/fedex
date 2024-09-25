package com.mercurio.lms.rest.municipios;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.rest.municipios.dto.RegionalChosenDTO;

@Path("/municipios/regionalChosen")
public class RegionalChosenRest extends BaseRest{

	@InjectInJersey
	private RegionalService regionalService;
		
	@GET
	@Path("/findChosen")
	public Response findChosen() {
		List<Regional> list = regionalService.findChosen();
		List<RegionalChosenDTO> listDto = new ArrayList<RegionalChosenDTO>();
		
		for(Regional regional : list) {
			listDto.add(new RegionalChosenDTO(regional.getIdRegional(), regional.getDsRegional()));
		}
		
		return Response.ok(listDto).build();
	}

}
