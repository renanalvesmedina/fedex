package com.mercurio.lms.contasreceber.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.util.FileUtils;

/**
 * @spring.bean id="lms.contasreceber.emitirFaturasExcelService"
 */
public class EmitirFaturasExcelService {
 
	private FaturaService faturaService;
	
	private static final String REPORT_NAME = "INFO_FATURA";

	public File executeExportacaoCsv(TypedFlatMap parameters,
			File generateOutputDir) {
		String nrFaturaFormatada = (String) parameters.get("nrFaturaFormatada");
		String[] nrFaturaFormatadaSplit = nrFaturaFormatada.trim().split(" ");
		String sgFilial = nrFaturaFormatadaSplit[0];
		Long nrFatura = Long.parseLong(nrFaturaFormatadaSplit[1]);
		
		Map<String, Object> mapParameters = new HashMap<String, Object>();
		mapParameters.put("sgFilial", sgFilial);
		mapParameters.put("nrFatura", nrFatura);

		List<Map<String, Object>> lista = getFaturaService().findInfoDoctosFatura(mapParameters);
		
		return FileUtils.generateReportFile(changeTitles(lista), generateOutputDir, REPORT_NAME);
	}

	private List<Map<String, Object>> changeTitles(List<Map<String, Object>> inputList) {
		List<Map<String, Object>> outputList = new ArrayList<Map<String,Object>>(); 
		
		for (Map<String, Object> inputMap : inputList) {
			LinkedHashMap<String, Object> outputMap = new LinkedHashMap<String, Object>();
			
			outputMap.put("Filial de origem da fatura", inputMap.get("fil_origem"));
			outputMap.put("Número da fatura", inputMap.get("nr_fat"));
			outputMap.put("Tipo de documento de serviço", inputMap.get("tp_doc_serv"));
			outputMap.put("Filial de origem do documento de serviço", inputMap.get("fil_or_doc_serv"));
			outputMap.put("Número do documento de serviço", inputMap.get("nr_doc_serv"));
			outputMap.put("Valor devido", inputMap.get("vl_devido"));
			outputMap.put("Valor desconto", inputMap.get("vl_desconto"));
			outputMap.put("Valor recebimento parcial", inputMap.get("vl_receb_parc"));
			outputMap.put("Valor do saldo devedor", inputMap.get("vl_saldo_dev"));
			
			outputList.add(outputMap);
		}
		
		return outputList;
	}
	
	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

}