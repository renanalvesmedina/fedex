package com.mercurio.lms.franqueados.model.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioAnaliticoReembarqueQuery {
	
    private static final Map<String, String> simulacaoReplaces;
    
    static
    {
    	simulacaoReplaces = new HashMap<String, String>();

    	simulacaoReplaces.put("REEMBARQUE_DOC_SERV_FRQ", "SIMUL_REEMB_DOC_SERV_FRQ");
    	simulacaoReplaces.put("ID_FRANQUIA", "ID_FILIAL");
    }
	
	
	private static final String projection = "SELECT   " +
			"ANO as \"Ano\", " +
			"MES as \"Mês\", " +
			"FRQ as \"FRQ\", " +
			"SG_FILIAL_ORIGEM as \"Origem\", " +
			"NR_DOCTO_SERVICO as \"Nr. CTRC\", " +
			"SG_FILIAL_DESTINO as \"Destino\", " +
			"DH_EMISSAO as \"Data Emissão\", " +
			"DT_LIQUIDACAO as \"Data Pagamento\", " +
			"MUNICIPIO_ORIGEM as \"Município Origem\", " +
			"MUNICIPIO_DESTINO as \"Município Destino\", " +
			"TP_FRETE as \"Frete\", " +
			"VL_TOTAL_DOC_SERVICO as \"Valor Frete\", "+
			"VL_MERCADORIA as \"Valor Mercadoria\", " +
			"PS_MERCADORIA as \"Peso Mercadoria\", " +
			"VL_CTE as \"Valor por CT-e\", "+
			"VL_TONELADA as \"Valor por Tonelada\", " +
			"TOTAL as \"Total\", " +
			"FILIAL_ORIGEM_MANIFESTO as \"Manifesto origem\", " +
			"NR_MANIFESTO_ORIGEM as \"Nr. Manifesto\" ";

	private static final String beginFrom = " FROM ( ";
	private static final String endFrom = " ) x ";
	
	private static final String query = "SELECT  "+
			"       TO_CHAR(RDSF.DT_COMPETENCIA, 'YYYY') AS ANO,  "+
			"       TO_CHAR(RDSF.DT_COMPETENCIA, 'MON') AS MES,  "+
			"		FF.SG_FILIAL AS FRQ,  "+
			"       FO.SG_FILIAL SG_FILIAL_ORIGEM,  "+
			"       DS.NR_DOCTO_SERVICO,  "+
			"       FD.SG_FILIAL SG_FILIAL_DESTINO,  "+
			"       TO_CHAR(DS.DH_EMISSAO,'DD/MM/YY') AS DH_EMISSAO, "+
			"       TO_CHAR(DEV.DT_LIQUIDACAO,'DD/MM/YY') AS DT_LIQUIDACAO,"+
			"       MC.NM_MUNICIPIO MUNICIPIO_ORIGEM, "+
			"       ME.NM_MUNICIPIO MUNICIPIO_DESTINO, "+
			"       DECODE(C.TP_FRETE, 'C', 'CIF', 'F', 'FOB', 'CIF') AS TP_FRETE, "+
			"       DS.VL_MERCADORIA, "+
			"       DS.VL_TOTAL_DOC_SERVICO, "+
			"       RDSF.PS_MERCADORIA, "+
			"       RDSF.VL_CTE, "+
			"       RDSF.VL_TONELADA, "+
			"       RDSF.VL_CTE + RDSF.VL_TONELADA AS TOTAL, "+
			"       FOM.SG_FILIAL FILIAL_ORIGEM_MANIFESTO, "+
			"       MVN.NR_MANIFESTO_ORIGEM "+
			"FROM   REEMBARQUE_DOC_SERV_FRQ RDSF,  "+
			"       DOCTO_SERVICO DS,  "+
			"       CONHECIMENTO C,  "+
			"       DEVEDOR_DOC_SERV_FAT DEV, "+
			"       MUNICIPIO MC,  "+
			"       MUNICIPIO ME,  "+
			"       FILIAL FO,  "+
			"       FILIAL FD, "+
			"       FILIAL FOM, "+
			"       FILIAL FF, "+
			"       MANIFESTO_VIAGEM_NACIONAL MVN "+
			"WHERE RDSF.ID_DOCTO_SERVICO  = DS.ID_DOCTO_SERVICO "+
			"AND   DS.ID_DOCTO_SERVICO    = C.ID_CONHECIMENTO "+
			"AND   DS.ID_DOCTO_SERVICO    = DEV.ID_DOCTO_SERVICO "+
			"AND   RDSF.ID_MANIFESTO      = MVN.ID_MANIFESTO_VIAGEM_NACIONAL "+
			"AND   MVN.ID_FILIAL          = FOM.ID_FILIAL "+
			"AND   C.ID_MUNICIPIO_COLETA  = MC.ID_MUNICIPIO "+
			"AND   C.ID_MUNICIPIO_ENTREGA = ME.ID_MUNICIPIO "+
			"AND   DS.ID_FILIAL_ORIGEM    = FO.ID_FILIAL "+
			"AND   DS.ID_FILIAL_DESTINO   = FD.ID_FILIAL "+
			"AND   RDSF.ID_FRANQUIA       = FF.ID_FILIAL "+
			"AND   RDSF.DT_COMPETENCIA = :competencia ";
			
	private static final String filtroFranquia = "AND    RDSF.ID_FRANQUIA = :idFilial "; 
	
	private static final String order = "ORDER BY SG_FILIAL_ORIGEM, NR_DOCTO_SERVICO ";
	
	
	public static String getQuery(boolean filtraFranquia, boolean isCSV){
		return getQuery(filtraFranquia, isCSV, false);
	}
	
	public static String getQuerySimulacao(boolean filtraFranquia, boolean isCSV){
		return getQuery(filtraFranquia, isCSV, true);
	}

	private static String getQuery(boolean filtraFranquia, boolean isCSV, boolean isSimulacao){
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

		if(isSimulacao){
			result = replaceForSimulation(result);
		}

		return result;
	}
	
	private static String replaceForSimulation(String result) {
		for (Iterator<String> iterator = simulacaoReplaces.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			result = result.replaceAll(key, simulacaoReplaces.get(key));
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
				sqlQuery.addScalar("FRQ");
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
				sqlQuery.addScalar("Peso Mercadoria", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Valor por CT-e", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Valor por Tonelada", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Total", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Manifesto origem");
				sqlQuery.addScalar("Nr. Manifesto");
			}
		};
		return csq;
	}
	

	public static ConfigureSqlQuery configurePDF() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ANO");
				sqlQuery.addScalar("MES");
				sqlQuery.addScalar("FRQ");
				sqlQuery.addScalar("SG_FILIAL_ORIGEM");
				sqlQuery.addScalar("NR_DOCTO_SERVICO");
				sqlQuery.addScalar("SG_FILIAL_DESTINO");
				sqlQuery.addScalar("PS_MERCADORIA");
				sqlQuery.addScalar("DH_EMISSAO");
				sqlQuery.addScalar("DT_LIQUIDACAO");
				sqlQuery.addScalar("MUNICIPIO_ORIGEM");
				sqlQuery.addScalar("MUNICIPIO_DESTINO");
				sqlQuery.addScalar("TP_FRETE");
				sqlQuery.addScalar("VL_MERCADORIA", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_TOTAL_DOC_SERVICO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_CTE", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_TONELADA", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("TOTAL", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("FILIAL_ORIGEM_MANIFESTO");
				sqlQuery.addScalar("NR_MANIFESTO_ORIGEM");
			}
		};
		return csq;
	}

}
