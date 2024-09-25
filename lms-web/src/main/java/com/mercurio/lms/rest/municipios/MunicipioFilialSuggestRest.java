package com.mercurio.lms.rest.municipios;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.rest.municipios.dto.MunicipioFilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.MunicipioFilialSuggestFilterDTO;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/municipios/municipioFilialSuggest")
public class MunicipioFilialSuggestRest extends BaseSuggestRest<MunicipioFilialSuggestDTO, MunicipioFilialSuggestFilterDTO>{
	
	@InjectInJersey
	MunicipioFilialService municipioFilialService;
	
	
	@Override
	protected Map<String, Object> filterConvert(MunicipioFilialSuggestFilterDTO filter) {
		Map<String, Object> map = new HashMap<String, Object>();
		Long idEmpresa = SessionUtils.getEmpresaSessao().getIdEmpresa();
		map.put("idEmpresa", idEmpresa);
		map.put("nmMunicipio", filter.getValue());
		
		return map;
	}

	@Override
	protected MunicipioFilialSuggestDTO responseConvert(Map<String, Object> map) {
		MunicipioFilialSuggestDTO municipioFilialSuggestDTO = new MunicipioFilialSuggestDTO();
		
		if (map.get("NMMUNICIPIO") != null) {
			municipioFilialSuggestDTO.setNmMunicipio(map.get("NMMUNICIPIO").toString());
		}
		
		if (map.get("SGUNIDADEFEDERATIVA") != null) {
			municipioFilialSuggestDTO.setSgUnidadeFederativa(map.get("SGUNIDADEFEDERATIVA").toString());
		}
		
		if (map.get("SGFILIAL") != null) {
			municipioFilialSuggestDTO.setSgFilial(map.get("SGFILIAL").toString());
		}
		
		if (map.get("IDEMPRESA") != null) {
			municipioFilialSuggestDTO.setIdEmpresa(Long.valueOf(map.get("IDEMPRESA").toString()));
		}
		
		if (map.get("IDMUNICIPIO") != null) {
			municipioFilialSuggestDTO.setIdMunicipio(Long.valueOf(map.get("IDMUNICIPIO").toString()));
		}
		if (map.get("IDFILIAL") != null) {
			municipioFilialSuggestDTO.setIdFilial(Long.valueOf(map.get("IDFILIAL").toString()));
		}
		if (map.get("IDMUNICIPIOFILIAL") != null) {
			municipioFilialSuggestDTO.setIdMunicipioFilial(Long.valueOf(map.get("IDMUNICIPIOFILIAL").toString()));
		}
		if (map.get("NMFANTASIA") != null) {
			municipioFilialSuggestDTO.setNmFantasia(map.get("NMFANTASIA").toString());
		}
		return municipioFilialSuggestDTO;
	}
	
	@Override
	protected MunicipioFilialService getService() {
		return municipioFilialService;
	}
	
}
	

	

