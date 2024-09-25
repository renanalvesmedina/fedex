package com.mercurio.lms.portaria.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.util.CsvUtils;
import com.mercurio.lms.portaria.model.service.ControleQuilometragemService;

public class RelatorioQuilometragemExcedidaCSVService {

	private static final String DOCUMENTOS = "Quilometragem";
	private static final String SEPARATOR = ";";

	private ControleQuilometragemService controleQuilometragemService;

	public Map<String, Object> execute(Map<String, Object> parameters) {
		final List<Map<String, Object>> listaQuilometragem = controleQuilometragemService.findQuilometragemExcedenteRota(parameters);

		return convertMappedListToCsv(DOCUMENTOS, listaQuilometragem, SEPARATOR);
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

	public void setControleQuilometragemService(ControleQuilometragemService controleQuilometragemService) {
		this.controleQuilometragemService = controleQuilometragemService;
	}
}
