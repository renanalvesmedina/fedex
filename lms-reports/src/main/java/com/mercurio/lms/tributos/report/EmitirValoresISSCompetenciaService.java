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
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author Bruno Zaccolo
 * Alterado por : José Rodrigo Moraes em 20/07/2006
 *
 * @spring.bean id="lms.tributos.emitirValoresISSCompetenciaService"
 * @spring.property name="reportName" value="com/mercurio/lms/tributos/report/emitirValoresISSCompetencia.jasper"
 */
public class EmitirValoresISSCompetenciaService extends ReportServiceSupport {
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		Map parametersReport = new HashMap();
		
		/*  
		 * Instância a classe SqlTemplate, que retorna o sql para geração do relatório
		 * Seleciona qual relatório a ser impresso, através do checkbox 
		 */
		SqlTemplate sqlNotasFiscaisServico = null;
		SqlTemplate sqlConhecimentos = null;
		StringBuilder stringSql = new StringBuilder();
		
		if ( parameters.get("soTotais").equals("true")){
			sqlNotasFiscaisServico = getSqlTotaisNotasFiscaisServico(tfm);
			sqlConhecimentos = getSqlTotaisConhecimento(tfm);
			this.setReportName("com/mercurio/lms/tributos/report/emitirValoresISSCompetenciaTotais.jasper"); 
		}else {
			sqlNotasFiscaisServico = mountSlqNotaFiscalServico(tfm);
			sqlConhecimentos = mountSlqConhecimento(tfm);
			this.setReportName("com/mercurio/lms/tributos/report/emitirValoresISSCompetencia.jasper");
		}
		
		sqlConhecimentos.addCriteriaValue(sqlNotasFiscaisServico.getCriteria());
		
		stringSql
			.append(sqlNotasFiscaisServico.getSql())
			.append(" UNION ALL ")
			.append(sqlConhecimentos.getSql());
		
