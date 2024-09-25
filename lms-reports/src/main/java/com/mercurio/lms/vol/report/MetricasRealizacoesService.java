package com.mercurio.lms.vol.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vol.model.service.VolGruposFrotasService;


/**
 * Classe responsável pela geração do relatório Métricas Realizações.
 * Especificação técnica 12.01.01.03
 * @author Roberto Azambuja
 * 
 * @spring.bean id="lms.vol.metricasRealizacoesService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/metricasRealizacoes.jasper"
 */
public class MetricasRealizacoesService extends ReportServiceSupport {
	
	private FilialService filialService;
	private VolGruposFrotasService volGruposFrotasService;
	 /**
     * Método responsável pela geração do relatório 
     */
	
    public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap tfm = (TypedFlatMap)parameters;
    	
    	YearMonthDay dtIni = (YearMonthDay)tfm.getYearMonthDay("dataInicial");
    	YearMonthDay dtFim = (YearMonthDay)tfm.getYearMonthDay("dataFinal");
		
    	tfm.put("dataInicial",dtIni);
    	tfm.put("dataFinal",dtFim);
    	 
    	//busca o id da filial, foi necessário devido ao comportamento da popup manter grupos frota. pois essa não retorna o id da filial
    	if( tfm.getLong("filial.idFilial") == null ){
	    	List filial =  this.getFilialService().findLookupBySgFilial(tfm.getString("filial.sgFilial"),tfm.getString("tpAcesso"));
	        Iterator it = filial.iterator();
	        Map linha = (Map) it.next();
	        tfm.put("filial.idFilial", linha.get("idFilial"));
    	}
    	
        SqlTemplate sql = mountMainSql(tfm);
        
        sql.addFilterSummary("filial", tfm.getString("filial.sgFilial") + " - " + tfm.getString("filial.nmFantasia"));
        
        if ( tfm.getLong("grupo.idGrupoFrota" ) != null) {
	        List grupoFrota = this.getVolGruposFrotasService().findDsNomeById(tfm.getLong("grupo.idGrupoFrota"));
	        Iterator itfrota = grupoFrota.iterator();
	        Map linhaFrota = (Map)itfrota.next();
	        String dsGrupoFrota = (String)linhaFrota.get("dsNome");
	        sql.addFilterSummary("grupoFrota", dsGrupoFrota);
        }
                
		sql.addFilterSummary("periodoInicial",dtIni);
		sql.addFilterSummary("periodoFinal",dtFim);
        JRReportDataObject jr = executeQuery(sql.getSql(), tfm);

        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
       
        parametersReport.put("PARAM_PESQUISA", sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosRelatorio", tfm);
        jr.setParameters(parametersReport);
        return jr;
    }
    
  	public void configReportDomains(ReportDomainConfig config) {
       	config.configDomainField("TP_RECUSA", "DM_STATUS_RECUSA"); 
    }

    
    public JRDataSource executeSubReportEntregas(TypedFlatMap tfm) {
        SqlTemplate sql = mountSqlEntregas(tfm);
      
        JRDataSource jr = executeQuery(sql.getSql(), tfm).getDataSource(); 
        return jr;
    }
    
    public JRDataSource executeSubReportColetas(TypedFlatMap tfm) {
        SqlTemplate sql = mountSqlColetas(tfm);
      
        JRDataSource jr = executeQuery(sql.getSql(), tfm).getDataSource(); 
        return jr;
    }
    
    public JRDataSource executeSubReportMotivosEntregas(TypedFlatMap tfm) {
        SqlTemplate sql = mountSqlMotivosEntregas(tfm);
      
        JRDataSource jr = executeQuery(sql.getSql(), tfm).getDataSource(); 
        return jr;
    }
    
    public JRDataSource executeSubReportMotivosColetas(TypedFlatMap tfm) {
        SqlTemplate sql = mountSqlMotivosColetas(tfm);
      
        JRDataSource jr = executeQuery(sql.getSql(), tfm).getDataSource(); 
        return jr;
    }
    
    
    public JRDataSource executeSubReportRecusas(TypedFlatMap tfm) {
        SqlTemplate sql = mountSqlRecusas(tfm);
      
        JRDataSource jr = executeQuery(sql.getSql(), tfm).getDataSource(); 
        return jr;
    }
    /**
     * Para a chamada dos sub-reports
     * @param criteria
     * @return
     */
    public SqlTemplate mountMainSql(TypedFlatMap criteria) {
        SqlTemplate sql = createSqlTemplate();
        
        StringBuilder sb = new StringBuilder()
          .append("   1 ")
          .append("   from dual ");
        
          sql.addProjection(sb.toString());  
		return sql;
    
    }
    
