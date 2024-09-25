package com.mercurio.lms.franqueados.report;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.franqueados.model.service.DoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.LancamentoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.ReembarqueDoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioSinteticoParticipacaoLancamentosDiversosService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioSinteticoParticipacaoLancamentosDiversos.jasper"
 */
public class RelatorioAnaliticoParticipacaoCSVService  {

	private static final String DOCUMENTOS = "Documentos";
	private static final String FRETES_LOCAL = "FretesLocal";
	private static final String BDM = "BDM";
	private static final String SERVICOS_ADICIONAIS = "ServicosAdicionais";
	private static final String REEMBARQUES = "Reembarques";
	private static final String DOC_COMPETENCIA_ANTERIOR = "DocumentosCompetenciaAnterior";
	private static final String IRE_TRI = "IreTri";
	private static final String SEPARATOR = ";";
	
	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;
	private ReembarqueDoctoServicoFranqueadoService reembarqueFranqueadoService;
	private LancamentoFranqueadoService lancamentoFranqueadoService;

	public Map<String, Object> execute(Map<String, Object> parameters) {
		boolean filtraFranquia = (parameters.containsKey("idFilial") && parameters.get("idFilial") != null);

		Map<String,Object> result = new HashMap<String, Object>();

		final List<Map<String, Object>> listaDocumentos = doctoServicoFranqueadoService.findRelatorioAnaliticoDocumentos(filtraFranquia,true, parameters);
		final List<Map<String, Object>> listaReembarque = reembarqueFranqueadoService.findRelatorioAnaliticoReembarques(filtraFranquia,true, parameters);
		final List<Map<String, Object>> listaFreteLocal = doctoServicoFranqueadoService.findRelatorioAnaliticoFretesLocal(filtraFranquia, true, parameters);
		final List<Map<String, Object>> listaBDM = doctoServicoFranqueadoService.findRelatorioAnaliticoBDM(filtraFranquia, true, parameters);
		final List<Map<String, Object>> listaServicoAdicional = doctoServicoFranqueadoService.findRelatorioAnaliticoServicosAdicionais(filtraFranquia,true, parameters);	
		final List<Map<String, Object>> listaCompetenciaAnterior = doctoServicoFranqueadoService.findRelatorioAnaliticoDocumentosCompetenciaAnterior(filtraFranquia,true, parameters);
		final List<Map<String, Object>> listaIRE = lancamentoFranqueadoService.findRelatorioAnaliticoIRE(filtraFranquia, true, parameters);
		
		result.putAll(FranqueadoUtils.convertMappedListToCsv(DOCUMENTOS, listaDocumentos, SEPARATOR));
		result.putAll(FranqueadoUtils.convertMappedListToCsv(FRETES_LOCAL, listaFreteLocal, SEPARATOR));
		result.putAll(FranqueadoUtils.convertMappedListToCsv(BDM, listaBDM, SEPARATOR));
		result.putAll(FranqueadoUtils.convertMappedListToCsv(SERVICOS_ADICIONAIS, listaServicoAdicional, SEPARATOR));
		result.putAll(FranqueadoUtils.convertMappedListToCsv(REEMBARQUES, listaReembarque, SEPARATOR));
		result.putAll(FranqueadoUtils.convertMappedListToCsv(DOC_COMPETENCIA_ANTERIOR, listaCompetenciaAnterior, SEPARATOR));
		result.putAll(FranqueadoUtils.convertMappedListToCsv(IRE_TRI, listaIRE, SEPARATOR));
		
		return result;
	}

	public void setDoctoServicoFranqueadoService(
			DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}

	public void setReembarqueFranqueadoService(
			ReembarqueDoctoServicoFranqueadoService reembarqueFranqueadoService) {
		this.reembarqueFranqueadoService = reembarqueFranqueadoService;
	}

	public void setLancamentoFranqueadoService(
			LancamentoFranqueadoService lancamentoFranqueadoService) {
		this.lancamentoFranqueadoService = lancamentoFranqueadoService;
	}
}
