package com.mercurio.lms.franqueados.model.report;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioBaixaCessaoCreditoQuery {

	private static final String query = "SELECT  TO_CHAR(RC.DT_LIQUIDACAO,'yyyy')  as \"Ano\", "+
			" 		TO_CHAR(RC.DT_LIQUIDACAO,'mon')   as \"Mês\", "+
			" 		CASE WHEN TO_CHAR(RC.DT_LIQUIDACAO,'mmdd') <= '0331' THEN 'Q1' ELSE "+  
			" 		CASE WHEN TO_CHAR(RC.DT_LIQUIDACAO,'mmdd') <= '0630' THEN 'Q2' ELSE "+
			" 		CASE WHEN TO_CHAR(RC.DT_LIQUIDACAO,'mmdd') <= '0930' THEN 'Q3' ELSE "+
			" 		'Q4' END END END as \"QTR\", "+
			"		FFRQ.SG_FILIAL as \"Unidade\", "+
			"       to_char(DS.DH_EMISSAO,'yyyy') as \"Ano Emissão\",  "+
			"       to_char(DS.DH_EMISSAO,'mon') as \"Mês Emissão\",  "+
			"       to_char(DS.DH_EMISSAO,'dd/mm/yyyy') as \"Emissão CTRC\",  "+
			"       P.NM_PESSOA as \"Cliente\", "+
			"       DECODE(DS.TP_DOCUMENTO_SERVICO, 'NFS', 'NF-e', 'NFE', 'NF-e', 'CTE', 'CT-e', 'CTR', 'CT-e', 'NFT', 'NT-e', 'NT-e') as \"Docto\", "+
			"       FDS.SG_FILIAL || ' ' || DS.NR_DOCTO_SERVICO as \"#Docto\",  "+
			"       DS.VL_TOTAL_DOC_SERVICO as \"Valor Frete\",  "+
			"       DECODE((SELECT TP_FRETE FROM CONHECIMENTO CO WHERE CO.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO), 'CE', 'CIF', 'CR', 'CIF', 'FE', 'FOB', 'FR', 'FOB', 'CIF') AS \"Tipo\", "+
			"       FF.SG_FILIAL || ' ' || F.NR_FATURA as \"Fatura\",  "+
			"       to_char(F.DT_EMISSAO,'yyyy') as \"Ano Fatura\",  "+
			"       to_char(F.DT_EMISSAO,'mon') as \"Mês Fatura\",  "+
			"       to_char(F.DT_EMISSAO,'dd/mm/yyyy') as \"Emissão Fatura\", "+
			"       to_char(F.DT_VENCIMENTO,'dd/mm/yyyy') as \"Venc Fatura\", "+
			"       FR.SG_FILIAL || ' ' || R.NR_REDECO as \"Redeco Baixa\", "+
			"       to_char(R.DT_EMISSAO,'yyyy') as \"Ano Baixa\",  "+
			"       to_char(R.DT_EMISSAO,'mon') as \"Mês Baixa\",  "+			
			"       to_char(R.DT_EMISSAO,'dd/mm/yyyy') as \"Data Redeco\", "+
			"       VI18N(SM.DS_SEGMENTO_MERCADO_I) as \"Segmento\" "+
			"FROM RELACAO_COBRANCA RC,  "+
			"     REDECO R,  "+
			"     ITEM_REDECO IR, "+
			"     FATURA F, "+
			"     ITEM_FATURA IF, "+
			"     DEVEDOR_DOC_SERV_FAT DEV, "+
			"     DOCTO_SERVICO DS, "+
			"     CLIENTE CL, "+
			"     PESSOA P, "+
			"     SEGMENTO_MERCADO SM, "+
			"     FILIAL FDS, "+
			"     FILIAL FF, "+
			"     FILIAL FR, "+
			"     FILIAL FFRQ, "+
			"     FRANQUIA FRQ "+
			"WHERE RC.ID_REDECO = R.ID_REDECO "+
			"AND   R.ID_REDECO = IR.ID_REDECO "+
			"AND   IR.ID_FATURA = F.ID_FATURA "+
			"AND   F.ID_FATURA = IF.ID_FATURA "+
			"AND   IF.ID_DEVEDOR_DOC_SERV_FAT = DEV.ID_DEVEDOR_DOC_SERV_FAT "+
			"AND   DEV.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO "+
			"AND   DEV.ID_CLIENTE = CL.ID_CLIENTE "+
			"AND   CL.ID_CLIENTE = P.ID_PESSOA "+
			"AND   CL.ID_SEGMENTO_MERCADO = SM.ID_SEGMENTO_MERCADO (+) "+
			"AND   DS.ID_FILIAL_ORIGEM = FDS.ID_FILIAL "+
			"AND   R.ID_FILIAL = FR.ID_FILIAL "+
			"AND   F.ID_FILIAL = FF.ID_FILIAL "+
			"AND   RC.ID_FILIAL = FRQ.ID_FRANQUIA "+
			"AND   FRQ.ID_FRANQUIA = FFRQ.ID_FILIAL "+
			"AND   R.TP_FINALIDADE IN ('CJ','DF') "+

			"AND RC.DT_LIQUIDACAO BETWEEN :dtInicio AND :dtFim ";
			
	private static final String filtroFranquia = "AND RC.ID_FILIAL = :idFilial "; 
	
	private static final String orderBy = "ORDER BY FFRQ.SG_FILIAL, FDS.SG_FILIAL, DS.NR_DOCTO_SERVICO";
	
	public static String getQuery(boolean filtraFranquia){
		String result = "";
		result += query;
		if(filtraFranquia == true){
			result += filtroFranquia;
		}
		
		result += orderBy;

		return result;
	}

	public static ConfigureSqlQuery createConfigureSql() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Ano");
				sqlQuery.addScalar("Mês");
				sqlQuery.addScalar("QTR", Hibernate.STRING);
				sqlQuery.addScalar("Unidade");
				sqlQuery.addScalar("Ano Emissão");
				sqlQuery.addScalar("Mês Emissão");
				sqlQuery.addScalar("Emissão CTRC");
				sqlQuery.addScalar("Cliente");
				sqlQuery.addScalar("Docto");
				sqlQuery.addScalar("#Docto");
				sqlQuery.addScalar("Valor Frete");
				sqlQuery.addScalar("Tipo");
				sqlQuery.addScalar("Fatura");
				sqlQuery.addScalar("Ano Fatura");
				sqlQuery.addScalar("Mês Fatura");
				sqlQuery.addScalar("Emissão Fatura");
				sqlQuery.addScalar("Venc Fatura");
				sqlQuery.addScalar("Redeco Baixa");
				sqlQuery.addScalar("Data Redeco");
				sqlQuery.addScalar("Ano Baixa");
				sqlQuery.addScalar("Mês Baixa");
				sqlQuery.addScalar("Segmento");
			}
		};
		return csq;
	}
}
