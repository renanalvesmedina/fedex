package com.mercurio.lms.franqueado.swt.action;

import java.io.File;

import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;

public class EmitirRelatorioDistanciasAction extends ReportActionSupport {

	private ReportExecutionManager reportExecutionManager;
	
	public String executeSWT(TypedFlatMap parameters) throws Exception {
		File reportFile;
		MultiReportCommand mrc = new MultiReportCommand("relatorioDistancias"); 
		
		mrc.addCommand("lms.franqueados.relatorioDistanciasTransferenciasService", parameters);
		mrc.addCommand("lms.franqueados.relatorioDistanciasColetaEntregaService", parameters);
		
		reportFile = this.reportExecutionManager.executeMultiReport(mrc);
			
		return reportExecutionManager.generateReportLocator(reportFile); 
	}
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	
}
