package com.mercurio.lms.rest.vendas;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.rest.vendas.dto.CaeSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.CaeSuggestFilterDTO;
import com.mercurio.lms.rest.vendas.dto.TerritorioSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.TerritorioSuggestFilterDTO;
import com.mercurio.lms.vendas.model.service.TerritorioService;

@Path("/expedicao/caeSuggest")
public class CaeSuggestRest extends BaseSuggestRest<CaeSuggestDTO, CaeSuggestFilterDTO> {

	@InjectInJersey
	private ConhecimentoService conhecimentoService;

	@Override
	protected Map<String, Object> filterConvert(CaeSuggestFilterDTO filter) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (filter.getValue() != null) {
			String sgFilial = filter.getValue().substring(0, 3).toUpperCase();
			map.put("sgFilial", sgFilial);
			map.put("nrCae", filter.getValue().substring(3).trim());
		}
		return map;
		
		
	}

	@Override
	protected ConhecimentoService getService() {
		return conhecimentoService;
	}

	@Override
	protected CaeSuggestDTO responseConvert(Map<String, Object> map) {
		CaeSuggestDTO caeSuggestDTO = new CaeSuggestDTO();

		if (map.get("SG_FILIAL") != null) {
			caeSuggestDTO.setNmFilial(map.get("SG_FILIAL").toString());
		}

		if (map.get("NR_CAE") != null) {
			caeSuggestDTO.setNrCae(ajustaNrCae(map.get("NR_CAE").toString()));
			caeSuggestDTO.setId(Long.valueOf(map.get("NR_CAE").toString()));
		}

		if (map.get("ID_FILIAL_ORIGEM") != null) {
			caeSuggestDTO.setIdFilial(Long.valueOf(map.get("ID_FILIAL_ORIGEM").toString()));
		}

		return caeSuggestDTO;
	}
	
	private String ajustaNrCae(String nrCae) {
		return StringUtils.leftPad(nrCae, 8, "0");
	}

}
