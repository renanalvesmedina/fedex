package com.mercurio.lms.fretecarreteiroviagem.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.fretecarreteiroviagem.emitirTotalizacaoCustosService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteiroviagem/report/emitirTotalizacaoCustos.jasper"
 */
public class EmitirTotalizacaoCustosService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {

		TypedFlatMap map = (TypedFlatMap)parameters;		
		SqlTemplate sql = createSqlTemplate(map);
		
		Map parametersReport = new HashMap();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		return jr;
	}
	
	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate sql = createSqlTemplate();
		return sql;
	}	
}
