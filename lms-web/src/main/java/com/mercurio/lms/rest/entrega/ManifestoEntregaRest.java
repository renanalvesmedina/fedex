package com.mercurio.lms.rest.entrega;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/entrega/manifestoEntrega")
public class ManifestoEntregaRest {
	
	@InjectInJersey ManifestoEntregaService manifestoEntregaService;

	@InjectInJersey
	private IntegracaoJwtService integracaoJwtService;

	@POST
	@Path("findManifestoEntregaSuggest")
	public Response findManifestoEntregaSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		return findManifestoEntregaSuggest(value, SessionUtils.getEmpresaSessao().getIdEmpresa());
	}

	@POST
	@Path("findManifestoEntregaSuggestByToken")
	public Response findManifestoEntregaSuggestByToken(Map<String, Object> data, @Context HttpHeaders headers) {
		String value = MapUtils.getString(data, "value");
		return findManifestoEntregaSuggest(value, integracaoJwtService.getIdEmpresaByToken(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0)));
	}
	
	private Response findManifestoEntregaSuggest(@QueryParam("value") String val, Long idEmpresa) {

		if (StringUtils.isNotBlank(val)) {
			
			val = val.replace(" ", "");
			
			if (val.length() >= 4) {
				
				String sgFilial = val.substring(0, 3).toUpperCase();
				
				String sNrManifestoEntrega = val.substring(3);
				
				if (StringUtils.isNumeric(sNrManifestoEntrega)) {
					
					Long nrManifestoEntrega = Long.valueOf(sNrManifestoEntrega);
					
					return Response.ok(manifestoEntregaService.findManifestoEntregaSuggest(sgFilial, nrManifestoEntrega, idEmpresa)).build();
					
				}
				
			}
		
		}
		
		return Response.ok().build();
		
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
}
