package com.mercurio.lms.seguros.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;

/**
 * @author lalmeida
 *
 * @spring.bean id="lms.seguros.relatorioManterApolicesSeguroService"
 * @spring.property name="reportName" value="com/mercurio/lms/seguros/report/relatorioManterApolicesSeguroExcel.jasper"
 */
public class RelatorioManterApolicesSeguroService extends ReportServiceSupport {

	@SuppressWarnings("unchecked")
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
        // Recupera a query
		final String query = mountSql((TypedFlatMap)parameters);
		
        // Recupera os critérios de pesquisa
        final Map<String, String> criterios = (Map<String, String>) getParametersUtilizados(parameters);
        
        JRReportDataObject jRReportDataObject = executeQuery(query, new Object[] { });

        Map parametersReport = new HashMap();
        parametersReport.put("ID_SEGURADO", criterios.containsKey("idSegurado") ? String.valueOf(criterios.get("idSegurado")) : "");
    	parametersReport.put("NM_SEGURADO", criterios.containsKey("nmSegurado") ? criterios.get("nmSegurado") : "");
    	parametersReport.put("NUMERO_APOLICE", criterios.containsKey("nrApolice") ? criterios.get("nrApolice") : ""); 
    	parametersReport.put("ID_SEGURADORA", criterios.containsKey("idSeguradora") ? String.valueOf(criterios.get("idSeguradora")) : "");
     	parametersReport.put("ID_REGULADORA", criterios.containsKey("idReguladora") ? String.valueOf(criterios.get("idReguladora")) : "");
    	parametersReport.put("ID_TIPO_SEGURO", criterios.containsKey("idTipoSeguro") ? String.valueOf(criterios.get("idTipoSeguro")) : "");
    	parametersReport.put("VIGENCIA", criterios.containsKey("dtVigencia") ? criterios.get("dtVigencia") : "");
    	parametersReport.put("LIMITE_VALOR", criterios.containsKey("vlLimiteApolice") ? String.valueOf(criterios.get("vlLimiteApolice")) : "");
    	
    	parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        
        jRReportDataObject.setParameters(parametersReport);
        
