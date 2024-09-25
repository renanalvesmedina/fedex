package com.mercurio.lms.carregamento.report;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;


/**
 * Classe responsável pela geração do Relatório de Controle de Cargas - Coleta-Entrega
 * Especificação técnica 05.01.01.03
 */


public class EmitirRelatorioControleCargaColetaEntregaService extends RelatorioControleCargaColetaEntregaService {

	public JRReportDataObject execute(Map parameters) throws Exception {
        return executeMountCabecalho(parameters);		
	}

	public JRDataSource executeSqlManifestos(Long idControleCarga) throws Exception {
        return this.executeMountManifestos(idControleCarga);
    }
    
	public JRDataSource executeSqlPedidosColeta(Long idControleCarga, Long idManifesto) throws Exception {
        return executeMountPedidosColeta(idControleCarga, idManifesto);
    }

   	public JRDataSource executeSqlDocumentosEntrega(Long idControleCarga, Long idManifesto) throws Exception {
       return executeMountDocumentosEntrega(idControleCarga, idManifesto);
   }

}