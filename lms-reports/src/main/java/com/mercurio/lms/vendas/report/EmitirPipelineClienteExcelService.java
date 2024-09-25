package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.emitirPipelineClienteExcelService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirRelacaoPipelineClienteExcel.jasper"
 */											
public class EmitirPipelineClienteExcelService extends ReportServiceSupport {
	

	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		String tpFormatoRelatorio = ((TypedFlatMap) parameters).getString("tpFormatoRelatorio.valor");
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tpFormatoRelatorio);
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("PC.ID_PIPELINE_CLIENTE", "ID_PIPELINE_CLIENTE");
		sql.addProjection("MO.DS_SIMBOLO", "DS_SIMBOLO");
		sql.addProjection("MO.SG_MOEDA", "SG_MOEDA");
		sql.addProjection("R.SG_REGIONAL", "SG_REGIONAL");
		sql.addProjection("F.SG_FILIAL", "SG_FILIAL");
		sql.addProjection("R.ID_REGIONAL", "ID_REGIONAL");
		sql.addProjection("F.ID_FILIAL", "ID_FILIAL");
		sql.addProjection("NVL(P_C.NM_PESSOA, PC.NM_CLIENTE) ", "NM_PESSOA_CLIENTE");
		
		sql.addProjection("P_C.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
		sql.addProjection("P_C.TP_IDENTIFICACAO ", "TP_IDENTIFICACAO");
		sql.addProjection("PC.TP_NEGOCIACAO", "TP_NEGOCIACAO");
		sql.addProjection("PE.DT_EVENTO", "DT_EVENTO"); 
		sql.addProjection("PC.NR_MES_FECHAMENTO", "NR_MES_FECHAMENTO");
		sql.addProjection("PC.NR_ANO_FECHAMENTO", "NR_ANO_FECHAMENTO");
		
		sql.addProjection("U.NM_USUARIO", "NM_PESSOA_VENDEDOR");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("SM.DS_SEGMENTO_MERCADO_I"), "DS_SEGMENTO_MERCADO");
		sql.addProjection("PE.TP_PIPELINE_ETAPA ", "TP_PIPELINE_ETAPA");
		sql.addProjection("PC.PC_PROBABILIDADE", "PC_PROBABILIDADE");
		sql.addProjection("PC.TP_PROBABILIDADE", "TP_PROBABILIDADE");
		sql.addProjection("PC.VL_RECEITA_ATUAL", "VL_RECEITA_PREVISTA");
		sql.addProjection("PC.TP_SITUACAO", "TP_SITUACAO");
		sql.addProjection("PC.DT_PERDA", "DT_PERDA");
		sql.addProjection("PC.TP_MOTIVO_PERDA", "TP_MOTIVO_PERDA");
		
		sql.addFrom("FILIAL", "F");
		sql.addFrom("PESSOA", "P_C");
		sql.addFrom("USUARIO", "U");
		sql.addFrom("PESSOA", "P_F");
		sql.addFrom("CLIENTE", "C");
		sql.addFrom("MOEDA", "MO");
		sql.addFrom("PIPELINE_CLIENTE", "PC");
		sql.addFrom("PIPELINE_ETAPA", "PE");
		
		sql.addFrom("SEGMENTO_MERCADO", "SM");
		sql.addFrom("REGIONAL_FILIAL", "RF");
		sql.addFrom("REGIONAL", "R");
		
		sql.addJoin("C.ID_CLIENTE(+)", "PC.ID_CLIENTE");
		sql.addJoin("P_C.ID_PESSOA(+)", "C.ID_CLIENTE");
		sql.addJoin("U.ID_USUARIO", "PC.ID_USUARIO");
		
		sql.addJoin("MO.ID_MOEDA", "PC.ID_MOEDA");
		
		sql.addJoin("RF.ID_FILIAL" , "F.ID_FILIAL");
		sql.addJoin("F.ID_FILIAL", "PC.ID_FILIAL");
		sql.addJoin("F.ID_FILIAL", "P_F.ID_PESSOA");
		sql.addJoin("RF.ID_REGIONAL" , "R.ID_REGIONAL");
				
		sql.addJoin("SM.ID_SEGMENTO_MERCADO", "PC.ID_SEGMENTO_MERCADO");
		sql.addJoin("PC.ID_PIPELINE_CLIENTE", "PE.ID_PIPELINE_CLIENTE(+)");
				
		sql.addCustomCriteria("PE.TP_PIPELINE_ETAPA IS NOT NULL");
				
		//REGIONAL
		Long idRegional = parameters.getLong("regional.idRegional");
		Long idFilial = parameters.getLong("filial.idFilial");
			
		if(idRegional!= null){
			sql.addCriteria("R.ID_REGIONAL", "=", idRegional);
			String regional = parameters.getString("regional.siglaDescricao") ;
			sql.addFilterSummary("regional", regional);
		}
		
		//FILIAL
		
		if(idFilial != null) {
			sql.addCriteria("PC.ID_FILIAL", "=", idFilial);
			String dsFilial = parameters.getString("sgFilial") + " - " + parameters.getString("filial.pessoa.nmFantasia");
			sql.addFilterSummary("filial", dsFilial);
		}
		
		//USUARIO
		Long idUsuario = parameters.getLong("usuarioByIdUsuario.idUsuario");
		if(idUsuario!= null){
			sql.addCriteria("PC.ID_USUARIO", "=", idUsuario);
			String vendedor = parameters.getString("usuarioByIdUsuario.nmUsuario") ;
			sql.addFilterSummary("vendedor", vendedor);
		}
		
		//RAMO ATUAÇÃO
		Long idSegmentoMercado = parameters.getLong("segmentoMercado.idSegmentoMercado");
		if(idSegmentoMercado!= null){
			sql.addCriteria("PC.ID_SEGMENTO_MERCADO", "=", idSegmentoMercado);
			String dsSegmento = parameters.getString("dsSegmento") ;
			sql.addFilterSummary("segmentoMercado", dsSegmento);
			
		}
		
		//SITUACAO
		String tpSituacao = parameters.getString("tpSituacao");
		if(StringUtils.isNotBlank(tpSituacao)) {
			sql.addCriteria("PC.TP_SITUACAO", "=", tpSituacao);
			String dsSituacao = parameters.getString("dsSituacao");
			sql.addFilterSummary("situacao", dsSituacao);
		}
		
		//ABRANGENCIA E MODAL
		String tpAbrangencia = parameters.getString("tpAbrangencia");
		String tpModal = parameters.getString("tpModal");
		String dsAbrangencia = parameters.getString("dsAbrangencia");
		String dsModal = parameters.getString("dsModal");
		
		sql.addProjection("PR2.VL_RECEITA", "VL_RECEITA");
			
			
			
		sql.addProjection("PR2.TP_ABRANGENCIA"
				, "TP_ABRANGENCIA");
		sql.addProjection("PR2.TP_MODAL", "TP_MODAL");
				sql.addFrom("PIPELINE_RECEITA", "PR2");
				sql.addJoin("PR2.ID_PIPELINE_CLIENTE(+)", "PC.ID_PIPELINE_CLIENTE");
				
		//ETAPA		
		String tpPipelineEtapa = parameters.getString("tpEtapa");
		if(StringUtils.isNotBlank(tpPipelineEtapa)) {
			sql.addCriteria("PE.TP_PIPELINE_ETAPA", "=", tpPipelineEtapa);
			String dsEtapa = parameters.getString("dsEtapa");
			sql.addFilterSummary("etapa", dsEtapa);
		}else{
			sql.addCustomCriteria("((PE.TP_PIPELINE_ETAPA=(SELECT Max(pec.tp_pipeline_etapa) FROM pipeline_etapa pec WHERE pc.id_pipeline_cliente = pec.id_pipeline_cliente)) OR (PE.TP_PIPELINE_ETAPA IS NULL))");
		}
		
		//DATA ATUAL
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		sql.addCriteria("RF.DT_VIGENCIA_INICIAL", "<=", dataAtual, YearMonthDay.class);
		sql.addCriteria("RF.DT_VIGENCIA_FINAL", ">=", dataAtual, YearMonthDay.class);

		YearMonthDay dataInicio = parameters.getYearMonthDay("dtInicio");
		if(dataInicio!= null){
			sql.addCriteria("PE.DT_EVENTO(+)", ">=", dataInicio, YearMonthDay.class);
			sql.addFilterSummary("dataInicio", dataInicio);
		}
		
		YearMonthDay dataFim = parameters.getYearMonthDay("dtFim");
		if(dataFim!= null){
			sql.addCriteria("PE.DT_EVENTO(+)", "<=", dataFim, YearMonthDay.class);
			sql.addFilterSummary("dataFim", dataFim);
		}
		
		sql.addOrderBy("R.SG_REGIONAL");
		sql.addOrderBy("F.SG_FILIAL");
		sql.addOrderBy("P_C.NM_PESSOA");
		
		
		return sql;
	}
	
		/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.report.ReportServiceSupport#configReportDomains(com.mercurio.adsm.framework.report.ReportServiceSupport.ReportDomainConfig)
	 */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_PIPELINE_ETAPA", "DM_ETAPA_PIPELINE");
		config.configDomainField("TP_MOTIVO_PERDA", "DM_MOTIVO_PERDA");
		
		config.configDomainField("TP_MODAL1", "DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA1", "DM_ABRANGENCIA");
		
		config.configDomainField("TP_MODAL2", "DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA2", "DM_ABRANGENCIA");
		
		config.configDomainField("TP_MODAL3", "DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA3", "DM_ABRANGENCIA");
		
		config.configDomainField("TP_MODAL4", "DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA4", "DM_ABRANGENCIA");
		
		config.configDomainField("TP_SITUACAO", "DM_SITUACAO_PIPELINE");
		config.configDomainField("TP_NEGOCIACAO", "DM_NEGOCIACAO_PIPELINE");
		
	}
}