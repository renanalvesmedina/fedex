package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.dao.ConsultarMCDDAO;
import com.mercurio.lms.municipios.model.param.MCDParam;
import com.mercurio.lms.municipios.model.service.FluxoFilialService;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.PrazoEntregaCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.PrazoEntregaClienteService;

/**
 * @author Mauricio Moraes
 *
 * @spring.bean id="lms.vendas.MCDClienteService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirRelatorioMCDCliente.jasper"
 */
public class MCDClienteService extends ReportServiceSupport {
	
	private PrazoEntregaClienteService prazoEntregaClienteService;
	private ClienteService clienteService;
	private PessoaService pessoaService;
	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private DomainValueService domainValueService;
	private FluxoFilialService fluxoFilialService;

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap filter = (TypedFlatMap) parameters;

		/** Converte parametros de tela e monta SQL */
		SqlTemplate sql = ConsultarMCDDAO.getSqlTemplate(this.createSqlTemplate(), configureMCDParam(filter));
		/** Adiciona os valores de Summary */
		applyFilterSummary(sql, filter);
		/** Executa o SQL */
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		/** Seta os parametro do relatorio */
		Map parametersReport = new HashMap();
		if(StringUtils.isNotBlank(filter.getString("cliente.idCliente"))) {
			applyParametrosCliente(parametersReport, filter.getLong("cliente.idCliente"));
		}
		applyParametrosEmpresa(parametersReport);
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("servico_idServico", filter.getString("servico.idServico"));
		if(StringUtils.isNotBlank(filter.getString("contato.nmContato"))){
			parametersReport.put("nmContato", filter.getString("contato.nmContato"));
		}
		parametersReport.put("tpEmissao", filter.getString("tpEmissao"));
		
		String tpFormatoRelatorio = (String)  parameters.get("tpFormatoRelatorio.valor");
		
