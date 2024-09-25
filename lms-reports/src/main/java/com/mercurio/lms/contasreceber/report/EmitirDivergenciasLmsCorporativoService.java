package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;


/**
 * @author Hector junior
 *
 * @spring.bean id="lms.contasreceber.emitirDivergenciasLmsCorporativoService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirDivergenciasLMSCorporativo.jasper"
 */
public class EmitirDivergenciasLmsCorporativoService extends ReportServiceSupport{
	
	/** 
	 * Método invocado pela EmitirDivergenciasLMSCorporativoAction, é o método default do Struts
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		String sgFilial = tfm.getString("sgFilial");
		YearMonthDay dtEmissaoAte = tfm.getYearMonthDay("emissaoAte");
		
		boolean blFiltroFilial = StringUtils.isNotBlank(sgFilial);
		
		Object[] arrObj = null;
		SqlTemplate sqlTmp = createSqlTemplate();
		
		
		if (blFiltroFilial){
			sqlTmp.addFilterSummary("filialCobranca", tfm.getString("sgFilial") + " - " + tfm.getString("filial.pessoa.nmFantasia"));
			arrObj = new Object[] {
			// LMS
			sgFilial, dtEmissaoAte, sgFilial, dtEmissaoAte, sgFilial, dtEmissaoAte,
			// CORPORATIVO
			sgFilial, dtEmissaoAte, sgFilial, dtEmissaoAte, sgFilial, dtEmissaoAte
			};
		} else {
			arrObj = new Object[] {
			// LMS
			dtEmissaoAte, dtEmissaoAte, dtEmissaoAte,
			// LMS
			dtEmissaoAte, dtEmissaoAte, dtEmissaoAte};
		}

		sqlTmp.addFilterSummary("posicaoAte", dtEmissaoAte);
		
		StringBuffer sqlPrincipal = new StringBuffer();
		
		/** Monta o sql principal*/
		sqlPrincipal.append(sqlApenasLms(blFiltroFilial));
		sqlPrincipal.append("\nUNION ALL\n");
		sqlPrincipal.append(sqlFilialLmsDiferenteFilialCorporativo(blFiltroFilial));
		sqlPrincipal.append("\nUNION ALL\n");
		sqlPrincipal.append(sqlLiquidadoApenasCorporativo(blFiltroFilial));
		sqlPrincipal.append("\nUNION ALL\n");
		sqlPrincipal.append(sqlApenasCorporativo(blFiltroFilial));
		sqlPrincipal.append("\nUNION ALL\n");
		sqlPrincipal.append(sqlFilialCorporativoDiferenteFilialLms(blFiltroFilial));
		sqlPrincipal.append("\nUNION ALL\n");
		sqlPrincipal.append(sqlLiquidadoApenasLms(blFiltroFilial));
		sqlPrincipal.append("\n order by 1, 2 \n");
		
