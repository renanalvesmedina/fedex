package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * @author 
 *
 * @spring.bean id="lms.expedicao.gerarCRTService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirCRT.jasper"
 */

public class GerarCRTService extends ReportServiceSupport {

	private ConfiguracoesFacade configuracoesFacade;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);
		
		Map parametersReport = new HashMap();
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		parametersReport.put("vlCorrigido", parameters.get("vlCorrigido"));
		parametersReport.put("tpEmissao", parameters.get("tpEmissao"));
		
		if(parameters.get("nrVia").equals("primeiraVia")) {
			parametersReport.put("nrVia", configuracoesFacade.getMensagem("viaNr1"));	
		}
		
		if(parameters.get("nrVia").equals("segundaVia")) {
			parametersReport.put("nrVia", configuracoesFacade.getMensagem("viaNr2"));	
		}

		if(parameters.get("nrVia").equals("terceiraVia")) {
			parametersReport.put("nrVia", configuracoesFacade.getMensagem("viaNr3"));	
		}

		jr.setParameters(parametersReport);
		
		return jr;
	}

	
	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		
		
		sql.addProjection("ci.ID_CTO_INTERNACIONAL", "ID_CTO_INTERNACIONAL");
		sql.addProjection("ci.DS_DADOS_REMETENTE", "DS_DADOS_REMETENTE");
		sql.addProjection("ci.SG_PAIS", "SG_PAIS");
		sql.addProjection("ci.NR_PERMISSO", "NR_PERMISSO");
		sql.addProjection("ci.NR_CRT", "NR_CRT");
		sql.addProjection("end.NM_PESSOA", "NM_PESSOA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("end.DS_TIPO_LOGRADOURO_I"), "DS_TIPO_LOGRADOURO");
		sql.addProjection("end.DS_ENDERECO", "DS_ENDERECO");
		sql.addProjection("end.NR_ENDERECO", "NR_ENDERECO");
		sql.addProjection("end.DS_COMPLEMENTO", "DS_COMPLEMENTO");
		sql.addProjection("end.NR_CEP", "NR_CEP");
		sql.addProjection("end.NM_MUNICIPIO", "NM_MUNICIPIO");
		sql.addProjection("end.SG_UNIDADE_FEDERATIVA", "SG_UNIDADE_FEDERATIVA");
		sql.addProjection("end.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sql.addProjection("end.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
		sql.addProjection("ci.DS_DADOS_DESTINATARIO", "DS_DADOS_DESTINATARIO");
		sql.addProjection("ci.DS_LOCAL_EMISSAO", "DS_LOCAL_EMISSAO");
		sql.addProjection("ci.DS_DADOS_CONSIGNATARIO", "DS_DADOS_CONSIGNATARIO");
		sql.addProjection("ci.DS_LOCAL_CARREGAMENTO", "DS_LOCAL_CARREGAMENTO");
		sql.addProjection("ci.DH_EMISSAO", "DH_EMISSAO");
		sql.addProjection("ci.DS_LOCAL_ENTREGA", "DS_LOCAL_ENTREGA");
		sql.addProjection("ci.DS_NOTIFICAR", "DS_NOTIFICAR");
		sql.addProjection("ci.DS_TRANSPORT_SUCESSIVOS", "DS_TRANSPORT_SUCESSIVOS");
		sql.addProjection("ci.DS_DADOS_MERCADORIA", "DS_DADOS_MERCADORIA");
		sql.addProjection("ci.PS_REAL", "PS_REAL");
		sql.addProjection("ci.PS_LIQUIDO", "PS_LIQUIDO");
		sql.addProjection("ci.VL_VOLUME", "VL_VOLUME");
		sql.addProjection("ci.DS_SIMBOLO", "DS_SIMBOLO");
		sql.addProjection("ci.VL_MERCADORIA", "VL_MERCADORIA");
		sql.addProjection("ci.DS_VALOR_MERCADORIA", "DS_VALOR_MERCADORIA");
		sql.addProjection("ci.VL_TOTAL_MERCADORIA", "VL_TOTAL_MERCADORIA");
		sql.addProjection("ci.DS_ANEXOS", "DS_ANEXOS");
		sql.addProjection("ci.DS_ADUANAS", "DS_ADUANAS");
		sql.addProjection("ci.VL_FRETE_EXTERNO", "VL_FRETE_EXTERNO");
		sql.addProjection("ci.DS_NOME_REMETENTE", "DS_NOME_REMETENTE");
		sql.addProjection("end.NR_TELEFONE_FO", "NR_TELEFONE_FO");
		sql.addProjection("end.NR_DDD_FO", "NR_DDD_FO");
		sql.addProjection("end.NR_DDI_FO", "NR_DDI_FO");
		sql.addProjection("end.NR_TELEFONE_FA", "NR_TELEFONE_FA");
		sql.addProjection("end.NR_DDD_FA", "NR_DDD_FA");
		sql.addProjection("end.NR_DDI_FA", "NR_DDI_FA");
		
		sql.addFrom(getProjectionsCto(parameters.getLong("idDoctoServico")), "ci");
		sql.addFrom(getProjectionsEndereco(), "end");
		
		return sql;
	}
	
	private String getProjectionsCto(Long idDoctoServico) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" ( SELECT ci.id_cto_internacional  id_cto_internacional, ")
			.append("		ci.ds_dados_remetente  ds_dados_remetente, ")
			.append("		ci.sg_pais  sg_pais, ")
			.append("		ci.nr_permisso  nr_permisso, ")
			.append("		ci.nr_crt  nr_crt, ")
			.append("		ci.ds_dados_destinatario  ds_dados_destinatario, ")
			.append("		ci.ds_local_emissao  ds_local_emissao, ")
			.append("		ci.ds_dados_consignatario  ds_dados_consignatario, ")
			.append("		ci.ds_local_carregamento  ds_local_carregamento, ")
			.append("		ds.dh_emissao  dh_emissao, ")
			.append("		ci.ds_local_entrega  ds_local_entrega, ")
			.append("		ci.ds_notificar  ds_notificar, ")
			.append("		ci.ds_transport_sucessivos  ds_transport_sucessivos, ")
			.append("		ci.ds_dados_mercadoria  ds_dados_mercadoria, ")
			.append("		ds.ps_real  ps_real, ")
			.append("		ci.ps_liquido  ps_liquido, ")
			.append("		ci.vl_volume  vl_volume, ")
			.append("		mo.ds_simbolo  ds_simbolo, ")
			.append("		ds.vl_mercadoria  vl_mercadoria, ")
			.append("		ci.ds_valor_mercadoria  ds_valor_mercadoria, ")
			.append("		ci.vl_total_mercadoria  vl_total_mercadoria, ")
			.append("		ci.ds_anexos  ds_anexos, ")
			.append("		ci.ds_aduanas  ds_aduanas, ")
			.append("		ci.vl_frete_externo  vl_frete_externo, ")
			.append("		ci.ds_nome_remetente  ds_nome_remetente ")
			.append("FROM  ")
			.append("		cto_internacional ci, ")
			.append("		docto_servico ds, ")
			.append("		moeda mo ")
			.append("WHERE ")
			.append("		ds.ID_MOEDA = mo.ID_MOEDA ")
			.append("		AND ci.ID_CTO_INTERNACIONAL = ds.ID_DOCTO_SERVICO ")
			.append("		AND ds.id_docto_servico = "+ idDoctoServico +" ) ");
		
		  return sb.toString(); 
	}

	
	
	private String getProjectionsEndereco() throws Exception {
		StringBuffer sb = new StringBuffer();
		  	sb.append(" ( SELECT 	pes.NM_PESSOA  NM_PESSOA, ")
	          	.append(PropertyVarcharI18nProjection.createProjection(" tpLog.DS_TIPO_LOGRADOURO_I")+ " DS_TIPO_LOGRADOURO, ")
       		  	.append("		endPes.DS_ENDERECO  DS_ENDERECO,")
       		  	.append("		endPes.NR_ENDERECO  NR_ENDERECO, ")
			  	.append("		endPes.DS_COMPLEMENTO  DS_COMPLEMENTO, ")
		        .append("		endPes.NR_CEP  NR_CEP, ")
		        .append("		mun.NM_MUNICIPIO  NM_MUNICIPIO, ")
	    		.append("		uf.SG_UNIDADE_FEDERATIVA  SG_UNIDADE_FEDERATIVA, ")
				.append("		pes.TP_IDENTIFICACAO  TP_IDENTIFICACAO, ")
				.append("		pes.NR_IDENTIFICACAO  NR_IDENTIFICACAO, ")
				.append("		fone.NR_TELEFONE_FO  NR_TELEFONE_FO, ")
				.append("		fone.NR_DDD_FO  NR_DDD_FO, ")
				.append("		fone.NR_DDI_FO  NR_DDI_FO, ")
				.append("		fax.NR_TELEFONE_FA  NR_TELEFONE_FA, ")
				.append("		fax.NR_DDD_FA  NR_DDD_FA, ")
				.append("		fax.NR_DDI_FA  NR_DDI_FA ")
				.append("FROM      	")
				.append("		EMPRESA emp, ")
				.append("		PESSOA pes, ")
				.append("		ENDERECO_PESSOA endPes, ")
				.append("		V_TIPO_LOGRADOURO_I tpLog, ")
	        	.append("		MUNICIPIO mun, ")
	        	.append("		UNIDADE_FEDERATIVA uf, ")
	        	.append("		"+getProjectionsFone()+"  fone, ")
	        	.append("		"+getProjectionsFax()+"  fax ")
	        	.append("WHERE   	")
	        	.append("		 emp.ID_EMPRESA = pes.ID_PESSOA ")
    			.append("AND 	 pes.ID_ENDERECO_PESSOA = endPes.ID_ENDERECO_PESSOA ")
				.append("AND 	 endPes.ID_TIPO_LOGRADOURO = tpLog.ID_TIPO_LOGRADOURO ")
				.append("AND 	 endPes.ID_MUNICIPIO = mun.ID_MUNICIPIO ")
				.append("AND 	 mun.ID_UNIDADE_FEDERATIVA = uf.ID_UNIDADE_FEDERATIVA ")
				.append("AND 	 emp.ID_EMPRESA = "+ SessionUtils.getEmpresaSessao().getIdEmpresa() +" ) ");


		
		  return sb.toString(); 
	}
	
	
	public JRDataSource executeTrecho(Long idDoctoServico) throws Exception {

		StringBuffer sb = new StringBuffer();
		
		sb.append("	SELECT 	tfi.ds_tramo_frete_internacional DS_TRAMO_FRETE_INTERNACIONAL, ")
			.append("			nvl(tci.VL_FRETE_REMETENTE,0)  VL_FRETE_REMETENTE, ")
			.append("			nvl(tci.VL_FRETE_DESTINATARIO,0)  VL_FRETE_DESTINATARIO, ")
			.append("			m.DS_SIMBOLO  DS_SIMBOLO, ")
			.append("			ci.ID_CTO_INTERNACIONAL ID_CTO_INTERNACIONAL ")
			.append(" FROM 		")
			.append("			cto_internacional ci, ")
			.append("			docto_servico ds, ")
			.append("			moeda m, ")
			.append("			trecho_cto_int tci, ")
			.append("			tramo_frete_internacional tfi ")
			.append(" WHERE		")
			.append("			ci.id_cto_internacional = tci.id_cto_internacional ")
			.append(" AND		ci.id_cto_internacional = ds.id_docto_servico ")
			.append(" AND		tci.id_tramo_frete_internacional = tfi.id_tramo_frete_internacional ")
			.append(" AND		ds.id_moeda = m.id_moeda ")
			.append(" AND 		ci.id_cto_internacional = "+idDoctoServico)
			.append(" ORDER BY	tfi.nr_tramo_frete_internacional ");

		return executeQuery(sb.toString(), new HashMap()).getDataSource();
	}

	public JRDataSource executeSumTrecho(Long idDoctoServico) throws Exception {

		StringBuffer sb = new StringBuffer();
		
		sb.append("	SELECT 	    sum(tci.vl_frete_destinatario) sum_frete_destinatario, ")
			.append("			sum(tci.vl_frete_remetente) sum_frete_remetente, ")
			.append("			m.ds_simbolo  ds_simbolo ")
			.append(" FROM 		")
			.append("			cto_internacional ci, ")
			.append("			docto_servico ds, ")
			.append("			moeda m, ")
			.append("			trecho_cto_int tci, ")
			.append("			tramo_frete_internacional tfi ")
			.append(" WHERE		")
			.append("			ci.id_cto_internacional = tci.id_cto_internacional ")
			.append(" AND		ci.id_cto_internacional = ds.id_docto_servico ")
			.append(" AND		tci.id_tramo_frete_internacional = tfi.id_tramo_frete_internacional ")
			.append(" AND		ds.id_moeda = m.id_moeda ")
			.append(" AND 		ci.id_cto_internacional = "+idDoctoServico)
			.append(" GROUP BY	m.ds_simbolo ");

		return executeQuery(sb.toString(), new HashMap()).getDataSource();
	}

	
	
	public JRDataSource executeDocAnexo(Long idDoctoServico) throws Exception {
		StringBuffer sb = new StringBuffer();
		
		sb.append(" ( SELECT 	ads.ds_anexo_docto_servico  DS_ANEXO_DOCTO_SERVICO, ")
			.append(" 	  		ds.id_docto_servico  ID_DOCTO_SERVICO, ")
			.append(" 	  		da.ds_documento  DS_DOCUMENTO ")
			.append(" FROM		 ")
			.append("	  		docto_servico ds, ")
			.append(" 	  		documento_anexo da, ")
			.append(" 	  		anexo_docto_servico ads ")
			.append(" WHERE 	 ")
			.append(" 	  		ds.id_docto_servico = da.id_cto_internacional ")
			.append(" AND 		da.id_anexo_docto_servico = ads.id_anexo_docto_servico ")
			.append(" AND 		ds.id_docto_servico = "+idDoctoServico+" ) ");
		 
		return executeQuery(sb.toString(), new HashMap()).getDataSource();
	}
	
	
	public JRDataSource executePontoParada(Long idDoctoServico) throws Exception {

		StringBuffer sb = new StringBuffer();
			sb.append(" ( SELECT 	pp.nm_ponto_parada NM_PONTO_PARADA ")
				.append(" FROM 		")
				.append("			docto_servico ds, ")
				.append(" 	  		aduana_cto_int aci, ")
				.append(" 	  		ponto_parada pp ")
				.append(" WHERE 	")
				.append(" 	  		ds.id_docto_servico = aci.id_cto_internacional ")
				.append(" 	  		AND aci.id_ponto_parada = pp.id_ponto_parada ")
				.append(" 	  		AND ds.id_docto_servico = "+idDoctoServico+" ) ");
		 
		return executeQuery(sb.toString(), new HashMap()).getDataSource();
	}

	public JRDataSource executeObs(Long idDoctoServico) throws Exception {

		StringBuffer sb = new StringBuffer();
		
		sb.append("	(SELECT 	ods.ds_observacao_docto_servico DS_OBSERVACAO_DOCTO_SERVICO ") 
			.append("FROM 		")
			.append("			docto_servico ds, ")
			.append("			observacao_docto_servico ods ")
			.append("WHERE		")
			.append("			ds.id_docto_servico = ods.id_docto_servico ") 
			.append("AND 		ds.id_docto_servico = "+idDoctoServico+" ) ");

		return executeQuery(sb.toString(), new HashMap()).getDataSource();
	}

		
	
	private String getProjectionsFone() throws Exception {
		StringBuffer sb = new StringBuffer();
			sb.append(" (SELECT  p.id_pessoa ID_PESSOA_FO, ")
			  .append(" 	 	te.nr_telefone NR_TELEFONE_FO, ")
			  .append(" 		te.NR_DDD NR_DDD_FO, ")
			  .append(" 		te.NR_DDI NR_DDI_FO ")
			  .append(" FROM	")
			  .append("			PESSOA p, ")
			  .append("			TELEFONE_ENDERECO te,  ")
			  .append("(")	
			  	.append("SELECT 	p2.ID_PESSOA ID_PESSOA, ")
			  	.append("		 	min(te2.ID_TELEFONE_ENDERECO) ID_TELEFONE_ENDERECO ")
				.append(" FROM	")
				.append("			TELEFONE_ENDERECO te2, ")
				.append("			PESSOA p2, ")
				.append("			ENDERECO_PESSOA ep2 ")
			  	.append(" WHERE	")
			  	.append(" 			p2.ID_ENDERECO_PESSOA   = ep2.ID_ENDERECO_PESSOA ")
			  	.append(" AND		te2.ID_ENDERECO_PESSOA  = ep2.ID_ENDERECO_PESSOA ")
			  	.append(" AND		te2.ID_PESSOA  = ep2.ID_PESSOA ")
			  	.append(" AND		te2.TP_USO  = 'FO' ")
			  	.append(" AND		te2.TP_TELEFONE  = decode(p2.TP_PESSOA,'F','R','C') ")
			  	.append(" GROUP BY  p2.ID_PESSOA ")
			  .append(") tel ")	
			  .append(" WHERE	")
			  .append("			p.ID_PESSOA = tel.ID_PESSOA(+) ")
			  .append("	AND		tel.ID_TELEFONE_ENDERECO = te.ID_TELEFONE_ENDERECO(+) ")
			  .append("	AND		p.ID_PESSOA = "+SessionUtils.getEmpresaSessao().getIdEmpresa()+" ) ");

		  return sb.toString(); 
	}
	
	private String getProjectionsFax() throws Exception {
		StringBuffer sb = new StringBuffer();
			sb.append(" (SELECT  p.id_pessoa ID_PESSOA_FA, ")
			  .append(" 	 	te.nr_telefone NR_TELEFONE_FA, ")
			  .append(" 		te.NR_DDD NR_DDD_FA, ")
			  .append(" 		te.NR_DDI NR_DDI_FA ")
			  .append(" FROM	")
			  .append("			PESSOA p, ")
			  .append("			TELEFONE_ENDERECO te,  ")
			  .append("(")	
			  	.append("SELECT 	p2.ID_PESSOA ID_PESSOA, ")
			  	.append("		 	min(te2.ID_TELEFONE_ENDERECO) ID_TELEFONE_ENDERECO ")
				.append(" FROM	")
				.append("			TELEFONE_ENDERECO te2, ")
				.append("			PESSOA p2, ")
				.append("			ENDERECO_PESSOA ep2 ")
			  	.append(" WHERE	")
			  	.append(" 			p2.ID_ENDERECO_PESSOA   = ep2.ID_ENDERECO_PESSOA ")
			  	.append(" AND		te2.ID_ENDERECO_PESSOA  = ep2.ID_ENDERECO_PESSOA ")
			  	.append(" AND		te2.ID_PESSOA  = ep2.ID_PESSOA ")
			  	.append(" AND		te2.TP_USO  = 'FA' ")
			  	.append(" AND		te2.TP_TELEFONE  = decode(p2.TP_PESSOA,'F','R','C') ")
			  	.append(" GROUP BY  p2.ID_PESSOA ")
			  .append(") tel ")	
			  .append(" WHERE	")
			  .append("			p.ID_PESSOA = tel.ID_PESSOA(+) ")
			  .append("	AND		tel.ID_TELEFONE_ENDERECO = te.ID_TELEFONE_ENDERECO(+) ")
			  .append("	AND		p.ID_PESSOA = "+SessionUtils.getEmpresaSessao().getIdEmpresa()+" ) ");

		  return sb.toString(); 
	}


	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	

}
