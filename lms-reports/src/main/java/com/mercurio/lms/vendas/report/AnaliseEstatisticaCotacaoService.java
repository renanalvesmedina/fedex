package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.vendas.analiseEstatisticaCotacaoService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/analiseEstatisticaCotacoes.jasper"
 */
public class AnaliseEstatisticaCotacaoService extends ReportServiceSupport {
	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();
	private ConfiguracoesFacade configuracoesFacade;
	private String tpModal;
	private String tpAbrangencia;

	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("idFilial", parameters.get("filial.idFilial"));
		parametersReport.put("idRegional", parameters.get("regional.idRegional"));
		parametersReport.put("tpCliente", parameters.get("tpCliente.valor"));
		parametersReport.put("idUsuarioRealizou", parameters.get("funcionario.codPessoa.codigo"));
		parametersReport.put("dtGeracaoCotacaoInicial", parameters.get("dataInicial"));
		parametersReport.put("dtGeracaoCotacaoFinal", parameters.get("dataFinal"));

		String tpFormatoRelatorio = ((TypedFlatMap) parameters).getString("tpFormatoRelatorio.valor");
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tpFormatoRelatorio);

		jr.setParameters(parametersReport);

		return jr;
	}

	public JRDataSource executeCotacoesAtendentes(Object[] parameters) throws Exception {
		SqlTemplate sql = super.createSqlTemplate();
		sql.setDistinct();

		sql.addProjection("vf.nm_usuario", "nm_funcionario");

		this.createSharedCounts(sql);
		this.createSharedFroms(sql);

		sql.addFrom("usuario", "vf");

		this.createSharedJoins(sql);
		sql.addJoin("vf.id_usuario", "co.id_usuario_realizou");
		this.createSubReportFilters(parameters, sql);
		sql.addGroupBy("vf.nm_usuario");
		sql.addOrderBy("vf.nm_usuario");

		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}
	
	public JRDataSource executeCotacoesTipoClientes(Object[] parameters) throws Exception {
		SqlTemplate sql = super.createSqlTemplate();
		sql.setDistinct();

		sql.addProjection("nvl(cl.tp_cliente, 'P')", "tp_cliente");

		this.createSharedCounts(sql);
		this.createSharedFroms(sql);

		this.createSharedJoins(sql);

		this.createSubReportFilters(parameters, sql);

		sql.addGroupBy("nvl(cl.tp_cliente, 'P')");
		sql.addOrderBy("nvl(cl.tp_cliente, 'P')");

		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = super.createSqlTemplate();
		sql.setDistinct();

		sql.addProjection("r.sg_regional", "sg_regional");
		sql.addProjection("r.id_regional", "id_regional");
		sql.addProjection("f.sg_filial", "sg_filial");
		sql.addProjection("f.id_filial", "id_filial");
		sql.addProjection("co.dt_geracao_cotacao", "dt_geracao_cotacao");

		this.createSharedCounts(sql);
		this.createSharedFroms(sql);
		this.createSharedJoins(sql);

		Long idRegional = parameters.getLong("regional.idRegional");
		if(idRegional != null) {
			sql.addCriteria("r.id_regional", "=", idRegional);
			String dsRegional = parameters.getString("regional.siglaDescricao");
			sql.addFilterSummary("regional", dsRegional);
		}

		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("co.id_filial", "=", idFilial);
			String dsFilial = parameters.getString("filial.sgFilialHidden");
			sql.addFilterSummary("filial", dsFilial);
		}

		Long idFuncionario = parameters.getLong("funcionario.codPessoa.codigo");
		if(idFuncionario != null) {
			sql.addCriteria("co.id_usuario_realizou", "=", idFuncionario);
			String dsFuncionario = parameters.getString("funcionario.codPessoa.nome");
			sql.addFilterSummary("funcionarioSolicitante", dsFuncionario);
		}

		String idTipoCliente = parameters.getString("tpCliente.valor");
		if(StringUtils.isNotBlank(idTipoCliente)) {
			sql.addCriteria("nvl(cl.tp_cliente, 'P')", "=", idTipoCliente);
			String dsTipoCliente = parameters.getString("tpCliente.descricao");
			sql.addFilterSummary("tipoCliente", dsTipoCliente);
		}

		tpModal = parameters.getString("tpModal.valor");
		if(StringUtils.isNotBlank(tpModal)) {
			sql.addCriteria("s.tp_modal", "=", tpModal);
			String dsTpModal = parameters.getString("tpModal.descricao");
			sql.addFilterSummary("modal", dsTpModal);
		}

		tpAbrangencia = parameters.getString("tpAbrangencia.valor");
		if(StringUtils.isNotBlank(tpAbrangencia)) {
			sql.addCriteria("s.tp_abrangencia", "=", tpAbrangencia);
			String dsTpAbrangencia = parameters.getString("tpAbrangencia.descricao");
			sql.addFilterSummary("abrangencia", dsTpAbrangencia);
		}

		YearMonthDay dataInicial = parameters.getYearMonthDay("dataInicial");
		if(dataInicial != null) {
			sql.addCriteria("co.dt_geracao_cotacao", ">=", dataInicial, YearMonthDay.class);
			sql.addFilterSummary("periodoInicial", JTFormatUtils.format(dataInicial));
		}

		YearMonthDay dataFinal = parameters.getYearMonthDay("dataFinal");
		if(dataFinal != null) {
			sql.addCriteria("co.dt_geracao_cotacao", "<", dataFinal.plusDays(1), YearMonthDay.class);
			sql.addFilterSummary("periodoFinal", JTFormatUtils.format(dataFinal));
		}

		if(StringUtils.isNotBlank(tpAbrangencia)) {
			Moeda moeda = null;
			if(ConstantesExpedicao.ABRANGENCIA_NACIONAL.equals(tpAbrangencia)) {
				moeda = configuracoesFacade.getMoeda(ConstantesConfiguracoes.NBR_ISO_BR_REAL);
			} else {
				moeda = configuracoesFacade.getMoeda(ConstantesConfiguracoes.NBR_ISO_US_DOLLAR);
			}
			String dsMoeda = FormatUtils.concatSiglaSimboloMoeda(moeda);
			sql.addFilterSummary("moeda", dsMoeda);
		}

		sql.addGroupBy("co.dt_geracao_cotacao");
		sql.addGroupBy("f.sg_filial");
		sql.addGroupBy("f.id_filial");
		sql.addGroupBy("r.sg_regional");
		sql.addGroupBy("r.id_regional");

		sql.addOrderBy("r.sg_regional");
		sql.addOrderBy("f.sg_filial");
		sql.addOrderBy("co.dt_geracao_cotacao");

		return sql;
	}

	private void createSharedCounts(SqlTemplate sql) {
		String qtAprovadas = this.createCotacaoSelect("count", "cot.id_cotacao", "cot.tp_situacao = 'E'", "qt_aprovadas", "");
		String qtReprovadas = this.createCotacaoSelect("count", "cot.id_cotacao", "cot.tp_situacao = 'P'", "qt_reprovadas", "");
		String qtCanceladas = this.createCotacaoSelect("count", "cot.id_cotacao", "cot.tp_situacao = 'CA'", "qt_canceladas", "");
		String qtPendentes = this.createCotacaoSelect("count", "cot.id_cotacao", "(cot.tp_situacao = 'O' or cot.tp_situacao = 'T')", "qt_pendentes", "");
		String vlCif = this.createCotacaoSelect("nvl(sum", "cot.vl_total_cotacao", "cot.tp_frete = 'C'", "vl_cif", "), 0.0");
		String vlFob = this.createCotacaoSelect("nvl(sum", "cot.vl_total_cotacao", "cot.tp_frete = 'F'", "vl_fob", "), 0.0");

		sql.addProjection(qtAprovadas);
		sql.addProjection(qtReprovadas);
		sql.addProjection(qtCanceladas);
		sql.addProjection(qtPendentes);
		sql.addProjection(vlCif);
		sql.addProjection(vlFob);
	}

	private void createSharedFroms(SqlTemplate sql) {
		sql.addFrom("filial", "f");
		sql.addFrom("regional", "r");
		sql.addFrom("regional_filial", "rf");
		sql.addFrom("cliente", "cl");
		sql.addFrom("cotacao", "co");
		sql.addFrom("servico", "s");
	}

	private void createSharedJoins(SqlTemplate sql) {
		sql.addJoin("co.id_cliente", "cl.id_cliente (+)");
		sql.addJoin("co.id_servico", "s.id_servico");
		sql.addJoin("co.id_filial", "f.id_filial");
		sql.addJoin("rf.id_filial", "f.id_filial");
		sql.addJoin("rf.id_regional", "r.id_regional");

		String filiais = this.getFiliaisUsuarioLogado();
		if(filiais != null) {
			sql.addCustomCriteria(filiais);
		}
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		sql.addCriteria("rf.dt_vigencia_inicial", "<=", dataAtual, YearMonthDay.class);
		sql.addCriteria("rf.dt_vigencia_final", ">=", dataAtual, YearMonthDay.class);
	}

	private void createSubReportFilters(Object[] parameters, SqlTemplate sql) {
		Long idFilial = (Long) parameters[0];
		if(idFilial != null) {
			sql.addCriteria("co.id_filial", "=", idFilial);
		}

		Long idRegional = (Long) parameters[1];
		if(idRegional != null) {
			sql.addCriteria("r.id_regional", "=", idRegional);
		}

		String tpCliente = (String) parameters[2];
		if(StringUtils.isNotBlank(tpCliente)) {
			sql.addCriteria("nvl(cl.tp_cliente, 'P')", "=", tpCliente);
		}

		String idUsuarioRealizou = (String) parameters[3];
		if(StringUtils.isNotBlank(idUsuarioRealizou)) {
			sql.addCriteria("co.id_usuario_realizou", "=", Long.valueOf(idUsuarioRealizou));
		}

		String dtGeracaoCotacaoInicial = (String) parameters[4];
		if(StringUtils.isNotBlank(dtGeracaoCotacaoInicial)) {
			String formatedDate = JTDateTimeUtils.convertFrameworkDateToFormat(dtGeracaoCotacaoInicial, "ddMMyyyy");
			sql.addCriteria("co.dt_geracao_cotacao", ">=", 
					JTDateTimeUtils.convertDataStringToYearMonthDay(formatedDate), 
					YearMonthDay.class);
		}

		String dtGeracaoCotacaoFinal = (String) parameters[5];
		if(StringUtils.isNotBlank(dtGeracaoCotacaoFinal)) {
			String formatedDate = JTDateTimeUtils.convertFrameworkDateToFormat(dtGeracaoCotacaoFinal, "ddMMyyyy");
			sql.addCriteria("co.dt_geracao_cotacao", "<=", 
					JTDateTimeUtils.convertDataStringToYearMonthDay(formatedDate), 
					YearMonthDay.class);
		}

		if(StringUtils.isNotBlank(tpModal)) {
			sql.addCriteria("s.tp_modal", "=", tpModal);
		}

		if(StringUtils.isNotBlank(tpAbrangencia)) {
			sql.addCriteria("s.tp_abrangencia", "=", tpAbrangencia);
		}
	}
	
	private String createCotacaoSelect(String operation, String select, String condicao, String alias, String extra) {
		StringBuffer sql = new StringBuffer();
		sql.append(operation + "((select " + select + LINE_SEPARATOR);
		sql.append("        from cotacao cot" + LINE_SEPARATOR);
		sql.append("       where " + condicao + " and" + LINE_SEPARATOR);
		sql.append("             cot.id_cotacao = co.id_cotacao)" + extra + LINE_SEPARATOR);
		sql.append("       ) " + alias + LINE_SEPARATOR);
		return sql.toString();
	}

	private String getFiliaisUsuarioLogado() {
		List<Filial> filiais = SessionUtils.getFiliaisUsuarioLogado();

		if(filiais != null && filiais.size() > 0) {
			StringBuilder sql = new StringBuilder();
			boolean first = true;
			for (Filial filial : filiais) {
				if(first) {
					first = false;
					sql.append("f.id_filial in (").append(filial.getIdFilial());
				} else {
					sql.append(", ").append(filial.getIdFilial());
				}
			}
			sql.append(")");
			return sql.toString();
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.report.ReportServiceSupport#configReportDomains(com.mercurio.adsm.framework.report.ReportServiceSupport.ReportDomainConfig)
	 */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_CLIENTE", "DM_TIPO_CLIENTE");
	}

	/**
	 * @param configuracoesFacade The configuracoesFacade to set.
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}
