package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/**
 * The persistent class for the ITEM_LOTE_SERASA database table.
 * 
 */
@Entity
@Table(name="ITEM_LOTE_SERASA")
public class ItemLoteSerasa  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ITEM_LOTE_SERASA")
    @SequenceGenerator(name = "ITEM_LOTE_SERASA_SEQ", sequenceName = "ITEM_LOTE_SERASA_SQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_LOTE_SERASA_SEQ")
	private Long idItemLoteSerasa;
	
	@ManyToOne
    @JoinColumn(name = "ID_LOTE_SERASA" , nullable = false)
    private LoteSerasa loteSerasa;
	
	@ManyToOne
	@JoinColumn(name = "ID_FATURA", nullable = false)
	private Fatura fatura;
	
	@ManyToOne
	@JoinColumn(name = "ID_MOTIVO_BAIXA_SERASA", nullable = true)
	private MotivoBaixaSerasa motivoBaixaSerasa;
	
	@Transient
	private String sgFilial;
	
	@Transient
	private Long nrFatura;
	
	@Transient
	private String nrFaturaDesc;
	
	@Transient
	private String nmPessoa;
	
	@Transient
	private BigDecimal vlSaldo;
	
	@Transient
	private YearMonthDay dtEmissao;
	
	@Transient
	private YearMonthDay dtVencimento;
	
	@Transient
	private DomainValue tpSituacaoFatura;
	
	@Transient
	private DomainValue tpIdentificacao;
	
	@Transient
	private String nrIdentificacao;
	
	@Transient
	private String nrIdentificacaoFormatado;
	
	@Transient
	private String sgMoeda;
	
	@Transient
	private String dsSimbolo;

	public ItemLoteSerasa() {
	}

	public Long getIdItemLoteSerasa() {
		return idItemLoteSerasa;
	}

	public void setIdItemLoteSerasa(Long idItemLoteSerasa) {
		this.idItemLoteSerasa = idItemLoteSerasa;
	}

	public LoteSerasa getLoteSerasa() {
		return loteSerasa;
	}

	public void setLoteSerasa(LoteSerasa loteSerasa) {
		this.loteSerasa = loteSerasa;
	}

	public Long getNrFatura() {
		return nrFatura;
	}

	public void setNrFatura(Long nrFatura) {
		this.nrFatura = nrFatura;
	}

	
	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public BigDecimal getVlSaldo() {
		return vlSaldo;
	}

	public void setVlSaldo(BigDecimal vlSaldo) {
		this.vlSaldo = vlSaldo;
	}

	public YearMonthDay getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(YearMonthDay dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public YearMonthDay getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(YearMonthDay dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public DomainValue getTpSituacaoFatura() {
		return tpSituacaoFatura;
	}

	public void setTpSituacaoFatura(DomainValue tpSituacaoFatura) {
		this.tpSituacaoFatura = tpSituacaoFatura;
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public DomainValue getTpIdentificacao() {
		return tpIdentificacao;
	}

	public void setTpIdentificacao(DomainValue tpIdentificacao) {
		this.tpIdentificacao = tpIdentificacao;
	}

	public String getNrIdentificacao() {
		return nrIdentificacao;
	}

	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}

	public String getNrIdentificacaoFormatado() {
		return nrIdentificacaoFormatado;
	}

	public void setNrIdentificacaoFormatado(String nrIdentificacaoFormatado) {
		this.nrIdentificacaoFormatado = nrIdentificacaoFormatado;
	}

	public String getSgMoeda() {
		return sgMoeda;
	}

	public void setSgMoeda(String sgMoeda) {
		this.sgMoeda = sgMoeda;
	}

	public String getDsSimbolo() {
		return dsSimbolo;
	}

	public void setDsSimbolo(String dsSimbolo) {
		this.dsSimbolo = dsSimbolo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
		* result
		+ ((idItemLoteSerasa == null) ? 0 : idItemLoteSerasa.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemLoteSerasa other = (ItemLoteSerasa) obj;
		if (idItemLoteSerasa == null) {
			if (other.idItemLoteSerasa != null)
				return false;
		} else if (!idItemLoteSerasa.equals(other.idItemLoteSerasa))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "ItemLoteSerasa [idItemLoteSerasa=" + idItemLoteSerasa + "]";
	}

	public String getNrFaturaDesc() {
		return nrFaturaDesc;
	}

	public void setNrFaturaDesc(String nrFaturaDesc) {
		this.nrFaturaDesc = nrFaturaDesc;
	}
	
	public MotivoBaixaSerasa getMotivoBaixaSerasa() {
		return motivoBaixaSerasa;
	}
	public void setMotivoBaixaSerasa(MotivoBaixaSerasa motivoBaixaSerasa) {
		this.motivoBaixaSerasa = motivoBaixaSerasa;
	}
}