package com.mercurio.lms.vendas.util;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

public class LMSScriptlet extends JRDefaultScriptlet {
	private boolean showLegendas;
	
	private boolean printColumnHeader;
	
	public Boolean setShowLegendas(Boolean bool) throws JRScriptletException {
		showLegendas = (bool != null)? bool.booleanValue() : false;
		return bool;
	}
	
	public Boolean showLegendas() {
		return Boolean.valueOf(showLegendas);
	}
	
	public Boolean showColuna() {
		if(!printColumnHeader){
			printColumnHeader = true;
			return Boolean.FALSE;
		}
		return Boolean.valueOf(printColumnHeader);
	}
	
	public Boolean setShowColuna(Boolean bool) {
		printColumnHeader = bool.booleanValue();
		return Boolean.TRUE;
	}
	
	
	public Boolean setLastPage(Boolean bool) throws JRScriptletException {
		setVariableValue("LastPage", Boolean.TRUE);
		return bool;
	}
	
	@Override
	public void afterColumnInit() throws JRScriptletException {
		super.afterColumnInit();
	}

	@Override
	public void afterDetailEval() throws JRScriptletException {
		super.afterDetailEval();
	}

	@Override
	public void afterPageInit() throws JRScriptletException {
		boolean entro = false;
		Integer totalGrupo = null;
		Integer totalDetail = null;
		
		if(variablesMap.get("grupoCount") != null){
			totalGrupo = getVariableValue("grupoCount") != null ?(Integer) getVariableValue("grupoCount") : null;
		}
		Integer totalReport = getVariableValue("REPORT_COUNT")!= null ?(Integer) getVariableValue("REPORT_COUNT") : null;
		
		if(parametersMap.get("TOTAL") != null){
			totalDetail = getParameterValue("TOTAL") != null ? (Integer) getParameterValue("TOTAL") : null;
		}
		
	
		if(totalGrupo != null){
				if(totalGrupo.intValue() >= totalDetail.intValue()){
					printColumnHeader = false;
					entro = true;
				}
		}else{
			if((totalReport.intValue() != 0 ) && (totalDetail != null)){
				if(totalReport.intValue() == totalDetail.intValue()){
					entro = true;
					printColumnHeader = false;
				}
			}
		}
		
		if(!entro){
			printColumnHeader = true;
		}
		super.afterPageInit();
	}

	@Override
	public void afterReportInit() throws JRScriptletException {
		// TODO Auto-generated method stub
		
		super.afterReportInit();
	}

	@Override
	public void beforeColumnInit() throws JRScriptletException {
		
		super.beforeColumnInit();
	}

	@Override
	public void beforeDetailEval() throws JRScriptletException {
		super.beforeDetailEval();
	}

	@Override
	public void beforePageInit() throws JRScriptletException {
		printColumnHeader = true;
		super.beforePageInit();
	}

	@Override
	public void beforeReportInit() throws JRScriptletException {
		// TODO Auto-generated method stub
		printColumnHeader = true;
		super.beforeReportInit();
	}
	

	
	
}
