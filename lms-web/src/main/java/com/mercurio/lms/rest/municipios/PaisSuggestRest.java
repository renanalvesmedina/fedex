package com.mercurio.lms.rest.municipios;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.rest.municipios.dto.PaisSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.PaisSuggestFilterDTO;
import com.mercurio.lms.util.PessoaUtils;

@Path("/municipios/paisSuggest")
public class PaisSuggestRest extends BaseSuggestRest<PaisSuggestDTO, PaisSuggestFilterDTO> {
	private static final int MAX_CODIGO_ISO = 4;
	
	@InjectInJersey
	private PaisService paisService;

	@Override
	protected Map<String, Object> filterConvert(PaisSuggestFilterDTO filter) {
		
		String value = filter.getValue();
		String nr = PessoaUtils.clearIdentificacao(value);
		
		Map<String, Object> filtros = new HashMap<String, Object>();
		if (filter.getIdZona() != null) {
			filtros.put("idZona", filter.getIdZona());
		}
		
		if(StringUtils.isNumeric(nr) && nr.length() < MAX_CODIGO_ISO) {
			filtros.put("cdIso", Integer.valueOf(nr));
		} else if(!StringUtils.isNumeric(value)) {
			filtros.put("dsPais", value);
		}
		return filtros;
	}

	@Override
	protected PaisSuggestDTO responseConvert(Map<String, Object> map) {
		PaisSuggestDTO paisSuggestDTO = new PaisSuggestDTO();
		if (map.get("IDPAIS") != null) {
			paisSuggestDTO.setIdPais(Long.valueOf(map.get("IDPAIS").toString()));
		}
		if (map.get("CDISO") != null) {
			paisSuggestDTO.setCdIso(Integer.valueOf(map.get("CDISO").toString()));
		}
		if (map.get("SGPAIS") != null) {
			paisSuggestDTO.setSgPais(map.get("SGPAIS").toString());
		}
		if (map.get("NMPAIS") != null) {
			paisSuggestDTO.setNmPais(map.get("NMPAIS").toString());
		}
		return paisSuggestDTO;
	}

	@Override
	protected PaisService getService() {
		return paisService;
	}
	
}