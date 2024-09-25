package com.mercurio.lms.franqueados.model.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioSinteticoParticipacaoQuery {

    private static final Map<String, String> simulacaoReplaces;
    
    static
    {
    	simulacaoReplaces = new HashMap<String, String>();
    	
    	simulacaoReplaces.put("ID_DOCTO_SERVICO_FRQ", "ID_SIMULACAO_DOC_SERV_FRQ");
    	simulacaoReplaces.put("ID_DOCTO_SERVICO_FRQ_ORIGINAL", "ID_SIMUL_DOC_SERV_FRQ_ORIGINAL");
    	simulacaoReplaces.put("DOCTO_SERVICO_FRQ", "SIMULACAO_DOC_SERV_FRQ");
    	simulacaoReplaces.put("ID_FRANQUIA", "ID_FILIAL");
    }
	
	private static final String wrapper = 	" SELECT TP_FRETE_REL as tpFreteRel, "+
										"				COUNT(*) as ctesTotal,  "+ 
										"				SUM(VL_DOCTO_SERVICO) as frete,  "+ 
										"				SUM(VL_BASE_CALCULO) as base,  "+
										"				SUM(VL_DESCONTO_LIMITADOR) as limt,  "+ 
										"				SUM(VL_PARTICIPACAO) as partic,  "+
										"				SUM(PS_AFERIDO) as peso  "+
										"	FROM (  ";
	
	private static final String wrapperEnd = 	") x GROUP BY TP_FRETE_REL ";

	private static final String query = " SELECT DSF.ID_DOCTO_SERVICO_FRQ, "
			+"        DSF.TP_FRETE, "
			+" 		  DSF.DT_COMPETENCIA, "
			+" 		  DSF.VL_MERCADORIA, "
			+" 		  DSF.VL_DOCTO_SERVICO, "
			+"        DSF.VL_ICMS, "
			+ "		  DSF.VL_PIS, "
			+" 		  DSF.VL_COFINS, "
			+" 		  DSF.VL_DESCONTO, "
			+" 		  DSF.VL_CUSTO_AEREO, "
			+"        DSF.VL_CUSTO_CARRETEIRO, "
			+" 		  DSF.VL_GENERALIDADE, "
			+" 		  DSF.VL_GRIS, "
			+"        DSF.VL_AJUSTE_BASE_NEGATIVA, "
			+" 		  DSF.VL_BASE_CALCULO, "
			+" 		  DSF.NR_KM_TRANSFERENCIA, "
			+"        DSF.VL_KM_TRANSFERENCIA, "
			+" 		  DSF.NR_KM_COLETA_ENTREGA, "
			+" 		  DSF.VL_KM_COLETA_ENTREGA,  "
			+"        DSF.VL_FIXO_COLETA_ENTREGA, "
			+" 		  DSF.VL_REPASSE_ICMS, "
			+" 		  DSF.VL_REPASSE_PIS,  "
			+"        DSF.VL_REPASSE_COFINS, "
			+" 		  DSF.VL_DESCONTO_LIMITADOR, "
			+" 		  DSF.TP_OPERACAO,  "
			+"        DSF.VL_REPASSE_GENERALIDADE, "
			+" 		  DSF.VL_PARTICIPACAO, "
			+"        DSF.VL_DIFERENCA_PARTICIPACAO, "
			+" 		  DSF.ID_MUNICIPIO_COLETA_ENTREGA,  "
			+"        DS.TP_DOCUMENTO_SERVICO, "
			+" 		  DSF.DT_COMPETENCIA, "
			+" 		  DS.PS_AFERIDO, "
			+ "		  DEV.DT_LIQUIDACAO, "
			+"        DECODE(SIGN(TO_CHAR(DSF.DT_COMPETENCIA,'YYYYMM') -  TO_CHAR(DEV.DT_LIQUIDACAO,'YYYYMM')),0,'S','N') AS BL_LIQ_NA_COMPETENCIA, "
			+"        DECODE(SIGN(DSF.VL_CUSTO_AEREO),0,DSF.TP_FRETE,'AE') AS TP_FRETE_REL, "
			+"        DECODE(DSF.TP_FRETE, 'CE', 'CO', 'FR', 'EN', 'CR', 'EN', 'FE', 'CO', DSF.TP_FRETE) AS TP_COLETA_ENTREGA_FISCAL, "
			+"        DECODE(DECODE(SIGN(DSF.VL_CUSTO_AEREO),0,DSF.TP_FRETE,'AE'), 'CE', 'CO', 'FR', 'EN', 'CR', 'EN', 'FE', 'CO', 'FL', 'FL','SE','SE','AE') AS TP_COLETA_ENTREGA_REL, "
			+"        DECODE(DECODE(SIGN(DSF.VL_CUSTO_AEREO),0,DSF.TP_FRETE,'AE'), 'CE', 1, 'FR', 2, 'CR', 3, 'FE', 4, 'AE', 5, 'FL', 6, 7) AS ORDEM, "
			+"        DECODE(DECODE(SIGN(DSF.VL_CUSTO_AEREO),0,DSF.TP_FRETE,'AE'), 'CE', 1, 'FR', 2, 'CR', 2, 'FE', 1, 'AE', 3, 'FL', 4, 5) AS ORDEM_COLETA_ENTREGA, "
			+ " CO.ID_MUNICIPIO_COLETA,"
			+ " CO.ID_MUNICIPIO_ENTREGA,"
			+ " MO.ID_UNIDADE_FEDERATIVA AS ID_UNIDADE_COLETA,"
			+ " MD.ID_UNIDADE_FEDERATIVA AS ID_UNIDADE_ENTREGA,"
			+ " MP.ID_MUNICIPIO AS ID_MUNICIPIO_SEDE,"
			+ " MP.ID_UNIDADE_FEDERATIVA AS ID_UNIDADE_SEDE"
			+" FROM   DOCTO_SERVICO_FRQ DSF, DOCTO_SERVICO DS, DEVEDOR_DOC_SERV_FAT DEV ,"
    			+ " CONHECIMENTO CO,"
    			+ " MUNICIPIO MO,"
    			+ " MUNICIPIO MD,"
    			+ " PESSOA P,"
    			+ " ENDERECO_PESSOA EP,"
    			+ " MUNICIPIO MP"
			+" WHERE  DSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO "
			+" AND    DSF.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO "
			+ " AND    DS.ID_DOCTO_SERVICO = CO.ID_CONHECIMENTO"
			+ " AND    CO.ID_MUNICIPIO_COLETA = MO.ID_MUNICIPIO"
			+ " AND    CO.ID_MUNICIPIO_ENTREGA = MD.ID_MUNICIPIO"
			+ " AND    DSF.ID_FRANQUIA = P.ID_PESSOA"
			+ " AND    P.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA"
			+ " AND    EP.ID_MUNICIPIO = MP.ID_MUNICIPIO"
			+" AND    DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL "
			+ "AND    DSF.TP_FRETE <> 'SE' "
			+ "AND    DSF.DT_COMPETENCIA = :competencia  "
			+ "AND    DSF.ID_FRANQUIA = :idFilial ";

	
	public static String getQueryOrderedByColetaEntrega(){
		return getQueryOrderedByColetaEntrega(false);
	}
	
	public static String getQueryOrderedByColetaEntregaSimulacao(){
		return getQueryOrderedByColetaEntrega(true);
	}
	
	
	private static String getQueryOrderedByColetaEntrega(boolean isSimulacao) {
		String result = query+ " ORDER BY ORDEM_COLETA_ENTREGA ";
		
		if(isSimulacao){
			result = replaceForSimulation(result);
		}

		return result;
	}
	
	public static String getQueryDefault(boolean sum){
		return getQueryDefault(sum,false);
	}
	
	public static String getQueryDefaultSimulacao(boolean sum){
		return getQueryDefault(sum,true);
	}
	
	private static String getQueryDefault(boolean sum, boolean isSimulacao) {
		String result = "";
		if(sum){
			result += wrapper;
		}
		result += query+" ORDER BY ORDEM ";
		
		if(sum){
			result += wrapperEnd; 
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

	public static ConfigureSqlQuery createConfigureSql(boolean sum) {
		if(sum){
			return configureSum();
		} else {
			return configureDefault();
		}
	}

	private static ConfigureSqlQuery configureSum() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("tpFreteRel");
				sqlQuery.addScalar("ctesTotal");
				sqlQuery.addScalar("frete");
				sqlQuery.addScalar("base");
				sqlQuery.addScalar("limt");
				sqlQuery.addScalar("peso");
				sqlQuery.addScalar("partic");
			}
		};
		return csq;
	}
	
	private static ConfigureSqlQuery configureDefault() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("TP_FRETE");
				sqlQuery.addScalar("DT_COMPETENCIA");
				sqlQuery.addScalar("VL_MERCADORIA");
				sqlQuery.addScalar("VL_DOCTO_SERVICO");
				sqlQuery.addScalar("VL_ICMS");
				sqlQuery.addScalar("VL_PIS");
				sqlQuery.addScalar("VL_COFINS");
				sqlQuery.addScalar("VL_DESCONTO");
				sqlQuery.addScalar("VL_CUSTO_AEREO");
				sqlQuery.addScalar("VL_CUSTO_CARRETEIRO");
				sqlQuery.addScalar("VL_GENERALIDADE");
				sqlQuery.addScalar("VL_GRIS");
				sqlQuery.addScalar("VL_AJUSTE_BASE_NEGATIVA");
				sqlQuery.addScalar("VL_BASE_CALCULO");
				sqlQuery.addScalar("NR_KM_TRANSFERENCIA");
				sqlQuery.addScalar("VL_KM_TRANSFERENCIA");
				sqlQuery.addScalar("NR_KM_COLETA_ENTREGA");
				sqlQuery.addScalar("VL_KM_COLETA_ENTREGA");
				sqlQuery.addScalar("VL_FIXO_COLETA_ENTREGA");
				sqlQuery.addScalar("VL_REPASSE_ICMS");
				sqlQuery.addScalar("VL_REPASSE_PIS");
				sqlQuery.addScalar("VL_REPASSE_COFINS");
				sqlQuery.addScalar("VL_DESCONTO_LIMITADOR");
				sqlQuery.addScalar("TP_OPERACAO");
				sqlQuery.addScalar("VL_REPASSE_GENERALIDADE");
				sqlQuery.addScalar("VL_PARTICIPACAO");
				sqlQuery.addScalar("VL_DIFERENCA_PARTICIPACAO");
				sqlQuery.addScalar("ID_MUNICIPIO_COLETA_ENTREGA");
				sqlQuery.addScalar("TP_DOCUMENTO_SERVICO");
				sqlQuery.addScalar("DT_COMPETENCIA");
				sqlQuery.addScalar("PS_AFERIDO");
				sqlQuery.addScalar("DT_LIQUIDACAO");
				sqlQuery.addScalar("BL_LIQ_NA_COMPETENCIA");
				sqlQuery.addScalar("TP_FRETE_REL");
				sqlQuery.addScalar("TP_COLETA_ENTREGA_REL");
				sqlQuery.addScalar("TP_COLETA_ENTREGA_FISCAL");				
				sqlQuery.addScalar("ORDEM");
				sqlQuery.addScalar("ORDEM_COLETA_ENTREGA");				
				sqlQuery.addScalar("ID_MUNICIPIO_COLETA", Hibernate.LONG);
				sqlQuery.addScalar("ID_MUNICIPIO_ENTREGA", Hibernate.LONG);
				sqlQuery.addScalar("ID_UNIDADE_COLETA", Hibernate.LONG);
				sqlQuery.addScalar("ID_UNIDADE_ENTREGA", Hibernate.LONG);
				sqlQuery.addScalar("ID_MUNICIPIO_SEDE", Hibernate.LONG);
				sqlQuery.addScalar("ID_UNIDADE_SEDE", Hibernate.LONG);
			}
		};
		return csq;
	}
}
