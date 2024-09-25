package com.mercurio.lms.workflow.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author JoseMR
 *
 * @spring.bean id="lms.workflow.emitirAprovacaoSolicitanteService"
 * @spring.property name="reportName" value="com/mercurio/lms/workflow/report/emitirAprovacaoSolicitante.jasper"
 */
public class EmitirAprovacaoSolicitanteService extends ReportServiceSupport {    
	
	/**
     * Método de execução para o relatório. Monta a query de acordo com os critérios,
     * executa a query e gera o arquivo de relatório
	 */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	
    	TypedFlatMap tfm = (TypedFlatMap) parameters;
		
        SqlTemplate sql = mountSql(tfm);
        
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        
		Map parametersReport = new HashMap();

		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		jr.setParameters(parametersReport);
		
		return jr; 
	}
    
    /**
     * Seta os domínios para o relatório
     */
    public void configReportDomains(ReportDomainConfig config) {
         
         config.configDomainField("SITUACAO_OCORRENCIA", "DM_STATUS_OCORRENCIA_WORKFLOW");
         config.configDomainField("SITUACAO_PENDENCIA" , "DM_STATUS_WORKFLOW");
         config.configDomainField("SITUACAO_ACAO"      , "DM_STATUS_ACAO_WORKFLOW");
         
    }
	
    /**
     * Monta a query de pesquisa para o relatório
     * @param parameters Critérios de pesquisa
     * @return Query montada
     * @throws Exception
     */
	private SqlTemplate mountSql(TypedFlatMap map) throws Exception{
		
		SqlTemplate sql = this.createSqlTemplate();
        
        sql.addFilterSummary("solicitante",map.getString("usuario.nmUsuario"));        
       
        sql.addProjection("UO.ID_USUARIO",              "ID_USUARIO_SOLICITANTE");
        sql.addProjection("UO.NM_USUARIO",              "NM_USUARIO_SOLICITANTE");        
        sql.addProjection("NVL(UI.NM_USUARIO, PE.DS_PERFIL)",              "NM_USUARIO_APROVADOR");
        sql.addProjection("PE.DS_PERFIL",               "DS_PERFIL");
        sql.addProjection("EW.ID_EVENTO_WORKFLOW",      "ID_EVENTO_WORKFLOW");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("TE.DS_TIPO_EVENTO_I"), "DS_TIPO_EVENTO");
        sql.addProjection("O.ID_OCORRENCIA",            "ID_OCORRENCIA");
        sql.addProjection("O.TP_SITUACAO_OCORRENCIA",   "SITUACAO_OCORRENCIA");
        sql.addProjection("O.DH_INCLUSAO",              "DH_INCLUSAO");
        sql.addProjection("P.ID_PENDENCIA",             "ID_PENDENCIA");
        sql.addProjection("P.TP_SITUACAO_PENDENCIA",    "SITUACAO_PENDENCIA");
        sql.addProjection("P.DS_PENDENCIA",             "DS_PENDENCIA");
        sql.addProjection("A.DH_LIBERACAO",             "DT_LIBERACAO");
        sql.addProjection("A.DH_ACAO",                  "DT_ACAO");
        sql.addProjection("A.TP_SITUACAO_ACAO",         "SITUACAO_ACAO");
        sql.addProjection("A.OB_ACAO",                  "OB_ACAO");
        
        sql.addFrom("USUARIO",          "UO");
        sql.addFrom("ACAO",             "A");
        sql.addFrom("INTEGRANTE I LEFT OUTER JOIN USUARIO UI ON I.ID_USUARIO = UI.ID_USUARIO" +
                    "             LEFT OUTER JOIN PERFIL PE  ON I.ID_PERFIL  = PE.ID_PERFIL");
        sql.addFrom("OCORRENCIA",       "O");
        sql.addFrom("PENDENCIA",        "P");
        sql.addFrom("EVENTO_WORKFLOW",  "EW");
        sql.addFrom("TIPO_EVENTO",      "TE");        
        
        sql.addJoin("UO.ID_USUARIO",        "O.ID_USUARIO");
        sql.addJoin("I.ID_INTEGRANTE",      "A.ID_INTEGRANTE");
        sql.addJoin("A.ID_PENDENCIA",       "P.ID_PENDENCIA");
        sql.addJoin("P.ID_OCORRENCIA",      "O.ID_OCORRENCIA");        
        sql.addJoin("O.ID_EVENTO_WORKFLOW", "EW.ID_EVENTO_WORKFLOW");        
        sql.addJoin("EW.ID_EVENTO_WORKFLOW","TE.ID_TIPO_EVENTO");
        
	    sql.addCriteria("O.ID_USUARIO","=",map.getLong("usuario.idUsuario"));

        String tpSituacao           = map.getString("tpSituacao");

        sql.addCriteria("A.TP_SITUACAO_ACAO","=",tpSituacao);
        sql.addFilterSummary("situacao",map.getString("dsSituacao"));
        
    	YearMonthDay dtInicial = map.getYearMonthDay("dtSolicitacaoInicial");
        if( dtInicial != null){
            sql.addCriteria("cast(O.DH_INCLUSAO as date)", ">=", dtInicial);
            sql.addFilterSummary("dataSolicitacaoInicial", JTFormatUtils.format(dtInicial, JTFormatUtils.YEARMONTHDAY));
        }
        
    	YearMonthDay dtFinal = map.getYearMonthDay("dtSolicitacaoFinal");
        if( dtFinal != null){
            sql.addCriteria("cast(O.DH_INCLUSAO as date)","<=",dtFinal);
            sql.addFilterSummary("dataSolicitacaoFinal", JTFormatUtils.format(dtFinal, JTFormatUtils.YEARMONTHDAY));
        }
        
        sql.addOrderBy("UO.NM_USUARIO");
        sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("TE.DS_TIPO_EVENTO_I"));
        sql.addOrderBy("O.DH_INCLUSAO");
        sql.addOrderBy("P.DS_PENDENCIA");
        sql.addOrderBy("I.NR_ORDEM_APROVACAO");
		
		return sql;
	}

    
}
