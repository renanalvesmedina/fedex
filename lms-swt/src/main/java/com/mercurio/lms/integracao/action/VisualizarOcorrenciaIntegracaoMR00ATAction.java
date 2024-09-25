package com.mercurio.lms.integracao.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.integracao.model.service.OcorrenciaIntegracaoService;
import com.mercurio.lms.integracao.model.service.OcorrenciaIntegracaoService.PILMSF27E;
import com.mercurio.lms.integracao.model.service.OcorrenciaIntegracaoService.piTypes;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.integracao.swt.visualizarOcorrenciaIntegracaoMR00ATAction"
 */
public class VisualizarOcorrenciaIntegracaoMR00ATAction extends CrudAction {
	public ResultSetPage findPaginated(Map criteria) {
		String sgFilial = (String) criteria.get("sgFilial");
		sgFilial = sgFilial != null ? sgFilial.toUpperCase() : null;
		Integer nrDocumento = (Integer) criteria.get("nrDocumento");
		
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		ResultSetPage rsp = getOcorrenciaIntegracaoService().findPaginatedLayoutList("MR00", piTypes.PILMSF27E.toString(), sgFilial, nrDocumento, findDef);
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
		sgFilial = sgFilial != null ? sgFilial.toUpperCase() : null;
		Integer nrDocumento = (Integer) criteria.get("nrDocumento");
		
		return getOcorrenciaIntegracaoService().getRowCount("MR00", piTypes.PILMSF27E.toString(), sgFilial, nrDocumento);
	}
	
	public void removeByIds(Map criteria) {
		List ids = (List) criteria.get("ids");
		if(ids != null && ids.size() > 0) {
			super.removeByIds(ids);
		}
	}
	
	public Map findDadosOcorrencia(Map criteria) {
		Long idOcorrenciaIntegracao = (Long) criteria.get("idOcorrenciaIntegracao");
		return getOcorrenciaIntegracaoService().findDadosOcorrenciaById(idOcorrenciaIntegracao, PILMSF27E.MR00AT.toString());
	}
		
	public void setOcorrenciaIntegracaoService(OcorrenciaIntegracaoService ocorrenciasIntegracaoService) {
		defaultService = ocorrenciasIntegracaoService;
	}
	
	public OcorrenciaIntegracaoService getOcorrenciaIntegracaoService() {
		return (OcorrenciaIntegracaoService) defaultService;
	}
}