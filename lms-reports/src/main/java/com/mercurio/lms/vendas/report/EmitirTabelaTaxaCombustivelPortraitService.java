package com.mercurio.lms.vendas.report;


import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;


/**
 * Tabela Taxa Combustivel
 * 
 * @spring.bean id="lms.vendas.report.emitirTabelaTaxaCombustivel_Portrait" 
 * @spring.property name="reportName" value="/com/mercurio/lms/tabelaprecos/report/subReportTaxaCombustivel_Portrait.vm"
 * @spring.property name="numberOfCrosstabs" value="1" 
 * @spring.property name="crossTabLefts" value="50"
 * @spring.property name="crossTabBandWidths" value="489" 
 * @spring.property name="numbersOfCrossTabColumns" value="27"
 */
public class EmitirTabelaTaxaCombustivelPortraitService extends ReportServiceSupport {

	private Map dadosClasse = new HashMap();
   
	/**
	 * Classe utilizada apenas para geracao dos arquivos do subrelatorio
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {

		JRReportDataObject jr = null;
		return jr;
	}
	

	/**
	 * 
	 * @param mapParameters
	 */
	public void setMapParameters(Map mapParameters) {
		
		dadosClasse = mapParameters;
	}
	
	/**
	 * 
	 * @param columnNumber
	 * @return
	 */
	public String getColumnName(Integer columnNumber)
	{
		String columnName = (String)dadosClasse.get(("COLUMN"+columnNumber));
		return columnName;
	}

}
