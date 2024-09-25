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
	 * Método invocado pela EmitirCFOPAction
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
		
		/** Adiciona o tipo de relatório no Map */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		
		jr.setParameters(parametersReport);
		return jr;
	}
	
	/**
	 * Configura variáveis do relatório, para receberem valores não abreviados do domínio 
	 * Ex: situação = I  -  vai ser configurado, e exibido no relatório como Inativo
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
		
		/** Verifica se o status não é nulo, caso não seja, adiciona o status como critério na consulta,
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
