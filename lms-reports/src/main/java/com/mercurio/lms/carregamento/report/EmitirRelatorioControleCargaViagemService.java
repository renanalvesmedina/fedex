package com.mercurio.lms.carregamento.report;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;


/**
 * Classe responsável pela geração do Relatório de Controle de Cargas - Coleta-Entrega
 * Especificação técnica 05.01.01.03
 */


public class EmitirRelatorioControleCargaViagemService extends RelatorioControleCargaViagemService{
	

	@SuppressWarnings("rawtypes")
	public JRReportDataObject execute(Map parameters) throws Exception {
        return executeMountCabecalho(parameters);		
	}

	public JRDataSource executeSqlManifestos(Long idControleCarga) throws Exception {
        return executeMountManifestos(idControleCarga);
    }

   	public JRDataSource executeSqlDocumentosNacionais(Long idControleCarga, Long idManifesto, String origemDados) throws Exception {
       return executeMountDocumentosNacionais(idControleCarga, idManifesto, origemDados);
   	}

   	public String findOcorrenciaEntrega(Long idDoctoServico) throws Exception{
		return super.findOcorrenciaEntrega(idDoctoServico);
	}
}