		JRReportDataObject jr = executeQuery(stringSql.toString(), sqlConhecimentos.getCriteria()); 
		
		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sqlConhecimentos.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());		
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,tfm.getString("tpFormatoRelatorio"));		
		
		jr.setParameters(parametersReport);
		 
		/** Seleciona qual relatório a ser impresso, através do checkbox */
		if ( parameters.get("soTotais").equals("true")) 
			this.setReportName("com/mercurio/lms/tributos/report/emitirValoresISSCompetenciaTotais.jasper");
		else
			this.setReportName("com/mercurio/lms/tributos/report/emitirValoresISSCompetencia.jasper");
	
		return jr;
	}
	
	/**
	 * Configura variáveis do relatório, para receberem valores não abreviados do domínio 
	 * Ex: situação = I  -  vai ser configurado, e exibido no relatório como Inativo
	 */
	public void configReportDomains(ReportDomainConfig config) {		
		super.configReportDomains(config);
	}

	/**
	 * Monta o sql analítico
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 21/02/2007
	 *
	 * @param tfm
	 * @return
	 * @throws Exception
	 *
	 */
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm){
	
		SqlTemplate sql = this.createSqlTemplate();
		sql.setDistinct();
		
		sql.addProjection("FI.SG_FILIAL || ' ' || DS.NR_DOCTO_SERVICO","NOTA_FISCAL");
		
		sql.addProjection("DS.ID_DOCTO_SERVICO", "ID_CONHECIMENTO");
		sql.addProjection("FI.SG_FILIAL", "SG_FILIAL_FI");
		sql.addProjection("DS.NR_DOCTO_SERVICO", "NR_DOCTO_SERVICO");
		sql.addProjection("DS.TP_DOCUMENTO_SERVICO", "TP_DOCTO_SERVICO");
		sql.addProjection("Pe.NM_FANTASIA", "NM_FANTASIA");
		
		
		sql.addProjection("Ds.DH_EMISSAO", "EMISSAO");
		
		sql.addProjection("DECODE(sads.ID_SERV_ADICIONAL_DOC_SERV,null," +
				          "      (select st.ds_servico_tributo from servico_tributo st where st.id_servico_tributo in (select pg.ds_conteudo from parametro_geral pg where NM_PARAMETRO_GERAL = 'ID_SERVICO_TRIBUTO_NFT')), " +
				          "      (select "+PropertyVarcharI18nProjection.createProjection("sa.ds_servico_adicional_i")+" from servico_adicional sa where sads.ID_SERVICO_ADICIONAL = sa.ID_SERVICO_ADICIONAL))","NOTAFISCALSERVICO");
		
		sql.addProjection("DECODE(DS.TP_SITUACAO, 'C', 0, IMs.PC_ALIQUOTA)","ALIQUOTA");
		sql.addProjection("DECODE(DS.TP_SITUACAO, 'C', 0, Ds.VL_TOTAL_DOC_SERVICO)", "VALORNOTA");	
		sql.addProjection("DECODE(DS.TP_SITUACAO, 'C', 0, DECODE(IMs.BL_RETENCAO_TOMADOR_SERVICO, 'S', 0, IMs.VL_IMPOSTO))","VALORISS");		
		sql.addProjection("DECODE(DS.TP_SITUACAO, 'C', 0, DECODE(IMs.BL_RETENCAO_TOMADOR_SERVICO, 'S', IMs.VL_IMPOSTO, 0))","VALOR_RETIDO");
		 
		sql.addProjection("FI.SG_FILIAL","SG_FILIAL");
		sql.addProjection("FI.ID_FILIAL","ID_FILIAL");		
		sql.addProjection("TO_CHAR(Ds.DH_EMISSAO, 'MM/YYYY') ","COMPETENCIA");
		sql.addProjection("Mu.NM_MUNICIPIO ","MUNICIPIO");
		sql.addProjection("Mu.ID_MUNICIPIO","ID_MUNICIPIO");		
		sql.addProjection("Pe.NM_PESSOA ","FILIAL");
		sql.addProjection("Fi.SG_FILIAL","SG_FILIAL_ORIGEM");
		sql.addProjection("PE.NM_FANTASIA","NM_FANTASIA_FATEND");
		
		getSqlDefault(tfm, sql);
		
		return sql;		
	}	
	
	private SqlTemplate mountSlqConhecimento(TypedFlatMap tfm){
		SqlTemplate sql = getSqlTemplate(tfm);
		
		sql.addJoin("IMs.TP_IMPOSTO", "'IS' AND " +   
				"DS.ID_DOCTO_SERVICO = IMs.ID_CONHECIMENTO ");
		
		sql.addOrderBy("3, 15, 16, 1");
		
		return sql;
	}
	
	private SqlTemplate mountSlqNotaFiscalServico(TypedFlatMap tfm){
		SqlTemplate sql = getSqlTemplate(tfm);
		 
		sql.addJoin("IMs.TP_IMPOSTO", "'IS' AND " +
				"DS.ID_DOCTO_SERVICO = IMs.ID_NOTA_FISCAL_SERVICO ");
		
		return sql;		
	}
	
	/**
	 * Monta o sql sintético
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 21/02/2007
	 *
	 * @param tfm
	 * @return
	 * @throws Exception
	 *
	 */
	private SqlTemplate getSqlTemplateTotais(TypedFlatMap tfm){
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("Pe.NM_FANTASIA", "NM_FANTASIA");
		sql.addProjection("FI.SG_FILIAL","SG_FILIAL");
		sql.addProjection("FI.ID_FILIAL","ID_FILIAL");				
		sql.addProjection("TO_CHAR(Ds.DH_EMISSAO, 'MM/YYYY') ","COMPETENCIA");
		sql.addProjection("Mu.NM_MUNICIPIO ","MUNICIPIO");
		sql.addProjection("SUM(DECODE(DS.TP_SITUACAO, 'C', 0, Ds.VL_TOTAL_DOC_SERVICO))", "VALORNOTA");	
		sql.addProjection("SUM(DECODE(DS.TP_SITUACAO, 'C', 0, DECODE(IMs.BL_RETENCAO_TOMADOR_SERVICO, 'S', 0, IMs.VL_IMPOSTO)))","VALORISS");		
		sql.addProjection("SUM(DECODE(DS.TP_SITUACAO, 'C', 0, DECODE(IMs.BL_RETENCAO_TOMADOR_SERVICO, 'S', IMs.VL_IMPOSTO, 0)))","VALOR_RETIDO");
		
		getSqlDefault(tfm, sql);
		
		sql.addGroupBy("TO_CHAR(Ds.DH_EMISSAO, 'MM/YYYY')");
		sql.addGroupBy("Mu.NM_MUNICIPIO");
		sql.addGroupBy("Pe.NM_FANTASIA");
		sql.addGroupBy("FI.SG_FILIAL");
		sql.addGroupBy("FI.ID_FILIAL");
		
		return sql;		
	}

	private SqlTemplate getSqlTotaisNotasFiscaisServico(TypedFlatMap tfm){
		
		SqlTemplate sql = getSqlTemplateTotais(tfm);
		
		sql.addJoin("IMs.TP_IMPOSTO", "'IS' AND " +
					"DS.ID_DOCTO_SERVICO = IMs.ID_NOTA_FISCAL_SERVICO ");
		
		return sql;
	} 
	
	private SqlTemplate getSqlTotaisConhecimento(TypedFlatMap tfm){
		
		SqlTemplate sql = getSqlTemplateTotais(tfm); 
		
		sql.addJoin("IMs.TP_IMPOSTO", "'IS' AND " +
				"DS.ID_DOCTO_SERVICO = IMs.ID_CONHECIMENTO ");
		
		sql.addOrderBy("2," +
				       "4," +
				       "5");
		
		return sql;
	}
	
	/**
	 * Monta o sql default
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 21/02/2007
	 *
	 * @param tfm
	 * @param sql
	 *
	 */
	private void getSqlDefault(TypedFlatMap tfm, SqlTemplate sql) { 
		
		sql.addFrom(" v_docto_servico_fin DS " +
				    "   inner join FILIAL Fi on ds.ID_FILIAL_ORIGEM = Fi.ID_FILIAL" +
				    " 	inner join PESSOA Pe on Fi.ID_FILIAL = Pe.ID_PESSOA " +
				    "   left outer join SERV_ADICIONAL_DOC_SERV sads on sads.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO, " +
				    " MUNICIPIO Mu " +
				    " 	inner join IMPOSTO_SERVICO IMs on Mu.ID_MUNICIPIO = IMs.ID_MUNICIPIO_INCIDENCIA");
		
		mountCriteriaDefault(tfm, sql);
	}

	/**
	 * Maonta os filtros default
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 21/02/2007
	 *
	 * @param tfm
	 * @param sql
	 *
	 */
	private void mountCriteriaDefault(TypedFlatMap tfm, SqlTemplate sql) {
		
		YearMonthDay dtCompInicial      = tfm.getYearMonthDay("competenciaInicial");
		YearMonthDay dtCompFinal        = tfm.getYearMonthDay("competenciaFinal");		

		/** critério da competência, se for preenchido as duas datas */
		if( !tfm.getString("competenciaInicial").equals("") ){
			
			if( !tfm.getString("competenciaFinal").equals("") ){
			  
	            sql.addCustomCriteria("to_char(Ds.DH_EMISSAO,'MM/YYYY') >= ? AND to_char(Ds.DH_EMISSAO,'MM/YYYY') <= ?");
	            sql.addCriteriaValue(JTFormatUtils.format(dtCompInicial, JTFormatUtils.SHORT, JTFormatUtils.MONTHYEAR));
	            sql.addCriteriaValue(JTFormatUtils.format(dtCompFinal, JTFormatUtils.SHORT, JTFormatUtils.MONTHYEAR));
	
	            sql.addFilterSummary("competenciaInicial",JTFormatUtils.format(dtCompInicial, JTFormatUtils.SHORT, JTFormatUtils.MONTHYEAR));
	            sql.addFilterSummary("competenciaFinal",JTFormatUtils.format(dtCompFinal, JTFormatUtils.SHORT, JTFormatUtils.MONTHYEAR));
	            
			} else {
				
				sql.addCustomCriteria("to_char(Ds.DH_EMISSAO,'MM/YYYY') >= ?");
				sql.addCriteriaValue(JTFormatUtils.format(dtCompInicial, JTFormatUtils.SHORT, JTFormatUtils.MONTHYEAR));
	
	            sql.addFilterSummary("competenciaInicial",JTFormatUtils.format(dtCompInicial, JTFormatUtils.SHORT, JTFormatUtils.MONTHYEAR));
			}
            
        } else if( !tfm.getString("competenciaFinal").equals("") ){
        	
        	sql.addCustomCriteria("to_char(Ds.DH_EMISSAO,'MM/YYYY') <= ?");
        	sql.addCriteriaValue(JTFormatUtils.format(dtCompFinal, JTFormatUtils.SHORT, JTFormatUtils.MONTHYEAR));

            sql.addFilterSummary("competenciaFinal",JTFormatUtils.format(dtCompFinal, JTFormatUtils.SHORT, JTFormatUtils.MONTHYEAR));
        }
		
		/** critério da filial */
		if (tfm.get("filial.idFilial") != null && StringUtils.isNotBlank(tfm.getString("filial.idFilial"))){
			sql.addCriteria("Fi.ID_FILIAL","=",tfm.get("filial.idFilial"),Long.class);
			sql.addFilterSummary("filial", tfm.getString("siglaFilial") +" - "+tfm.getString("nomeFilial"));
		}
		/** critério do municipio */
		if (tfm.get("municipio.idMunicipio") != null && StringUtils.isNotBlank(tfm.getString("municipio.idMunicipio"))){
			sql.addCriteria("Mu.ID_MUNICIPIO","=",tfm.get("municipio.idMunicipio"),Long.class);
			sql.addFilterSummary("municipio", tfm.getString("nomeMunicipio"));
		}
	}	
}


