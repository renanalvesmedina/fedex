package com.mercurio.lms.tributos.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.RegiaoGeografica;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.RegiaoGeograficaService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author Hector junior
 *
 * @spring.bean id="lms.tributos.emitirAliquotasICMSService"
 * @spring.property name="reportName" value="com/mercurio/lms/tributos/report/emitirAliquotasICMS.jasper"
 */
public class EmitirAliquotasICMSService extends ReportServiceSupport {

	private UnidadeFederativaService unidadeFederativaService;
	private RegiaoGeograficaService  regiaoGeograficaService;
	
	/** 
	 * Método invocado pela EmitirAliquotasICMSAction, é o método default do Struts
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Seta o parâmetro de tipo de arquivo a ser gerado */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));		
		
		jr.setParameters(parametersReport);

		return jr;
	}
	
	/**
	 * Configura variáveis do relatório, para receberem valores não abreviados do domínio 
	 * Ex: situação = I  -  vai ser configurado, e exibido no relatório como Inativo
	 */
	public void configReportDomains(ReportDomainConfig config) {		
		config.configDomainField("TIPO_FRETE","DM_TIPO_FRETE");
		config.configDomainField("SITTRIB_REM","DM_SITUACAO_TRIBUTARIA");
		config.configDomainField("SITTRIB_DES","DM_SITUACAO_TRIBUTARIA");
		super.configReportDomains(config);
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception{
		
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("A.TP_TIPO_FRETE", "TIPO_FRETE");
		sql.addProjection("UFO.SG_UNIDADE_FEDERATIVA || ' - ' || UFO.NM_UNIDADE_FEDERATIVA", "UF_ORIGEM");
		sql.addProjection("UFD.SG_UNIDADE_FEDERATIVA || ' - ' || UFD.NM_UNIDADE_FEDERATIVA", "UF_DESTINO");
					
				
		sql.addProjection("A.TP_SITUACAO_TRIB_REMETENTE", "SITTRIB_REM");
		sql.addProjection("A.TP_SITUACAO_TRIB_DESTINATARIO", "SITTRIB_DES");
		sql.addProjection("A.PC_EMBUTE", "PERCENT_EMBUT");
		sql.addProjection("A.PC_ALIQUOTA", "PERCENT_ALIQ");
		sql.addProjection("A.DT_VIGENCIA_INICIAL", "VIGENCIA_INICIAL");
		sql.addProjection("A.DT_VIGENCIA_FINAL", "VIGENCIA_FINAL");
		sql.addProjection("TT.DS_TIPO_TRIBUTACAO_ICMS", "TIPO_TRIB");
		sql.addProjection(""+PropertyVarcharI18nProjection.createProjection("RGO.DS_REGIAO_GEOGRAFICA_I")+"", "REGIAO_GEOGRAFICA");
		
    	sql.addFrom("ALIQUOTA_ICMS", new StringBuffer("A")		
			.append(" \n INNER JOIN UNIDADE_FEDERATIVA   UFO        ON A.ID_UNIDADE_FEDERATIVA_ORIGEM     = UFO.ID_UNIDADE_FEDERATIVA ")		
			.append(" \n INNER JOIN TIPO_TRIBUTACAO_ICMS TT         ON TT.ID_TIPO_TRIBUTACAO_ICMS         = A.ID_TIPO_TRIBUTACAO_ICMS ")		
		
			.append(" \n  LEFT JOIN UNIDADE_FEDERATIVA UFD          ON A.ID_UNIDADE_FEDERATIVA_DESTINO    = UFD.ID_UNIDADE_FEDERATIVA ")
			.append(" \n  LEFT JOIN REGIAO_GEOGRAFICA  RGO 			ON A.ID_REGIAO_GEOGRAFICA_DESTINO     = RGO.ID_REGIAO_GEOGRAFICA ")
		
			.toString()
		);		
				
		String ufOrigem = tfm.getString("unidadeFederativaOrigem.idUnidadeFederativa");
		if(StringUtils.isNotBlank(ufOrigem)) {
			UnidadeFederativa unidadeFederativa = this.getUnidadeFederativaService().findById(Long.valueOf(ufOrigem));
			sql.addCriteria("A.ID_UNIDADE_FEDERATIVA_ORIGEM", "=", ufOrigem, Long.class);
			sql.addFilterSummary("ufOrigem", unidadeFederativa.getSgUnidadeFederativa()+" - "+unidadeFederativa.getNmUnidadeFederativa());
		}
		
		String ufDestino = tfm.getString("unidadeFederativaDestino.idUnidadeFederativa");
		if(StringUtils.isNotBlank(ufDestino)) {
			UnidadeFederativa unidadeFederativa = this.getUnidadeFederativaService().findById(Long.valueOf(ufDestino));
			sql.addCriteria("A.ID_UNIDADE_FEDERATIVA_DESTINO", "=", ufDestino);
			sql.addFilterSummary("ufDestino", unidadeFederativa.getSgUnidadeFederativa()+" - "+unidadeFederativa.getNmUnidadeFederativa());
		}
		
		String sitTribRem = tfm.getString("tpSituacaoTribRemetente");
		if(StringUtils.isNotBlank(sitTribRem)) {
			sql.addCriteria("A.TP_SITUACAO_TRIB_REMETENTE", "=", sitTribRem);
			sql.addFilterSummary("situacaoTributariaRemetente", this.getDomainValueService().findDomainValueDescription("DM_SITUACAO_TRIBUTARIA",sitTribRem));
		}
		
		String sitTribDest = tfm.getString("tpSituacaoTribDestinatario");
		if(StringUtils.isNotBlank(sitTribDest)) {
			sql.addCriteria("A.TP_SITUACAO_TRIB_DESTINATARIO", "=", sitTribDest);
			sql.addFilterSummary("situacaoTributariaDestinatario", this.getDomainValueService().findDomainValueDescription("DM_SITUACAO_TRIBUTARIA",sitTribDest));
		}
		
		String regGeografica = tfm.getString("regiaoGeografica.idRegiaoGeografica");
		if(StringUtils.isNotBlank(regGeografica)) {
			RegiaoGeografica regiaoGeografica = this.getRegiaoGeograficaService().findById(Long.valueOf(regGeografica));
			sql.addCriteria("A.ID_REGIAO_GEOGRAFICA_DESTINO", "=", regGeografica, Long.class);
			sql.addFilterSummary("regiaoGeografica", regiaoGeografica.getDsRegiaoGeografica());
		}
		
		/*Parametros utilizados para emitir o relatório de aliquotas através do período de datas
		do campo ALIQUOTA_ICMS.DT_VIGENCIA_FINAL - Solicitado pelo Joelson no Quest 20063*/		
		YearMonthDay dtVigenciaInicial = tfm.getYearMonthDay("dtVigenciaInicial");
		YearMonthDay dtVigenciaFinal   = tfm.getYearMonthDay("dtVigenciaFinal");
		if(dtVigenciaInicial != null && dtVigenciaFinal != null){
			sql.addCustomCriteria(" A.DT_VIGENCIA_FINAL BETWEEN ? AND ? ");
			sql.addCriteriaValue(new Object[]{dtVigenciaInicial,dtVigenciaFinal});
		}
				
		/** Resgata o parametro  do request */
		YearMonthDay dtVigencia = tfm.getYearMonthDay("dtVigencia");
		
		/** Verifica se o parametro não é nulo, caso não seja, adiciona o parametro como critério na consulta,
		 *  e seta o mesmo como um campo utilizado no filtro 
		 */
		if(dtVigencia != null) {
			sql.addCustomCriteria("(? BETWEEN A.DT_VIGENCIA_INICIAL AND A.DT_VIGENCIA_FINAL) ");
			sql.addCriteriaValue(dtVigencia);
			sql.addFilterSummary("vigencia", JTFormatUtils.format(dtVigencia));
		}
		
		String tpFrete = tfm.getString("tpTipoFrete");
		if(StringUtils.isNotBlank(tpFrete)) {
			sql.addCriteria("A.TP_TIPO_FRETE", "=", tpFrete);
			sql.addFilterSummary("tipoFrete", this.getDomainValueService().findDomainValueDescription("DM_TIPO_FRETE",tpFrete));
		}
		
		if( tfm.getLong("tipoTributacaoIcms.idTipoTributacaoIcms") != null ){
			sql.addCriteria("TT.ID_TIPO_TRIBUTACAO_ICMS","=",tfm.getLong("tipoTributacaoIcms.idTipoTributacaoIcms"));
			sql.addFilterSummary("tipoTributacao", tfm.getString("dsTipoTributacaoIcms"));
		}
		
		sql.addOrderBy("UFO.SG_UNIDADE_FEDERATIVA, UFD.SG_UNIDADE_FEDERATIVA, REGIAO_GEOGRAFICA");
		sql.addOrderBy("A.TP_TIPO_FRETE, A.TP_SITUACAO_TRIB_REMETENTE, A.TP_SITUACAO_TRIB_DESTINATARIO, A.DT_VIGENCIA_INICIAL");

		return sql;
	}

	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public RegiaoGeograficaService getRegiaoGeograficaService() {
		return regiaoGeograficaService;
}

	public void setRegiaoGeograficaService(
			RegiaoGeograficaService regiaoGeograficaService) {
		this.regiaoGeograficaService = regiaoGeograficaService;
	}

}
