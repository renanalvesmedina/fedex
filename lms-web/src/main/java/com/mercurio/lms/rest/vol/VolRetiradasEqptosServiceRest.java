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
import com.mercurio.lms.rest.vol.dto.request.ExecuteControleEquipamentoRequestDTO;
import com.mercurio.lms.rest.vol.dto.response.ExecuteControleEquipamentoResponseDTO;
import com.mercurio.lms.vol.model.service.VolRetiradasEqptosService;

@Public
@Path("/vol/retiradaseqptos")
public class VolRetiradasEqptosServiceRest extends BaseRest {
	
	@InjectInJersey
	private AutenticacaoService autenticacaoService;
	
	private static final Logger LOGGER = LogManager.getLogger(VolRetiradasEqptosServiceRest.class);

	@InjectInJersey
	private VolRetiradasEqptosService volRetiradasEqptosService;

	@POST
	@Path("executeControleEquipamento")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeControleEquipamento(@HeaderParam(value = "token") String token, @Valid ExecuteControleEquipamentoRequestDTO executeControleEquipamentoRequestDTO) {
		ExecuteControleEquipamentoResponseDTO response = new ExecuteControleEquipamentoResponseDTO();
		TypedFlatMap map = new TypedFlatMap();

		try {

			autenticacaoService.validarTokenOkta(token);
			
			map.put("idFilial",
					executeControleEquipamentoRequestDTO.getIdFilial());
			map.put("imei", executeControleEquipamentoRequestDTO.getImei());
			map.put("dhRetirada",
					executeControleEquipamentoRequestDTO.getDhRetirada());
			map.put("idMotorista",
					executeControleEquipamentoRequestDTO.getIdMotorista());
			map.put("idFrota",
					executeControleEquipamentoRequestDTO.getIdFrota());

			volRetiradasEqptosService.executeControleEquipamento(map);
			
			response.setSucesso(true);
			response.setMensagem("Controle equipamento executado com sucesso.");
		}catch (NotAuthorizedException ex) {
			return Response.status(401).build();
		} catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
			response.setSucesso(false);
			response.setMensagem("Falha ao executar o controle equipamento.");
		}
		return Response.ok(response).build();
	}

}
