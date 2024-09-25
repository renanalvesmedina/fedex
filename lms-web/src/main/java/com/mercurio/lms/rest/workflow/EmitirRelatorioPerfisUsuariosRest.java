package com.mercurio.lms.rest.workflow;

import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.model.service.PerfilService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;

@Path("/workflow/emitirRelatorioPerfisUsuarios")
public class EmitirRelatorioPerfisUsuariosRest {
	
	@InjectInJersey
	PerfilService perfilService;
	
	@InjectInJersey
	ReportExecutionManager reportExecutionManager;
	
	@POST
	@Path("find")
    public Response find(TypedFlatMap criteria) {
		List<Map<String, Object>> listForCSV = perfilService.findRelatorioPerfisUsuarios(criteria);
		String reportLocator = reportExecutionManager.generateReportLocator(listForCSV, Boolean.TRUE);
		
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("reportLocator", reportLocator);
		
		return Response.ok(retorno).build();
    }
}