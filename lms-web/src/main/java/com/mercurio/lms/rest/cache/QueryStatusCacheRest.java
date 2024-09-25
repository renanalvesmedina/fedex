package com.mercurio.lms.rest.cache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;

@Public
@Path("/queryStatusCache")
public class QueryStatusCacheRest {
	private static String CHAVE_CONSULTA_CACHE = "paramConsultaCache";

	@InjectInJersey ParametroGeralService parametroGeralService;
	
	@GET
	@Path("verifyCache")
	public Response verifyCache(){
		Response response = null;
		try {
			String consultaCacheValue = parametroGeralService.findSimpleConteudoByNomeParametro(CHAVE_CONSULTA_CACHE);
			if (!StringUtils.isEmpty(consultaCacheValue)){
				response = Response.ok().build();
			}
		} catch(BusinessException bs) {
			response = Response.status(Response.Status.NOT_FOUND).build(); 		
		}
		
		return response;
	}
	
}
