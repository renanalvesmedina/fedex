package com.mercurio.lms.tributos.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.service.AliquotaIcmsAereoLogService;

/**
 * @spring.bean id="lms.tributos.consultarAliquotaIcmsAereoLogAction"
 */
public class ConsultarAliquotaIcmsAereoLogAction extends CrudAction {

	private PaisService paisService;
	private UnidadeFederativaService unidadeFederativaService;

	public void setService(AliquotaIcmsAereoLogService service) {

		this.defaultService = service;
	}
	
	public List findUnidadeFederativa(Map criteria){
		
		Map map = new HashMap();
		map.put("sgPais","BRA");
		
		Pais paisBrasil = null;
		
		List paises = paisService.find(map);
		
		if( paises != null && !paises.isEmpty() ){
			
			paisBrasil = (Pais) paises.get(0);
		
			String tpSituacao = (criteria != null && criteria.containsKey("tpSituacao")) ? (String) criteria.get("tpSituacao") : null;
		
			return getUnidadeFederativaService().findUfsByPais(paisBrasil.getIdPais(),tpSituacao);
			
		} else {
			return null;
		}
	}
	
	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
}