package com.mercurio.lms.rest.municipios;

import java.util.Map;

import javax.ws.rs.Path;

import com.mercurio.lms.rest.municipios.dto.EmpresaSuggestFilterDTO;

@Path("/municipios/ciaAereaSuggest")
public class CiaAereaSuggestRest extends EmpresaSuggestRest {
	
	@Override
	protected Map<String, Object> filterConvert(EmpresaSuggestFilterDTO empresaSuggestFilterDTO) {
		Map<String, Object> map = super.filterConvert(empresaSuggestFilterDTO);
		map.put("tpEmpresa", "C");
		return map;
	}

}
