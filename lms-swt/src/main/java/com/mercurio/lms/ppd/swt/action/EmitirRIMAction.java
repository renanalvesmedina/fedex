package com.mercurio.lms.ppd.swt.action;

import java.util.Map;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.ppd.report.EmitirRIMService;
import com.mercurio.lms.ppd.report.PpdExcelRecibosService;

public class EmitirRIMAction{
	private ReportExecutionManager reportExecutionManager;
	private EmitirRIMService emitirRIMService;

	public String execute(Map parameters) throws Exception{
		return reportExecutionManager.generateReportLocator(emitirRIMService, parameters);
	}
	
	public void setEmitirRIMServiceService(EmitirRIMService emitirRIMService) {
		this.emitirRIMService = emitirRIMService;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}	
}
