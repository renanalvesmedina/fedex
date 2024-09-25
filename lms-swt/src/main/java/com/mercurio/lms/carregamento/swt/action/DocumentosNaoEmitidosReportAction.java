package com.mercurio.lms.carregamento.swt.action;

import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.report.DocumentosNaoEmitidosServiceSupport;


/**
 * 
 * @spring.bean id="lms.carregamento.swt.documentosNaoEmitidosReportAction"
 */
public class DocumentosNaoEmitidosReportAction {
	
	private DocumentosNaoEmitidosServiceSupport reportServiceSupport;
	private ReportExecutionManager reportExecutionManager;
	
	public String execute(Map filters) throws Exception {
		TypedFlatMap tfm = new TypedFlatMap();
		ReflectionUtils.flatMap(tfm, filters);
		return getReportExecutionManager().generateReportLocator(getReportServiceSupport(), tfm);
	}

	public void setReportServiceSupport(DocumentosNaoEmitidosServiceSupport reportServiceSupport) {
		this.reportServiceSupport = reportServiceSupport;
	}

	public DocumentosNaoEmitidosServiceSupport getReportServiceSupport() {
		return reportServiceSupport;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

}
