package com.mercurio.lms.franqueados.model.report;

import java.util.Map;

import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioSinteticoGeralTipoFreteQuery {
	
	public static String getQuery(Map<String,Object> parameters){
		StringBuilder query = new StringBuilder();
		
		query.append(" SELECT TO_CHAR(DSF.DT_COMPETENCIA, 'YYYY') AS \"Ano\",   ");
		query.append(" 		  TO_CHAR(DSF.DT_COMPETENCIA, 'MON') AS \"Mês\",  ");
		query.append("        (CASE WHEN TO_CHAR(DSF.DT_COMPETENCIA, 'MMDD') <= '0331' THEN 'Q1'  ");
		query.append("              WHEN TO_CHAR(DSF.DT_COMPETENCIA, 'MMDD') <= '0630' THEN 'Q2' ");
		query.append("              WHEN TO_CHAR(DSF.DT_COMPETENCIA, 'MMDD') <= '0930' THEN 'Q3' ");
		query.append("              ELSE 'Q4' END ");
		query.append("         ) AS \"Qter\", ");
		query.append("        F.SG_FILIAL as \"FRQ\",  ");
		query.append("        UFP.SG_UNIDADE_FEDERATIVA AS \"UF\", ");
		query.append("        DECODE(DSF.TP_FRETE, 'CE', 'COL', 'FR', 'ENT', 'CR', 'ENT', 'FE', 'COL', 'FL', 'LOCAL', 'SE', 'SERV') AS \"Col/Ent\", ");
		query.append("        DECODE(DSF.TP_FRETE, 'CE', 'CIF', 'CR', 'CIF', 'FE', 'FOB', 'FR', 'FOB', 'CIF') AS \"Tp Frete\", ");
		query.append("        DECODE(DSF.TP_FRETE, 'CE', 'CIF Exp', 'CR', 'CIF Rec', 'FE', 'FOB Exp', 'FR', 'FOB Rec', 'FL', 'Frete Local', 'Serviço Adicional') AS \"Tp Frete I\", ");
		query.append("        DECODE(DECODE(SIGN(DSF.VL_CUSTO_AEREO),0,DSF.TP_FRETE,'AE'), 'CE', 'CIF Exp', 'FR', 'FOB Rec', 'CR', 'CIF Rec', 'FE', 'FOB Exp', 'FL', 'Frete Local', 'SE', 'Serviço Adicional', 'Aéreo') AS \"Tp Frete II\", ");
		query.append("        DECODE(DSF.TP_FRETE, 'CE', 'DEC', 'FR', 'DEC', 'FL', 'DEC', 'CR', 'NDEC', 'FE', 'NDEC', 'SE', 'SERV') AS \"Decisor\", ");
		query.append("        count(*) as \"Qtde CTRC\", ");
		query.append("        sum(DSF.VL_MERCADORIA) as \"Vlr Mercadoria\",  ");
		query.append("        sum(DS.QT_VOLUMES) as \"Qtd Volume\",  ");
		query.append("        sum(DS.PS_REAL) as \"Peso\",  ");
		query.append("        sum(DSF.VL_DOCTO_SERVICO) as \"Frete\",  ");
		query.append("        sum(DSF.VL_ICMS) as \"ICMS\",  ");
		query.append("        sum(DSF.VL_PIS) as \"PIS\",  ");
		query.append("        sum(DSF.VL_COFINS) as \"COFINS\",  ");
		query.append("        sum(DSF.VL_DESCONTO) AS \"Desc. Cobr\",  ");
		query.append("        sum(DSF.VL_CUSTO_AEREO) AS \"Custo Aéreo\",  ");
		query.append("        sum(DSF.VL_CUSTO_CARRETEIRO) AS \"Frete Carret.\",  ");
		query.append("        sum(DSF.VL_GENERALIDADE) as \"Generalidade\",  ");
		query.append("        sum(DSF.VL_GRIS) as \"$GRIS\",  ");
		query.append("        sum(DSF.VL_AJUSTE_BASE_NEGATIVA) as \"BC < 0\",  ");
		query.append("        sum(DSF.VL_BASE_CALCULO) as \"Base de cálculo\",  ");
		query.append("        sum(DSF.NR_KM_TRANSFERENCIA) as \"Distância de transferencia\",  ");
		query.append("        sum(DSF.VL_KM_TRANSFERENCIA) as \"Valor transferência\",  ");
		query.append("        sum(DSF.NR_KM_COLETA_ENTREGA) as \"Distância de coleta/entrega\",  ");
		query.append("        sum(DSF.VL_KM_COLETA_ENTREGA) as \"Valor coleta/entrega\",  ");
		query.append("        sum(DECODE(DSF.TP_FRETE, 'FL', VL_PARTICIPACAO, 0)) AS \"Valor frete local\", ");
		query.append("        sum(DSF.VL_KM_TRANSFERENCIA + DSF.VL_KM_COLETA_ENTREGA + DECODE(DSF.TP_FRETE, 'FL', VL_PARTICIPACAO, 0)) AS \"Participação básica\", ");
		query.append("        sum(DSF.VL_FIXO_COLETA_ENTREGA) as \"Fixo CTRC\", ");
		query.append("        sum(DSF.VL_KM_TRANSFERENCIA + DSF.VL_KM_COLETA_ENTREGA + DECODE(DSF.TP_FRETE, 'FL', VL_PARTICIPACAO, 0) + DSF.VL_FIXO_COLETA_ENTREGA) AS \"Participação Base\",  ");
		query.append("        sum(DSF.VL_REPASSE_ICMS) as \"Repasse ICMS\",  ");
		query.append("        sum(DSF.VL_REPASSE_PIS) as \"Repasse PIS\",  ");
		query.append("        sum(DSF.VL_REPASSE_COFINS) as \"Repasse COFINS\",  ");
		query.append("        sum(DSF.VL_KM_TRANSFERENCIA + DSF.VL_KM_COLETA_ENTREGA + DECODE(DSF.TP_FRETE, 'FL', VL_PARTICIPACAO, 0) + DSF.VL_FIXO_COLETA_ENTREGA + DSF.VL_REPASSE_ICMS + DSF.VL_REPASSE_PIS + DSF.VL_REPASSE_COFINS) AS \"Participaçao\", ");
		query.append("        sum(DSF.VL_DESCONTO_LIMITADOR) as \"Desconto limitador\",  ");
		query.append("        sum(DSF.VL_KM_TRANSFERENCIA + DSF.VL_KM_COLETA_ENTREGA + DECODE(DSF.TP_FRETE, 'FL', VL_PARTICIPACAO, 0) + DSF.VL_FIXO_COLETA_ENTREGA + DSF.VL_REPASSE_ICMS + DSF.VL_REPASSE_PIS + DSF.VL_REPASSE_COFINS - DSF.VL_DESCONTO_LIMITADOR) AS \"Participação total\", ");
		query.append("        sum(DSF.VL_REPASSE_GENERALIDADE) as \"Repasse generalidade\",  ");
		query.append("        sum(DSF.VL_PARTICIPACAO) AS \"Participação Final\" ");
		query.append(" FROM   FILIAL F, ");
		query.append("        DOCTO_SERVICO_FRQ DSF,  ");
		query.append("        DOCTO_SERVICO DS,  ");
		query.append("        PESSOA P,  ");
		query.append("        ENDERECO_PESSOA EP,  ");
		query.append("        MUNICIPIO MP, ");
		query.append("        UNIDADE_FEDERATIVA UFP, ");
		query.append("        DEVEDOR_DOC_SERV_FAT DEV ");
		query.append(" WHERE  DSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
		query.append(" AND    DSF.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO ");
		query.append(" AND    DSF.ID_FRANQUIA = F.ID_FILIAL ");
		query.append(" AND    DSF.ID_FRANQUIA = P.ID_PESSOA ");
		query.append(" AND    P.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA ");
		query.append(" AND    EP.ID_MUNICIPIO = MP.ID_MUNICIPIO ");
		query.append(" AND    MP.ID_UNIDADE_FEDERATIVA = UFP.ID_UNIDADE_FEDERATIVA ");
		query.append(" AND    DSF.DT_COMPETENCIA BETWEEN TO_DATE(':competenciaInicial','dd/MM/yyyy') AND TO_DATE(':competenciaFinal','dd/MM/yyyy') ");

		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			query.append(" AND         DSF.ID_FRANQUIA = :franquia ");
		}

		query.append(" group by TO_CHAR(DSF.DT_COMPETENCIA, 'YYYY') , TO_CHAR(DSF.DT_COMPETENCIA, 'MON'),  ");
		query.append("        (CASE WHEN TO_CHAR(DSF.DT_COMPETENCIA, 'MMDD') <= '0331' THEN 'Q1'  ");
		query.append("              WHEN TO_CHAR(DSF.DT_COMPETENCIA, 'MMDD') <= '0630' THEN 'Q2' ");
		query.append("              WHEN TO_CHAR(DSF.DT_COMPETENCIA, 'MMDD') <= '0930' THEN 'Q3' ");
		query.append("              ELSE 'Q4' END ");
		query.append("         ), ");
		query.append("        F.SG_FILIAL, UFP.SG_UNIDADE_FEDERATIVA, ");
		query.append("        DECODE(DSF.TP_FRETE, 'CE', 'COL', 'FR', 'ENT', 'CR', 'ENT', 'FE', 'COL', 'FL', 'LOCAL', 'SE', 'SERV'), ");
		query.append("        DECODE(DSF.TP_FRETE, 'CE', 'CIF', 'CR', 'CIF', 'FE', 'FOB', 'FR', 'FOB', 'CIF'), ");
		query.append("        DECODE(DSF.TP_FRETE, 'CE', 'CIF Exp', 'CR', 'CIF Rec', 'FE', 'FOB Exp', 'FR', 'FOB Rec', 'FL', 'Frete Local', 'Serviço Adicional'), ");
		query.append("        DECODE(DECODE(SIGN(DSF.VL_CUSTO_AEREO),0,DSF.TP_FRETE,'AE'), 'CE', 'CIF Exp', 'FR', 'FOB Rec', 'CR', 'CIF Rec', 'FE', 'FOB Exp', 'FL', 'Frete Local', 'SE', 'Serviço Adicional', 'Aéreo'), ");
		query.append("        DECODE(DSF.TP_FRETE, 'CE', 'DEC', 'FR', 'DEC', 'FL', 'DEC', 'CR', 'NDEC', 'FE', 'NDEC', 'SE', 'SERV') ");
		query.append(" order by 1,2,3,4 ");
		
		String sql = query.toString();

		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
    		Long idFranquia = (Long) parameters.get("idFilial");
    		sql = sql.replaceAll(":franquia", idFranquia.toString());
    	}
		
		YearMonthDay dtCompetenciaInicial = (YearMonthDay) parameters.get("competenciaInicial");
		String competenciaInicial = dtCompetenciaInicial.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
		sql = sql.replaceAll(":competenciaInicial", competenciaInicial);
		
		YearMonthDay dtCompetenciaFinal = (YearMonthDay) parameters.get("competenciaFinal");
		String competenciaFinal = dtCompetenciaFinal.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
		sql = sql.replaceAll(":competenciaFinal", competenciaFinal);
		

		return sql;
	}
	
	public static ConfigureSqlQuery createConfigureSql() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Ano");
				sqlQuery.addScalar("Mês");
				sqlQuery.addScalar("Qter", Hibernate.STRING);
				sqlQuery.addScalar("FRQ");
				sqlQuery.addScalar("UF");
				sqlQuery.addScalar("Col/Ent");
				sqlQuery.addScalar("Tp Frete");
				sqlQuery.addScalar("Tp Frete I");
				sqlQuery.addScalar("Tp Frete II");
				sqlQuery.addScalar("Decisor");
				sqlQuery.addScalar("Qtde CTRC", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Vlr Mercadoria", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Qtd Volume", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Peso", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Frete", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("ICMS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("PIS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("COFINS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Desc. Cobr", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Custo Aéreo", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Frete Carret.", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Generalidade", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("$GRIS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("BC < 0", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Base de cálculo", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Distância de transferencia", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Valor transferência", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Distância de coleta/entrega", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Valor coleta/entrega", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Valor frete local", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Participação básica", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Fixo CTRC", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Participação Base", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Repasse ICMS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Repasse PIS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Repasse COFINS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Participaçao", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Desconto limitador", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Participação total", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Repasse generalidade", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Participação Final", Hibernate.BIG_DECIMAL);
			}
		};
		return csq;
	}
	
}
