package com.mercurio.lms.rest.carregamento;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.service.CIOTService;
import com.mercurio.lms.rest.carregamento.dto.EmitirRelatorioCIOTFilterDTO;
import com.mercurio.lms.util.JTDateTimeUtils;
 
@Path("/carregamento/emitirRelatorioCIOT") 
public class EmitirRelatorioCIOTRest { 
 
	@InjectInJersey 
	CIOTService ciotService;
	
	@POST
	@Path("find")
    public Response find(EmitirRelatorioCIOTFilterDTO emitirRelatorioCIOTFilterDTO) {
		TypedFlatMap criteria = getMap(emitirRelatorioCIOTFilterDTO);
		
		String reportLocator = ciotService.findRelatorioCIOT(criteria);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("reportLocator", reportLocator);
		return Response.ok(retorno).build();
    }

	private TypedFlatMap getMap(EmitirRelatorioCIOTFilterDTO emitirRelatorioCIOTFilterDTO) {
		TypedFlatMap criteria = new TypedFlatMap();
		
		criteria.put("idFilial", emitirRelatorioCIOTFilterDTO.getFilial().getIdFilial());
		criteria.put("periodoInicial", emitirRelatorioCIOTFilterDTO.getPeriodoInicial());
		criteria.put("periodoFinal", JTDateTimeUtils.getLastHourOfDay(emitirRelatorioCIOTFilterDTO.getPeriodoFinal()));
		if(emitirRelatorioCIOTFilterDTO.getTpSituacao() != null){
			criteria.put("tpSituacao", emitirRelatorioCIOTFilterDTO.getTpSituacao().getValue());
		}
		if(emitirRelatorioCIOTFilterDTO.getTpControleCarga() != null){
			criteria.put("tpControleCarga", emitirRelatorioCIOTFilterDTO.getTpControleCarga().getValue());
		}
		
		return criteria;
	}
} 
