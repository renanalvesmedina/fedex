package com.mercurio.lms.rest.vendas;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.tabelaprecos.report.EmitirRelatorioClientesDivisaoService;

@Path("/vendas/infoReajusteCliente")
public class InfoReajusteClienteRest extends BaseRest {

	@InjectInJersey ReportExecutionManager reportExecutionManager;
	@InjectInJersey EmitirRelatorioClientesDivisaoService emitirRelatorioClientesDivisaoService;
	
	@POST
	@Path("imprimirRelatorioClientes")
	public Response imprimirRelatorioClientes(Map<String,Object> params) throws Exception {
		Map<String, String> retorno = new HashMap<String, String>();
		String fileName = reportExecutionManager.generateReportLocator(emitirRelatorioClientesDivisaoService, params);
		retorno.put("fileName", fileName);
		return Response.ok(retorno).build();
	}

}
