package com.mercurio.lms.tabelaprecos.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.tabelaprecos.tarifaPromocionalAereoService"
 * @spring.property name="reportName" value="com/mercurio/lms/tabelaprecos/report/relatorioTarifasPromocionaisAereo.jasper"
 */
public class TarifaPromocionalAereoService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate(parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());

		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(Map parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("p.nm_pessoa", "nm_cia_aerea");
		sql.addProjection("ufo.sg_unidade_federativa", "sg_uf_origem");
		sql.addProjection("ao.sg_aeroporto", "sg_aeroporto_origem");
		sql.addProjection("ufd.sg_unidade_federativa", "sg_uf_destino");
		sql.addProjection("ad.sg_aeroporto", "sg_aeroporto_destino");
		sql.addProjection("fp.vl_faixa_progressiva", "vl_faixa_progressiva");
		sql.addProjection("vfp.dt_vigencia_promocao_inicial", "dt_inicio_promocao");
		sql.addProjection("vfp.dt_vigencia_promocao_final", "dt_fim_promocao");
		sql.addProjection("vfp.vl_fixo", "vl_fixo");

		sql.addFrom("TABELA_PRECO", "tp");
		sql.addFrom("TIPO_TABELA_PRECO", "ttp");
		sql.addFrom("TABELA_PRECO_PARCELA", "tpp");
		sql.addFrom("FAIXA_PROGRESSIVA", "fp");
		sql.addFrom("VALOR_FAIXA_PROGRESSIVA", "vfp");
		sql.addFrom("ROTA_PRECO", "rp");
		sql.addFrom("UNIDADE_FEDERATIVA", "ufo");
		sql.addFrom("UNIDADE_FEDERATIVA", "ufd");
		sql.addFrom("AEROPORTO", "ao");
		sql.addFrom("AEROPORTO", "ad");
		sql.addFrom("PESSOA", "p");
		sql.addFrom("EMPRESA", "e");

		Map<String, Object> empresa = MapUtils.getMap(parameters, "empresa");
		Long idEmpresa = MapUtils.getLong(empresa, "idEmpresa");
		if(idEmpresa != null) {
			sql.addCriteria("ttp.id_empresa_cadastrada", "=", idEmpresa);
			String dsEmpresa = MapUtils.getString(MapUtils.getMap(empresa, "pessoa"), "nmPessoa");
			sql.addFilterSummary("ciaAerea", dsEmpresa);
		}

		Long idEmpresaUsuario = SessionUtils.getEmpresaSessao().getIdEmpresa();
		sql.addCriteria("ttp.id_empresa_logada", "=", idEmpresaUsuario, Long.class);

		sql.addCriteria("vfp.bl_promocional", "=", "S", String.class);
		sql.addCriteria("e.tp_empresa", "=", "C", String.class);

		YearMonthDay dtIni = (YearMonthDay) ReflectionUtils.toObject((String) parameters.get("dataInicialPromocao"), YearMonthDay.class);
		YearMonthDay dtFin = (YearMonthDay) ReflectionUtils.toObject((String) parameters.get("dataFinalPromocao"), YearMonthDay.class);

		if((dtIni!=null) && (dtFin!=null)) {
			sql.addCriteria("vfp.dt_vigencia_promocao_inicial", "<=", dtFin, YearMonthDay.class);
			sql.addCriteria("vfp.dt_vigencia_promocao_final", ">=", dtIni, YearMonthDay.class);
			sql.addFilterSummary("periodoInicial", JTFormatUtils.format(dtIni, JTFormatUtils.DEFAULT, JTFormatUtils.YEARMONTHDAY), YearMonthDay.class);
			sql.addFilterSummary("periodoFinal", JTFormatUtils.format(dtFin, JTFormatUtils.DEFAULT, JTFormatUtils.YEARMONTHDAY), YearMonthDay.class);
		} else {
			if(dtIni!= null) {
				sql.addCriteria("vfp.dt_vigencia_promocao_inicial", ">=", dtIni, YearMonthDay.class);
				sql.addFilterSummary("periodoInicial", JTFormatUtils.format(dtIni, JTFormatUtils.DEFAULT, JTFormatUtils.YEARMONTHDAY), YearMonthDay.class);
			}
			if(dtFin!=null) {
				sql.addCustomCriteria("(vfp.dt_vigencia_promocao_final <= ? or vfp.dt_vigencia_promocao_final = ?)");
				sql.addCriteriaValue(dtFin);
				sql.addCriteriaValue(JTDateTimeUtils.MAX_YEARMONTHDAY);
				sql.addFilterSummary("periodoFinal", JTFormatUtils.format(dtFin, JTFormatUtils.DEFAULT, JTFormatUtils.YEARMONTHDAY), YearMonthDay.class);
			}
		}

		sql.addJoin("tp.id_tipo_tabela_preco", "ttp.id_tipo_tabela_preco");
		sql.addJoin("tp.id_tabela_preco", "tpp.id_tabela_preco");
		sql.addJoin("fp.id_tabela_preco_parcela", "tpp.id_tabela_preco_parcela");
		sql.addJoin("vfp.id_faixa_progressiva", "fp.id_faixa_progressiva");
		sql.addJoin("rp.id_rota_preco", "vfp.id_rota_preco");
		sql.addJoin("ufo.id_unidade_federativa", "rp.id_uf_origem");
		sql.addJoin("ufd.id_unidade_federativa", "rp.id_uf_destino");
		sql.addJoin("ao.id_aeroporto", "rp.id_aeroporto_origem");
		sql.addJoin("ad.id_aeroporto", "rp.id_aeroporto_destino");
		sql.addJoin("p.id_pessoa", "ttp.id_empresa_cadastrada");
		sql.addJoin("ttp.id_empresa_cadastrada", "e.id_empresa");

		sql.addOrderBy("p.nm_pessoa");
		sql.addOrderBy("ufo.sg_unidade_federativa");
		sql.addOrderBy("ao.sg_aeroporto");
		sql.addOrderBy("ufd.sg_unidade_federativa");
		sql.addOrderBy("ad.sg_aeroporto");
		sql.addOrderBy("vfp.dt_vigencia_promocao_inicial");
		sql.addOrderBy("fp.vl_faixa_progressiva");

		return sql;
	}
}
