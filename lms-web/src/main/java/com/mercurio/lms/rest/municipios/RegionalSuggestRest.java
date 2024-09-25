package com.mercurio.lms.rest.municipios;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.rest.municipios.dto.RegionalSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.RegionalSuggestFilterDTO;

@Path("/municipios/regionalSuggest")
public class RegionalSuggestRest extends BaseSuggestRest<RegionalSuggestDTO, RegionalSuggestFilterDTO> {

	@InjectInJersey
	private RegionalService regionalService;

	@Override
	protected Map<String, Object> filterConvert(RegionalSuggestFilterDTO filter) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (filter.getValue() != null) {
			map.put("sgRegional", filter.getValue());
		}
		return map;
	}

	@Override
	protected RegionalSuggestDTO responseConvert(Map<String, Object> map) {
		RegionalSuggestDTO regionalSuggestDTO = new RegionalSuggestDTO();
		if (map.get("IDREGIONAL") != null) {
			regionalSuggestDTO.setId(Long.valueOf(map.get("IDREGIONAL").toString()));
		}
		if (map.get("SGREGIONAL") != null) {
			regionalSuggestDTO.setSgRegional(map.get("SGREGIONAL").toString());
		}
		if (map.get("DSREGIONAL") != null) {
			regionalSuggestDTO.setDsRegional(map.get("DSREGIONAL").toString());
		}
		return regionalSuggestDTO;
	}

	@Override
	protected RegionalService getService() {
		return regionalService;
	}
	
}
