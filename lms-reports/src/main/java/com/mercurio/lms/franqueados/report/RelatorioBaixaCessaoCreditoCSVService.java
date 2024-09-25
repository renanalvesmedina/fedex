package com.mercurio.lms.franqueados.report;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.franqueados.model.service.DoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioSinteticoParticipacaoLancamentosDiversosService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioSinteticoParticipacaoLancamentosDiversos.jasper"
 */
public class RelatorioBaixaCessaoCreditoCSVService  {

	private static final String RELATORIO_BAIXA_CESSAO = "BaixaCessaoCredito";
	
	private static final String SEPARATOR = ";";
	
	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;
	
	public Map<String, Object> execute(Map<String, Object> parameters) {
		
		boolean filtraFranquia = (parameters.containsKey("idFilial") && parameters.get("idFilial") != null);
		
		if(parameters != null && parameters.containsKey("competenciaIni") && parameters.containsKey("competenciaFim")){
			YearMonthDay dateIni = (YearMonthDay) parameters.get("competenciaIni");
			YearMonthDay dateFim = (YearMonthDay) parameters.get("competenciaFim");
			parameters.put("dtInicio",FranqueadoUtils.buscarPrimeiroDiaMes(dateIni));
			parameters.put("dtFim",FranqueadoUtils.buscarUltimoDiaMes(dateFim));
		}
		
		Map<String,Object> result = new HashMap<String, Object>();

		final List<Map<String, Object>> lista = doctoServicoFranqueadoService.findRelatorioBaixaCessaoCredito(filtraFranquia, parameters);

		result.putAll(FranqueadoUtils.convertMappedListToCsv(RELATORIO_BAIXA_CESSAO, lista, SEPARATOR));

		return result;
	}

	public void setDoctoServicoFranqueadoService(
			DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
}
