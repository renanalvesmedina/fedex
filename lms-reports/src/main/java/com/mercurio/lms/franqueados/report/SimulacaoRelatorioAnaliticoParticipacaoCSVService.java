package com.mercurio.lms.franqueados.report;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.franqueados.model.service.SimulacaoDoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.SimulacaoReembarqueDoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioSinteticoParticipacaoLancamentosDiversosService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioSinteticoParticipacaoLancamentosDiversos.jasper"
 */
public class SimulacaoRelatorioAnaliticoParticipacaoCSVService  {

	
	private static final String DOCUMENTOS = "Documentos";
	private static final String FRETES_LOCAL = "FretesLocal";
	private static final String SERVICOS_ADICIONAIS = "ServicosAdicionais";
	private static final String REEMBARQUES = "Reembarques";
	private static final String SEPARATOR = ";";
	
	private SimulacaoDoctoServicoFranqueadoService doctoServicoFranqueadoService;
	private SimulacaoReembarqueDoctoServicoFranqueadoService reembarqueFranqueadoService;
	
	public Map<String, Object> execute(Map<String, Object> parameters) {
		boolean filtraFranquia = (parameters.containsKey("idFilial") && parameters.get("idFilial") != null);

		Map<String,Object> result = new HashMap<String, Object>();

		final List<Map<String, Object>> listaDocumentos = doctoServicoFranqueadoService.findRelatorioAnaliticoDocumentos(filtraFranquia,true, parameters);
		final List<Map<String, Object>> listaReembarque = reembarqueFranqueadoService.findRelatorioAnaliticoReembarques(filtraFranquia,true, parameters);
		final List<Map<String, Object>> listaFreteLocal = doctoServicoFranqueadoService.findRelatorioAnaliticoFretesLocal(filtraFranquia, true, parameters);
		final List<Map<String, Object>> listaServicoAdicional = doctoServicoFranqueadoService.findRelatorioAnaliticoServicosAdicionais(filtraFranquia,true, parameters);	
		
		result.putAll(FranqueadoUtils.convertMappedListToCsv(DOCUMENTOS, listaDocumentos, SEPARATOR));
		result.putAll(FranqueadoUtils.convertMappedListToCsv(FRETES_LOCAL, listaFreteLocal, SEPARATOR));
		result.putAll(FranqueadoUtils.convertMappedListToCsv(SERVICOS_ADICIONAIS, listaServicoAdicional, SEPARATOR));
		result.putAll(FranqueadoUtils.convertMappedListToCsv(REEMBARQUES, listaReembarque, SEPARATOR));
		
		return result;
	}

	public void setDoctoServicoFranqueadoService(
			SimulacaoDoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}

	public void setReembarqueFranqueadoService(
			SimulacaoReembarqueDoctoServicoFranqueadoService reembarqueFranqueadoService) {
		this.reembarqueFranqueadoService = reembarqueFranqueadoService;
	}
}
