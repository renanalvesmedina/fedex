package com.mercurio.lms.sim.report;

import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;


/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.sim.emitirDocumentosServicoEmitidosTotaisService"
 * @spring.property name="reportName" value="com/mercurio/lms/sim/report/emitirDocumentosServicoEmitidosTotais.jasper"
 */
public class EmitirDocumentosServicoEmitidosTotaisService extends EmitirDocumentosServicoEmitidosMunicipioService {


	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		return super.execute(parameters);
		
	}	
}
