package com.mercurio.lms.rest.vendas;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.vendas.dto.TerritorioSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.TerritorioSuggestFilterDTO;
import com.mercurio.lms.vendas.model.service.TerritorioService;

@Path("/vendas/territorioSuggest")
public class TerritorioSuggestRest extends BaseSuggestRest<TerritorioSuggestDTO, TerritorioSuggestFilterDTO> {

	@InjectInJersey
	private TerritorioService territorioService;

	@Override
	protected Map<String, Object> filterConvert(TerritorioSuggestFilterDTO filter) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (filter.getValue() != null) {
			map.put("nmTerritorio", filter.getValue());
		}
		return map;
	}

	@Override
	protected TerritorioService getService() {
		return territorioService;
	}

	@Override
	protected TerritorioSuggestDTO responseConvert(Map<String, Object> map) {
		TerritorioSuggestDTO territorioSuggestDTO = new TerritorioSuggestDTO();

		if (map.get("ID_TERRITORIO") != null) {
			territorioSuggestDTO.setId(Long.valueOf(map.get("ID_TERRITORIO").toString()));
		}

		if (map.get("NM_TERRITORIO") != null) {
			territorioSuggestDTO.setNmTerritorio(map.get("NM_TERRITORIO").toString());
		}

		return territorioSuggestDTO;
	}

}
