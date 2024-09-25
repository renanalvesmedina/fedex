package com.mercurio.lms.indenizacoes.report;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.indenizacoes.model.service.DoctoServicoIndenizacaoService;

public class RelatorioIndenizacoesFranqueadosCSVService  {
	
	private static final String RELATORIO_INDENIZACOES_FRANQUEADOS = "RelatorioIndenizacoesFranqueados";

	private static final String SEPARATOR = ";";
	
	private DoctoServicoIndenizacaoService doctoServicoIndenizacaoService;
	
	public Map<String, Object> execute(Map<String, Object> parameters) {
		Map<String,Object> result = new HashMap<String, Object>();
		
		final List<Map<String, Object>> lista = doctoServicoIndenizacaoService.findRelatorioIndenizacoesFranqueados(parameters);
		
		result.putAll(FranqueadoUtils.convertMappedListToCsv(RELATORIO_INDENIZACOES_FRANQUEADOS, lista, SEPARATOR));
		
		return result;
	}

	public void setDoctoServicoIndenizacaoService(
			DoctoServicoIndenizacaoService doctoServicoIndenizacaoService) {
		this.doctoServicoIndenizacaoService = doctoServicoIndenizacaoService;
	}

}
