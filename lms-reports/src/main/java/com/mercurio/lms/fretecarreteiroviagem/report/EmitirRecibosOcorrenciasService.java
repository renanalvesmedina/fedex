package com.mercurio.lms.fretecarreteiroviagem.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.UncategorizedSQLException;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
*
* @spring.bean id="lms.contratacaoveiculos.emitirRecibosOcorrenciasService"
* @spring.property name="reportName" value="com/mercurio/lms/fretecarreteiroviagem/report/emitirRecibosOcorrencias.jasper"
*/
public class EmitirRecibosOcorrenciasService  extends ReportServiceSupport{

	private ConversaoMoedaService conversaoMoedaService;
	
	
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_SITUACAO_RECIBO", "DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE");
		config.configDomainField("TP_OCORRENCIA", "DM_TIPO_OCORRENCIA_RECIBO_CARRETEIRO");
		 
		super.configReportDomains(config);
	}
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap map = (TypedFlatMap) parameters;
		
		SqlTemplate sql = montaSql(map);
		montaFilterSummary(sql, map);
		
		Map parametersReport = new HashMap();		
		parametersReport.put("ID_MOEDA", Long.valueOf((String)map.get("moeda.idMoeda")));
		parametersReport.put("DS_SIMBOLO", map.get("moedaPais.moeda.dsSimbolo"));
		parametersReport.put("ID_PAIS", Long.valueOf((String)map.get("pais.idPais")));
		parametersReport.put("TP_RECIBO", map.getString("tpRecibo"));
		
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,
					parameters.get("tpFormatoRelatorio"));
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		// definindo os parametros do relatorio
		JRReportDataObject jr = null;
		try{
			jr = executeQuery(sql.getSql(), sql.getCriteria());
			jr.setParameters(parametersReport);		
		}catch(UncategorizedSQLException e){
			throw new InfrastructureException(e.getCause());
		}
		return jr; 

	}

	private SqlTemplate montaSql(TypedFlatMap map) {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("f.id_filial");
		sql.addProjection("f.sg_filial");
		sql.addProjection("pes_fil.nm_fantasia");
		sql.addProjection("p.id_proprietario");
		sql.addProjection("pes_prop.tp_pessoa");
		sql.addProjection("pes_prop.tp_identificacao");
		sql.addProjection("pes_prop.nr_identificacao");
		sql.addProjection("pes_prop.nm_pessoa");
		sql.addProjection("fil_cc_org.sg_filial", "sg_filial_origem");
		sql.addProjection("cc.nr_controle_carga");
		sql.addProjection("fil_cc_des.sg_filial", "sg_filial_destino");
		sql.addProjection("rfc.id_recibo_frete_carreteiro");
		sql.addProjection("rfc.nr_recibo_frete_carreteiro");
		sql.addProjection("rfc.dh_emissao");
		sql.addProjection("rfc.tp_situacao_recibo");
		sql.addProjection("rfc.dt_pagto_real");
		sql.addProjection("nvl(rfc.vl_bruto,0)", "vl_bruto");
		sql.addProjection("case when ofc.tp_ocorrencia = 'D' then nvl(ofc.vl_desconto,0) else 0 end", "vl_desconto");
		sql.addProjection("ofc.tp_ocorrencia");
		sql.addProjection("ofc.tp_ocorrencia", "tp_ocorrencia_aux");
		sql.addProjection("ofc.dt_ocorrencia_frete_carreteiro");
		sql.addProjection("ofc.ob_motivo");
		sql.addProjection("mp.id_pais");
		sql.addProjection("mp.id_moeda");

		addConvMoedaProjection(sql, map, "nvl(rfc.vl_bruto,0)", "VL_BRUTO_CONVERTIDO");
		addConvMoedaProjection(sql, map, "case when ofc.tp_ocorrencia = 'D' then nvl(ofc.vl_desconto,0) else 0 end", "VL_DESCONTO_CONVERTIDO");
		
		sql.addFrom("ocorrencia_frete_carreteiro", "ofc");
		sql.addFrom("recibo_frete_carreteiro", "rfc");
		sql.addFrom("proprietario", "p");
		sql.addFrom("pessoa", "pes_prop");
		sql.addFrom("controle_carga", "cc");
		sql.addFrom("filial", "fil_cc_org");
		sql.addFrom("filial", "fil_cc_des");
		sql.addFrom("filial", "f");
		sql.addFrom("pessoa", "pes_fil");
		sql.addFrom("moeda_pais", "mp");
				
		sql.addJoin("ofc.id_recibo_frete_carreteiro", "rfc.id_recibo_frete_carreteiro");
		sql.addJoin("rfc.id_proprietario", "p.id_proprietario");
		sql.addJoin("p.id_proprietario", "pes_prop.id_pessoa");
		sql.addJoin("rfc.id_controle_carga", "cc.id_controle_carga (+)");
		sql.addJoin("cc.id_filial_origem", "fil_cc_org.id_filial (+)");
		sql.addJoin("cc.id_filial_destino", "fil_cc_des.id_filial (+)");
		sql.addJoin("rfc.id_filial", "f.id_filial");
		sql.addJoin("f.id_filial", "pes_fil.id_pessoa");
		sql.addJoin("mp.id_moeda_pais", "rfc.id_moeda_pais");
			
		sql.addCustomCriteria("ofc.bl_desconto_cancelado = 'N'");
		sql.addCriteria("f.id_filial", "=", map.getLong("filial.idFilial"));
		sql.addCriteria("tp_recibo_frete_carreteiro", "=", map.getString("tpRecibo"));
		sql.addCriteria("p.tp_proprietario", "=", map.getString("tpCarreteiro"));
		sql.addCriteria("rfc.tp_situacao_recibo", "=", map.getString("tpSituacaoRecibo"));
		sql.addCriteria("ofc.tp_ocorrencia", "=", map.getString("tpOcorrencia"));
		sql.addCriteria("rfc.dh_emissao", ">=", map.getYearMonthDay("dtPeriodoInicial").toDateTimeAtMidnight());
		sql.addCriteria("rfc.dh_emissao", "<", map.getYearMonthDay("dtPeriodoFinal").toDateTimeAtMidnight().plus(1));
		
		sql.addOrderBy("f.sg_filial");
		sql.addOrderBy("f.id_filial");
		sql.addOrderBy("pes_prop.nm_pessoa");
		sql.addOrderBy("p.id_proprietario");		
		sql.addOrderBy("rfc.nr_recibo_frete_carreteiro");
		sql.addOrderBy("rfc.id_recibo_frete_carreteiro");
		sql.addOrderBy("ofc.dt_ocorrencia_frete_carreteiro");
		
		return sql;
	}

	/**
	 * Inclui uma projection no sqlTemplate com a função de conversão de moeda 
	 * montada
	 * 
	 * @author Vagner Huzalo
	 * 
	 * @param sql  	
	 * @param parameters 
	 * @param projectionField 
	 * @param alias
	 */
	private void addConvMoedaProjection(SqlTemplate sql, TypedFlatMap parameters,String projectionField,String alias){
		StringBuilder strBuild = new StringBuilder();
		String moeda = parameters.getString("moeda.idMoeda");
		String pais = parameters.getString("pais.idPais");
		strBuild.append("F_CONV_MOEDA(")
			.append("mp.id_pais,")	//Pais e Moeda de origem(documento)
			.append("mp.id_moeda,")
			.append(pais+",")		//Pais e moeda destido(paâmetros)
			.append(moeda+",")
			.append("rfc.dh_emissao,")
			.append(projectionField) 
			.append(")");
		sql.addProjection(strBuild.toString(), alias);
	}
	
	private void montaFilterSummary(SqlTemplate sql, TypedFlatMap parametros){
		if (!"".equals(parametros.getString("filial.sgFilial")) && !"".equals(parametros.getString("filial.pessoa.nmFantasia")))
			sql.addFilterSummary("filial", parametros.getString("filial.sgFilial") + " - " + parametros.getString("filial.pessoa.nmFantasia"));
		
		sql.addFilterSummary("tipoRecibo", parametros.getString("dsTpRecibo"));
		sql.addFilterSummary("tipoCarreteiro", parametros.getString("dsTpCarreteiro"));		
		sql.addFilterSummary("situacaoRecibo", parametros.getString("dsTpSituacaoRecibo"));
		sql.addFilterSummary("tipoOcorrencia", parametros.getString("dsTpOcorrencia"));
		sql.addFilterSummary("converterParaMoeda", parametros.getString("moeda.siglaSimbolo"));
		
		if (!"".equals(parametros.getString("dtPeriodoInicial")))
			sql.addFilterSummary("periodoEmissaoInicial", JTFormatUtils.format(parametros.getYearMonthDay("dtPeriodoInicial")));
		
		if (!"".equals(parametros.getString("dtPeriodoFinal")))
			sql.addFilterSummary("periodoEmissaoFinal", JTFormatUtils.format(parametros.getYearMonthDay("dtPeriodoFinal")));
	}
	
	@Deprecated
	public Double converteMoeda(Object[] parameters) {		
		return new Double(conversaoMoedaService.findConversaoMoeda((Long)parameters[0],(Long)parameters[1],
				(Long)parameters[2],(Long)parameters[3],
				JTFormatUtils.buildDateTimeFromTimestampTzString((String)parameters[5]).toYearMonthDay(),
				(BigDecimal)parameters[4]).doubleValue());
		
	}

	/**
	 * @param conversaoMoedaService The conversaoMoedaService to set.
	 */
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
}
