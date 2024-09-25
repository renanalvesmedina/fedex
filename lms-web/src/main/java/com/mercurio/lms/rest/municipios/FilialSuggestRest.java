package com.mercurio.lms.rest.municipios;

import java.util.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rest.municipios.dto.FilialSuggestFilterDTO;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/municipios/filialSuggest")
public class FilialSuggestRest extends BaseSuggestRest<FilialSuggestDTO, FilialSuggestFilterDTO>{
	
	@InjectInJersey
	private IntegracaoJwtService integracaoJwtService;

	@InjectInJersey
	FilialService filialService;

	@POST
	@Path("/findSuggestByToken")
	public Response findSuggestByToken(FilialSuggestFilterDTO filialSuggestFilterDTO, @Context HttpHeaders headers) {
		if(filialSuggestFilterDTO.getSomenteEmpresaUsuario()) {
			filialSuggestFilterDTO.setIdEmpresa(integracaoJwtService.getIdEmpresaByToken(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0)));
		}
		return findSuggest(filialSuggestFilterDTO);
	}

	@Override
	protected Map<String, Object> filterConvert(FilialSuggestFilterDTO filialSuggestFilterDTO) {
		Map<String, Object> map = new HashMap<>();
		
		Long idEmpresa = filialSuggestFilterDTO.getIdEmpresa();
		if (idEmpresa == null) {
			Boolean somenteEmpresaUsuario = filialSuggestFilterDTO.getSomenteEmpresaUsuario();
			if (somenteEmpresaUsuario) {
				idEmpresa = SessionUtils.getEmpresaSessao().getIdEmpresa();
			}
		}
		map.put("idEmpresa", idEmpresa);
		map.put("sgFilial", filialSuggestFilterDTO.getValue());
		
		return map;
	}

	@Override
	protected FilialSuggestDTO responseConvert(Map<String, Object> map) {
		FilialSuggestDTO filialSuggestDTO = new FilialSuggestDTO();
		
		if (map.get("IDFILIAL") != null) {
			filialSuggestDTO.setId(Long.valueOf(map.get("IDFILIAL").toString()));
		}
		
		if (map.get("SGFILIAL") != null) {
			filialSuggestDTO.setSgFilial(map.get("SGFILIAL").toString());
		}
		
		if (map.get("NMFILIAL") != null) {
			filialSuggestDTO.setNmFilial(map.get("NMFILIAL").toString());
		}
		
		if (map.get("IDEMPRESA") != null) {
			filialSuggestDTO.setIdEmpresa(Long.valueOf(map.get("IDEMPRESA").toString()));
		}
		
		if (map.get("NMEMPRESA") != null) {
			filialSuggestDTO.setNmEmpresa(map.get("NMEMPRESA").toString());
		}

		return filialSuggestDTO;
	}
	
	@Override
	protected FilialService getService() {
		return filialService;
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
}
