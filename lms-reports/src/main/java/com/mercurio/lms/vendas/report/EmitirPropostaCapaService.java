package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.model.service.PropostaService;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

/**
 * 
 *
 * 
 * @spring.bean id="lms.vendas.emitirPropostaCapaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirPropostaCapa.jrxml"
 * 
 */
public class EmitirPropostaCapaService extends ReportServiceSupport {
	
	public JRReportDataObject execute(Map criteria) throws Exception {
		
		TypedFlatMap map = new TypedFlatMap(criteria);
		SqlTemplate sql = createSqlTemplate(map);

		// Seta os parametros
		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("idTabelaPreco", LongUtils.getLong(map.get("idTabelaPreco")));
		parametersReport.put("idDivisaoCliente", LongUtils.getLong(map.get("divisaoCliente.idDivisaoCliente")));
		parametersReport.put("dsSimbolo", map.getString("dsSimbolo"));
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		jr.setParameters(parametersReport);
		return jr;
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		/** SELECT */
		sql.addProjection("s.id_simulacao");
		sql.addProjection("s.nr_simulacao");
		sql.addProjection("S.DT_TABELA_VIGENCIA_INICIAL", "DT_TABELA_VIGENCIA_INICIAL");
		sql.addProjection("S.DT_VALIDADE_PROPOSTA", "DT_VALIDADE_PROPOSTA");
		sql.addProjection("U.NM_USUARIO", "NM_PROMOTOR");
		sql.addProjection("P.NM_PESSOA", "NM_CLIENTE");

		/** FROM */
		sql.addFrom("simulacao", "s");
		sql.addFrom("USUARIO", "U");
		sql.addFrom("PESSOA", "P");

		/** JOIN */
		// sql.addJoin("MUNICIPIO_TRT_CLIENTE.ID_TRT_CLIENTE","TRT_CLIENTE.ID_TRT_CLIENTE");

		/** WHERE */
		sql.addCriteria("s.id_simulacao", "=", criteria.getLong("simulacao.idSimulacao"));
		sql.addCustomCriteria("U.NR_MATRICULA = S.NR_MATRICULA_PROMOTOR");
		sql.addCustomCriteria("S.ID_CLIENTE = P.ID_PESSOA");

		/** ORDER BY */
		sql.addOrderBy("s.id_simulacao");

		return sql;
	}

	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("tpSituacaoAprovacao", "DM_STATUS_WORKFLOW");
	}
	
}
