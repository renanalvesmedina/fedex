package com.mercurio.lms.carregamento.report;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;


/**
 * Classe responsável pela geração do Relatório de Controle de Cargas - Coleta
 * Especificação técnica 05.01.01.03
 */


public class EmitirRelatorioControleCargaManifestoViagemService extends RelatorioControleCargaViagemService {
	

	@SuppressWarnings("rawtypes")
	public JRReportDataObject execute(Map parameters) throws Exception {
        return super.executeMountCabecalho(parameters);
	}
	
	public JRDataSource executeMountDocumentosNacionais(Long idControleCarga, Long idManifesto, String origemDados) throws Exception {
		return super.executeMountDocumentosNacionais(idControleCarga, idManifesto, origemDados);
	}
	
	public String findOcorrenciaEntrega(Long idDoctoServico) throws Exception{
		return super.findOcorrenciaEntrega(idDoctoServico);
	}

}