		/** Cria o JRReportDataObject para ser passado para o relatório */
		JRReportDataObject jr = executeQuery(sqlPrincipal.toString(), arrObj);
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sqlTmp.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);
		return jr;
	}

	/**
	 * 1ª CONSULTA ('O Documento de Serviço encontra-se apenas no LMS')
	 */
	private String sqlApenasLms(boolean blFiltroFilial){
		SqlTemplate sql = createSqlTemplate();
		getProjectionLMS(sql, 1);
		getFromLMS(sql);
		getWhereLMS(sql, blFiltroFilial);

		sql.addCustomCriteria("NOT exists ( \n" +
				  "		SELECT 1 \n" +
				  "		FROM   DIVERGENCIA_CORPORATIVO CORP \n" +
				  "		WHERE  corp.filial_conhecimento = lms.filial_conhecimento \n" +
				  "		AND    corp.conhecimento = lms.conhecimento \n" +
				  "		AND    corp.filial_cobranca = lms.filial_cobranca) ");		
		return sql.getSql();
	}
	
	/**
	 * 2ª CONSULTA ('Filial de cobrança do Documento de Serviço no LMS difere da filial de cobrança do CORPORATIVO')
	 */
	private String sqlFilialLmsDiferenteFilialCorporativo(boolean blFiltroFilial){
		SqlTemplate sql = createSqlTemplate();
		getProjectionLMS(sql, 2);
		getFromLMS(sql);
		getWhereLMS(sql, blFiltroFilial);

		sql.addCustomCriteria("exists ( \n" +
		   		  "		SELECT 1 \n" +
		   		  "		FROM   DIVERGENCIA_CORPORATIVO CORP \n" +
		   		  "		WHERE  corp.filial_conhecimento = lms.filial_conhecimento \n" +
		   		  "		AND    corp.conhecimento = lms.conhecimento \n" +
		   		  "		AND    corp.filial_cobranca <> lms.filial_cobranca) ");
		
		return sql.getSql();
	}
	
	/**
	 * 3ª CONSULTA ('O Documento de Serviço encontra-se liquidado apenas no CORPORATIVO')
	 */
	private String sqlLiquidadoApenasCorporativo(boolean blFiltroFilial){
		SqlTemplate sql = createSqlTemplate();
		getProjectionLMS(sql, 3);
		getFromLMS(sql);
		getWhereLMS(sql, blFiltroFilial);

		sql.addCustomCriteria("not exists ( \n" +
		   		  "		SELECT 1 \n" +
				  "		FROM   DIVERGENCIA_CORPORATIVO CORP \n" +
				  "		WHERE  corp.filial_conhecimento = lms.filial_conhecimento \n" +
				  "		AND    corp.conhecimento = lms.conhecimento \n" +
		   		  "		and		CORP.tp_situacao in ('3', '4') ) ");
		sql.addCustomCriteria("LMS.tp_situacao <> 'L'");
		return sql.getSql();
	}
	
	/**
	 * 4ª CONSULTA ('O Documento de Serviço encontra-se apenas no CORPORATIVO')
	 */
	private String sqlApenasCorporativo(boolean blFiltroFilial){
		SqlTemplate sql = createSqlTemplate(); 
		getProjectionCorporativo(sql, 4);
		getFromCorporativo(sql);
		getWhereCorporativo(sql, blFiltroFilial);
		sql.addCustomCriteria("not exists (SELECT 1 FROM   DIVERGENCIA_LMS LMS WHERE  corp.filial_conhecimento = lms.filial_conhecimento AND corp.conhecimento = lms.conhecimento) ");
		return sql.getSql();
	}
	
	/**
	 * 5ª CONSULTA ('Filial de cobrança do Documento de Serviço  no CORPORATIVO fifere da filial de cobrança do LMS')
	 */
	private String sqlFilialCorporativoDiferenteFilialLms(boolean blFiltroFilial){
		SqlTemplate sql = createSqlTemplate();
		getProjectionCorporativo(sql, 5);
		getFromCorporativo(sql);
		getWhereCorporativo(sql, blFiltroFilial);
		sql.addCustomCriteria("exists (SELECT 1 FROM   DIVERGENCIA_LMS LMS WHERE  corp.filial_conhecimento = lms.filial_conhecimento AND corp.conhecimento = lms.conhecimento AND corp.filial_cobranca <> lms.filial_cobranca) ");
		return sql.getSql();
	}

	/**
	 * 6ª CONSULTA ('O Documento de Serviço encontra-se liquidado apenas no LMS')
	 */
	private String sqlLiquidadoApenasLms(boolean blFiltroFilial){
		SqlTemplate sql = createSqlTemplate();
		getProjectionCorporativo(sql, 6);
		getFromCorporativo(sql);
		getWhereCorporativo(sql, blFiltroFilial);
		sql.addCustomCriteria("exists (SELECT 1 FROM DIVERGENCIA_LMS LMS WHERE  corp.filial_conhecimento = lms.filial_conhecimento AND corp.conhecimento = lms.conhecimento AND LMS.TP_SITUACAO = 'L' ) ");
		sql.addCustomCriteria("CORP.TP_SITUACAO not in ('3', '4')");
		return sql.getSql();
	}
	
	/**
	 * Monta a projection da consulta LMS
	 * @param sql
	 * @param nrConsulta
	 */
	private void getProjectionLMS(SqlTemplate sql, int nrConsulta){
		sql.addProjection("	FILIAL_CONHECIMENTO,\n" +
						"	CONHECIMENTO,\n" +
						"	FILIAL_COBRANCA,\n" +
						"	TP_SITUACAO AS TP_SITUACAO_CONHECIMENTO,\n" +
						"	TP_IDENTIFICACAO,\n" +
						"	CPF_CNPJ,\n" +
						"	TP_CONHECIMENTO,\n" +
						"	NOME_RESPONSAVEL,\n" +
						"	DATA_EMISSAO,\n" +
						"	VALOR_FRETE,\n" +
						getObservacao(sql, nrConsulta) + " "); 						  	  
	}
	
	/**
	 * Monta a projection da consulta Corporativo
	 * @param sql
	 */
	private void getProjectionCorporativo(SqlTemplate sql, int nrCosulta){
		sql.addProjection("FILIAL_CONHECIMENTO");
		sql.addProjection("CONHECIMENTO");
		sql.addProjection("FILIAL_COBRANCA");
		sql.addProjection("TP_SITUACAO AS TP_SITUACAO_CONHECIMENTO");
		sql.addProjection("TP_IDENTIFICACAO");
		sql.addProjection("CPF_CNPJ");
		sql.addProjection("TP_CONHECIMENTO");
		sql.addProjection("NOME_RESPONSAVEL");
		sql.addProjection("DATA_EMISSAO");
		sql.addProjection("VALOR_FRETE");
		sql.addProjection(getObservacao(sql, nrCosulta)); 
	}
	
	/**
	 * Monta o from da consulta Lms
	 * @param sql
	 */
	private void getFromLMS(SqlTemplate sql){
			sql.addFrom("DIVERGENCIA_LMS", "LMS"); 		
	}
	
	/**
	 * Monta o from  da consulta Corporativo
	 * @param sql
	 * @param nrConsulta
	 */
	private void getFromCorporativo(SqlTemplate sql){
		sql.addFrom("DIVERGENCIA_CORPORATIVO", "CORP");
	}
	
	/**
	 * Monta o where da consulta Lms
	 * @param sql
	 */
	private void getWhereLMS(SqlTemplate sql, boolean blFiltroFilial){		
		if (blFiltroFilial){
			sql.addCustomCriteria("FILIAL_COBRANCA = ?");
		}
		
		sql.addCustomCriteria("DATA_EMISSAO <= ?");
	}
	
	/**
	 * Monta o where da consulta Corporativo
	 * @param sql
	 * @param nrConsulta
	 */
	private void getWhereCorporativo(SqlTemplate sql, boolean blFiltroFilial){
		if (blFiltroFilial){
			sql.addCustomCriteria("CORP.FILIAL_COBRANCA = ? ");
		}
		
		sql.addCustomCriteria("CORP.DATA_EMISSAO <= ? ");	
	}
	
	private String getObservacao(SqlTemplate sql, int nrConsulta){
		if(nrConsulta == 1){
			return "		'O Documento de Serviço encontra-se apenas no LMS.' as OBSERVACAO ";
		}else if(nrConsulta == 2){
			return "		'Filial de cobrança do Documento de Serviço no LMS difere da filial de cobrança do CORPORATIVO.' as OBSERVACAO ";
		}else if(nrConsulta == 3){
			return "		'O Documento de Serviço encontra-se liquidado apenas no CORPORATIVO.' as OBSERVACAO ";
		}else if(nrConsulta == 4){
			return "		'O Documento de Serviço encontra-se apenas no CORPORATIVO.' as OBSERVACAO ";
		}else if(nrConsulta == 5){
			return "		'Filial de cobrança do Documento de Serviço no CORPORATIVO difere da filial de cobrança do LMS.' as OBSERVACAO ";
		}else{
			return "		'O Documento de Serviço encontra-se liquidado apenas no LMS.' as OBSERVACAO ";
		}
	}

}
