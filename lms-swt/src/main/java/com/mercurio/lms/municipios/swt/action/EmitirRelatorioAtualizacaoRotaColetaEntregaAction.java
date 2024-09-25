package com.mercurio.lms.municipios.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.report.RelatorioAtualizacaoRotaColetaEntregaCSVService;

public class EmitirRelatorioAtualizacaoRotaColetaEntregaAction extends ReportActionSupport {

	private RotaColetaEntregaService rotaColetaEntregaService;
	private RelatorioAtualizacaoRotaColetaEntregaCSVService relatorioAtualizacaoRotaColetaEntregaCSVService;
	
	private static final int INTERVAL_OF_DAYS = 31;
	
	/**
	 * Inicializa campos para a tela principal.
	 * 
	 * @param map
	 * @return  Map<String, Object> 
	 */
	public Map<String, Object> initFields(Map<String, Object> map) {	
		/*
		 * Define o valor final e o inicial do campo data atualização, sendo o
		 * incial 31 dias antes do final.
		 */
		YearMonthDay dhAtualizacaoFinal = new YearMonthDay();		
		YearMonthDay dhAtualizacaoInicial = dhAtualizacaoFinal.minusDays(INTERVAL_OF_DAYS);
		
		map.put("dhAtualizacaoInicial", dhAtualizacaoInicial);
		map.put("dhAtualizacaoFinal", dhAtualizacaoFinal);
		
		return map;
	}
		
	public List<Object> findLookupRotaColetaEntrega(Map<String, Object> criteria) {
    	Map<String, Object> mapFilial = new HashMap<String, Object>();
    	mapFilial.put("idFilial", criteria.get("idFilial"));
    	criteria.put("filial", mapFilial);
    	
    	List list = rotaColetaEntregaService.findLookup(criteria);
    	List<Object> retorno = new ArrayList<Object>();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		RotaColetaEntrega rotaColetaEntrega = (RotaColetaEntrega)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega());
    		typedFlatMap.put("nrRota", rotaColetaEntrega.getNrRota());
    		typedFlatMap.put("dsRota", rotaColetaEntrega.getDsRota());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
	
	public Object executeSWT(TypedFlatMap parameters) throws Exception {
		return this.relatorioAtualizacaoRotaColetaEntregaCSVService.execute(parameters);
	}
	
	public void setRelatorioAtualizacaoRotaColetaEntregaCSVService(RelatorioAtualizacaoRotaColetaEntregaCSVService relatorioAtualizacaoRotaColetaEntregaCSVService) {
		this.relatorioAtualizacaoRotaColetaEntregaCSVService = relatorioAtualizacaoRotaColetaEntregaCSVService;
	}
	
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
}