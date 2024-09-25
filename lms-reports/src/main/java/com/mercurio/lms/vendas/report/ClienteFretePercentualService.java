package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Alexandre Poletto
 *
 * 30.03.02.36 Relação de Clientes com Frete Percentual percentual
 *
 * @spring.bean id="lms.vendas.report.clienteFretePercentualService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/relacaoClientesFretePercentual.jasper"
 */
public class ClienteFretePercentualService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap criteria = (TypedFlatMap) parameters;

		SqlTemplate sql = montaSql(criteria);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());

		String tpFormatoRelatorio = criteria.getString("tpFormatoRelatorio.valor");
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tpFormatoRelatorio);		

		jr.setParameters(parametersReport);
		return jr;
	}

	private SqlTemplate montaSql(TypedFlatMap criteria) {
		SqlTemplate sql = createSqlTemplate();

		sql.setDistinct();
		sql.addProjection("r.sg_regional","REGIONAL");
		sql.addProjection("r.ds_regional", "DESCRICAO_REGIONAL");
		sql.addProjection("f.sg_filial","FILIAL");
		sql.addProjection("pf.nm_fantasia","DESCRICAO_FILIAL");
		sql.addProjection("ser.TP_MODAL","MODAL");		
		sql.addProjection("ser.TP_ABRANGENCIA","ABRANGENCIA");

		sql.addProjection("p.nm_pessoa","CLIENTE");
		sql.addProjection("p.tp_identificacao","TIPO_IDENT");
		sql.addProjection("p.nr_identificacao","IDENTIFICACAO");
		sql.addProjection("d.ds_divisao_cliente","DIVISAO");
		sql.addProjection("ttp.tp_tipo_tabela_preco","tipo_tabela");
		sql.addProjection("ttp.nr_versao","versao");
		sql.addProjection("stp.tp_subtipo_tabela_preco","subtipo_tabela");

		sql.addFrom("cliente c");
		sql.addFrom("divisao_cliente d");
		sql.addFrom("tabela_divisao_cliente tdc");
		sql.addFrom("parametro_cliente pc");
		sql.addFrom("tabela_preco tp");
		sql.addFrom("tipo_tabela_preco ttp");
		sql.addFrom("subtipo_tabela_preco stp");
		sql.addFrom("servico ser");
		sql.addFrom("regional r");
		sql.addFrom("filial f");
		sql.addFrom("pessoa p");
		sql.addFrom("pessoa pf");

		sql.addCustomCriteria("c.id_cliente = d.id_cliente");
		sql.addCustomCriteria("d.id_divisao_cliente = tdc.id_divisao_cliente");
		sql.addCustomCriteria("tdc.id_servico = ser.id_servico");
		sql.addCustomCriteria("tdc.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente");
		sql.addCustomCriteria("tdc.id_tabela_preco = tp.id_tabela_preco");
		sql.addCustomCriteria("tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco");
		sql.addCustomCriteria("tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco");
		sql.addCustomCriteria("ser.TP_MODAL in (?,?)");
		sql.addCriteriaValue("A");
		sql.addCriteriaValue("R");
		sql.addCriteria("ser.TP_ABRANGENCIA","=","N");
		sql.addCriteria("pc.pc_frete_percentual","<>","0");
		sql.addCustomCriteria("pc.PS_FRETE_PERCENTUAL is not null ");
		sql.addCustomCriteria("pc.VL_MINIMO_FRETE_PERCENTUAL is not null ");
		sql.addCustomCriteria("pc.VL_TONELADA_FRETE_PERCENTUAL is not null ");
		sql.addCustomCriteria("r.id_regional = c.id_regional_comercial");
		sql.addCustomCriteria("c.id_filial_atende_comercial = f.id_filial");
		sql.addCustomCriteria("p.id_pessoa = c.id_cliente");
		sql.addCustomCriteria("f.id_filial = pf.id_pessoa");

		Long idRegional = criteria.getLong("regional.idRegional");
		Long idFilial = criteria.getLong("filial.idFilial");
		YearMonthDay dtReferencia = criteria.getYearMonthDay("dataReferencia");

		if(idRegional != null) {
			sql.addCriteria("r.id_regional", "=", idRegional);
			sql.addFilterSummary("regional", criteria.getString("regional.siglaDescricao"));
		}

		if(idFilial != null) {
			sql.addCriteria("f.id_filial", "=", idFilial);
			sql.addFilterSummary("filial", criteria.getString("filial.sgFilial"));
		}

		if(dtReferencia != null){
			sql.addCriteria("pc.dt_vigencia_inicial", "<=", dtReferencia);
			sql.addCustomCriteria("(pc.dt_vigencia_final >= ? or pc.dt_vigencia_final is null) ", dtReferencia);
			sql.addFilterSummary("dataReferencia", dtReferencia);
		}

		sql.addOrderBy("r.sg_regional");
		sql.addOrderBy("f.sg_filial");
		sql.addOrderBy("ser.TP_MODAL");
		sql.addOrderBy("ser.TP_ABRANGENCIA");

		return sql;
	}

}
