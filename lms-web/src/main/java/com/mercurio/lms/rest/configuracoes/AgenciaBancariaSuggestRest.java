package com.mercurio.lms.rest.configuracoes;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.AgenciaBancariaService;
import com.mercurio.lms.rest.configuracoes.dto.AgenciaBancariaSuggestDTO;
import com.mercurio.lms.rest.configuracoes.dto.AgenciaBancariaSuggestFilterDTO;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Rest responsável pelo controle da tela manter agência bancária.
 * 
 */
@Path("/configuracoes/agenciaBancariaSuggest")
public class AgenciaBancariaSuggestRest extends BaseSuggestRest<AgenciaBancariaSuggestDTO, AgenciaBancariaSuggestFilterDTO> {
	private static final int MAX_LENGTH = 5;
	
	@InjectInJersey
	private AgenciaBancariaService agenciaBancariaService; 
	
	@Override
	protected Map<String, Object> filterConvert(AgenciaBancariaSuggestFilterDTO filter) {
		String nr = PessoaUtils.clearIdentificacao(filter.getValue());
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (filter.getIdBanco() != null) {
			map.put("idBanco", filter.getIdBanco());
		}
		
		if(StringUtils.isNumeric(nr) && nr.length() < MAX_LENGTH) {
			map.put("nrAgenciaBancaria", Short.valueOf(nr));
		} else if(!StringUtils.isNumeric(filter.getValue())) {
			map.put("nmAgenciaBancaria", filter.getValue());
		}
		
		return map;
	}

	@Override
	protected AgenciaBancariaSuggestDTO responseConvert(Map<String, Object> map) {
		AgenciaBancariaSuggestDTO agenciaBancariaSuggestDTO = new AgenciaBancariaSuggestDTO();
		
		if (map.get("IDAGENCIABANCARIA") != null) {
			agenciaBancariaSuggestDTO.setIdAgenciaBancaria(Long.valueOf(map.get("IDAGENCIABANCARIA").toString()));
		}
		
		if (map.get("NRAGENCIABANCARIA") != null) {
			agenciaBancariaSuggestDTO.setNrAgenciaBancaria(Short.valueOf(map.get("NRAGENCIABANCARIA").toString()));
		}
		
		if (map.get("NMAGENCIABANCARIA") != null) {
			agenciaBancariaSuggestDTO.setNmAgenciaBancaria(map.get("NMAGENCIABANCARIA").toString());
		}
		
		return agenciaBancariaSuggestDTO;
	}

	@Override
	protected AgenciaBancariaService getService() {
		return agenciaBancariaService;
	}

}
