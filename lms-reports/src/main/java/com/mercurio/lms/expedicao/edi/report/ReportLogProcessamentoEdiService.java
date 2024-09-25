package com.mercurio.lms.expedicao.edi.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Cleveland Júnior Soares
 *
 * @spring.bean id="lms.expedicao.edi.reportLogProcessamentoEdiService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/edi/report/emitirLogProcessamentoEdi.jasper"
 */
public class ReportLogProcessamentoEdiService extends ReportServiceSupport {
	
	public JRReportDataObject execute(Map criteria) throws Exception {

		TypedFlatMap map = new TypedFlatMap(criteria);
		SqlTemplate sql = createSqlTemplate(map);

		// Seta os parametros
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		jr.setParameters(parametersReport);
		
		return jr;
	}
	
	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) throws Exception {
		SqlTemplate sql = new SqlTemplate();
		
		/** SELECT */ 
		sql.addProjection("loae.id_Log_Atualizacao_Edi", "id_log_atualizacao_edi");
		sql.addProjection("loae.nr_Nota_Fiscal", "nr_nota_fiscal");
		sql.addProjection("loae.nr_Processamento","nr_processamento");
		sql.addProjection("pess.nr_Identificacao","nr_identificacao");
		sql.addProjection("pess.nr_Identificacao","cnpj_remetente");
		sql.addProjection("pess.nm_Fantasia", "nm_Fantasia");
		sql.addProjection("pess.nm_Pessoa", "nm_Pessoa");
		sql.addProjection("loae.ds_mensagem_erro", "ds_mensagem_erro");

		/** FROM */
		sql.addFrom("LOG_ATUALIZACAO_EDI", "LOAE");
		sql.addFrom("PESSOA", "PESS");

		/** JOIN */
		sql.addJoin("LOAE.ID_CLIENTE_REMETENTE", "PESS.ID_PESSOA");

		/** WHERE */
		Long nrProcessamento = criteria.getLong("nrProcessamento");
		if (nrProcessamento != null) {
			sql.addCriteria("LOAE.NR_PROCESSAMENTO", "=", nrProcessamento);
		}
		
		return sql;
	}
}