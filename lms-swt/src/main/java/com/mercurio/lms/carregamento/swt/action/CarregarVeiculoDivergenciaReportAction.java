package com.mercurio.lms.carregamento.swt.action;

import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.report.DocumentosFimCarregamentoService;
import com.mercurio.lms.carregamento.report.VolumesFimCarregamentoService;


/**
 * 
 * @spring.bean id="lms.carregamento.swt.carregarVeiculoDivergenciaReportAction"
 */
public class CarregarVeiculoDivergenciaReportAction {
	
	private ReportServiceSupport reportServiceSupport;
	private ReportExecutionManager reportExecutionManager;
	private VolumesFimCarregamentoService volumesFimCarregamentoService;
	private DocumentosFimCarregamentoService documentosFimCarregamentoService;
	
	
	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	
	
	public String execute(Map filters) throws Exception {
		TypedFlatMap tfm = new TypedFlatMap();
		ReflectionUtils.flatMap(tfm, filters);
		return reportExecutionManager.generateReportLocator(reportServiceSupport, tfm);
	}

	
	public String executeDocCarregadoConferido(Map parameters) throws Exception{
		parameters.put("tpDocumentoCarregamento", "docCarregadoConferido");
		this.reportServiceSupport = documentosFimCarregamentoService;
		return execute(parameters);

	}
	
	public String executeDocCarregadoNaoConferido(Map parameters) throws Exception{
		parameters.put("tpDocumentoCarregamento", "docCarregadoNaoConferido");
		this.reportServiceSupport = documentosFimCarregamentoService;
		return execute(parameters);
	}
	
	public String executeDocNaoCarregadoConferido(Map parameters) throws Exception{
		parameters.put("tpDocumentoCarregamento", "docNaoCarregadoConferido");
		this.reportServiceSupport = documentosFimCarregamentoService;
		return execute(parameters);
	}
	
	public String executeCarregadosSemPreManifestoDocumento(Map parameters) throws Exception{
		parameters.put("tpDocumentoCarregamento", "carregadosSemPreManifestoDocumento");
		this.reportServiceSupport = documentosFimCarregamentoService;
		return execute(parameters);
	}
	
	public String executeCarregadosIncompletos(Map parameters) throws Exception{
		parameters.put("tpDocumentoCarregamento", "carregadosIncompletos");
		this.reportServiceSupport = documentosFimCarregamentoService;
		return execute(parameters);	
	}



	public VolumesFimCarregamentoService getVolumesFimCarregamentoService() {
		return volumesFimCarregamentoService;
	}



	public void setVolumesFimCarregamentoService(
			VolumesFimCarregamentoService volumesFimCarregamentoService) {
		this.volumesFimCarregamentoService = volumesFimCarregamentoService;
	}



	public DocumentosFimCarregamentoService getDocumentosFimCarregamentoService() {
		return documentosFimCarregamentoService;
	}



	public void setDocumentosFimCarregamentoService(
			DocumentosFimCarregamentoService documentosFimCarregamentoService) {
		this.documentosFimCarregamentoService = documentosFimCarregamentoService;
	}

}
