package com.mercurio.lms.sgr.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;

public class EmitirRelatorioControleCargaSMPService extends ReportServiceSupport{
	
	private DomainValueService domainValueService;
	private FilialService filialService;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap typedFlatMap = (TypedFlatMap)parameters;
     	SqlTemplate sql = mountSql(typedFlatMap);
		final List lista = getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
		
		JRReportDataObject jr = new JRReportDataObject() {
			Map parameters = new HashMap();
	        
        	public JRDataSource getDataSource() {
        		return new JRBeanCollectionDataSource(lista);
        	}
        
        	public Map getParameters() {
        		return parameters;
        	}
        
        	public void setParameters(Map arg0) {
        		parameters = arg0;
        	}
		};
		
		Map parametersReport = new HashMap();
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		
		jr.setParameters(parametersReport);
		
        return jr;		
	}

	private SqlTemplate mountSql(TypedFlatMap map) {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("MAX(CASE CC.TP_CONTROLE_CARGA WHEN 'C' THEN 'COLETA/ENTREGA' " +
						  										         "ELSE 'VIAGEM' END)",  "tipoControle");
		sql.addProjection("CC.DH_GERACAO", "dtEmissao");
		sql.addProjection("CV.NR_IDENTIFICADOR", "cavalo");
		sql.addProjection("CT.NR_IDENTIFICADOR", "carreta");
		sql.addProjection("FLO.SG_FILIAL", "origem");
		sql.addProjection("FLD.SG_FILIAL", "destino");
		sql.addProjection("MOT.NR_IDENTIFICACAO", "cpfMotorista");
		sql.addProjection("MOT.NM_PESSOA", "nomeMotorista");
		sql.addProjection("FLO.SG_FILIAL || ' ' || CC.NR_CONTROLE_CARGA", "sgFilialNrControleCarga");
		
		sql.addProjection (" MAX(" +
							    
								"(SELECT NVL(SUM (NVL (MN.VL_TOTAL_MANIFESTO, 0)), 0)" +
							    "   FROM MANIFESTO MN " +
								"  WHERE MN.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA" +
								"    AND TP_STATUS_MANIFESTO <> 'CA') + " +
								
		                        "(SELECT NVL(SUM (NVL (PC.VL_TOTAL_VERIFICADO, PC.VL_TOTAL_INFORMADO)), 0)" +
						        "   FROM MANIFESTO_COLETA MC," +
						        "        PEDIDO_COLETA PC " +
						        "  WHERE MC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA" +
							    "    AND MC.ID_MANIFESTO_COLETA = PC.ID_MANIFESTO_COLETA " + 
							    "    AND MC.TP_STATUS_MANIFESTO_COLETA <> 'CA') )" , "vlTotal");
		
		sql.addProjection("	MAX((SELECT E.DH_EVENTO " +
						  "        FROM EVENTO_CONTROLE_CARGA E " +
						  "       WHERE E.ID_EVENTO_CONTROLE_CARGA IN (SELECT MAX(EVC.ID_EVENTO_CONTROLE_CARGA) " +
						  "	                                             FROM EVENTO_CONTROLE_CARGA EVC " + 
						  "                                             WHERE EVC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA)))", "dhEvento");
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection(
						  "	    MAX((SELECT VD.DS_VALOR_DOMINIO_I " +
						  "            FROM EVENTO_CONTROLE_CARGA E, " +
						  "		            DOMINIO DM, " +
						  "                 VALOR_DOMINIO VD " +
						  "  	      WHERE E.ID_EVENTO_CONTROLE_CARGA IN (SELECT MAX(EVC.ID_EVENTO_CONTROLE_CARGA) " +
						  "		    										 FROM EVENTO_CONTROLE_CARGA EVC " +
						  "   	                                            WHERE EVC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA) " +
						  " 		   										  AND E.TP_EVENTO_CONTROLE_CARGA = VD.VL_VALOR_DOMINIO " +
						  "	  	   										      AND VD.ID_DOMINIO = DM.ID_DOMINIO " +
						  "	   										  		  AND DM.NM_DOMINIO = 'DM_TIPO_EVENTO_CONTROLE_CARGA' ))"),  "descEvento");
		
		sql.addProjection("CC.NR_SMP" , "nrSMP");
		
		sql.addProjection(" MAX( CAST( SUBSTR( CLB_XML_RET, DBMS_LOB.INSTR(CLB_XML_RET,'<DS_MENSAGEM>', 1) + 13, " +
						  "                				   (DBMS_LOB.INSTR(CLB_XML_RET,'</DS_MENSAGEM>',1)-DBMS_LOB.INSTR(CLB_XML_RET,'<DS_MENSAGEM>',1)-13)) AS VARCHAR2(4000) ))", "msgRetorno");
		sql.addProjection(" MAX(SMP.ID_SMP)", "idSMP");
		
		sql.addFrom("CONTROLE_CARGA", "CC");
		sql.addFrom("MEIO_TRANSPORTE", "CV"); 
		sql.addFrom("MEIO_TRANSPORTE", "CT");
		sql.addFrom("FILIAL", "FLO");
		sql.addFrom("FILIAL", "FLD");
		sql.addFrom("PESSOA", "MOT");        
		sql.addFrom("SMP_GERENCIA_RISCO", "SMP");
		
		sql.addJoin("CC.ID_TRANSPORTADO", "CV.ID_MEIO_TRANSPORTE");
		sql.addJoin("CC.ID_SEMI_REBOCADO", "CT.ID_MEIO_TRANSPORTE (+)");
		sql.addJoin("CC.ID_FILIAL_ORIGEM", "FLO.ID_FILIAL");
		sql.addJoin("CC.ID_FILIAL_DESTINO", "FLD.ID_FILIAL");
		sql.addJoin("CC.ID_MOTORISTA", "MOT.ID_PESSOA (+)");
		sql.addJoin("CC.ID_CONTROLE_CARGA", "SMP.ID_CONTROLE_CARGA");
		
		sql.addCriteria("CC.TP_STATUS_CONTROLE_CARGA", "<>", "'CA'");
		
		YearMonthDay dataIni = map.getYearMonthDay("dataIni");
		if(dataIni != null) {
			sql.addCustomCriteria("TRUNC(CC.DH_GERACAO) >= TO_DATE('" + dataIni + "','YYYY-MM-DD')");	
			sql.addFilterSummary("dataIni", dataIni);
		}
		
		YearMonthDay dataFinal = map.getYearMonthDay("dataFinal");
		if(dataFinal != null) {
			sql.addCustomCriteria("TRUNC(CC.DH_GERACAO) <= TO_DATE('" + dataFinal + "','YYYY-MM-DD')");	
			sql.addFilterSummary("dataFinal", dataFinal);
		}
		
		Long idFilialResponsavel = map.getLong("idFilialResponsavel");
		if (idFilialResponsavel != null){
			sql.addCriteria("CC.ID_FILIAL_ORIGEM", "=", idFilialResponsavel);	
			Filial filial = filialService.findById(idFilialResponsavel);		
			sql.addFilterSummary("filial", filial.getSgFilial());			
		}
	
		String tpControleCarga = map.getString("tpControleCarga");
		if (tpControleCarga != null){
			sql.addCriteria("CC.TP_CONTROLE_CARGA ", "=", tpControleCarga);
			String tipoControleCarga = domainValueService.findDomainValueByValue(
					"DM_TIPO_CONTROLE_CARGAS", tpControleCarga).getDescriptionAsString();
			sql.addFilterSummary("tipo", tipoControleCarga);					
		}
		
		String tpVinculo = map.getString("tpVinculo");
		if (tpVinculo != null){
			sql.addCriteria("CV.TP_VINCULO ", "=", tpVinculo);
			String tipoVinculo = domainValueService.findDomainValueByValue(
					"DM_TIPO_VINCULO_VEICULO", tpVinculo).getDescriptionAsString();
			sql.addFilterSummary("tipoVinculo", tipoVinculo);
		}
        
		sql.addGroupBy("CC.DH_GERACAO, CV.NR_IDENTIFICADOR, CT.NR_IDENTIFICADOR, FLO.SG_FILIAL, FLD.SG_FILIAL," +
					   "MOT.NR_IDENTIFICACAO, MOT.NM_PESSOA, FLO.SG_FILIAL || ' ' || CC.NR_CONTROLE_CARGA, CC.NR_SMP");
		
		sql.addOrderBy("CC.DH_GERACAO");
		
		return sql;		
	}
	
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
}