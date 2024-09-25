package com.mercurio.lms.entrega.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.entrega.emitirEficienciaEntregaMeioTransporteService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirEficienciaEntregaMeioTransporte.jasper"
 */ 
public class EmitirEficienciaEntregaMeioTransporteService extends ReportServiceSupport {
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
        SqlTemplate sql = getSqlTemplate(tfm);	    
        
	    JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
	                    
        Map parametersReport = new HashMap();
        
        configureFilterSummary(sql,tfm); 
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
                
        jr.setParameters(parametersReport);
        
        return jr;
	}
	
	private void configureFilterSummary(SqlTemplate sql, TypedFlatMap parameters) {
				
		if (parameters.getLong("filial.idFilial") != null) {
			sql.addFilterSummary("filial",parameters.getString("filial.sgFilial") + 
					" - " + parameters.getString("filial.pessoa.nmFantasia"));	
		}
		
		if (parameters.getLong("meioTransporte.idMeioTransporte") != null) {
			sql.addFilterSummary("meioTransporte",parameters.getString("meioTransporte2.nrFrota") + 
					" - " + parameters.getString("meioTransporte.nrIdentificador"));	
		}
		
		sql.addFilterSummary("periodoFechamentoInicial",parameters.getYearMonthDay("dtFechamentoInicial"));
		sql.addFilterSummary("periodoFechamentoFinal",parameters.getYearMonthDay("dtFechamentoFinal"));
		sql.addFilterSummary("converterParaMoeda",parameters.getString("moedaPais.moeda.siglaSimbolo"));
	}
	
	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) {	
		SqlTemplate mainSql = createSqlTemplate();
		
		mainSql.addProjection("F.SG_FILIAL");
		mainSql.addProjection("P.NM_FANTASIA");
		mainSql.addProjection("MT.NR_FROTA");
		mainSql.addProjection("MT.NR_IDENTIFICADOR");
		mainSql.addProjection("DADOS_01.*");
		
		mainSql.addFrom("(" + getSqlDados(mainSql,parameters) + ")","DADOS_01");
		mainSql.addFrom("FILIAL","F");
		mainSql.addFrom("PESSOA","P");
		mainSql.addFrom("MEIO_TRANSPORTE","MT");
	
		mainSql.addJoin("F.ID_FILIAL","DADOS_01.ID_FILIAL_DESTINO");
		mainSql.addJoin("P.ID_PESSOA","F.ID_FILIAL");
		mainSql.addJoin("MT.ID_MEIO_TRANSPORTE","DADOS_01.ID_MEIO_TRANSPORTE");
		
		mainSql.addOrderBy("F.SG_FILIAL");
		mainSql.addOrderBy("MT.NR_FROTA");
		
		return mainSql;
	}
		
	private String getSqlKmRodados(SqlTemplate mainSql) {
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer case01 = new StringBuffer()
			.append("CASE")
			.append("  WHEN (CQ.BL_VIROU_HODOMETRO = ? AND CQ.BL_SAIDA = ?)")
			.append("    THEN 1000000 - Nvl(CQ.NR_QUILOMETRAGEM,0)")
			.append("  WHEN CQ.BL_SAIDA = ?")
			.append("    THEN Nvl(-CQ.NR_QUILOMETRAGEM,0)")
			.append("  ELSE Nvl(CQ.NR_QUILOMETRAGEM,0)")
			.append("END");
		sql.addProjection("Sum(" + case01 + ")");
		mainSql.addCriteriaValue("S");
		mainSql.addCriteriaValue("S");
		mainSql.addCriteriaValue("S");
		
		sql.addFrom("CONTROLE_QUILOMETRAGEM","CQ");
		sql.addJoin("CQ.ID_CONTROLE_CARGA","CC.ID_CONTROLE_CARGA");
		sql.addGroupBy("CQ.ID_CONTROLE_CARGA");
		
		return sql.getSql();
	}
		
	private String getSqlManifestosValidos(SqlTemplate mainSql) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("M.ID_MANIFESTO");
		sql.addFrom("MANIFESTO","M");	
		
		sql.addJoin("M.ID_CONTROLE_CARGA","CC.ID_CONTROLE_CARGA");
			
		sql.addCustomCriteria("M.TP_STATUS_MANIFESTO = ?");
		mainSql.addCriteriaValue("FE");
		
		sql.addCustomCriteria("M.TP_MANIFESTO = ?");
		mainSql.addCriteriaValue("E");

		return sql.getSql();
	}
	
	private String getSqlCountMaisDeUmDocumento(SqlTemplate mainSql) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("count(*)");
		sql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO","MED2");
        sql.addJoin("MED2.ID_DOCTO_SERVICO","MED.ID_DOCTO_SERVICO");
        
        sql.addCustomCriteria("MED2.TP_SITUACAO_DOCUMENTO <> ?");
        mainSql.addCriteriaValue("CANC");
        
        return sql.getSql();
	}
	
	private String getSqlControlesCarga(SqlTemplate mainSql,TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("CC.ID_CONTROLE_CARGA");
		sql.addProjection("R.ID_REGIONAL");
		sql.addProjection("CC.ID_FILIAL_ORIGEM");
		sql.addProjection("CC.DH_CHEGADA_COLETA_ENTREGA");
		sql.addProjection("CC.DH_SAIDA_COLETA_ENTREGA");
		sql.addProjection("CC.ID_NOTA_CREDITO");
		sql.addProjection("CC.ID_TRANSPORTADO");
		sql.addProjection("(" + getSqlKmRodados(mainSql) + ")","KM_RODADOS");
		
		sql.addFrom("CONTROLE_CARGA","CC");
		sql.addFrom("FILIAL","FO");
		sql.addFrom("REGIONAL_FILIAL","RF");
		sql.addFrom("REGIONAL","R");		
		sql.addFrom("NOTA_CREDITO","NC");
		
		sql.addJoin("CC.ID_FILIAL_ORIGEM","FO.ID_FILIAL");
		sql.addJoin("RF.ID_FILIAL","FO.ID_FILIAL");
	    sql.addJoin("R.ID_REGIONAL","RF.ID_REGIONAL");		
		sql.addJoin("NC.ID_NOTA_CREDITO (+)","CC.ID_NOTA_CREDITO");
		
		sql.addCustomCriteria("RF.DT_VIGENCIA_INICIAL <= ?");
		mainSql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCustomCriteria("RF.DT_VIGENCIA_FINAL >= ?");
		mainSql.addCriteriaValue(JTDateTimeUtils.getDataAtual());	
		sql.addCustomCriteria("CC.ID_FILIAL_ORIGEM IS NOT NULL");
		
		sql.addCustomCriteria("EXISTS (" + getSqlManifestosValidos(mainSql) + ")");
		
		Long idFilial = parameters.getLong("filial.idFilial");
		if (idFilial != null) {
			sql.addCustomCriteria("CC.ID_FILIAL_ORIGEM = ?");
			mainSql.addCriteriaValue(idFilial);
		}
		
		Long idMeioTransporte = parameters.getLong("meioTransporte.idMeioTransporte");
		if (idMeioTransporte != null) {
			sql.addCustomCriteria("CC.ID_TRANSPORTADO = ?");
			mainSql.addCriteriaValue(idMeioTransporte);
		}		
	
		
		return sql.getSql();
	}
	
	private String getSqlDados(SqlTemplate mainSql,TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("Max(DADOS_CC.ID_REGIONAL)","ID_REGIONAL");
		sql.addProjection("Max(DADOS_CC.ID_FILIAL_ORIGEM)","ID_FILIAL_DESTINO");
		sql.addProjection("Max(TO_SECONDS(DADOS_CC.DH_CHEGADA_COLETA_ENTREGA - DADOS_CC.DH_SAIDA_COLETA_ENTREGA))",
				"TEMPO_ENTREGA");
		sql.addProjection("Max(DADOS_CC.ID_TRANSPORTADO)","ID_MEIO_TRANSPORTE");
		sql.addProjection("Count(DISTINCT M.ID_MANIFESTO)","QTDE_ME");
		sql.addProjection("Count(MED.ID_MANIFESTO_ENTREGA_DOCUMENTO)","ENT_PROG");
		
		sql.addProjection("Sum(CASE WHEN OE.TP_OCORRENCIA IN (?,?) THEN 1 ELSE 0 END)","ENT_REAL");
		mainSql.addCriteriaValue("E");
		mainSql.addCriteriaValue("A");
		sql.addProjection("Sum(CASE WHEN OE.TP_OCORRENCIA = ? THEN 1 ELSE 0 END)","ENT_NAO_REAL");
		mainSql.addCriteriaValue("N");
		sql.addProjection("Sum(CASE WHEN OE.TP_OCORRENCIA = ? THEN 1 ELSE 0 END)","ENT_REC");
		mainSql.addCriteriaValue("R");
		sql.addProjection("Sum(CASE WHEN OE.TP_OCORRENCIA = ? THEN 1 ELSE 0 END)","ENT_REENT");
		mainSql.addCriteriaValue("S");
		
		sql.addProjection("Sum(CASE WHEN(" + getSqlCountMaisDeUmDocumento(mainSql) + ") > 1" +
				" THEN 1 ELSE 0 END)","ENT_MAIS1");

		sql.addProjection("Max(DADOS_CC.KM_RODADOS)","KM_RODADOS");
 
		sql.addFrom("(" + getSqlControlesCarga(mainSql,parameters) + ")","DADOS_CC");
		
		sql.addFrom("MANIFESTO","M");
		sql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO","MED");
		sql.addFrom("MANIFESTO_ENTREGA","ME");
		sql.addFrom("OCORRENCIA_ENTREGA","OE");
		
		sql.addJoin("M.ID_CONTROLE_CARGA","DADOS_CC.ID_CONTROLE_CARGA");
		
		sql.addJoin("ME.ID_MANIFESTO_ENTREGA","M.ID_MANIFESTO");
  
		sql.addJoin("MED.ID_MANIFESTO_ENTREGA","ME.ID_MANIFESTO_ENTREGA");
		
		sql.addJoin("OE.ID_OCORRENCIA_ENTREGA (+)","MED.ID_OCORRENCIA_ENTREGA");
		
		YearMonthDay dtFechamentoInicial = parameters.getYearMonthDay("dtFechamentoInicial");
		if (dtFechamentoInicial != null) {
			sql.addCustomCriteria("Trunc(ME.DH_FECHAMENTO) >= ?");
			mainSql.addCriteriaValue(dtFechamentoInicial);
		}
		
		YearMonthDay dtFechamentoFinal = parameters.getYearMonthDay("dtFechamentoFinal");
		if (dtFechamentoFinal != null) {
			sql.addCustomCriteria("Trunc(ME.DH_FECHAMENTO) <= ?");
			mainSql.addCriteriaValue(dtFechamentoFinal);
		}
				
		sql.addCustomCriteria("M.TP_STATUS_MANIFESTO = ?");
		mainSql.addCriteriaValue("FE");
		sql.addCustomCriteria("M.TP_MANIFESTO = ?");
		mainSql.addCriteriaValue("E");
		
		sql.addGroupBy("DADOS_CC.ID_CONTROLE_CARGA");
		sql.addGroupBy("DADOS_CC.ID_NOTA_CREDITO");
		sql.addGroupBy("ME.DH_FECHAMENTO");
		
		return sql.getSql();
	}
	
	public String converteTempoEntrega(BigDecimal tempoSegundos) {
		return JTFormatUtils.formatTime(tempoSegundos.longValue(),JTFormatUtils.HOURS,JTFormatUtils.MINUTES);
	}
	
}