        return jRReportDataObject;
	}
	
	private TypedFlatMap getParametersUtilizados(Map parameters) {
		TypedFlatMap criteria = new TypedFlatMap();
				
		if(MapUtils.getLong(parameters, "segurado.idPessoa") != null) {
			criteria.put("idSegurado", MapUtils.getLong(parameters, "segurado.nrPessoa"));
			criteria.put("nmSegurado", parameters.get("segurado.nmPessoa"));
		}
		
		if(!"".equals(parameters.get("nrApolice"))) {
			criteria.put("nrApolice", parameters.get("nrApolice"));
		}
		
		if(MapUtils.getLong(parameters, "seguradora.idSeguradora") != null) {
			criteria.put("idSeguradora", parameters.get("seguradora.pessoa.nmPessoa"));
		}
		
		if(MapUtils.getLong(parameters, "reguladoraSeguro.idReguladora") != null) {
			criteria.put("idReguladora", parameters.get("reguladoraSeguro.pessoa.nmPessoa"));
		}
		
		if(MapUtils.getLong(parameters, "tipoSeguro.idTipoSeguro") != null) {
			criteria.put("idTipoSeguro", parameters.get("tipoSeguro.sgTipo"));
		}
				
		if(!"".equals(parameters.get("dtVigencia"))) {
			criteria.put("dtVigencia", ((TypedFlatMap) parameters).getYearMonthDay("dtVigencia").toString("dd/MM/yyyy"));
		}
		 
		if(!"".equals(parameters.get("vlLimiteApolice"))) {
			criteria.put("vlLimiteApolice", FormatUtils.formatBigDecimalWithPattern(new BigDecimal((String)parameters.get("vlLimiteApolice")), "#,###,###,##0.00"));
		}
		
		return criteria;
	}
	
	private String mountSql(TypedFlatMap parameters) {
		
		StringBuilder sql = new StringBuilder()
		.append("SELECT pessoa_segurado.nr_identificacao                     AS nr_identificacao, ")
		.append("  PESSOA_SEGURADO.NM_PESSOA                                 AS NM_PESSOA_SEGURADO, ")
		.append("  apolice_seguro.nr_apolice                                 AS nr_apolice, ")
		.append("  PESSOA_SEGURADORA.nm_pessoa                               AS nm_pessoa_SEGURADORA, ")
		.append("  pessoa_reguladora.nm_pessoa                               AS nm_pessoa_reguladora, ")
		.append("  TO_CHAR(apolice_seguro.dt_vigencia_inicial, 'DD/MM/YYYY') AS dt_vigencia_inicial, ")
		.append("  TO_CHAR(apolice_seguro.dt_vigencia_final, 'DD/MM/YYYY')   AS dt_vigencia_final, ")
		.append("  tipo_seguro.sg_tipo                                       AS sg_tipo, ")
		.append("  vi18n(tipo_seguro.ds_tipo_i)                              AS ds_tipo, ")
		.append("  moeda.sg_moeda || ' ' || moeda.ds_simbolo                 AS ds_simbolo, ")
		.append("  apolice_seguro.vl_limite_apolice                          AS vl_limite_apolice, ")
		.append("  apolice_seguro.vl_franquia                                AS vl_franquia, ")
		.append("  (SELECT SUM(APS.VL_PARCELA) ")
		.append("  FROM APOLICE_SEGURO_PARCELA APS ")
		.append("  WHERE aps.id_apolice_seguro = apolice_seguro.id_apolice_seguro ")
		.append("  ) AS valor_premio, ")
		.append("  (SELECT SUM(APS.VL_PARCELA) ")
		.append("  FROM APOLICE_SEGURO_PARCELA APS ")
		.append("  WHERE aps.dt_vencimento   > SYSDATE ")
		.append("  AND aps.id_apolice_seguro = apolice_seguro.id_apolice_seguro ")
		.append("  )                           AS valor_premio_a_vencer, ")
		.append("  apolice_seguro.ds_cobertura AS ds_cobertura, ")
		.append("  apolice_seguro.ds_limite    AS ds_limite, ")
		.append("  apolice_seguro.ds_franquia  AS ds_franquia ")
		.append("FROM APOLICE_SEGURO, ")
		.append("  TIPO_SEGURO, ")
		.append("  REGULADORA_SEGURO, ")
		.append("  PESSOA PESSOA_REGULADORA, ")
		.append("  SEGURADORA, ")
		.append("  PESSOA PESSOA_SEGURADORA, ")
		.append("  MOEDA, ")
		.append("  PESSOA PESSOA_SEGURADO ")
		.append("WHERE APOLICE_SEGURO.ID_TIPO_SEGURO =TIPO_SEGURO.ID_TIPO_SEGURO(+) ")
		.append("AND APOLICE_SEGURO.ID_REGULADORA    =REGULADORA_SEGURO.ID_REGULADORA(+) ")
		.append("AND REGULADORA_SEGURO.ID_REGULADORA = PESSOA_REGULADORA.ID_PESSOA(+) ")
		.append("AND APOLICE_SEGURO.ID_SEGURADORA    =SEGURADORA.ID_SEGURADORA(+) ")
		.append("AND SEGURADORA.ID_SEGURADORA        =PESSOA_SEGURADORA.ID_PESSOA(+) ")
		.append("AND APOLICE_SEGURO.ID_MOEDA         =MOEDA.ID_MOEDA(+) ")
		.append("AND APOLICE_SEGURO.ID_SEGURADO      =PESSOA_SEGURADO.ID_PESSOA ");
		
		if(parameters.getYearMonthDay("dtVigencia") != null) {
			final String dtVigencia = parameters.getYearMonthDay("dtVigencia").toString("dd/MM/yyyy");
			sql.append(" AND ")
					.append("TO_DATE('")
					.append(dtVigencia)
					.append("', 'DD/MM/YYYY') BETWEEN dt_vigencia_inicial AND dt_vigencia_final ");
		}
		
		if(parameters.getLong("segurado.idPessoa") != null) {
			sql.append(" AND APOLICE_SEGURO.ID_SEGURADO = ").append(parameters.getLong("segurado.idPessoa"));
		}
		
		if(!"".equals(parameters.getString("nrApolice"))) {
			sql.append(" AND APOLICE_SEGURO.NR_APOLICE LIKE '").append(parameters.getString("nrApolice")).append("%'");
		}
		
		if(parameters.getLong("reguladoraSeguro.idReguladora") != null) {
			sql.append(" AND APOLICE_SEGURO.ID_REGULADORA = ").append(parameters.getLong("reguladoraSeguro.idReguladora"));
		}
		
		if(parameters.getLong("seguradora.idSeguradora") != null) {
			sql.append(" AND APOLICE_SEGURO.ID_SEGURADORA = ").append(parameters.getLong("seguradora.idSeguradora"));
		}
		
		if(parameters.getLong("tipoSeguro.idTipoSeguro") != null) {
			sql.append(" AND APOLICE_SEGURO.ID_TIPO_SEGURO = ").append(parameters.getLong("tipoSeguro.idTipoSeguro"));
		}
		
		if(parameters.getLong("moeda.idMoeda") != null) {
			sql.append(" AND APOLICE_SEGURO.ID_MOEDA = ").append(parameters.getLong("moeda.idMoeda"));
		}
		
		if(parameters.getBigDecimal("vlLimiteApolice") != null) {
			sql.append(" AND APOLICE_SEGURO.VL_LIMITE_APOLICE = ").append(parameters.getBigDecimal("vlLimiteApolice"));
		}
		
		sql.append(" ORDER BY PESSOA_SEGURADO.NM_PESSOA ASC, ")
		.append("APOLICE_SEGURO.NR_APOLICE ASC");
		
		return sql.toString();
	}
}
