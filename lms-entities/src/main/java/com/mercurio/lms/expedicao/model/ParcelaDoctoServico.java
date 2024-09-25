package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParcelaDoctoServico implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idParcelaDoctoServico;

	/** persistent field */
	private BigDecimal vlBrutoParcela;

	/** persistent field */
	private BigDecimal vlParcela;

	/** persistent field */
	private ParcelaPreco parcelaPreco;

	/** persistent field */
	private DoctoServico doctoServico;

	/** full constructor */
	public ParcelaDoctoServico(BigDecimal vlBrutoParcela, BigDecimal vlParcela,
			ParcelaPreco parcelaPreco, DoctoServico doctoServico) {
		this.vlBrutoParcela = vlBrutoParcela;
		this.vlParcela = vlParcela;
		this.parcelaPreco = parcelaPreco;
		this.doctoServico = doctoServico;
	}

	public ParcelaDoctoServico(ParcelaServico parcelaServico,
			DoctoServico doctoServico) {
		this(parcelaServico.getVlBrutoParcela(), parcelaServico.getVlParcela(),
				parcelaServico.getParcelaPreco(), doctoServico);
	}

	/** default constructor */
	public ParcelaDoctoServico() {
	}

	public Long getIdParcelaDoctoServico() {
		return this.idParcelaDoctoServico;
	}

	public void setIdParcelaDoctoServico(Long idParcelaDoctoServico) {
		this.idParcelaDoctoServico = idParcelaDoctoServico;
	}

	public BigDecimal getVlBrutoParcela() {
		return vlBrutoParcela;
	}

	public void setVlBrutoParcela(BigDecimal vlBrutoParcela) {
		this.vlBrutoParcela = vlBrutoParcela;
	}

	public BigDecimal getVlParcela() {
		return this.vlParcela;
	}

	public void setVlParcela(BigDecimal vlParcela) {
		this.vlParcela = vlParcela;
	}

	public ParcelaPreco getParcelaPreco() {
		return this.parcelaPreco;
	}

	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public DoctoServico getDoctoServico() {
		return this.doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idParcelaDoctoServico",
				getIdParcelaDoctoServico()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParcelaDoctoServico))
			return false;
		ParcelaDoctoServico castOther = (ParcelaDoctoServico) other;
		return new EqualsBuilder().append(this.getIdParcelaDoctoServico(),
				castOther.getIdParcelaDoctoServico()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdParcelaDoctoServico())
			.toHashCode();
	}

}
