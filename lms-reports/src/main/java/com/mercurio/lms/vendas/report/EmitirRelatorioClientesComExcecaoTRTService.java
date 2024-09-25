package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 *
 * 
 * @spring.bean id="lms.vendas.emitirRelatorioClientesComExcecaoTRTService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirRelatorioClientesComExcecaoTRT.jrxml"
 *                  
 */
public class EmitirRelatorioClientesComExcecaoTRTService extends ReportServiceSupport {

	
	private static final String MAX_YEAR_MONTH_DAY_STRING = "01/01/4000"; 
	private static String PDF_REPORT_NAME = "com/mercurio/lms/vendas/report/emitirRelatorioClientesComExcecaoTRT.jasper";
    private static String EXCEL_REPORT_NAME = "com/mercurio/lms/vendas/report/emitirRelatorioClientesComExcecaoTRTExcel.jasper";
	
	
	@SuppressWarnings("unchecked")
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap map = new TypedFlatMap(criteria);
		SqlTemplate sql = createSqlTemplate(map);
		String formato = (String) criteria.get("formatoRelatorio"); 
			if(formato.equals("pdf")){
				this.setReportName(PDF_REPORT_NAME);				
			} else {
				this.setReportName(EXCEL_REPORT_NAME);
			}
		// Seta os parametros
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, "csv".equals(formato) ? "xls" : formato);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		jr.setParameters(parametersReport);
		return jr;
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		
		/** SELECT */
		sql.addProjection("REGIONAL.SG_REGIONAL", "regional");
		sql.addProjection("FILIAL.SG_FILIAL", "filial");
		sql.addProjection("PESSOA.NR_IDENTIFICACAO", "nrCpfCnpj");
		sql.addProjection("PESSOA.NM_PESSOA", "nmRazaoSocial");
		sql.addProjection("CASE WHEN TRT_CLIENTE.DT_VIGENCIA_INICIAL = TO_DATE('"+ MAX_YEAR_MONTH_DAY_STRING + "', 'DD/MM/YYYY') THEN NULL ELSE TRT_CLIENTE.DT_VIGENCIA_INICIAL END", "vigenciaInicial");
		sql.addProjection("CASE WHEN TRT_CLIENTE.DT_VIGENCIA_FINAL = TO_DATE('"+ MAX_YEAR_MONTH_DAY_STRING + "', 'DD/MM/YYYY') THEN NULL ELSE TRT_CLIENTE.DT_VIGENCIA_FINAL END", "vigenciaFinal");
		sql.addProjection("UNIDADE_FEDERATIVA.SG_UNIDADE_FEDERATIVA", "sgUF");
		sql.addProjection("MUNICIPIO.NM_MUNICIPIO", "municipio");
		sql.addProjection("MUNICIPIO_TRT_CLIENTE.BL_COBRA_TRT", "cobraSN");
		sql.addProjection("PESSOA.TP_IDENTIFICACAO", "tpIdentificacao");
		sql.addProjection("CASE WHEN (TRT_CLIENTE.DT_VIGENCIA_INICIAL_SOLICITADA = TO_DATE('"+ MAX_YEAR_MONTH_DAY_STRING + "', 'DD/MM/YYYY') OR TRT_CLIENTE.TP_SITUACAO_APROVACAO = 'A') THEN NULL ELSE TRT_CLIENTE.DT_VIGENCIA_INICIAL_SOLICITADA END", "vigenciaInicialSolicitada");
		sql.addProjection("CASE WHEN (TRT_CLIENTE.DT_VIGENCIA_FINAL_SOLICITADA = TO_DATE('"+ MAX_YEAR_MONTH_DAY_STRING + "', 'DD/MM/YYYY') OR TRT_CLIENTE.TP_SITUACAO_APROVACAO = 'A') THEN NULL ELSE TRT_CLIENTE.DT_VIGENCIA_FINAL_SOLICITADA END", "vigenciaFinalSolicitada");
		sql.addProjection("TRT_CLIENTE.TP_SITUACAO_APROVACAO", "tpSituacaoAprovacao");
		
		/** FROM */
		sql.addFrom("TRT_CLIENTE");
		sql.addFrom("MUNICIPIO_TRT_CLIENTE");
		sql.addFrom("CLIENTE");
		sql.addFrom("PESSOA");
		sql.addFrom("FILIAL");
		sql.addFrom("REGIONAL");
		sql.addFrom("REGIONAL_FILIAL");
		sql.addFrom("MUNICIPIO");
		sql.addFrom("UNIDADE_FEDERATIVA");
		
		/** JOIN */
		sql.addJoin("MUNICIPIO_TRT_CLIENTE.ID_TRT_CLIENTE","TRT_CLIENTE.ID_TRT_CLIENTE");
		sql.addJoin("CLIENTE.ID_CLIENTE","TRT_CLIENTE.ID_CLIENTE");
		sql.addJoin("PESSOA.ID_PESSOA","CLIENTE.ID_CLIENTE");
		sql.addJoin("FILIAL.ID_FILIAL","CLIENTE.ID_FILIAL_ATENDE_COMERCIAL");
		sql.addJoin("REGIONAL_FILIAL.ID_FILIAL","FILIAL.ID_FILIAL");
		sql.addJoin("REGIONAL.ID_REGIONAL","REGIONAL_FILIAL.ID_REGIONAL");
		sql.addJoin("MUNICIPIO.ID_MUNICIPIO","MUNICIPIO_TRT_CLIENTE.ID_MUNICIPIO");
		sql.addJoin("UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA","MUNICIPIO.ID_UNIDADE_FEDERATIVA");
		sql.addCustomCriteria("TRUNC(SYSDATE) BETWEEN REGIONAL_FILIAL.DT_VIGENCIA_INICIAL AND REGIONAL_FILIAL.DT_VIGENCIA_FINAL");
		sql.addCustomCriteria("TRUNC(SYSDATE) BETWEEN REGIONAL.DT_VIGENCIA_INICIAL AND REGIONAL.DT_VIGENCIA_FINAL");


		/** WHERE */
		Long idRegional = criteria.getLong("idRegional");
		if (idRegional != null) {
			sql.addCriteria("REGIONAL.ID_REGIONAL", "=", idRegional);
			sql.addFilterSummary("regional",  criteria.getString("dsRegional"));
		}
		
		Long idFilial = criteria.getLong("idFilial");
		if(idFilial != null){
			sql.addCriteria("CLIENTE.ID_FILIAL_ATENDE_COMERCIAL", "=", idFilial); 
			sql.addFilterSummary("filial", criteria.getString("sgFilial"));
		}
		
		Long idCliente = criteria.getLong("idPessoa");
		if(idCliente!=null){
			sql.addCriteria("CLIENTE.ID_CLIENTE", "=", idCliente); 
			sql.addFilterSummary("cliente", criteria.getString("cliente"));
		}
		
		Long idMunicipio = criteria.getLong("idMunicipio");
		if(idMunicipio != null){
			sql.addCriteria("MUNICIPIO.ID_MUNICIPIO", "=", idMunicipio); 
			sql.addFilterSummary("municipio", criteria.getString("municipio"));
		}
		
		Long idUf = criteria.getLong("idUnidadeFederativa");
		if(idUf != null){
			sql.addCriteria("UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA", "=", idUf); 
			sql.addFilterSummary("unidadeFederativa", criteria.getString("sgUnidadeFederativa"));
		}
    	YearMonthDay dtInicio = (YearMonthDay )criteria.get("dtVigenciaInicial");
    	YearMonthDay dtFim = (YearMonthDay )criteria.get("dtVigenciaFinal");
		String strDtInicio = null;
    	StringBuilder datas = new StringBuilder();
    	if(dtInicio != null){
    		strDtInicio = dtInicio.toString("dd/MM/yyyy");
    		datas.append(" to_date('" + strDtInicio+"','dd/mm/yyyy') >= TRT_CLIENTE.DT_VIGENCIA_INICIAL");
    		sql.addFilterSummary("vigenciaInicial", criteria.getYearMonthDay("dtVigenciaInicial"));
    	}
    	String strDtFim = null;
    	if(dtFim != null){
    		strDtFim = dtFim.toString("dd/MM/yyyy");
    		datas.append(" and to_date('" + strDtFim+"','dd/mm/yyyy') <= TRT_CLIENTE.DT_VIGENCIA_FINAL");
    		sql.addCustomCriteria(datas.toString());
    		sql.addFilterSummary("vigenciaFinal", criteria.getYearMonthDay("dtVigenciaFinal"));
    	}
    	
    	String vigente = criteria.getString("situacao");
    	if(vigente!=null){
    		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    		if("V".equals(vigente)){
    			sql.addCustomCriteria("TRUNC(SYSDATE) BETWEEN TRT_CLIENTE.DT_VIGENCIA_INICIAL and TRT_CLIENTE.DT_VIGENCIA_FINAL");
    		}else{
    			String dtAtual = "to_date('"+ dataAtual.toString("dd/MM/yyyy") +"','dd/mm/yyyy')";
    			sql.addCustomCriteria("("+dtAtual+ " < TRT_CLIENTE.DT_VIGENCIA_INICIAL OR "+dtAtual+" > TRT_CLIENTE.DT_VIGENCIA_FINAL)");
    		}
    		sql.addFilterSummary("situacao", "V".equals(vigente) ? "Vigente" : "Não Vigente");
    	}
    	
    	
		
		
		/** ORDER BY */
		sql.addOrderBy("REGIONAL.SG_REGIONAL");
		sql.addOrderBy("FILIAL.SG_FILIAL");
		sql.addOrderBy("PESSOA.NM_PESSOA");
		sql.addOrderBy("UNIDADE_FEDERATIVA.SG_UNIDADE_FEDERATIVA");
		sql.addOrderBy("MUNICIPIO.NM_MUNICIPIO");

		return sql;
	}
	
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("tpSituacaoAprovacao", "DM_STATUS_WORKFLOW");
	}
}
