package com.mercurio.lms.vol.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * 
 * @spring.bean id="lms.vol.relatorioDeColetasService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/RelatorioDeColetas.jasper"
 */
public class RelatorioDeColetasService extends ReportServiceSupport {
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;
	 
	public JRReportDataObject execute(Map parameters) throws Exception {
		Map parametersReport = new HashMap();
		Map filial;
		Map nomeFilial;
		SqlTemplate sql = null;
		
		int codRelatorio = Integer.parseInt((String)parameters.get("codRelatorio"));
		
		if (codRelatorio < 6) {
		   filial = (Map)parameters.get("filial"); 
		   nomeFilial = (Map)filial.get("pessoa");  
		   parameters.put("nmFilial", nomeFilial.get("nmFantasia"));
		}
		
		
		switch (codRelatorio){
		case 1: sql = this.montaSqlCOL1(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41031"));
				break;
		case 2: sql = this.montaSqlCOL2(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41032"));
				break;
		case 3: sql = this.montaSqlCOL3(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41033"));
				break;
		case 4: sql = this.montaSqlCOL4(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41034"));
				break;
		case 5: sql = this.montaSqlCOL5(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41035"));
				break;
		case 6: sql = this.montaSqlCOL6(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41047"));
				break;
		
		}
		
		
			
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario()); 
		jr.setParameters(parametersReport);
        
        return jr;
	}
	
	public SqlTemplate montaSqlCOL1(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" metr.nr_frota, ") 
			.append(" f.sg_filial || ' ' || LPAD(mc.nr_manifesto,8,0) manifesto ,")
			.append(" f.sg_filial || ' ' || LPAD(pc.nr_coleta,8,0) documento ,")
			.append(" ep.ds_endereco||' '||ep.nr_endereco endereco, ")
			.append(" p.nr_identificacao nr_identificacao, ")
			.append(" p.tp_identificacao tp_identificacao, ")
			.append(" p.nm_pessoa cliente, ")
			.append(" pc.QT_TOTAL_VOLUMES_INFORMADO volumes, ")
			.append(PropertyVarcharI18nProjection.createProjection("oc.ds_descricao_completa_i", "baixa")).append(",") 
			.append(" ec.dh_evento baixa_, ")
			.append(" pc.dt_previsao_coleta dpc ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" pedido_coleta pc ")
		  	.append(" left join pessoa             p    on p.id_pessoa = pc.id_cliente ")
			.append(" left join manifesto_coleta   mc   on pc.id_manifesto_coleta = mc.id_manifesto_coleta ")
			.append(" left join filial             f    on mc.id_filial_origem = f.id_filial ")
		  	.append(" left join evento_coleta      ec   ON ec.id_pedido_coleta = pc.id_pedido_coleta  AND ec.tp_evento_coleta = 'EX' ")
		  	.append(" left join meio_transporte    metr on metr.id_meio_transporte = ec.id_meio_transporte	")
			.append(" left join ocorrencia_coleta  oc   on oc.id_ocorrencia_coleta = ec.id_ocorrencia_coleta ")
			.append(" left join endereco_pessoa    ep   on ep.ID_ENDERECO_PESSOA = pc.ID_ENDERECO_PESSOA ");
		sql.addFrom(query.toString());

		sql.addCriteria("ec.id_meio_transporte","=",criteria.get("idFrota"));
		
		Map filial = (Map)criteria.get("filial");
		sql.addCriteria("pc.id_filial_responsavel","=",filial.get("idFilial"));
		sql.addFilterSummary("filial", criteria.get("sgFilial") + " - " + criteria.get("nmFilial"));
		
    	YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
		    	
    	sql.addCustomCriteria(" PC.DH_PEDIDO_COLETA between ? and ? ");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
				
		sql.addOrderBy("mc.nr_manifesto, pc.nr_coleta");
		
		return sql;		
	}

	
	public SqlTemplate montaSqlCOL2(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" metr.nr_frota, ") 
			.append("   f.sg_filial || ' ' || LPAD(mc.nr_manifesto,8,0) manifesto ,")
			.append("   f.sg_filial || ' ' || LPAD(pc.nr_coleta,8,0) documento ,")
			.append(" ep.ds_endereco||' '||ep.nr_endereco endereco, ")
			.append("	p.nr_identificacao nr_identificacao, ")
			.append("	p.tp_identificacao tp_identificacao, ")
			.append("	p.nm_pessoa cliente, ")
			.append(" pc.QT_TOTAL_VOLUMES_INFORMADO volumes, ")
			.append(PropertyVarcharI18nProjection.createProjection("oc.ds_descricao_completa_i", "baixa")).append(",") 
			.append(" ec.dh_evento baixa_, ")
			.append(" pc.dt_previsao_coleta dpc ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" pedido_coleta pc ")
	    	.append(" left join pessoa p on p.id_pessoa = pc.id_cliente ")
	    	.append(" left join endereco_pessoa ep on ep.ID_ENDERECO_PESSOA = pc.ID_ENDERECO_PESSOA ")
	    	.append(" left join manifesto_coleta mc on pc.id_manifesto_coleta = mc.id_manifesto_coleta ")
	    	.append(" inner join filial f ON f.id_filial = pc.id_filial_responsavel ") 
	    	.append(" inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ")
	    	.append(" left join meio_transporte metr on metr.id_meio_transporte = ec.id_meio_transporte ")
	    	.append(" left join ocorrencia_coleta oc on oc.id_ocorrencia_coleta = ec.id_ocorrencia_coleta ");
		sql.addFrom(query.toString());
		
		sql.addCriteria("pc.tp_status_coleta","<>","EX");
		sql.addCriteria("ec.id_meio_transporte","=",criteria.get("idFrota"));
		
		Map filial = (Map)criteria.get("filial");
		sql.addCriteria("pc.id_filial_responsavel","=",filial.get("idFilial"));
		sql.addFilterSummary("filial", criteria.get("sgFilial") + " - " + criteria.get("nmFilial"));
		
    	YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
		    	
    	sql.addCustomCriteria(" PC.DH_PEDIDO_COLETA between ? and ? ");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
				
		sql.addOrderBy("mc.nr_manifesto, pc.nr_coleta");
		
		return sql;		
	}
	
	public SqlTemplate montaSqlCOL3(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" metr.nr_frota, ") 
			.append("   f.sg_filial || ' ' || LPAD(mc.nr_manifesto,8,0) manifesto ,")
			.append("   f.sg_filial || ' ' || LPAD(pc.nr_coleta,8,0) documento ,")
			.append(" ep.ds_endereco||' '||ep.nr_endereco endereco, ")
			.append("	p.nr_identificacao nr_identificacao, ")
			.append("	p.tp_identificacao tp_identificacao, ")
			.append("	p.nm_pessoa cliente, ")
			.append(" pc.QT_TOTAL_VOLUMES_INFORMADO volumes, ")
			.append(PropertyVarcharI18nProjection.createProjection("oc.ds_descricao_completa_i", "baixa")).append(",")
			.append(" ec.dh_evento baixa_, ")
			.append(" pc.dt_previsao_coleta dpc ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" pedido_coleta pc ")
	    	.append(" left join pessoa p on p.id_pessoa = pc.id_cliente ")
	    	.append(" left join endereco_pessoa ep on ep.ID_ENDERECO_PESSOA = pc.ID_ENDERECO_PESSOA ")
	    	.append(" inner join filial f ON f.id_filial = pc.id_filial_responsavel ") 
	    	.append( "inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ") 
	    	.append(" left join ocorrencia_coleta oc on oc.id_ocorrencia_coleta = ec.id_ocorrencia_coleta ")
	    	.append(" left join manifesto_coleta mc on pc.id_manifesto_coleta = mc.id_manifesto_coleta ")
	    	.append(" left join meio_transporte metr on metr.id_meio_transporte = ec.id_meio_transporte ");
		sql.addFrom(query.toString());
		
		sql.addCriteria("pc.tp_status_coleta","=","TR");
		sql.addCriteria("ec.id_meio_transporte","=",criteria.get("idFrota"));
		
		Map filial = (Map)criteria.get("filial");
		sql.addCriteria("pc.id_filial_responsavel","=",filial.get("idFilial"));
		sql.addFilterSummary("filial", criteria.get("sgFilial") + " - " + criteria.get("nmFilial"));
		
    	YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
		    	
    	sql.addCustomCriteria(" PC.DH_PEDIDO_COLETA between ? and ? ");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
				
		sql.addOrderBy("mc.nr_manifesto, pc.nr_coleta");
		
		System.out.println("\n "+sql.getSql());
		
		for (int i = 0; i < sql.getCriteria().length; i++){
			System.out.println(sql.getCriteria()[i]);
		}
		
		return sql;		
	}
	
	public SqlTemplate montaSqlCOL4(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" metr.nr_frota, ") 
			.append(" f.sg_filial || ' ' || LPAD(mc.nr_manifesto,8,0) manifesto ,")
			.append(" f.sg_filial || ' ' || LPAD(pc.nr_coleta,8,0) documento ,")
			.append(" ep.ds_endereco||' '||ep.nr_endereco endereco, ")
			.append(" p.nr_identificacao nr_identificacao, ")
			.append(" p.tp_identificacao tp_identificacao, ")
			.append(" p.nm_pessoa cliente, ")
			.append(" pc.QT_TOTAL_VOLUMES_INFORMADO volumes, ")
			.append(PropertyVarcharI18nProjection.createProjection("oc.ds_descricao_completa_i", "baixa")).append(",") 
			.append(" ec.dh_evento baixa_, ")
			.append(" pc.dt_previsao_coleta dpc ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" pedido_coleta pc ")
	    	.append(" left join pessoa p on p.id_pessoa = pc.id_cliente ")
	    	.append(" left join endereco_pessoa ep on ep.ID_ENDERECO_PESSOA = pc.ID_ENDERECO_PESSOA ")
	    	.append(" inner join filial f ON f.id_filial = pc.id_filial_responsavel ") 
	    	.append( "inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ") 
	    	.append(" left join ocorrencia_coleta oc on oc.id_ocorrencia_coleta = ec.id_ocorrencia_coleta ")
	    	.append(" left join manifesto_coleta mc on pc.id_manifesto_coleta = mc.id_manifesto_coleta ")
	    	.append(" left join meio_transporte metr on metr.id_meio_transporte = ec.id_meio_transporte ");
		sql.addFrom(query.toString());
		
		sql.addCriteria("pc.tp_modo_pedido_coleta","=","AU");
		sql.addCriteria("ec.id_meio_transporte","=",criteria.get("idFrota"));
		
		Map filial = (Map)criteria.get("filial");
		sql.addCriteria("pc.id_filial_responsavel","=",filial.get("idFilial"));
		sql.addFilterSummary("filial", criteria.get("sgFilial") + " - " + criteria.get("nmFilial"));
		
    	YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
		    	
    	sql.addCustomCriteria(" PC.DH_PEDIDO_COLETA between ? and ? ");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
				
		sql.addOrderBy("mc.nr_manifesto, pc.nr_coleta");
		
		for (int i = 0; i < sql.getCriteria().length; i++){
			System.out.println(sql.getCriteria()[i]);
		}
		
		return sql;		
	}

	
	public SqlTemplate montaSqlCOL5(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" metr.nr_frota, ") 
			.append("   f.sg_filial || ' ' || LPAD(mc.nr_manifesto,8,0) manifesto ,")
			.append("   f.sg_filial || ' ' || LPAD(pc.nr_coleta,8,0) documento ,")
			.append(" ep.ds_endereco||' '||ep.nr_endereco endereco, ")
			.append("	p.nr_identificacao nr_identificacao, ")
			.append("	p.tp_identificacao tp_identificacao, ")
			.append("	p.nm_pessoa cliente, ")
			.append(" pc.QT_TOTAL_VOLUMES_INFORMADO volumes, ")
			.append(PropertyVarcharI18nProjection.createProjection("oc.ds_descricao_completa_i", "baixa")).append(",") 
			.append(" ec.dh_evento baixa_, ")
			.append(" pc.dt_previsao_coleta dpc ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" pedido_coleta pc ")
	    	.append(" left join pessoa p on p.id_pessoa = pc.id_cliente ")
	    	.append(" left join endereco_pessoa ep on ep.ID_ENDERECO_PESSOA = pc.ID_ENDERECO_PESSOA ")
	    	.append(" inner join filial f ON f.id_filial = pc.id_filial_responsavel ") 
	    	.append( "inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ") 
	    	.append(" left join ocorrencia_coleta oc on oc.id_ocorrencia_coleta = ec.id_ocorrencia_coleta ")
	    	.append(" left join manifesto_coleta mc on pc.id_manifesto_coleta = mc.id_manifesto_coleta ")
	    	.append(" left join meio_transporte metr on metr.id_meio_transporte = ec.id_meio_transporte ");
		sql.addFrom(query.toString());
		
		sql.addCriteria("ec.tp_evento_coleta","=","EX");
		sql.addCriteria("ec.id_meio_transporte","=",criteria.get("idFrota"));
		
		Map filial = (Map)criteria.get("filial");
		sql.addCriteria("pc.id_filial_responsavel","=",filial.get("idFilial"));
		sql.addFilterSummary("filial", criteria.get("sgFilial") + " - " + criteria.get("nmFilial"));
		
    	YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
		    	
    	sql.addCustomCriteria(" PC.DH_PEDIDO_COLETA between ? and ? ");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
				
		
		StringBuffer sub = new StringBuffer()
			.append(" pc.id_pedido_coleta in ( ")
			.append(" 	select id_pedido_coleta from evento_coleta ") 
			.append(" 		having sum(case when tp_evento_coleta ='TR' then 1 else 0 end) > 1 ")
			.append(" 		group by id_pedido_coleta )");
		sql.addCustomCriteria(sub.toString());
		
		
		sql.addOrderBy("mc.nr_manifesto, pc.nr_coleta");
		
		System.out.println("\n "+sql.getSql());
		
		for (int i = 0; i < sql.getCriteria().length; i++){
			System.out.println(sql.getCriteria()[i]);
		}
		
		return sql;		
	}
	
	public SqlTemplate montaSqlCOL6(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" * from ( ")
			.append(" select metr.nr_frota, ") 
			.append(" f.sg_filial || ' ' || LPAD(mc.nr_manifesto,8,0) manifesto ,")
			.append(" f.sg_filial || ' ' || LPAD(pc.nr_coleta,8,0) documento ,")
			.append(" ep.ds_endereco||' '||ep.nr_endereco endereco, ")
			.append(" p.nr_identificacao nr_identificacao, ")
			.append(" p.tp_identificacao tp_identificacao, ")
			.append(" p.nm_pessoa cliente, ")
			.append(" pc.QT_TOTAL_VOLUMES_INFORMADO volumes, ")
			.append(PropertyVarcharI18nProjection.createProjection("oc.ds_descricao_completa_i", "baixa")).append(",")
			.append(" ec.dh_evento baixa_, ")
			.append(" pc.dt_previsao_coleta dpc, ")
			.append( "row_number() over ( partition by pc.nr_coleta order by EC.DH_EVENTO desc) rn ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" pedido_coleta pc ")
		  	.append(" left join pessoa             p    on p.id_pessoa = pc.id_cliente ")
			.append(" left join manifesto_coleta   mc   on pc.id_manifesto_coleta = mc.id_manifesto_coleta ")
			.append(" left join controle_carga     cc   on mc.id_controle_carga = cc.id_controle_carga ")
			.append(" left join filial             f    on mc.id_filial_origem = f.id_filial ")
		  	.append(" left join evento_coleta      ec   ON ec.id_pedido_coleta = pc.id_pedido_coleta ")
		  	.append(" left join meio_transporte    metr on metr.id_meio_transporte = ec.id_meio_transporte	")
			.append(" left join ocorrencia_coleta  oc   on oc.id_ocorrencia_coleta = ec.id_ocorrencia_coleta ")
			.append(" left join endereco_pessoa    ep   on ep.ID_ENDERECO_PESSOA = pc.ID_ENDERECO_PESSOA ");
		sql.addFrom(query.toString());

		sql.addCriteria("ec.id_meio_transporte","=",criteria.get("idFrota"));
		
		sql.addCriteria("pc.id_filial_responsavel","=",criteria.get("idFilial"));
		
		sql.addCustomCriteria("cc.tp_status_controle_carga not in ('FE','CA') ");
	
		sql.addCustomCriteria("ec.tp_evento_coleta in ('TR', 'EX') )");
		
		sql.add("where rn = 1"); 
		
		List l = getFilialService().findNmSgFilialByIdFilial(Long.valueOf(""+criteria.get("idFilial")));
		TypedFlatMap t = (TypedFlatMap)l.get(0);
		
		sql.addFilterSummary("filial",t.getString("sgFilial") + " - " + t.getString("pessoa.nmFantasia"));
					
		System.out.println("\n "+sql.getSql());
		
		for (int i = 0; i < sql.getCriteria().length; i++){
			System.out.println(sql.getCriteria()[i]);
		}
		
		return sql;		
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}	
	
	
} 