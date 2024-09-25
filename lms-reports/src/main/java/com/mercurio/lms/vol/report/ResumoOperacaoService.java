package com.mercurio.lms.vol.report;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração dos relatórios de Coleta e Entrega da tela Visualiza Andamento
 * @author Luciano Flores
 * 
 * @spring.bean id="lms.vol.resumoOperacaoService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/resumoOperacao.jasper"
 */
public class ResumoOperacaoService extends ReportServiceSupport {
	private FilialService filialService;
	private DomainValue domainValue;
		

	/**
     * Método responsável pela geração do relatório 
     */
    public JRReportDataObject execute(Map parameters) throws Exception { 
        SqlTemplate sql = mountMainSql(parameters);
             
        //retorna os dados da filial
        List l = getFilialService().findNmSgFilialByIdFilial(Long.valueOf(""+parameters.get("idFilial")));
		TypedFlatMap t = (TypedFlatMap)l.get(0);
    	sql.addFilterSummary("filial",t.getString("sgFilial") + " - " + t.getString("pessoa.nmFantasia"));
                    
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

        Map parametersReport = new HashMap();
       
        parametersReport.put("PARAMETROS_PESQUISA", sql.getFilterSummary());
        parametersReport.put("USUARIO_EMISSOR", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("PARAMETROS_RELATORIO", parameters);
        jr.setParameters(parametersReport);
        return jr;
    	
    }
    
  
    
    public JRDataSource executeSubReportEntregas(Map tfm) {
        SqlTemplate sql = mountSqlEntregas(tfm);
      
        JRDataSource jr = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource(); 
        return jr;
    }
    
    public JRDataSource executeSubReportColetas(Map tfm) {
        SqlTemplate sql = mountSqlColetas(tfm);
      
        JRDataSource jr = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource(); 
        return jr;
    }
    
   
    /**
     * Para a chamada dos sub-reports
     * @param criteria
     * @return
     */
    public SqlTemplate mountMainSql(Map criteria) { 
        SqlTemplate sql = createSqlTemplate();
        
        StringBuilder sb = new StringBuilder()
          .append("   1 ")
          .append("   from dual ");
        
          sql.addProjection(sb.toString());  
		return sql;
    
    }
    
    public void configReportDomains(ReportDomainConfig config) {
		 config.configDomainField("FORMA_BAIXA", "DM_FORMA_BAIXA_ENTREGA"); 
	}
    
    public SqlTemplate mountSqlEntregas(Map criteria) {	
    	SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" 	metr.nr_frota, ")
			.append("   fme.sg_filial || ' ' || LPAD(me.nr_manifesto_entrega,8,0) manifesto ,") 
			.append("   fds.sg_filial || ' ' || LPAD(ds.nr_docto_servico,8,0) documento, ") 
			.append("	ds.ds_endereco_entrega_real endereco, ")
			.append("	p.nr_identificacao nr_identificacao, ")
			.append("	p.tp_identificacao tp_identificacao, ")
			.append("	p.nm_pessoa cliente, ")
			.append("	ds.qt_volumes volumes, ")
			.append("	med.dh_ocorrencia baixa, ")
			.append("   med.tp_forma_baixa FORMA_BAIXA, ")  
			.append("	"+PropertyVarcharI18nProjection.createProjection("oe.ds_ocorrencia_entrega_i", "baixa_")+", ")
			.append("   cg.dh_geracao dh_geracao, ")
			.append("	ds.dt_prev_entrega dpe ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" manifesto_entrega me ")
	    	.append(" 	left join filial fme ON fme.id_filial = me.id_filial ")
	    	.append(" 	left join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
	    	.append(" 	left join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
	    	.append(" 	left join meio_transporte metr on metr.id_meio_transporte = cg.id_transportado ")
	    	.append("	left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
	    	.append(" 	left join docto_servico ds on ds.id_docto_servico = med.id_docto_servico ")
	    	.append("   left join filial fds ON fds.id_filial = ds.id_filial_origem ")
	    	.append(" 	inner join pessoa p on p.id_pessoa = ds.ID_CLIENTE_DESTINATARIO ")
	    	.append(" 	left join nota_fiscal_conhecimento nfc ON nfc.id_nota_fiscal_conhecimento = med.id_nota_fiscal_conhecimento ")
	    	.append(" 	left join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega ");
		sql.addFrom(query.toString());

		sql.addCriteria("metr.id_meio_transporte","=",criteria.get("idFrota"));
		sql.addCriteria("fme.id_filial","=",criteria.get("idFilial"));
		sql.addCustomCriteria("cg.tp_status_controle_carga not in ('FE','CA')");
		sql.addCustomCriteria("med.tp_situacao_documento not in ('CANC')");
		
		sql.addOrderBy("me.nr_manifesto_entrega, ds.nr_docto_servico");
		
		return sql;		   
    }
    
    
    public SqlTemplate mountSqlColetas(Map criteria) {
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
			.append(" "+PropertyVarcharI18nProjection.createProjection("oc.ds_descricao_completa_i", "baixa_")+", ") 
			.append(" ec.dh_evento baixa, ")
			.append(" pc.dt_previsao_coleta dpc, ")
			.append(" cc.dh_geracao dh_geracao, ")
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
	
		sql.addCustomCriteria("(ec.tp_evento_coleta is null or ec.tp_evento_coleta <> 'CA') )");
		
		sql.add("where rn = 1"); 
							
		return sql;		
    }
    
	public DomainValue getDomainValue() {
		return domainValue;
	}

	public void setDomainValue(DomainValue domainValue) {
		this.domainValue = domainValue;
	}
	
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
    
}
