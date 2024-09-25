package com.mercurio.lms.tributos.swt.action;


import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.report.EmitirExcecoesSaneamentoService;

public class EmitirExcecoesSaneamentoAction extends ReportActionSupport{
	private ReportExecutionManager reportExecutionManager;

	public void setEmitirExcecoeSaneamentoService(EmitirExcecoesSaneamentoService emitirExcecoeSaneamentoService) {
		this.reportServiceSupport = emitirExcecoeSaneamentoService;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	
	public String executeSWT(TypedFlatMap parameters) throws Exception {
		String generateReport = reportExecutionManager.generateReportLocator(this.reportServiceSupport, parameters);
		return generateReport;
	}
}
