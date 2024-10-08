package com.mercurio.lms.entrega.swt.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.entrega.report.EmitirEntregaParceirosService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.swt.emitirEntregaParceirosAction"
 */
public class EmitirEntregaParceirosAction{

	private ManifestoService manifestoService;
	private EmitirEntregaParceirosService emitirEntregaParceirosService;
	private ReportExecutionManager reportExecutionManager;
	
	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	
	public EmitirEntregaParceirosService getEmitirEntregaParceirosService() {
		return emitirEntregaParceirosService;
	}

	public void setEmitirEntregaParceirosService(
			EmitirEntregaParceirosService emitirEntregaParceirosService) {
		this.emitirEntregaParceirosService = emitirEntregaParceirosService;
	}

	/**
	 * M�todo q executa o rel�torio
	 */
	public String execute(Map parameters) throws Exception {
		String generateReport = this.reportExecutionManager.generateReportLocator(this.emitirEntregaParceirosService, parameters);
		return generateReport;
	}

	public List findManifestoEntrega(Map criteria){
		return manifestoService.findLookup(criteria);
	}
	

}
