package com.mercurio.lms.franqueados.model.report;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioAnaliticoIREQuery {

	
	private static String projection = "SELECT SG_FILIAL as \"Origem\", "+
			" DT_COMPETENCIA as \"Competência\", "+
			" NR_DOCUMENTO as \"Documento\", "+
			" VL_LANCAMENTO as \"Valor\", "+
			" DS_LANCAMENTO as \"Descrição\" ";
			
	private static String beginFrom = " FROM ( ";
	private static String endFrom = " ) x ";
	
	
	private static String query =  "SELECT F.SG_FILIAL, "+
			"  LF.DT_COMPETENCIA, "+
			"  LF.SG_DOCTO_INTERNACIONAL "+
			"  || '.' "+
			"  || LF.CD_DOCTO_INTERNACIONAL "+
			"  || '.' "+
			"  || LF.NR_DOCTO_INTERNACIONAL AS NR_DOCUMENTO, "+
			"  LF.VL_LANCAMENTO, "+
			"  LF.DS_LANCAMENTO "+
			"FROM LANCAMENTO_FRQ LF, "+
			"  CONTA_CONTABIL_FRQ CCF, "+
			"  FILIAL F "+
			"WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ "+
			"AND LF.ID_FRANQUIA             = F.ID_FILIAL "+
			"AND CCF.TP_CONTA_CONTABIL      = 'IR' "+
			"AND LF.TP_SITUACAO_PENDENCIA   = 'A' "+
			"AND LF.DT_COMPETENCIA          = :competencia ";
			
	private static String filtroFranquia = "AND    LF.ID_FRANQUIA = :idFilial "; 

	private static String order = 	"ORDER BY F.SG_FILIAL, "+
									"  LF.DT_COMPETENCIA, "+
									"  LF.SG_DOCTO_INTERNACIONAL, "+
									"  LF.CD_DOCTO_INTERNACIONAL, "+
									"  LF.NR_DOCTO_INTERNACIONAL";
							
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
				sqlQuery.addScalar("Origem");
				sqlQuery.addScalar("Competência");
				sqlQuery.addScalar("Documento");
				sqlQuery.addScalar("Valor", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("Descrição");
			}
		};
		return csq;
	}
	
	private static ConfigureSqlQuery configurePDF() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("SG_FILIAL");
				sqlQuery.addScalar("DT_COMPETENCIA");
				sqlQuery.addScalar("NR_DOCUMENTO");
				sqlQuery.addScalar("VL_LANCAMENTO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("DS_LANCAMENTO");
			}
		};
		return csq;
	}
}

