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
import com.mercurio.lms.rest.vol.dto.request.FindEntregasRequestDTO;
import com.mercurio.lms.rest.vol.dto.response.FindEntregasResponseDTO;
import com.mercurio.lms.vol.model.service.VolTransmitirEntregasService;

@Public
@Path("/vol/transmitirentregas")
public class VolTransmitirEntregasRest extends BaseRest {
	
	@InjectInJersey
	private AutenticacaoService autenticacaoService;
	
	private static final Logger LOGGER = LogManager.getLogger(VolTransmitirEntregasRest.class);

	@InjectInJersey
	private VolTransmitirEntregasService volTransmitirEntregasService;

	@POST
	@Path("findEntregas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findEntregas(@HeaderParam(value = "token") String token, @Valid FindEntregasRequestDTO findEntregasRequestDTO) {
		FindEntregasResponseDTO response = new FindEntregasResponseDTO();
		Map<String, Object> retorno = new HashMap<String, Object>();
		TypedFlatMap map = new TypedFlatMap();
		
		try {

			autenticacaoService.validarTokenOkta(token);
			
			map.put("idControleCarga",
					findEntregasRequestDTO.getIdControleCarga());
			map.put("idEquipamento", findEntregasRequestDTO.getIdEquipamento());

			retorno = volTransmitirEntregasService.findEntregas(map);
			
			response.setSucesso(true);
			response.setMensagem("Sucesso ao buscar as entregas");
			response.setRetorno(retorno);
		}catch (NotAuthorizedException ex) {
			return Response.status(401).build();
		} catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
			response.setSucesso(false);
			response.setMensagem("Falha ao buscar as entregas");
		}
		
		return Response.ok(response).build();
	}
}
