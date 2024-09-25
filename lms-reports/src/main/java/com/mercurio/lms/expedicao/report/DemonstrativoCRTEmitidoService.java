package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.demonstrativoCRTEmitidoService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/demonstrativoCRTsEmitidos.jasper"
 */
public class DemonstrativoCRTEmitidoService extends ReportServiceSupport {
	
	private ConversaoMoedaService conversaoMoedaService;
	private ConfiguracoesFacade configuracoesFacade;
	private MoedaPaisService moedaPaisService;
	
	
	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap params = (TypedFlatMap) parameters;
		YearMonthDay dtCotacao = params.getYearMonthDay("dtReferenciaCotacao");
		if(dtCotacao == null) 
			dtCotacao = params.getYearMonthDay("dtEmissaoFinal");

		SqlTemplate sql = getSqlTemplate(params, dtCotacao);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());

		Long idPaisDestino = Long.valueOf(((BigDecimal)configuracoesFacade.getValorParametro("ID_PAIS_BRASIL")).longValue());
 
		Moeda m = moedaPaisService.findMoedaPaisMaisUtilizada(idPaisDestino).getMoeda();
		Long idMoedaDestino = m.getIdMoeda();
		String dsSimboloMoeda = FormatUtils.concatSiglaSimboloMoeda(m);

		 // Parametros para localização do pais origem e para conversão de moeda
        parametersReport.put("conversaoMoedaService", conversaoMoedaService);
        parametersReport.put("dataCotacao", dtCotacao);
        parametersReport.put("idPaisDestino", idPaisDestino);
        parametersReport.put("idMoedaDestino", idMoedaDestino);
        parametersReport.put("moedaSelecionada", dsSimboloMoeda);

		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters, YearMonthDay dtCotacao) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("sum(ds.vl_total_doc_servico)", "valorcobrar");
		sql.addProjection("ds.id_pais", "id_pais");
		sql.addProjection("ds.id_moeda", "id_moeda");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("p.nm_pais_i"), "pais");
		sql.addProjection("f.sg_filial", "sg_filial");
		sql.addProjection("fp.nm_fantasia", "nm_fantasia");

		sql.addFrom("cto_internacional", "c");
		sql.addFrom("docto_servico" ,"ds"); 
		sql.addFrom("filial", "f");
		sql.addFrom("pessoa", "fp");
		sql.addFrom("pais", "p");

		/** Emissao CRT Inicial */
		YearMonthDay dtInicial = parameters.getYearMonthDay("dtEmissaoInicial");
		if(dtInicial != null) {
			sql.addCriteria("dh_emissao", ">=", dtInicial, YearMonthDay.class);
			sql.addFilterSummary("periodoEmissaoCRTInicial", JTFormatUtils.format(dtInicial, JTFormatUtils.DEFAULT));
		}
		/** Emissao CRT Final */
		YearMonthDay dtFinal = parameters.getYearMonthDay("dtEmissaoFinal");
		if(dtFinal != null) {
			sql.addCriteria("dh_emissao", "<", dtFinal.plusDays(1), YearMonthDay.class);
			sql.addFilterSummary("periodoEmissaoCRTFinal", JTFormatUtils.format(dtFinal, JTFormatUtils.DEFAULT));
		}
		/** Data Cotacao */
		if (dtCotacao != null) {
			sql.addFilterSummary("dataReferenciaCotacao", JTFormatUtils.format(dtCotacao, JTFormatUtils.DEFAULT));
		}
		/** Filial */
		Long idFilial = parameters.getLong("filial.idFilial");
		if (idFilial != null) {
			sql.addFilterSummary("filial", parameters.getString("filial.sgFilial") + " - " + parameters.getString("filial.pessoa.nmFantasia"));
			sql.addCriteria("ds.id_filial_origem", "=", parameters.getLong("filial.idFilial"));
		}
		sql.addCriteria("ds.tp_documento_servico", "=", ConstantesExpedicao.CONHECIMENTO_INTERNACIONAL, String.class);
		sql.addCriteria("c.tp_situacao_crt", "=", ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO, String.class);

		String filiais = this.getFiliaisUsuarioLogado();
		if(filiais != null) {
			sql.addCustomCriteria(filiais);
		}

		sql.addJoin("ds.id_docto_servico" , "c.id_cto_internacional");
		sql.addJoin("ds.id_filial_origem", "f.id_filial");
		sql.addJoin("f.id_filial", "fp.id_pessoa");
		sql.addJoin("ds.id_pais", "p.id_pais");

		sql.addGroupBy("ds.id_pais");
		sql.addGroupBy("ds.id_moeda");
		sql.addGroupBy(PropertyVarcharI18nProjection.createProjection("p.nm_pais_i"));
		sql.addGroupBy("f.sg_filial");
		sql.addGroupBy("fp.nm_fantasia");

		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("p.nm_pais_i"));
		sql.addOrderBy("f.sg_filial");
		return sql;
	}

	private String getFiliaisUsuarioLogado() {
		List filiais = SessionUtils.getFiliaisUsuarioLogado();

		if(filiais != null && filiais.size() > 0) {
			StringBuilder sql = new StringBuilder();
			boolean first = true;
			for(Iterator it = filiais.iterator(); it.hasNext();) {
				Filial filial = (Filial) it.next();
				if(first) {
					first = false;
					sql.append("ds.id_filial_origem in (").append(filial.getIdFilial());
				} else {
					sql.append(", ").append(filial.getIdFilial());
				}
			}
			sql.append(")");
			return sql.toString();
		}

		return null;
	}

}
