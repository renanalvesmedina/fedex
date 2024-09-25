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
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.model.service.AutenticacaoService;
import com.mercurio.lms.rest.vol.dto.request.PingConexaoRequestDTO;
import com.mercurio.lms.rest.vol.dto.request.StoreUptimeConexaoRequestDTO;
import com.mercurio.lms.rest.vol.dto.response.PingConexaoResponseDTO;
import com.mercurio.lms.rest.vol.dto.response.StoreUptimeConexaoResponseDTO;
import com.mercurio.lms.vol.model.service.VolUptimeConexaoService;

@Public
@Path("/vol/uptimeconexao")
public class VolUptimeConexaoRest {
	
	@InjectInJersey
	private AutenticacaoService autenticacaoService;
	
	private static final long serialVersionUID = 1L;	

	private static final Logger LOGGER = LogManager.getLogger(VolUptimeConexaoRest.class);

	@InjectInJersey
	private VolUptimeConexaoService volUptimeConexaoService;

	@POST
	@Path("pingConexao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pingConexao(@HeaderParam(value = "token") String token, @Valid PingConexaoRequestDTO pingConexaoRequestDTO) {
		PingConexaoResponseDTO response = new PingConexaoResponseDTO();
		TypedFlatMap map = new TypedFlatMap();
	
		try {

			autenticacaoService.validarTokenOkta(token);
			
			volUptimeConexaoService.pingConexao(map);
			
			response.setSucesso(true);
			response.setMensagem("Sucesso ao executar o ping da conexão.");
		}catch (NotAuthorizedException ex) {
			return Response.status(401).build();
		} catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
			response.setSucesso(false);
			response.setMensagem("Falha ao executar o ping da conexão.");
		}
		return Response.ok(response).build();
	}

	@POST
	@Path("storeUptimeConexao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response storeUptimeConexao(@HeaderParam(value = "token") String token,
									   @Valid StoreUptimeConexaoRequestDTO storeUptimeConexaoRequestDTO) {
		StoreUptimeConexaoResponseDTO response = new StoreUptimeConexaoResponseDTO();		
		TypedFlatMap map = new TypedFlatMap();
		
		try {
			
			autenticacaoService.validarTokenOkta(token);
			
			map.put("idFilial", storeUptimeConexaoRequestDTO.getIdFilial());
			map.put("dhInicioChamada",
					storeUptimeConexaoRequestDTO.getDhInicioChamada());
			map.put("httpCode", storeUptimeConexaoRequestDTO.getHttpCode());
			map.put("tempoExecucao",
					storeUptimeConexaoRequestDTO.getTempoExecucao());
			map.put("idEquipamento",
					storeUptimeConexaoRequestDTO.getIdEquipamento());
			map.put("idFrota", storeUptimeConexaoRequestDTO.getIdFrota());
			map.put("latitude", storeUptimeConexaoRequestDTO.getLatitude());
			map.put("longitude", storeUptimeConexaoRequestDTO.getLongitude());
			
			volUptimeConexaoService.storeUptimeConexao(map);
			
			response.setSucesso(true);
			response.setMensagem("Sucesso ao executar o store uptime conexão.");
		} catch (NotAuthorizedException ex) {
			return Response.status(401).build();
		} catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
			response.setSucesso(false);
			response.setMensagem("Falha ao executar o store uptime conexão.");
		}
		return Response.ok(response).build();
	}	
}
