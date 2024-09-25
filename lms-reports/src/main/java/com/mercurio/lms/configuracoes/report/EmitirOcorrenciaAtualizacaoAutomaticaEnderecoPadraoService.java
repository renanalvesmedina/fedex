package com.mercurio.lms.configuracoes.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Hector Julian Esnaola Junior
 *
 * @spring.bean id="lms.configuracoes.emitirOcorrenciaAtualizacaoAutomaticaEnderecoPadraoService"
 * @spring.property name="reportName" value="com/mercurio/lms/configuracoes/report/emitirOcorrenciaAtualizacaoAutomaticaEnderecoPadrao.jasper"
 */
public class EmitirOcorrenciaAtualizacaoAutomaticaEnderecoPadraoService extends ReportServiceSupport {

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
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception{
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("vd.ds_valor_dominio_i", "OCORRENCIA") + ", " +
						  "p.tp_identificacao as TP_IDENTIFICACAO, " +
						  "p.nr_identificacao as NR_IDENTIFICACAO, " +
						  "p.nm_pessoa as PESSOA, " +
						  "oe.dt_ocorrencia as ATUALIZACAO, " +
						  "oe.tp_ocorrencia as TP_OCORRENCIA ");
		
		sql.addFrom("ocorrencia_endereco oe " +
					"inner join pessoa p on oe.id_pessoa = p.id_pessoa " +
					"inner join valor_dominio vd on tp_ocorrencia = vl_valor_dominio " +
					"inner join dominio d on d.id_dominio = vd.id_dominio "); 
		
		sql.addCriteria("d.nm_dominio","=", "DM_TIPO_OCORRENCIA_ENDERECO");
		
		YearMonthDay dtInicial = tfm.getYearMonthDay("dtInicial");
		YearMonthDay dtFinal = tfm.getYearMonthDay("dtFinal");
		
		if(dtInicial != null && dtFinal != null){
			sql.addCustomCriteria("( oe.dt_ocorrencia  between ? and ? )");
			sql.addCriteriaValue(dtInicial);
			sql.addFilterSummary("dataAtualizacaoInicial",dtInicial);
			sql.addFilterSummary("dataAtualizacaoFinal",dtFinal);
			sql.addCriteriaValue(dtFinal);
		}
		
		String tpOcorrenciaEndereco = tfm.getDomainValue("tpOcorrencia").getValue();
		if(!tpOcorrenciaEndereco.equals("")){
			sql.addCriteria("oe.tp_ocorrencia", "=", tpOcorrenciaEndereco);
			sql.addFilterSummary("tipoOcorrencia", super.getDomainValueService().findDomainValueDescription("DM_TIPO_OCORRENCIA_ENDERECO", tpOcorrenciaEndereco));
		}
		
		
		sql.addOrderBy("oe.dt_ocorrencia, p.nm_pessoa");
		
		return sql;

	}

}
