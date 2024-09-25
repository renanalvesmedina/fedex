package com.mercurio.lms.carregamento.report;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;


/**
 * Classe respons�vel pela gera��o do Relat�rio de Controle de Cargas - Coleta-Entrega
 * Especifica��o t�cnica 05.01.01.03
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