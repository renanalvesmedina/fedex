package com.mercurio.lms.franqueados.model.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioAnaliticoDocumentosQuery {
	
    private static final Map<String, String> simulacaoReplaces;
    
    static
    {
    	simulacaoReplaces = new HashMap<String, String>();
    	
    	simulacaoReplaces.put("ID_DOCTO_SERVICO_FRQ", "ID_SIMULACAO_DOC_SERV_FRQ");
    	simulacaoReplaces.put("ID_DOCTO_SERVICO_FRQ_ORIGINAL", "ID_SIMUL_DOC_SERV_FRQ_ORIGINAL");
    	simulacaoReplaces.put("DOCTO_SERVICO_FRQ", "SIMULACAO_DOC_SERV_FRQ");
    	simulacaoReplaces.put("ID_FRANQUIA", "ID_FILIAL");
    }
	
	private static final String query = "SELECT DSF.ID_DOCTO_SERVICO, "
			+ "       DSF.TP_FRETE, DSF.VL_MERCADORIA, DSF.VL_DOCTO_SERVICO,"
			+ "       DSF.VL_ICMS, DSF.VL_PIS, DSF.VL_COFINS, DSF.VL_DESCONTO, DSF.VL_CUSTO_AEREO,"
			+ "       DSF.VL_CUSTO_CARRETEIRO, DSF.VL_GENERALIDADE, DSF.VL_GRIS,"
			+ "       DSF.VL_AJUSTE_BASE_NEGATIVA, DSF.VL_BASE_CALCULO, DSF.NR_KM_TRANSFERENCIA,"
			+ "       DSF.VL_KM_TRANSFERENCIA, DSF.NR_KM_COLETA_ENTREGA, DSF.VL_KM_COLETA_ENTREGA, "
			+ "       DSF.VL_FIXO_COLETA_ENTREGA, DSF.VL_REPASSE_ICMS, DSF.VL_REPASSE_PIS, "
			+ "       DSF.VL_REPASSE_COFINS, DSF.VL_DESCONTO_LIMITADOR, DSF.TP_OPERACAO, "
			+ "       DSF.VL_REPASSE_GENERALIDADE, DSF.VL_PARTICIPACAO, "
			+ "       DSF.VL_DIFERENCA_PARTICIPACAO, DSF.ID_MUNICIPIO_COLETA_ENTREGA, "
			+ "       DS.TP_DOCUMENTO_SERVICO, DS.NR_DOCTO_SERVICO, FDS.SG_FILIAL, FD.SG_FILIAL AS SG_FILIAL_DEST,"
			+ "      	TO_CHAR(DS.DH_EMISSAO,'DD/MM/YY') AS DH_EMISSAO, DS.PS_AFERIDO, TO_CHAR(DEV.DT_LIQUIDACAO,'DD/MM/YY') AS DT_LIQUIDACAO, M.NM_MUNICIPIO,"
			+ "       DECODE(DSF.TP_FRETE, 'CE', 'CIF', 'CR', 'CIF', 'FE', 'FOB', 'FR', 'FOB', 'CIF') AS TP_FRETE_DOC,"
			+ "       DECODE(SIGN(TO_CHAR(DSF.DT_COMPETENCIA,'YYYYMM') -"
			+ "                   TO_CHAR(DEV.DT_LIQUIDACAO,'YYYYMM')),0,'S','N') AS BL_LIQ_NA_COMPETENCIA,"
			+ "       DECODE(SIGN(DSF.VL_CUSTO_AEREO),0,DSF.TP_FRETE,'AE') AS TP_FRETE_REL,"
			+ "       DECODE(DECODE(SIGN(DSF.VL_CUSTO_AEREO),0,DSF.TP_FRETE,'AE'), 'CE', 1, 'FR', 2, 'CR', 3, 'FE', 4, 'AE', 5, 6) AS ORDEM "
			+ "FROM   DOCTO_SERVICO_FRQ DSF, "
			+ "       DOCTO_SERVICO DS, "
			+ "       DEVEDOR_DOC_SERV_FAT DEV, "
			+ "       FILIAL FDS, "
			+ "       FILIAL FD, "
			+ "       MUNICIPIO M "
			+ "WHERE  DSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO "
			+ "AND    DS.ID_FILIAL_ORIGEM = FDS.ID_FILIAL "
			+ "AND    DS.ID_FILIAL_DESTINO = FD.ID_FILIAL "
			+ "AND    DSF.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO "
			+ "AND    DSF.ID_MUNICIPIO_COLETA_ENTREGA = M.ID_MUNICIPIO "
			+ "AND    DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL "
			+ "AND    DSF.TP_FRETE NOT IN ('SE','FL') "
			+ "AND    DSF.DT_COMPETENCIA = :competencia ";
	
	private static final String filtroFranquia = "AND    DSF.ID_FRANQUIA = :idFilial ";
		
	private static final String order = "ORDER BY ORDEM, DSF.VL_DOCTO_SERVICO DESC";
	
	private static final String queryCsv = "SELECT TO_CHAR(DSF.DT_COMPETENCIA, 'YYYY') AS \"Ano\", "												
			+ "   TO_CHAR(DSF.DT_COMPETENCIA, 'MON')       AS \"Mês\", "
			+ "   ( "
			+ "   CASE "
			+ "     WHEN TO_CHAR(DSF.DT_COMPETENCIA, 'MMDD') <= '0331' "
			+ "     THEN 'Q1' "
			+ "     WHEN TO_CHAR(DSF.DT_COMPETENCIA, 'MMDD') <= '0630' "
			+ "     THEN 'Q2' "
			+ "     WHEN TO_CHAR(DSF.DT_COMPETENCIA, 'MMDD') <= '0930' "
			+ "     THEN 'Q3' "
			+ "     ELSE 'Q4' "
			+ "   END ) AS \"Qter\", "
			+ "   F.SG_FILIAL AS \"FRQ\", "
			+ "   MP.NM_MUNICIPIO AS \"Sede\", "
			+ "   UFP.SG_UNIDADE_FEDERATIVA AS \"UF\", "
			+ "   TO_CHAR(DS.DH_EMISSAO, 'DD/MM/YYYY') AS \"Dt emissão\", "
			+ "   TO_CHAR(DEV.DT_LIQUIDACAO, 'DD/MM/YYYY') AS \"Dt liquid.\", "
			+ "   UFO.SG_UNIDADE_FEDERATIVA AS \"UF origem\", "
			+ "   FO.SG_FILIAL AS \"Origem\", "
			+ "   MO.NM_MUNICIPIO AS \"Munic. coleta\", "
			+ "   UFD.SG_UNIDADE_FEDERATIVA AS \"UF Dest.\", "
			+ "   FD.SG_FILIAL AS \"Destino\", "
			+ "   MD.NM_MUNICIPIO AS \"Munic. entrega\", "
			+ "   MCE.NM_MUNICIPIO AS \"Município FRQ\", "
			+ "   DECODE(DSF.TP_FRETE, 'CE', 'COL', 'FR', 'ENT', 'CR', 'ENT', 'FE', 'COL', 'FL', 'LOCAL', 'SE', 'SERV') AS \"Col/Ent\", "
			+ "   DECODE(DSF.TP_FRETE, 'CE', 'CIF', 'CR', 'CIF', 'FE', 'FOB', 'FR', 'FOB', 'CIF') AS \"Tp Frete\", "
			+ "   DECODE(DSF.TP_FRETE, 'CE', 'CIF Exp', 'CR', 'CIF Rec', 'FE', 'FOB Exp', 'FR', 'FOB Rec', 'FL', 'Frete Local', "
			+ "	  'Serviço Adicional') AS \"Tp Frete I\", "
			+ "   DECODE(DECODE(SIGN(DSF.VL_CUSTO_AEREO),0,DSF.TP_FRETE,'AE'), 'CE', 'CIF Exp', 'FR', 'FOB Rec', 'CR', 'CIF Rec', "
			+ "	  'FE', 'FOB Exp', 'FL', 'Frete Local', 'SE', 'Serviço Adicional', 'Aéreo') AS \"Tp Frete II\", "
			+ "   DECODE(DSF.TP_FRETE, 'CE', 'DEC', 'FR', 'DEC', 'FL', 'DEC', 'CR', 'NDEC', 'FE', 'NDEC', 'SE', 'SERV') AS \"Decisor\", "
			+ "   DS.TP_DOCUMENTO_SERVICO AS \"Tipo\", "
			+ "   DS.NR_DOCTO_SERVICO AS \"Nr. Documento\", "
			+ "   (SELECT nvl(MIN(FCC.SG_FILIAL "
			+ "     || ' ' "
			+ "     || CC.NR_CONTROLE_CARGA "
			+ "     || '§' "
			+ "     || FME.SG_FILIAL "
			+ "     || ' ' "
			+ "     || ME.NR_MANIFESTO_ENTREGA "
			+ "     || '§' "
			+ "     || ROT.NR_ROTA "
			+ "     || '§' "
			+ "     || To_CHAR(DS.DT_PREV_ENTREGA,'DD/MM/YYYY') "
			+ "     || '§' "
			+ "     || TO_CHAR(MED.DH_OCORRENCIA,'DD/MM/YYYY')), '§§§§') "
			+ "   FROM MANIFESTO_ENTREGA ME, "
			+ "     MANIFESTO_ENTREGA_DOCUMENTO MED, "
			+ "     CONTROLE_CARGA CC, "
			+ "     DOCTO_SERVICO DS, "
			+ "     FILIAL FCC, "
			+ "     MANIFESTO M, "
			+ "     ROTA_COLETA_ENTREGA ROT, "
			+ "     FILIAL FME "
			+ "   WHERE ME.ID_MANIFESTO_ENTREGA = MED.ID_MANIFESTO_ENTREGA "
			+ "   AND MED.ID_DOCTO_SERVICO      = DSF.ID_DOCTO_SERVICO "
			+ "   AND MED.ID_DOCTO_SERVICO      = DS.ID_DOCTO_SERVICO "
			+ "   AND ME.ID_MANIFESTO_ENTREGA   = M.ID_MANIFESTO "
			+ "   AND M.ID_CONTROLE_CARGA       = CC.ID_CONTROLE_CARGA "
			+ "   AND CC.ID_ROTA_COLETA_ENTREGA = ROT.ID_ROTA_COLETA_ENTREGA "
			+ "   AND CC.ID_FILIAL_ORIGEM       = FCC.ID_FILIAL "
			+ "   AND ME.ID_FILIAL              = FME.ID_FILIAL "
			+ "   AND MED.ID_OCORRENCIA_ENTREGA = 5 "
			+ "   ) AS \"DADOS_ENTREGA\", "
			+ "   DSF.VL_MERCADORIA AS \"Vlr Mercadoria\", "
			+ "   DS.QT_VOLUMES AS \"Qtd Volume\", "
			+ "   DS.PS_REAL AS \"Peso\", "
			+ "   ROUND(DSF.VL_DOCTO_SERVICO / DSF.VL_MERCADORIA, 2) AS \"RPK\", "
			+ "   DSF.VL_DOCTO_SERVICO AS \"Frete\","
			+ "   DSF.VL_ICMS AS \"ICMS\", "
			+ "   DSF.VL_PIS AS \"PIS\", "
			+ "   DSF.VL_COFINS AS \"COFINS\", "
			+ "   DSF.VL_DESCONTO AS \"Desc. Cobr\", "
			+ "   DSF.VL_CUSTO_AEREO AS \"Custo Aéreo\", "
			+ "   DSF.VL_CUSTO_CARRETEIRO AS \"Frete Carret.\", "
			+ "   DSF.VL_GENERALIDADE AS \"Generalidade\", "
			+ "   DSF.VL_GRIS AS \"$GRIS\", "
			+ "   DSF.VL_AJUSTE_BASE_NEGATIVA AS \"BC < 0\", "
			+ "   DSF.VL_BASE_CALCULO AS \"Base de cálculo\", "
			+ "   DSF.NR_KM_TRANSFERENCIA AS \"Distância de transferencia\",  "
			+ "   DSF.VL_KM_TRANSFERENCIA AS \"Valor transferência\", "
			+ "   DSF.NR_KM_COLETA_ENTREGA AS \"Distância de coleta/entrega\", "
			+ "   DSF.VL_KM_COLETA_ENTREGA AS \"Valor coleta/entrega\", "
			+ "   DECODE(DSF.TP_FRETE, 'FL', VL_PARTICIPACAO, 0) AS \"Valor frete local\", "
			+ "   DSF.VL_KM_TRANSFERENCIA + DSF.VL_KM_COLETA_ENTREGA + DECODE(DSF.TP_FRETE, 'FL', VL_PARTICIPACAO, 0) AS \"Participação básica\", "
			+ "   DSF.VL_FIXO_COLETA_ENTREGA  AS \"Fixo CTRC\", "
			+ "   DSF.VL_KM_TRANSFERENCIA + DSF.VL_KM_COLETA_ENTREGA + DECODE(DSF.TP_FRETE, 'FL', VL_PARTICIPACAO, 0) + DSF.VL_FIXO_COLETA_ENTREGA "
			+ "	  AS \"Participação Base\", "
			+ "   DSF.VL_REPASSE_ICMS AS \"Repasse ICMS\", "
			+ "   DSF.VL_REPASSE_PIS AS \"Repasse PIS\", "
			+ "   DSF.VL_REPASSE_COFINS AS \"Repasse COFINS\", "
			+ "   DSF.VL_KM_TRANSFERENCIA + DSF.VL_KM_COLETA_ENTREGA + DECODE(DSF.TP_FRETE, 'FL', VL_PARTICIPACAO, 0) + DSF.VL_FIXO_COLETA_ENTREGA + "
			+ "DSF.VL_REPASSE_ICMS + DSF.VL_REPASSE_PIS + DSF.VL_REPASSE_COFINS AS \"Participaçao\", "
			+ "   DSF.VL_DESCONTO_LIMITADOR AS \"Desconto limitador\", "
			+ "   DSF.VL_KM_TRANSFERENCIA + DSF.VL_KM_COLETA_ENTREGA + DECODE(DSF.TP_FRETE, 'FL', VL_PARTICIPACAO, 0) + DSF.VL_FIXO_COLETA_ENTREGA + " 
			+ " DSF.VL_REPASSE_ICMS + DSF.VL_REPASSE_PIS + DSF.VL_REPASSE_COFINS - DSF.VL_DESCONTO_LIMITADOR AS \"Participação total\", "
			+ "   DSF.VL_REPASSE_GENERALIDADE AS \"Repasse generalidade\","
			+ "   DSF.VL_PARTICIPACAO AS \"Participação Final\", "
			+ "   DECODE(DSF.TP_OPERACAO,'NA','Nacional', 'Entre franquias') AS \"Tipo de operação\", "
			+ "   VI18N(SMR.DS_SEGMENTO_MERCADO_I) AS \"Segmento Remetente\", "
			+ "   REM.NR_IDENTIFICACAO AS \"CPF/CNPJ remetente\", "
			+ "   REM.NM_PESSOA AS \"Remetente\", "
			+ "   VI18N(SMD.DS_SEGMENTO_MERCADO_I) AS \"Segmento Destinatário\", "
			+ "   DEST.NR_IDENTIFICACAO AS \"CPF/CNPJ destinatário\", "
			+ "   DEST.NM_PESSOA AS \"Destinatário\", "
			+ "   RESP.NR_IDENTIFICACAO AS \"CPF/CNPJ Resp. Frete\", "
			+ "   RESP.NM_PESSOA AS \"Resp. Frete\" "
			+ " FROM FILIAL F, "
			+ "   FILIAL FO, "
			+ "   FILIAL FD, "
			+ "   DOCTO_SERVICO_FRQ DSF, "
			+ "   DOCTO_SERVICO DS, "
			+ "   CONHECIMENTO CO, "
			+ "   MUNICIPIO MO, "
			+ "   MUNICIPIO MD, "
			+ "   MUNICIPIO MCE, "
			+ "   UNIDADE_FEDERATIVA UFO, "
			+ "   UNIDADE_FEDERATIVA UFD, "
			+ "   PESSOA P, "
			+ "   ENDERECO_PESSOA EP, "
			+ "   MUNICIPIO MP, "
			+ "   UNIDADE_FEDERATIVA UFP, "
			+ "   DEVEDOR_DOC_SERV_FAT DEV, "
			+ "   PESSOA REM, "
			+ "   PESSOA DEST, "
			+ "   PESSOA RESP, "
			+ "   CLIENTE CR, "
			+ "   CLIENTE CD, "
			+ "   SEGMENTO_MERCADO SMR, "
			+ "   SEGMENTO_MERCADO SMD "
			+ " WHERE DSF.ID_DOCTO_SERVICO          = DS.ID_DOCTO_SERVICO "
			+ " AND DSF.ID_DOCTO_SERVICO            = DEV.ID_DOCTO_SERVICO "
			+ " AND DS.ID_FILIAL_ORIGEM             = FO.ID_FILIAL "
			+ " AND DS.ID_FILIAL_DESTINO            = FD.ID_FILIAL "
			+ " AND DEV.ID_CLIENTE            		= RESP.ID_PESSOA "
			+ " AND DS.ID_DOCTO_SERVICO             = CO.ID_CONHECIMENTO (+) "
			+ " AND CO.ID_MUNICIPIO_COLETA          = MO.ID_MUNICIPIO (+) "
			+ " AND CO.ID_MUNICIPIO_ENTREGA         = MD.ID_MUNICIPIO (+) "
			+ " AND MO.ID_UNIDADE_FEDERATIVA        = UFO.ID_UNIDADE_FEDERATIVA(+) "
			+ " AND MD.ID_UNIDADE_FEDERATIVA        = UFD.ID_UNIDADE_FEDERATIVA(+) "
			+ " AND DS.ID_CLIENTE_REMETENTE         = REM.ID_PESSOA (+) "
			+ " AND DS.ID_CLIENTE_REMETENTE         = CR.ID_CLIENTE (+) "
			+ " AND CR.ID_SEGMENTO_MERCADO          = SMR.ID_SEGMENTO_MERCADO (+) "
			+ " AND DS.ID_CLIENTE_DESTINATARIO      = DEST.ID_PESSOA (+) "
			+ " AND DS.ID_CLIENTE_DESTINATARIO      = CD.ID_CLIENTE (+) "
			+ " AND CD.ID_SEGMENTO_MERCADO          = SMD.ID_SEGMENTO_MERCADO (+) "
			+ " AND DSF.ID_FRANQUIA                 = F.ID_FILIAL "
			+ " AND DSF.ID_FRANQUIA                 = P.ID_PESSOA "
			+ " AND P.ID_ENDERECO_PESSOA            = EP.ID_ENDERECO_PESSOA "
			+ " AND EP.ID_MUNICIPIO                 = MP.ID_MUNICIPIO "
			+ " AND MP.ID_UNIDADE_FEDERATIVA        = UFP.ID_UNIDADE_FEDERATIVA "
			+ " AND DSF.ID_MUNICIPIO_COLETA_ENTREGA = MCE.ID_MUNICIPIO (+) "
			+ " AND DSF.DT_COMPETENCIA              = :competencia ";
	
	public static String getQuery(boolean filtraFranquia, boolean isCSV){
		return getQuery(filtraFranquia, isCSV, false);
	}
	
	public static String getQuerySimulacao(boolean filtraFranquia, boolean isCSV){
		return getQuery(filtraFranquia, isCSV, true);
	}
	
	private static String getQuery(boolean filtraFranquia, boolean isCSV, boolean isSimulacao){
		String result = isCSV ? queryCsv : query;
		if(filtraFranquia == true){
			result += filtroFranquia;
		}
		if(!isCSV){
			result += order;
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
		
		if(!isCSV){
			final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
				public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("ID_DOCTO_SERVICO");
					sqlQuery.addScalar("NM_MUNICIPIO");
					sqlQuery.addScalar("TP_FRETE");
					sqlQuery.addScalar("VL_MERCADORIA", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_DOCTO_SERVICO", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_ICMS", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_PIS", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_COFINS", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_DESCONTO", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_CUSTO_AEREO", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_CUSTO_CARRETEIRO", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_GENERALIDADE", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_GRIS", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_AJUSTE_BASE_NEGATIVA", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_BASE_CALCULO", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("NR_KM_TRANSFERENCIA");
					sqlQuery.addScalar("VL_KM_TRANSFERENCIA", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("NR_KM_COLETA_ENTREGA");
					sqlQuery.addScalar("VL_KM_COLETA_ENTREGA", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_FIXO_COLETA_ENTREGA", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_REPASSE_ICMS", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_REPASSE_PIS", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_REPASSE_COFINS", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_DESCONTO_LIMITADOR", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("TP_OPERACAO");
					sqlQuery.addScalar("VL_REPASSE_GENERALIDADE", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_PARTICIPACAO", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("VL_DIFERENCA_PARTICIPACAO", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("ID_MUNICIPIO_COLETA_ENTREGA");
					sqlQuery.addScalar("TP_DOCUMENTO_SERVICO");
					sqlQuery.addScalar("NR_DOCTO_SERVICO");
					sqlQuery.addScalar("SG_FILIAL");
					sqlQuery.addScalar("SG_FILIAL_DEST");
					sqlQuery.addScalar("DH_EMISSAO");
					sqlQuery.addScalar("DT_LIQUIDACAO");
					sqlQuery.addScalar("TP_FRETE_DOC");
					sqlQuery.addScalar("BL_LIQ_NA_COMPETENCIA");
					sqlQuery.addScalar("TP_FRETE_REL");
					sqlQuery.addScalar("ORDEM");
				}
			};
			return csq;
		} else {
			final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
				public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("Ano");				
					sqlQuery.addScalar("Mês");               
					sqlQuery.addScalar("Qter", Hibernate.STRING);
					sqlQuery.addScalar("FRQ");               
					sqlQuery.addScalar("Sede");              
					sqlQuery.addScalar("UF");                
					sqlQuery.addScalar("Dt emissão");        
					sqlQuery.addScalar("Dt liquid.");        
					sqlQuery.addScalar("UF origem");         
					sqlQuery.addScalar("Origem");            
					sqlQuery.addScalar("Munic. coleta");     
					sqlQuery.addScalar("UF Dest.");          
					sqlQuery.addScalar("Destino");           
					sqlQuery.addScalar("Munic. entrega");    
					sqlQuery.addScalar("Município FRQ");     
					sqlQuery.addScalar("Col/Ent");           
					sqlQuery.addScalar("Tp Frete");          
					sqlQuery.addScalar("Tp Frete I");        
					sqlQuery.addScalar("Tp Frete II");       
					sqlQuery.addScalar("Decisor");           
					sqlQuery.addScalar("Tipo");              
					sqlQuery.addScalar("Nr. Documento", Hibernate.STRING);
					sqlQuery.addScalar("DADOS_ENTREGA");
					sqlQuery.addScalar("Vlr Mercadoria", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Qtd Volume", Hibernate.BIG_DECIMAL);                 
					sqlQuery.addScalar("Peso", Hibernate.BIG_DECIMAL);                       
					sqlQuery.addScalar("RPK", Hibernate.BIG_DECIMAL);                        
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
					sqlQuery.addScalar("Tipo de operação");           
					sqlQuery.addScalar("Segmento Remetente");         
					sqlQuery.addScalar("CPF/CNPJ remetente");         
					sqlQuery.addScalar("Remetente");                  
					sqlQuery.addScalar("Segmento Destinatário");      
					sqlQuery.addScalar("CPF/CNPJ destinatário");      
					sqlQuery.addScalar("Destinatário");  
					sqlQuery.addScalar("CPF/CNPJ Resp. Frete");      
					sqlQuery.addScalar("Resp. Frete");
					
				}
			};
			return csq;
		}
	}
	
}
