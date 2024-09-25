package com.mercurio.lms.rest.vol;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import com.mercurio.lms.rest.vol.dto.request.FindColetasRequestDTO;
import com.mercurio.lms.rest.vol.dto.request.StoreConfirmaRecebimentoColetaRequestDTO;
import com.mercurio.lms.rest.vol.dto.response.FindColetasResponseDTO;
import com.mercurio.lms.rest.vol.dto.response.StoreConfirmaRecebimentoColetaResponseDTO;
import com.mercurio.lms.vol.model.service.VolTransmitirColetasService;

@Public
@Path("/vol/transmitircoletas")
public class VolTransmitirColetasRest extends BaseRest {
	
	@InjectInJersey
	private AutenticacaoService autenticacaoService;
	
	private static final Logger LOGGER = LogManager.getLogger(VolTransmitirColetasRest.class);

	@InjectInJersey
	private VolTransmitirColetasService volTransmitirColetasService;

	@POST
	@Path("findColetas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findColetas(@HeaderParam(value = "token") String token, @Valid FindColetasRequestDTO findColetasRequestDTO) {
		FindColetasResponseDTO response = new FindColetasResponseDTO();
		TypedFlatMap map = new TypedFlatMap();
		
		try {

			autenticacaoService.validarTokenOkta(token);
			
			map.put("idControleCarga",
					findColetasRequestDTO.getIdControleCarga());
			map.put("idEquipamento", findColetasRequestDTO.getIdEquipamento());

			response.setSucesso(true);
			response.setMensagem("Sucesso ao buscar as coletas.");
			response.setRetorno(volTransmitirColetasService.findColetas(map));
		}catch (NotAuthorizedException ex) {
			return Response.status(401).build();
		} catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
			response.setSucesso(false);
			response.setMensagem("Falha ao buscar as coletas.");
		}
		
		return Response.ok(response).build();
	}

	@POST
	@Path("storeConfirmaRecebimentoColeta")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response storeConfirmaRecebimentoColeta(@HeaderParam(value = "token") String token, StoreConfirmaRecebimentoColetaRequestDTO storeConfirmaRecebimentoColetaRequestDTO) {
		StoreConfirmaRecebimentoColetaResponseDTO response = new StoreConfirmaRecebimentoColetaResponseDTO();
		TypedFlatMap map = new TypedFlatMap();
				
		try {

			autenticacaoService.validarTokenOkta(token);
			
			map.put("idFilial",
					storeConfirmaRecebimentoColetaRequestDTO.getIdFilial());
			map.put("idPedidoColeta", storeConfirmaRecebimentoColetaRequestDTO
					.getIdPedidoColeta());
			map.put("dhConfirmacaoVol",
					storeConfirmaRecebimentoColetaRequestDTO
							.getDhConfirmacaoVol());

			volTransmitirColetasService.storeConfirmaRecebimentoColeta(map);
			
			response.setSucesso(true);
			response.setMensagem("Sucesso ao confirmar o recebimento da coleta.");
		}catch (NotAuthorizedException ex) {
			return Response.status(401).build();
		} catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
			response.setSucesso(false);
			response.setMensagem("Falha ao confirmar o recebimento da coleta.");
		}
		
		return Response.ok(response).build();
	}
}
