package com.mercurio.lms.indenizacoes.model.report;

import java.util.Map;

import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioIndenizacoesFranqueadosQuery {
	
	public static String getQuery(Map<String,Object> parameters){
		StringBuilder query = new StringBuilder();
		
		query.append(" SELECT      REG.DS_REGIONAL AS \"Regional debitada\", ");
		query.append("             FFRQ.SG_FILIAL AS \"Filial debitada\", ");
		query.append("             P.NR_IDENTIFICACAO AS \"CPF/CNPJ\", ");
		query.append("             P.NM_PESSOA AS \"Cliente\", ");
		query.append("             MIN(ROUND(RI.VL_INDENIZACAO * (FD.PC_DEBITADO / 100),2)) as \"Valor\", ");
		query.append("             F.SG_FILIAL || ' ' || RI.NR_RECIBO_INDENIZACAO AS \"RIM\", ");
		query.append("             TO_CHAR(RI.DT_LIBERACAO_PAGAMENTO,'DD/MM/YYYY') as \"Data pagto\", ");
		query.append("             DECODE(MANC.TP_MOTIVO, 'AV','Avaria','FV','Falta','Outra') AS \"Tipo\", ");
		query.append("             MIN(FDS.SG_FILIAL || ' ' || DS.NR_DOCTO_SERVICO) AS \"CT-e\", ");
		query.append("             MIN(TO_CHAR(DS.DH_EMISSAO, 'DD/MM/YYYY')) AS \"Data de emissão\", ");
		query.append("             MIN((SELECT U.NM_USUARIO ");
		query.append("                  FROM ACAO A, USUARIO U ");
		query.append("                  WHERE A.ID_USUARIO = U.ID_USUARIO ");
		query.append("                  AND   A.ID_PENDENCIA = RI.ID_PENDENCIA ");
		query.append("                  AND   A.ID_ACAO = (SELECT MAX(A1.ID_ACAO) ");
		query.append("                                     FROM   ACAO A1 ");
		query.append("                                     WHERE  A1.ID_PENDENCIA = RI.ID_PENDENCIA))) AS \"Usuário Análise NC\", ");
		query.append("             TO_CHAR(RIF.DT_COMPETENCIA,'DD/MM/YYYY') AS \"Data 1° parcela\", ");
		query.append("             DECODE(RIF.TP_SITUACAO_PENDENCIA,'E','Em aprovação','A','Aprovado','R','Reprovado','C','Cancelado') AS \"Situação de aprovação\", ");
		query.append("             MIN((SELECT U.NM_USUARIO ");
		query.append("                  FROM ACAO A, USUARIO U ");
		query.append("                  WHERE A.ID_USUARIO = U.ID_USUARIO ");
		query.append("                  AND   A.ID_PENDENCIA = RIF.ID_PENDENCIA ");
		query.append("                  AND   A.ID_ACAO = (SELECT MAX(A1.ID_ACAO) ");
		query.append("                                     FROM   ACAO A1 ");
		query.append("                                     WHERE  A1.ID_PENDENCIA = RIF.ID_PENDENCIA))) AS \"Usuário Análise NC Franquia\", ");
		query.append("             RIF.VL_INDENIZADO * (RIF.PC_INDENIZACAO / 100) as \"Valor indenizado\", ");
		query.append("             RIF.PC_INDENIZACAO as \"% indenização\", ");
		query.append("             RIF.NR_PARCELAS as \"Nr. Parcelas\", ");
		query.append("             (RIF.VL_INDENIZADO * (RIF.PC_INDENIZACAO / 100)) / RIF.NR_PARCELAS as \"Valor da parcela\", ");
		query.append("             RIF.DS_INDENIZACAO as \"Descrição\" ");
		query.append(" FROM        RECIBO_INDENIZACAO RI,  ");
		query.append("             RECIBO_INDENIZACAO_FRQ RIF, ");
		query.append("             DOCTO_SERVICO_INDENIZACAO DSI, ");
		query.append("             DOCTO_SERVICO DS, ");
		query.append("             DEVEDOR_DOC_SERV_FAT DEV, ");
		query.append("             PESSOA P, ");
		query.append("             FILIAL FDS, ");
		query.append("             FILIAL F, ");
		query.append("             FRANQUIA FRQ, ");
		query.append("             FILIAL FFRQ, ");
		query.append("             FILIAL_DEBITADA FD, ");
		query.append("             OCORRENCIA_NAO_CONFORMIDADE ONC,  ");
		query.append("             MOTIVO_ABERTURA_NC MANC, ");
		query.append("             REGIONAL REG, ");
		query.append("             REGIONAL_FILIAL REF ");
		query.append(" WHERE       RI.ID_FILIAL = F.ID_FILIAL ");
		query.append(" AND         RI.ID_RECIBO_INDENIZACAO = DSI.ID_RECIBO_INDENIZACAO ");
		query.append(" AND         RI.ID_RECIBO_INDENIZACAO = RIF.ID_RECIBO_INDENIZACAO ");
		query.append(" AND         DSI.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
		query.append(" AND         DS.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO ");
		query.append(" AND         DEV.ID_CLIENTE = P.ID_PESSOA ");
		query.append(" AND         DS.ID_FILIAL_ORIGEM = FDS.ID_FILIAL ");
		query.append(" AND         FRQ.ID_FRANQUIA = FFRQ.ID_FILIAL ");
		query.append(" AND         RI.ID_RECIBO_INDENIZACAO = FD.ID_RECIBO_INDENIZACAO ");
		query.append(" AND         FD.ID_FILIAL = FRQ.ID_FRANQUIA ");
		query.append(" AND         ONC.ID_OCORRENCIA_NAO_CONFORMIDADE = DSI.ID_OCORRENCIA_NAO_CONFORMIDADE ");
		query.append(" AND         ONC.ID_MOTIVO_ABERTURA_NC = MANC.ID_MOTIVO_ABERTURA_NC ");
		query.append(" AND         REG.ID_REGIONAL = REF.ID_REGIONAL ");
		query.append(" AND         REF.ID_FILIAL = FFRQ.ID_FILIAL ");
		query.append(" AND         RI.DT_PAGAMENTO_EFETUADO BETWEEN REF.DT_VIGENCIA_INICIAL AND REF.DT_VIGENCIA_FINAL ");
		query.append(" AND         RI.DT_PAGAMENTO_EFETUADO BETWEEN REG.DT_VIGENCIA_INICIAL AND REG.DT_VIGENCIA_FINAL ");
		query.append(" AND         RIF.DT_COMPETENCIA BETWEEN TO_DATE(':competenciaInicial','dd/MM/yyyy') and TO_DATE(':competenciaFinal','dd/MM/yyyy') ");
		
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			query.append(" AND         FRQ.ID_FRANQUIA = :franquia ");
		}
		
		query.append(" GROUP BY REG.DS_REGIONAL, ");
		query.append("             FFRQ.SG_FILIAL, ");
		query.append("             F.SG_FILIAL || ' ' || RI.NR_RECIBO_INDENIZACAO, ");
		query.append("             TO_CHAR(RI.DT_LIBERACAO_PAGAMENTO,'DD/MM/YYYY'), ");
		query.append("             DECODE(MANC.TP_MOTIVO, 'AV','Avaria','FV','Falta','Outra'), ");
		query.append("             P.NR_IDENTIFICACAO, ");
		query.append("             P.NM_PESSOA, ");
		query.append("             RIF.TP_SITUACAO_PENDENCIA, ");
		query.append("             TO_CHAR(RIF.DT_COMPETENCIA,'DD/MM/YYYY'), ");
		query.append("             RIF.VL_INDENIZADO, ");
		query.append("             RIF.PC_INDENIZACAO, ");
		query.append("             RIF.NR_PARCELAS, ");
		query.append("             RIF.DS_INDENIZACAO ");
		query.append(" ORDER BY \"Filial debitada\", \"RIM\" ");
		
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
				sqlQuery.addScalar("Regional debitada");
				sqlQuery.addScalar("Filial debitada");
				sqlQuery.addScalar("CPF/CNPJ");
				sqlQuery.addScalar("Cliente");
				sqlQuery.addScalar("Valor", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("RIM");
				sqlQuery.addScalar("Data pagto");
				sqlQuery.addScalar("Tipo");
				sqlQuery.addScalar("CT-e");
				sqlQuery.addScalar("Data de emissão");
				sqlQuery.addScalar("Usuário Análise NC");
				sqlQuery.addScalar("Data 1° parcela");
				sqlQuery.addScalar("Situação de aprovação");
				sqlQuery.addScalar("Usuário Análise NC Franquia");
				sqlQuery.addScalar("Valor indenizado", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("% indenização", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Nr. Parcelas");
				sqlQuery.addScalar("Valor da parcela", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Descrição");
			}
		};
		return csq;
	}
	
}
