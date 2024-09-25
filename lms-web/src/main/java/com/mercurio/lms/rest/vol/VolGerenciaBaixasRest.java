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
import com.mercurio.lms.rest.vol.dto.request.GerenciaBaixaRequestDTO;
import com.mercurio.lms.rest.vol.dto.response.GerenciaBaixaResponseDTO;
import com.mercurio.lms.vol.model.service.VolGerenciaBaixasService;

@Public
@Path("/vol/gerenciabaixas")
public class VolGerenciaBaixasRest extends BaseRest {
	
	private static final Logger LOGGER = LogManager.getLogger(VolGerenciaBaixasRest.class);

	@InjectInJersey
	private VolGerenciaBaixasService volGerenciaBaixasService;
	
	@InjectInJersey
	private AutenticacaoService autenticacaoService;

	@POST
	@Path("executeGerenciaBaixa")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeGerenciaBaixa(@HeaderParam(value = "token") String token, @Valid GerenciaBaixaRequestDTO gerenciaBaixaRequestDTO) {
		GerenciaBaixaResponseDTO response = new GerenciaBaixaResponseDTO();
		TypedFlatMap map = new TypedFlatMap();

		try {

			autenticacaoService.validarTokenOkta(token);
			
			map.put("idFilial", gerenciaBaixaRequestDTO.getIdFilial());
			map.put("tpBaixa", gerenciaBaixaRequestDTO.getTpBaixa());
			map.put("ids", gerenciaBaixaRequestDTO.getIds());			
			map.put("idDoctoServico", gerenciaBaixaRequestDTO.getIdDoctoServico());			
			map.put("image", gerenciaBaixaRequestDTO.getImage());			
			map.put("perguntasRiscoResposta", gerenciaBaixaRequestDTO.getPerguntasRiscoResposta());
			map.put("idEquipamento", gerenciaBaixaRequestDTO.getIdEquipamento());	
			map.put("qtVolumes", gerenciaBaixaRequestDTO.getQtVolumes());
			map.put("peso", gerenciaBaixaRequestDTO.getPeso());
			map.put("cdOcorrenciaColeta", gerenciaBaixaRequestDTO.getCdOcorrenciaColeta());
			map.put("dhEvento", gerenciaBaixaRequestDTO.getDhEvento());
			map.put("idManifesto", gerenciaBaixaRequestDTO.getIdManifesto());
			map.put("nmRecebedor", gerenciaBaixaRequestDTO.getNmRecebedor());
			map.put("cdOcorrencia", gerenciaBaixaRequestDTO.getCdOcorrencia());
			map.put("tpEntregaParcial", gerenciaBaixaRequestDTO.getTpEntregaParcial());
			map.put("cdComplementoMotivo", gerenciaBaixaRequestDTO.getCdComplementoMotivo());
			map.put("dcma", gerenciaBaixaRequestDTO.getDcma());
			map.put("dhOcorrencia", gerenciaBaixaRequestDTO.getDhOcorrencia());	
			map.put("idPedidoColeta", gerenciaBaixaRequestDTO.getIdPedidoColeta());
			map.put("valor", gerenciaBaixaRequestDTO.getValor());
			
			volGerenciaBaixasService.executeGerenciaBaixa(map);
			
			response.setSucesso(true);
			response.setMensagem("Gerencia baixa executada com sucesso.");
		}catch (NotAuthorizedException ex) {
			return Response.status(401).build();
		}catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
			response.setSucesso(false);
			response.setMensagem("Ocorreu um erro ao executar a gerencia baixa.");
		}
		return Response.ok(response).build();
	}
	
}
