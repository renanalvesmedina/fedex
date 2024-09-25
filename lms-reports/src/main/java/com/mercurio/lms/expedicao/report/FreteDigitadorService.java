package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.freteDigitadorService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/relatorioFretesDigitador.jasper"
 */
public class FreteDigitadorService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {

		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());

		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("1", "GRUPOFALSO");
		sql.addProjection("2", "TIPOAGRUP");
		sql.addProjection("3", "TOTALCTOGRUPO");
		sql.addProjection("4", "PERCENTCTOTOTAL");
		sql.addProjection("5", "VALORMERCADORIA");
		sql.addProjection("6", "TOTALPESO");
		sql.addProjection("7", "TOTALFRETE");
		sql.addProjection("8", "TOTALICMS");

		sql.addFrom("DUAL", "d");

		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("", "=", idFilial);
			String dsFilial = parameters.getString("filial.pessoa.nmFantasia");
			sql.addFilterSummary("filial", dsFilial);
		}

		YearMonthDay dataInicial = parameters.getYearMonthDay("dataInicial");
		if(dataInicial != null) {
			sql.addCriteria("", ">=", dataInicial, YearMonthDay.class);
			sql.addFilterSummary("periodoInicial", JTFormatUtils.format(dataInicial));
		}

		YearMonthDay dataFinal = parameters.getYearMonthDay("dataFinal");
		if(dataFinal != null) {
			sql.addCriteria("", "<", dataFinal.plusDays(1), YearMonthDay.class);
			sql.addFilterSummary("periodoFinal", JTFormatUtils.format(dataFinal));
		}

		return sql;
	}
}
