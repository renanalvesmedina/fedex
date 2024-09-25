package com.mercurio.lms.franqueados.model.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioSinteticoParticipacaoServicoAdicionalQuery {
	
    private static final Map<String, String> simulacaoReplaces;
    
    static
    {
    	simulacaoReplaces = new HashMap<String, String>();
    	
    	simulacaoReplaces.put("ID_DOCTO_SERVICO_FRQ", "ID_SIMULACAO_DOC_SERV_FRQ");
    	simulacaoReplaces.put("ID_DOCTO_SERVICO_FRQ_ORIGINAL", "ID_SIMUL_DOC_SERV_FRQ_ORIGINAL");
    	simulacaoReplaces.put("DOCTO_SERVICO_FRQ", "SIMULACAO_DOC_SERV_FRQ");
    	simulacaoReplaces.put("ID_FRANQUIA", "ID_FILIAL");
    }
    
	public static String getQuery(){
		return getQuery(false);
	}
	
	public static String getQuerySimulacao(){
		return getQuery(true);
	}

	private static String getQuery(boolean isSimulacao){
		String query = " SELECT VI18N(DS_SERVICO_ADICIONAL_I) AS DS_SERVICO_ADICIONAL_I, "
				+" 			COUNT(*) AS NR_TOTAL_NF, "
				+" 			SUM(VL_PARTICIPACAO) AS VL_TOTAL_SERVICO, "
				+" 			SUM(VL_PARTICIPACAO) / COUNT(*) AS VL_MEDIA "
				+" FROM		DOCTO_SERVICO_FRQ DSF, "
				+"       	SERV_ADICIONAL_DOC_SERV SADS, "
				+"        	SERVICO_ADICIONAL SA "
				+" WHERE  	DSF.ID_DOCTO_SERVICO = SADS.ID_DOCTO_SERVICO "
				+" AND    	SADS.ID_SERVICO_ADICIONAL = SA.ID_SERVICO_ADICIONAL "
				+" AND    	DSF.DT_COMPETENCIA = :competencia "
				+" AND    	DSF.ID_FRANQUIA = :idFilial "
				+ "AND    	DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL "
				+" GROUP BY DS_SERVICO_ADICIONAL_I ";
		
		if(isSimulacao){
			query = replaceForSimulation(query);
		}

		return query;
	}
	
	private static String replaceForSimulation(String result) {
		for (Iterator<String> iterator = simulacaoReplaces.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			result = result.replaceAll(key, simulacaoReplaces.get(key));
		}
		return result;
	}
	
	public static ConfigureSqlQuery createConfigureSql() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("DS_SERVICO_ADICIONAL_I");
				sqlQuery.addScalar("NR_TOTAL_NF");
				sqlQuery.addScalar("VL_TOTAL_SERVICO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_MEDIA", Hibernate.BIG_DECIMAL);
			}
		};
		return csq;
	}
}
