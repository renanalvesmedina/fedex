package com.mercurio.lms.sim.report;

import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;


/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.sim.emitirDocumentosServicoEmitidosUFService"
 * @spring.property name="reportName" value="com/mercurio/lms/sim/report/emitirDocumentosServicoEmitidosUF.jasper"
 */
public class EmitirDocumentosServicoEmitidosUFService extends EmitirDocumentosServicoEmitidosMunicipioService {

	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		return super.execute(parameters);
		
	}	

}
