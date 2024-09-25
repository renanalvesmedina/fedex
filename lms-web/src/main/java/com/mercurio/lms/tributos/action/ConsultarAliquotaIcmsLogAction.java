package com.mercurio.lms.tributos.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.service.AliquotaIcmsLogService;
import com.mercurio.lms.util.session.SessionKey;

/**
 * @spring.bean id="lms.tributos.consultarAliquotaIcmsLogAction"
 */
public class ConsultarAliquotaIcmsLogAction extends CrudAction {
	
	private UnidadeFederativaService unidadeFederativaService;

	public void setService(AliquotaIcmsLogService service) {

		this.defaultService = service;
	}
	
	public List findUnidadeFederativaPaisLogado(TypedFlatMap criteria){
		Pais p = (Pais)SessionContext.get(SessionKey.PAIS_KEY);
		if (criteria == null)
			criteria = new TypedFlatMap();
		criteria.put("pais.idPais",p.getIdPais().toString());
		return getUnidadeFederativaService().findByPais(criteria);
	}
	
	public UnidadeFederativaService getUnidadeFederativaService() {
		
		return unidadeFederativaService;
	}
	
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		
		this.unidadeFederativaService = unidadeFederativaService;
	}
}