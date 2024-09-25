package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.model.service.PropostaService;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;
import com.mercurio.lms.vendas.util.TabelasClienteUtil;

/**
 * 
 *
 * 
 * @spring.bean id="lms.vendas.emitirPropostaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirPropostaAereo.jrxml"
 * 
 */
public class EmitirPropostaService extends ReportServiceSupport {
	
	private PropostaService propostaService;
	private TabelasClienteService tabelasClienteService;
	private EmitirTabelasClientesService emitirTabelasClientesService;
	private ConfiguracoesFacade configuracoesFacade;
	private ServicoService servicoService;
	private PessoaService pessoaService;
	private InscricaoEstadualService inscricaoEstadualService;
	private TelefoneEnderecoService telefoneEnderecoService;
	
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
		configuraPrametrosListaPesos(parametersReport, map);
		configuraPrametrosListaProdutoEspecifico(parametersReport, map);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		jr.setParameters(parametersReport);
		return jr;
	}
	
	private void configuraPrametrosListaProdutoEspecifico(Map<String, Object> parametersReport, TypedFlatMap map) {
		String ordenacao = map.getString("ordenacao");
		
		Long idSimulacao = LongUtils.getLong(map.get("simulacao.idSimulacao"));
		Long idCliente = LongUtils.getLong(map.get("cliente.idCliente"));
		
		List<Map<String, Object>> listProdutoEspecifico = propostaService.findRelatorioPropostaPromocionalProdutoEspecifico(idSimulacao, idCliente, ordenacao);
		parametersReport.put("dataSourceProdutoEspecifico", !listProdutoEspecifico.isEmpty() ? new JRBeanCollectionDataSource(listProdutoEspecifico) : null);
		parametersReport.put("dsProdutoEspecifico", obtemDsProdutoEspecifico(listProdutoEspecifico));
	}
	
	private Object obtemDsProdutoEspecifico(List<Map<String, Object>> listProdutoEspecifico) {
		if(!listProdutoEspecifico.isEmpty()){
			String nrTarifaEspecifica = "000" + (String) ((Map<String, Object>) listProdutoEspecifico.get(0)).get("NRTARIFAESPECIFICA");
			nrTarifaEspecifica = nrTarifaEspecifica.substring(nrTarifaEspecifica.length()-3, nrTarifaEspecifica.length());
			return " TE " + nrTarifaEspecifica;
		}
		return "";
	}

	private void configuraPrametrosListaPesos(Map<String, Object> parametersReport, TypedFlatMap map) {
		String ordenacao = map.getString("ordenacao");
		
		Long idTabelaPreco = LongUtils.getLong(map.get("idTabelaPreco"));
		
		Long idServico = LongUtils.getLong(map.get("idServico"));
		Servico servico = servicoService.findById(idServico);
		String abrangencia = servico.getDsServico().toString();
		
		String sgFilial = (String) map.get("sgFilial");
		int numeroProposta = (Integer) map.get("nrProposta");
		String dataInclusao = (String) map.get("dtSimulacao");
		String situacao = (String) map.get("situacao");
		
		Long idCliente = LongUtils.getLong(map.get("cliente.idCliente"));
		Pessoa pessoa = pessoaService.findById(idCliente);
		String nmCliente = pessoa.getNmPessoa();
		
		EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa();
		String endereco1 = enderecoPessoa.getDsEndereco()+ ", " +enderecoPessoa.getDsBairro()+ ", " +enderecoPessoa.getNrEndereco();
		String endereco2 = enderecoPessoa.getNrCep()+ ", " +enderecoPessoa.getMunicipio().getNmMunicipio()+ ", " +enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();
		String cnpj = FormatUtils.formatCNPJ(pessoa.getNrIdentificacao());
		String inscricaoEstadual = inscricaoEstadualService.findIeByIdPessoaAtivoPadrao(pessoa.getIdPessoa()).getNrInscricaoEstadual();
		String telefone = telefoneEnderecoService.findTelefoneByIdPessoa(pessoa.getIdPessoa()).getDddTelefone();
		
		parametersReport.put("nmCliente", nmCliente);
		parametersReport.put("endereco1", endereco1);
		parametersReport.put("endereco2", endereco2);
		parametersReport.put("cnpj", cnpj);
		parametersReport.put("inscricaoEstadual", inscricaoEstadual);
		parametersReport.put("telefone", telefone);
		
		parametersReport.put("numeroProposta", sgFilial + " " +numeroProposta);
		parametersReport.put("dataInclusao", dataInclusao);
		parametersReport.put("situacao", situacao);
		parametersReport.put("abrangencia", abrangencia);
		List<Map<String, Object>> listPrecos2a15 = propostaService.findRelatorioPropostaPromocional2a15(idTabelaPreco, idCliente, ordenacao);
		parametersReport.put("dataSourcePesos2a15", !listPrecos2a15.isEmpty() ? new JRBeanCollectionDataSource(listPrecos2a15) : null);
		List<Map<String, Object>> listPrecos16a30 = propostaService.findRelatorioPropostaPromocional16a30(idTabelaPreco, idCliente, ordenacao);
		parametersReport.put("dataSourcePesos16a30", !listPrecos16a30.isEmpty() ? new JRBeanCollectionDataSource(listPrecos16a30) : null);
		parametersReport.put("origem", obtemOrigem(listPrecos2a15));
	}
	
	private Object obtemOrigem(List<Map<String, Object>> listPrecos2a15) {
		if(!listPrecos2a15.isEmpty()){
			return (String) ((Map<String, Object>) listPrecos2a15.get(0)).get("ORIGEM");
		}
		return "";
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		/** SELECT */
		sql.addProjection("s.id_simulacao");
		sql.addProjection("s.nr_simulacao");

		/** FROM */
		sql.addFrom("simulacao", "s");

		/** JOIN */
		// sql.addJoin("MUNICIPIO_TRT_CLIENTE.ID_TRT_CLIENTE","TRT_CLIENTE.ID_TRT_CLIENTE");

		/** WHERE */
		sql.addCriteria("s.id_simulacao", "=", criteria.getLong("simulacao.idSimulacao"));

		/** ORDER BY */
		sql.addOrderBy("s.id_simulacao");

		return sql;
	}

	public JRDataSource generateSubReportTDE(Long idTabelaPreco){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("GEN.VL_GENERALIDADE");
		sql.addProjection("GEN.VL_MINIMO");
		sql.addProjection("TP.OB_TABELA_PRECO");
		
		sql.addFrom("TABELA_PRECO","TP");
		sql.addFrom("TABELA_PRECO_PARCELA","TPP");
		sql.addFrom("PARCELA_PRECO","PP");
		sql.addFrom("GENERALIDADE","GEN");
		
		sql.addCustomCriteria("TP.ID_TABELA_PRECO = TPP.ID_TABELA_PRECO");
		sql.addCustomCriteria("TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO");
		sql.addCustomCriteria("GEN.ID_GENERALIDADE = TPP.ID_TABELA_PRECO_PARCELA");
		
		sql.addCriteria("PP.TP_SITUACAO", "=", "A");
		sql.addCriteria("TP.ID_TABELA_PRECO", "=", idTabelaPreco);
		sql.addCriteria("PP.CD_PARCELA_PRECO", "=", "IDTde");
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}
	
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public JRDataSource generateSubReportServicosAdicionaisTabela(Long idTabelaPreco) throws Exception {
		List servicosNaoContratados = TabelasClienteUtil.getSubRelServicosAdicionais(idTabelaPreco, configuracoesFacade, getJdbcTemplate(), null);
		return new JRBeanCollectionDataSource(servicosNaoContratados);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRDataSource generateSubReportGeneralidadesTabela(Long idTabelaPreco, Long idDivisaoCliente, String dsSimbolo) throws Exception {
		List generalidades = tabelasClienteService.getGeneralidadesTabelaPreco(tabelasClienteService.montaSqlSubGeneralidadesTab(), idTabelaPreco, dsSimbolo, configuracoesFacade, getJdbcTemplate(), idDivisaoCliente);
		return new JRBeanCollectionDataSource(emitirTabelasClientesService.normalizaGeneralidades(generalidades));
	}
	
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("tpSituacaoAprovacao", "DM_STATUS_WORKFLOW");
	}
	
	public void setPropostaService(PropostaService propostaService) {
		this.propostaService = propostaService;
	}

	public void setEmitirTabelasClientesService(EmitirTabelasClientesService emitirTabelasClientesService) {
		this.emitirTabelasClientesService = emitirTabelasClientesService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	
	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}
}
