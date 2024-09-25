package com.mercurio.lms.integracao.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.integracao.model.OcorrenciaIntegracao;
import com.mercurio.lms.integracao.model.service.OcorrenciaIntegracaoService;
import com.mercurio.lms.integracao.model.service.OcorrenciaIntegracaoService.PILMSC019E;
import com.mercurio.lms.integracao.model.service.OcorrenciaIntegracaoService.piTypes;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.integracao.swt.visualizarOcorrenciaIntegracaoMAAction"
 */
public class VisualizarOcorrenciaIntegracaoMAAction extends CrudAction {

	public ResultSetPage findPaginated(Map criteria) {
		String sgFilial = (String) criteria.get("sgFilial");
		Integer nrManifesto = (Integer) criteria.get("nrManifesto");
		sgFilial = sgFilial != null ? sgFilial.toUpperCase() : null;
		
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		ResultSetPage rsp = getOcorrenciaIntegracaoService().findPaginatedLayoutList(PILMSC019E.MA.toString(), piTypes.PILMSC019E.toString(), sgFilial, nrManifesto, findDef);
		List<Map> result = AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList());
		if(result != null) {
			for (Map map : result) {
				map.remove("message");
			}
		}
		rsp.setList(result);
		return rsp;
	}
	
	@Override
	public Integer getRowCount(Map criteria) {
		String sgFilial = (String) criteria.get("sgFilial");
		Integer nrManifesto = (Integer) criteria.get("nrManifesto");
		sgFilial = sgFilial != null ? sgFilial.toUpperCase() : null;
		
		return getOcorrenciaIntegracaoService().getRowCount(PILMSC019E.MA.toString(), piTypes.PILMSC019E.toString(), sgFilial, nrManifesto);
	}

	public void removeByIds(Map criteria) {
		List ids = (List) criteria.get("ids");
		if(ids != null && ids.size() > 0) {
			super.removeByIds(ids);
		}
	}
	
	public Map findDadosOcorrencia(Map criteria) {
		Long idOcorrenciaIntegracao = (Long) criteria.get("idOcorrenciaIntegracao");
		return getOcorrenciaIntegracaoService().findDadosOcorrenciaById(idOcorrenciaIntegracao, PILMSC019E.MA.toString());
	}
	
	public void updateMessageIntegracao(TypedFlatMap criteria){
		List<OcorrenciaIntegracao> integration = (List<OcorrenciaIntegracao>)criteria.get("MessageIntegracao");
		for (OcorrenciaIntegracao ocorrenciaIntegracao : integration) {
			this.getOcorrenciaIntegracaoService().updateOcorrenciaIntegracao(ocorrenciaIntegracao);
		}
	}
		
	public void setOcorrenciaIntegracaoService(OcorrenciaIntegracaoService ocorrenciasIntegracaoService) {
		defaultService = ocorrenciasIntegracaoService;
	}
	
	public OcorrenciaIntegracaoService getOcorrenciaIntegracaoService() {
		return (OcorrenciaIntegracaoService) defaultService;
	}
	
}