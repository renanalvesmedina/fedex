package com.mercurio.lms.rnc.swt.action;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rnc.model.service.RNCFaltaIndevidaService;
import com.mercurio.lms.util.session.SessionUtils;

public class ConsultarRNCFaltaIndevidaAction extends CrudAction {
	public void setService(RNCFaltaIndevidaService rncFaltaIndevidaService) {
		setDefaultService( rncFaltaIndevidaService );
	}
	
	public Map<String, Object> findDadosSessao() {
		Map<String, Object> retorno = new HashMap<String, Object>();
		Filial filial = SessionUtils.getFilialSessao();
		retorno.put("sgFilial", filial.getSgFilial());
		retorno.put("idFilial", filial.getIdFilial());
		retorno.put("nmFantasia", filial.getPessoa().getNmFantasia());
		return retorno;
	}

	public ResultSetPage findFaltaIndevidaPaginated(Map criteria) {
		return ((RNCFaltaIndevidaService) defaultService).findFaltaIndevidaPaginated(criteria);
	}
}