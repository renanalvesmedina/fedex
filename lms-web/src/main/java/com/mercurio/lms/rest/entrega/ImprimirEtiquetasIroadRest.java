package com.mercurio.lms.rest.entrega;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.entrega.model.service.DoctoServicoIroadService;

@Path("/entrega/imprimirEtiquetasIroad")
public class ImprimirEtiquetasIroadRest extends BaseRest{
	
	@InjectInJersey
	private DoctoServicoIroadService doctoServicoIroadService;
	
	@POST
	@Path("find")
    public Response find(TypedFlatMap criteria) throws Exception {
	    String codigoBarras = (String) criteria.get("codigoBarras");
	    Long impressora = Long.valueOf(((Map) criteria.get("impressora")).get("idImpressora").toString());
	    doctoServicoIroadService.findImprimirEtiquetaIroad(impressora, codigoBarras);
		return Response.ok().build();
    }
	
}