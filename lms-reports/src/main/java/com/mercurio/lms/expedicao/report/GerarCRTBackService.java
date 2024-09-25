package com.mercurio.lms.expedicao.report;

import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;


/**
 * @author 
 *
 * @spring.bean id="lms.expedicao.gerarCRTBackService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirCRTBack.jasper"
 */

public class GerarCRTBackService extends ReportServiceSupport { 

	public JRReportDataObject execute(Map parameters) throws Exception {
		return createReportDataObject(new JREmptyDataSource(),parameters);
	}

}
