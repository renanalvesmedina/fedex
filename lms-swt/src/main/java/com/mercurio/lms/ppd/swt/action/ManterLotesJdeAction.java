package com.mercurio.lms.ppd.swt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.ppd.model.PpdLoteJde;
import com.mercurio.lms.ppd.model.service.PpdLoteJdeService;
import com.mercurio.lms.ppd.model.service.PpdReciboService;
import com.mercurio.lms.ppd.report.PpdExcelRecibosLoteService;
 
public class ManterLotesJdeAction {
	private PpdReciboService reciboService;			
	private PpdLoteJdeService loteJdeService;
	private ReportExecutionManager reportExecutionManager;
	private PpdExcelRecibosLoteService excelRecibosLoteService;
      
	 public Map<String, Object> findById(Long id) {    		    	
	   	return loteJdeService.findById(id).getMapped();
	 }
	 
	public Map<String, Object> find(Map<String,Object> criteria) {
		PpdLoteJde lote = loteJdeService.findLoteAberto(); 
		if(lote != null) {
			return lote.getMapped();
		} else {
			return null;
		}		
	}
	
	public Map<String, Object> storeSendLoteAberto(Map<String,Object> bean) throws Exception {		
		Long idLoteEnviado = (Long)loteJdeService.storeSendLoteAberto();	
		Map<String,Object> reportParemeter = new HashMap<String, Object>();
		reportParemeter.put("idLoteJde", idLoteEnviado);		
		String reportReturn = reportExecutionManager.generateReportLocator(excelRecibosLoteService, reportParemeter);
		Map<String,Object> retorno = new HashMap<String, Object>();
		retorno.put("idLoteJde", idLoteEnviado);
		retorno.put("report", reportReturn);
		return retorno;
	}
	
	public Map<String, Object> storeBloqueioLote(Map<String,Object> bean) {		
		Boolean blBloqueado = (Boolean)bean.get("blBloqueado");
		return loteJdeService.storeBloqueio(blBloqueado).getMapped();		
	}
	
	public void removeById(Long id) {
		reciboService.removeById(id);
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		reciboService.removeByIds(ids);
	}
	
	//Sets das Services
	public void setReciboService(PpdReciboService reciboService) {
		this.reciboService = reciboService;
	}	
	
	public void setLoteJdeService(PpdLoteJdeService loteJdeService) {
		this.loteJdeService = loteJdeService;
	}  	
	
	public void setPpdExcelRecibosLoteService(PpdExcelRecibosLoteService excelRecibosLoteService) {
		this.excelRecibosLoteService = excelRecibosLoteService;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}	
}