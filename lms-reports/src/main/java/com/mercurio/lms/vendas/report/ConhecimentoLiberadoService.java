package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.vendas.conhecimentoLiberadoService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirListagemCtosLiberados.jasper"
 */
public class ConhecimentoLiberadoService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		// Regra 1.7
		YearMonthDay dtIni = ((TypedFlatMap)parameters).getYearMonthDay("dataEmissaoInicial");
		YearMonthDay dtFin = ((TypedFlatMap)parameters).getYearMonthDay("dataEmissaoFinal");
		if ((dtIni != null && dtFin != null) && JTDateTimeUtils.getIntervalInDays(dtIni, dtFin) > 31)
			throw(new BusinessException("LMS-04134"));
		
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);

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
		
		sql.addProjection("r.sg_regional", "sg_regional");
		sql.addProjection("fo.sg_filial", "sg_filial_ori");
		sql.addProjection("c.nr_conhecimento","nr_conhecimento");
		sql.addProjection("c.dv_conhecimento","dv_conhecimento");
		sql.addProjection("fd.sg_filial","sg_filial_des");
		sql.addProjection("ds.dh_emissao","dh_emissao");
		sql.addProjection("pr.nm_pessoa","nm_pessoa_remet");
		sql.addProjection("pd.nm_pessoa","nm_pessoa_dest");
		sql.addProjection("u.nm_usuario","nm_usuario");
		sql.addProjection("lds.tp_bloqueio_liberado","tp_bloqueio_liberado");
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tle.ds_tipo_liberacao_embarque_i"),"ds_tipo_liberacao_embarque");

		sql.addFrom("filial fo");
		sql.addFrom("filial", "fd");
		sql.addFrom("pessoa", "pr");
		sql.addFrom("pessoa", "pd");
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("conhecimento", "c");
		sql.addFrom("usuario", "u");
		sql.addFrom("tipo_liberacao_embarque", "tle");
		sql.addFrom("regional", "r");
		sql.addFrom("regional_filial", "rf");
		sql.addFrom("liberacao_doc_serv", "lds");

		sql.addJoin("ds.id_docto_servico", "c.id_conhecimento");
		sql.addJoin("ds.id_filial_origem", "fo.id_filial");
		sql.addJoin("ds.id_filial_destino", "fd.id_filial");
		sql.addJoin("fo.id_filial", "rf.id_filial");
		sql.addJoin("rf.id_regional", "r.id_regional");
		sql.addJoin("ds.id_cliente_remetente", "pr.id_pessoa");
		sql.addJoin("ds.id_cliente_destinatario", "pd.id_pessoa");
		sql.addJoin("ds.id_docto_servico", "lds.id_docto_servico");
		sql.addJoin("lds.id_tipo_liberacao_embarque", "tle.id_tipo_liberacao_embarque");
		sql.addJoin("lds.id_usuario", "u.id_usuario");

		// WHERE
		sql.addCriteria("ds.tp_documento_servico", "=", "CTR");
		sql.addCriteria("c.tp_situacao_conhecimento", "=", "E");
		sql.addCriteria("rf.dt_vigencia_inicial", "<=", JTDateTimeUtils.getDataAtual());
		sql.addCriteria("rf.dt_vigencia_final", ">=", JTDateTimeUtils.getDataAtual());
		
		Long idRegional = parameters.getLong("regional.idRegional");
		if(idRegional != null) {
			sql.addCriteria("r.id_regional", "=", idRegional);
			String dsRegional = parameters.getString("regional.siglaDescricao");
			sql.addFilterSummary("regional", dsRegional);
		}

		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("ds.id_filial_origem", "=", idFilial);
			String dsFilial = parameters.getString("filial.sgFilial");
			sql.addFilterSummary("filial", dsFilial);
		} else {
			SQLUtils.joinExpressionsWithComma(getIdsFiliais(), sql, "ds.id_filial_origem");
		}

		Long idTipoLiberacaoEmbarque = parameters.getLong("tipoLiberacao.idTipoLiberacaoEmbarque");
		if(idTipoLiberacaoEmbarque != null) {
			sql.addCriteria("lds.id_tipo_liberacao_embarque", "=", idTipoLiberacaoEmbarque);
			String dsTipoLiberacaoEmbarque = parameters.getString("tipoLiberacao.dsTipoLiberacaoEmbarque");
			sql.addFilterSummary("tipoLiberacao", dsTipoLiberacaoEmbarque);
		}

		YearMonthDay dtInicial = parameters.getYearMonthDay("dataEmissaoInicial");
		if(dtInicial != null) {
			sql.addCriteria("ds.dh_emissao", ">=", dtInicial, YearMonthDay.class);
			sql.addFilterSummary("periodoInicial", JTFormatUtils.format(dtInicial, JTFormatUtils.MEDIUM));
		}

		YearMonthDay dtFinal = parameters.getYearMonthDay("dataEmissaoFinal");
		if(dtFinal != null) {
			sql.addCriteria("ds.dh_emissao", "<", dtFinal.plusDays(1), YearMonthDay.class);
			sql.addFilterSummary("periodoFinal", JTFormatUtils.format(dtFinal, JTFormatUtils.MEDIUM));
		}

		String tpBloqueioLiberado = parameters.getString("tpBloqueioLiberado.valor");
		if(tpBloqueioLiberado != null) {
			sql.addCriteria("lds.tp_bloqueio_liberado", "=", tpBloqueioLiberado);
			String dsTipoBloqueioLiberado = parameters.getString("tpBloqueioLiberado.descricao");
			sql.addFilterSummary("bloqueioLiberado", dsTipoBloqueioLiberado);
		}

		sql.addOrderBy("r.sg_regional");
		sql.addOrderBy("r.sg_regional");
		sql.addOrderBy("fo.sg_filial");
		sql.addOrderBy("c.nr_conhecimento");
		sql.addOrderBy("c.dv_conhecimento");

		return sql;
	}

	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_BLOQUEIO_LIBERADO", "DM_TIPO_BLOQUEIO_LIBERADO");
		super.configReportDomains(config);
	}

	private List<Long> getIdsFiliais() {
		List<Long> result = new ArrayList<Long>();
		List<Filial> filiais = SessionUtils.getFiliaisUsuarioLogado();
		for (Filial filial : filiais) {
			result.add(filial.getIdFilial());
		}
		return result;
	}
}
