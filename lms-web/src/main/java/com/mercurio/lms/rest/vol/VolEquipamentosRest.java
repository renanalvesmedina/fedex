package com.mercurio.lms.rest.vol;


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
import com.mercurio.lms.rest.vol.dto.request.ValidaImeiRequestDTO;
import com.mercurio.lms.rest.vol.dto.response.ValidaImeiResponseDTO;
import com.mercurio.lms.vol.model.service.VolEquipamentosService;

@Public
@Path("/vol/equipamento")
public class VolEquipamentosRest extends BaseRest {
	
	@InjectInJersey
	private AutenticacaoService autenticacaoService;
	
	private static final Logger LOGGER = LogManager.getLogger(VolEquipamentosRest.class);

	@InjectInJersey
	private VolEquipamentosService volEquipamentosService;

	@POST
	@Path("executeValidaImei")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response executeValidaImei(@HeaderParam(value = "token") String token, @Valid ValidaImeiRequestDTO validaImeiRequestDto) {
		ValidaImeiResponseDTO response = new ValidaImeiResponseDTO();
		TypedFlatMap map = new TypedFlatMap();

		try {

			autenticacaoService.validarTokenOkta(token);
			
			map.put("filial", validaImeiRequestDto.getCdFilial());
			map.put("imei", validaImeiRequestDto.getImei());

			response.setSucesso(true);
			response.setRetorno(volEquipamentosService.executeValidaImei(map));
			response.setMensagem("Validação do Imei efetuada com sucesso.");
		}catch (NotAuthorizedException ex) {
			return Response.status(401).build();
		}catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
			response.setSucesso(false);
			response.setMensagem("Ocorreu um erro ao validar o Imei.");
		}
		return Response.ok(response).build();
	}
	
}
