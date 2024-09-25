package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 * @spring.bean id="lms.vendas.emitirRelatorioProdutividadeVendasService"
 * @spring.property name="reportName"
 *  				value="com/mercurio/lms/vendas/report/emitirRelatorioProdutividadeVendas.jasper"
 */
public class EmitirRelatorioProdutividadeVendasService extends ReportServiceSupport {
	
	private static String PDF_REPORT_NAME = "com/mercurio/lms/vendas/report/emitirRelatorioProdutividadeVendas.jasper";
    private static String EXCEL_REPORT_NAME = "com/mercurio/lms/vendas/report/emitirRelatorioProdutividadeVendasExcel.jasper";
    private static String EXCEL_DETAIL_REPORT_NAME = "com/mercurio/lms/vendas/report/emitirRelatorioProdutividadeVendasDetalhadoExcel.jasper";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap map = new TypedFlatMap(criteria);
		Boolean relatorioDetalhado = (Boolean) criteria.get("relatorioDetalhado");
		SqlTemplate sql = createSqlTemplate(map);
		
		if(relatorioDetalhado){
			addCustomOrderBy(sql);
		}else {
			addOrderBy(sql);
		}
		
		String formato = (String) criteria.get("formatoRelatorio"); 
		
		if(formato.equals("pdf")){
			this.setReportName(PDF_REPORT_NAME);				
		} else {
			this.setReportName(EXCEL_REPORT_NAME);
			if(relatorioDetalhado){
				this.setReportName(EXCEL_DETAIL_REPORT_NAME);
				buildSqlTemplateDetalhado(sql);
			}
			formato = "csv";
		}
		
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("filialSolicitante", SessionUtils.getFilialSessao().getSgFilial());
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, formato);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		jr.setParameters(parametersReport);
		
		return jr;
	}
	
	private void buildSqlTemplateDetalhado(SqlTemplate sql) {
		sql.addProjection(createSolicitacaoAnaliseCreditoTemplate(), "dtHrSolicitacaoAnaliseCredito");
		sql.addProjection(createDtHrAprovacaoNivelTemplate("P"), "dtHrAprovacaoNivel1");
		sql.addProjection(createDtHrAprovacaoNivelTemplate("S"), "dtHrAprovacaoNivel2");
		sql.addProjection(createDtHrAprovacaoNivelTemplate("T"), "dtHrAprovacaoNivel3");
		sql.addProjection(createDtHrAprovacaoNivelTemplate("Q"), "dtHrAprovacaoNivel4");
		sql.addProjection(createDtHrAprovacaoNivelTemplate("C"), "dtHrConclusaoAnaliseCredito");
		sql.addProjection("to_char(SIMULACAO.DT_SIMULACAO, 'dd/MM/yyyy')", "dtInsercaoProposta");
		sql.addProjection(createDtHrAprovacaoAcaoNivel(1), "dtHrAprovacaoAcaoNivel1");
		sql.addProjection(createDtHrAprovacaoAcaoNivel(2), "dtHrAprovacaoAcaoNivel2");
		sql.addProjection(createDtHrAprovacaoAcaoNivel(3), "dtHrAprovacaoAcaoNivel3");
		sql.addProjection(createDtHrEnvioEfetivacao(), "dtHrEnvioEfetivacao");
	}

	private SqlTemplate createDtHrEnvioEfetivacao() {
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("to_char(Max(HISTORICO_EFETIVACAO.DH_SOLICITACAO), 'dd/MM/yyyy HH24:MI')");
		sql.addFrom("HISTORICO_EFETIVACAO");
		sql.addCustomCriteria("HISTORICO_EFETIVACAO.ID_SIMULACAO = SIMULACAO.ID_SIMULACAO");
		return sql;
	}
	
	private void addOrderBy(SqlTemplate sql){
		sql.addOrderBy("REGIONAL.DS_REGIONAL");
		sql.addOrderBy("FILIAL.SG_FILIAL");
		sql.addOrderBy("V_FUNCIONARIO.NM_FUNCIONARIO");
		sql.addOrderBy("SIMULACAO.TP_SITUACAO_APROVACAO");
	}
	
	private void addCustomOrderBy(SqlTemplate sql){
		sql.addOrderBy("REGIONAL.DS_REGIONAL");
		sql.addOrderBy("FILIAL.SG_FILIAL");
		sql.addOrderBy("dtHrSolicitacaoAnaliseCredito");
		sql.addOrderBy("SIMULACAO.DT_SIMULACAO", "DESC");
		sql.addOrderBy("V_FUNCIONARIO.NM_FUNCIONARIO");
		sql.addOrderBy("SIMULACAO.TP_SITUACAO_APROVACAO");
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		/** SELECT */
		
		sql.addProjection("DISTINCT REGIONAL.DS_REGIONAL", "dsRegional");
		sql.addProjection("FILIAL.SG_FILIAL", "sgFilial");
		sql.addProjection("PESSOA.NR_IDENTIFICACAO", "nrIdentificacao");
		sql.addProjection("PESSOA.TP_IDENTIFICACAO", "tpIdentificacao");
		sql.addProjection("PESSOA.NM_PESSOA", "nmPessoa");
		sql.addProjection("SIMULACAO.NR_MATRICULA_PROMOTOR", "nrMatriculaPromotor");
		sql.addProjection("SIMULACAO.NR_SIMULACAO", "nrSimulacao");
		sql.addProjection("V_FUNCIONARIO.NM_FUNCIONARIO", "nmPromotor");
		sql.addProjection("PROPOSTA.ID_PROPOSTA", "nrProposta");
		sql.addProjection("TIPO_TABELA_PRECO.TP_TIPO_TABELA_PRECO || TIPO_TABELA_PRECO.NR_VERSAO || '-' || SUBTIPO_TABELA_PRECO.TP_SUBTIPO_TABELA_PRECO ", "nmTabelaDivisao");
		sql.addProjection("to_char(SIMULACAO.DT_EFETIVACAO,'dd/mm/yyyy')", "dtEfetivacao");
		sql.addProjection("SIMULACAO.TP_SIMULACAO", "tpSimulacao");
		sql.addProjection("SIMULACAO.TP_SITUACAO_APROVACAO", "tpSituacaoAprovacao");
		sql.addProjection("SIMULACAO.TP_GERACAO_PROPOSTA", "tpGeracaoProposta");
		sql.addProjection("CLIENTE.TP_CLIENTE", "tpCliente");
		sql.addProjection("CLIENTE.TP_SITUACAO", "tpSituacao");
		sql.addProjection("DIVISAO_CLIENTE.DS_DIVISAO_CLIENTE", "dsDivisaoCliente");
		sql.addProjection("DIVISAO_CLIENTE.TP_SITUACAO", "tpSituacaoDivisaoCliente");
		sql.addProjection("SIMULACAO.DT_SIMULACAO");
		

		/** FROM */
		
		sql.addFrom("SIMULACAO");
		sql.addFrom("FILIAL");
		sql.addFrom("REGIONAL");
		sql.addFrom("REGIONAL_FILIAL");
		sql.addFrom("PESSOA");
		sql.addFrom("PROPOSTA");
		sql.addFrom("TABELA_PRECO");
		sql.addFrom("TIPO_TABELA_PRECO");
		sql.addFrom("SUBTIPO_TABELA_PRECO");
		sql.addFrom("CLIENTE");
		sql.addFrom("DIVISAO_CLIENTE");
		sql.addFrom("V_FUNCIONARIO");
		
		/** JOIN */
		
		sql.addJoin("SIMULACAO.ID_FILIAL", "FILIAL.ID_FILIAL");
		sql.addJoin("FILIAL.ID_FILIAL", "REGIONAL_FILIAL.ID_FILIAL");
		sql.addJoin("REGIONAL.ID_REGIONAL", "REGIONAL_FILIAL.ID_REGIONAL");
		sql.addJoin("SIMULACAO.ID_CLIENTE", "PESSOA.ID_PESSOA");
		sql.addJoin("SIMULACAO.ID_SIMULACAO", "PROPOSTA.ID_SIMULACAO(+)");
		sql.addJoin("SIMULACAO.ID_TABELA_PRECO", "TABELA_PRECO.ID_TABELA_PRECO");
		sql.addJoin("TABELA_PRECO.ID_TIPO_TABELA_PRECO", "TIPO_TABELA_PRECO.ID_TIPO_TABELA_PRECO");
		sql.addJoin("TABELA_PRECO.ID_SUBTIPO_TABELA_PRECO", "SUBTIPO_TABELA_PRECO.ID_SUBTIPO_TABELA_PRECO");
		sql.addJoin("TABELA_PRECO.ID_TIPO_TABELA_PRECO", "TIPO_TABELA_PRECO.ID_TIPO_TABELA_PRECO");
		sql.addJoin("SIMULACAO.ID_CLIENTE", "CLIENTE.ID_CLIENTE");
		sql.addJoin("SIMULACAO.ID_DIVISAO_CLIENTE", "DIVISAO_CLIENTE.ID_DIVISAO_CLIENTE");
		sql.addJoin("SIMULACAO.NR_MATRICULA_PROMOTOR", "V_FUNCIONARIO.NR_MATRICULA(+)");
		
		
		/** WHERE */
		
		sql.addCustomCriteria("TRUNC(SYSDATE) BETWEEN REGIONAL_FILIAL.DT_VIGENCIA_INICIAL AND REGIONAL_FILIAL.DT_VIGENCIA_FINAL");
		
		Long idRegional = criteria.getLong("idRegional");
		String dsRegional = criteria.getString("dsRegional");
		if (idRegional != null) {
			sql.addCriteria("REGIONAL.ID_REGIONAL", "=", idRegional);
			sql.addFilterSummary("regional", dsRegional);
		}
		
		Long idFilial = criteria.getLong("idFilial");
		String dsFilial = criteria.getString("dsFilial");
		if(idFilial != null){
			sql.addCriteria("FILIAL.ID_FILIAL", "=", idFilial); 
			sql.addFilterSummary("filial", dsFilial);
		}
		
		String nrMatriculaPromotor = criteria.getString("nrMatricula");
		String nmPromotor = criteria.getString("nmPromotor");
		if(nrMatriculaPromotor != null){
			sql.addCriteria("SIMULACAO.NR_MATRICULA_PROMOTOR", "=", nrMatriculaPromotor); 
			sql.addFilterSummary("promotorVendas", nmPromotor);
		}
		
		String blEfetivada = criteria.getString("propostasEfetivadas");
		String dsPropostasEfetivadas = criteria.getString("dsPropostasEfetivadas");
		if(blEfetivada != null){
			sql.addCriteria("SIMULACAO.BL_EFETIVADA", "=", blEfetivada); 
			sql.addFilterSummary("propostasEfetivadas", dsPropostasEfetivadas);
		}
		
		String tpGeracaoProposta = criteria.getString("tipoProposta");
		String dsTipoProposta = criteria.getString("dsTipoProposta");
		if(tpGeracaoProposta != null){
			sql.addCriteria("SIMULACAO.TP_GERACAO_PROPOSTA", "=", tpGeracaoProposta); 
			sql.addFilterSummary("tipoProposta", dsTipoProposta);
		}
		
		String tpSituacaoAprovacao = criteria.getString("statusProposta");
		String dsStatusProposta = criteria.getString("dsStatusProposta");
		if(tpSituacaoAprovacao != null){
			sql.addCriteria("SIMULACAO.TP_SITUACAO_APROVACAO", "=", tpSituacaoAprovacao); 
			sql.addFilterSummary("statusProposta", dsStatusProposta);
		}
		
		YearMonthDay dtEfetivacaoInicio = (YearMonthDay )criteria.get("dtDataInicial");
    	YearMonthDay dtEfetivacaoFim = (YearMonthDay )criteria.get("dtDataFinal");
    	StringBuilder datas = new StringBuilder();
    	String strDtEfetivacaoInicio = null;
    	if(dtEfetivacaoInicio != null){
    		strDtEfetivacaoInicio = dtEfetivacaoInicio.toString("dd/MM/yyyy");
    		datas.append("SIMULACAO.DT_EFETIVACAO  >= to_date('" + strDtEfetivacaoInicio + "','dd/mm/yyyy')");
    	}
    	
    	String strDtEfetivacaoFim = null;
    	if(dtEfetivacaoFim != null){
    		strDtEfetivacaoFim = dtEfetivacaoFim.toString("dd/MM/yyyy");
    		datas.append(" and SIMULACAO.DT_EFETIVACAO <= to_date('" + strDtEfetivacaoFim + "','dd/mm/yyyy')");	    		
    	}
    	if (datas.length() > 0){
    		sql.addCustomCriteria(datas.toString());
    	}

    	if(dtEfetivacaoInicio != null && dtEfetivacaoFim != null){
    		sql.addFilterSummary("dataEfetivacao", strDtEfetivacaoInicio + " até " + strDtEfetivacaoFim);
    	}
    	
    	String nmTabelaPreco = criteria.getString("nmTabelaPreco");
		if(nmTabelaPreco != null){
			sql.addCriteria("TIPO_TABELA_PRECO.TP_TIPO_TABELA_PRECO || TIPO_TABELA_PRECO.NR_VERSAO || '-' || SUBTIPO_TABELA_PRECO.TP_SUBTIPO_TABELA_PRECO ", "=", nmTabelaPreco); 
			sql.addFilterSummary("tabela", nmTabelaPreco);
		}
		
		return sql;
	}

	private SqlTemplate createSolicitacaoAnaliseCreditoTemplate() {
		SqlTemplate sqlSolicitacaoAnaliseCredito = createSqlTemplate();
		
		sqlSolicitacaoAnaliseCredito.addProjection("to_char(Max(ANALISE_CREDITO_CLIENTE.DH_SOLICITACAO), 'dd/MM/yyyy HH24:MI')");
		sqlSolicitacaoAnaliseCredito.addFrom("ANALISE_CREDITO_CLIENTE");
		sqlSolicitacaoAnaliseCredito.addCustomCriteria("ANALISE_CREDITO_CLIENTE.ID_CLIENTE = CLIENTE.ID_CLIENTE");
		return sqlSolicitacaoAnaliseCredito;
	}
	
	private SqlTemplate createDtHrAprovacaoNivelTemplate(String tpEvento) {
		SqlTemplate sqlSolicitacaoAnaliseCredito = createSqlTemplate();
		
		sqlSolicitacaoAnaliseCredito.addProjection("to_char(Max(HISTORICO_ANALISE_CREDITO.DH_EVENTO), 'dd/MM/yyyy HH24:MI')");
		sqlSolicitacaoAnaliseCredito.addFrom("ANALISE_CREDITO_CLIENTE");
		sqlSolicitacaoAnaliseCredito.addFrom("HISTORICO_ANALISE_CREDITO");
		sqlSolicitacaoAnaliseCredito.addCustomCriteria("ANALISE_CREDITO_CLIENTE.ID_CLIENTE = CLIENTE.ID_CLIENTE");
		sqlSolicitacaoAnaliseCredito.addCustomCriteria("ANALISE_CREDITO_CLIENTE.ID_ANALISE_CREDITO_CLIENTE = HISTORICO_ANALISE_CREDITO.ID_ANALISE_CREDITO_CLIENTE");
		sqlSolicitacaoAnaliseCredito.addCustomCriteria("HISTORICO_ANALISE_CREDITO.TP_EVENTO = '"+tpEvento + "'");
		
		return sqlSolicitacaoAnaliseCredito;
	}
	
	private SqlTemplate createDtHrAprovacaoAcaoNivel(int nrOrdemAprovacao) {
		SqlTemplate sqlAprovacaoAcaoNivel = this.createSqlTemplate();
		
		sqlAprovacaoAcaoNivel.addProjection("to_char(ACAO.DH_LIBERACAO, 'dd/MM/yyyy HH24:MI')");
		sqlAprovacaoAcaoNivel.addFrom("ACAO");
		sqlAprovacaoAcaoNivel.addFrom("PENDENCIA");
		sqlAprovacaoAcaoNivel.addCustomCriteria("ACAO.ID_PENDENCIA = PENDENCIA.ID_PENDENCIA");
		sqlAprovacaoAcaoNivel.addCustomCriteria("SIMULACAO.ID_PENDENCIA_APROVACAO = PENDENCIA.ID_PENDENCIA");
		sqlAprovacaoAcaoNivel.addCustomCriteria("ACAO.NR_ORDEM_APROVACAO = "+ nrOrdemAprovacao);
		return sqlAprovacaoAcaoNivel;
	}
	
	
}