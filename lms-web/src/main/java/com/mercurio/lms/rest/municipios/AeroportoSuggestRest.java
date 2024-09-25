package com.mercurio.lms.rest.municipios;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.rest.municipios.dto.AeroportoSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.AeroportoSuggestFilterDTO;

@Path("/municipios/aeroportoSuggest")
public class AeroportoSuggestRest extends BaseSuggestRest<AeroportoSuggestDTO, AeroportoSuggestFilterDTO> {

	@InjectInJersey
	private AeroportoService aeroportoService;
	
	@Override
	protected Map<String, Object> filterConvert(AeroportoSuggestFilterDTO filter) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (filter.getValue() != null) {
			map.put("sgAeroporto", filter.getValue());
		}
		return map;
	}

	@Override
	protected AeroportoSuggestDTO responseConvert(Map<String, Object> map) {
		AeroportoSuggestDTO aeroportoSuggestDTO = new AeroportoSuggestDTO();
		if (map.get("IDAEROPORTO") != null) {
			aeroportoSuggestDTO.setId(Long.valueOf(map.get("IDAEROPORTO").toString()));
		}
		if (map.get("SGAEROPORTO") != null) {
			aeroportoSuggestDTO.setSgAeroporto(map.get("SGAEROPORTO").toString());
		}
		if (map.get("NMAEROPORTO") != null) {
			aeroportoSuggestDTO.setNmAeroporto(map.get("NMAEROPORTO").toString());
		}
		return aeroportoSuggestDTO;
	}

	@Override
	protected AeroportoService getService() {
		return aeroportoService;
	}
}
