package com.mercurio.lms.rest.workflow;

import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.HistoricoWorkflowService;

@Path("/workflow/emitirRelatorioWorkflow")
public class EmitirRelatorioHistoricoWorkflowRest {

	@InjectInJersey 
	HistoricoWorkflowService historicoWorkflowService;
	
	@InjectInJersey 
	RegionalFilialService regionalFilialService;
	
	@InjectInJersey 
	RegionalService regionalService;
	
	@POST
	@Path("carregarValoresPadrao")
    public Response carregarValoresPadrao() {
		TypedFlatMap retorno = new TypedFlatMap();
		
		Filial filialLogada = SessionUtils.getFilialSessao();
		TypedFlatMap retornoFilial = new TypedFlatMap();
		retornoFilial.put("idFilial", filialLogada.getIdFilial());
		retornoFilial.put("sgFilial", filialLogada.getSgFilial());
		retornoFilial.put("nmFilial", filialLogada.getPessoa().getNmFantasia());
		retornoFilial.put("isBuscaValoresPadrao", Boolean.TRUE);
		retorno.put("filial", retornoFilial);
				
		List<Map<String,Object>> regionais = regionalService.findRegionaisVigentes();
		retorno.put("regionais", regionais);

		Regional regional = regionalFilialService.findLastRegionalVigente(filialLogada.getIdFilial());
		if(regional != null){
			retorno.put("idRegional", regional.getIdRegional());
		}

		return Response.ok(retorno).build();
    }
	
	
	@POST
	@Path("find")
    public Response find(TypedFlatMap criteria) {
		String reportLocator = historicoWorkflowService.findRelatorioHistoricoWorkflow(criteria);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("reportLocator", reportLocator);
		return Response.ok(retorno).build();
    }
	
	@POST
	@Path("findRegionalFilial")
    public Response findRegionalFilial(Long idFilial) {
		TypedFlatMap retorno = new TypedFlatMap();

		Regional regional = regionalFilialService.findLastRegionalVigente(idFilial);
		if(regional != null){
			retorno.put("idRegional", regional.getIdRegional());
		}
		
		return Response.ok(retorno).build();
    }
	
	
}