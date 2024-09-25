package com.mercurio.lms.portaria.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.portaria.emitirUtilizacaoDocasBoxesService"
 * @spring.property name="reportName" value="com/mercurio/lms/portaria/report/emitirUtilizacaoDocasBoxes.jasper"
 */
public class EmitirUtilizacaoDocasBoxesService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap flatMap = (TypedFlatMap)parameters;

		StringBuffer sb = new StringBuffer()
						.append("SELECT D.NR_DOCA, B.NR_BOX, P.NM_FANTASIA, NVL2(EMT.ID_BOX,'O','L') TP_SITUACAO, EMT.DH_EVENTO_INICIAL AS DH_EVENTO, \n")
						.append("CD.TP_OPERACAO, \n")
						.append("MT.NR_FROTA || ' - '|| MT.NR_IDENTIFICADOR  AS NR_IDENTIFICACAO, R.DS_ROTA, CC.ID_CONTROLE_CARGA, \n")
						.append("PT.NM_PESSOA AS NM_TERMINAL \n")
						.append("FROM TERMINAL T \n")
						.append("INNER JOIN FILIAL F ON F.ID_FILIAL = T.ID_FILIAL \n")
						.append("INNER JOIN PESSOA P ON P.ID_PESSOA = F.ID_FILIAL \n")
						.append("INNER JOIN PESSOA PT ON PT.ID_PESSOA = T.ID_TERMINAL \n")
						.append("INNER JOIN DOCA D ON D.ID_TERMINAL = T.ID_TERMINAL \n")
						.append("INNER JOIN BOX B ON B.ID_DOCA = D.ID_DOCA \n")
						.append("LEFT  JOIN EVENTO_MEIO_TRANSPORTE EMT ON EMT.ID_BOX = B.ID_BOX AND EMT.DH_EVENTO_INICIAL <= ? AND (EMT.DH_EVENTO_FINAL >= ? OR EMT.DH_EVENTO_FINAL IS NULL) \n")
						.append("LEFT  JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = EMT.ID_MEIO_TRANSPORTE \n")
						.append("LEFT  JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = EMT.ID_CONTROLE_CARGA \n")
						.append("LEFT  JOIN CARREGAMENTO_DESCARGA CD ON CD.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CD.DH_INICIO_OPERACAO <= EMT.DH_EVENTO_INICIAL AND DH_FIM_OPERACAO IS NULL \n")
						.append("LEFT  JOIN ROTA_IDA_VOLTA RIV ON RIV.ID_ROTA_IDA_VOLTA = CC.ID_ROTA_IDA_VOLTA \n")
						.append("LEFT  JOIN ROTA R ON R.ID_ROTA = RIV.ID_ROTA \n")
						.append("LEFT  JOIN ROTA_COLETA_ENTREGA RCE ON RCE.ID_ROTA_COLETA_ENTREGA = CC.ID_ROTA_COLETA_ENTREGA \n");
		   
		SqlTemplate sqlT = createSqlTemplate();
		Object[] criterias = new Object[8];
		criterias[0] = JTDateTimeUtils.getDataHoraAtual();
		criterias[1] = criterias[0];
		criterias[2] = criterias[0];
		criterias[3] = criterias[0];
		criterias[4] = criterias[0];
		criterias[5] = criterias[0];
		criterias[6] = criterias[0];
		criterias[7] = criterias[0];
		
		sb.append("WHERE T.DT_VIGENCIA_INICIAL <= ? AND (T.DT_VIGENCIA_FINAL >= ? OR T.DT_VIGENCIA_FINAL IS NULL) ")
			.append("AND  D.DT_VIGENCIA_INICIAL <= ? AND (D.DT_VIGENCIA_FINAL >= ? OR D.DT_VIGENCIA_FINAL IS NULL) ")
			.append("AND B.DT_VIGENCIA_INICIAL <= ? AND (B.DT_VIGENCIA_FINAL >= ? OR B.DT_VIGENCIA_FINAL IS NULL) ");
		
		
		if (flatMap.getLong("filial.idFilial") != null) {
			String sgFilial = flatMap.getString("filial.sgFilial");
			String nmFantasia = flatMap.getString("filial.pessoa.nmFantasia");
			sqlT.addFilterSummary("filial",sgFilial.concat(" - ").concat(nmFantasia));
			sb.append("AND ").append("F.ID_FILIAL = ? ");
			criterias = updateCriteria(criterias,flatMap.getLong("filial.idFilial"));
		}
		if (flatMap.getLong("terminal.id") != null) {
			sqlT.addFilterSummary("terminal",flatMap.getString("terminal.ds"));
			sb.append("AND ").append("T.ID_TERMINAL = ? ");
			criterias = updateCriteria(criterias,flatMap.getLong("terminal.id"));
		}
		if (flatMap.getLong("doca.id") != null) {
			sqlT.addFilterSummary("doca",flatMap.getString("doca.ds"));
			sb.append("AND ").append("D.ID_DOCA = ? ");
			criterias = updateCriteria(criterias,flatMap.getLong("doca.id"));
		}
		if (flatMap.getLong("box.id") != null) {
			sqlT.addFilterSummary("box",flatMap.getString("box.ds"));
			sb.append("AND ").append("B.ID_BOX = ? ");
			criterias = updateCriteria(criterias,flatMap.getLong("box.id"));
		}
		sb.append("ORDER BY PT.NM_PESSOA, D.NR_DOCA, B.NR_BOX");
		
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa",sqlT.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
		JRReportDataObject jr = executeQuery(sb.toString(),criterias);
						   jr.setParameters(parametersReport);
    	return jr;
	}
	
	public Object[] updateCriteria(Object[] criteria,Object add) {
		Object[] temp = new Object[criteria.length + 1];
		System.arraycopy(criteria, 0, temp, 0, criteria.length);
		criteria = temp;
		criteria[criteria.length - 1] = add;
		return criteria;
	}
	
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_OPERACAO","DM_TIPO_OPERACAO_CARREG_DESCARGA");
		super.configReportDomains(config);
	}

	public String formatDate(String strDate) {
		return JTFormatUtils.format(JTFormatUtils.buildDateTimeFromTimestampTzString(strDate));
	}
}
