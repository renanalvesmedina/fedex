package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.emitirPipelineClienteService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirRelacaoPipelineCliente.jasper"
 */											
public class EmitirPipelineClienteService extends ReportServiceSupport {
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = this.findPipelieQuery((TypedFlatMap) parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		String tpFormatoRelatorio = ((TypedFlatMap) parameters).getString("tpFormatoRelatorio.valor");
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tpFormatoRelatorio);
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate findPipelieQuery(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		if (parameters.getBoolean("blFaturamento")){
			this.configurePipelineFaturamentoReport(sql, parameters);
		} else if (parameters.getBoolean("blResumidoCategoria")){
			this.configurePipelineCategoriaReport(sql, parameters);
		} else if (parameters.getBoolean("blResumidoEtapa")){
			this.configurePipelineEtapaReport(sql, parameters);
		} else {
			this.configurePipelineReport(sql, parameters);
		}
		return sql;
	}
		
	/**
	 * Relatório Pipiline padrão
	 * 
	 * @author André Valadas
	 * @param sql
	 * @param parameters
	 * @throws Exception
	 */
	private void configurePipelineReport(SqlTemplate sql, TypedFlatMap parameters) throws Exception {
		sql.addProjection("PC.ID_PIPELINE_CLIENTE", "ID_PIPELINE_CLIENTE");
		sql.addProjection("NVL(P_C.NM_PESSOA, PC.NM_CLIENTE) ", "NM_PESSOA_CLIENTE");
		
		sql.addProjection("P_C.NR_IDENTIFICACAO ", "NR_IDENTIFICACAO");
		sql.addProjection("P_C.TP_IDENTIFICACAO ", "TP_IDENTIFICACAO");
		sql.addProjection("PC.TP_NEGOCIACAO", "TP_NEGOCIACAO");
		sql.addProjection("PE.DT_EVENTO", "DT_EVENTO"); 
		sql.addProjection("PC.NR_MES_FECHAMENTO", "NR_MES_FECHAMENTO");
		sql.addProjection("PC.NR_ANO_FECHAMENTO", "NR_ANO_FECHAMENTO");
		
		sql.addProjection("U.NM_USUARIO", "NM_PESSOA_VENDEDOR");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("SM.DS_SEGMENTO_MERCADO_I"), "DS_SEGMENTO_MERCADO");
		sql.addProjection("PE.TP_PIPELINE_ETAPA ", "TP_PIPELINE_ETAPA");
		sql.addProjection("PC.PC_PROBABILIDADE", "PC_PROBABILIDADE");
		sql.addProjection("PC.TP_PROBABILIDADE", "TP_PROBABILIDADE");
		sql.addProjection("PC.VL_RECEITA_PREVISTA", "VL_RECEITA_PREVISTA");
		sql.addProjection("PC.TP_SITUACAO", "TP_SITUACAO");
		sql.addProjection("PC.DT_PERDA", "DT_PERDA");
		sql.addProjection("PC.TP_MOTIVO_PERDA", "TP_MOTIVO_PERDA");
		
		sql.addProjection("'false'", "isCategoriaGroup");
		sql.addProjection("'false'", "isEtapaGroup");
		sql.addProjection("NULL", "CD_PIPELINE_ETAPA");
		sql.addProjection("NULL", "VL_TOTAL_PREVISTO");
		sql.addProjection("NULL", "NR_QUANTIDADE_TOTAL");
		sql.addProjection("NULL", "QTD_PERDAS");
		sql.addProjection("NULL", "VL_PERDAS");
		
		this.setDefaultProjections(sql);
		this.setDefaultJoins(sql, parameters);
		
		//ABRANGENCIA E MODAL
		String tpAbrangencia = parameters.getString("tpAbrangencia");
		String tpModal = parameters.getString("tpModal");
		String dsAbrangencia = parameters.getString("dsAbrangencia");
		String dsModal = parameters.getString("dsModal");
		
