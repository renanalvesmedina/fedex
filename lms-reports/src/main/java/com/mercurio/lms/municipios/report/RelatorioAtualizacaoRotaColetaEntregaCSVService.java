package com.mercurio.lms.municipios.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.util.CsvUtils;
import com.mercurio.lms.municipios.model.service.HistoricoColetaEntregaService;

public class RelatorioAtualizacaoRotaColetaEntregaCSVService {

	private static final String DOCUMENTOS = "Historico";
	private static final String SEPARATOR = ";";

	private HistoricoColetaEntregaService historicoColetaEntregaService;

	public Map<String, Object> execute(Map<String, Object> parameters) {
		final List<Map<String, Object>> listaDocumentos = historicoColetaEntregaService.findHistoricoColetaEntrega(parameters);

		return convertMappedListToCsv(DOCUMENTOS, listaDocumentos, SEPARATOR);
	}

	public static Map<String, Object> convertMappedListToCsv(String reportName,
			final List<Map<String, Object>> lista, String separador) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (lista != null && !lista.isEmpty()) {
			String csv = CsvUtils.convertMappedListToCsv(lista, separador);
			map.put(reportName, csv);
		}

		return map;
	}

	public void setHistoricoColetaEntregaService(
			HistoricoColetaEntregaService historicoColetaEntregaService) {
		this.historicoColetaEntregaService = historicoColetaEntregaService;
	}
}
