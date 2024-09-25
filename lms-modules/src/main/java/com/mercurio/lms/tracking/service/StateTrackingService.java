package com.mercurio.lms.tracking.service;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tracking.State;
import com.mercurio.lms.tracking.States;
import com.mercurio.lms.tracking.util.AliasState;
import com.thoughtworks.xstream.XStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de servico para tracking:
 * 
 * 
 * O valor do <code>id</code> informado abaixo deve ser utilizado para
 * referenciar este servico.
 * 
 * @spring.bean id="lms.tracking.stateTrackingService"
 */
@ServiceSecurity
public class StateTrackingService {

	private final Long ID_PAIS_BRASIL = 30L;
	private UnidadeFederativaService unidadeFederativaService;

	/**
	 * 
	 * @param mapNaoUtilizado Foi necessário acrescentar este parâmetro devido a alteração do Jira ADSM-256, o mesmo será removido futuramente.
	 * @return
	 */
	@MethodSecurity(processGroup = "tracking.stateTrackingService", processName = "findStates", authenticationRequired = false)
	public Map<String, Object> findStates(TypedFlatMap mapNaoUtilizado) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Map> unidadeFederativaListMap = unidadeFederativaService.findUfsAtivasByPais(ID_PAIS_BRASIL);

		States states = new States();
		for (Map mapUnidadeFederativa : unidadeFederativaListMap) {
			State state = new State((Long) mapUnidadeFederativa.get("idUnidadeFederativa"), (String) mapUnidadeFederativa.get("nmUnidadeFederativa"),
					(String) mapUnidadeFederativa.get("sgUnidadeFederativa"));
			states.addState(state);
		}

		XStream xStream = AliasState.createAlias();
		String xml = xStream.toXML(states);

		map.put("xml", xml);
		map.put("isXml", true);

		return map;
	}

	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
}