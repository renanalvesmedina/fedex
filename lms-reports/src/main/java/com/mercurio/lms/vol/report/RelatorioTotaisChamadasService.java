package com.mercurio.lms.vol.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * 
 * @spring.bean id="lms.vol.relatorioTotaisChamadasService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/RelatorioTotaisChamadas.jasper"
 */
public class RelatorioTotaisChamadasService extends ReportServiceSupport {
	private ConfiguracoesFacade configuracoesFacade;
	
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		Map parametersReport = new HashMap();
		
		SqlTemplate sql = null;
		
		Map filial = (Map)parameters.get("filial"); 
		Map nomeFilial = (Map)filial.get("pessoa");  
		parameters.put("nmFilial", nomeFilial.get("nmFantasia"));
		
		int codRelatorio = Integer.parseInt((String)parameters.get("codRelatorio"));
		
		switch (codRelatorio){
		case 1: sql = this.montaSqlVOL1(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41036"));
				break;
		case 2: sql = this.montaSqlVOL2(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41037"));
				break;
		case 3: sql = this.montaSqlVOL3(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41038"));
				break;
		}
	
			
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		jr.setParameters(parametersReport);
        
        return jr;
	}
	

	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("ORIGEM", "DM_TP_ORIGEM_CEL");
		config.configDomainField("TP_TIPO_EVENTO", "DM_TP_EVENTO_CEL");
	}
	
	public SqlTemplate montaSqlVOL3(Map criteria){
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" mt.id_meio_transporte, ")
			.append(" mt.nr_frota, ")
			.append(" ec.tp_origem origem, ")
			.append(" ec.dh_solicitacao, ")
			.append(" ec.dh_atendimento, ")
			.append(" te.tp_tipo_evento, ")
			.append(" fc.sg_filial || ' ' || LPAD(c.nr_conhecimento,8,0) nr_conhecimento ,")
			.append(" te.ds_nome, ")
			.append(" ua.nm_usuario, ")
			.append(" ec.ob_atendente ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" meio_transporte mt ") 
	    	.append(" inner join eventos_celular ec ON mt.id_meio_transporte = ec.id_meio_transporte ")
	    	.append(" inner join tipo_evento_celular te ON te.id_tipo_evento = ec.id_tipo_evento ")
	    	.append(" left join conhecimento c on c.id_conhecimento = ec.id_conhecimento ")
	    	.append(" left join filial fc on fc.id_filial = c.id_filial_origem ")
	    	.append(" left join usuario_lms ul on ul.id_usuario = ec.id_tratado_por ")
	    	.append(" left join usuario_adsm ua on ua.id_usuario = ul.id_usuario "); 
		sql.addFrom(query.toString());

		sql.addCustomCriteria("(ec.tp_origem = ? and te.tp_tipo_evento = ?) or (te.tp_tipo_evento = ?)");
		sql.addCriteriaValue("C");
		sql.addCriteriaValue("E");
		sql.addCriteriaValue("C");
		
		Map filial = (Map)criteria.get("filial");	
		sql.addCriteria("mt.id_filial","=",filial.get("idFilial"));
		
		YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
    	sql.addCustomCriteria("ec.dh_solicitacao between ? and ?");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		
				
		sql.addFilterSummary("filial", criteria.get("sgFilial") + " - " + criteria.get("nmFilial"));
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
		
		sql.addCriteria("mt.id_meio_transporte","=",criteria.get("idFrota"));
		sql.addOrderBy("dh_solicitacao");
		
		return sql;		
	}
	
	
	public SqlTemplate montaSqlVOL1(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" mt.id_meio_transporte, ")
			.append(" mt.nr_frota, ")
			.append(" ec.tp_origem origem, ")
			.append(" ec.dh_solicitacao, ")
			.append(" ec.dh_atendimento, ")
			.append(" te.tp_tipo_evento, ")
			.append(" fc.sg_filial || ' ' || LPAD(c.nr_conhecimento,8,0) nr_conhecimento ,")
			.append(" te.ds_nome, ")
			.append(" ua.nm_usuario, ")
			.append(" ec.ob_atendente ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" meio_transporte mt ") 
	    	.append(" inner join eventos_celular ec ON mt.id_meio_transporte = ec.id_meio_transporte and ec.tp_origem = 'C' ")
	    	.append(" inner join tipo_evento_celular te ON te.id_tipo_evento = ec.id_tipo_evento and te.tp_tipo_evento = 'E' ")
	    	.append(" left join conhecimento c on c.id_conhecimento = ec.id_conhecimento ")
	    	.append(" left join filial fc on fc.id_filial = c.id_filial_origem ")
	    	.append(" left join usuario_lms ul on ul.id_usuario = ec.id_tratado_por ")
	    	.append(" left join usuario_adsm ua on ua.id_usuario = ul.id_usuario "); 
		sql.addFrom(query.toString());
				
		Map filial = (Map)criteria.get("filial");	
		sql.addCriteria("mt.id_filial","=",filial.get("idFilial"));
		
		YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
    	sql.addCustomCriteria("ec.dh_solicitacao between ? and ?");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		
				
		sql.addFilterSummary("filial", criteria.get("sgFilial") + " - " + criteria.get("nmFilial"));
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
		
		sql.addCriteria("mt.id_meio_transporte","=",criteria.get("idFrota"));
		sql.addOrderBy("dh_solicitacao");
		
		return sql;		
	}
	
	public SqlTemplate montaSqlVOL2(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" mt.id_meio_transporte, ")
			.append(" mt.nr_frota, ")
			.append(" ec.tp_origem origem, ")
			.append(" ec.dh_solicitacao, ")
			.append(" ec.dh_atendimento, ")
			.append(" te.tp_tipo_evento, ")
			.append(" fc.sg_filial || ' ' || LPAD(c.nr_conhecimento,8,0) nr_conhecimento ,")
			.append(" te.ds_nome, ")
			.append(" ua.nm_usuario, ")
			.append(" ec.ob_atendente ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" meio_transporte mt ") 
	    	.append(" inner join eventos_celular ec ON mt.id_meio_transporte = ec.id_meio_transporte ")
	    	.append(" inner join tipo_evento_celular te ON te.id_tipo_evento = ec.id_tipo_evento and tp_tipo_evento = 'C' ")
	    	.append(" left join conhecimento c on c.id_conhecimento = ec.id_conhecimento ")
	    	.append(" left join filial fc on fc.id_filial = c.id_filial_origem ")
	    	.append(" left join usuario_lms ul on ul.id_usuario = ec.id_tratado_por ")
	    	.append(" left join usuario_adsm ua on ua.id_usuario = ul.id_usuario "); 
		sql.addFrom(query.toString());
				
		Map filial = (Map)criteria.get("filial");	
		sql.addCriteria("mt.id_filial","=",filial.get("idFilial"));
		
		YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
    	sql.addCustomCriteria("ec.dh_solicitacao between ? and ?");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		
				
		sql.addFilterSummary("filial", criteria.get("sgFilial") + " - " + criteria.get("nmFilial"));
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
		
		sql.addCriteria("mt.id_meio_transporte","=",criteria.get("idFrota"));
		sql.addOrderBy("dh_solicitacao");
		
		return sql;		
	}
	
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}	
	
	
} 