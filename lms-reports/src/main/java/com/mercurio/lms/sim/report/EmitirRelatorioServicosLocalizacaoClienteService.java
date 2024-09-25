package com.mercurio.lms.sim.report;

import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.sim.emitirRelatorioServicosLocalizacaoClienteService"
 * @spring.property name="reportName" value="com/mercurio/lms/sim/report/emitirServicosLocalizacaoCliente.jasper"
 */
public class EmitirRelatorioServicosLocalizacaoClienteService extends EmitirServicosLocalizacaoClienteService {
	
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		return super.execute(parameters);
		
	}	
}
