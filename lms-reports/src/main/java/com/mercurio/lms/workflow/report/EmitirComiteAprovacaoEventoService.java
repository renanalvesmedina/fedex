package com.mercurio.lms.workflow.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.workflow.emitirComiteAprovacaoEventoService"
 * @spring.property name="reportName" value="com/mercurio/lms/workflow/report/emitirComiteAprovacaoEvento.jasper"
 */
public class EmitirComiteAprovacaoEventoService extends ReportServiceSupport {
	
	public JRReportDataObject execute(Map parameters) throws Exception {		

		SqlTemplate sql = mountSql(parameters);
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		jr.setParameters(parametersReport);
		
		return jr; 
	}
	
	public void configReportDomains(ReportDomainConfig config) {

		 config.configDomainField("modal", "DM_MODAL");
		 config.configDomainField("abrangencia", "DM_ABRANGENCIA");
	}

	private SqlTemplate mountSql(Map parameters) throws Exception{
		
		SqlTemplate sql = this.createSqlTemplate();		
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("c.nm_comite_i"), "nm_comite");
		sql.addProjection("c.tp_modal","modal");
		sql.addProjection("c.tp_abrangencia","abrangencia");
		sql.addProjection("te.nr_tipo_evento","nr_tipo_evento");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("te.ds_tipo_evento_i"), "ds_tipo_evento");
		sql.addProjection("NVL(u.nm_usuario, p.ds_perfil)","integrante");
		sql.addProjection("i.nr_ordem_aprovacao");
		
		sql.addFrom("comite","c");
		sql.addFrom("evento_workflow","ew");
		sql.addFrom("tipo_evento","te");
		sql.addFrom("integrante i left outer join usuario u on i.id_usuario = u.id_usuario" +
				              " left outer join perfil p on i.id_perfil = p.id_perfil");
		sql.addJoin("c.id_comite","i.id_comite");
		sql.addJoin("c.id_comite","ew.id_comite");
		sql.addJoin("ew.id_evento_workflow","te.id_tipo_evento");
		
		
		String id_tipo_evento = (String)((Map)parameters.get("tipoEvento")).get("idTipoEvento");
		if (StringUtils.isNotBlank(id_tipo_evento)) {
			sql.addFilterSummary("evento", (String)parameters.get("nrTipoEvento") + " - " + (String)parameters.get("dsTipoEvento"));
		}
		sql.addCriteria("te.id_tipo_evento","=", (String)((Map)parameters.get("tipoEvento")).get("idTipoEvento"), Long.class);
		
		String tp_modal = (String)parameters.get("modal");
		if (StringUtils.isNotBlank(tp_modal)) {
			sql.addFilterSummary("modal",(String)parameters.get("dsModal"));
		}
		sql.addCriteria("c.tp_modal","=", (String)parameters.get("modal"));
		
		String tp_abrangencia = (String)parameters.get("abrangencia");
		if (StringUtils.isNotBlank(tp_abrangencia)) {
			sql.addFilterSummary("abrangencia",(String)parameters.get("dsAbrangencia"));
		}
		sql.addCriteria("c.tp_abrangencia","=", (String)parameters.get("abrangencia"));
		
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("c.nm_comite_i"));
		sql.addOrderBy("te.nr_tipo_evento");
		sql.addOrderBy("i.nr_ordem_aprovacao");
		
		return sql;
	}
}