package com.mercurio.lms.rest.municipios;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.rest.municipios.dto.EmpresaSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.EmpresaSuggestFilterDTO;
import com.mercurio.lms.util.PessoaUtils;

@Path("/municipios/empresaSuggest")
public class EmpresaSuggestRest extends BaseSuggestRest<EmpresaSuggestDTO, EmpresaSuggestFilterDTO> {
	
	@InjectInJersey
	private EmpresaService empresaService;
	
	@Override
	protected Map<String, Object> filterConvert(EmpresaSuggestFilterDTO ciaAereaSuggestFilterDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		String value = ciaAereaSuggestFilterDTO.getValue();
		String aux = PessoaUtils.clearIdentificacao(value);
		if (StringUtils.isNumeric(aux)) {
			value = aux;
		}
		
		if (StringUtils.isNumeric(value) && value.length() >= 8 && value.length() <= 14) {
			map.put("nrIdentificacao", value);
		} else {
			map.put("nmPessoa", value);
		}
		
		return map;
	}

	@Override
	protected EmpresaSuggestDTO responseConvert(Map<String, Object> arg0) {
		EmpresaSuggestDTO empresaSuggestDTO = new EmpresaSuggestDTO();
		
		if (arg0.get("ID_PESSOA") != null) {
			empresaSuggestDTO.setIdEmpresa(Long.valueOf(arg0.get("ID_PESSOA").toString()));
		}
		if (arg0.get("NM_FANTASIA") != null) {
			empresaSuggestDTO.setNmFantasia(arg0.get("NM_FANTASIA").toString());
		}
		if (arg0.get("NM_MUNICIPIO") != null) {
			empresaSuggestDTO.setNmMunicipio(arg0.get("NM_MUNICIPIO").toString());
		}
		if (arg0.get("NM_PESSOA") != null) {
			empresaSuggestDTO.setNmPessoa(arg0.get("NM_PESSOA").toString());
		}
		if (arg0.get("NR_IDENTIFICACAO") != null) {
			empresaSuggestDTO.setNrIdentificacao(arg0.get("NR_IDENTIFICACAO").toString());
		}
		if (arg0.get("SG_UNIDADE_FEDERATIVA") != null) {
			empresaSuggestDTO.setSgUnidadeFederativa(arg0.get("SG_UNIDADE_FEDERATIVA").toString());
		}

		return empresaSuggestDTO;
	}
	
	@Override
	protected EmpresaService getService() {
		return empresaService;
	}
}
