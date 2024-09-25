package com.mercurio.lms.franqueado.swt.action;

import java.io.File;

import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;

public class EmitirRelatorioParametrosCalculoAction extends ReportActionSupport {

	private ReportExecutionManager reportExecutionManager;
	
	public String executeSWT(TypedFlatMap parameters) throws Exception {
		File reportFile;
		MultiReportCommand mrc = new MultiReportCommand("relatorioParametrosCalculo"); 
		
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoDescontosService", parameters);
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoDistanciaTransferenciasService", parameters);
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoDistanciaColetaEntregaService", parameters);
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoValorFixoService", parameters);
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoRepasseService", parameters);
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoLimiteService", parameters);
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoFreteLocalService", parameters);
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoReembarqueService", parameters);
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoContasContabeisService", parameters);
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoGeneralidadesService", parameters);
		mrc.addCommand("lms.franqueados.relatorioParametrosCalculoServicosAdicionaisService", parameters);
		
		reportFile = this.reportExecutionManager.executeMultiReport(mrc);
			
		return reportExecutionManager.generateReportLocator(reportFile); 
	}
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	
}
