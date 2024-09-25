package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParcelaCotacao implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idParcelaCotacao;

	/** persistent field */
	private BigDecimal vlBrutoParcela;

	/** persistent field */
	private BigDecimal vlParcelaCotacao;

	/** persistent field */
	private ParcelaPreco parcelaPreco;

	/** persistent field */
	private Cotacao cotacao;

	public Long getIdParcelaCotacao() {
		return this.idParcelaCotacao;
	}

	public void setIdParcelaCotacao(Long idParcelaCotacao) {
		this.idParcelaCotacao = idParcelaCotacao;
	}

	public BigDecimal getVlBrutoParcela() {
		return vlBrutoParcela;
	}

	public void setVlBrutoParcela(BigDecimal vlBrutoParcela) {
		this.vlBrutoParcela = vlBrutoParcela;
	}

	public BigDecimal getVlParcelaCotacao() {
		return this.vlParcelaCotacao;
	}

	public void setVlParcelaCotacao(BigDecimal vlParcelaCotacao) {
		this.vlParcelaCotacao = vlParcelaCotacao;
	}

	public ParcelaPreco getParcelaPreco() {
		return this.parcelaPreco;
	}

	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public Cotacao getCotacao() {
		return this.cotacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idParcelaCotacao",
				getIdParcelaCotacao()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParcelaCotacao))
			return false;
		ParcelaCotacao castOther = (ParcelaCotacao) other;
		return new EqualsBuilder().append(this.getIdParcelaCotacao(),
				castOther.getIdParcelaCotacao()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdParcelaCotacao()).toHashCode();
	}

}
