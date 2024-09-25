package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.vendas.clienteTabelaDesatualizadaSinteticoService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/clientesTabelasAntigasSintetico.jasper"
 */
public class ClienteTabelaDesatualizadaSinteticoService extends ReportServiceSupport {
	private DomainValueService domainValueService;

	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate((TypedFlatMap)parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());

		String tpFormatoRelatorio = ((TypedFlatMap) parameters).getString("tpFormatoRelatorio.valor");
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tpFormatoRelatorio);

		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.setDistinct();

		sql.addProjection("f.sg_filial", "sg_filial");
		sql.addProjection("r.sg_regional", "sg_regional");
		sql.addProjection("u.nm_usuario", "nm_usuario");
		sql.addProjection("ttp.tp_tipo_tabela_preco", "tp_tipo_tabela_preco");
		sql.addProjection("ttp.nr_versao", "nr_versao");
		sql.addProjection("stp.tp_subtipo_tabela_preco", "tp_subtipo_tabela_preco");
		sql.addProjection("count(distinct c.id_cliente)", "nr_clientes");

		sql.addFrom("parametro_cliente", "pc");
		sql.addFrom("tabela_divisao_cliente", "tdc");
		sql.addFrom("divisao_cliente", "dc");
		sql.addFrom("tabela_preco", "tp");
		sql.addFrom("tipo_tabela_preco", "ttp");
		sql.addFrom("subtipo_tabela_preco", "stp");
		sql.addFrom("servico", "s");
		sql.addFrom("cliente", "c");
		sql.addFrom("pessoa", "p");
		sql.addFrom("filial", "f");
		sql.addFrom("regional_filial", "rf");
		sql.addFrom("regional", "r");
		sql.addFrom("promotor_cliente", "pr");
		sql.addFrom("usuario", "u");

		sql.addJoin("tdc.id_tabela_divisao_cliente", "pc.id_tabela_divisao_cliente(+)");
		sql.addJoin("tdc.id_tabela_preco", "tp.id_tabela_preco");
		sql.addJoin("tdc.id_divisao_cliente", "dc.id_divisao_cliente");
		sql.addJoin("c.id_cliente", "dc.id_cliente");
		sql.addJoin("p.id_pessoa", "c.id_cliente");
		sql.addJoin("ttp.id_tipo_tabela_preco", "tp.id_tipo_tabela_preco");
		sql.addJoin("stp.id_subtipo_tabela_preco", "tp.id_subtipo_tabela_preco");
		sql.addJoin("ttp.id_servico", "s.id_servico");
		sql.addJoin("c.id_filial_atende_comercial", "f.id_filial");
		sql.addJoin("f.id_filial", "rf.id_filial");
		sql.addJoin("rf.id_regional", "r.id_regional");
		sql.addJoin("c.id_cliente", "pr.id_cliente(+)");
		sql.addJoin("pr.id_usuario", "u.id_usuario(+)");

		sql.addCriteria("pc.tp_situacao_parametro(+)", "=", "A");
		sql.addCriteria("tp.bl_efetivada", "=", "S");

		//c.tp_cliente = S
		sql.addCriteria("C.TP_CLIENTE","=",ConstantesVendas.CLIENTE_ESPECIAL);
		sql.addCriteria("dc.tp_Situacao","=","A");
		
		//AND r.id_regional = <Regional>
		Long idRegional = parameters.getLong("regional.idRegional");
		if(idRegional != null) {
			sql.addCriteria("r.id_regional", "=", idRegional);
			String dsRegional = parameters.getString("regional.siglaDescricao");
			sql.addFilterSummary("regional", dsRegional);
		}

		//AND c.id_filial_atende_comercial = <Filial>
		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("c.id_filial_atende_comercial", "=", idFilial);
			String dsFilial = parameters.getString("filial.sgFilial");
			sql.addFilterSummary("filial", dsFilial);
		} else {
			SQLUtils.joinExpressionsWithComma(getIdsFiliais(), sql, "c.id_filial_atende_comercial");
		}

		//AND u.id_usuario = <Promotor>
		Long idPromotor = parameters.getLong("usuario.idUsuario");
		if(idPromotor != null) {
			sql.addCriteria("u.id_usuario", "=", idPromotor);
			String dsPromotor = parameters.getString("funcionario.codPessoa.nome");
			sql.addFilterSummary("promotor", dsPromotor);
		}

		sql.addCustomCriteria("(pr.tp_modal = s.tp_modal or pr.tp_modal is null)");

		sql.addCustomCriteria("(pr.tp_abrangencia = s.tp_abrangencia or pr.tp_abrangencia is null)");

		sql.addCriteria("pc.dt_vigencia_final(+)", ">=", JTDateTimeUtils.getDataAtual());

		sql.addCriteria("pc.dt_vigencia_inicial(+)", "<=", JTDateTimeUtils.getDataAtual());		

		sql.addCriteria("tp.dt_vigencia_final", "<", JTDateTimeUtils.getDataAtual());

		sql.addCriteria("rf.dt_vigencia_inicial", "<=", JTDateTimeUtils.getDataAtual());

		sql.addCriteria("rf.dt_vigencia_final", ">=", JTDateTimeUtils.getDataAtual());

		sql.addGroupBy("r.id_regional");
		sql.addGroupBy("r.sg_regional");
		sql.addGroupBy("f.id_filial");
		sql.addGroupBy("f.sg_filial");
		sql.addGroupBy("u.id_usuario");
		sql.addGroupBy("u.nm_usuario");
		sql.addGroupBy("tp.id_tabela_preco");
		sql.addGroupBy("ttp.id_tipo_tabela_preco");
		sql.addGroupBy("ttp.tp_tipo_tabela_preco");
		sql.addGroupBy("ttp.nr_versao");
		sql.addGroupBy("stp.id_subtipo_tabela_preco");
		sql.addGroupBy("stp.tp_subtipo_tabela_preco");

		sql.addOrderBy("r.sg_regional");
		sql.addOrderBy("f.sg_filial");
		sql.addOrderBy("u.nm_usuario");

		String dsTipo = domainValueService.findDomainValueByValue("DM_TIPO_ANALITICO_SINTETICO", parameters.getString("tipo")).getDescription().getValue();
		sql.addFilterSummary("tipo", dsTipo);

		return sql;
	}

	private List<Long> getIdsFiliais() {
		List<Long> result = new ArrayList<Long>();
		List<Filial> filiais = SessionUtils.getFiliaisUsuarioLogado();
		for (Filial filial : filiais) {
			result.add(filial.getIdFilial());
		}
		return result;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}
