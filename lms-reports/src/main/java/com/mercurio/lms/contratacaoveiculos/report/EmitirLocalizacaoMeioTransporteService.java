package com.mercurio.lms.contratacaoveiculos.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.contratacaoveiculos.emitirLocalizacaoMeioTransporteService"
 * @spring.property name="reportName" value="com/mercurio/lms/contratacaoveiculos/report/emitirLocalizacaoMeioTransporte.jasper"
 */

public class EmitirLocalizacaoMeioTransporteService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap map = (TypedFlatMap)parameters;
		SqlTemplate sql = createSqlTemplate(map);

		Map parametersReport = new HashMap();
			parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
			parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
			parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
		JRReportDataObject jr = executeQuery(sql.getSql(),sql.getCriteria());
						   jr.setParameters(parametersReport);
		return jr;
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate sql = createSqlTemplate();
		//Projections
		sql.addProjection("F.SG_FILIAL");
		sql.addProjection("FP.NM_FANTASIA","NM_PESSOA");
		sql.addProjection("MT.TP_VINCULO");
		sql.addProjection("EMT.DH_EVENTO_INICIAL");
		sql.addProjection("EMT.TP_SITUACAO_MEIO_TRANSPORTE");
		sql.addProjection("P.NR_IDENTIFICACAO","NR_IDENTIFICACAO_P");
		sql.addProjection("P.TP_IDENTIFICACAO","TP_IDENTIFICACAO_VALUE_P");
		sql.addProjection("P.TP_IDENTIFICACAO","TP_IDENTIFICACAO_P");
		sql.addProjection("P.NM_PESSOA","NM_PESSOA_P");
		sql.addProjection("M.NR_IDENTIFICACAO","NR_IDENTIFICACAO_M");
		sql.addProjection("M.TP_IDENTIFICACAO","TP_IDENTIFICACAO_VALUE_M");
		sql.addProjection("M.TP_IDENTIFICACAO","TP_IDENTIFICACAO_M");
		sql.addProjection("M.NM_PESSOA","NM_PESSOA_M");
		sql.addProjection("MT.NR_FROTA");
		sql.addProjection("MT.NR_IDENTIFICADOR");
		
		StringBuffer sb = new StringBuffer();
					 sb.append("FILIAL F \n")
					   .append("LEFT JOIN REGIONAL_FILIAL RF ON F.ID_FILIAL = RF.ID_FILIAL  \n")
					   .append("INNER JOIN PESSOA FP ON FP.ID_PESSOA = F.ID_FILIAL  \n")
					   .append("LEFT JOIN REGIONAL R ON R.ID_REGIONAL = RF.ID_REGIONAL  \n")
					   .append("INNER JOIN EVENTO_MEIO_TRANSPORTE EMT ON EMT.ID_FILIAL = F.ID_FILIAL \n")
					   .append("INNER JOIN MEIO_TRANSPORTE MT ON EMT.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE \n")
					   .append("LEFT  JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = EMT.ID_CONTROLE_CARGA  \n")
					   .append("LEFT  JOIN PESSOA M ON CC.ID_MOTORISTA = M.ID_PESSOA  \n")
					   .append("LEFT  JOIN PESSOA P ON P.ID_PESSOA = CC.ID_PROPRIETARIO  ");		
		sql.addFrom(sb.toString());

		//WHERE OUTROS
		sql.addCustomCriteria("EMT.DH_EVENTO_INICIAL = (SELECT MAX(DH_EVENTO_INICIAL) FROM EVENTO_MEIO_TRANSPORTE WHERE ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE)");
		sql.addCustomCriteria("RF.DT_VIGENCIA_INICIAL <= ? AND RF.DT_VIGENCIA_FINAL >= ?");
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
        
		 
		//WHERE VIEW
		sql.addFilterSummary("regional",criteria.getString("regional.desc"));
		sql.addCriteria("RF.ID_REGIONAL","=",criteria.getLong("regional.id"));
		
		if (criteria.getLong("filial.idFilial") != null) {
			sql.addFilterSummary("filial",criteria.getString("filial.sgFilial") + " - " + criteria.getString("filial.pessoa.nmFantasia"));
			sql.addCriteria("RF.ID_FILIAL","=",criteria.getLong("filial.idFilial"));
		}
		
		sql.addFilterSummary("tipoVinculo",criteria.getString("tpVinculo.desc"));
		sql.addCriteria("MT.TP_VINCULO","=",criteria.getString("tpVinculo.value"));
		
		if (criteria.getLong("meioTransporte.idMeioTransporte") != null) {
			sql.addFilterSummary("meioTransporte",criteria.getString("meioTransporte2.nrFrota") + " - " + criteria.getString("meioTransporte.nrIdentificador"));
			sql.addCriteria("MT.ID_MEIO_TRANSPORTE","=",criteria.getLong("meioTransporte.idMeioTransporte"));
		}

		sql.addFilterSummary("evento",criteria.getString("tpEvento.desc"));
		sql.addCriteria("EMT.TP_SITUACAO_MEIO_TRANSPORTE","=",criteria.getString("tpEvento.value"));
		
		sql.addOrderBy("F.SG_FILIAL");
		sql.addOrderBy("MT.TP_VINCULO");
		sql.addOrderBy("MT.NR_FROTA");
		return sql;
	}
	public String formatIdentificacao(String tpIdentificacao,String nrIdentificacao) {
		return FormatUtils.formatIdentificacao(tpIdentificacao,nrIdentificacao);
	}
	
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_SITUACAO_MEIO_TRANSPORTE","DM_EVENTO_MEIO_TRANSPORTE");
		config.configDomainField("TP_IDENTIFICACAO_M","DM_TIPO_IDENTIFICACAO_PESSOA");
		config.configDomainField("TP_IDENTIFICACAO_P","DM_TIPO_IDENTIFICACAO_PESSOA");
		config.configDomainField("TP_VINCULO","DM_TIPO_VINCULO_VEICULO");
		super.configReportDomains(config);
	}
}
