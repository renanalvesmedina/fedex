package com.mercurio.lms.carregamento.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;


/**
 * Classe responsável pela geração do Relatório de Controle de Cargas - Coleta
 * Especificação técnica 05.01.01.03
 */


public class EmitirRelatorioControleCargaManifestoColetaService extends RelatorioControleCargaColetaEntregaService {
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		Object[] criteriaManifestos = null;
		
		TypedFlatMap tfm = (TypedFlatMap)parameters;
		Long idControleCarga = tfm.getLong("idControleCarga");
		Long idManifesto = tfm.getLong("idManifesto");

        StringBuilder sqlCabecalho = new StringBuilder("select ").append(idControleCarga).append(" as ID_CONTROLE_CARGA,")
    															.append(idManifesto).append(" as ID_MANIFESTO from dual");
        
        JRReportDataObject jRReportDataObject = executeQuery(sqlCabecalho.toString(), criteriaManifestos);
        
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
                
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        
        jRReportDataObject.setParameters(parametersReport);
        
        return jRReportDataObject;		
	}
	
	public JRDataSource executeMountCabecalho(Long idControleCarga, Long idManifesto) throws Exception {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("idControleCarga", idControleCarga);
		tfm.put("idManifesto", idManifesto);
		tfm.put("tpManifesto", ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_COLETA);
		return super.executeMountCabecalho(tfm).getDataSource();
	}
	
	public JRDataSource executeMountPedidosColeta(Long idControleCarga, Long idManifesto) throws Exception {
		return super.executeMountPedidosColeta(idControleCarga, idManifesto);
	}

}