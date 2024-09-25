package com.mercurio.lms.portaria.swt.action;

import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.portaria.report.RelatorioQuilometragemExcedidaCSVService;

public class EmitirRelatorioQuilometragemExcedidaAction extends ReportActionSupport {

	private RelatorioQuilometragemExcedidaCSVService relatorioQuilometragemExcedidaCSVService;
	
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
		DateTime dhPeriodoMedicaoFinal = new DateTime();		
		DateTime dhPeriodoMedicaoInicial = dhPeriodoMedicaoFinal.minusDays(INTERVAL_OF_DAYS);
		
		map.put("dhPeriodoMedicaoInicial", dhPeriodoMedicaoInicial);
		map.put("dhPeriodoMedicaoFinal", dhPeriodoMedicaoFinal);
		
		return map;
	}
			
	public Object executeSWT(TypedFlatMap parameters) throws Exception {
		return this.relatorioQuilometragemExcedidaCSVService.execute(parameters);
	}
	
	public void setRelatorioQuilometragemExcedidaCSVService(RelatorioQuilometragemExcedidaCSVService relatorioQuilometragemExcedidaCSVService) {
		this.relatorioQuilometragemExcedidaCSVService = relatorioQuilometragemExcedidaCSVService;
	}
}