package com.mercurio.lms.rest.tabeladeprecos;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.tabelaprecos.model.service.TrtService;

@Path("/tabeladeprecos/cloneTrtMunicipio")
public class CloneTrtMunicipioRest extends BaseRest {

	@InjectInJersey TrtService trtService;

	@POST
	@Path("cloneTrtTabela")
	public Response cloneTrtTabela(Map<String, Object> params){
		validaParametros(params);
		trtService.executeCloneTRTbyTabela(Long.valueOf(params.get("tabelaBase").toString()), Long.valueOf(params.get("tabelaNova").toString()));
		return Response.ok().build();
	}

	
	private void validaParametros(Map<String, Object> params) {
		if(params == null || params.get("tabelaBase") == null || params.get("tabelaNova") == null){
			throw new BusinessException("LMS-30079");
		}
	}

}
