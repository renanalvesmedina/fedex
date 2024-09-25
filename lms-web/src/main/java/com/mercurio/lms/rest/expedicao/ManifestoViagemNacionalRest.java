package com.mercurio.lms.rest.expedicao;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import br.com.tntbrasil.integracao.domains.expedicao.SolicitarRelatatorioIroadsDMN;
import br.com.tntbrasil.integracao.domains.jms.HeaderParam;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.utils.EventoSistemaLmsType;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.expedicao.exception.RelatorioIroadsException;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/expedicao/manifestoViagemNacional")
public class ManifestoViagemNacionalRest {

	@InjectInJersey ManifestoViagemNacionalService manifestoViagemNacionalService;
	@InjectInJersey private IntegracaoJmsService integracaoJmsService;
	@InjectInJersey
	private IntegracaoJwtService integracaoJwtService;
	
	@POST
	@Path("findManifestoViagemNacionalSuggest")
	public Response findManifestoViagemNacionalSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		return findManifestoViagemNacionalSuggest(value, SessionUtils.getEmpresaSessao().getIdEmpresa());
	}

	@POST
	@Path("findManifestoViagemNacionalSuggestByToken")
	public Response findManifestoViagemNacionalSuggestByToken(Map<String, Object> data, @Context HttpHeaders headers) {
		String value = MapUtils.getString(data, "value");
		return findManifestoViagemNacionalSuggest(value, integracaoJwtService.getIdEmpresaByToken(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0)));
	}
	
	public Response findManifestoViagemNacionalSuggest(String val, Long idEmpresa) {

		if (StringUtils.isNotBlank(val)) {
			
			val = val.replace(" ", "");
			
			if (val.length() >= 4) {
				
				String sgFilial = val.substring(0, 3).toUpperCase();
				
				String sNrManifestoOrigem = val.substring(3);
				
				if (StringUtils.isNumeric(sNrManifestoOrigem)) {
					
					Long nrManifestoOrigem = Long.valueOf(sNrManifestoOrigem);
					
					return Response.ok(manifestoViagemNacionalService.findManifestoViagemNacionalSuggest(sgFilial, nrManifestoOrigem, idEmpresa)).build();
					
				}
				
			}
		
		}
		
		return Response.ok().build();
		
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "solicitarRelatoioIroads")
	public Response solicitarRelatorioIroads(SolicitarRelatatorioIroadsDMN solicitarRelatatorioIroadsDMN)
			throws RelatorioIroadsException {

		Map<String, String> response = new HashMap<>();
		response.put("status", Response.Status.OK.toString());
		response.put("message", "Solicitação realizada com sucesso");

		this.manifestoViagemNacionalService.validationFieldsSolicitationReportIroads(solicitarRelatatorioIroadsDMN);

		IntegracaoJmsService.JmsMessageSender msg =
				integracaoJmsService.createMessage(Queues.EVENTO_SISTEMA_LMS, solicitarRelatatorioIroadsDMN);
		msg.addHeader(HeaderParam.EVENT_TYPE.getName(), EventoSistemaLmsType.SOLICITAR_RELATORIO_IROADS.getName());
		integracaoJmsService.storeMessage(msg);

		return Response.ok(response).build();
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
}
