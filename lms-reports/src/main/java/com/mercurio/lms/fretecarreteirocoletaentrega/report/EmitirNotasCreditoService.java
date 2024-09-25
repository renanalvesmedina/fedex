package com.mercurio.lms.fretecarreteirocoletaentrega.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.UncategorizedSQLException;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.fretecarreteirocoletaentrega.emitirNotasCreditoService"
 * @spring.property name="reportName"
 *                  value="com/mercurio/lms/fretecarreteirocoletaentrega/report/consultarNotasCredito.jasper"
 */

public class EmitirNotasCreditoService extends ReportServiceSupport {
	private ConversaoMoedaService conversaoMoedaService;

	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}

	public void setConversaoMoedaService(
			ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap map = (TypedFlatMap) parameters;

		SqlTemplate sql = createSqlTemplate(map);

		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado()
				.getNmUsuario());
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());

		if (StringUtils.isNotBlank(map.getString("idMoedaDestino"))) {
			parametersReport.put("idMoedaPaisDestino", map
					.getLong("idMoedaDestino"));
			parametersReport.put("idPaisDestino", map.getLong("idPaisDestino"));
			parametersReport.put("moeda", "(" + map.getString("dsSimbolo")
					+ ")");
		}

		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters
				.get("tpFormatoRelatorio"));

		JRReportDataObject jr = null;
		try {
			jr = executeQuery(sql.getSql(), sql.getCriteria());
			jr.setParameters(parametersReport);
		} catch (UncategorizedSQLException e) {
			throw new InfrastructureException(e.getCause());
		}

		return jr;
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) {

		SqlTemplate sqlPrincipal = createSqlTemplate();

		SqlTemplate sqlSubquery = createSqlTemplate();
		
		// ************** PARAMETROS OBRIGATORIOS DA TELA
		// *************************

		YearMonthDay dataInicio = criteria
				.getYearMonthDay("dataEmissaoNotaCreditoInicial");
		YearMonthDay dataFim = criteria
				.getYearMonthDay("dataEmissaoNotaCreditoFinal");

		sqlSubquery.addProjection("NC.ID_NOTA_CREDITO");
		sqlSubquery.addProjection("FIL.ID_FILIAL");
		sqlSubquery.addProjection("MOEDAP.ID_MOEDA", "ID_MOEDA_PAIS_ORIGEM");
		sqlSubquery.addProjection("PAIS.ID_PAIS", "ID_PAIS_ORIGEM");

		sqlSubquery.addProjection("MT.ID_MEIO_TRANSPORTE");
		sqlSubquery.addProjection("PROP.ID_PROPRIETARIO");
		sqlSubquery.addProjection("FIL.SG_FILIAL || ' - ' || PES.NM_FANTASIA",
				"DESC_FILIAL");
		sqlSubquery.addProjection("PESPROP.NR_IDENTIFICACAO");
		sqlSubquery.addProjection("PESPROP.TP_IDENTIFICACAO");
		sqlSubquery.addProjection("PESPROP.NM_PESSOA");
		sqlSubquery.addProjection(
						"NVL2(MT.NR_FROTA,MT.NR_FROTA|| ' - ' ||MT.NR_IDENTIFICADOR, '')",
						"IDENTIFICACAO_MT");

		sqlSubquery.addProjection("NC.NR_NOTA_CREDITO", "NR_NOTA_CREDITO");
		sqlSubquery.addProjection("FIL.SG_FILIAL", "SG_NOTA_CREDITO");
		sqlSubquery.addProjection("NC.DH_EMISSAO");
		sqlSubquery.addProjection("CC.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
		sqlSubquery.addProjection("FILCONT.SG_FILIAL ", "SG_CONTROLE_CARGA");

		sqlSubquery.addProjection("(SELECT SUM(NCP1.QT_NOTA_CREDITO_PARCELA) FROM NOTA_CREDITO_PARCELA NCP1, PARCELA_TABELA_CE PTC WHERE NCP1.ID_NOTA_CREDITO= NC.ID_NOTA_CREDITO AND NCP1.ID_PARCELA_TABELA_CE= PTC.ID_PARCELA_TABELA_CE AND PTC.TP_PARCELA = 'EV') AS QTDE_EVENTO ");
		//As queries abaixo foram separadas em strings para reutilizar na função de conversão de moeda, que não aceita aliases nos parâmetros
		String queryValorEvento = "(SELECT SUM(NCP1.VL_NOTA_CREDITO_PARCELA*NCP1.QT_NOTA_CREDITO_PARCELA) FROM NOTA_CREDITO_PARCELA NCP1, PARCELA_TABELA_CE PTC WHERE NCP1.ID_NOTA_CREDITO= NC.ID_NOTA_CREDITO AND NCP1.ID_PARCELA_TABELA_CE= PTC.ID_PARCELA_TABELA_CE AND PTC.TP_PARCELA = 'EV')";
		String queryValorDiaria = "(SELECT sum(NCP1.VL_NOTA_CREDITO_PARCELA*NCP1.QT_NOTA_CREDITO_PARCELA) FROM NOTA_CREDITO_PARCELA NCP1, PARCELA_TABELA_CE PTC WHERE NCP1.ID_NOTA_CREDITO= NC.ID_NOTA_CREDITO AND NCP1.ID_PARCELA_TABELA_CE= PTC.ID_PARCELA_TABELA_CE AND PTC.TP_PARCELA = 'DH')";
		String queryValorKmexc ="(SELECT SUM(NCP1.VL_NOTA_CREDITO_PARCELA*NCP1.QT_NOTA_CREDITO_PARCELA) FROM NOTA_CREDITO_PARCELA NCP1, PARCELA_TABELA_CE PTC WHERE NCP1.ID_NOTA_CREDITO= NC.ID_NOTA_CREDITO AND NCP1.ID_PARCELA_TABELA_CE= PTC.ID_PARCELA_TABELA_CE AND PTC.TP_PARCELA = 'QU')";
		String queryValorFretePeso ="(SELECT sum(NCP1.VL_NOTA_CREDITO_PARCELA * NCP1.qt_nota_credito_parcela) FROM NOTA_CREDITO_PARCELA NCP1, PARCELA_TABELA_CE PTC WHERE NCP1.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO AND NCP1.ID_PARCELA_TABELA_CE= PTC.ID_PARCELA_TABELA_CE AND PTC.TP_PARCELA = 'FP')";
		String queryValorPercMerc ="(SELECT SUM(NCP1.VL_NOTA_CREDITO_PARCELA) FROM NOTA_CREDITO_PARCELA NCP1, PARCELA_TABELA_CE PTC WHERE NCP1.ID_NOTA_CREDITO= NC.ID_NOTA_CREDITO AND NCP1.ID_PARCELA_TABELA_CE= PTC.ID_PARCELA_TABELA_CE AND PTC.TP_PARCELA = 'PV')";
		String queryValorPercFrete = "(SELECT SUM(NCP1.VL_NOTA_CREDITO_PARCELA) FROM NOTA_CREDITO_PARCELA NCP1, PARCELA_TABELA_CE PTC WHERE NCP1.ID_NOTA_CREDITO= NC.ID_NOTA_CREDITO AND NCP1.ID_PARCELA_TABELA_CE= PTC.ID_PARCELA_TABELA_CE AND PTC.TP_PARCELA = 'PF')";
		
		sqlSubquery.addProjection(queryValorEvento+" AS VALOR_EVENTO ");
		sqlSubquery.addProjection("(SELECT MAX(NCP1.QT_NOTA_CREDITO_PARCELA) FROM NOTA_CREDITO_PARCELA NCP1, PARCELA_TABELA_CE PTC WHERE NCP1.ID_NOTA_CREDITO= NC.ID_NOTA_CREDITO AND NCP1.ID_PARCELA_TABELA_CE= PTC.ID_PARCELA_TABELA_CE AND PTC.TP_PARCELA = 'DH') AS QTDE_DIARIA  ");
		sqlSubquery.addProjection(queryValorDiaria+" AS VALOR_DIARIA  ");
		sqlSubquery.addProjection("(SELECT SUM(NCP1.QT_NOTA_CREDITO_PARCELA) FROM NOTA_CREDITO_PARCELA NCP1, PARCELA_TABELA_CE PTC WHERE NCP1.ID_NOTA_CREDITO= NC.ID_NOTA_CREDITO AND NCP1.ID_PARCELA_TABELA_CE= PTC.ID_PARCELA_TABELA_CE AND PTC.TP_PARCELA = 'QU') AS QTDE_KMEXC ");
		sqlSubquery.addProjection(queryValorKmexc+" AS VALOR_KMEXC ");
		sqlSubquery.addProjection("'1' AS QTDE_FRETEPESO ");
		sqlSubquery.addProjection(queryValorFretePeso+" AS VALOR_FRETEPESO ");
		sqlSubquery.addProjection(queryValorPercMerc+" AS VALOR_PERCMERC ");
		sqlSubquery.addProjection(queryValorPercFrete+" AS VALOR_PERC_FRETE ");

		sqlSubquery.addProjection("(SELECT COUNT(*) FROM NOTA_CREDITO_PARCELA NCP1, PARCELA_TABELA_CE PTC, TABELA_COLETA_ENTREGA TCE WHERE NCP1.ID_NOTA_CREDITO= NC.ID_NOTA_CREDITO AND NCP1.ID_PARCELA_TABELA_CE= PTC.ID_PARCELA_TABELA_CE AND PTC.ID_TABELA_COLETA_ENTREGA = TCE.ID_TABELA_COLETA_ENTREGA and TCE.TP_CALCULO = 'C2') AS CALC2 ");

		sqlSubquery.addProjection("CC.VL_PEDAGIO");

		sqlSubquery.addProjection("RFC.NR_RECIBO_FRETE_CARRETEIRO", "NR_RECIBO");
		sqlSubquery.addProjection("FILREC.SG_FILIAL", "SG_RECIBO");
		sqlSubquery.addProjection("NC.VL_ACRESCIMO");
		sqlSubquery.addProjection("NC.VL_DESCONTO");
		sqlSubquery.addProjection("nvl(NC.VL_ACRESCIMO, 0) - nvl(NC.VL_DESCONTO, 0)",
				"VL_ACRESCIMO_DESCONTO");
		
		addProjectionConversaoMoeda(sqlSubquery, criteria, queryValorDiaria, "VALOR_DIARIA_CONVERTIDO");
		addProjectionConversaoMoeda(sqlSubquery, criteria, queryValorEvento, "VALOR_EVENTO_CONVERTIDO");
		addProjectionConversaoMoeda(sqlSubquery, criteria, queryValorKmexc, "VALOR_KMEXC_CONVERTIDO");
		addProjectionConversaoMoeda(sqlSubquery, criteria, queryValorFretePeso, "VALOR_FRETEPESO_CONVERTIDO");
		addProjectionConversaoMoeda(sqlSubquery, criteria, queryValorPercFrete, "VALOR_PERC_FRETE_CONVERTIDO");
		addProjectionConversaoMoeda(sqlSubquery, criteria, queryValorPercMerc, "VALOR_PERCMERC_CONVERTIDO");
		addProjectionConversaoMoeda(sqlSubquery, criteria, "CC.VL_PEDAGIO", "VL_PEDAGIO_CONVERTIDO");
		
		sqlSubquery.addFrom("FILIAL", "FIL");
		sqlSubquery.addFrom("PESSOA", "PES");
		sqlSubquery.addFrom("NOTA_CREDITO", "NC");
		sqlSubquery.addFrom("CONTROLE_CARGA", "CC");
		sqlSubquery.addFrom("PROPRIETARIO", "PROP");
		sqlSubquery.addFrom("PESSOA", "PESPROP");
		sqlSubquery.addFrom("MEIO_TRANSPORTE", "MT");
		sqlSubquery.addFrom("FILIAL", "FILCONT");
		sqlSubquery.addFrom("RECIBO_FRETE_CARRETEIRO", "RFC");
		sqlSubquery.addFrom("FILIAL", "FILREC");

		sqlSubquery.addFrom("PAIS", "PAIS");
		sqlSubquery.addFrom(" MOEDA_PAIS", "MOEDAP");

		sqlSubquery.addJoin("NC.ID_FILIAL", "FIL.ID_FILIAL");
		sqlSubquery.addJoin("FIL.ID_FILIAL", "PES.ID_PESSOA");
		sqlSubquery.addJoin("CC.ID_NOTA_CREDITO", "NC.ID_NOTA_CREDITO");
		sqlSubquery.addJoin("CC.ID_PROPRIETARIO", "PROP.ID_PROPRIETARIO(+)");
		sqlSubquery.addJoin("PROP.ID_PROPRIETARIO", "PESPROP.ID_PESSOA(+)");
		sqlSubquery.addJoin("CC.ID_TRANSPORTADO", "MT.ID_MEIO_TRANSPORTE(+)");
		sqlSubquery.addJoin("CC.ID_FILIAL_ORIGEM", "FILCONT.ID_FILIAL");
		sqlSubquery.addJoin("NC.ID_RECIBO_FRETE_CARRETEIRO",
				"RFC.ID_RECIBO_FRETE_CARRETEIRO(+)");
		sqlSubquery.addJoin("RFC.ID_FILIAL", "FILREC.ID_FILIAL(+)");

		sqlSubquery.addJoin("NC.ID_MOEDA_PAIS", "MOEDAP.ID_MOEDA_PAIS");
		sqlSubquery.addJoin("MOEDAP.ID_PAIS", "PAIS.ID_PAIS");

		// campos obrigatorios
		sqlSubquery.addCustomCriteria("NC.DH_EMISSAO IS NOT NULL");
		sqlSubquery.addCriteria("trunc(cast(NC.DH_EMISSAO as DATE))", ">=", dataInicio);
		sqlSubquery.addCriteria("trunc(cast(NC.DH_EMISSAO as DATE))", "<=", dataFim);

		// campos nao obrigatorios
		if (StringUtils.isNotBlank(criteria.getString("filial.idFilial"))) {
			sqlSubquery.addCriteria("NC.ID_FILIAL", "=", criteria
					.getLong("filial.idFilial"));
			sqlSubquery.addFilterSummary("filial", criteria.getString("filialSigla")
					+ " - " + criteria.getString("filial.pessoa.nmFantasia"));
		}
		if (StringUtils.isNotBlank(criteria.getString("proprietario.idProprietario"))) {
			
			String identificacao = criteria.getString("proprietarioNrIdentificacao");
			if (identificacao.length() == 11) identificacao = FormatUtils.formatCPF(identificacao);
			else if (identificacao.length() == 14) identificacao = FormatUtils.formatCNPJ(identificacao);
			
			sqlSubquery.addCriteria("CC.ID_PROPRIETARIO", "=",criteria.getLong("proprietario.idProprietario"));
			sqlSubquery.addFilterSummary("proprietario", identificacao+ " - "+ 
					criteria.getString("proprietario.pessoa.nmPessoa"));
		}
		if (StringUtils.isNotBlank(criteria.getString("meioTransporteRodoviario.idMeioTransporte"))) {
			sqlSubquery.addCriteria("CC.ID_TRANSPORTADO", "=", criteria.getLong("meioTransporteRodoviario.idMeioTransporte"));
			sqlSubquery.addFilterSummary("meioTransporte", criteria.getString("meioTransporteNrFrota")+ " - "
							+ criteria.getString("identificacaoMeioTransporte"));
		}

		sqlSubquery.addOrderBy("FIL.SG_FILIAL");
		sqlSubquery.addOrderBy("PESPROP.NM_PESSOA");
		sqlSubquery.addOrderBy("MT.NR_FROTA");
		sqlSubquery.addOrderBy("NC.NR_NOTA_CREDITO");

		if (org.apache.commons.lang.StringUtils.isNotBlank(criteria
				.getString("descricaoMoeda"))) {
			sqlSubquery.addFilterSummary("converterParaMoeda", criteria
					.getString("descricaoMoeda"));

		}
		sqlSubquery.addFilterSummary("periodoEmissaoInicial", JTFormatUtils
				.format(dataInicio));
		sqlSubquery.addFilterSummary("periodoEmissaoFinal", JTFormatUtils
				.format(dataFim));

		sqlPrincipal.addProjection("sql_basico.*");
		sqlPrincipal.addProjection("(nvl(VALOR_EVENTO,0) + "+
				  "nvl(VALOR_DIARIA,0) + "+
				  "nvl(VALOR_KMEXC,0) + "+
				  "nvl(VALOR_FRETEPESO,0) + "+
				  "nvl(VALOR_PERCMERC,0) + "+
				  "nvl(VALOR_PERC_FRETE,0) + "+
				  "nvl(VL_ACRESCIMO_DESCONTO,0)) ", "valor_total_nota");
		sqlPrincipal.addFrom(sqlSubquery,"sql_basico");

		return sqlPrincipal;

	}

	public static String formataIdentificacao(String tpIdentificacao,
			String conteudo) {
		return FormatUtils.formatIdentificacao(tpIdentificacao, conteudo);

	}

	public BigDecimal findConversaoMoeda(Long idMoedaPaisOrigem,
			Long idPaisOrigem, Long idMoedaPaisDestino, Long idPaisDestino,
			BigDecimal valor, String dtCotacaoDate) {
		YearMonthDay dtCotacao = null;
		if (idMoedaPaisDestino != null && idPaisDestino == null) {
			Pais p = SessionUtils.getPaisSessao();
			idPaisDestino = p.getIdPais();
		}
		if (idMoedaPaisDestino == null && idPaisDestino == null) {
			idMoedaPaisDestino = idMoedaPaisOrigem;
			idPaisDestino = idPaisOrigem;
		}
		if (dtCotacaoDate != "" && dtCotacaoDate != null) {
			dtCotacao = ((DateTime) JTFormatUtils
					.buildDateTimeFromTimestampTzString(dtCotacaoDate))
					.toYearMonthDay();
		}
		return getConversaoMoedaService().findConversaoMoeda(idPaisOrigem,
				idMoedaPaisOrigem, idPaisDestino, idMoedaPaisDestino,
				dtCotacao, valor);
	}
	
	private void addProjectionConversaoMoeda(SqlTemplate sql, TypedFlatMap criteria, String campo, String alias){
		StringBuilder projection = new StringBuilder();
		projection.append("F_CONV_MOEDA(");
		projection.append("PAIS.ID_PAIS, ");
		projection.append("MOEDAP.ID_MOEDA, ");
		projection.append(criteria.getString("idPaisDestino")+", ");
		projection.append(criteria.getString("idMoedaDestino")+", ");
		projection.append("NC.DH_EMISSAO, ");
		projection.append(campo);
		projection.append(")");
		sql.addProjection(projection.toString(), alias);
	}

	public JRDataSource generateSubReportConsultaNotaCredito(Long idNotaCredito, Long idFilial) throws Exception {
		
		JRDataSource dataSource = null;
		
		if (idNotaCredito != null && idFilial != null) {
			SqlTemplate sql = null;
			sql = createSqlFracaoPeso (idNotaCredito, idFilial);
			
			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
}
		
		return dataSource;
	}

	private SqlTemplate createSqlFracaoPeso(Long idNotaCredito, Long idFilial) {
		
		SqlTemplate sql = new SqlTemplate();
		
		/** SELECT */
		sql.addProjection("FPPTC.TP_FATOR", "TP_FATOR");
		sql.addProjection("NCP.QT_NOTA_CREDITO_PARCELA", "QT_NOTA");
		sql.addProjection("NCP.VL_NOTA_CREDITO_PARCELA * NCP.QT_NOTA_CREDITO_PARCELA ", "VL_NOTA");
		
		/** FROM */
		sql.addFrom("NOTA_CREDITO", "NC");
		sql.addFrom("NOTA_CREDITO_PARCELA", "NCP");
		sql.addFrom("FAIXA_PESO_PARC_TAB_CE", "FPPTC");
		sql.addFrom("PARCELA_TABELA_CE", "PTE");

		/** JOIN */
		sql.addJoin("NC.ID_NOTA_CREDITO", "NCP.ID_NOTA_CREDITO");
		sql.addJoin("NCP.ID_FAIXA_PESO_PARC_TAB_CE", "FPPTC.ID_FAIXA_PESO_PARC_TAB_CE(+)");
		sql.addJoin("NCP.ID_PARCELA_TABELA_CE", "PTE.ID_PARCELA_TABELA_CE");
		
		/** CRITERIA */
		sql.addCriteria("NC.ID_NOTA_CREDITO", "=", idNotaCredito);
		sql.addCriteria("NC.ID_FILIAL", "=", idFilial);
		sql.addCriteria("PTE.TP_PARCELA", "=", "FP");
		
		return sql;
	}

}
