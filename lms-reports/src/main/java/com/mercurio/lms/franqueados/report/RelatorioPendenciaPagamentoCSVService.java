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
public class RelatorioPendenciaPagamentoCSVService  {

	private static final String RELATORIO_PENDENCIA = "PendenciasPagamento";
	
	private static final String SEPARATOR = ";";
	
	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;
	
	public Map<String, Object> execute(Map<String, Object> parameters) {
		
		boolean filtraFranquia = (parameters.containsKey("idFilial") && parameters.get("idFilial") != null);
		
		Map<String,Object> result = new HashMap<String, Object>();

		final List<Map<String, Object>> lista = doctoServicoFranqueadoService.findRelatorioPendenciaPagamento(filtraFranquia, parameters);
		
		result.putAll(FranqueadoUtils.convertMappedListToCsv(RELATORIO_PENDENCIA, lista, SEPARATOR));

		return result;
	}

	public void setDoctoServicoFranqueadoService(
			DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
}
