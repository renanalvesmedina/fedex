package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.vendas.tabelaFreteInternacionalRotaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteInternacionalRotas.jasper"
 */
public class TabelaFreteInternacionalRotaService extends ReportServiceSupport {

	private TabelasClienteService tabelasClienteService;

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}	
	
	public JRReportDataObject execute(Map parameters) throws Exception {

		SqlTemplate sql = getSqlTemplate(parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		getTabelasClienteService().montaLogoMercurio(parametersReport, getJdbcTemplate());
		
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(Map parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("1", "ORIGEM");
		sql.addProjection("2", "DESTINO");
		sql.addProjection("3", "COLUNA01");
		sql.addProjection("4", "COLUNA02");
		sql.addProjection("5", "COLUNA03");
		sql.addProjection("6", "COLUNA04");
		sql.addProjection("7", "COLUNA05");
		sql.addProjection("8", "COLUNA06");
		sql.addProjection("9", "COLUNA07");
		sql.addProjection("10", "COLUNA08");
		sql.addProjection("11", "FRETEVALOR");

		sql.addFrom("DUAL", "d");
		sql.addCriteria("1", "=", "1");

		return sql;
	}
}
