package com.mercurio.lms.rest.tabeladeprecos;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.tabelaprecos.report.EmitirRelatorioTabelaDePrecosService;
 
@Path("/tabeladeprecos/relatorioTabelaDePrecos") 
public class RelatorioTabelaDePrecosRest extends BaseRest { 
	
	
	@InjectInJersey ReportExecutionManager reportExecutionManager;
	@InjectInJersey EmitirRelatorioTabelaDePrecosService emitirRelatorioTabelaDePrecosService;

	@POST
	@Path("gerarRelatorio")
	public Response gerarRelatorio(Map<String,Object> params) throws Exception{
		
		params = extraiIds(params);
		
		Map<String, String> retorno = new HashMap<String, String>();
		String fileName = reportExecutionManager.generateReportLocator(emitirRelatorioTabelaDePrecosService, params);
		retorno.put("fileName", fileName);
		return Response.ok(retorno).build();
	}

	private Map<String, Object> extraiIds(Map<String, Object> criteria) {
		Map<String,Object> parameters = new HashMap<String, Object>();
		
		for(String key : criteria.keySet()){
			parameters = getValue(criteria, key,      parameters);
		}

		return parameters;
	}
	

    private Map<String,Object> getValue(Map<String,Object> params, String key, Map<String,Object> parameters){
		if(params.get(key) != null){
			parameters.put(key, ((Map)params.get(key)).get("id"));
		}
		
		return parameters;
	}
} 
