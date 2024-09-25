package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author Éderson Frozi
 *
 * @spring.bean id="lms.expedicao.controleFormularioCTRCService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirControleFormulariosCTRCCTR.jasper"
 */
public class ControleFormularioCTRCService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate((TypedFlatMap)parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());

		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("c.nr_formulario", "nr_formulario");
		sql.addProjection("c.nr_conhecimento","nr_conhecimento");
		sql.addProjection("c.dv_conhecimento", "dv_conhecimento");
		sql.addProjection("ds.dh_emissao", "dh_emissao");
		sql.addProjection("ds.nr_aidf", "nr_aidf");
		sql.addProjection("f.sg_filial", "sg_filial");
		sql.addProjection("p.nm_fantasia", "nm_filial");
		sql.addProjection("c.tp_situacao_conhecimento", "tp_situacao_conhecimento");

		sql.addFrom("conhecimento", "c");
		sql.addFrom("docto_servico" ,"ds"); 
		sql.addFrom("filial", "f");
		sql.addFrom("pessoa", "p");

		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("f.id_filial", "=", idFilial);
			String dsFilial = parameters.getString("filial.sgFilial") + " - " + parameters.getString("filial.pessoa.nmFantasia");
			sql.addFilterSummary("filial", dsFilial);
		}

		DateTime dhInicial = parameters.getYearMonthDay("dataInicial").toDateMidnight(JTDateTimeUtils.getUserDtz()).toDateTime();
		if(dhInicial != null) {
			sql.addCriteria("ds.dh_emissao", ">=", dhInicial, DateTime.class);
			sql.addFilterSummary("periodoInicial", JTFormatUtils.format(dhInicial, JTFormatUtils.MEDIUM, JTFormatUtils.YEARMONTHDAY));
		}

		DateTime dhFinal = parameters.getYearMonthDay("dataFinal").toDateMidnight(JTDateTimeUtils.getUserDtz()).toDateTime();
		if(dhFinal != null) {
			sql.addCriteria("ds.dh_emissao", "<", dhFinal.plusDays(1), DateTime.class);
			sql.addFilterSummary("periodoFinal", JTFormatUtils.format(dhFinal, JTFormatUtils.MEDIUM, JTFormatUtils.YEARMONTHDAY));
		}
		sql.addCriteria("c.tp_documento_servico", "=", "CTR", String.class);
		sql.addCustomCriteria("c.tp_situacao_conhecimento in (?, ?, ?)");
		sql.addCriteriaValue("E");
		sql.addCriteriaValue("B");
		sql.addCriteriaValue("C");		

		sql.addJoin("ds.id_docto_servico" , "c.id_conhecimento");
		sql.addJoin("ds.id_filial_origem", "f.id_filial");
		sql.addJoin("f.id_filial", "p.id_pessoa");

		sql.addOrderBy("f.sg_filial");
		DomainValue ordenadoPor = parameters.getDomainValue("ordenadoPor");
		if("F".equals(ordenadoPor.getValue())) {
			sql.addOrderBy("c.nr_formulario");
			sql.addOrderBy("c.nr_conhecimento");
		} else if("C".equals(ordenadoPor.getValue())) {
			sql.addOrderBy("c.nr_conhecimento");
			sql.addOrderBy("c.nr_formulario");
		}

		return sql;
	}
}
