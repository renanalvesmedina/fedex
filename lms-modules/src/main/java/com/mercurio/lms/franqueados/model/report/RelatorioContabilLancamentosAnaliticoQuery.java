package com.mercurio.lms.franqueados.model.report;

import java.util.Map;

import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

public class RelatorioContabilLancamentosAnaliticoQuery {
	
	public static String getQuery(Map<String,Object> parameters){
		StringBuilder query = new StringBuilder();
		
		query.append(" SELECT TO_CHAR(DT_COMPETENCIA, 'YYYY') as \"Ano\", ");
		query.append("        TO_CHAR(DT_COMPETENCIA, 'MON') AS \"Mês\",  ");
		query.append("        (CASE WHEN TO_CHAR(DT_COMPETENCIA, 'MMDD') <= '0331' THEN 'Q1' ");
		query.append("              WHEN TO_CHAR(DT_COMPETENCIA, 'MMDD') <= '0630' THEN 'Q2' ");
		query.append("              WHEN TO_CHAR(DT_COMPETENCIA, 'MMDD') <= '0930' THEN 'Q3' ");
		query.append("              ELSE 'Q4' END ");
		query.append("         ) AS QTR, ");
		query.append("       SG_FILIAL AS \"Unidade\",  ");
		query.append("       CD_CONTA_CONTABIL AS \"Cód. conta contábil\", ");
		query.append("       DS_CONTA_CONTABIL AS \"Conta Contábil\", ");
		query.append("       TP_LANCAMENTO AS \"Tipo de lançamento\", ");
		query.append("       VL_TOTAL AS \"Valor total\", ");
		query.append("       QT_TOTAL AS \"Qtd. total\" ");
		query.append(" FROM ( ");
		query.append("     SELECT F.SG_FILIAL, LF.DT_COMPETENCIA, CCF.CD_CONTA_CONTABIL, ");
		query.append("            UPPER(CCF.DS_CONTA_CONTABIL) AS DS_CONTA_CONTABIL, CCF.TP_LANCAMENTO, ");
		query.append("            SUM(NVL(LF.VL_CONTABIL,LF.VL_LANCAMENTO)) AS VL_TOTAL, COUNT(*) AS QT_TOTAL ");
		query.append("     FROM LANCAMENTO_FRQ LF, CONTA_CONTABIL_FRQ CCF, FILIAL F ");
		query.append("     WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ");
		query.append("     AND   LF.ID_FRANQUIA = F.ID_FILIAL ");
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			query.append("     AND   LF.ID_FRANQUIA = :franquia ");
		}
		query.append("     AND   LF.DT_COMPETENCIA BETWEEN TO_DATE(':inicioAno','dd/MM/yyyy') AND TO_DATE(':competencia','dd/MM/yyyy') ");
		query.append("     AND   LF.TP_SITUACAO_PENDENCIA = 'A'  ");
		query.append("     GROUP BY F.SG_FILIAL, LF.DT_COMPETENCIA, CCF.CD_CONTA_CONTABIL,  ");
		query.append("           CCF.DS_CONTA_CONTABIL, CCF.TP_LANCAMENTO ");
		query.append("     UNION  ");
		query.append("     SELECT F.SG_FILIAL, DSF.DT_COMPETENCIA, '',  ");
		query.append("            'PARTICIPAÇÃO DE FRETES', 'C',  ");
		query.append("            SUM(DSF.VL_PARTICIPACAO), COUNT(*) ");
		query.append("     FROM  DOCTO_SERVICO_FRQ DSF, FILIAL F ");
		query.append("     WHERE DSF.ID_FRANQUIA = F.ID_FILIAL ");
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			query.append("     AND   DSF.ID_FRANQUIA = :franquia ");
		}
		query.append("     AND   DSF.DT_COMPETENCIA BETWEEN TO_DATE(':inicioAno','dd/MM/yyyy') AND TO_DATE(':competencia','dd/MM/yyyy') ");
		query.append("     AND   DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL ");
		query.append("     AND   DSF.TP_FRETE <> 'SE' ");
		query.append("     GROUP BY F.SG_FILIAL, DSF.DT_COMPETENCIA ");
		query.append("     UNION  ");
		query.append("     SELECT F.SG_FILIAL, DSF.DT_COMPETENCIA, '',  ");
		query.append("            'PARTICIPAÇÃO DE SERVIÇOS ADICIONAIS', 'C',  ");
		query.append("            SUM(DSF.VL_PARTICIPACAO), COUNT(*) ");
		query.append("     FROM  DOCTO_SERVICO_FRQ DSF, FILIAL F ");
		query.append("     WHERE DSF.ID_FRANQUIA = F.ID_FILIAL ");
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			query.append("     AND   DSF.ID_FRANQUIA = :franquia ");
		}
		query.append("     AND   DSF.DT_COMPETENCIA BETWEEN TO_DATE(':inicioAno','dd/MM/yyyy') AND TO_DATE(':competencia','dd/MM/yyyy') ");
		query.append("     AND   DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL ");
		query.append("     AND   DSF.TP_FRETE = 'SE' ");
		query.append("     GROUP BY F.SG_FILIAL, DSF.DT_COMPETENCIA ");
		query.append("     UNION ");
		query.append("     SELECT F.SG_FILIAL, DSF.DT_COMPETENCIA, '',  ");
		query.append("            'RECÁLCULOS DE FRETES', 'D',  ");
		query.append("            SUM(DSF.VL_DIFERENCA_PARTICIPACAO), COUNT(*) ");
		query.append("     FROM  DOCTO_SERVICO_FRQ DSF, FILIAL F ");
		query.append("     WHERE DSF.ID_FRANQUIA = F.ID_FILIAL ");
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			query.append("     AND   DSF.ID_FRANQUIA = :franquia ");
		}
		query.append("     AND   DSF.DT_COMPETENCIA BETWEEN TO_DATE(':inicioAno','dd/MM/yyyy') AND TO_DATE(':competencia','dd/MM/yyyy') ");
		query.append("     AND   DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NOT NULL ");
		query.append("     AND   DSF.TP_FRETE <> 'SE' ");
		query.append("     GROUP BY F.SG_FILIAL, DSF.DT_COMPETENCIA ");
		query.append("     UNION ");
		query.append("     SELECT F.SG_FILIAL, DSF.DT_COMPETENCIA, '',  ");
		query.append("            'RECÁLCULOS DE SERVIÇOS ADICIONAIS', 'D',  ");
		query.append("            SUM(DSF.VL_DIFERENCA_PARTICIPACAO), COUNT(*) ");
		query.append("     FROM  DOCTO_SERVICO_FRQ DSF, FILIAL F ");
		query.append("     WHERE DSF.ID_FRANQUIA = F.ID_FILIAL ");
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			query.append("     AND   DSF.ID_FRANQUIA = :franquia ");
		}
		query.append("     AND   DSF.DT_COMPETENCIA BETWEEN TO_DATE(':inicioAno','dd/MM/yyyy') AND TO_DATE(':competencia','dd/MM/yyyy') ");
		query.append("     AND   DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NOT NULL ");
		query.append("     AND   DSF.TP_FRETE = 'SE' ");
		query.append("     GROUP BY F.SG_FILIAL, DSF.DT_COMPETENCIA ");
		query.append("     UNION ");
		query.append("     SELECT F.SG_FILIAL, RBQ.DT_COMPETENCIA, '',  ");
		query.append("            'REEMBARQUE', 'C',  ");
		query.append("            SUM(RBQ.VL_CTE + RBQ.VL_TONELADA), COUNT(*) ");
		query.append("     FROM  REEMBARQUE_DOC_SERV_FRQ RBQ, FILIAL F ");
		query.append("     WHERE RBQ.ID_FRANQUIA = F.ID_FILIAL ");
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			query.append("     AND   RBQ.ID_FRANQUIA = :franquia ");
		}
		query.append("     AND   RBQ.DT_COMPETENCIA BETWEEN TO_DATE(':inicioAno','dd/MM/yyyy') AND TO_DATE(':competencia','dd/MM/yyyy') ");
		query.append("     GROUP BY F.SG_FILIAL, RBQ.DT_COMPETENCIA ");
		query.append(" ) X ");
		query.append(" ORDER BY TO_CHAR(DT_COMPETENCIA, 'YYYY'), TO_CHAR(DT_COMPETENCIA, 'MM'),  ");
		query.append("          SG_FILIAL, DS_CONTA_CONTABIL ");
		
		String sql = query.toString();

		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
    		Long idFranquia = (Long) parameters.get("idFilial");
    		sql = sql.replaceAll(":franquia", idFranquia.toString());
    	}

		YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
		String competencia = dtCompetencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
		sql = sql.replaceAll(":competencia", competencia);
		
    	YearMonthDay dtInicioAno = FranqueadoUtils.buscarPrimeiroDiaAno(dtCompetencia);
		String inicioAno = dtInicioAno.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
		sql = sql.replaceAll(":inicioAno", inicioAno);

		return sql;
	}
	
	public static ConfigureSqlQuery createConfigureSql() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Ano");
				sqlQuery.addScalar("Mês");
				sqlQuery.addScalar("QTR", Hibernate.STRING);
				sqlQuery.addScalar("Unidade");
				sqlQuery.addScalar("Cód. conta contábil");
				sqlQuery.addScalar("Conta Contábil");
				sqlQuery.addScalar("Tipo de lançamento");
				sqlQuery.addScalar("Valor total", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Qtd. total", Hibernate.BIG_DECIMAL);
			}
		};
		return csq;
	}
	
}
