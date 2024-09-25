package com.mercurio.lms.expedicao.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 Classe responsável pela geração do Relatório de AWBs Emitidos e Cancelados
 * 
 */
public class EmitirRelatorioAWBEmitidosCanceladosService extends ReportServiceSupport {
	
	

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		 SqlTemplate sql = getSql(new TypedFlatMap(parameters));
	     JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
	     
	     Map parametersReport = new HashMap();
	     parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		 parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
         parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		 jr.setParameters(parametersReport);
		return jr;
		
	}
	
	private SqlTemplate getSql(TypedFlatMap parameters) {
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("aero1.sg_aeroporto", "Aeroporto_Origem");
		sql.addProjection("p.nm_pessoa", "Cia_Aerea");
		sql.addProjection("lpad(a.nr_awb,7,0) || '-' || dv_awb", "N_AWB");
		sql.addProjection("aero2.sg_aeroporto", "Aeroporto_Destino");
		sql.addProjection("to_char(a.dh_emissao,'DD/MM/YYYY HH24:MI')", "Data_Hora_Emissao");
		sql.addProjection("a.ps_total", "Peso_Real");
		sql.addProjection("a.ps_cubado", "Peso_Cubado");
		sql.addProjection("sum(ds.vl_total_doc_servico)", "Receita_Conhecimentos");
		sql.addProjection("a.vl_frete", "Custo_AWB");
		sql.addProjection("a.vl_frete * 100 / sum(ds.vl_total_doc_servico)", "Custo_Receita");
		sql.addProjection("substr(v.ds_valor_dominio_i,7,length(v.ds_valor_dominio_i)-7)", "Situacao");
		
		sql.addFrom("awb", "a");
		sql.addFrom("cto_awb", "ca");
		sql.addFrom("cia_filial_mercurio", "cfm");
		sql.addFrom("pessoa", "p");
		sql.addFrom("aeroporto", "aero1");
		sql.addFrom("aeroporto", "aero2");
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("dominio", "d");
		sql.addFrom("valor_dominio", "v");
		
		sql.addJoin("ca.id_awb(+)", "a.id_awb");
    	sql.addJoin("ca.id_conhecimento", "ds.id_docto_servico(+)");
    	
    	//CIA AEREA
   	 	Long ciaAerea = parameters.getLong("idEmpresa");
   	 	sql.addCriteria("cfm.id_empresa", "=", ciaAerea);
   	 	if(ciaAerea != null){
   	 		sql.addFilterSummary("ciaAerea",  parameters.getLong("dsCiaAerea"));
   	 	}
   	 	
    	
	   	//AEROPORTO ORIGEM
	   	Long aeroportoOrigem = parameters.getLong("idAeroportoOrigem");
	   	sql.addCriteria("a.ID_AEROPORTO_ORIGEM", "=", aeroportoOrigem);
   	 	sql.addFilterSummary("aeroportoDeOrigem",  parameters.getString("dsAeroportoDeOrigem"));
	   	
	   	//AEROPORTO DESTINO
	   	Long aeroportoDestino = parameters.getLong("idAeroportoDestino");
	   	sql.addCriteria("a.ID_AEROPORTO_DESTINO", "=", aeroportoDestino);
	   	sql.addFilterSummary("aeroportoDeDestino",  parameters.getString("dsAeroportoDeDestino"));
    	
    	sql.addJoin("cfm.id_cia_filial_mercurio", "a.id_cia_filial_mercurio");
    	sql.addJoin("cfm.id_empresa", "p.id_pessoa");
    	sql.addJoin("a.id_aeroporto_origem", "aero1.id_aeroporto");
    	sql.addJoin("a.id_aeroporto_destino", "aero2.id_aeroporto");
    	sql.addJoin("v.id_dominio", "d.id_dominio");
    	
    	//SITUACAO AWB
    	String situacao = parameters.getString("tpStatusAwb");
    	List listSituacao = new ArrayList();
    	listSituacao.add("E");
    	listSituacao.add("C");
    	if(situacao != null){
    		sql.addCriteria("a.tp_status_awb", "=", situacao);
    		sql.addFilterSummary("situacao",  parameters.getString("dsSituacao"));
    	}else{
    		sql.addCriteriaIn("a.tp_status_awb",listSituacao);
    	}
		
		//PERIODO EMISSAO
    	YearMonthDay dataInicio = parameters.getYearMonthDay("dtEmissaoInicial");
    	YearMonthDay dataFim = parameters.getYearMonthDay("dtEmissaoFinal");
    	final String PERIODO_EMISSAO_INICIO = "a.dh_emissao >= to_date('"+ JTFormatUtils.format(dataInicio) +"', 'dd/mm/yyyy')";
    	final String PERIODO_EMISSAO_FIM = "a.dh_emissao < to_date('" + JTFormatUtils.format(dataFim.plusDays(1)) + "', 'dd/mm/yyyy')"; 
    	sql.addCustomCriteria(PERIODO_EMISSAO_INICIO);
    	sql.addCustomCriteria(PERIODO_EMISSAO_FIM);
    	sql.addFilterSummary("periodoEmissao",  JTFormatUtils.format(dataInicio) + " até " +  JTFormatUtils.format(dataFim) );
    	
    	sql.addCustomCriteria("d.nm_dominio ='DM_STATUS_AWB'");
    	
    	sql.addJoin("a.tp_status_awb", "v.vl_valor_dominio");
    	
    	sql.addGroupBy("aero1.sg_aeroporto, p.nm_pessoa, lpad(a.nr_awb,7,0) || '-' || dv_awb, aero2.sg_aeroporto, to_char(a.dh_emissao,'DD/MM/YYYY HH24:MI'), a.ps_total, a.ps_cubado, a.vl_frete, substr(v.ds_valor_dominio_i,7,length(v.ds_valor_dominio_i)-7) ");
    	sql.addOrderBy("aero1.sg_aeroporto, p.nm_pessoa, to_char(a.dh_emissao,'DD/MM/YYYY HH24:MI'), aero2.sg_aeroporto ");
  
		return sql;
	}
	
	private String getFiltros(Map parameters){
		StringBuffer sb =new StringBuffer();
		return sb.toString();
	}
	
}
