package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.vendas.clienteLiberadoMunicipioEmbarqueProibidoService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/clientesLiberadosMunicipiosEmbarqueProibido.jasper"
 */
public class ClienteLiberadoMunicipioEmbarqueProibidoService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {

		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("1", "REGIONAL");
		sql.addProjection("2", "FILIAL");
		sql.addProjection("3", "CLIENTE");
		sql.addProjection("4", "CNPJ");
		sql.addProjection("5", "MODAL");
		sql.addProjection("6", "MUNICIPIOLIB");

		sql.addFrom("DUAL", "d");

		Long idRegional = parameters.getLong("regional.idRegional");
		if(idRegional != null) {
			sql.addCriteria("", "=", idRegional);
			String dsRegional = parameters.getString("regional.siglaDescricao");
			sql.addFilterSummary("regional", dsRegional);
		}

		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("", "=", idFilial);
			String dsFilial = parameters.getString("filial.pessoa.nmPessoa");
			sql.addFilterSummary("filial", dsFilial);
		}

		return sql;
	}
}
