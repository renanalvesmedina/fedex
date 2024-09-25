package com.mercurio.lms.franqueado.swt.action;

import java.io.File;
import java.util.List;

import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.model.FranqueadoFranquia;
import com.mercurio.lms.franqueados.model.service.FranqueadoFranquiaService;
import com.mercurio.lms.franqueados.report.GerarCartaCessaoCreditoService;
import com.mercurio.lms.util.JTDateTimeUtils;

public class GerarCartaCessaoCreditoAction extends ReportActionSupport {

	private ReportExecutionManager reportExecutionManager;
	private FranqueadoFranquiaService franqueadoFranquiaService;
    
	public String executeSWT(TypedFlatMap parameters) throws Exception {
		File reportFile;
		MultiReportCommand mrc = new MultiReportCommand("gerarCartaCessaoCredito"); 
		
		if(parameters.containsKey("idFilial") && parameters.get("idFilial") != null){
			mrc.addCommand("lms.franqueados.gerarCartaCessaoCreditoService", parameters);
			mrc.addCommand("lms.franqueados.gerarCartaCessaoCreditoServiceAnexo", parameters);
		} else {
			List<FranqueadoFranquia> franqueadoFranquiasList = franqueadoFranquiaService.findFranqueadoFranquiasVigentes(JTDateTimeUtils.getDataAtual());
			
			for(FranqueadoFranquia franqueadoFranquia : franqueadoFranquiasList){
				TypedFlatMap map = new TypedFlatMap();
				map.putAll(parameters);
				map.put("idFilial", franqueadoFranquia.getFranquia().getIdFranquia());
				map.put("dsFranquia", franqueadoFranquia.getFranquia().getFilial().getSgFilial());
				mrc.addCommand("lms.franqueados.gerarCartaCessaoCreditoService", map);
			}
		}
		reportFile = this.reportExecutionManager.executeMultiReport(mrc);
			
		return reportExecutionManager.generateReportLocator(reportFile); 
	}
	
	public void setGerarCartaCessaoCreditoService(GerarCartaCessaoCreditoService gerarCartaCessaoCreditoService) {
		this.reportServiceSupport = gerarCartaCessaoCreditoService;
	}
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setFranqueadoFranquiaService(
			FranqueadoFranquiaService franqueadoFranquiaService) {
		this.franqueadoFranquiaService = franqueadoFranquiaService;
	}
}
