package com.mercurio.lms.franqueados.model.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioAnaliticoServicoAdicionalQuery {
	
    private static final Map<String, String> simulacaoReplaces;
    
    static
    {
    	simulacaoReplaces = new HashMap<String, String>();
    	
    	simulacaoReplaces.put("ID_DOCTO_SERVICO_FRQ", "ID_SIMULACAO_DOC_SERV_FRQ");
    	simulacaoReplaces.put("ID_DOCTO_SERVICO_FRQ_ORIGINAL", "ID_SIMUL_DOC_SERV_FRQ_ORIGINAL");
    	simulacaoReplaces.put("DOCTO_SERVICO_FRQ", "SIMULACAO_DOC_SERV_FRQ");
    	simulacaoReplaces.put("ID_FRANQUIA", "ID_FILIAL");
    }
	
	private static final String projection = "SELECT  " +
			"ANO as \"Ano\", " +
			"MES as \"Mês\", " +
			"SG_FILIAL_ORIGEM as \"Unid.\", " +
			"NR_DOCTO_SERVICO as \"Nr. NF\", " +
			"NM_SERVICO_ADICIONAL as \"Nome Serviço Adicional\", " +
			"DH_EMISSAO as \"Data Emissão\", " +
			"DT_LIQUIDACAO as \"Data Pagamento\", " +
			"NM_MUNICIPIO as \"Município Coleta/Entrega\", " +
			"VL_DOCTO_SERVICO as \"Valor Serv.\", "+
			"VL_MERCADORIA as \"Valor Mercadoria\", " +
			"VL_ICMS as \"ISS\", " +
			"VL_PIS as \"PIS\", "+
			"VL_COFINS as \"COFINS\", " +
			"VL_DESCONTO as \"Desc. Cobr\", " +
			"VL_BASE_CALCULO as \"Base Cálculo\", " +
			"VL_PARTICIPACAO as \"Participação\", " +
			"(PC_BC*100) as \"%BC\", "+
			"TOMADOR as \"Tomador Serviço\" ";

	private static final String beginFrom = " FROM ( ";
	private static final String endFrom = " ) x ";
	
	private static final String query = "SELECT   "+
			"       TO_CHAR(DSF.DT_COMPETENCIA, 'YYYY') AS ANO,  "+
			"       TO_CHAR(DSF.DT_COMPETENCIA, 'MON') AS MES,  "+
			"       FO.SG_FILIAL SG_FILIAL_ORIGEM,  "+
			"       DS.NR_DOCTO_SERVICO,  "+
			"		SUBSTR(REGEXP_SUBSTR(SA.DS_SERVICO_ADICIONAL_I, 'pt_BR»[^¦]+'), "+
			"	    INSTR(REGEXP_SUBSTR(SA.DS_SERVICO_ADICIONAL_I, 'pt_BR»[^¦]+'), "+
			"	    'pt_BR»') +LENGTH('pt_BR»')) AS NM_SERVICO_ADICIONAL, "+
			"		SA.DS_SERVICO_ADICIONAL_I, "+
			"       TO_CHAR(DS.DH_EMISSAO,'DD/MM/YY') AS DH_EMISSAO, "+
			"       TO_CHAR(DEV.DT_LIQUIDACAO,'DD/MM/YY') AS DT_LIQUIDACAO,"+
			"       M.NM_MUNICIPIO, "+
			"       DSF.VL_MERCADORIA, "+
			"       DSF.VL_DOCTO_SERVICO, "+
			"       DSF.VL_ICMS, "+
			"       DSF.VL_PIS, "+
			"       DSF.VL_COFINS, "+
			"       DSF.VL_DESCONTO, "+
			"       DSF.VL_BASE_CALCULO, "+
			"       DSF.VL_PARTICIPACAO, "+
			"       ROUND(DECODE(DSF.VL_BASE_CALCULO, 0, 0, (DSF.VL_PARTICIPACAO / DSF.VL_BASE_CALCULO)), 2) AS PC_BC, "+
			"       P.NM_PESSOA AS TOMADOR "+
			"FROM   DOCTO_SERVICO_FRQ DSF,  "+
			"       DOCTO_SERVICO DS,  "+
			"       NOTA_FISCAL_SERVICO NFS,  "+
			"       DEVEDOR_DOC_SERV_FAT DEV, "+
			"       MUNICIPIO M,  "+
			"       FILIAL FO,  " +
			"		PESSOA P, "+
			"		SERV_ADICIONAL_DOC_SERV SADS, "+
			"		SERVICO_ADICIONAL SA "+
			"WHERE DSF.ID_DOCTO_SERVICO   = DS.ID_DOCTO_SERVICO "+
			"AND   DSF.ID_DOCTO_SERVICO   = SADS.ID_DOCTO_SERVICO "+
			"AND   SADS.ID_SERVICO_ADICIONAL = SA.ID_SERVICO_ADICIONAL "+
			"AND   DS.ID_DOCTO_SERVICO    = NFS.ID_NOTA_FISCAL_SERVICO "+
			"AND   DS.ID_DOCTO_SERVICO    = DEV.ID_DOCTO_SERVICO "+
			"AND   NFS.ID_MUNICIPIO       = M.ID_MUNICIPIO "+
			"AND   DS.ID_FILIAL_ORIGEM    = FO.ID_FILIAL "+
			"AND   DEV.ID_CLIENTE = P.ID_PESSOA "+
			"AND   DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL  "+
			"AND   DSF.TP_FRETE           = 'SE' "+
			"AND   DSF.DT_COMPETENCIA = :competencia ";
			
	private static final String filtroFranquia = "AND    DSF.ID_FRANQUIA = :idFilial "; 
	
	private static final String order = "ORDER BY SG_FILIAL_ORIGEM, NR_DOCTO_SERVICO";
	
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
				sqlQuery.addScalar("Unid.");
				sqlQuery.addScalar("Nr. NF");
				sqlQuery.addScalar("Nome Serviço Adicional");
				sqlQuery.addScalar("Data Emissão");
				sqlQuery.addScalar("Data Pagamento");
				sqlQuery.addScalar("Município Coleta/Entrega");
				sqlQuery.addScalar("Valor Mercadoria");
				sqlQuery.addScalar("Valor Serv.");
				sqlQuery.addScalar("ISS");
				sqlQuery.addScalar("PIS");
				sqlQuery.addScalar("COFINS");
				sqlQuery.addScalar("Desc. Cobr");
				sqlQuery.addScalar("Base Cálculo");
				sqlQuery.addScalar("Participação");
				sqlQuery.addScalar("%BC");
				sqlQuery.addScalar("Tomador Serviço");
			}
		};
		return csq;
	}
	
	public static ConfigureSqlQuery configurePDF() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ANO");
				sqlQuery.addScalar("MES");
				sqlQuery.addScalar("SG_FILIAL_ORIGEM");
				sqlQuery.addScalar("NR_DOCTO_SERVICO");
				sqlQuery.addScalar("NM_SERVICO_ADICIONAL");
				sqlQuery.addScalar("DH_EMISSAO");
				sqlQuery.addScalar("DT_LIQUIDACAO");
				sqlQuery.addScalar("NM_MUNICIPIO");
				sqlQuery.addScalar("VL_MERCADORIA");
				sqlQuery.addScalar("VL_DOCTO_SERVICO");
				sqlQuery.addScalar("VL_ICMS");
				sqlQuery.addScalar("VL_PIS");
				sqlQuery.addScalar("VL_COFINS");
				sqlQuery.addScalar("VL_DESCONTO");
				sqlQuery.addScalar("VL_BASE_CALCULO");
				sqlQuery.addScalar("VL_PARTICIPACAO");
				sqlQuery.addScalar("PC_BC");
				sqlQuery.addScalar("TOMADOR");
			}
		};
		return csq;
	}
}
