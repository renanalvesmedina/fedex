package com.mercurio.lms.rest.vendas;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.CotacaoService;

@Path("/vendas/cotacao")
public class CotacaoRest {

	@InjectInJersey CotacaoService cotacaoService;
	
	@POST
	@Path("findCotacaoSuggest")
	public Response findCotacaoSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		return findCotacaoSuggest(value, SessionUtils.getEmpresaSessao().getIdEmpresa());
	}
	
	public Response findCotacaoSuggest(String val, Long idEmpresa) {

		if (StringUtils.isNotBlank(val)) {
			
			val = val.replace(" ", "");
			
			if (val.length() >= 4) {
				
				String sgFilial = val.substring(0, 3).toUpperCase();
				
				String sNrCotacao = val.substring(3);
				
				if (StringUtils.isNumeric(sNrCotacao)) {
					
					Long nrCotacao = Long.valueOf(sNrCotacao);
					
					return Response.ok(cotacaoService.findCotacaoSuggest(sgFilial, nrCotacao, idEmpresa)).build();
					
				}
				
			}
		
		}
		
		return Response.ok().build();

	}
	
}
