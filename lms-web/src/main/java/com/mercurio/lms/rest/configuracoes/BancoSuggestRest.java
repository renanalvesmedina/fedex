package com.mercurio.lms.rest.configuracoes;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.BancoService;
import com.mercurio.lms.rest.configuracoes.dto.BancoSuggestDTO;
import com.mercurio.lms.rest.configuracoes.dto.BancoSuggestFilterDTO;
import com.mercurio.lms.util.PessoaUtils;


@Path("/configuracoes/bancoSuggest")
public class BancoSuggestRest extends BaseSuggestRest<BancoSuggestDTO, BancoSuggestFilterDTO> {
	private static final int MAX_LENGTH = 4;

	@InjectInJersey
	private BancoService bancoService; 
	
	@Override
	protected Map<String, Object> filterConvert(BancoSuggestFilterDTO filter) {
		String nr = PessoaUtils.clearIdentificacao(filter.getValue());
		Map<String, Object> filtros = new HashMap<String, Object>();
		
		if(StringUtils.isNumeric(nr) && nr.length() < MAX_LENGTH ) {
			filtros.put("nrBanco", Short.valueOf(nr));
		} else if(!StringUtils.isNumeric(filter.getValue())) {
			filtros.put("nmBanco", filter.getValue());
		}
		return filtros;
	}

	@Override
	protected BancoSuggestDTO responseConvert(Map<String, Object> map) {
		BancoSuggestDTO bancoSuggestDTO = new BancoSuggestDTO();
		if (map.get("IDBANCO") != null) {
			bancoSuggestDTO.setIdBanco(Long.valueOf(map.get("IDBANCO").toString()));
		}
		if (map.get("NRBANCO") != null) {
			bancoSuggestDTO.setNrBanco(Short.valueOf(map.get("NRBANCO").toString()));
		}
		if (map.get("NMBANCO") != null) {
			bancoSuggestDTO.setNmBanco(map.get("NMBANCO").toString());
		}
		return bancoSuggestDTO;
	}

	@Override
	protected BancoService getService() {
		return bancoService;
	}

}