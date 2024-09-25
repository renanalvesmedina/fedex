package com.mercurio.lms.ppd.swt.action;

import java.util.Map;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.ppd.report.PpdExcelRecibosService;

public class ExportarListagemRecibosAction{
	private ReportExecutionManager reportExecutionManager;
	private PpdExcelRecibosService exportarListagemRecibosService;

	public String execute(Map parameters) throws Exception{
		return reportExecutionManager.generateReportLocator(exportarListagemRecibosService, parameters);
	}
	
	public void setExportarListagemChamadosService(PpdExcelRecibosService exportarListagemRecibosService) {
		this.exportarListagemRecibosService = exportarListagemRecibosService;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}	
}
