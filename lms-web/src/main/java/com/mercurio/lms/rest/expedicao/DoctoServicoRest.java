package com.mercurio.lms.rest.expedicao;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/expedicao/doctoServico")
public class DoctoServicoRest {

	@InjectInJersey DoctoServicoService doctoServicoService;

	@InjectInJersey
	private IntegracaoJwtService integracaoJwtService;
	
	@POST
	@Path("findDoctoServicoSuggest")
	public Response findDoctoServicoSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		return findDoctoServicoSuggest(value, SessionUtils.getEmpresaSessao().getIdEmpresa());
	}

	@POST
	@Path("/findDoctoServicoSuggestByToken")
	public Response findDoctoServicoSuggestByToken(Map<String, Object> data, @Context HttpHeaders headers) {
		String value = MapUtils.getString(data, "value");
		return findDoctoServicoSuggest(value, integracaoJwtService.getIdEmpresaByToken(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0)));
	}
	
	public Response findDoctoServicoSuggest(String val, Long idEmpresa) {

		if (StringUtils.isNotBlank(val)) {
			
			val = val.replace(" ", "");
			
			if (val.length() >= 4) {
				
				String sgFilial = val.substring(0, 3).toUpperCase();
				
				String sNrDoctoServico = val.substring(3);
				
				if (StringUtils.isNumeric(sNrDoctoServico)) {

					Long nrDoctoServico = Long.valueOf(sNrDoctoServico);
					
					return Response.ok(doctoServicoService.findDoctoServicoSuggest(sgFilial, nrDoctoServico, idEmpresa)).build();
					
				}
				
			}
		
		}
		
		return Response.ok().build();
		
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
}
