package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.vendas.model.Cotacao;

/** @author LMS Custom Hibernate CodeGenerator */
public class Dimensao implements Serializable, Comparable<Dimensao> {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idDimensao;

	/** nullable persistent field */
	private Integer nrAltura;

	/** nullable persistent field */
	private Integer nrLargura;

	/** nullable persistent field */
	private Integer nrComprimento;

	/** nullable persistent field */
	private Integer nrQuantidade;

	/** persistent field */
	private CtoInternacional ctoInternacional;

	/** persistent field */
	private Conhecimento conhecimento;

	/** persistent field */
	private Awb awb;

	/** persistent field */
	private Cotacao cotacao;

	public Long getIdDimensao() {
		return this.idDimensao;
	}

	public void setIdDimensao(Long idDimensao) {
		this.idDimensao = idDimensao;
	}

	public Integer getNrAltura() {
		return this.nrAltura;
	}

	public void setNrAltura(Integer nrAltura) {
		this.nrAltura = nrAltura;
	}

	public Integer getNrLargura() {
		return this.nrLargura;
	}

	public void setNrLargura(Integer nrLargura) {
		this.nrLargura = nrLargura;
	}

	public Integer getNrComprimento() {
		return this.nrComprimento;
	}

	public void setNrComprimento(Integer nrComprimento) {
		this.nrComprimento = nrComprimento;
	}

	public Integer getNrQuantidade() {
		return this.nrQuantidade;
	}

	public void setNrQuantidade(Integer nrQuantidade) {
		this.nrQuantidade = nrQuantidade;
	}

	public CtoInternacional getCtoInternacional() {
		return this.ctoInternacional;
	}

	public void setCtoInternacional(CtoInternacional ctoInternacional) {
		this.ctoInternacional = ctoInternacional;
	}

	public Conhecimento getConhecimento() {
		return this.conhecimento;
	}

	public void setConhecimento(Conhecimento conhecimento) {
		this.conhecimento = conhecimento;
	}

	public Awb getAwb() {
		return this.awb;
	}

	public void setAwb(Awb awb) {
		this.awb = awb;
	}
 
	public Cotacao getCotacao() {
		return this.cotacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idDimensao", getIdDimensao())
			.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Dimensao))
			return false;
		Dimensao castOther = (Dimensao) other;
		return new EqualsBuilder().append(this.getIdDimensao(),
				castOther.getIdDimensao()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdDimensao()).toHashCode();
	}

	/**
	 * Uma dimensão é maior que a outra caso o seu volume total final seja maior
	 * que o outro.
	 */
	public int compareTo(Dimensao o) {
		Integer volumeTotal = nrLargura * nrAltura * nrComprimento
				* nrQuantidade;
		Integer volumeTotalOther = o.nrLargura * o.nrAltura * o.nrComprimento
				* o.nrQuantidade;
		return volumeTotal.compareTo(volumeTotalOther);
	}

}
