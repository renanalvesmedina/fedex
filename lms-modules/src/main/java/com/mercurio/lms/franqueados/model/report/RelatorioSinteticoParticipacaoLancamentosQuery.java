package com.mercurio.lms.franqueados.model.report;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioSinteticoParticipacaoLancamentosQuery {


	public static String getQuery(){
		String query =  
				" SELECT TP_CONTA_CONTABIL, DS_CONTA_CONTABIL, VL_LANCAMENTO, DS_LANCAMENTO, NM_USUARIO_APROVADOR " +
				" FROM ( " +
				" SELECT DECODE(CCF.TP_CONTA_CONTABIL, 'IO','IN','IF','IN','IA','IN','CA','CD','DA','DD', CCF.TP_CONTA_CONTABIL) AS TP_CONTA_CONTABIL,  " +
				"         CCF.DS_CONTA_CONTABIL,  " +
				"         LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1) AS VL_LANCAMENTO,  " +
				"         LF.DS_LANCAMENTO ,  " +
				"         (SELECT U.NM_USUARIO " +
				"          FROM   ACAO A,  " +
				"                 USUARIO U  " +
				"          WHERE  A.ID_USUARIO = U.ID_USUARIO  " +
				"          AND    A.ID_PENDENCIA = LF.ID_PENDENCIA " +
				"          AND    A.ID_ACAO = (SELECT MAX(A1.ID_ACAO) FROM ACAO A1 WHERE A1.ID_PENDENCIA = LF.ID_PENDENCIA) " +
				"          ) as NM_USUARIO_APROVADOR " +
				"  FROM   LANCAMENTO_FRQ LF,  " +
				"         CONTA_CONTABIL_FRQ CCF " +
				"  WHERE  LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ " +
				"  AND    LF.TP_SITUACAO_PENDENCIA = 'A' " +
				"  AND    CCF.TP_CONTA_CONTABIL NOT IN ('BD','IR') " +
				"  AND    LF.DT_COMPETENCIA = :competencia " +
				"  AND    LF.ID_FRANQUIA = :idFilial " +
				"  UNION ALL " +
				"  SELECT 'AB', " +
				"         DS_CONTA_CONTABIL, " +
				"         SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1)) AS VL_LANCAMENTO,  " +
				"         'Conforme relatório analítico de participação',  " +
				"         'Regra de remuneração' " +
				"  FROM   LANCAMENTO_FRQ LF,  " +
				"         CONTA_CONTABIL_FRQ CCF " +
				"  WHERE  LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ " +
				"  AND    LF.TP_SITUACAO_PENDENCIA = 'A' " +
				"  AND    CCF.TP_CONTA_CONTABIL = 'BD' " +
				"  AND    LF.DT_COMPETENCIA = :competencia " +
				"  AND    LF.ID_FRANQUIA = :idFilial " +
				"  GROUP BY CCF.DS_CONTA_CONTABIL " +
				"  UNION ALL" +
				"  SELECT 'OS', " +
				"         DS_CONTA_CONTABIL, " +
				"         SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1)) AS VL_LANCAMENTO,  " +
				"         'Conforme relatório analítico de participação',  " +
				"         'Regra de remuneração - Automático' " +
				"  FROM   LANCAMENTO_FRQ LF,  " +
				"         CONTA_CONTABIL_FRQ CCF " +
				"  WHERE  LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ " +
				"  AND    LF.TP_SITUACAO_PENDENCIA = 'A' " +
				"  AND    CCF.TP_CONTA_CONTABIL = 'IR' " +
				"  AND    LF.DT_COMPETENCIA = :competencia " +
				"  AND    LF.ID_FRANQUIA = :idFilial " +
				"  GROUP BY CCF.DS_CONTA_CONTABIL " +
				"  UNION ALL" +
				"  SELECT 'AB', " +
				"         'Recálculos da participação dos descontos posteriores a competência', " +
				"         SUM(DSF.VL_DIFERENCA_PARTICIPACAO) * -1, " +
				"         'Conforme relatório analítico de participação',  " +
				"         'Recalc. pagto comp. anteriores' " +
				"  FROM   DOCTO_SERVICO_FRQ DSF " +
				"  WHERE  DSF.DT_COMPETENCIA = :competencia " +
				"  AND    DSF.ID_FRANQUIA = :idFilial " +
				"  AND    DSF.TP_FRETE <> 'SE' " +
				"  AND    DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NOT NULL " +
				"  UNION ALL" +
				"  SELECT 'AB', " +
				"         'Recálculos de serviços dos descontos posteriores a competência', " +
				"         SUM(DSF.VL_DIFERENCA_PARTICIPACAO) * -1, " +
				"         'Conforme relatório analítico de participação',  " +
				"         'Recalc. pagto comp. anteriores' " +
				"  FROM   DOCTO_SERVICO_FRQ DSF " +
				"  WHERE  DSF.DT_COMPETENCIA = :competencia " +
				"  AND    DSF.ID_FRANQUIA = :idFilial " +
				"  AND    DSF.TP_FRETE = 'SE' " +
				"  AND    DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NOT NULL " +
				"  UNION ALL" +
				"  SELECT 'OS', " +
				"         'Serviços adicionais', " +
				"         SUM(DSF.VL_PARTICIPACAO), " +
				"         'Conforme relatório analítico de participação',  " +
				"         'Recalc. pagto comp. anteriores' " +
				"  FROM   DOCTO_SERVICO_FRQ DSF " +
				"  WHERE  DSF.DT_COMPETENCIA = :competencia " +
				"  AND    DSF.ID_FRANQUIA = :idFilial " +
				"  AND    DSF.TP_FRETE = 'SE' " +
				"  AND    DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL " +
				"  UNION ALL" +
				"  SELECT 'OS',  " +
				"         'Reembarque',  " +
				"         SUM(RBQ.VL_CTE + RBQ.VL_TONELADA),  " +
				"         'Conforme relatório analítico de participação',  " +
				"         'Regra de remuneração - Automático' " +
				"  FROM   REEMBARQUE_DOC_SERV_FRQ RBQ, FILIAL F " +
				"  WHERE  RBQ.ID_FRANQUIA = F.ID_FILIAL " +
				"  AND    RBQ.ID_FRANQUIA = :idFilial " +
				"  AND    RBQ.DT_COMPETENCIA = :competencia " +
				"  ) X  " +
				" WHERE VL_LANCAMENTO IS NOT NULL " +
				" ORDER BY TP_CONTA_CONTABIL, DS_CONTA_CONTABIL";

		return query;
	}
	
	public static ConfigureSqlQuery createConfigureSql() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("TP_CONTA_CONTABIL");
				sqlQuery.addScalar("DS_CONTA_CONTABIL");
				sqlQuery.addScalar("VL_LANCAMENTO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("DS_LANCAMENTO");
				sqlQuery.addScalar("NM_USUARIO_APROVADOR");
			}
		};
		return csq;
	}

}
