package com.mercurio.lms.tributos.swt.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.EmbasamentoLegalIcms;
import com.mercurio.lms.tributos.model.service.EmbasamentoLegalIcmsService;
import com.mercurio.lms.tributos.model.service.TipoTributacaoIcmsService;
import com.mercurio.lms.util.session.SessionUtils;


public class EmbasamentoAction {

	private UnidadeFederativaService 	unidadeFederativaService;
	private TipoTributacaoIcmsService 	tipoTributacaoIcmsService;
	private EmbasamentoLegalIcmsService embasamentoLegalIcmsService;
	
	public List findTipoTributacaoIcms(Map criteria){
		return tipoTributacaoIcmsService.find(criteria);
	}
	
	public List findUnidadeFederativa(TypedFlatMap criteria) {
		criteria.put("idPais", SessionUtils.getPaisSessao().getIdPais());
		return unidadeFederativaService.findComboAtivo(criteria);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage resultPage = embasamentoLegalIcmsService.findPaginated(createFindCriteria(criteria));
		List<TypedFlatMap> listResult = new ArrayList<TypedFlatMap>();

		for(int i=0; i< resultPage.getList().size(); i++) {
			EmbasamentoLegalIcms element = (EmbasamentoLegalIcms) resultPage.getList().get(i);
			TypedFlatMap map  = new TypedFlatMap();
			map.put("idEmbasamento", 				element.getIdEmbasamento());
			map.put("dsTipoTributacaoIcms",			element.getTipoTributacaoIcms().getDsTipoTributacaoIcms());
			map.put("sgUnidadeFederativa", 			element.getUnidadeFederativaOrigem().getSgUnidadeFederativa());
			map.put("dsEmbLegalCompleto", 			element.getDsEmbLegalCompleto());
			map.put("dsEmbLegalResumido",			element.getDsEmbLegalResumido());
			listResult.add(map);
		}

		resultPage.setList(listResult);
    	return resultPage;
	}
	
	private TypedFlatMap createFindCriteria(Map criteria) {
		TypedFlatMap mapCriteria = new TypedFlatMap();
		mapCriteria.put("_currentPage", criteria.get("_currentPage"));
    	mapCriteria.put("_pageSize", criteria.get("_pageSize"));
    	mapCriteria.put("_order", criteria.get("_order"));

    	mapCriteria.put("unidadeFederativaOrigem.idUnidadeFederativa", criteria.get("idUnidadeFederativa"));
    	mapCriteria.put("tipoTributacaoIcms.idTipoTributacaoIcms", criteria.get("idTipoTributacaoIcms"));
    	return mapCriteria;
	}  	
	
	public int findRowCount(TypedFlatMap criteria){
		return embasamentoLegalIcmsService.getRowCount(createFindCriteria(criteria));
	}
	
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setTipoTributacaoIcmsService(TipoTributacaoIcmsService tipoTributacaoIcmsService) {
		this.tipoTributacaoIcmsService = tipoTributacaoIcmsService;
	}
	
	public void setEmbasamentoLegalIcmsService(EmbasamentoLegalIcmsService embasamentoLegalIcmsService) {
		this.embasamentoLegalIcmsService = embasamentoLegalIcmsService;
	}
}