		if ("xls".equals(tpFormatoRelatorio)){
			parametersReport.put("titulo", "Relatório de MCD - Mapa de Códigos de Distância do Cliente");
			this.setReportName("com/mercurio/lms/municipios/report/emitirMapaCodigosDistanciaExcel.jasper");
		} else {
			this.setReportName("com/mercurio/lms/vendas/report/emitirRelatorioMCDCliente.jasper");
		}
		
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,tpFormatoRelatorio);
		jr.setParameters(parametersReport);
		return jr;
	}

	private MCDParam configureMCDParam(TypedFlatMap values) {
    	MCDParam dados = new MCDParam();

    	Long idCliente = values.getLong("cliente.idCliente");
		if(idCliente != null) {
			EnderecoPessoa end = enderecoPessoaService.findEnderecoPessoaPadrao(idCliente);
			dados.setIdMunicipioOrigem(end.getMunicipio().getIdMunicipio());
			dados.setIdCliente(idCliente);
		} else {
			dados.setIdMunicipioOrigem((Long)enderecoPessoaService.findMunicipioUfByIdPessoa(SessionUtils.getFilialSessao().getIdFilial()).get("idMunicipio"));
		}

		dados.setIdServico(values.getLong("servico.idServico"));
		dados.setIdUnidadeFederativaDestino(values.getLong("uf.idUnidadeFederativa"));
		dados.setTpEmissao(values.getString("tpEmissao"));
		dados.setDtVigencia(JTDateTimeUtils.getDataAtual());
		return dados;
    }

	private void applyFilterSummary(SqlTemplate sql, TypedFlatMap values) {
		if(values.getLong("servico.idServico") != null) {
			sql.addFilterSummary("servico", values.getString("servico.dsServico"));
		}
		if(values.getLong("uf.idUnidadeFederativa") != null) {
			sql.addFilterSummary("ufDestino", values.getString("uf.siglaDescricao"));
		}
		if(values.getString("tpEmissao") != null) {
			DomainValue tpEmissao = domainValueService.findDomainValueByValue("DM_TIPO_EMISSAO_MCD", values.getString("tpEmissao"));
			sql.addFilterSummary("tipoEmissao", tpEmissao.getDescription().getValue());
		}
	}

	private void applyParametrosCliente(Map parameters, Long idCliente) {
		if(idCliente == null) {
			return;
		}

		parameters.put("cliente_idCliente", idCliente.toString());
		Cliente cliente = clienteService.findById2(idCliente);
		Pessoa pessoa = cliente.getPessoa();
		EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa();
		TelefoneEndereco fone = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FO");
		if(fone == null) {
			fone = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FF");
		}
		TelefoneEndereco fax = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FA");
		String text = null;

		text = pessoa.getNmPessoa();
		parameters.put("cliente_nmPessoa", text);
		/** Endereco */
		StringBuffer dsEndereco = new StringBuffer();
		dsEndereco.append(enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro());
		if(StringUtils.isNotBlank(enderecoPessoa.getDsEndereco())) {
			dsEndereco.append(" ");
			dsEndereco.append(enderecoPessoa.getDsEndereco());
		}
		dsEndereco.append(", ");
		if(StringUtils.isNotBlank(enderecoPessoa.getNrEndereco())) {
			dsEndereco.append(enderecoPessoa.getNrEndereco());
		}
		if(StringUtils.isNotBlank(enderecoPessoa.getDsComplemento())) {
			dsEndereco.append(" - ");
			dsEndereco.append(enderecoPessoa.getDsComplemento());
		}
		if(StringUtils.isNotBlank(enderecoPessoa.getDsBairro())) {
			dsEndereco.append(" - ");
			dsEndereco.append(enderecoPessoa.getDsBairro());
		}
		parameters.put("cliente_dsEndereco1", dsEndereco.toString());
		/** Endereco 2 */
		dsEndereco = new StringBuffer();
		dsEndereco.append(FormatUtils.formatCep(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getSgPais(), enderecoPessoa.getNrCep()));
		if(StringUtils.isNotBlank(enderecoPessoa.getMunicipio().getNmMunicipio())) {
			dsEndereco.append(" - ");
			dsEndereco.append(enderecoPessoa.getMunicipio().getNmMunicipio());
		}
		if(StringUtils.isNotBlank(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa())) {
			dsEndereco.append(" - ");
			dsEndereco.append(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		}
		parameters.put("cliente_dsEndereco2", dsEndereco.toString());

		text = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao());
		parameters.put("cliente_nrIdentificacao", text);
		parameters.put("cliente_tpIdentificacao", pessoa.getTpIdentificacao().getValue());
		text = "";
		if(!pessoa.getInscricaoEstaduais().isEmpty()) {
			text = ((InscricaoEstadual) pessoa.getInscricaoEstaduais().get(0)).getNrInscricaoEstadual();
		}
		parameters.put("cliente_nrIE", text);
		text = "";
		if(fone != null) {
			text = FormatUtils.formatTelefone(fone.getNrTelefone(), fone.getNrDdd(), fone.getNrDdi());
		}
		parameters.put("cliente_nrFone", text);
		text = "";
		if(fax != null) {
			text = FormatUtils.formatTelefone(fax.getNrTelefone(), fax.getNrDdd(), fax.getNrDdi());
		}
		parameters.put("cliente_nrFax", text);
		text = null;
		if(cliente.getTpDificuldadeColeta() != null) {
			text = cliente.getTpDificuldadeColeta().getValue();
		}
		parameters.put("cliente_tpDificuldadeColeta", StringUtils.defaultString(text, "0"));
		text = null;
		if(cliente.getTpDificuldadeEntrega() != null) {
			text = cliente.getTpDificuldadeEntrega().getValue();
		}
		parameters.put("cliente_tpDificuldadeEntrega", StringUtils.defaultString(text, "0"));
		text = null;
		if(cliente.getTpDificuldadeClassificacao() != null) {
			text = cliente.getTpDificuldadeClassificacao().getValue();
		}
		parameters.put("cliente_tpDificuldadeClassificacao", StringUtils.defaultString(text, "0"));

	}

	public void applyParametrosEmpresa(Map parameters) {
		Empresa empresa = SessionUtils.getEmpresaSessao();
		Pessoa pessoa = pessoaService.findById(empresa.getPessoa().getIdPessoa());
		Municipio municipio = pessoa.getEnderecoPessoa().getMunicipio();

		parameters.put("filial_nmPessoa", StringUtils.upperCase(pessoa.getNmPessoa()));
		parameters.put("filial_dsURL", empresa.getDsHomePage());
		parameters.put("filial_nmMunicipio", StringUtils.left(municipio.getNmMunicipio(),50));
	}

	/**
	 * Método utilizado internamento no relátório de MCD para calcular PPE 
	 * @see com/mercurio/lms/vendas/report/emitirRelatorioMCDCliente.jrxml
	 * 
	 * @param idCliente
	 * @param idServico
	 * @param nrPpe
	 * @param tpEmissao
	 * @param tpDificuldadeColeta
	 * @param tpDificuldadeEntrega
	 * @param tpDificuldadeClassificacao
	 * @param idZonaOrigem
	 * @param idZonaDestino
	 * @param idPaisOrigem
	 * @param idPaisDestino
	 * @param idUnidadeFederativaOrigem
	 * @param idUnidadeFederativaDestino
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idMunicipioOrigem
	 * @param idMunicipioDestino
	 * @param idMunicipioFilialOrigem
	 * @param idMunicipioFilialDestino
	 * @param idTipoLocalizacaoOrigem
	 * @param idTipoLocalizacaoDestino
	 * @return Valor PPE
	 */
	public BigDecimal findPPE(
			Long idCliente,
			Long idServico,
			BigDecimal nrPpe,
			String tpEmissao,
			Long tpDificuldadeColeta,
			Long tpDificuldadeEntrega,
			Long tpDificuldadeClassificacao,
			Long idZonaOrigem, 
			Long idZonaDestino, 
			Long idPaisOrigem, 
			Long idPaisDestino, 
			Long idUnidadeFederativaOrigem,
			Long idUnidadeFederativaDestino, 
			Long idFilialOrigem, 
			Long idFilialDestino, 
			Long idMunicipioOrigem, 
			Long idMunicipioDestino,
			Long idMunicipioFilialOrigem, 
			Long idMunicipioFilialDestino, 
			Long idTipoLocalizacaoOrigem,
			Long idTipoLocalizacaoDestino) {

		/** Verifica se não foi passado o Cliente */
		if(idCliente == null) {
			return nrPpe;
		}

		// Monta as restrições para Origem
		RestricaoRota restricaoRotaOrigem = new RestricaoRota(); 
		restricaoRotaOrigem.setIdAeroporto(null);
		restricaoRotaOrigem.setIdFilial(idFilialOrigem);
		restricaoRotaOrigem.setIdMunicipio(idMunicipioOrigem);
		restricaoRotaOrigem.setIdPais(idPaisOrigem);
		restricaoRotaOrigem.setIdTipoLocalizacao(idTipoLocalizacaoOrigem);
		restricaoRotaOrigem.setIdUnidadeFederativa(idUnidadeFederativaOrigem);
		restricaoRotaOrigem.setIdZona(idZonaOrigem);

		// Monta as restrições para Destino
		RestricaoRota restricaoRotaDestino = new RestricaoRota();
		restricaoRotaDestino.setIdAeroporto(null);
		restricaoRotaDestino.setIdFilial(idFilialDestino);

		restricaoRotaDestino.setIdMunicipio(idMunicipioDestino);
		restricaoRotaDestino.setIdPais(idPaisDestino);
		restricaoRotaDestino.setIdTipoLocalizacao(idTipoLocalizacaoDestino);
		restricaoRotaDestino.setIdUnidadeFederativa(idUnidadeFederativaDestino);
		restricaoRotaDestino.setIdZona(idZonaDestino);

		BigDecimal nrPrazo = BigDecimalUtils.defaultBigDecimal(nrPpe);
		BigDecimal qtHorasDia = BigDecimal.valueOf(24);
		
		/** Busca Prazo Entrega Cliente*/
		PrazoEntregaCliente prazoEntregaCliente = prazoEntregaClienteService.findPrazoEntregaCliente(
				idCliente, 
				idServico, 
				restricaoRotaOrigem, 
				restricaoRotaDestino);
		
		/** Caso exista atribui valor como referencia de prazo */
		if (prazoEntregaCliente != null){
			nrPrazo = BigDecimalUtils.getBigDecimal(prazoEntregaCliente.getNrPrazo());
			/** Se Tipo de emissão == “Dias” */
			if("D".equals(tpEmissao)) {
				nrPrazo = BigDecimalUtils.round(BigDecimalUtils.divide(nrPrazo,qtHorasDia),0,BigDecimalUtils.ROUND_UP);
			}
		} else {
			/** Caso contrário utiliza o PPE do fluxo da filial */
			nrPrazo = calcPPE(
					idServico, 
					tpEmissao,
					idMunicipioFilialOrigem, 
					idMunicipioFilialDestino, 
					restricaoRotaOrigem,
					restricaoRotaDestino);
		}

		/** 
		 * Atribui os parametros de dificuldade 
		 **/

		/** Se Tipo de emissão == “Horas” multiplica valor */
		if(LongUtils.hasValue(tpDificuldadeColeta)) {
			BigDecimal vlDificuldadeAdicional = BigDecimalUtils.getBigDecimal(tpDificuldadeColeta);
			if("H".equals(tpEmissao)) {
				vlDificuldadeAdicional = vlDificuldadeAdicional.multiply(qtHorasDia);
			}
			nrPrazo = nrPrazo.add(vlDificuldadeAdicional);
		}
		if(LongUtils.hasValue(tpDificuldadeEntrega)) {
			BigDecimal vlDificuldadeAdicional = BigDecimalUtils.getBigDecimal(tpDificuldadeEntrega);
			if("H".equals(tpEmissao)) {
				vlDificuldadeAdicional = vlDificuldadeAdicional.multiply(qtHorasDia);
			}
			nrPrazo = nrPrazo.add(vlDificuldadeAdicional);
		}
		if(LongUtils.hasValue(tpDificuldadeClassificacao)) {
			BigDecimal vlDificuldadeAdicional = BigDecimalUtils.getBigDecimal(tpDificuldadeClassificacao);
			if("H".equals(tpEmissao)) {
				vlDificuldadeAdicional = vlDificuldadeAdicional.multiply(qtHorasDia);
			}
			nrPrazo = nrPrazo.add(vlDificuldadeAdicional);
		}
		return nrPrazo;
	}

	/**
	 * Calcula o PPE do MCD
	 * @param idServico
	 * @param tpEmissao
	 * @param idMunicipioFilialOrigem
	 * @param idMunicipioFilialDestino
	 * @param restricaoRotaOrigem
	 * @param restricaoRotaDestino
	 * @return Tempo de nrPPE entre Coleta, Fluxo Filiais e Entrega
	 */
	private BigDecimal calcPPE(
		Long idServico,
		String tpEmissao,
		Long idMunicipioFilialOrigem,
		Long idMunicipioFilialDestino,
		RestricaoRota restricaoRotaOrigem,
		RestricaoRota restricaoRotaDestino) {

		return new BigDecimal(0);
		}		
	
	public void setPrazoEntregaClienteService(PrazoEntregaClienteService prazoEntregaClienteService) {
		this.prazoEntregaClienteService = prazoEntregaClienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}

	@Override
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public FluxoFilialService getFluxoFilialService() {
		return fluxoFilialService;
	}

	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}

}