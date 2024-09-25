package com.mercurio.lms.tributos.report;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;

import org.joda.time.YearMonthDay;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author Inacio G Klassmann
 *
 * @spring.bean id="lms.tributos.emitirIcmsSTConhecimentosService"
 * @spring.property name="reportName" value="com/mercurio/lms/tributos/report/emitirIcmsSTConhecimentos.jasper"
 */
public class EmitirIcmsSTConhecimentosService extends ReportServiceSupport {
	private DomainValueService domainService 	= new DomainValueService();
	private FilialService filialService			= new FilialService();
	private InscricaoEstadualService ieService 	= new InscricaoEstadualService();


	@Override
	@SuppressWarnings("rawtypes")
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm 		= (TypedFlatMap) parameters;
		SqlTemplate sql  		= getSqlTemplate(tfm);
		JRReportDataObject jr 	= executeQuery(sql.getSql(), sql.getCriteria());
		Map reportParams  		= createParams(tfm);

		jr.setParameters(reportParams);

		return jr;
	}


	@SuppressWarnings({"rawtypes"})
	public File executeReport(Map parameters) throws Exception {
		TypedFlatMap tfm 		= (TypedFlatMap) parameters;
		SqlTemplate sql  		= getSqlTemplate(tfm);
		SqlRowSet rs 			= getJdbcTemplate().queryForRowSet(sql.getSql(), sql.getCriteria());
		File file 				= null;

		if (rs.next()) {
			file = File.createTempFile("report-emitiricmsstconhecimentos-" + System.currentTimeMillis(), ".csv", new ReportExecutionManager().generateOutputDir());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"));

			addColumn(writer, getMessage("filialFatura"));
			addColumn(writer, getMessage("numeroFatura"));
			addColumn(writer, getMessage("dataFatura"));
			addColumn(writer, getMessage("filialCtrc"));
			addColumn(writer, getMessage("numeroCTRC"));
			addColumn(writer, getMessage("dataEmissao"));
			addColumn(writer, getMessage("tipoDocumento"));
			addColumn(writer, getMessage("aliquota"));
			addColumn(writer, getMessage("valorPrestacao"));
			addColumn(writer, getMessage("baseCalculo"));
			addColumn(writer, getMessage("vlrIcms"));
			addColumn(writer, getMessage("icmsRetido"));
			addColumn(writer, getMessage("icmsCreditoPresumido"));
			addColumn(writer, getMessage("pedagio"));
			addColumn(writer, getMessage("valorTotalMercadoria"));
			addColumn(writer, getMessage("razaoSocial") + getMessage("destinatario"));
			addColumn(writer, getMessage("cnpjDestinatario"));
			addColumn(writer, getMessage("ieDestinatario"));
			addColumn(writer, getMessage("numSerieNf"));
			addColumn(writer, getMessage("razaoSocial") + getMessage("remetente"));
			addColumn(writer, getMessage("cnpjRemetente"));
			addColumn(writer, getMessage("ieRemetente"), true);

			writer.write("\n");

			DecimalFormat duasCasas = new DecimalFormat("##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

			do {
				addColumn(writer, rs.getString("sg_filial"));
				addColumn(writer, rs.getString("nr_fatura"));

				if (rs.getDate("dt_fatura") == null) {
					addColumn(writer, null);
				} else {
					addColumn(writer, JTFormatUtils.format(rs.getDate("dt_fatura")));
				}

				addColumn(writer, rs.getString("sg_filial_origem"));
				addColumn(writer, rs.getString("nr_docto_servico"));

				if (rs.getDate("dh_emissao") == null) {
					addColumn(writer, null);
				} else {
					addColumn(writer, JTFormatUtils.format(rs.getDate("dh_emissao")));
				}

				addColumn(writer, rs.getString("tp_documento_servico"));
				addColumn(writer, rs.getBigDecimal("pc_aliquota_icms").toString());
				addColumn(writer, duasCasas.format(rs.getBigDecimal("vl_total_doc_servico")));
				addColumn(writer, duasCasas.format(rs.getBigDecimal("vl_base_calc_imposto")));
				addColumn(writer, duasCasas.format(rs.getBigDecimal("vl_imposto")));
				addColumn(writer, duasCasas.format(rs.getBigDecimal("vl_icms_st")));
				addColumn(writer, duasCasas.format(rs.getBigDecimal("vl_credito_presumido")));
				addColumn(writer, duasCasas.format(rs.getBigDecimal("vl_pegadio")));
				addColumn(writer, duasCasas.format(rs.getBigDecimal("vl_mercadoria")));
				addColumn(writer, rs.getString("nm_destinatario"));
				addColumn(writer, rs.getString("nr_destinatario"));
				addColumn(writer, rs.getString("nr_ie_destinatatio"));
				addColumn(writer, rs.getString("num_nf"));
				addColumn(writer, rs.getString("nm_remetente"));
				addColumn(writer, rs.getString("nr_remetente"));
				addColumn(writer, rs.getString("nr_ie_remetente"), true);
				writer.write("\n");
			} while(rs.next());
			writer.close();
		}

		return file;
	}


	private Map<Object, Object> createParams(TypedFlatMap tfm)  {
		Map<Object, Object> reportParams 	= new HashMap<Object, Object>();
		InscricaoEstadual ie 				= ieService.findById(tfm.getLong("idInscricaoEstadual"));
		Filial filial						= filialService.findById(tfm.getLong("idFilial"));

		reportParams.put("nrInscricaoEstadual", ie.getNrInscricaoEstadual());
		reportParams.put("nrIdentificacao", 	ie.getPessoa().getNrIdentificacao());
		reportParams.put("usuarioEmissor", 		SessionContext.getUser().getNmUsuario());
        reportParams.put("dtDataInicial", 		JTFormatUtils.format(tfm.getYearMonthDay("dtDataInicial")));
        reportParams.put("dtDataFinal", 		JTFormatUtils.format(tfm.getYearMonthDay("dtDataFinal")));
        reportParams.put("sgFilial", 			filial.getSgFilial());

        DomainValue domainValue = domainService.findDomainValueByValue("DM_TIPO_INTEGRANTE_FRETE", tfm.getString("dsIntegranteCtrc"));
        reportParams.put("dsIntegranteCtrc", domainValue.getDescription().toString());

		if (tfm.getString("tpFrete") == null) {
			reportParams.put("tpFrete", "");
		} else {
			domainValue = domainService.findDomainValueByValue("DM_TIPO_FRETE", tfm.getString("tpFrete"));
			reportParams.put("tpFrete", domainValue.getDescription().toString());
		}

		if (tfm.getString("blIcmsSt") == null) {
			reportParams.put("blIcmsSt", "");
		} else {
			domainValue = domainService.findDomainValueByValue("DM_SIM_NAO", tfm.getString("blIcmsSt"));
			reportParams.put("blIcmsSt", domainValue.getDescription().toString());
		}

		reportParams.put(JRReportDataObject.EXPORT_MODE_PARAM, tfm.getString("tpFormatoRelatorio"));
		reportParams.put(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		reportParams.put(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		reportParams.put(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);

		return reportParams;
	}


	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception{
		SqlTemplate sql 		= createSqlTemplate();
		SqlTemplate sqlPedagio 	= createSqlTemplate();
		SqlTemplate sqlNumNf 	= createSqlTemplate();
		SqlTemplate sqlNf 		= createSqlTemplate();

		sqlPedagio.addProjection("pds.vl_parcela");
		sqlPedagio.addFrom("parcela_docto_servico", "pds");
		sqlPedagio.addFrom("parcela_preco", "pp");
		sqlPedagio.addJoin("pds.id_docto_servico", "ds.id_docto_servico");
		sqlPedagio.addJoin("pds.id_parcela_preco", "pp.id_parcela_preco");
		sqlPedagio.addCustomCriteria("pp.cd_parcela_preco='"+ConstantesExpedicao.CD_PEDAGIO+"'");

		sqlNf.addProjection("nfc.nr_nota_fiscal || decode(nfc.ds_serie, null, '', '-') || nfc.ds_serie", "num_nf");
		sqlNf.addProjection("nfc.id_conhecimento", "id_conhecimento");
		sqlNf.addFrom("nota_fiscal_conhecimento", "nfc");
		sqlNf.addOrderBy("nfc.nr_nota_fiscal");

		sqlNumNf.addProjection("wm_concat(t.num_nf)");
		sqlNumNf.addFrom(sqlNf, "t");
		sqlNumNf.addJoin("t.id_conhecimento", "c.id_conhecimento");
		sqlNumNf.add(") as varchar2(13))");
		
		sql.addProjection("ffat.sg_filial",  		"sg_filial");			// Filial fatura
		sql.addProjection("f.nr_fatura",  			"nr_fatura");			// Número fatura
		sql.addProjection("f.dt_emissao",  			"dt_fatura");			// Data fatura
		sql.addProjection("forig.sg_filial",  		"sg_filial_origem");	// Filial CTRC
		sql.addProjection("ds.nr_docto_servico",	"nr_docto_servico");	// Número CTRC
		sql.addProjection("trunc(ds.dh_emissao)",	"dh_emissao");			// Data emissão
		sql.addProjection("ds.tp_documento_servico","tp_documento_servico");// Tipo de documento
		sql.addProjection("pd.nm_pessoa",			"nm_destinatario");		// Razão social destinatário
		sql.addProjection("pd.nr_identificacao",	"nr_destinatario");		// CNPJ destinatário
		sql.addProjection("ds.vl_mercadoria",		"vl_mercadoria");		// Valor total mercadoria
		sql.addProjection("pr.nm_pessoa",			"nm_remetente");		// Razão social remetente
		sql.addProjection("pr.nr_identificacao",	"nr_remetente");		// CNPJ remetente

		sql.addProjection("cast((" +sqlNumNf,					"num_nf");				// Núm-série NF
		
		sql.addProjection("ied.nr_inscricao_estadual",			"nr_ie_destinatatio");			// IE destinatário
		sql.addProjection("ier.nr_inscricao_estadual",			"nr_ie_remetente");				// IE remetente
		sql.addProjection("nvl(ds.vl_total_doc_servico, 0)",	"vl_total_doc_servico");		// Valor prestação
		sql.addProjection("nvl(ds.vl_base_calc_imposto, 0)",	"vl_base_calc_imposto");		// Base cálculo
		sql.addProjection("nvl(ds.pc_aliquota_icms, 0)",		"pc_aliquota_icms");			// Alíquota
		sql.addProjection("nvl(ds.vl_imposto, 0)",				"vl_imposto");					// Valor ICMS
		sql.addProjection("nvl(ds.vl_icms_st, 0)",				"vl_icms_st");					// ICMS retido
		sql.addProjection("nvl(("+ sqlPedagio.getSql() +"),0)", "vl_pegadio");					// Pedágio
		sql.addProjection("nvl(ds.vl_imposto-ds.vl_icms_st,0)", "vl_credito_presumido");		// ICMS crédito presumido

		sql.addFrom("docto_servico", 		"ds");
		sql.addFrom("devedor_doc_serv_fat", "dev");
		sql.addFrom("fatura", 				"f");
		sql.addFrom("filial", 				"forig");
		sql.addFrom("conhecimento", 		"c");
		sql.addFrom("filial", 				"ffat");
		sql.addFrom("pessoa", 				"pr");
		sql.addFrom("inscricao_estadual", 	"ier");
		sql.addFrom("pessoa", 				"pd");
		sql.addFrom("inscricao_estadual",	"ied");
		sql.addFrom("pessoa", 				"pdev");

		sql.addJoin("ds.id_filial_origem", 			"forig.id_filial");
		sql.addJoin("ds.id_docto_servico", 			"c.id_conhecimento");
		sql.addJoin("ds.id_docto_servico", 			"dev.id_docto_servico");
		sql.addJoin("dev.id_fatura", 				"f.id_fatura(+)");
		sql.addJoin("f.id_filial", 					"ffat.id_filial(+)");
		sql.addJoin("ds.id_cliente_remetente", 		"pr.id_pessoa");
		sql.addJoin("ds.id_ie_remetente", 			"ier.id_inscricao_estadual(+)");
		sql.addJoin("ds.id_cliente_destinatario", 	"pd.id_pessoa");
		sql.addJoin("ds.id_ie_destinatario", 		"ied.id_inscricao_estadual(+)");
		sql.addJoin("dev.id_cliente", 				"pdev.id_pessoa");

		sql.addCriteriaIn("c.tp_situacao_conhecimento", new String[]{"E", "B"});
		sql.addCriteria("ds.nr_docto_servico", 			">", 0);
		sql.addCriteria("ds.id_filial_origem", 			"=", tfm.getLong("idFilial"));

		YearMonthDay dtDataInicial = tfm.getYearMonthDay("dtDataInicial");
		YearMonthDay dtDataFinal   = tfm.getYearMonthDay("dtDataFinal");
		if (dtDataInicial != null && dtDataFinal != null) {
			sql.addCustomCriteria("trunc(CAST(ds.dh_emissao AS DATE)) BETWEEN to_date(?, 'dd/mm/yyyy') AND to_date(?, 'dd/mm/yyyy')");
			sql.addCriteriaValue(new Object[]{JTDateTimeUtils.formatDateYearMonthDayToString(dtDataInicial), JTDateTimeUtils.formatDateYearMonthDayToString(dtDataFinal)});
		}

		if ("D".equals(tfm.getString("dsIntegranteCtrc"))) {
			sql.addCriteria("pd.id_pessoa", "=", tfm.getLong("idCliente"));
			sql.addCriteria("ied.id_inscricao_estadual", "=", tfm.getLong("idInscricaoEstadual"));
		} else if ("R".equals(tfm.getString("dsIntegranteCtrc"))) {
			sql.addCriteria("pr.id_pessoa", "=", tfm.getLong("idCliente"));
			sql.addCriteria("ier.id_inscricao_estadual", "=", tfm.getLong("idInscricaoEstadual"));
		} else if ("V".equals(tfm.getString("dsIntegranteCtrc"))) {
			sql.addCriteria("pdev.id_pessoa", "=", tfm.getLong("idCliente"));
		}

		if (tfm.getString("tpFrete") != null && !tfm.getString("tpFrete").trim().isEmpty()) {
			sql.addCriteria("c.tp_frete", "=", tfm.getString("tpFrete"));
		}

		if ("S".equals(tfm.getString("blIcmsSt"))) {
			sql.addCriteria("ds.vl_icms_st", ">", 0);
		} else if ("N".equals(tfm.getString("blIcmsSt"))) {
			sql.addCriteria("ds.vl_icms_st", "=", 0);
		}

		sql.addOrderBy("pr.nr_identificacao");
		sql.addOrderBy("forig.sg_filial");
		sql.addOrderBy("ds.nr_docto_servico");

		return sql;
	}


	public void setDomainService(DomainValueService domainService) {
		this.domainService = domainService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


	public void setIeService(InscricaoEstadualService ieService) {
		this.ieService = ieService;
	}

	private void addColumn(BufferedWriter writer, String value) throws IOException{
		addColumn(writer, value, false);
	}

	private void addColumn(BufferedWriter writer, String value, boolean last) throws IOException{
		if(value != null){
			writer.write(value);
		}
		if(!last){
			writer.write(";");
		}
	}
}
