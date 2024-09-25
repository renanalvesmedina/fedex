package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.vendas.clienteSituacaoService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/listarClientesStatus.jasper"
 */
public class ClienteSituacaoService extends ReportServiceSupport {

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

		sql.addProjection("f.sg_filial", "sg_filial");
		sql.addProjection("r.sg_regional", "sg_regional");
		sql.addProjection("p.nm_fantasia", "nm_fantasia");
		sql.addProjection("p.nm_pessoa", "nm_pessoa");
		sql.addProjection("p.tp_identificacao", "tp_identificacao");
		sql.addProjection("p.nr_identificacao", "nr_identificacao");
		sql.addProjection("tp_cliente_dom.vl","tp_cliente_vl");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tp_cliente_dom.ds","tp_cliente"));
		sql.addProjection("tp_situacao_dom.vl","tp_situacao_vl");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tp_situacao_dom.ds","tp_situacao"));
		sql.addProjection("c.dt_ultimo_movimento", "dt_ultimo_movimento");
		
		sql.addFrom("pessoa","p");
		sql.addFrom("cliente","c");
		sql.addFrom("filial","f");
		sql.addFrom("regional_filial","rf");
		sql.addFrom("regional","r");
		sql.addFrom(getSelectDominio("DM_TIPO_CLIENTE"), "tp_cliente_dom");
		sql.addFrom(getSelectDominio("DM_STATUS_PESSOA"), "tp_situacao_dom");

		sql.addCustomCriteria("c.id_cliente=p.id_pessoa");
		sql.addCustomCriteria("c.id_filial_atende_comercial=f.id_filial");
		sql.addCustomCriteria("rf.id_filial=f.id_filial");
		sql.addCustomCriteria("rf.id_regional=r.id_regional");
		sql.addCustomCriteria("c.tp_cliente=tp_cliente_dom.vl(+)");
		sql.addCustomCriteria("c.tp_situacao=tp_situacao_dom.vl(+)");
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		sql.addCriteria("rf.dt_vigencia_inicial","<=",dataAtual,YearMonthDay.class);
		sql.addCriteria("rf.dt_vigencia_final",">=",dataAtual,YearMonthDay.class);

		Long idRegional = parameters.getLong("regional.idRegional");
		if(idRegional != null) {
			sql.addCriteria("r.id_regional", "=", idRegional);
			String dsRegional = parameters.getString("regional.siglaDescricao");
			sql.addFilterSummary("regional", dsRegional);
		}

		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("c.id_filial_atende_comercial", "=", idFilial);
			String dsFilial = parameters.getString("filial.sgFilial");
			sql.addFilterSummary("filial", dsFilial);
		}

		String tpTipoCliente = parameters.getString("tpCliente.valor");
		if(tpTipoCliente != null) {
			sql.addCriteria("c.tp_cliente", "=", tpTipoCliente);
			String dsTipoCliente = parameters.getString("tpCliente.descricao");
			sql.addFilterSummary("tipoCliente", dsTipoCliente);
		}

		String tpSituacao = parameters.getString("situacao.valor");
		if(tpSituacao != null) {
			sql.addCriteria("c.tp_situacao", "=", tpSituacao);
			String dsSituacao = parameters.getString("situacao.descricao");
			sql.addFilterSummary("situacao", dsSituacao);
		}

		sql.addOrderBy("r.sg_regional");
		sql.addOrderBy("f.sg_filial");
		sql.addOrderBy("p.nm_pessoa");
		sql.addOrderBy("p.nr_identificacao");

		return sql;
	}

	private String getSelectDominio(String nomeDominio){
		StringBuilder sb = new StringBuilder()
			.append("(select vdom.vl_valor_dominio as vl, vdom.ds_valor_dominio_I as ds ")
			.append("from dominio dom, valor_dominio vdom ")
			.append("where dom.id_dominio = vdom.id_dominio ")
			.append("and dom.nm_dominio='"+nomeDominio+"') ");
		return sb.toString();
	}
}
