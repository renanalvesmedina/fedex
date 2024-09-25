package com.mercurio.lms.municipios.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.municipios.emitirPerformanceHorarioRotasViagemService"
 * @spring.property name="reportName" value="com/mercurio/lms/municipios/report/emitirPerformanceHorarioRotas.jasper"
 */
public class EmitirPerformanceHorarioRotasViagemService extends
		ReportServiceSupport {

	private RecursoMensagemService recursoMensagemService;
	private FilialService filialService;
	private RotaService rotaService; 
	private DomainValueService domainValueService;
	private Logger log = LogManager.getLogger(this.getClass());
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sqlT = createSqlTemplate(parameters);
		
		String sql = createCustomProjection(sqlT.getSql(),((parameters.get("analisePor") != null) ? (String)parameters.get("analisePor") : ""));
		
		log.info(sql);
		Map parametersReport = new HashMap();
    		parametersReport.put("parametrosPesquisa",sqlT.getFilterSummary());
    		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
    		parametersReport.put("analise",((parameters.get("analisePor") != null) ? (String)parameters.get("analisePor") : ""));
    		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
    	JRReportDataObject jr = executeQuery(sql, sqlT.getCriteria());
        				   jr.setParameters(parametersReport);
        return jr;
	}
	
	private SqlTemplate createSqlTemplate(Map criteria) {
		SqlTemplate sqlT = createSqlTemplate();

		sqlT.addProjection("(F.SG_FILIAL || ' - ' || P.NM_FANTASIA) AS FILIAL");
		sqlT.addProjection("R.DS_ROTA");
		sqlT.addProjection("(F_RO.SG_FILIAL || ' - ' || F_RD.SG_FILIAL) AS TRECHO");
		
		sqlT.addProjection("NVL(COUNT(CC_T.ID_CONTROLE_CARGA),0) AS TV_T");
		sqlT.addProjection("NVL(COUNT((SELECT ECC.ID_EVENTO_CONTROLE_CARGA FROM EVENTO_CONTROLE_CARGA ECC WHERE ECC.ID_CONTROLE_CARGA = CT_T.ID_CONTROLE_CARGA " + 
		   		  			"AND ECC.TP_EVENTO_CONTROLE_CARGA = 'EM' AND ECC.ID_FILIAL = FRO.ID_FILIAL AND ECC.DH_EVENTO > CT_T.DH_PREVISAO_SAIDA)),0) AS CCEA_T");
		sqlT.addProjection("SUM(NVL2(CT_T.DH_SAIDA,1,0)) AS CCSS_T");
		sqlT.addProjection("SUM(NVL2(CT_T.DH_CHEGADA,1,0)) AS CCSC_T");
		/*
		 * 
		 */
		sqlT.addProjection("SUM(CASE WHEN CT_T.DH_CHEGADA < ? THEN 1 ELSE 0 END) AS TC_T");
		sqlT.addProjection("SUM(CASE WHEN CT_T.DH_SAIDA < ? THEN 1 ELSE 0 END) AS TS_T");
		sqlT.addProjection("SUM(CASE WHEN CT_T.DH_SAIDA > CT_T.DH_PREVISAO_SAIDA THEN 1 ELSE 0 END) AS SA_T");
		
		sqlT.addProjection("SUM(CASE WHEN CT_T.DH_CHEGADA > CT_T.DH_PREVISAO_CHEGADA THEN 1 ELSE 0 END) AS CA_T");
		sqlT.addProjection("NVL(COUNT(CC_R.ID_CONTROLE_CARGA),0) AS TV_R");
		sqlT.addProjection("NVL(COUNT((SELECT ECC.ID_EVENTO_CONTROLE_CARGA FROM EVENTO_CONTROLE_CARGA ECC WHERE ECC.ID_CONTROLE_CARGA = CT_R.ID_CONTROLE_CARGA " + 
		   		  				"AND ECC.TP_EVENTO_CONTROLE_CARGA = 'EM' AND ECC.ID_FILIAL = FRD.ID_FILIAL AND ECC.DH_EVENTO > CT_R.DH_PREVISAO_SAIDA)),0) AS CCEA_R");
		sqlT.addProjection("SUM(NVL2(CT_R.DH_SAIDA,1,0)) AS CCSS_R");
		sqlT.addProjection("SUM(NVL2(CT_R.DH_CHEGADA,1,0)) AS CCSC_R");
		sqlT.addProjection("SUM(CASE WHEN CT_R.DH_CHEGADA < ? THEN 1 ELSE 0 END) AS TC_R");
		sqlT.addProjection("SUM(CASE WHEN CT_R.DH_SAIDA < ? THEN 1 ELSE 0 END) AS TS_R");
		sqlT.addProjection("SUM(CASE WHEN CT_R.DH_SAIDA > CT_R.DH_PREVISAO_SAIDA THEN 1 ELSE 0 END) AS SA_R");
		sqlT.addProjection("SUM(CASE WHEN CT_R.DH_CHEGADA > CT_R.DH_PREVISAO_CHEGADA THEN 1 ELSE 0 END) AS CA_R");
		
		sqlT.addCriteriaValue(JTDateTimeUtils.createWithMaxTime(JTDateTimeUtils.getDataAtual()));
		sqlT.addCriteriaValue(JTDateTimeUtils.createWithMaxTime(JTDateTimeUtils.getDataAtual()));
		sqlT.addCriteriaValue(JTDateTimeUtils.createWithMaxTime(JTDateTimeUtils.getDataAtual()));
		sqlT.addCriteriaValue(JTDateTimeUtils.createWithMaxTime(JTDateTimeUtils.getDataAtual()));
		
		
		JTDateTimeUtils.createWithMaxTime(new YearMonthDay());
		
		sqlT.addFrom("ROTA","R");
		sqlT.addFrom("ROTA_IDA_VOLTA","RIV");
		sqlT.addFrom("ROTA_VIAGEM","RV");
		sqlT.addFrom("TRECHO_ROTA_IDA_VOLTA","TRIV");
		sqlT.addFrom("FILIAL_ROTA","FRD");
		sqlT.addFrom("FILIAL_ROTA","FRO");
		sqlT.addFrom("FILIAL","F");
		sqlT.addFrom("PESSOA","P");
		sqlT.addFrom("TRECHO_ROTA_IDA_VOLTA","TR");
		sqlT.addFrom("FILIAL_ROTA","TR_FRD");
		sqlT.addFrom("FILIAL_ROTA","TR_FRO");
		sqlT.addFrom("FILIAL","F_RO");
		sqlT.addFrom("FILIAL","F_RD");
		//ROTA
		sqlT.addFrom("CONTROLE_TRECHO","CT_R");
		sqlT.addFrom("CONTROLE_CARGA","CC_R");
		sqlT.addFrom("EVENTO_CONTROLE_CARGA","ECC_R");
		//TRECHO
		sqlT.addFrom("CONTROLE_TRECHO","CT_T");
		sqlT.addFrom("CONTROLE_CARGA","CC_T");
		sqlT.addFrom("EVENTO_CONTROLE_CARGA","ECC_T");
		
		
		//JOINS
		sqlT.addJoin("RIV.ID_ROTA","R.ID_ROTA");
		sqlT.addJoin("RV.ID_ROTA_VIAGEM","RIV.ID_ROTA_VIAGEM");
		sqlT.addJoin("TRIV.ID_ROTA_IDA_VOLTA","RIV.ID_ROTA_IDA_VOLTA");
		sqlT.addJoin("FRD.ID_ROTA","R.ID_ROTA");
		sqlT.addJoin("FRO.ID_ROTA","R.ID_ROTA");
		sqlT.addJoin("FRO.ID_FILIAL","F.ID_FILIAL");
		sqlT.addJoin("P.ID_PESSOA","F.ID_FILIAL");
		sqlT.addJoin("TR.ID_ROTA_IDA_VOLTA","RIV.ID_ROTA_IDA_VOLTA");
		sqlT.addJoin("TR.ID_FILIAL_ROTA_DESTINO","TR_FRD.ID_FILIAL_ROTA");
		sqlT.addJoin("TR.ID_FILIAL_ROTA_ORIGEM","TR_FRO.ID_FILIAL_ROTA");
		sqlT.addJoin("F_RO.ID_FILIAL","TR_FRO.ID_FILIAL");
		sqlT.addJoin("F_RD.ID_FILIAL","TR_FRD.ID_FILIAL");
		//ROTA 
		sqlT.addJoin("CT_R.ID_TRECHO_ROTA_IDA_VOLTA","TRIV.ID_TRECHO_ROTA_IDA_VOLTA");
		sqlT.addJoin("ECC_R.ID_CONTROLE_CARGA","CT_R.ID_CONTROLE_CARGA");
		sqlT.addJoin("ECC_R.ID_FILIAL","F_RO.ID_FILIAL");
		//TRECHO
		sqlT.addJoin("CT_T.ID_TRECHO_ROTA_IDA_VOLTA","TRIV.ID_TRECHO_ROTA_IDA_VOLTA");
		sqlT.addJoin("CC_T.ID_CONTROLE_CARGA","CT_T.ID_CONTROLE_CARGA");
		sqlT.addJoin("ECC_T.ID_CONTROLE_CARGA","CT_T.ID_CONTROLE_CARGA");
		sqlT.addJoin("ECC_T.ID_FILIAL","F_RO.ID_FILIAL");
		
		//WHERE
		sqlT.addCustomCriteria("FRO.ID_FILIAL_ROTA = TRIV.ID_FILIAL_ROTA_ORIGEM");
		sqlT.addCustomCriteria("FRO.BL_ORIGEM_ROTA = 'S'");
		sqlT.addCustomCriteria("FRD.BL_DESTINO_ROTA = 'S'");
		sqlT.addCustomCriteria("CC_R.TP_CONTROLE_CARGA = 'V'");
		sqlT.addCustomCriteria("0 = (SELECT COUNT(*) FROM EVENTO_CONTROLE_CARGA WHERE ID_CONTROLE_CARGA = CC_R.ID_CONTROLE_CARGA AND TP_EVENTO_CONTROLE_CARGA = 'CA')");

		 
		
		sqlT.addCustomCriteria("TO_CHAR(ECC_T.DH_EVENTO,'d') <> 1");
		sqlT.addCustomCriteria("TO_CHAR(ECC_T.DH_EVENTO,'d') <> 7");
		sqlT.addCustomCriteria("TO_CHAR(ECC_R.DH_EVENTO,'d') <> 1");
		sqlT.addCustomCriteria("TO_CHAR(ECC_R.DH_EVENTO,'d') <> 7");
		
		
		//WHERE TIER VIEW  
		Map filial = (Map) criteria.get("filial");
		if (filial != null && StringUtils.isNotBlank((String)filial.get("sgFilial"))) {
			sqlT.addCriteria("R.DS_ROTA","like",(String)filial.get("sgFilial") + "%");
			sqlT.addFilterSummary("filialOrigem",(String)filial.get("sgFilial"));
		}
		Map filialOrig = (Map)criteria.get("filialOrigem");
		if (filialOrig != null && StringUtils.isNotBlank((String)filialOrig.get("idFilial"))) {
			sqlT.addCriteria("F_RO.ID_FILIAL","=",Long.valueOf((String)filialOrig.get("idFilial")));
			sqlT.addFilterSummary("filialOrigemTrecho",(filialService.findById(Long.valueOf((String)filialOrig.get("idFilial")))).getSiglaNomeFilial());
		}
		Map filialDest = (Map)criteria.get("filialDestino");
		if (filialDest != null && StringUtils.isNotBlank((String)filialDest.get("idFilial"))) {
			sqlT.addCriteria("F_RD.ID_FILIAL","=",Long.valueOf((String)filialDest.get("idFilial")));
			sqlT.addFilterSummary("filialDestinoTrecho",(filialService.findById(Long.valueOf((String)filialDest.get("idFilial")))).getSiglaNomeFilial());
		}
		Map rota = (Map)criteria.get("rota");
		if (rota != null && StringUtils.isNotBlank((String)rota.get("idRota"))) {
			sqlT.addCriteria("R.ID_ROTA","=",Long.valueOf((String)rota.get("idRota")));
			sqlT.addFilterSummary("rotaViagem",(rotaService.findById(Long.valueOf((String)rota.get("idRota")))).getDsRota());
		}

		if (!StringUtils.isBlank((String)criteria.get("dtVigenciaInicial"))) {
			sqlT.addCriteria("ECC_R.DH_EVENTO",">=",((YearMonthDay)ReflectionUtils.toObject((String)criteria.get("dtVigenciaInicial"),YearMonthDay.class)).toDateTimeAtMidnight());
			sqlT.addCriteria("ECC_T.DH_EVENTO",">=",((YearMonthDay)ReflectionUtils.toObject((String)criteria.get("dtVigenciaInicial"),YearMonthDay.class)).toDateTimeAtMidnight());
			sqlT.addFilterSummary("dataEmissaoInicial",(String)criteria.get("dtVigenciaInicial"),YearMonthDay.class);
		}
		if (!StringUtils.isBlank((String)criteria.get("dtVigenciaFinal"))) {
			sqlT.addCriteria("ECC_R.DH_EVENTO","<=",JTDateTimeUtils.createWithMaxTime((YearMonthDay)ReflectionUtils.toObject((String)criteria.get("dtVigenciaFinal"),YearMonthDay.class)));
			sqlT.addCriteria("ECC_T.DH_EVENTO","<=",JTDateTimeUtils.createWithMaxTime((YearMonthDay)ReflectionUtils.toObject((String)criteria.get("dtVigenciaFinal"),YearMonthDay.class)));
			sqlT.addFilterSummary("dataEmissaoFinal",(String)criteria.get("dtVigenciaFinal"),YearMonthDay.class);
		}

		if (!StringUtils.isBlank((String)criteria.get("somenteAtraso")) && ((String)criteria.get("somenteAtraso")).equalsIgnoreCase("true")){
			sqlT.addCustomCriteria("(CT_R.DH_SAIDA > CT_R.DH_PREVISAO_SAIDA OR CT_R.DH_CHEGADA > CT_R.DH_PREVISAO_CHEGADA)");
			sqlT.addCustomCriteria("(CT_T.DH_SAIDA > CT_T.DH_PREVISAO_SAIDA OR CT_T.DH_CHEGADA > CT_T.DH_PREVISAO_CHEGADA)");
			sqlT.addFilterSummary("somenteAtraso",(String)criteria.get("somenteAtrasoDescription"));
		}else if (!StringUtils.isBlank((String)criteria.get("somenteAtraso")))
			sqlT.addFilterSummary("somenteAtraso",(String)criteria.get("somenteAtrasoDescription"));
				
		 
		if (!StringUtils.isBlank((String)criteria.get("analisePor")))
			sqlT.addFilterSummary("analisePor",(String)criteria.get("analisePorDescription"));
		//GROUP BY
		sqlT.addGroupBy("F.SG_FILIAL");
		sqlT.addGroupBy("P.NM_FANTASIA");
		sqlT.addGroupBy("R.DS_ROTA");
		sqlT.addGroupBy("F_RO.SG_FILIAL");
		sqlT.addGroupBy("F_RD.SG_FILIAL");
		sqlT.addGroupBy("TR_FRO.NR_ORDEM");
		sqlT.addGroupBy("TR_FRD.NR_ORDEM");

		//ORDER		 
		sqlT.addOrderBy("F.SG_FILIAL");
		sqlT.addOrderBy("R.DS_ROTA");
		sqlT.addOrderBy("TR_FRO.NR_ORDEM");
		sqlT.addOrderBy("TR_FRD.NR_ORDEM");
		
		//WHERE TIER VIEW
		

		return sqlT;
	}
	
	private String  createCustomProjection(String sql,String analise) {
		
		StringBuffer sb = new StringBuffer(sql.length() + 100);
		sb.append("SELECT ").append("FILIAL, ")
		  .append("DS_ROTA, ")
		  .append("TRECHO, ").append("TV_T, ")
		  .append("CCEA_T, ").append("CCSS_T, ")
		  .append("CCSC_T, ").append("SA_T, ")
		  .append("CA_T, ")
		  .append("CCEA_R, ")
		  .append("CCSC_R, ")
		  .append("TC_T, ")
		  .append("TS_T, ").append("TS_R, ");
		  
		  if (!StringUtils.isBlank(analise) && analise.equals("HS"))
			sb.append("NULL AS TC_R, ").append("NULL AS CA_R, ");
		  else
			sb.append("TC_R, ").append("CA_R, ");
		  
		  if (!StringUtils.isBlank(analise) && analise.equals("HC"))
			sb.append("NULL AS ES_R, ").append("NULL AS TV_R, ").append("NULL AS CCSS_R, ").append("NULL AS SA_R, ");
		  else
			sb.append("(100 - (((CCEA_R + CCSS_R + SA_R) / TV_R) * 100)) ES_R, ").append("TV_R, ").append("CCSS_R, ").append("SA_R, ");
		  
		  
		  sb.append("(100 - (((CCSC_R + CA_R) / TV_R) * 100)) EC_R, ")
		    .append("(100 - (((CCEA_T + CCSS_T + SA_T) / TV_T) * 100)) ES_T, ")
		    .append("(100 - (((CCSC_T + CA_T) / TV_T) * 100)) AS EC_T ")
		    .append("FROM (").append(sql).append(")");
		return sb.toString();
	}

	public RecursoMensagemService getRecursoMensagemService() {
		return recursoMensagemService;
	}

	public void setRecursoMensagemService(
			RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public RotaService getRotaService() {
		return rotaService;
	}

	public void setRotaService(RotaService rotaService) {
		this.rotaService = rotaService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}
