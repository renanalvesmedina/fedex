package com.mercurio.lms.franqueados.model.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioSinteticoParticipacaoDocumentosFiscaisQuery {
	
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
    
	public static String getQuery(boolean isSimulacao){
		String query = "SELECT DECODE(DSF.TP_FRETE, 'CE', 'CO', 'FR', 'EN', 'CR', 'EN', 'FE', 'CO', DSF.TP_FRETE) AS TP_COLETA_ENTREGA_REL,"
    			+ " DS.TP_DOCUMENTO_SERVICO,"
    			+ " CO.ID_MUNICIPIO_COLETA,"
    			+ " CO.ID_MUNICIPIO_ENTREGA,"
    			+ " MO.ID_UNIDADE_FEDERATIVA AS ID_UNIDADE_COLETA,"
    			+ " MD.ID_UNIDADE_FEDERATIVA AS ID_UNIDADE_ENTREGA,"
    			+ " MP.ID_MUNICIPIO AS ID_MUNICIPIO_SEDE,"
    			+ " MP.ID_UNIDADE_FEDERATIVA AS ID_UNIDADE_SEDE,"
    			+ " DSF.VL_PARTICIPACAO"
    			+ " FROM   DOCTO_SERVICO_FRQ DSF,"
    			+ " DOCTO_SERVICO DS,"
    			+ " CONHECIMENTO CO,"
    			+ " MUNICIPIO MO,"
    			+ " MUNICIPIO MD,"
    			+ " PESSOA P,"
    			+ " ENDERECO_PESSOA EP,"
    			+ " MUNICIPIO MP"
    			+ " WHERE  DSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO"
    			+ " AND    DS.ID_DOCTO_SERVICO = CO.ID_CONHECIMENTO"
    			+ " AND    CO.ID_MUNICIPIO_COLETA = MO.ID_MUNICIPIO"
    			+ " AND    CO.ID_MUNICIPIO_ENTREGA = MD.ID_MUNICIPIO"
    			+ " AND    DSF.ID_FRANQUIA = P.ID_PESSOA"
    			+ " AND    P.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA"
    			+ " AND    EP.ID_MUNICIPIO = MP.ID_MUNICIPIO"
    			+ " AND    DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL"
    			+ " AND    DSF.DT_COMPETENCIA = :competencia "
    			+ " AND    DSF.ID_FRANQUIA = :idFilial";
		
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
				sqlQuery.addScalar("TP_COLETA_ENTREGA_REL");
				sqlQuery.addScalar("TP_DOCUMENTO_SERVICO");
				sqlQuery.addScalar("ID_MUNICIPIO_COLETA", Hibernate.LONG);
				sqlQuery.addScalar("ID_MUNICIPIO_ENTREGA", Hibernate.LONG);
				sqlQuery.addScalar("ID_UNIDADE_COLETA", Hibernate.LONG);
				sqlQuery.addScalar("ID_UNIDADE_ENTREGA", Hibernate.LONG);
				sqlQuery.addScalar("ID_MUNICIPIO_SEDE", Hibernate.LONG);
				sqlQuery.addScalar("ID_UNIDADE_SEDE", Hibernate.LONG);
				sqlQuery.addScalar("VL_PARTICIPACAO",Hibernate.DOUBLE);
			}
		};
		return csq;
	}
}
