package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.tabeladeprecos.ProdutoEspecificoDTO;
import com.mercurio.lms.rest.tabeladeprecos.ServicoSuggestDTO;
import com.mercurio.lms.rest.tabeladeprecos.TabelaPrecoSuggestDTO;

public class PropostaClienteDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String identificacao;
	private Long idFilial;
	private Long idFilialSessao;
	private String sgFilial;
	private String efetivada;
	private boolean blEfetivada;
	private String dtSimulacao;
	private Long nrSimulacao;
	private FuncionarioPromotorSuggestDTO promotor;
	private ClienteSuggestDTO cliente;
	private DivisaoClienteSuggestDTO divisaoCliente;
	private TabelaPrecoSuggestDTO tabelaPreco;
	private Long idPendenciaAprovacao;
	private ServicoSuggestDTO servico;
	private TabelaPrecoSuggestDTO tabelaPrecoFob;
	private DomainValue tpGeracaoProposta;
	private BigDecimal nrFatorCubagem;
	private BigDecimal nrFatorDensidade;
	private DomainValue tpSituacaoAprovacao;
	private DomainValue tpPeriodicidadeFaturamento;
	private Short nrDiasPrazoPagamento;
	private YearMonthDay dtValidadeProposta;
	private YearMonthDay dtTabelaVigenciaInicial;
	private YearMonthDay dtAceiteCliente;
	private YearMonthDay dtAprovacao;
	private YearMonthDay dtEmissaoTabela;
	private FuncionarioPromotorSuggestDTO funcionarioAprovador;
	private FuncionarioPromotorSuggestDTO funcionarioEfetivador;
	private String obProposta;
	private YearMonthDay dtEfetivacao;
	private DateTime dhSolicitacao;
	private DateTime dhReprovacao;
	private String dsMotivo;
	private String saveMode;
	private boolean disableAll = false;
	private String isFilialMatriz;
	private ProdutoEspecificoDTO produtoEspecifico;
	private String ordenacao;

	private CamposPropostaDTO campos;
	private BotoesPropostaDTO botoes;
	
	public PropostaClienteDTO() {
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public String getEfetivada() {
		return efetivada;
	}

	public void setEfetivada(String efetivada) {
		this.efetivada = efetivada;
	}

	public String getDtSimulacao() {
		return dtSimulacao;
	}

	public void setDtSimulacao(String dtSimulacao) {
		this.dtSimulacao = dtSimulacao;
	}

	public Long getNrSimulacao() {
		return nrSimulacao;
	}

	public void setNrSimulacao(Long nrSimulacao) {
		this.nrSimulacao = nrSimulacao;
	}

	public FuncionarioPromotorSuggestDTO getPromotor() {
		return promotor;
	}

	public void setPromotor(FuncionarioPromotorSuggestDTO promotor) {
		this.promotor = promotor;
	}

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public DivisaoClienteSuggestDTO getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoClienteSuggestDTO divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public TabelaPrecoSuggestDTO getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPrecoSuggestDTO tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public ServicoSuggestDTO getServico() {
		return servico;
	}

	public void setServico(ServicoSuggestDTO servico) {
		this.servico = servico;
	}

	public TabelaPrecoSuggestDTO getTabelaPrecoFob() {
		return tabelaPrecoFob;
	}

	public void setTabelaPrecoFob(TabelaPrecoSuggestDTO tabelaPrecoFob) {
		this.tabelaPrecoFob = tabelaPrecoFob;
	}

	public DomainValue getTpGeracaoProposta() {
		return tpGeracaoProposta;
	}

	public void setTpGeracaoProposta(DomainValue tpGeracaoProposta) {
		this.tpGeracaoProposta = tpGeracaoProposta;
	}

	public BigDecimal getNrFatorCubagem() {
		return nrFatorCubagem;
	}

	public void setNrFatorCubagem(BigDecimal nrFatorCubagem) {
		this.nrFatorCubagem = nrFatorCubagem;
	}

	public BigDecimal getNrFatorDensidade() {
		return nrFatorDensidade;
	}

	public void setNrFatorDensidade(BigDecimal nrFatorDensidade) {
		this.nrFatorDensidade = nrFatorDensidade;
	}

	public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public CamposPropostaDTO getCampos() {
		return campos;
	}

	public void setCampos(CamposPropostaDTO campos) {
		this.campos = campos;
	}

	public Long getIdPendenciaAprovacao() {
		return idPendenciaAprovacao;
	}

	public void setIdPendenciaAprovacao(Long idPendenciaAprovacao) {
		this.idPendenciaAprovacao = idPendenciaAprovacao;
	}

	public DomainValue getTpPeriodicidadeFaturamento() {
		return tpPeriodicidadeFaturamento;
	}

	public void setTpPeriodicidadeFaturamento(DomainValue tpPeriodicidadeFaturamento) {
		this.tpPeriodicidadeFaturamento = tpPeriodicidadeFaturamento;
	}

	public Short getNrDiasPrazoPagamento() {
		return nrDiasPrazoPagamento;
	}

	public void setNrDiasPrazoPagamento(Short nrDiasPrazoPagamento) {
		this.nrDiasPrazoPagamento = nrDiasPrazoPagamento;
	}

	public YearMonthDay getDtValidadeProposta() {
		return dtValidadeProposta;
	}

	public void setDtValidadeProposta(YearMonthDay dtValidadeProposta) {
		this.dtValidadeProposta = dtValidadeProposta;
	}

	public YearMonthDay getDtTabelaVigenciaInicial() {
		return dtTabelaVigenciaInicial;
	}

	public void setDtTabelaVigenciaInicial(YearMonthDay dtTabelaVigenciaInicial) {
		this.dtTabelaVigenciaInicial = dtTabelaVigenciaInicial;
	}

	public YearMonthDay getDtAceiteCliente() {
		return dtAceiteCliente;
	}

	public void setDtAceiteCliente(YearMonthDay dtAceiteCliente) {
		this.dtAceiteCliente = dtAceiteCliente;
	}

	public YearMonthDay getDtAprovacao() {
		return dtAprovacao;
	}

	public void setDtAprovacao(YearMonthDay dtAprovacao) {
		this.dtAprovacao = dtAprovacao;
	}

	public FuncionarioPromotorSuggestDTO getFuncionarioAprovador() {
		return funcionarioAprovador;
	}

	public void setFuncionarioAprovador(
			FuncionarioPromotorSuggestDTO funcionarioAprovador) {
		this.funcionarioAprovador = funcionarioAprovador;
	}

	public YearMonthDay getDtEmissaoTabela() {
		return dtEmissaoTabela;
	}

	public void setDtEmissaoTabela(YearMonthDay dtEmissaoTabela) {
		this.dtEmissaoTabela = dtEmissaoTabela;
	}

	public FuncionarioPromotorSuggestDTO getFuncionarioEfetivador() {
		return funcionarioEfetivador;
	}

	public void setFuncionarioEfetivador(
			FuncionarioPromotorSuggestDTO funcionarioEfetivador) {
		this.funcionarioEfetivador = funcionarioEfetivador;
	}

	public String getObProposta() {
		return obProposta;
	}

	public void setObProposta(String obProposta) {
		this.obProposta = obProposta;
	}

	public boolean isBlEfetivada() {
		return blEfetivada;
	}

	public void setBlEfetivada(boolean blEfetivada) {
		this.blEfetivada = blEfetivada;
	}

	public YearMonthDay getDtEfetivacao() {
		return dtEfetivacao;
	}

	public void setDtEfetivacao(YearMonthDay dtEfetivacao) {
		this.dtEfetivacao = dtEfetivacao;
	}

	public DateTime getDhSolicitacao() {
		return dhSolicitacao;
	}

	public void setDhSolicitacao(DateTime dhSolicitacao) {
		this.dhSolicitacao = dhSolicitacao;
	}

	public DateTime getDhReprovacao() {
		return dhReprovacao;
	}

	public void setDhReprovacao(DateTime dhReprovacao) {
		this.dhReprovacao = dhReprovacao;
	}

	public String getDsMotivo() {
		return dsMotivo;
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}
	
	public void setDsMotivo(String dsMotivo) {
		this.dsMotivo = dsMotivo;
	}

	public BotoesPropostaDTO getBotoes() {
		return botoes;
	}

	public void setBotoes(BotoesPropostaDTO botoes) {
		this.botoes = botoes;
	}

	public String getSaveMode() {
		return saveMode;
	}

	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}

	public boolean isDisableAll() {
		return disableAll;
	}

	public void setDisableAll(boolean disableAll) {
		this.disableAll = disableAll;
	}

	public String getIsFilialMatriz() {
		return isFilialMatriz;
	}

	public void setIsFilialMatriz(String isFilialMatriz) {
		this.isFilialMatriz = isFilialMatriz;
	}

	public Long getIdFilialSessao() {
		return idFilialSessao;
	}

	public void setIdFilialSessao(Long idFilialSessao) {
		this.idFilialSessao = idFilialSessao;
	}

	public ProdutoEspecificoDTO getProdutoEspecifico() {
		return produtoEspecifico;
	}

	public void setProdutoEspecifico(ProdutoEspecificoDTO produtoEspecifico) {
		this.produtoEspecifico = produtoEspecifico;
	}

	public String getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}
}
