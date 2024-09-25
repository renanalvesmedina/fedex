package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;

/** @author LMS Custom Hibernate CodeGenerator */
public class TabelaDivisaoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idTabelaDivisaoCliente;

	/** persistent field */
	private Boolean blAtualizacaoAutomatica;

	/** persistent field */
	private Boolean blPagaFreteTonelada;

	/** nullable persistent field */
	private BigDecimal pcAumento;

	/** nullable persistent field */
	private Boolean blObrigaDimensoes;

	/** nullable persistent field */
	private Boolean blObrigaDimensoesSolicitado;

	private Boolean blPesoAferido;
	
	/** nullable persistent field */
	private Boolean blPesoDeclarado;
	
	/** nullable persistent field */
	private Boolean blPesoCubadoDeclarado;
	
	/** nullable persistent field */
	private Boolean blPesoCubadoAferido;
	
	/** nullable persistent field */
	private Boolean blImpBaseDevolucao;
	
	/** nullable persistent field */
	private Boolean blImpBaseReentrega;
	
	/** nullable persistent field */
	private Boolean blImpBaseRefaturamento;
	
	/** persistent field */
	private Servico servico;

	/** persistent field */
	private TabelaPreco tabelaPreco;

	/** persistent field */
	private DivisaoCliente divisaoCliente;

	/** nullable persistent field */
	private TabelaPreco tabelaPrecoFob;

	/** persistent field */
	private List<ServicoAdicionalCliente> servicoAdicionalClientes;

	/** persistent field */
	private List<ParametroCliente> parametroClientes;

	private BigDecimal nrFatorCubagem;

	private BigDecimal nrFatorCubagemSolicitado;
	
	private BigDecimal nrFatorDensidade;

	private BigDecimal nrFatorDensidadeSolicitado;
	
	private DomainValue tpPesoCalculo;

	private DomainValue tpPesoCalculoSolicitado;
	
	/** transient field **/
	private String dsMotivoSolicitacao;
	
	/** transient field **/
	private Boolean temPendenciaTpPesoCalculo;
	
	/** transient field **/
	private Boolean temPendenciaNrFatorDensidade;
	
	/** transient field **/
	private Boolean temPendenciaNrFatorCubagem;
	
	/** transient field **/
	private Boolean temPendenciaBlObrigaDimensoes;
	
	private List divisaoParcelas;
	
	private BigDecimal nrLimiteMetragemCubica;

	private BigDecimal nrLimiteQuantVolume;
	
	public Long getIdTabelaDivisaoCliente() {
		return this.idTabelaDivisaoCliente;
	}

	public void setIdTabelaDivisaoCliente(Long idTabelaDivisaoCliente) {
		this.idTabelaDivisaoCliente = idTabelaDivisaoCliente;
	}

	public Boolean getBlAtualizacaoAutomatica() {
		return this.blAtualizacaoAutomatica;
	}

	public void setBlAtualizacaoAutomatica(Boolean blAtualizacaoAutomatica) {
		this.blAtualizacaoAutomatica = blAtualizacaoAutomatica;
	}

	public Boolean getBlPagaFreteTonelada() {
		return blPagaFreteTonelada;
	}

	public void setBlPagaFreteTonelada(Boolean blPagaFreteTonelada) {
		this.blPagaFreteTonelada = blPagaFreteTonelada;
	}

	public BigDecimal getPcAumento() {
		return this.pcAumento;
	}

	public void setPcAumento(BigDecimal pcAumento) {
		this.pcAumento = pcAumento;
	}

	public Boolean getBlObrigaDimensoes() {
		return this.blObrigaDimensoes;
	}

	public void setBlObrigaDimensoes(Boolean blObrigaDimensoes) {
		this.blObrigaDimensoes = blObrigaDimensoes;
	}

	public Boolean getBlPesoAferido() {
		return blPesoAferido;
	}

	public void setBlPesoAferido(Boolean blPesoAferido) {
		this.blPesoAferido = blPesoAferido;
	}

	public Boolean getBlPesoDeclarado() {
		return blPesoDeclarado;
	}

	public void setBlPesoDeclarado(Boolean blPesoDeclarado) {
		this.blPesoDeclarado = blPesoDeclarado;
	}

	public Boolean getBlPesoCubadoDeclarado() {
		return blPesoCubadoDeclarado;
	}

	public void setBlPesoCubadoDeclarado(Boolean blPesoCubadoDeclarado) {
		this.blPesoCubadoDeclarado = blPesoCubadoDeclarado;
	}

	public Boolean getBlPesoCubadoAferido() {
		return blPesoCubadoAferido;
	}

	public void setBlPesoCubadoAferido(Boolean blPesoCubadoAferido) {
		this.blPesoCubadoAferido = blPesoCubadoAferido;
	}

	public Boolean getBlImpBaseDevolucao() {
		return blImpBaseDevolucao;
	}

	public void setBlImpBaseDevolucao(Boolean blImpBaseDevolucao) {
		this.blImpBaseDevolucao = blImpBaseDevolucao;
	}

	public Boolean getBlImpBaseReentrega() {
		return blImpBaseReentrega;
	}

	public void setBlImpBaseReentrega(Boolean blImpBaseReentrega) {
		this.blImpBaseReentrega = blImpBaseReentrega;
	}

	public Boolean getBlImpBaseRefaturamento() {
		return blImpBaseRefaturamento;
	}

	public void setBlImpBaseRefaturamento(Boolean blImpBaseRefaturamento) {
		this.blImpBaseRefaturamento = blImpBaseRefaturamento;
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

	public DivisaoCliente getDivisaoCliente() {
		return this.divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public TabelaPreco getTabelaPrecoFob() {
		return tabelaPrecoFob;
	}

	public void setTabelaPrecoFob(TabelaPreco tabelaPrecoFob) {
		this.tabelaPrecoFob = tabelaPrecoFob;
	}

	@ParametrizedAttribute(type = ServicoAdicionalCliente.class)	 
	public List<ServicoAdicionalCliente> getServicoAdicionalClientes() {
		return this.servicoAdicionalClientes;
	}

	public void setServicoAdicionalClientes(
			List<ServicoAdicionalCliente> servicoAdicionalClientes) {
		this.servicoAdicionalClientes = servicoAdicionalClientes;
	}

	@ParametrizedAttribute(type = ParametroCliente.class)	 
	public List<ParametroCliente> getParametroClientes() {
		return this.parametroClientes;
	}

	public void setParametroClientes(List<ParametroCliente> parametroClientes) {
		this.parametroClientes = parametroClientes;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idTabelaDivisaoCliente",
				getIdTabelaDivisaoCliente()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TabelaDivisaoCliente))
			return false;
		TabelaDivisaoCliente castOther = (TabelaDivisaoCliente) other;
		return new EqualsBuilder().append(this.getIdTabelaDivisaoCliente(),
				castOther.getIdTabelaDivisaoCliente()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdTabelaDivisaoCliente())
			.toHashCode();
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

	public DomainValue getTpPesoCalculo() {
		return tpPesoCalculo;
	}

	public void setTpPesoCalculo(DomainValue tpPesoCalculo) {
		this.tpPesoCalculo = tpPesoCalculo;
	}

	public DomainValue getTpPesoCalculoSolicitado() {
		return tpPesoCalculoSolicitado;
	}

	public void setTpPesoCalculoSolicitado(DomainValue tpPesoCalculoSolicitado) {
		this.tpPesoCalculoSolicitado = tpPesoCalculoSolicitado;
	}

	public String getDsMotivoSolicitacao() {
		return dsMotivoSolicitacao;
	}

	public void setDsMotivoSolicitacao(String dsMotivoSolicitacao) {
		this.dsMotivoSolicitacao = dsMotivoSolicitacao;
	}

	public Boolean getTemPendenciaTpPesoCalculo() {
		return temPendenciaTpPesoCalculo;
	}

	public void setTemPendenciaTpPesoCalculo(Boolean temPendenciaTpPesoCalculo) {
		this.temPendenciaTpPesoCalculo = temPendenciaTpPesoCalculo;
	}

	public Boolean getTemPendenciaNrFatorDensidade() {
		return temPendenciaNrFatorDensidade;
	}

	public void setTemPendenciaNrFatorDensidade(Boolean temPendenciaNrFatorDensidade) {
		this.temPendenciaNrFatorDensidade = temPendenciaNrFatorDensidade;
	}

	public Boolean getTemPendenciaNrFatorCubagem() {
		return temPendenciaNrFatorCubagem;
	}

	public void setTemPendenciaNrFatorCubagem(Boolean temPendenciaNrFatorCubagem) {
		this.temPendenciaNrFatorCubagem = temPendenciaNrFatorCubagem;
	}

	public Boolean getTemPendenciaBlObrigaDimensoes() {
		return temPendenciaBlObrigaDimensoes;
	}

	public void setTemPendenciaBlObrigaDimensoes(Boolean temPendenciaBlObrigaDimensoes) {
		this.temPendenciaBlObrigaDimensoes = temPendenciaBlObrigaDimensoes;
	}

	public Boolean getBlObrigaDimensoesSolicitado() {
		return blObrigaDimensoesSolicitado;
	}

	public void setBlObrigaDimensoesSolicitado(Boolean blObrigaDimensoesSolicitado) {
		this.blObrigaDimensoesSolicitado = blObrigaDimensoesSolicitado;
	}

	public BigDecimal getNrFatorCubagemSolicitado() {
		return nrFatorCubagemSolicitado;
	}

	public void setNrFatorCubagemSolicitado(BigDecimal nrFatorCubagemSolicitado) {
		this.nrFatorCubagemSolicitado = nrFatorCubagemSolicitado;
	}

	public BigDecimal getNrFatorDensidadeSolicitado() {
		return nrFatorDensidadeSolicitado;
	}

	public void setNrFatorDensidadeSolicitado(BigDecimal nrFatorDensidadeSolicitado) {
		this.nrFatorDensidadeSolicitado = nrFatorDensidadeSolicitado;
	}

	@ParametrizedAttribute(type = DivisaoParcela.class)
	public List getDivisaoParcelas() {
		return divisaoParcelas;
	}

	public void setDivisaoParcelas(List divisaoParcelas) {
		this.divisaoParcelas = divisaoParcelas;
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
