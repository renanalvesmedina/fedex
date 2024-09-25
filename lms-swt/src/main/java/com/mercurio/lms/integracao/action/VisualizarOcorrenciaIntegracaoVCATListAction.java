package com.mercurio.lms.integracao.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.integracao.model.service.OcorrenciaIntegracaoService;
import com.mercurio.lms.integration.pilmsf26e.VCAT;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.integracao.swt.visualizarOcorrenciaIntegracaoVCATListAction"
 */
public class VisualizarOcorrenciaIntegracaoVCATListAction extends CrudAction {

	public void store(Map parameters) {
		List<VCAT> list = (List) parameters.get("layoutList");
		Long idOcorrenciaIntegracao = (Long) parameters.get("idOcorrenciaIntegracao");
		getOcorrenciaIntegracaoService().storeLayoutVCPI26e(idOcorrenciaIntegracao, list);
	}
	
	public void setOcorrenciaIntegracaoService(OcorrenciaIntegracaoService ocorrenciasIntegracaoService) {
		defaultService = ocorrenciasIntegracaoService;
	}
	
	public OcorrenciaIntegracaoService getOcorrenciaIntegracaoService() {
		return (OcorrenciaIntegracaoService) defaultService;
	}
}