package com.mercurio.lms.tributos.report;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.dao.ExcecoesSaneamentoDAO;
import com.mercurio.lms.util.session.SessionUtils;

import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;

/**
 * @spring.bean id="lms.tributos.emitirIcmsSTConhecimentosService"
 * @spring.property name="reportName" value="com/mercurio/lms/tributos/report/emitirExecoesSaneamento.jasper"
 */
public class EmitirExcecoesSaneamentoService extends ReportServiceSupport {


	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap map = (TypedFlatMap) parameters;
		SqlTemplate sql = getSqlTemplate(map);
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

	    Map parametersReport = new HashMap();
		parametersReport.put("PARAMETROS_PESQUISA",  sql.getFilterSummary());
        parametersReport.put("USUARIO_EMISSOR", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		jr.setParameters(parametersReport);
		return jr;
}

	private void configureFilterSummary(SqlTemplate sql, TypedFlatMap tfm) {
		String strAux = "";
		
		if (tfm.getYearMonthDay("dtDataInicial") != null) {
			sql.addFilterSummary("dtDataInicial", tfm.getYearMonthDay("dtDataInicial"));
		}
		if (tfm.getYearMonthDay("dtDataFinal") != null) {
			sql.addFilterSummary("dtDataFinal", tfm.getYearMonthDay("dtDataFinal"));
		}
		if (tfm.getString("tpAtualizacao") != null) {
			sql.addFilterSummary("tpAtualizacao", tfm.getString("tpAtualizacao"));
		}
	}
	
	private SqlTemplate getSqlTemplate(TypedFlatMap values) {
		SqlTemplate sql = createSqlTemplate();

		return ExcecoesSaneamentoDAO.getSqlTemplate(sql,values);
	}


	private Map<Object, Object> createParams(TypedFlatMap tfm)  {
		Map<Object, Object> reportParams 	= new HashMap<Object, Object>();
		reportParams.put("dataInicio", tfm.getDateTime("dtDataInicial"));
		reportParams.put("dataFim", 	tfm.getDateTime("dtDataFinal"));
		reportParams.put("tpAtualizacao", tfm.getString("tpAtualizacao"));

		reportParams.put(JRReportDataObject.EXPORT_MODE_PARAM, tfm.getString("tpFormatoRelatorio"));
		reportParams.put(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		reportParams.put(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		reportParams.put(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);

		return reportParams;
	}

	private void addColumn(BufferedWriter writer, String value) throws IOException{
		addColumn(writer, value, false);
	}

	private void addColumn(BufferedWriter writer, String value, boolean last) throws IOException{
		if(value != null){
			writer.write(value);
		}
		if(!last){
			writer.write(";");
		}
	}
}
