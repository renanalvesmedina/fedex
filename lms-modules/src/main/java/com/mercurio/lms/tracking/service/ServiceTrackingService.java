package com.mercurio.lms.tracking.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.tracking.Service;
import com.mercurio.lms.tracking.Services;
import com.mercurio.lms.tracking.util.AliasService;
import com.thoughtworks.xstream.XStream;

/**
 * Classe de servico para tracking:
 * 
 * 
 * O valor do <code>id</code> informado abaixo deve ser utilizado para
 * referenciar este servico.
 * 
 * @spring.bean id="lms.tracking.serviceTrackingService"
 */
@ServiceSecurity
public class ServiceTrackingService {

	private ServicoService servicoService;

	@SuppressWarnings("unchecked")
	@MethodSecurity(processGroup = "tracking.serviceTrackingService", processName = "findServices", authenticationRequired = false)
	public Map<String, Object> findServices() {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Map> servicosListMap = servicoService.findByTpAbrangencia("N");

		Services services = new Services();
		for (Map mapServico : servicosListMap) {
			services.addService(createService(mapServico));
		}

		XStream xStream = AliasService.createAlias();
		String xml = xStream.toXML(services);

		map.put("xml", xml);
		map.put("isXml", true);

		return map;
	}

	private Service createService(Map mapServico) {
		Service service = new Service();
		service.setId((Long) mapServico.get("idServico"));

		VarcharI18n dsServicoI18n = (VarcharI18n) mapServico.get("dsServico");
		if (dsServicoI18n != null) {
			service.setDescription(dsServicoI18n.getValue());
		}

		return service;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
}