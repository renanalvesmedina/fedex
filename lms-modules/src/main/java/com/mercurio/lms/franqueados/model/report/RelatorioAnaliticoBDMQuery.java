package com.mercurio.lms.franqueados.model.report;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioAnaliticoBDMQuery {
	
	
	private static final String projection = "SELECT  " +
			"ANO as \"Ano\", " +
			"MES as \"Mês\", " +
			"SG_FILIAL_ORIGEM as \"Origem\", " +
			"NR_DOCTO_SERVICO as \"Nr. CTRC\", " +
			"SG_FILIAL_DESTINO as \"Destino\", " +
			"DH_EMISSAO as \"Data Emissão\", " +
			"DT_LIQUIDACAO as \"Data Pagamento\", " +
			"MUNICIPIO_ORIGEM as \"Município Origem\", " +
			"MUNICIPIO_DESTINO as \"Município Destino\", " +
			"TP_FRETE as \"Frete\", " +
			"VL_DOCTO_SERVICO as \"Valor Frete\", "+
			"VL_MERCADORIA as \"Valor Mercadoria\", " +
			"VL_ICMS as \"ICMS\", " +
			"VL_PIS as \"PIS\", "+
			"VL_COFINS as \"COFINS\", " +
			"VL_DESCONTO as \"Desc. Cobr\", " +
			"VL_GRIS as \"GRIS\", " +
			"VL_BASE_CALCULO as \"Base Cálculo\", " +
			"VL_PARTICIPACAO as \"Participação\", " +
			"(PC_BC*100) as \"%BC\" ";

	private static final String beginFrom = " FROM ( ";
	private static final String endFrom = " ) x ";
	
	
	private static final String query = "SELECT  "+
			"       TO_CHAR(DSF.DT_COMPETENCIA, 'YYYY') AS ANO,  "+
			"       TO_CHAR(DSF.DT_COMPETENCIA, 'MON') AS MES,  "+
			"       FO.SG_FILIAL SG_FILIAL_ORIGEM,  "+
			"       DS.NR_DOCTO_SERVICO,  "+
			"       FD.SG_FILIAL SG_FILIAL_DESTINO,  "+
			"       TO_CHAR(DS.DH_EMISSAO,'DD/MM/YY') AS DH_EMISSAO, "+
			"       TO_CHAR(DEV.DT_LIQUIDACAO,'DD/MM/YY') AS DT_LIQUIDACAO,"+
			"       MC.NM_MUNICIPIO MUNICIPIO_ORIGEM, "+
			"       ME.NM_MUNICIPIO MUNICIPIO_DESTINO, "+
			"       DECODE(DSF.TP_FRETE, 'CE', 'CIF', 'CR', 'CIF', 'FE', 'FOB', 'FR', 'FOB', 'CIF') AS TP_FRETE, "+
			"       DSF.VL_MERCADORIA, "+
			"       DSF.VL_DOCTO_SERVICO, "+
			"       DSF.VL_ICMS, "+
			"       DSF.VL_PIS, "+
			"       DSF.VL_COFINS, "+
			"       DSF.VL_DESCONTO, "+
			"       DSF.VL_GRIS, "+
			"       DSF.VL_BASE_CALCULO, "+
			"       DSF.VL_PARTICIPACAO, "+
			"       ROUND(DECODE(DSF.VL_BASE_CALCULO, 0, 0, (DSF.VL_PARTICIPACAO / DSF.VL_BASE_CALCULO)),2) AS PC_BC "+
			"FROM   DOCTO_SERVICO_FRQ DSF,  "+
			"       DOCTO_SERVICO DS,  "+
			"       CONHECIMENTO C,  "+
			"       DEVEDOR_DOC_SERV_FAT DEV, "+
			"       MUNICIPIO MC,  "+
			"       MUNICIPIO ME,  "+
			"       FILIAL FO,  "+
			"       FILIAL FD, "+
			"       BAIXA_DEV_MERC DBM, "+
			"       ITEM_BAIXA_DEV_MERC IBDM "+
			"WHERE DSF.ID_DOCTO_SERVICO        = DS.ID_DOCTO_SERVICO "+
			"AND   DS.ID_DOCTO_SERVICO         = C.ID_CONHECIMENTO "+
			"AND   DS.ID_DOCTO_SERVICO         = DEV.ID_DOCTO_SERVICO "+
			"AND   DEV.ID_DEVEDOR_DOC_SERV_FAT = IBDM.ID_DEVEDOR_DOC_SERV_FAT "+
			"AND   IBDM.ID_BAIXA_DEV_MERC      = DBM.ID_BAIXA_DEV_MERC "+
			"AND   C.ID_MUNICIPIO_COLETA       = MC.ID_MUNICIPIO "+
			"AND   C.ID_MUNICIPIO_ENTREGA      = ME.ID_MUNICIPIO "+
			"AND   DS.ID_FILIAL_ORIGEM         = FO.ID_FILIAL "+
			"AND   DS.ID_FILIAL_DESTINO        = FD.ID_FILIAL "+
			"AND   DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL  "+
			"AND   TO_CHAR(DBM.DT_EMISSAO,'MM/YYYY') = TO_CHAR(:competencia,'MM/YYYY') ";
			
	private static final String filtroFranquia = "AND    DSF.ID_FRANQUIA = :idFilial "; 
	
	private static final String order = "ORDER BY DS.NR_DOCTO_SERVICO";
	public static String getQuery(boolean filtraFranquia, boolean isCSV){
		String tmp = "";
		tmp += query;
		if(filtraFranquia == true){
			tmp += filtroFranquia;
		}
		tmp += order;
		
		String result = "";
		if(isCSV){
			result += projection;
			result += beginFrom;
			result += tmp;
			result += endFrom;
		} else {
			result = tmp;
		}

		return result;
	}

	public static ConfigureSqlQuery createConfigureSql(boolean isCSV) {
		if(isCSV){
			return configureCSV();
		} else {
			return configurePDF();
		}
	}

	private static ConfigureSqlQuery configureCSV() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Ano");
				sqlQuery.addScalar("Mês");
				sqlQuery.addScalar("Origem");
				sqlQuery.addScalar("Nr. CTRC");
				sqlQuery.addScalar("Destino");
				sqlQuery.addScalar("Data Emissão");
				sqlQuery.addScalar("Data Pagamento");
				sqlQuery.addScalar("Município Origem");
				sqlQuery.addScalar("Município Destino");
				sqlQuery.addScalar("Frete");
				sqlQuery.addScalar("Valor Mercadoria", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Valor Frete", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("ICMS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("PIS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("COFINS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Desc. Cobr", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("GRIS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Base Cálculo", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Participação", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("%BC");
			}
		};
		return csq;
	}
	
	private static ConfigureSqlQuery configurePDF() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ANO");
				sqlQuery.addScalar("MES");
				sqlQuery.addScalar("SG_FILIAL_ORIGEM");
				sqlQuery.addScalar("NR_DOCTO_SERVICO");
				sqlQuery.addScalar("SG_FILIAL_DESTINO");
				sqlQuery.addScalar("DH_EMISSAO");
				sqlQuery.addScalar("DT_LIQUIDACAO");
				sqlQuery.addScalar("MUNICIPIO_ORIGEM");
				sqlQuery.addScalar("MUNICIPIO_DESTINO");
				sqlQuery.addScalar("TP_FRETE");
				sqlQuery.addScalar("VL_MERCADORIA", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_DOCTO_SERVICO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_ICMS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_PIS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_COFINS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_DESCONTO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_GRIS", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_BASE_CALCULO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_PARTICIPACAO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("PC_BC");
			}
		};
		return csq;
	}
}
