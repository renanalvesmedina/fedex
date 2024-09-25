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
import com.mercurio.lms.rest.vol.dto.request.ExecuteInformarOcorrenciaRequestDTO;
import com.mercurio.lms.rest.vol.dto.response.ExecuteInformarOcorrenciaResponseDTO;
import com.mercurio.lms.vol.model.service.VolInformarOcorrenciasService;

@Public
@Path("/vol/informarocorrencias")
public class VolInformarOcorrenciasRest extends BaseRest {

	private static final Logger LOGGER = LogManager.getLogger(VolInformarOcorrenciasRest.class);

	@InjectInJersey
	private AutenticacaoService autenticacaoService;

	@InjectInJersey
	private VolInformarOcorrenciasService volInformarOcorrenciasService;

	@POST
	@Path("executeInformarOcorrencia")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeInformarOcorrencia(@HeaderParam(value = "token") String token, @Valid ExecuteInformarOcorrenciaRequestDTO executeInformarOcorrenciaRequestDTO) {
		ExecuteInformarOcorrenciaResponseDTO response = new ExecuteInformarOcorrenciaResponseDTO();
		TypedFlatMap map = new TypedFlatMap();

		try {

			autenticacaoService.validarTokenOkta(token);
			
			map.put("idFilial",
					executeInformarOcorrenciaRequestDTO.getIdFilial());
			map.put("idMeioTransporte",
					executeInformarOcorrenciaRequestDTO.getIdMeioTransporte());
			map.put("idControleCarga",
					executeInformarOcorrenciaRequestDTO.getIdControleCarga());
			map.put("idConhecimento",
					executeInformarOcorrenciaRequestDTO.getIdConhecimento());
			map.put("cdEvento",
					executeInformarOcorrenciaRequestDTO.getCdEvento());
			map.put("idPedidoColeta",
					executeInformarOcorrenciaRequestDTO.getIdPedidoColeta());
			map.put("usuarioSessao",
					executeInformarOcorrenciaRequestDTO.getUsuarioSessao());
			map.put("dhSolicitacao",
					executeInformarOcorrenciaRequestDTO.getDhSolicitacao());
			map.put("volOkta", true);

			volInformarOcorrenciasService.executeInformarOcorrencia(map);
			
			response.setSucesso(true);
			response.setMensagem("Ocorrência informada com sucesso.");
		}catch (NotAuthorizedException ex) {
			return Response.status(401).build();
		} catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
			response.setSucesso(false);
			response.setMensagem("Ocorreu uma falha ao informar uma ocorrência.");
		}
		return Response.ok(response).build();
	}

}
