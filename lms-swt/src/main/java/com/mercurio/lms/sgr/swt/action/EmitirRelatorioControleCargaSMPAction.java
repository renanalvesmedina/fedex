package com.mercurio.lms.sgr.swt.action;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.report.EmitirRelatorioControleCargaSMPService;

public class EmitirRelatorioControleCargaSMPAction{
	
	private ReportExecutionManager reportExecutionManager;
	private EmitirRelatorioControleCargaSMPService emitirRelatorioControleCargaSMPService;
	
	public String execute(TypedFlatMap parameters) throws Exception {
		return getReportExecutionManager().generateReportLocator(emitirRelatorioControleCargaSMPService, parameters);
	}
	
	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	
	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setEmitirRelatorioControleCargaSMPService(
			EmitirRelatorioControleCargaSMPService emitirRelatorioControleCargaSMPService) {
		this.emitirRelatorioControleCargaSMPService = emitirRelatorioControleCargaSMPService;
	}

	public EmitirRelatorioControleCargaSMPService getEmitirRelatorioControleCargaSMPService() {
		return emitirRelatorioControleCargaSMPService;
	}
}