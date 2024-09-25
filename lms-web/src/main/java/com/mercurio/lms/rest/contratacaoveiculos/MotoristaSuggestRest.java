package com.mercurio.lms.rest.contratacaoveiculos;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MotoristaSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MotoristaSuggestFilterDTO;
import com.mercurio.lms.util.FormatUtils;

@Path("/contratacaoveiculos/motoristaSuggest")
public class MotoristaSuggestRest extends BaseSuggestRest<MotoristaSuggestDTO, MotoristaSuggestFilterDTO> {
	
	@InjectInJersey
	private MotoristaService motoristaService;
	
	@Override
	protected Map<String, Object> filterConvert(MotoristaSuggestFilterDTO filter) {
		Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("value", filter.getValue());
		return filterMap;
	}

	@Override
	protected MotoristaSuggestDTO responseConvert(Map<String, Object> map) {
		MotoristaSuggestDTO dto = new MotoristaSuggestDTO();
		dto.setId(Long.valueOf(map.get("ID_MOTORISTA").toString()));
		dto.setNrIdentificacao(FormatUtils.formatIdentificacao(map.get("TP_IDENTIFICACAO").toString(),map.get("NR_IDENTIFICACAO").toString()));
		dto.setNmPessoa(map.get("NM_PESSOA").toString());
		return dto;
	}

	@Override
	protected MotoristaService getService() {
		return motoristaService;
	}
}
