package com.mercurio.lms.franqueados.report;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.franqueados.model.service.DoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioSinteticoParticipacaoLancamentosDiversosService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioSinteticoParticipacaoLancamentosDiversos.jasper"
 */
public class RelatorioSinteticoGeralTipoFreteCSVService  {

	private static final String SINTETICO_GERAL_TIPO_FRETE = "SinteticoGeralTipoFrete";
	
	private static final String SEPARATOR = ";";
	
	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;
	
	public Map<String, Object> execute(Map<String, Object> parameters) {
		
		Map<String,Object> result = new HashMap<String, Object>();

		final List<Map<String, Object>> lista = doctoServicoFranqueadoService.findRelatorioSinteticoGeralTipoFrete(parameters);
		
		result.putAll(FranqueadoUtils.convertMappedListToCsv(SINTETICO_GERAL_TIPO_FRETE, lista, SEPARATOR));

		return result;
	}

	public void setDoctoServicoFranqueadoService(
			DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
}
