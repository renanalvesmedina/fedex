package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReajusteCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idReajusteCliente;

	/** persistent field */
	private Long nrReajuste;

	/** persistent field */
	private DomainValue tpSituacaoAprovacao;

	/** persistent field */
	private String dsJustificativa;

	/** persistent field */
	private Boolean blEfetivado;

	/** persistent field */
	private BigDecimal pcReajusteSugerido;

	/** persistent field */
	private BigDecimal pcReajusteAcordado;

	/** persistent field */
	private YearMonthDay dtInicioVigencia;

	/** persistent field */
	private YearMonthDay dtAprovacao;

	/** persistent field */
	private Usuario usuarioByIdUsuarioAprovou;
	
	/** persistent field */
	private Pendencia pendenciaAprovacao;

	/** persistent field */
	private Filial filial;

	/** persistent field */
	private Cliente cliente;

	/** persistent field */
	private DivisaoCliente divisaoCliente;

	/** persistent field */
	private TabelaDivisaoCliente tabelaDivisaoCliente;

	/** persistent field */
	private TabelaPreco tabelaPreco;

	/** persistent field */
	private Boolean blReajustaPercTde;
	
	/** persistent field */
	private Boolean blReajustaPercTrt;
	
	/** persistent field */
	private Boolean blReajustaPercGris;
	
	/** persistent field */
	private Boolean blReajustaAdValorEm;

	/** persistent field */
	private Boolean blReajustaAdValorEm2;
	
	/** persistent field */
	private Boolean blReajustaFretePercentual;
	
	/** persistent field */
	private Boolean blGerarSomenteMarcados;
	
	/** persistent field */
	private YearMonthDay dtEfetivacao;
	
	/** persistent field */
	private Usuario usuarioEfetivacao;
	
	
	
	public Long getIdReajusteCliente() {
		return idReajusteCliente;
	}

	public void setIdReajusteCliente(Long idReajusteCliente) {
		this.idReajusteCliente = idReajusteCliente;
	}

	public Long getNrReajuste() {
		return nrReajuste;
	}

	public void setNrReajuste(Long nrReajuste) {
		this.nrReajuste = nrReajuste;
	}

	public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public String getDsJustificativa() {
		return dsJustificativa;
	}

	public void setDsJustificativa(String dsJustificativa) {
		this.dsJustificativa = dsJustificativa;
	}

	public Boolean getBlEfetivado() {
		return blEfetivado;
	}

	public void setBlEfetivado(Boolean blEfetivado) {
		this.blEfetivado = blEfetivado;
	}

	public BigDecimal getPcReajusteSugerido() {
		return pcReajusteSugerido;
	}

	public void setPcReajusteSugerido(BigDecimal pcReajusteSugerido) {
		this.pcReajusteSugerido = pcReajusteSugerido;
	}

	public BigDecimal getPcReajusteAcordado() {
		return pcReajusteAcordado;
	}

	public void setPcReajusteAcordado(BigDecimal pcReajusteAcordado) {
		this.pcReajusteAcordado = pcReajusteAcordado;
	}

	public YearMonthDay getDtInicioVigencia() {
		return dtInicioVigencia;
	}

	public void setDtInicioVigencia(YearMonthDay dtInicioVigencia) {
		this.dtInicioVigencia = dtInicioVigencia;
	}

	public YearMonthDay getDtAprovacao() {
		return dtAprovacao;
	}

	public void setDtAprovacao(YearMonthDay dtAprovacao) {
		this.dtAprovacao = dtAprovacao;
	}

	public Usuario getUsuarioByIdUsuarioAprovou() {
		return usuarioByIdUsuarioAprovou;
	}

	public void setUsuarioByIdUsuarioAprovou(Usuario usuarioByIdUsuarioAprovou) {
		this.usuarioByIdUsuarioAprovou = usuarioByIdUsuarioAprovou;
	}

	public Pendencia getPendenciaAprovacao() {
		return pendenciaAprovacao;
	}

	public void setPendenciaAprovacao(Pendencia pendenciaAprovacao) {
		this.pendenciaAprovacao = pendenciaAprovacao;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public TabelaDivisaoCliente getTabelaDivisaoCliente() {
		return tabelaDivisaoCliente;
	}

	public void setTabelaDivisaoCliente(
			TabelaDivisaoCliente tabelaDivisaoCliente) {
		this.tabelaDivisaoCliente = tabelaDivisaoCliente;
	}

	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	/**
	 * @return the blReajustaPercTde
	 */
	public Boolean getBlReajustaPercTde() {
		return blReajustaPercTde;
	}

	/**
	 * @return the blReajustaPercTrt
	 */
	public Boolean getBlReajustaPercTrt() {
		return blReajustaPercTrt;
	}

	/**
	 * @return the blReajustaPercGris
	 */
	public Boolean getBlReajustaPercGris() {
		return blReajustaPercGris;
	}

	/**
	 * @return the blReajustaAdValorEm
	 */
	public Boolean getBlReajustaAdValorEm() {
		return blReajustaAdValorEm;
	}

	/**
	 * @return the blReajustaAdValorEm2
	 */
	public Boolean getBlReajustaAdValorEm2() {
		return blReajustaAdValorEm2;
	}

	/**
	 * @return the blReajustaFretePercentual
	 */
	public Boolean getBlReajustaFretePercentual() {
		return blReajustaFretePercentual;
	}

	/**
	 * @return the blGerarSomenteMarcados
	 */
	public Boolean getBlGerarSomenteMarcados() {
		return blGerarSomenteMarcados;
	}

	/**
	 * @param blReajustaPercTde the blReajustaPercTde to set
	 */
	public void setBlReajustaPercTde(Boolean blReajustaPercTde) {
		this.blReajustaPercTde = blReajustaPercTde;
	}

	/**
	 * @param blReajustaPercTrt the blReajustaPercTrt to set
	 */
	public void setBlReajustaPercTrt(Boolean blReajustaPercTrt) {
		this.blReajustaPercTrt = blReajustaPercTrt;
	}

	/**
	 * @param blReajustaPercGris the blReajustaPercGris to set
	 */
	public void setBlReajustaPercGris(Boolean blReajustaPercGris) {
		this.blReajustaPercGris = blReajustaPercGris;
	}

	/**
	 * @param blReajustaAdValorEm the blReajustaAdValorEm to set
	 */
	public void setBlReajustaAdValorEm(Boolean blReajustaAdValorEm) {
		this.blReajustaAdValorEm = blReajustaAdValorEm;
	}

	/**
	 * @param blReajustaAdValorEm2 the blReajustaAdValorEm2 to set
	 */
	public void setBlReajustaAdValorEm2(Boolean blReajustaAdValorEm2) {
		this.blReajustaAdValorEm2 = blReajustaAdValorEm2;
	}

	/**
	 * @param blReajustaFretePercentual the blReajustaFretePercentual to set
	 */
	public void setBlReajustaFretePercentual(Boolean blReajustaFretePercentual) {
		this.blReajustaFretePercentual = blReajustaFretePercentual;
	}

	/**
	 * @param blGerarSomenteMarcados the blGerarSomenteMarcados to set
	 */
	public void setBlGerarSomenteMarcados(Boolean blGerarSomenteMarcados) {
		this.blGerarSomenteMarcados = blGerarSomenteMarcados;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idReajusteCliente",
				getIdReajusteCliente()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReajusteCliente))
			return false;
		ReajusteCliente castOther = (ReajusteCliente) other;
		return new EqualsBuilder().append(this.getIdReajusteCliente(),
				castOther.getIdReajusteCliente()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdReajusteCliente())
			.toHashCode();
	}

	public YearMonthDay getDtEfetivacao() {
		return dtEfetivacao;
	}

	public void setDtEfetivacao(YearMonthDay dtEfetivacao) {
		this.dtEfetivacao = dtEfetivacao;
	}

	public Usuario getUsuarioEfetivacao() {
		return usuarioEfetivacao;
	}

	public void setUsuarioEfetivacao(Usuario usuarioEfetivacao) {
		this.usuarioEfetivacao = usuarioEfetivacao;
	}


}