		if(StringUtils.isNotBlank(tpAbrangencia) || StringUtils.isNotBlank(tpModal)) {
			sql.addProjection("(SELECT MAX(PR.ID_PIPELINE_RECEITA) FROM PIPELINE_RECEITA PR WHERE PR.ID_PIPELINE_CLIENTE=PC.ID_PIPELINE_CLIENTE)", "ID_PIPELINE_RECEITA");
			sql.addProjection("'TRUE'", "BL_POTENCIAL_RECEITA");
			sql.addProjection("PR1.VL_RECEITA", "VL_RECEITA1");
			sql.addProjection("PR1.TP_ABRANGENCIA", "TP_ABRANGENCIA1");
			sql.addProjection("PR1.TP_MODAL", "TP_MODAL1");
			sql.addFrom("PIPELINE_RECEITA", "PR1");
			sql.addJoin("PR1.ID_PIPELINE_CLIENTE(+)", "PC.ID_PIPELINE_CLIENTE");
			
			sql.addProjection("NULL", "VL_RECEITA3");
			sql.addProjection("NULL", "TP_ABRANGENCIA3");
			sql.addProjection("NULL", "TP_MODAL3");
			sql.addProjection("NULL", "VL_RECEITA4");
			sql.addProjection("NULL", "TP_ABRANGENCIA4");
			sql.addProjection("NULL", "TP_MODAL4");
			
			if(StringUtils.isNotBlank(dsAbrangencia)){
				sql.addFilterSummary("abrangencia", dsAbrangencia);
			}
			if(StringUtils.isNotBlank(dsModal)){
				sql.addFilterSummary("modal", dsModal);
			}
			
			if(StringUtils.isNotBlank(tpAbrangencia) && StringUtils.isNotBlank(tpModal)) {
				sql.addProjection("NULL", "VL_RECEITA2");
				sql.addProjection("NULL", "TP_ABRANGENCIA2");
				sql.addProjection("NULL", "TP_MODAL2");
				
				sql.addCriteria("PR1.TP_ABRANGENCIA(+)", "=", tpAbrangencia);
				sql.addCriteria("PR1.TP_MODAL(+)", "=", tpModal);
			}else{
				sql.addProjection("PR2.VL_RECEITA", "VL_RECEITA2");
				sql.addProjection("PR2.TP_ABRANGENCIA", "TP_ABRANGENCIA2");
				sql.addProjection("PR2.TP_MODAL", "TP_MODAL2");
				sql.addFrom("PIPELINE_RECEITA", "PR2");
				sql.addJoin("PR2.ID_PIPELINE_CLIENTE(+)", "PC.ID_PIPELINE_CLIENTE");
				
				if(StringUtils.isNotBlank(tpAbrangencia)) {
					sql.addCriteria("PR1.TP_ABRANGENCIA(+)", "=", tpAbrangencia);
					sql.addCriteria("PR2.TP_ABRANGENCIA(+)", "=", tpAbrangencia);
					sql.addCriteria(" PR1.TP_MODAL (+)", "=", "R");
					sql.addCriteria(" PR2.TP_MODAL (+)", "=", "A");
					
					
				}else if(StringUtils.isNotBlank(tpModal)) {
					sql.addCriteria("PR1.TP_MODAL(+)", "=", tpModal);
					sql.addCriteria("PR2.TP_MODAL(+)", "=", tpModal);
					sql.addCriteria("PR1.TP_ABRANGENCIA(+)", "=", "I");
					sql.addCriteria("PR2.TP_ABRANGENCIA(+)", "=", "N");
				}
			}
		} else {
			this.setEmptyProjections(sql);
		}
			
