package com.mercurio.lms.vol.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.DominioVolDto;
/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volDominioService"
 */
@ServiceSecurity
public class VolDominioService {
	
	private DomainService domainService;
	
	@MethodSecurity(processGroup = "vol.volDominioService", processName = "findDominios", authenticationRequired=false)
	public List<DominioVolDto> findDominios(TypedFlatMap map){
		
		String nmDominio = map.getString("chave");
				
		Domain dominio = domainService.findByName(nmDominio);
		
		List<DominioVolDto> dominiosValores = new ArrayList<DominioVolDto>();
		
		for (Object value : dominio.getDomainValues()) {
			DominioVolDto dominioVol = new DominioVolDto(nmDominio, ((DomainValue) value).getValue(), ((DomainValue)value).getDescriptionAsString());
			dominiosValores.add(dominioVol);
		}
		
		return dominiosValores;
	}
	
	@MethodSecurity(processGroup = "vol.volDominioService", processName = "findMapDominios", authenticationRequired=false)
	public Map findMapDominios(TypedFlatMap map){
		
		Map retorna = new HashMap();
		
		String nmDominio = map.getString("chave");
				
		Domain dominio = domainService.findByName(nmDominio);
		
		List<DominioVolDto> dominiosValores = new ArrayList<DominioVolDto>();
		
		for (Object value : dominio.getDomainValues()) {
			DominioVolDto dominioVol = new DominioVolDto(nmDominio, ((DomainValue) value).getValue(), ((DomainValue)value).getDescriptionAsString());
			dominiosValores.add(dominioVol);
		}
		
		retorna.put(nmDominio, dominiosValores);
		
		return retorna;
	}
	
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}
}
