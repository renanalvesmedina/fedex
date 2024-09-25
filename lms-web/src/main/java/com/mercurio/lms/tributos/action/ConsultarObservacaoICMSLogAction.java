package com.mercurio.lms.tributos.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.service.ObservacaoICMSLogService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.tributos.consultarObservacaoICMSLogAction"
 */
public class ConsultarObservacaoICMSLogAction extends CrudAction {
	
	private UnidadeFederativaService unidadeFederativaService; 

	public void setService(ObservacaoICMSLogService service) {

		this.defaultService = service;
	}
	
	public List findUf() {
		
		Pais pais = SessionUtils.getPaisSessao();
		
		return unidadeFederativaService.findUfsByPais(pais.getIdPais(), null);
	}
	
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {

		this.unidadeFederativaService = unidadeFederativaService;
	}
}