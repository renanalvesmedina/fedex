package com.mercurio.lms.fretecarreteiro.swt.action;

import java.io.File;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteirocoletaentrega.report.EmitirNotasCreditoColetaEntregaService;

public class EmitirNotaCreditoParceiraAction extends ReportActionSupport {

	private EmitirNotasCreditoColetaEntregaService emitirNotasCreditoColetaEntregaService;
	private ReportExecutionManager reportExecutionManager;

	public void setEmitirNotasCreditoColetaEntregaService(EmitirNotasCreditoColetaEntregaService emitirNotasCreditoColetaEntregaService) {
		this.reportServiceSupport = emitirNotasCreditoColetaEntregaService;
		this.emitirNotasCreditoColetaEntregaService = emitirNotasCreditoColetaEntregaService;
	} 

	public String executeEmitirNota(Map parameters) throws Exception{
		TypedFlatMap map = new TypedFlatMap();
		map.putAll(parameters);
		
		File file = emitirNotasCreditoColetaEntregaService.executeReport(map);
		return reportExecutionManager.generateReportLocator(file);
	}


	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

}
