package com.mercurio.lms.tracking.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.tracking.Cities;
import com.mercurio.lms.tracking.City;
import com.mercurio.lms.tracking.util.AliasCity;
import com.thoughtworks.xstream.XStream;

/**
 * Classe de servico para tracking:
 * 
 * 
 * O valor do <code>id</code> informado abaixo deve ser utilizado para
 * referenciar este servico.
 * 
 * @spring.bean id="lms.tracking.cityTrackingService"
 */
@ServiceSecurity
public class CityTrackingService {

	private MunicipioService municipioService;

	@MethodSecurity(processGroup = "tracking.cityTrackingService", processName = "findCityByState", authenticationRequired = false)
	public Map<String, Object> findCityByState(TypedFlatMap tfm) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Municipio> municipioList = municipioService.findByIdUnidadeFederativa(tfm.getLong("idState"));

		Cities cities = new Cities();
		for (Municipio municipio : municipioList) {
			City city = new City(municipio.getIdMunicipio(), municipio.getNmMunicipio());
			cities.addCity(city);
		}

		XStream xStream = AliasCity.createAlias();
		String xml = xStream.toXML(cities);

		map.put("xml", xml);
		map.put("isXml", true);

		return map;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
}