		sql.addOrderBy("R.SG_REGIONAL");
		sql.addOrderBy("F.SG_FILIAL");
		sql.addOrderBy("P_C.NM_PESSOA");
	}

	/**
	 * Relatório Pipeline por Faturamento
	 * 
	 * @author André Valadas
	 * @param sql
	 * @param parameters
	 * @throws Exception
	 */
	private void configurePipelineFaturamentoReport(SqlTemplate sql, TypedFlatMap parameters) throws Exception {
		sql.addProjection("PC.ID_PIPELINE_CLIENTE", "ID_PIPELINE_CLIENTE");
		sql.addProjection("NVL(P_C.NM_PESSOA, PC.NM_CLIENTE) ", "NM_PESSOA_CLIENTE");
		
		sql.addProjection("P_C.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
		sql.addProjection("P_C.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sql.addProjection("PC.TP_NEGOCIACAO", "TP_NEGOCIACAO");
		sql.addProjection("PE.DT_EVENTO", "DT_EVENTO"); 
		sql.addProjection("PC.NR_MES_FECHAMENTO", "NR_MES_FECHAMENTO");
		sql.addProjection("PC.NR_ANO_FECHAMENTO", "NR_ANO_FECHAMENTO");
		
		sql.addProjection("U.NM_USUARIO", "NM_PESSOA_VENDEDOR");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("SM.DS_SEGMENTO_MERCADO_I"), "DS_SEGMENTO_MERCADO");
		sql.addProjection("PE.TP_PIPELINE_ETAPA ", "TP_PIPELINE_ETAPA");
		sql.addProjection("PC.PC_PROBABILIDADE", "PC_PROBABILIDADE");
		sql.addProjection("PC.VL_RECEITA_PREVISTA", "VL_RECEITA_PREVISTA");
		sql.addProjection("PC.TP_SITUACAO", "TP_SITUACAO");
		sql.addProjection("PC.TP_NEGOCIACAO", "TP_NEGOCIACAO");
		sql.addProjection("PC.DT_PERDA", "DT_PERDA");
		sql.addProjection("PC.TP_MOTIVO_PERDA", "TP_MOTIVO_PERDA");

		sql.addProjection("'false'", "isCategoriaGroup");
		sql.addProjection("'false'", "isEtapaGroup");
		sql.addProjection("NULL", "CD_PIPELINE_ETAPA");
		sql.addProjection("NULL", "VL_TOTAL_PREVISTO");
		sql.addProjection("NULL", "NR_QUANTIDADE_TOTAL");
		sql.addProjection("NULL", "QTD_PERDAS");
		sql.addProjection("NULL", "VL_PERDAS");

		this.setDefaultProjections(sql);
		this.setDefaultJoins(sql, parameters);

			sql.addFilterSummary("faturamentoModalAbrangencia", "Sim");
			sql.addProjection("(SELECT MAX(PR.ID_PIPELINE_RECEITA) FROM PIPELINE_RECEITA PR WHERE PR.ID_PIPELINE_CLIENTE=PC.ID_PIPELINE_CLIENTE)", "ID_PIPELINE_RECEITA");
			sql.addProjection("'TRUE'", "BL_POTENCIAL_RECEITA");
			sql.addProjection("PR1.VL_RECEITA", "VL_RECEITA1");
			sql.addProjection("PR1.TP_ABRANGENCIA", "TP_ABRANGENCIA1");
			sql.addProjection("PR1.TP_MODAL", "TP_MODAL1");
			
			sql.addProjection("PR2.VL_RECEITA", "VL_RECEITA2");
			sql.addProjection("PR2.TP_ABRANGENCIA", "TP_ABRANGENCIA2");
			sql.addProjection("PR2.TP_MODAL", "TP_MODAL2");
			
			sql.addProjection("PR3.VL_RECEITA", "VL_RECEITA3");
			sql.addProjection("PR3.TP_ABRANGENCIA", "TP_ABRANGENCIA3");
			sql.addProjection("PR3.TP_MODAL", "TP_MODAL3");
			
			sql.addProjection("PR4.VL_RECEITA", "VL_RECEITA4");
			sql.addProjection("PR4.TP_ABRANGENCIA", "TP_ABRANGENCIA4");
			sql.addProjection("PR4.TP_MODAL", "TP_MODAL4");
			
			sql.addFrom("PIPELINE_RECEITA", "PR1");
			sql.addFrom("PIPELINE_RECEITA", "PR2");
			sql.addFrom("PIPELINE_RECEITA", "PR3");
			sql.addFrom("PIPELINE_RECEITA", "PR4");
			
			sql.addJoin("PR1.ID_PIPELINE_CLIENTE(+)", "PC.ID_PIPELINE_CLIENTE");
			sql.addJoin("PR2.ID_PIPELINE_CLIENTE(+)", "PC.ID_PIPELINE_CLIENTE");
			sql.addJoin("PR3.ID_PIPELINE_CLIENTE(+)", "PC.ID_PIPELINE_CLIENTE");
			sql.addJoin("PR4.ID_PIPELINE_CLIENTE(+)", "PC.ID_PIPELINE_CLIENTE");
			
			sql.addCriteria(" PR1.TP_ABRANGENCIA (+)", "=", "N");
			sql.addCriteria(" PR2.TP_ABRANGENCIA (+)", "=", "N");
			sql.addCriteria(" PR3.TP_ABRANGENCIA (+)", "=", "I");
			sql.addCriteria(" PR4.TP_ABRANGENCIA (+)", "=", "I");
			sql.addCriteria(" PR1.TP_MODAL (+)", "=", "R");
			sql.addCriteria(" PR2.TP_MODAL (+)", "=", "A");
			sql.addCriteria(" PR3.TP_MODAL (+)", "=", "R");
			sql.addCriteria(" PR4.TP_MODAL (+)", "=", "A");

		sql.addOrderBy("R.SG_REGIONAL");
		sql.addOrderBy("F.SG_FILIAL");
		sql.addOrderBy("P_C.NM_PESSOA");
	}

	/**
	 * Relatório Resumido por Categoria
	 * 
	 * @author André Valadas
	 * @param sql
	 * @param parameters
	 * @throws Exception
	 */
	private void configurePipelineCategoriaReport(SqlTemplate sql, TypedFlatMap parameters) throws Exception {
		sql.addFilterSummary("resumidoPorCategoria", "Sim");
		StringBuilder caseQuery = new StringBuilder();
		caseQuery.append("(case PE.TP_PIPELINE_ETAPA");
		caseQuery.append("	when '05' then '01'");
		caseQuery.append("	when '10' then '01'");
		caseQuery.append("	when '15' then '02'");
		caseQuery.append("	when '20' then '02'");
		caseQuery.append("	when '25' then '02'");
		caseQuery.append("	when '30' then '02'");
		caseQuery.append("	when '35' then '03'");
		caseQuery.append("	when '40' then '03'");
		caseQuery.append("	when '45' then '03'");
		caseQuery.append("	when '50' then '03'");
		caseQuery.append("	when '55' then '03'");
		caseQuery.append("	else '04'");
		caseQuery.append(" end)");
		sql.addProjection(caseQuery.toString(), "CD_PIPELINE_ETAPA");
		sql.addProjection("NULL", "DT_PERDA");
		sql.addProjection("NULL", "TP_PIPELINE_ETAPA");
		sql.addProjection("'true'", "isCategoriaGroup");
		sql.addProjection("'false'", "isEtapaGroup");

		this.setDefaultCategoriaEtapaReport(sql, parameters);

		/** ORDER BY */
		sql.addOrderBy("R.SG_REGIONAL");
		sql.addOrderBy("F.SG_FILIAL");
		sql.addOrderBy("PE.TP_PIPELINE_ETAPA");
	}

	/**
	 * Relatório Resumido por Etapa
	 * 
	 * @author André Valadas
	 * @param sql
	 * @param parameters
	 * @throws Exception
	 */
	private void configurePipelineEtapaReport(SqlTemplate sql, TypedFlatMap parameters) throws Exception {
		sql.addFilterSummary("resumidoPorEtapa", "Sim");
		sql.addProjection("PE.TP_PIPELINE_ETAPA", "TP_PIPELINE_ETAPA");
		sql.addProjection("NULL", "DT_PERDA");
		sql.addProjection("NULL", "CD_PIPELINE_ETAPA");
		sql.addProjection("'false'", "isCategoriaGroup");
		sql.addProjection("'true'", "isEtapaGroup");

		this.setDefaultCategoriaEtapaReport(sql, parameters);

		sql.addOrderBy("R.SG_REGIONAL");
		sql.addOrderBy("F.SG_FILIAL");
		sql.addOrderBy("PE.TP_PIPELINE_ETAPA");
	}

	/**
	 * Relatório Padrão para os Resumos de Categoria e Etapa
	 * 
	 * @author André Valadas
	 * @param sql
	 * @param parameters
	 * @throws Exception
	 */
	private void setDefaultCategoriaEtapaReport(SqlTemplate sql, TypedFlatMap parameters) throws Exception {
		sql.addProjection("SUM(PC.VL_RECEITA_PREVISTA)", "VL_TOTAL_PREVISTO");
		sql.addProjection("COUNT(PE.ID_PIPELINE_CLIENTE)", "NR_QUANTIDADE_TOTAL");
				
		sql.addProjection(sqlQuantidadePerdas(parameters), "QTD_PERDAS");
		sql.addProjection(sqlValorTotalPerdas(parameters), "VL_PERDAS");
		
		sql.addProjection("NULL", "ID_PIPELINE_CLIENTE");
		sql.addProjection("NULL", "NM_PESSOA_CLIENTE");
		
		sql.addProjection("NULL", "NR_IDENTIFICACAO");
		sql.addProjection("NULL", "TP_IDENTIFICACAO");
		sql.addProjection("NULL", "TP_NEGOCIACAO");
		sql.addProjection("NULL", "DT_EVENTO"); 
		sql.addProjection("NULL", "NR_MES_FECHAMENTO");
		sql.addProjection("NULL", "NR_ANO_FECHAMENTO");
		
		sql.addProjection("NULL", "NM_PESSOA_VENDEDOR");
		sql.addProjection("NULL", "DS_SEGMENTO_MERCADO");
		sql.addProjection("NULL", "PC_PROBABILIDADE");
		sql.addProjection("NULL", "VL_RECEITA_PREVISTA");
		sql.addProjection("NULL", "TP_SITUACAO");
		sql.addProjection("NULL", "TP_NEGOCIACAO");
		sql.addProjection("NULL", "TP_MOTIVO_PERDA");

		this.setDefaultProjections(sql);	
		this.setEmptyProjections(sql);
		this.setDefaultJoins(sql, parameters);
		
		/** GROUP BY */
		sql.addGroupBy("PE.TP_PIPELINE_ETAPA");
		sql.addGroupBy("MO.DS_SIMBOLO");
		sql.addGroupBy("MO.SG_MOEDA");
		sql.addGroupBy("R.ID_REGIONAL");
		sql.addGroupBy("R.SG_REGIONAL");
		sql.addGroupBy("R.DS_REGIONAL");
		sql.addGroupBy("F.ID_FILIAL");
		sql.addGroupBy("F.SG_FILIAL");
		sql.addGroupBy("P_F.NM_FANTASIA");
	}
	/**
	 * Método chamado durante a execução do relatório Jasper.
	 * @param tpPipelineEtapa
	 * @return
	 */
	public static String convertTpPipelineEtapaToCategoria(String tpPipelineEtapa, String prospeccao, String negociacao, String fechamento, String perdaNegocio, String defaultResult) {
		if("01".equals(tpPipelineEtapa)) {
			return prospeccao;
		} else if("02".equals(tpPipelineEtapa)) {
			return negociacao;
		} else if("03".equals(tpPipelineEtapa)) {
			return fechamento;
		} else if("04".equals(tpPipelineEtapa)) {
			return perdaNegocio;
		}
		return defaultResult;
	}
	
	public String sqlQuantidadePerdas(TypedFlatMap parameters) {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		YearMonthDay dataInicio = parameters.getYearMonthDay("dtInicio");
		YearMonthDay dataFim = parameters.getYearMonthDay("dtFim");
		Long idRegional = parameters.getLong("regional.idRegional");
		Long idFilial = parameters.getLong("filial.idFilial");
		
		String sql = "( SELECT count(1) "+
				"	FROM FILIAL F2, "+
				"	PESSOA P_C2, "+
				"   PESSOA P_F2, "+
				"   CLIENTE C2, "+
				"   PIPELINE_CLIENTE PC2, "+
				"   PIPELINE_ETAPA PE2, "+
				"   REGIONAL_FILIAL RF2, "+
				"   REGIONAL R2 "+
				" WHERE C2.ID_CLIENTE(+) = PC2.ID_CLIENTE "+
				" 	AND P_C2.ID_PESSOA(+) = C2.ID_CLIENTE "+
				"   AND RF2.ID_FILIAL = F2.ID_FILIAL "+
			    "   AND F2.ID_FILIAL = PC2.ID_FILIAL "+
				"	AND F2.ID_FILIAL = P_F2.ID_PESSOA "+
				"	AND RF2.ID_REGIONAL = R2.ID_REGIONAL "+
				"	AND PC2.ID_PIPELINE_CLIENTE = PE2.ID_PIPELINE_CLIENTE(+) "+
				"	AND PE2.TP_PIPELINE_ETAPA IS NOT NULL "+ 
				"	AND PC2.DT_PERDA IS NOT NULL "+
				"	AND ((PE2.TP_PIPELINE_ETAPA=(SELECT Max(pec.tp_pipeline_etapa) FROM pipeline_etapa pec WHERE pc2.id_pipeline_cliente = pec.id_pipeline_cliente)) OR (PE2.TP_PIPELINE_ETAPA IS NULL)) ";
				
				if(idFilial != null){
					sql = sql.concat(" AND PC2.ID_FILIAL = ").concat(idFilial.toString());
				}
				
				if(idRegional != null){
					sql = sql.concat(" AND R2.ID_REGIONAL = ").concat(idRegional.toString());
				}
				
				sql = sql.concat("	AND RF2.DT_VIGENCIA_INICIAL <= to_date('"+JTDateTimeUtils.formatDateYearMonthDayToString(dataAtual)+"','dd/mm/yyyy') ");
				sql = sql.concat("	AND RF2.DT_VIGENCIA_FINAL >= to_date('"+JTDateTimeUtils.formatDateYearMonthDayToString(dataAtual)+"','dd/mm/yyyy') ");
				
				if(dataInicio != null){
					sql = sql.concat("	AND PE2.DT_EVENTO(+) >= to_date('"+JTDateTimeUtils.formatDateYearMonthDayToString(dataInicio)+"','dd/mm/yyyy') ");			
				}
				
				if(dataFim != null){
					sql = sql.concat("	AND PE2.DT_EVENTO(+) <= to_date('"+JTDateTimeUtils.formatDateYearMonthDayToString(dataFim)+"','dd/mm/yyyy')) ");
				}
		
		;
		return sql;
	}
	
	public String sqlValorTotalPerdas(TypedFlatMap parameters) {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		YearMonthDay dataInicio = parameters.getYearMonthDay("dtInicio");
		YearMonthDay dataFim = parameters.getYearMonthDay("dtFim");
		Long idRegional = parameters.getLong("regional.idRegional");
		Long idFilial = parameters.getLong("filial.idFilial");
		
		String sql = "( SELECT sum(PC2.VL_RECEITA_PREVISTA) "+
				"	FROM FILIAL F2, "+
				"	PESSOA P_C2, "+
				"   PESSOA P_F2, "+
				"   CLIENTE C2, "+
				"   PIPELINE_CLIENTE PC2, "+
				"   PIPELINE_ETAPA PE2, "+
				"   REGIONAL_FILIAL RF2, "+
				"   REGIONAL R2 "+
				" WHERE C2.ID_CLIENTE(+) = PC2.ID_CLIENTE "+
				" 	AND P_C2.ID_PESSOA(+) = C2.ID_CLIENTE "+
				"   AND RF2.ID_FILIAL = F2.ID_FILIAL "+
			    "   AND F2.ID_FILIAL = PC2.ID_FILIAL "+
				"	AND F2.ID_FILIAL = P_F2.ID_PESSOA "+
				"	AND RF2.ID_REGIONAL = R2.ID_REGIONAL "+
				"	AND PC2.ID_PIPELINE_CLIENTE = PE2.ID_PIPELINE_CLIENTE(+) "+
				"	AND PE2.TP_PIPELINE_ETAPA IS NOT NULL "+ 
				"	AND PC2.DT_PERDA IS NOT NULL "+
				"	AND ((PE2.TP_PIPELINE_ETAPA=(SELECT Max(pec.tp_pipeline_etapa) FROM pipeline_etapa pec WHERE pc2.id_pipeline_cliente = pec.id_pipeline_cliente)) OR (PE2.TP_PIPELINE_ETAPA IS NULL)) ";
				
				if(idFilial != null){
					sql = sql.concat(" AND PC2.ID_FILIAL = ").concat(idFilial.toString());
				}
				
				if(idRegional != null){
					sql = sql.concat(" AND R2.ID_REGIONAL = ").concat(idRegional.toString());
				}
				
				sql = sql.concat("	AND RF2.DT_VIGENCIA_INICIAL <= to_date('"+JTDateTimeUtils.formatDateYearMonthDayToString(dataAtual)+"','dd/mm/yyyy') ");
				sql = sql.concat("	AND RF2.DT_VIGENCIA_FINAL >= to_date('"+JTDateTimeUtils.formatDateYearMonthDayToString(dataAtual)+"','dd/mm/yyyy') ");
				
				if(dataInicio != null){
					sql = sql.concat("	AND PE2.DT_EVENTO(+) >= to_date('"+JTDateTimeUtils.formatDateYearMonthDayToString(dataInicio)+"','dd/mm/yyyy') ");			
				}
				
				if(dataFim != null){
					sql = sql.concat("	AND PE2.DT_EVENTO(+) <= to_date('"+JTDateTimeUtils.formatDateYearMonthDayToString(dataFim)+"','dd/mm/yyyy')) ");
				}
		
		;
		return sql;
	}

	private void setDefaultProjections(SqlTemplate sql) {
		sql.addProjection("MO.DS_SIMBOLO", "DS_SIMBOLO");
		sql.addProjection("MO.SG_MOEDA", "SG_MOEDA");
		sql.addProjection("R.SG_REGIONAL||' - '||R.DS_REGIONAL", "SG_REGIONAL");
		sql.addProjection("F.SG_FILIAL||' - '||P_F.NM_FANTASIA", "SG_FILIAL");
		sql.addProjection("R.ID_REGIONAL", "ID_REGIONAL");
		sql.addProjection("F.ID_FILIAL", "ID_FILIAL");
	}

	private void setEmptyProjections(SqlTemplate sql) {
			sql.addProjection("NULL", "BL_POTENCIAL_RECEITA");
			sql.addProjection("NULL", "ID_PIPELINE_RECEITA");
			sql.addProjection("NULL", "VL_RECEITA1");
			sql.addProjection("NULL", "TP_ABRANGENCIA1");
			sql.addProjection("NULL", "TP_MODAL1");
			sql.addProjection("NULL", "VL_RECEITA2");
			sql.addProjection("NULL", "TP_ABRANGENCIA2");
			sql.addProjection("NULL", "TP_MODAL2");
			sql.addProjection("NULL", "VL_RECEITA3");
			sql.addProjection("NULL", "TP_ABRANGENCIA3");
			sql.addProjection("NULL", "TP_MODAL3");
			sql.addProjection("NULL", "VL_RECEITA4");
			sql.addProjection("NULL", "TP_ABRANGENCIA4");
			sql.addProjection("NULL", "TP_MODAL4");
		}
				
	private void setDefaultJoins(SqlTemplate sql, TypedFlatMap parameters) {
		sql.addFrom("FILIAL", "F");
		sql.addFrom("PESSOA", "P_C");
		sql.addFrom("USUARIO", "U");
		sql.addFrom("PESSOA", "P_F");
		sql.addFrom("CLIENTE", "C");
		sql.addFrom("MOEDA", "MO");
		sql.addFrom("PIPELINE_CLIENTE", "PC");
		sql.addFrom("PIPELINE_ETAPA", "PE");
		
		sql.addFrom("SEGMENTO_MERCADO", "SM");
		sql.addFrom("REGIONAL_FILIAL", "RF");
		sql.addFrom("REGIONAL", "R");
		
		sql.addJoin("C.ID_CLIENTE(+)", "PC.ID_CLIENTE");
		sql.addJoin("P_C.ID_PESSOA(+)", "C.ID_CLIENTE");
		sql.addJoin("U.ID_USUARIO ", "PC.ID_USUARIO");
		
		sql.addJoin("MO.ID_MOEDA", "PC.ID_MOEDA");
		
		sql.addJoin("RF.ID_FILIAL" , "F.ID_FILIAL");
		sql.addJoin("F.ID_FILIAL", "PC.ID_FILIAL");
		sql.addJoin("F.ID_FILIAL", "P_F.ID_PESSOA");
		sql.addJoin("RF.ID_REGIONAL" , "R.ID_REGIONAL");
				
		sql.addJoin("SM.ID_SEGMENTO_MERCADO", "PC.ID_SEGMENTO_MERCADO");
		sql.addJoin("PC.ID_PIPELINE_CLIENTE", "PE.ID_PIPELINE_CLIENTE(+)");

		/**
		 * Apenas Pipeline existentes
		 * Regra inclusa por Eri
		 * @since 22/06/2009
		 */
		sql.addCustomCriteria("PE.TP_PIPELINE_ETAPA IS NOT NULL");
		
		if (parameters.getBoolean("blResumidoCategoria") || parameters.getBoolean("blResumidoEtapa")){
			sql.addCustomCriteria("PC.DT_PERDA IS NULL");
		} 
		
		//REGIONAL
		Long idRegional = parameters.getLong("regional.idRegional");
		Long idFilial = parameters.getLong("filial.idFilial");
			
		if(idRegional!= null){
			sql.addCriteria("R.ID_REGIONAL", "=", idRegional);
			String regional = parameters.getString("regional.siglaDescricao") ;
			sql.addFilterSummary("regional", regional);
		}
		
		//FILIAL
		if(idFilial != null) {
			sql.addCriteria("PC.ID_FILIAL", "=", idFilial);
			String dsFilial = parameters.getString("sgFilial") + " - " + parameters.getString("filial.pessoa.nmFantasia");
			sql.addFilterSummary("filial", dsFilial);
		}
		
		//USUARIO
		Long idUsuario = parameters.getLong("usuarioByIdUsuario.idUsuario");
		if(idUsuario!= null){
			sql.addCriteria("PC.ID_USUARIO", "=", idUsuario);
			String vendedor = parameters.getString("usuarioByIdUsuario.nmUsuario") ;
			sql.addFilterSummary("vendedor", vendedor);
		}
		
		//RAMO ATUAÇÃO
		Long idSegmentoMercado = parameters.getLong("segmentoMercado.idSegmentoMercado");
		if(idSegmentoMercado!= null){
			sql.addCriteria("PC.ID_SEGMENTO_MERCADO", "=", idSegmentoMercado);
			String dsSegmento = parameters.getString("dsSegmento") ;
			sql.addFilterSummary("segmentoMercado", dsSegmento);
		}
		
		//SITUACAO
		String tpSituacao = parameters.getString("tpSituacao");
		if(StringUtils.isNotBlank(tpSituacao)) {
			sql.addCriteria("PC.TP_SITUACAO", "=", tpSituacao);
			String dsSituacao = parameters.getString("dsSituacao");
			sql.addFilterSummary("situacao", dsSituacao);
		}
		
		/** WHERE **/
		//ETAPA		
		String tpPipelineEtapa = parameters.getString("tpEtapa");
		if(StringUtils.isNotBlank(tpPipelineEtapa)) {
			sql.addCriteria("PE.TP_PIPELINE_ETAPA", "=", tpPipelineEtapa);
			String dsEtapa = parameters.getString("dsEtapa");
			sql.addFilterSummary("etapa", dsEtapa);
		}else{
			sql.addCustomCriteria("((PE.TP_PIPELINE_ETAPA=(SELECT Max(pec.tp_pipeline_etapa) FROM pipeline_etapa pec WHERE pc.id_pipeline_cliente = pec.id_pipeline_cliente)) OR (PE.TP_PIPELINE_ETAPA IS NULL))");
		}
		
		//DATA ATUAL
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		sql.addCriteria("RF.DT_VIGENCIA_INICIAL", "<=", dataAtual, YearMonthDay.class);
		sql.addCriteria("RF.DT_VIGENCIA_FINAL", ">=", dataAtual, YearMonthDay.class);

		YearMonthDay dataInicio = parameters.getYearMonthDay("dtInicio");
		if(dataInicio!= null){
			sql.addCriteria("PE.DT_EVENTO(+)", ">=", dataInicio, YearMonthDay.class);
			sql.addFilterSummary("dataInicio", dataInicio);
		}
		
		YearMonthDay dataFim = parameters.getYearMonthDay("dtFim");
		if(dataFim!= null){
			sql.addCriteria("PE.DT_EVENTO(+)", "<=", dataFim, YearMonthDay.class);
			sql.addFilterSummary("dataFim", dataFim);
		}
	}
	
		/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.report.ReportServiceSupport#configReportDomains(com.mercurio.adsm.framework.report.ReportServiceSupport.ReportDomainConfig)
	 */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_PIPELINE_ETAPA", "DM_ETAPA_PIPELINE");
		config.configDomainField("TP_MOTIVO_PERDA", "DM_MOTIVO_PERDA");
		
		config.configDomainField("TP_MODAL1", "DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA1", "DM_ABRANGENCIA");
		
		config.configDomainField("TP_MODAL2", "DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA2", "DM_ABRANGENCIA");
		
		config.configDomainField("TP_MODAL3", "DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA3", "DM_ABRANGENCIA");
		
		config.configDomainField("TP_MODAL4", "DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA4", "DM_ABRANGENCIA");
		
		config.configDomainField("TP_SITUACAO", "DM_SITUACAO_PIPELINE");
		
		config.configDomainField("TP_NEGOCIACAO", "DM_NEGOCIACAO_PIPELINE");
	}
}