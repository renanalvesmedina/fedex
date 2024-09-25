package com.mercurio.lms.util.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.model.service.DoctoServicoLookupService;

/**
 *  
 * @spring.bean id="lms.util.swt.documentoServicoAction"
 */
public class DocumentoServicoAction {

	private FilialService filialService;
	private DoctoServicoLookupService doctoServicoLookupService;
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setDoctoServicoLookupService(DoctoServicoLookupService doctoServicoLookupService) {
		this.doctoServicoLookupService = doctoServicoLookupService;
	}

	public List findLookupFilial(Map criteria) {		
		List<Map<String, Object>> filiais = filialService.findLookupBySgFilial((String)criteria.get("sgFilial"), (String) criteria.get("tpAcesso"));
		if (filiais != null) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("idFilial", filial.get("idFilial"));
				mapFilial.put("sgFilial", filial.get("sgFilial"));
				Map pessoa = (Map) filial.get("pessoa");
				mapFilial.put("nmFantasia", pessoa.get("nmFantasia"));
				result.add(mapFilial);
			}
			return result;
		}
		return null;
	}
	
	/**
	 * 
	 * @param criteria ==> tpDocumentoServico; idFilialOrigem; nrDoctoServico
	 * @return
	 */
	public List findDoctoServico(Map criteria) {
		criteria.put("idFilialOrigem", criteria.get("idFilial"));
		criteria.put("nrDoctoServico", criteria.get("nrDoctoServico"));
		criteria.put("dvConhecimento", criteria.get("dvConhecimento"));
		return doctoServicoLookupService.findDoctoServicoByTpDoctoServicoByIdFilialOrigemByNumeroDoctoServico(criteria);
	}
	
}
