package com.mercurio.lms.tracking.service;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.tracking.Depot;
import com.mercurio.lms.tracking.Depots;
import com.mercurio.lms.tracking.util.AliasDepot;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de servico para tracking:   
 *
 * 
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servico.
 * @spring.bean id="lms.tracking.filialTrackingService"
 */
@ServiceSecurity
public class FilialTrackingService {
	
	private FilialService filialService;

	
	@MethodSecurity(processGroup = "tracking.filialTrackingService", processName = "findDepotByStateAndCity", authenticationRequired = false)
	public Map<String, Object> findDepotByStateAndCity(TypedFlatMap tfm) {
		List<Depot> listaDepots = filialService.findDepotByStateAndCity(tfm);
		Depots depots = new Depots();
		depots.addAllDepots(listaDepots);
		return convertToXml(depots);
	}
	
	/**
	 * 
	 * @param mapNaoUtilizado Foi necessário acrescentar este parâmetro devido a alteração do Jira ADSM-256, o mesmo será removido futuramente.
	 * @return
	 */
	@MethodSecurity(processGroup = "tracking.filialTrackingService", processName = "findDepot", authenticationRequired = false)
	public Map<String, Object> findDepot(TypedFlatMap mapNaoUtilizado) {
		List<Depot> listaDepots = filialService.findDepot();
		Depots depots = new Depots();
		depots.addAllDepots(listaDepots);
		return convertToXml(depots);
	}
	
	/**
	 * 
	 * @param mapNaoUtilizado Foi necessário acrescentar este parâmetro devido a alteração do Jira ADSM-256, o mesmo será removido futuramente.
	 * @return
	 */
	@MethodSecurity(processGroup = "tracking.filialTrackingService", processName = "findFiliais", authenticationRequired = false)
	public Map<String, Object> findFiliais(TypedFlatMap mapNaoUtilizado) {
		List<Filial> filialList = filialService.findListFilial();
		
		Depots depots = new Depots();
		for (Filial filial : filialList) {
			Depot depot = createDepot(filial);
			depots.addDepot(depot);
		}
		
		return convertToXml(depots);
	}
	
	private Map<String, Object> convertToXml(Depots depots){
		Map<String, Object> map = new HashMap<String, Object>();
		XStream xStream = AliasDepot.createAlias();
		String xml = xStream.toXML(depots);
		map.put("xml", xml);
		map.put("isXml", true);
		return map;
	}
	
	private Depot createDepot(Filial filial){
		Depot depot = new Depot();
		depot.setCode(filial.getIdFilial());
		depot.setDescription( StringUtils.isBlank(filial.getPessoa().getNmFantasia()) ? "" : filial.getPessoa().getNmFantasia() );
		depot.setAcronym( StringUtils.isBlank(filial.getSgFilial()) ? "" : filial.getSgFilial() );
		return depot;		
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}