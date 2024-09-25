package com.mercurio.lms.entrega.reports;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;

public class AgendamentoReportRunnerCCT extends ExcelRemoteReportRunnerCCT {
	public AgendamentoReportRunnerCCT(byte[] byteArrayTemplate, String fileName, File reportOutputDir) {
		super(byteArrayTemplate, fileName, reportOutputDir);
	}

	@Override
	protected void fillReportDadosGerais(HSSFSheet sheet) {
		if(dadosGerais != null && !dadosGerais.isEmpty()){
			ExcelReportUtilsCCT.setStringVar(sheet, "ds_remetente", dadosGerais.get("ds_remetente"));
			ExcelReportUtilsCCT.setStringVar(sheet, "ds_destinatario", dadosGerais.get("ds_destinatario"));
			ExcelReportUtilsCCT.setCpfCnpfVar(sheet, "nr_cnpj_remetente", dadosGerais.get("nr_cnpj_remetente"), null);
			ExcelReportUtilsCCT.setCpfCnpfVar(sheet, "nr_cnpj_destinatario", dadosGerais.get("nr_cnpj_destinatario"), null);
			ExcelReportUtilsCCT.setFormatedDateVar(sheet, "dt_entrega", dadosGerais.get("dt_entrega"));
			ExcelReportUtilsCCT.setFormatedDateVar(sheet, "dt_inclu_agend", dadosGerais.get("dt_inclu_agend"));
			ExcelReportUtilsCCT.setTimeVar(sheet, "hr_entrega", dadosGerais.get("hr_entrega"));
		}
	}

	@Override
	protected void fillReportDadosTotalizadores(HSSFSheet sheet) {
		BigDecimal smNrQtdeItem = BigDecimal.ZERO; 
		BigDecimal smVlTotalCIPI = BigDecimal.ZERO;
		for (Map<String, Object> map : dadosItens) {
			if(map.get("vl_qtde_item") != null){
				smNrQtdeItem = smNrQtdeItem.add(new BigDecimal((String) map.get("vl_qtde_item"))); 
			}
			
			if(map.get("vl_total_item") != null){
				smVlTotalCIPI = smVlTotalCIPI.add(new BigDecimal((String) map.get("vl_total_item"))); 
			}
			
			if(map.get("vl_ipi") != null){
				smVlTotalCIPI = smVlTotalCIPI.add(new BigDecimal((String) map.get("vl_ipi"))); 
			}
		}
		
		ExcelReportUtilsCCT.setNumericVar(sheet, "sm_vl_qtde_item", smNrQtdeItem.toString(), 3);
		ExcelReportUtilsCCT.setNumericVar(sheet, "sm_vl_total_cIPI", smVlTotalCIPI.toString(), 2);
	}
}
