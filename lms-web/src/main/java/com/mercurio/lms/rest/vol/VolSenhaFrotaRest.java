package com.mercurio.lms.rest.vol;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.model.service.AutenticacaoService;
import com.mercurio.lms.rest.vol.dto.request.ExecuteLoginRequestDTO;
import com.mercurio.lms.rest.vol.dto.response.ExecuteLoginResponseDTO;
import com.mercurio.lms.vol.model.service.VolSenhaFrotaService;

@Public
@Path("/vol/senhafrota")
public class VolSenhaFrotaRest extends BaseRest {
	
	@InjectInJersey
	private AutenticacaoService autenticacaoService;
	
	private static final Logger LOGGER = LogManager.getLogger(VolSenhaFrotaRest.class);

	@InjectInJersey
	private VolSenhaFrotaService volSenhaFrotaService;

	@POST
	@Path("executeLogin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeLogin(@HeaderParam(value = "token") String token, @Valid ExecuteLoginRequestDTO executeLoginRequestDTO) {
		ExecuteLoginResponseDTO response = new ExecuteLoginResponseDTO(); 
		Map<String, Object> retorno;
		TypedFlatMap map = new TypedFlatMap();
		
		try {

			autenticacaoService.validarTokenOkta(token);

			map.put("filial",
					executeLoginRequestDTO.getFilial());
			map.put("senha",
					executeLoginRequestDTO.getSenha());
			map.put("controleCarga",
					executeLoginRequestDTO.getControleCarga());
			map.put("versao",
					executeLoginRequestDTO.getVersao());
			map.put("versaoSO",
					executeLoginRequestDTO.getVersaoSO());
			map.put("dataHoraCelular",
					executeLoginRequestDTO.getDataHoraCelular());
			map.put("imei",
					executeLoginRequestDTO.getImei());
			map.put("volOkta", true);

			retorno = volSenhaFrotaService.executeLogin(map);
			
			response.setSucesso(true);
			response.setMensagem("Sucesso ao executar login.");
			response.setRetorno(retorno);
		}catch (NotAuthorizedException ex) {
			return Response.status(401).build();
		} catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
			response.setSucesso(false);
			response.setMensagem("Falha ao executar login.");
		}
		return Response.ok(response).build();
	}

	public VolSenhaFrotaService getVolSenhaFrotaService() {
		return volSenhaFrotaService;
	}

	public void setVolSenhaFrotaService(VolSenhaFrotaService volSenhaFrotaService) {
		this.volSenhaFrotaService = volSenhaFrotaService;
	}
}
