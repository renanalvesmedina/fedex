package com.mercurio.lms.util.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.model.service.DoctoServicoLookupService;

/**
 *  
 * @spring.bean id="lms.util.swt.adsmDocumentoServicoAction"
 */
public class AdsmDocumentoServicoAction {

	private FilialService filialService;
	private DoctoServicoLookupService doctoServicoLookupService;
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setDoctoServicoLookupService(DoctoServicoLookupService doctoServicoLookupService) {
		this.doctoServicoLookupService = doctoServicoLookupService;
	}

	public List findLookupBySgFilial(Map criteria) {		
		List listResult = filialService.findLookupBySgFilial((String)criteria.get("sgFilial"), null);
		
		List listFilial = new ArrayList();
		for (Iterator iter = listResult.iterator(); iter.hasNext();) {
			Map mapResult = (Map) iter.next();
			Map mapFilial = new HashMap();
			
			mapFilial.put("idFilial", mapResult.get("idFilial"));
			mapFilial.put("sgFilial", mapResult.get("sgFilial"));
			Map pessoa = (Map)mapResult.get("pessoa");
			mapFilial.put("nmFantasia", pessoa.get("nmFantasia"));
			
			listFilial.add(mapFilial);
		}
			
		return listFilial;
	}
	
	/**
	 * 
	 * @param criteria ==> tpDocumentoServico; idFilialOrigem; nrDoctoServico
	 * @return
	 */
	public List findDoctoServicoByTpDoctoServicoByIdFilialOrigemByNumeroDoctoServico(TypedFlatMap criteria) {
		criteria.put("idFilialOrigem", String.valueOf(criteria.get("idFilialOrigem")));
		criteria.put("nrDoctoServico", String.valueOf(criteria.get("nrDoctoServico")));
		criteria.put("dvConhecimento", String.valueOf(criteria.get("dvConhecimento")));
		return doctoServicoLookupService.findDoctoServicoByTpDoctoServicoByIdFilialOrigemByNumeroDoctoServico(criteria);
	}
	
}
