package com.mercurio.lms.tributos.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Hector junior
 *
 * @spring.bean id="lms.tributos.emitirCFOPService"
 * @spring.property name="reportName" value="com/mercurio/lms/tributos/report/emitirCFOP.jasper"
 */
public class EmitirCFOPService extends ReportServiceSupport {

	/** 
	 * M�todo invocado pela EmitirCFOPAction
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os par�metros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Inst�ncia a classe SqlTemplate, que retorna o sql para gera��o do relat�rio */
		SqlTemplate sql = getSqlTemplate(tfm);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		/** Adiciona os par�metros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usu�rio no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Adiciona o tipo de relat�rio no Map */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		
		jr.setParameters(parametersReport);
		return jr;
	}
	
	/**
	 * Configura vari�veis do relat�rio, para receberem valores n�o abreviados do dom�nio 
	 * Ex: situa��o = I  -  vai ser configurado, e exibido no relat�rio como Inativo
	 */
	public void configReportDomains(ReportDomainConfig config) {		
		config.configDomainField("SITUACAO","DM_STATUS");
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
		
		sql.addProjection("CD_CFOP", "CFOP");
		sql.addProjection("DS_CFOP", "DESCRICAO");
		sql.addProjection("TP_SITUACAO", "SITUACAO");
		
		sql.addFrom("CODIGO_FISCAL_OPERACAO", "CFO");
		
		/** Resgata a o status do request */
		String tpSituacao = tfm.getString("status");
		
		/** Verifica se o status n�o � nulo, caso n�o seja, adiciona o status como crit�rio na consulta,
		 *  e seta o mesmo como um campo utilizado no filtro 
		 */
		if(StringUtils.isNotBlank(tpSituacao)) {
			sql.addCriteria("CFO.TP_SITUACAO", "=", tpSituacao);
			sql.addFilterSummary("situacao", this.getDomainValueService().findDomainValueDescription("DM_STATUS",tpSituacao));
		}
		
		sql.addOrderBy("CFO.CD_CFOP");

		return sql;
	}

}
