package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Simulacao implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idSimulacao;

	/** persistent field */
	private Long nrSimulacao;

	/** persistent field */
	private DomainValue tpSimulacao;

	/** persistent field */
	private DomainValue tpFormaInsercao;

	/** persistent field */
	private YearMonthDay dtSimulacao;

	/** persistent field */
	private DomainValue tpIntegranteFrete;

	/** persistent field */
	private Boolean blCalculoPesoCubado;

	/** persistent field */
	private Boolean blEfetivada;

	/** nullable persistent field */
	private BigDecimal pcReeentrega;

	/** nullable persistent field */
	private BigDecimal pcDevolucao;

	/** nullable persistent field */
	private Short nrDiasPrazoPagamento;

	/** nullable persistent field */
	private YearMonthDay dtInicial;

	/** nullable persistent field */
	private YearMonthDay dtFinal;

	/** nullable persistent field */
	private YearMonthDay dtEmissaoTabela;

	/** nullable persistent field */
	private YearMonthDay dtTabelaVigenciaInicial;

	/** nullable persistent field */
	private YearMonthDay dtAceiteCliente;

	/** nullable persistent field */
	private YearMonthDay dtAprovacao;

	/** nullable persistent field */
	private YearMonthDay dtValidadeProposta;
	
	/** persistent field**/
	private YearMonthDay dtEfetivacao;

	/** nullable persistent field */
	private DomainValue tpFrete;

	/** nullable persistent field */
	private DomainValue tpPeriodicidadeFaturamento;

	/** nullable persistent field */
	private DomainValue tpSituacaoAprovacao;

	/** nullable persistent field */
	private DomainValue tpGeracaoProposta;

	/** nullable persistent field */
	private Blob dcSimulacao;

	/** persistent field */
	private DomainValue tpRegistro;

	/** persistent field */
	private DomainValue tpDiferencaAdvalorem;	

	/** persistent field */
	private Boolean blPagaFreteTonelada;

	/** persistent field */
	private String obProposta;

	/** persistent field */
	private Boolean blEmiteCargaCompleta;

	/** persistent field */
	private BigDecimal nrFatorCubagem;
	
	/** persistent field */
	private BigDecimal nrFatorDensidade;
	
	/** persistent field */
	private UnidadeFederativa unidadeFederativaByIdUfDestino;

	/** persistent field */
	private UnidadeFederativa unidadeFederativaByIdUfOrigem;

	/** persistent field */
	private Cliente clienteByIdCliente;

	/** persistent field */
	private Cliente clienteByIdClienteBase;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoOrigem;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoDestino;

	/** persistent field */
	private Usuario usuarioByIdUsuarioAprovou;

	/** persistent field */
	private Usuario usuarioByIdUsuarioEfetivou;

	/** persistent field */
	private Usuario usuarioByIdUsuario;

	/** persistent field */
	private Servico servico;

	/** persistent field */
	private TabelaPreco tabelaPreco;

	/** nullable persistent field */
	private TabelaPreco tabelaPrecoFob;

	/** persistent field */
	private Pessoa pessoa;

	/** persistent field */
	private DivisaoCliente divisaoCliente;

	/** persistent field */
	private Filial filial;

	/** persistent field */
	private Pendencia pendenciaAprovacao;
	
	/** persistent field */
	private List<ServicoAdicionalCliente> servicoAdicionalClientes;

	/** persistent field */
	private List<ParametroCliente> parametroClientes;

	/** persistent field */
	private List<NotaFiscalSimulacao> notaFiscalSimulacoes;

	/** persistent field */
	private List<DestinoSimulacao> destinoSimulacoes;

	/** nullable persistent field */
	private Funcionario promotor;

	// LMS-6172 - Documentos anexos
	List<SimulacaoAnexo> anexos;
	
	private Boolean blNovaUI;

	private ProdutoEspecifico produtoEspecifico;
	
	/** persistent field */
	private BigDecimal nrLimiteMetragemCubica;
	
	/** persistent field */
	private BigDecimal nrLimiteQuantVolume;
	
	public Long getIdSimulacao() {
		return this.idSimulacao;
	}

	public void setIdSimulacao(Long idSimulacao) {
		this.idSimulacao = idSimulacao;
	}

	public Long getNrSimulacao() {
		return this.nrSimulacao;
	}

	public void setNrSimulacao(Long nrSimulacao) {
		this.nrSimulacao = nrSimulacao;
	}

	public DomainValue getTpSimulacao() {
		return this.tpSimulacao;
	}

	public void setTpSimulacao(DomainValue tpSimulacao) {
		this.tpSimulacao = tpSimulacao;
	}

	public DomainValue getTpFormaInsercao() {
		return this.tpFormaInsercao;
	}

	public void setTpFormaInsercao(DomainValue tpFormaInsercao) {
		this.tpFormaInsercao = tpFormaInsercao;
	}

	public YearMonthDay getDtSimulacao() {
		return this.dtSimulacao;
	}

	public void setDtSimulacao(YearMonthDay dtSimulacao) {
		this.dtSimulacao = dtSimulacao;
	}

	public DomainValue getTpIntegranteFrete() {
		return this.tpIntegranteFrete;
	}

	public void setTpIntegranteFrete(DomainValue tpIntegranteFrete) {
		this.tpIntegranteFrete = tpIntegranteFrete;
	}

	public Boolean getBlCalculoPesoCubado() {
		return this.blCalculoPesoCubado;
	}

	public void setBlCalculoPesoCubado(Boolean blCalculoPesoCubado) {
		this.blCalculoPesoCubado = blCalculoPesoCubado;
	}

	public Boolean getBlEfetivada() {
		return blEfetivada;
	}

	public void setBlEfetivada(Boolean blEfetivada) {
		this.blEfetivada = blEfetivada;
	}

	public BigDecimal getPcReeentrega() {
		return this.pcReeentrega;
	}

	public void setPcReeentrega(BigDecimal pcReeentrega) {
		this.pcReeentrega = pcReeentrega;
	}

	public BigDecimal getPcDevolucao() {
		return this.pcDevolucao;
	}

	public void setPcDevolucao(BigDecimal pcDevolucao) {
		this.pcDevolucao = pcDevolucao;
	}

	public Short getNrDiasPrazoPagamento() {
		return this.nrDiasPrazoPagamento;
	}

	public void setNrDiasPrazoPagamento(Short nrDiasPrazoPagamento) {
		this.nrDiasPrazoPagamento = nrDiasPrazoPagamento;
	}

	public YearMonthDay getDtInicial() {
		return this.dtInicial;
	}

	public void setDtInicial(YearMonthDay dtInicial) {
		this.dtInicial = dtInicial;
	}

	public YearMonthDay getDtFinal() {
		return this.dtFinal;
	}

	public void setDtFinal(YearMonthDay dtFinal) {
		this.dtFinal = dtFinal;
	}

	public YearMonthDay getDtEmissaoTabela() {
		return this.dtEmissaoTabela;
	}

	public void setDtEmissaoTabela(YearMonthDay dtEmissaoTabela) {
		this.dtEmissaoTabela = dtEmissaoTabela;
	}

	public YearMonthDay getDtTabelaVigenciaInicial() {
		return this.dtTabelaVigenciaInicial;
	}

	public void setDtTabelaVigenciaInicial(YearMonthDay dtTabelaVigenciaInicial) {
		this.dtTabelaVigenciaInicial = dtTabelaVigenciaInicial;
	}

	public YearMonthDay getDtAceiteCliente() {
		return this.dtAceiteCliente;
	}

	public void setDtAceiteCliente(YearMonthDay dtAceiteCliente) {
		this.dtAceiteCliente = dtAceiteCliente;
	}

	public YearMonthDay getDtAprovacao() {
		return this.dtAprovacao;
	}

	public void setDtAprovacao(YearMonthDay dtAprovacao) {
		this.dtAprovacao = dtAprovacao;
	}

	public YearMonthDay getDtValidadeProposta() {
		return this.dtValidadeProposta;
	}

	public void setDtValidadeProposta(YearMonthDay dtValidadeProposta) {
		this.dtValidadeProposta = dtValidadeProposta;
	}
	
	public YearMonthDay getDtEfetivacao() {
		return dtEfetivacao;
	}
	
	public void setPendenciaAprovacao(Pendencia pendenciaAprovacao) {
		this.pendenciaAprovacao = pendenciaAprovacao;
	}

	public DomainValue getTpFrete() {
		return this.tpFrete;
	}

	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}

	public DomainValue getTpPeriodicidadeFaturamento() {
		return this.tpPeriodicidadeFaturamento;
	}

	public void setTpPeriodicidadeFaturamento(
			DomainValue tpPeriodicidadeFaturamento) {
		this.tpPeriodicidadeFaturamento = tpPeriodicidadeFaturamento;
	}

	public DomainValue getTpSituacaoAprovacao() {
		return this.tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public Blob getDcSimulacao() {
		return this.dcSimulacao;
	}

	public void setDcSimulacao(Blob dcSimulacao) {
		this.dcSimulacao = dcSimulacao;
	}

	public DomainValue getTpRegistro() {
		return this.tpRegistro;
	}

	public void setTpRegistro(DomainValue tpRegistro) {
		this.tpRegistro = tpRegistro;
	}

	public DomainValue getTpGeracaoProposta() {
		return tpGeracaoProposta;
	}

	public void setTpGeracaoProposta(DomainValue tpGeracaoProposta) {
		this.tpGeracaoProposta = tpGeracaoProposta;
	}

	public Boolean getBlPagaFreteTonelada() {
		return blPagaFreteTonelada;
	}

	public void setBlPagaFreteTonelada(Boolean blPagaFreteTonelada) {
		this.blPagaFreteTonelada = blPagaFreteTonelada;
	}

	public String getObProposta() {
		return obProposta;
	}

	public void setObProposta(String obProposta) {
		this.obProposta = obProposta;
	}

	public Boolean getBlEmiteCargaCompleta() {
		return blEmiteCargaCompleta;
	}

	public void setBlEmiteCargaCompleta(Boolean blEmiteCargaCompleta) {
		this.blEmiteCargaCompleta = blEmiteCargaCompleta;
	}

	public UnidadeFederativa getUnidadeFederativaByIdUfDestino() {
		return this.unidadeFederativaByIdUfDestino;
	}

	public void setUnidadeFederativaByIdUfDestino(
			UnidadeFederativa unidadeFederativaByIdUfDestino) {
		this.unidadeFederativaByIdUfDestino = unidadeFederativaByIdUfDestino;
	}

	public UnidadeFederativa getUnidadeFederativaByIdUfOrigem() {
		return this.unidadeFederativaByIdUfOrigem;
	}

	public void setUnidadeFederativaByIdUfOrigem(
			UnidadeFederativa unidadeFederativaByIdUfOrigem) {
		this.unidadeFederativaByIdUfOrigem = unidadeFederativaByIdUfOrigem;
	}

	public Cliente getClienteByIdCliente() {
		return this.clienteByIdCliente;
	}

	public void setClienteByIdCliente(Cliente clienteByIdCliente) {
		this.clienteByIdCliente = clienteByIdCliente;
	}

	public Cliente getClienteByIdClienteBase() {
		return this.clienteByIdClienteBase;
	}

	public void setClienteByIdClienteBase(Cliente clienteByIdClienteBase) {
		this.clienteByIdClienteBase = clienteByIdClienteBase;
	}

	public Aeroporto getAeroportoByIdAeroportoOrigem() {
		return this.aeroportoByIdAeroportoOrigem;
	}

	public void setAeroportoByIdAeroportoOrigem(
			Aeroporto aeroportoByIdAeroportoOrigem) {
		this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
	}

	public Aeroporto getAeroportoByIdAeroportoDestino() {
		return this.aeroportoByIdAeroportoDestino;
	}

	public void setAeroportoByIdAeroportoDestino(
			Aeroporto aeroportoByIdAeroportoDestino) {
		this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
	}

	public Usuario getUsuarioByIdUsuarioAprovou() {
		return this.usuarioByIdUsuarioAprovou;
	}

	public void setUsuarioByIdUsuarioAprovou(Usuario usuarioByIdUsuarioAprovou) {
		this.usuarioByIdUsuarioAprovou = usuarioByIdUsuarioAprovou;
	}

	public Usuario getUsuarioByIdUsuario() {
		return this.usuarioByIdUsuario;
	}

	public void setUsuarioByIdUsuario(Usuario usuarioByIdUsuario) {
		this.usuarioByIdUsuario = usuarioByIdUsuario;
	}

	public Servico getServico() {
		return this.servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public TabelaPreco getTabelaPreco() {
		return this.tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public TabelaPreco getTabelaPrecoFob() {
		return tabelaPrecoFob;
	}

	public void setTabelaPrecoFob(TabelaPreco tabelaPrecoFob) {
		this.tabelaPrecoFob = tabelaPrecoFob;
	}

	public Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DivisaoCliente getDivisaoCliente() {
		return this.divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public Filial getFilial() {
		return this.filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Pendencia getPendenciaAprovacao() {
		return this.pendenciaAprovacao;
	}
	
	public void setDtEfetivacao(YearMonthDay dtEfetivacao) {
		this.dtEfetivacao = dtEfetivacao;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ServicoAdicionalCliente.class) 
	public List<ServicoAdicionalCliente> getServicoAdicionalClientes() {
		return this.servicoAdicionalClientes;
	}

	public void setServicoAdicionalClientes(
			List<ServicoAdicionalCliente> servicoAdicionalClientes) {
		this.servicoAdicionalClientes = servicoAdicionalClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ParametroCliente.class) 
	public List<ParametroCliente> getParametroClientes() {
		return this.parametroClientes;
	}

	public void setParametroClientes(List<ParametroCliente> parametroClientes) {
		this.parametroClientes = parametroClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.NotaFiscalSimulacao.class) 
	public List<NotaFiscalSimulacao> getNotaFiscalSimulacoes() {
		return this.notaFiscalSimulacoes;
	}

	public void setNotaFiscalSimulacoes(
			List<NotaFiscalSimulacao> notaFiscalSimulacoes) {
		this.notaFiscalSimulacoes = notaFiscalSimulacoes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DestinoSimulacao.class) 
	public List<DestinoSimulacao> getDestinoSimulacoes() {
		return this.destinoSimulacoes;
	}

	public void setDestinoSimulacoes(List<DestinoSimulacao> destinoSimulacoes) {
		this.destinoSimulacoes = destinoSimulacoes;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("idSimulacao", getIdSimulacao()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Simulacao))
			return false;
		Simulacao castOther = (Simulacao) other;
		return new EqualsBuilder().append(this.getIdSimulacao(),
				castOther.getIdSimulacao()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdSimulacao()).toHashCode();
	}

	public BigDecimal getNrFatorCubagem() {
		return nrFatorCubagem;
	}

	public void setNrFatorCubagem(BigDecimal nrFatorCubagem) {
		this.nrFatorCubagem = nrFatorCubagem;
	}

	public DomainValue getTpDiferencaAdvalorem() {
		return tpDiferencaAdvalorem;
	}

	public void setTpDiferencaAdvalorem(DomainValue tpDiferencaAdvalorem) {
		this.tpDiferencaAdvalorem = tpDiferencaAdvalorem;
	}

	public BigDecimal getNrFatorDensidade() {
		return nrFatorDensidade;
}

	public void setNrFatorDensidade(BigDecimal nrFatorDensidade) {
		this.nrFatorDensidade = nrFatorDensidade;
	}

	public Funcionario getPromotor() {
		return promotor;
}

	public void setPromotor(Funcionario promotor) {
		this.promotor = promotor;
	}

	public List<SimulacaoAnexo> getAnexos() {
		return anexos;
}

	public void setAnexos(List<SimulacaoAnexo> anexos) {
		this.anexos = anexos;
	}

	public Usuario getUsuarioByIdUsuarioEfetivou() {
		return usuarioByIdUsuarioEfetivou;
	}

	public void setUsuarioByIdUsuarioEfetivou(Usuario usuarioByIdUsuarioEfetivou) {
		this.usuarioByIdUsuarioEfetivou = usuarioByIdUsuarioEfetivou;
	}

	public Boolean getBlNovaUI() {
		return blNovaUI;
	}

	public void setBlNovaUI(Boolean blNovaUI) {
		this.blNovaUI = blNovaUI;
	}

	public ProdutoEspecifico getProdutoEspecifico() {
		return produtoEspecifico;
	}

	public void setProdutoEspecifico(ProdutoEspecifico produtoEspecifico) {
		this.produtoEspecifico = produtoEspecifico;
	}

	public BigDecimal getNrLimiteMetragemCubica() {
		return nrLimiteMetragemCubica;
}

	public void setNrLimiteMetragemCubica(BigDecimal nrLimiteMetragemCubica) {
		this.nrLimiteMetragemCubica = nrLimiteMetragemCubica;
	}

	public BigDecimal getNrLimiteQuantVolume() {
		return nrLimiteQuantVolume;
	}

	public void setNrLimiteQuantVolume(BigDecimal nrLimiteQuantVolume) {
		this.nrLimiteQuantVolume = nrLimiteQuantVolume;
	}

	
}
