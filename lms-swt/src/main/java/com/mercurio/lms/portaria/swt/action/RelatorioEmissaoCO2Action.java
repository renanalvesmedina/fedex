package com.mercurio.lms.portaria.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel;
import com.mercurio.lms.fretecarreteiroviagem.model.service.TipoCombustivelService;
import com.mercurio.lms.portaria.report.RelatorioEmissaoCO2Service;

/**
 * @spring.bean id="lms.portaria.swt.relatorioEmissaoCO2Action"
 */

public class RelatorioEmissaoCO2Action extends ReportActionSupport{

	private TipoCombustivelService tipoCombustivelService;
	private TipoMeioTransporteService tipoMeioTransporteService;
	private ReportExecutionManager reportExecutionManager;
	
	/**
	 * Método responsável pela busca dos Tipos de Combustíveis
	 */
	public List findTipoCombustivel(Map criteria) {
    	List result = tipoCombustivelService.findCombo(criteria);    	
    	List<Map<String, Object>> retorno = new ArrayList<Map<String, Object>>();
    	
    	for (Iterator iter = result.iterator(); iter.hasNext();) {
			TipoCombustivel tipoCombustivel = (TipoCombustivel)iter.next();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idTipoCombustivel", tipoCombustivel.getIdTipoCombustivel());
			map.put("dsTipoCombustivel", tipoCombustivel.getDsTipoCombustivel());
			
			retorno.add(map);
		}
    	
    	return retorno;
	}
	
	/**
	 * Método responsável pela busca dos Tipos de Meios de Transporte
	 */
	public List findTipoMeioTransporte(Map criteria) {
    	List result = tipoMeioTransporteService.findCombo(criteria);    	
    	List<Map<String, Object>> retorno = new ArrayList<Map<String, Object>>();
    	
    	for (Iterator iter = result.iterator(); iter.hasNext();) {
			HashMap<String, Object> tpMeioTransporte = (HashMap<String, Object>)iter.next();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idTipoMeioTransporte", tpMeioTransporte.get("idTipoMeioTransporte"));
			map.put("dsTipoMeioTransporte", tpMeioTransporte.get("dsTipoMeioTransporte"));
			
			retorno.add(map);
		}
    	
    	return retorno;
	}
	
	/**
	 * Método q executa o relátorio
	 */
	public String executeSWT(TypedFlatMap parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(this.reportServiceSupport, parameters);
	}
	
	public void setTipoCombustivelService(TipoCombustivelService tipoCombustivelService){
		this.tipoCombustivelService = tipoCombustivelService;
	}
	
	public void setReportExecuteManager(ReportExecutionManager reportExecurionManager){
		this.reportExecutionManager = reportExecurionManager;
	}
	
	public void setRelatorioEmissaoCO2Service(RelatorioEmissaoCO2Service relatorioEmissaoCO2Service){
		this.reportServiceSupport = relatorioEmissaoCO2Service;
	}
	
	public void setTipoMeioTransporteService(TipoMeioTransporteService tipoMeioTransporteService){
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}
}
