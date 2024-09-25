package com.mercurio.lms.rest.coleta;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/coleta/manifestoColeta")
public class ManifestoColetaRest {
	
	@InjectInJersey ManifestoColetaService manifestoColetaService;

	@POST
	@Path("findManifestoColetaSuggest")
	public Response findManifestoColetaSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		return findManifestoColetaSuggest(value, SessionUtils.getEmpresaSessao().getIdEmpresa());
	}
	
	private Response findManifestoColetaSuggest(@QueryParam("value") String val, Long idEmpresa) {

		if (StringUtils.isNotBlank(val)) {
			
			val = val.replace(" ", "");
			
			if (val.length() >= 4) {
				
				String sgFilial = val.substring(0, 3).toUpperCase();
				
				String sNrManifestoColeta = val.substring(3);
				
				if (StringUtils.isNumeric(sNrManifestoColeta)) {
					
					Long nrManifestoColeta = Long.valueOf(sNrManifestoColeta);
					
					return Response.ok(manifestoColetaService.findManifestoColetaSuggest(sgFilial, nrManifestoColeta, idEmpresa)).build();
					
				}
				
			}
		
		}
		
		return Response.ok().build();
		
	}
	
}
