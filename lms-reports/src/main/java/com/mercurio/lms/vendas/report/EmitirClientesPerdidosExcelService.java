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
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirRelacaoClientesPerdidosExcel.jasper"
 */											
public class EmitirClientesPerdidosExcelService extends ReportServiceSupport {
	

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
		
		sql.addProjection("CP.ID_CLIENTE_PERDIDO", "ID_CLIENTE_PERDIDO");
		sql.addProjection("MO.DS_SIMBOLO", "DS_SIMBOLO");
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("SM.DS_SEGMENTO_MERCADO_I"), "DS_SEGMENTO_MERCADO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("RA.DS_RAMO_ATIVIDADE_I"), "DS_RAMO_ATIVIDADE");
		
		sql.addProjection("CP.NR_PESO_MEDIO", "NR_PESO_MEDIO");
		sql.addProjection("CP.NR_MEDIA_ENVIO", "NR_MEDIA_ENVIO");
		sql.addProjection("CP.NR_MEDIA_CTRC", "NR_MEDIA_CTRC");
		sql.addProjection("CP.TP_ABRANGENCIA", "TP_ABRANGENCIA");
		sql.addProjection("CP.TP_MODAL", "TP_MODAL");
		
		sql.addProjection("MO.SG_MOEDA", "SG_MOEDA");
		sql.addProjection("R.SG_REGIONAL||' - '||R.DS_REGIONAL", "SG_REGIONAL");
		sql.addProjection("F.SG_FILIAL||' - '||P_F.NM_FANTASIA", "SG_FILIAL");
		sql.addProjection("R.ID_REGIONAL", "ID_REGIONAL");
		sql.addProjection("F.ID_FILIAL", "ID_FILIAL");
		sql.addProjection("P_C.NM_PESSOA", "NM_PESSOA_CLIENTE");
		sql.addProjection("CP.TP_MOTIVO_PERDA", "TP_MOTIVO_PERDA");
		sql.addProjection("CP.TP_PERDA", "TP_PERDA");
		sql.addProjection("CP.NR_RECEITA_PERDIDA", "NR_RECEITA_PERDIDA");
		sql.addProjection("CP.NR_RECEITA_MEDIA", "NR_RECEITA_MEDIA");
		sql.addProjection("CP.DT_PERDA", "DT_PERDA");
		sql.addProjection("CP.dt_final_operacao", "DT_TERMINO");
		
		sql.addFrom("FILIAL", "F");
		sql.addFrom("PESSOA", "P_C");
		sql.addFrom("PESSOA", "P_F");
		sql.addFrom("CLIENTE", "C");
		sql.addFrom("MOEDA", "MO");
		sql.addFrom("CLIENTE_PERDIDO", "CP");
		
		sql.addFrom("RAMO_ATIVIDADE", "RA");
		sql.addFrom("SEGMENTO_MERCADO", "SM");
		sql.addFrom("REGIONAL_FILIAL", "RF");
		sql.addFrom("REGIONAL", "R");
		
		sql.addJoin("C.ID_CLIENTE", "CP.ID_CLIENTE");
		sql.addJoin("P_C.ID_PESSOA", "C.ID_CLIENTE");
		
		
		sql.addJoin("MO.ID_MOEDA", "CP.ID_MOEDA");
		
		sql.addJoin("RF.ID_FILIAL" , "F.ID_FILIAL");
		sql.addJoin("F.ID_FILIAL", "CP.ID_FILIAL");
		sql.addJoin("F.ID_FILIAL", "P_F.ID_PESSOA");
		sql.addJoin("RF.ID_REGIONAL" , "R.ID_REGIONAL");
				
		sql.addJoin("SM.ID_SEGMENTO_MERCADO", "CP.ID_SEGMENTO_MERCADO");
		sql.addJoin("RA.ID_RAMO_ATIVIDADE", "CP.ID_RAMO_ATIVIDADE(+)");
				
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
			sql.addCriteria("CP.ID_FILIAL", "=", idFilial);
			String dsFilial = parameters.getString("sgFilial") + " - " + parameters.getString("filial.pessoa.nmFantasia");
			sql.addFilterSummary("filial", dsFilial);
		}
		
				
		//RAMO ATUAÇÃO
		Long idSegmentoMercado = parameters.getLong("segmentoMercado.idSegmentoMercado");
		if(idSegmentoMercado!= null){
			sql.addCriteria("CP.ID_SEGMENTO_MERCADO", "=", idSegmentoMercado);
			String dsSegmento = parameters.getString("dsSegmento") ;
			sql.addFilterSummary("segmentoMercado", dsSegmento);
			
		}
		
		//RAMO ATUAÇÃO
		Long idRamoAtividade = parameters.getLong("ramoAtividade.idRamoAtividade");
		if(idRamoAtividade!= null){
			sql.addCriteria("CP.ID_RAMO_ATIVIDADE", "=", idRamoAtividade);
			String dsRamoAtividade = parameters.getVarcharI18n("dsRamoAtividade").getValue();
			sql.addFilterSummary("ramoAtividade", dsRamoAtividade);
		}
						
		//ABRANGENCIA E MODAL
		String tpAbrangencia = parameters.getString("tpAbrangencia");
		String tpModal = parameters.getString("tpModal");
		String dsAbrangencia = parameters.getString("dsAbrangencia");
		String dsModal = parameters.getString("dsModal");
		
		if(StringUtils.isNotBlank(tpAbrangencia)){
			sql.addCriteria("CP.TP_ABRANGENCIA", "=", tpAbrangencia);
			sql.addFilterSummary("abrangencia", dsAbrangencia);
		}
		
		if(StringUtils.isNotBlank(tpModal)){
			sql.addCriteria("CP.TP_MODAL", "=", tpModal);
			sql.addFilterSummary("modal", dsModal);
		}
					
		//DATA ATUAL
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		sql.addCriteria("RF.DT_VIGENCIA_INICIAL", "<=", dataAtual, YearMonthDay.class);
		sql.addCriteria("RF.DT_VIGENCIA_FINAL", ">=", dataAtual, YearMonthDay.class);

		YearMonthDay dataInicio = parameters.getYearMonthDay("dtInicio");
		if(dataInicio!= null){
			sql.addCriteria("CP.DT_PERDA", ">=", dataInicio, YearMonthDay.class);
			sql.addFilterSummary("dataInicio", dataInicio);
		}
		
		YearMonthDay dataFim = parameters.getYearMonthDay("dtFim");
		if(dataFim!= null){
			sql.addCriteria("CP.DT_PERDA", "<=", dataFim, YearMonthDay.class);
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
		config.configDomainField("TP_PERDA", "DM_TIPO_PERDA");	
		config.configDomainField("TP_MOTIVO_PERDA", "DM_MOTIVO_PERDA");
		config.configDomainField("TP_MODAL", "DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA", "DM_ABRANGENCIA");
	}
}