    public SqlTemplate mountSqlEntregas(TypedFlatMap criteria) {
        SqlTemplate sql = createSqlTemplate();
        StringBuilder sb = new StringBuilder()
	        .append(" tipo ") 
	        .append(" ,count(*) ")
	        .append("from (     ")
	        .append("       select id_transportado id_meio_transporte ") 
	        .append("             ,'TE' tipo ")
	        .append("         from manifesto_entrega me ") 
	        .append("              inner join filial f ON f.id_filial = me.id_filial ") 
	        .append("              and me.id_filial = :filial.idFilial ")
	        .append(" 			   and trunc(me.dh_emissao) between :dataInicial and :dataFinal ")
	        .append("              inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ") 
	        .append("              inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ") 
	        .append("              inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
	        .append("              inner join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega") 
	        .append("              and oe.tp_ocorrencia = 'E'")
	        .append("       union all ")
	        .append("       select id_transportado id_meio_transporte ")
	        .append("             ,'NR' tipo ")
	        .append("         from manifesto_entrega me ") 
	        .append("           inner join filial f ON f.id_filial = me.id_filial ")
	        .append("           and me.id_filial = :filial.idFilial ") 
	        .append(" 			and trunc(me.dh_emissao) between :dataInicial and :dataFinal ")
	        .append("           inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
	        .append("           inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ") 
	        .append("           inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega") 
	        .append("           inner join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega") 
	        .append("           and oe.tp_ocorrencia = 'N'") 
	        .append("       union all")
	        .append("       select id_transportado id_meio_transporte ")
	        .append("             ,'RE' tipo ")
	        .append("        from (	")
	        .append("               select cg.id_transportado, med.id_docto_servico ") 
	        .append("                     ,count(id_manifesto_entrega_documento) ") 	
	        .append("                 from manifesto_entrega me ") 
	        .append("		            inner join filial f ON f.id_filial = me.id_filial ")
	        .append("                   and me.id_filial = :filial.idFilial ")	
	        .append(" 			        and trunc(me.dh_emissao) between :dataInicial and :dataFinal ")
	        .append("                   inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")	
	        .append("                   inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")	
	        .append("                   inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")	
	        .append("                 group by cg.id_transportado,med.id_docto_servico ") 
	        .append("                 having count(id_manifesto_entrega_documento) > 1 ")
	        .append("             ) ")
	        .append("  ) v_entr")
	        .append("    inner join meio_transporte mt on mt.id_meio_transporte = v_entr.id_meio_transporte ")
	        .append("    inner join GRUPO_FROTA_VEICULO vgf on vgf.id_meio_transporte = mt.id_meio_transporte ");
	     
            if (criteria.getLong("grupo.idGrupoFrota") != null) {
 		       sb.append(" and vgf.id_grupo_frota =:grupo.idGrupoFrota ");
 		    }
 	    
	        sb.append(" group by tipo ");
	        
	    
		sql.addProjection(sb.toString());  
        return sql;
    	
    }
    public SqlTemplate mountSqlColetas(TypedFlatMap criteria) {
        SqlTemplate sql = createSqlTemplate();
        StringBuilder sb = new StringBuilder()
	        .append(" tipo ") 
	        .append(" ,count(*) ")
	        .append("from (     ")
	        .append("       select ec.id_meio_transporte id_meio_transporte ") 
	        .append("             ,'CR' tipo ")
	        .append(" 		  from pedido_coleta pc ")
			.append(" 			   inner join filial f on f.id_filial = pc.id_filial_responsavel ")
			.append(" 			   inner join evento_coleta ec on ec.id_pedido_coleta = pc.id_pedido_coleta ")
			.append(" 		 where f.id_filial =:filial.idFilial ")
			.append(" 			and pc.dh_pedido_coleta between :dataInicial and :dataFinal ")
			.append("           and ec.tp_evento_coleta = 'EX'")
			.append("       union all ")
	        .append("       select ec.id_meio_transporte id_meio_transporte ")
	        .append("              ,'CNR' tipo ")
			.append(" 		  from pedido_coleta pc ")
			.append(" 			   inner join filial f on f.id_filial = pc.id_filial_responsavel ")
			.append(" 			   inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ")
			.append(" 		 where f.id_filial =:filial.idFilial ")
			.append(" 		   and trunc(pc.dh_pedido_coleta) between :dataInicial and :dataFinal ")
			.append("          and pc.tp_status_coleta <> 'EX'")
	        .append("  ) v_coletas")
	        .append("    inner join meio_transporte mt on mt.id_meio_transporte = v_coletas.id_meio_transporte ")
	        .append("    inner join GRUPO_FROTA_VEICULO vgf on vgf.id_meio_transporte = mt.id_meio_transporte ");
	     
            if (criteria.getLong("grupo.idGrupoFrota") != null) {
 		       sb.append(" and vgf.id_grupo_frota =:grupo.idGrupoFrota ");
 		    }
 	    
	        sb.append(" group by tipo ");
	        
	    
		sql.addProjection(sb.toString());  
        return sql;
    	
    }
    
    public SqlTemplate mountSqlMotivosEntregas(TypedFlatMap criteria) {
    	 SqlTemplate sql = createSqlTemplate();
         StringBuilder sb = new StringBuilder()
 	        .append(" tipo ") 
 	        .append(" ,count(*) ")
 	        .append("from (     ")
 	        .append("       select id_transportado id_meio_transporte ,")
 	        .append( PropertyVarcharI18nProjection.createProjection( "oe.ds_ocorrencia_entrega_i" , "tipo" ) )
 	        .append("         from manifesto_entrega me ") 
 	        .append("           inner join filial f ON f.id_filial = me.id_filial ")
 	        .append("           and me.id_filial = :filial.idFilial ") 
 	        .append(" 			and trunc(me.dh_emissao) between :dataInicial and :dataFinal ")
 	        .append("           inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
 	        .append("           inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ") 
 	        .append("           inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega") 
 	        .append("           inner join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega") 
 	        .append("           and oe.tp_ocorrencia = 'N'") 
 	        .append("  ) v_motivo")
 	        .append("    inner join meio_transporte mt on mt.id_meio_transporte = v_motivo.id_meio_transporte ")
 	        .append("    inner join GRUPO_FROTA_VEICULO vgf on vgf.id_meio_transporte = mt.id_meio_transporte ");
 	     
             if (criteria.getLong("grupo.idGrupoFrota") != null) {
  		       sb.append(" and vgf.id_grupo_frota =:grupo.idGrupoFrota ");
  		    }
  	    
 	        sb.append(" group by tipo ");
 	        
 	    
 		sql.addProjection(sb.toString());  
        return sql;
     	
    }
    
    public SqlTemplate mountSqlMotivosColetas(TypedFlatMap criteria) {
   	    SqlTemplate sql = createSqlTemplate();
        StringBuilder sb = new StringBuilder()
	        .append(" tipo ") 
	        .append(" ,count(*) ")
	        .append("from (     ")
            .append(" 		select ec.id_meio_transporte id_meio_transporte, ") 
            .append( PropertyVarcharI18nProjection.createProjection("oc.ds_descricao_resumida_i", "tipo" ))
	        .append(" 		  from pedido_coleta pc ")
			.append(" 			   inner join filial f on f.id_filial = pc.id_filial_responsavel ")
			.append(" 			   inner join evento_coleta ec on ec.id_pedido_coleta = pc.id_pedido_coleta ")
			.append(" 			   inner join ocorrencia_coleta oc on oc.id_ocorrencia_coleta = ec.id_ocorrencia_coleta ")
			.append(" 		 where f.id_filial =:filial.idFilial ")
			.append(" 			and trunc(pc.dh_pedido_coleta) between :dataInicial and :dataFinal ")
			.append("           and ec.tp_evento_coleta <> 'EX'")
	        .append("  ) v")
 	        .append("    inner join meio_transporte mt on mt.id_meio_transporte = v.id_meio_transporte ")
 	        .append("    inner join GRUPO_FROTA_VEICULO vgf on vgf.id_meio_transporte = mt.id_meio_transporte ");
 	     
             if (criteria.getLong("grupo.idGrupoFrota") != null) {
  		       sb.append(" and vgf.id_grupo_frota =:grupo.idGrupoFrota ");
  		    }
  	    
 	        sb.append(" group by tipo ");
 	        
 	    sql.addProjection(sb.toString());  
 	    return sql;
 	     	
    } 
    
    public SqlTemplate mountSqlRecusas(TypedFlatMap criteria) {
   	    SqlTemplate sql = createSqlTemplate();
        StringBuilder sb = new StringBuilder()
	        .append(" tp_recusa ")
	        .append(" ,count(*) ")
	        .append("from (     ")
            .append(" 		select cg.id_transportado id_meio_transporte")
			.append(" 			  ,tp_recusa  ")
			.append(" 		from controle_carga cg ")
			.append(" 			 left join manifesto m ON m.id_controle_carga = cg.id_controle_carga	")
			.append(" 			 left join manifesto_entrega me ON me.id_manifesto_entrega = m.id_manifesto ")
			.append(" 			 left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append(" 			 left join recusa r ON r.id_manifesto_entrega_documento = med.id_manifesto_entrega_documento ")
			.append(" 	   where r.id_filial =:filial.idFilial  ")
			.append(" 			 and trunc(r.dh_recusa) between :dataInicial and :dataFinal ")
			.append("            and tp_recusa is not null")
	        .append("  ) v")
 	        .append("    inner join meio_transporte mt on mt.id_meio_transporte = v.id_meio_transporte ")
 	        .append("    inner join GRUPO_FROTA_VEICULO vgf on vgf.id_meio_transporte = mt.id_meio_transporte ");
 	     
             if (criteria.getLong("grupo.idGrupoFrota") != null) {
  		       sb.append(" and vgf.id_grupo_frota =:grupo.idGrupoFrota ");
  		    }
  	    
 	        sb.append(" group by tp_recusa ");
 	        
 	    sql.addProjection(sb.toString());  
 	    return sql;
 	     	
    }

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public VolGruposFrotasService getVolGruposFrotasService() {
		return volGruposFrotasService;
	}

	public void setVolGruposFrotasService(
			VolGruposFrotasService volGruposFrotasService) {
		this.volGruposFrotasService = volGruposFrotasService;
	} 	        
}