package com.mercurio.lms.entrega.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * Classe responsável pela geração do Relatório de Entrega dos Parceiros
 * 
 * @author Tairone Lopes
 * 
 * @spring.bean id="lms.entrega.emitirEntregaParceirosService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirEntregaParceiros.jasper"
 */
public class EmitirEntregaParceirosService extends ReportServiceSupport{

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		 SqlTemplate sql = getSql(parameters);
	     JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
	     
	     Map parametersReport = new HashMap();
		 parametersReport.put("PARAMETROS_PESQUISA", sql.getFilterSummary());
         parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
         parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio").toString());
		 jr.setParameters(parametersReport);
		return jr;
	}

	 private SqlTemplate getSql(Map parameters) {
		  
		 /**

SELECT FCC.SG_FILIAL AS SG_FILIAL_CC,
       CC.NR_CONTROLE_CARGA AS NR_CONTROLE_CARGA,
       FME.SG_FILIAL AS SG_FILIAL_ME,
       ME.NR_MANIFESTO_ENTREGA AS NR_MANIFESTO_ENTREGA,
       DS.TP_DOCUMENTO_SERVICO AS TP_DOCUMENTO_SERVICO,
       FDS.SG_FILIAL AS SG_FILIAL,
       DS.NR_DOCTO_SERVICO AS NR_DOCTO_SERVICO,
       MIN(NF.NR_NOTA_FISCAL) AS NR_NOTA_FISCAL,
       PS.TP_IDENTIFICACAO AS TP_IDENTIFICACAO,
       PS.NR_IDENTIFICACAO AS NR_IDENTIFICACAO,
       PS.NM_PESSOA AS NM_PESSOA,
       DS.DS_ENDERECO_ENTREGA_REAL AS DS_ENDERECO_ENTREGA_REAL,
       to_char(ME.DH_EMISSAO, 'dd/MM/yyyy HH24:MI TZR') AS DH_EMISSAO, 
       to_char(DS.DT_PREV_ENTREGA, 'dd/MM/yyyy') AS DT_PREV_ENTREGA,
       to_char(MED.DH_OCORRENCIA, 'dd/MM/yyyy HH24:MI TZR') AS DH_OCORRENCIA,
       OE.CD_OCORRENCIA_ENTREGA AS CD_OCORRENCIA_ENTREGA,
       OE.DS_OCORRENCIA_ENTREGA_I AS DS_OCORRENCIA_ENTREGA_I 
  FROM DOCTO_SERVICO DS,       
       FILIAL FDS,
       PESSOA PS,
       MANIFESTO_ENTREGA_DOCUMENTO MED, 
       CONHECIMENTO C,
       MANIFESTO_ENTREGA ME,
       FILIAL FME,
       OCORRENCIA_ENTREGA OE,
       MANIFESTO M,
       CONTROLE_CARGA CC,       
       NOTA_FISCAL_CONHECIMENTO NF,
       FILIAL FCC
 WHERE DS.ID_FILIAL_ORIGEM = FDS.ID_FILIAL
   AND DS.ID_CLIENTE_REMETENTE = PS.ID_PESSOA
   AND DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO
   AND DS.ID_DOCTO_SERVICO = C.ID_CONHECIMENTO
   AND MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA
   AND ME.ID_FILIAL = FME.ID_FILIAL
   AND MED.ID_OCORRENCIA_ENTREGA = OE.ID_OCORRENCIA_ENTREGA
   AND ME.ID_MANIFESTO_ENTREGA = M.ID_MANIFESTO
   AND M.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA
   AND C.ID_CONHECIMENTO = NF.ID_CONHECIMENTO
   AND CC.ID_FILIAL_ORIGEM = FCC.ID_FILIAL
   AND M.TP_MANIFESTO_ENTREGA IN ('EP')
   AND CC.TP_CONTROLE_CARGA = 'C'
   and C.ID_CONHECIMENTO = 6175250   
 GROUP BY FCC.SG_FILIAL,
       CC.NR_CONTROLE_CARGA,
       FME.SG_FILIAL,
       ME.NR_MANIFESTO_ENTREGA,
       DS.TP_DOCUMENTO_SERVICO,
       FDS.SG_FILIAL,
       DS.NR_DOCTO_SERVICO,
       PS.TP_IDENTIFICACAO,
       PS.NR_IDENTIFICACAO,
       PS.NM_PESSOA,
       DS.DS_ENDERECO_ENTREGA_REAL,
       to_char(ME.DH_EMISSAO, 'dd/MM/yyyy HH24:MI TZR'), 
       to_char(DS.DT_PREV_ENTREGA, 'dd/MM/yyyy'),
       to_char(MED.DH_OCORRENCIA, 'dd/MM/yyyy HH24:MI TZR'),
       OE.CD_OCORRENCIA_ENTREGA,
       OE.DS_OCORRENCIA_ENTREGA_I
 ORDER BY FCC.SG_FILIAL,
       CC.NR_CONTROLE_CARGA,
       FME.SG_FILIAL,
       ME.NR_MANIFESTO_ENTREGA,
       DS.TP_DOCUMENTO_SERVICO,
       FDS.SG_FILIAL,
       DS.NR_DOCTO_SERVICO,
       OE.DS_OCORRENCIA_ENTREGA_I

		  */
	    	SqlTemplate sql = createSqlTemplate();
	    	
	    	//projection...
	    	sql.addProjection("FCC.SG_FILIAL", "SG_FILIAL_CC");
	    	sql.addProjection("CC.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
	    	sql.addProjection("FME.SG_FILIAL", "SG_FILIAL_ME");
	    	sql.addProjection("ME.NR_MANIFESTO_ENTREGA", "NR_MANIFESTO_ENTREGA");
	    	sql.addProjection("DS.TP_DOCUMENTO_SERVICO", "TP_DOCUMENTO_SERVICO");
	    	sql.addProjection("FDS.SG_FILIAL", "SG_FILIAL");
	    	sql.addProjection("DS.NR_DOCTO_SERVICO", "NR_DOCTO_SERVICO");
	    	sql.addProjection("MIN(NF.NR_NOTA_FISCAL)", "NR_NOTA_FISCAL");
	    	sql.addProjection("PS.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
	    	sql.addProjection("PS.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
	    	sql.addProjection("PS.NM_PESSOA", "NM_PESSOA");
	    	sql.addProjection("DS.DS_ENDERECO_ENTREGA_REAL", "DS_ENDERECO_ENTREGA_REAL");
	    	sql.addProjection("to_char(ME.DH_EMISSAO, 'dd/MM/yyyy HH24:MI TZR')", "DH_EMISSAO"); 
	    	sql.addProjection("to_char(DS.DT_PREV_ENTREGA, 'dd/MM/yyyy')", "DT_PREV_ENTREGA");
	    	sql.addProjection("to_char(MED.DH_OCORRENCIA, 'dd/MM/yyyy HH24:MI TZR')", "DH_OCORRENCIA");
	    	sql.addProjection("OE.CD_OCORRENCIA_ENTREGA", "CD_OCORRENCIA_ENTREGA");
	    	sql.addProjection("OE.DS_OCORRENCIA_ENTREGA_I", "DS_OCORRENCIA_ENTREGA_I");
			
	    	//from...
	    	sql.addFrom("DOCTO_SERVICO", "DS");
	    	sql.addFrom("FILIAL", "FDS");
	    	sql.addFrom("PESSOA", "PS");
	    	sql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO", "MED");
	    	sql.addFrom("CONHECIMENTO", "C");
	    	sql.addFrom("MANIFESTO_ENTREGA", "ME");
	    	sql.addFrom("FILIAL", "FME");
	    	sql.addFrom("OCORRENCIA_ENTREGA", "OE");
	    	sql.addFrom("MANIFESTO", "M");
	    	sql.addFrom("CONTROLE_CARGA", "CC");
	    	sql.addFrom("NOTA_FISCAL_CONHECIMENTO", "NF");
	    	sql.addFrom("FILIAL", "FCC");
	    	
	    	//join...
	    	sql.addJoin("DS.ID_FILIAL_ORIGEM", "FDS.ID_FILIAL");
	    	sql.addJoin("DS.ID_CLIENTE_DESTINATARIO", "PS.ID_PESSOA");
	    	sql.addJoin("DS.ID_DOCTO_SERVICO", "MED.ID_DOCTO_SERVICO");
	    	sql.addJoin("DS.ID_DOCTO_SERVICO", "C.ID_CONHECIMENTO");
	    	sql.addJoin("MED.ID_MANIFESTO_ENTREGA", "ME.ID_MANIFESTO_ENTREGA");
	    	sql.addJoin("ME.ID_FILIAL", "FME.ID_FILIAL");
	    	sql.addJoin("MED.ID_OCORRENCIA_ENTREGA", "OE.ID_OCORRENCIA_ENTREGA(+)");
	    	sql.addJoin("ME.ID_MANIFESTO_ENTREGA", "M.ID_MANIFESTO");
	    	sql.addJoin("M.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA");
	    	sql.addJoin("C.ID_CONHECIMENTO", "NF.ID_CONHECIMENTO");
	    	sql.addJoin("CC.ID_FILIAL_ORIGEM", "FCC.ID_FILIAL");
	    	
	    	//Criteria...

	    	sql.addCustomCriteria("M.TP_MANIFESTO_ENTREGA IN ('EP')");
	    	sql.addCustomCriteria("CC.TP_CONTROLE_CARGA = 'C'");

	     	Long idProprietario = MapUtils.getLong(parameters,"idProprietario");
	    	if(idProprietario!=null){
	    		sql.addCriteria("CC.ID_PROPRIETARIO", "=", idProprietario);
	    		String nmPessoa = MapUtils.getString(parameters,"nmPessoa");
	    		sql.addFilterSummary("parceiro",  nmPessoa);
	    	}
	    	Long idControleCarga = MapUtils.getLong(parameters,"idControleCarga");
	    	if(idControleCarga!=null){
	    		sql.addCriteria("CC.ID_CONTROLE_CARGA", "=", idControleCarga);
	    		String strControleCarga = FormatUtils.formatSgFilialWithLong(MapUtils.getString(parameters, "sgFilialControleCarga"),  MapUtils.getLong(parameters, "nrControleCarga"));
	            sql.addFilterSummary("controleCarga", strControleCarga);
	    	}
	    	Long idMeioTransporte = MapUtils.getLong(parameters,"idMeioTransporte");
	    	if(idMeioTransporte!=null){
	    		sql.addCriteria("CC.ID_TRANSPORTADO", "=", idMeioTransporte);
	    		StringBuilder sb = new StringBuilder();
	    		sb.append(FormatUtils.formatNrFrota(MapUtils.getString(parameters, "nrFrota")))
	    		.append(" ")
	    		.append(MapUtils.getString(parameters, "nrIdentificador"));
	    		sql.addFilterSummary("frotaPlaca",  sb.toString());
	    	}

	    	Long idManifestoEntrega = MapUtils.getLong(parameters,"idManifestoEntrega");
	    	if(idManifestoEntrega!=null){
	    		sql.addCriteria("ME.ID_MANIFESTO_ENTREGA", "=", idManifestoEntrega);
	    		String strManifestoEntrega = FormatUtils.formatSgFilialWithLong(MapUtils.getString(parameters, "sgFilialManifestoEntrega"),  MapUtils.getLong(parameters, "nrManifestoEntrega"));
	            sql.addFilterSummary("controleCarga", strManifestoEntrega);
	    	}
	    	//Período de emissao do manifesto	    	
	    	
	    	YearMonthDay dtInicio = (YearMonthDay )parameters.get("dtInicioEmissaoManifesto");
	    	String strDtInicio = null;
	    	String strDtFim = null;
	    	if (dtInicio != null){
	    		strDtInicio = dtInicio.toString("dd/MM/yyyy");
	    		sql.addCriteria("trunc(ME.DH_EMISSAO)", ">=", dtInicio);
	    		sql.addFilterSummary("dataInicio", strDtInicio);
	    	}
	    	
	    	YearMonthDay dtFim = (YearMonthDay )parameters.get("dtFimEmissaoManifesto");

	    	if (dtFim != null){
	    		strDtFim = dtFim.toString("dd/MM/yyyy");
	    		sql.addCriteria("trunc(ME.DH_EMISSAO)", "<=", dtFim);
	    		sql.addFilterSummary("dataFim", strDtFim);
	    	}

	    	
	    	sql.addGroupBy("FCC.SG_FILIAL");
	    	sql.addGroupBy("CC.NR_CONTROLE_CARGA");
	    	sql.addGroupBy("FME.SG_FILIAL");
	    	sql.addGroupBy("ME.NR_MANIFESTO_ENTREGA");
	    	sql.addGroupBy("DS.TP_DOCUMENTO_SERVICO");
	    	sql.addGroupBy("FDS.SG_FILIAL");
	    	sql.addGroupBy("DS.NR_DOCTO_SERVICO");
	    	sql.addGroupBy("PS.TP_IDENTIFICACAO");
	    	sql.addGroupBy("PS.NR_IDENTIFICACAO");
	    	sql.addGroupBy("PS.NM_PESSOA");
	    	sql.addGroupBy("DS.DS_ENDERECO_ENTREGA_REAL");
	    	sql.addGroupBy("to_char(ME.DH_EMISSAO, 'dd/MM/yyyy HH24:MI TZR')"); 
	    	sql.addGroupBy("to_char(DS.DT_PREV_ENTREGA, 'dd/MM/yyyy')");
	    	sql.addGroupBy("to_char(MED.DH_OCORRENCIA, 'dd/MM/yyyy HH24:MI TZR')");
	    	sql.addGroupBy("OE.CD_OCORRENCIA_ENTREGA");
	    	sql.addGroupBy("OE.DS_OCORRENCIA_ENTREGA_I");
	    	
	    	sql.addOrderBy("FCC.SG_FILIAL");
	    	sql.addOrderBy("CC.NR_CONTROLE_CARGA");
	    	sql.addOrderBy("FME.SG_FILIAL");
	    	sql.addOrderBy("ME.NR_MANIFESTO_ENTREGA");
	    	sql.addOrderBy("DS.TP_DOCUMENTO_SERVICO");
	    	sql.addOrderBy("FDS.SG_FILIAL");
	    	sql.addOrderBy("DS.NR_DOCTO_SERVICO");
	    	sql.addOrderBy("OE.DS_OCORRENCIA_ENTREGA_I");

	        return sql;         
	    }
	    
}
