package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class Moeda implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idMoeda;

	/** persistent field */
	private VarcharI18n dsMoeda;

	/** persistent field */
	private String sgMoeda;

	/** persistent field */
	private String dsSimbolo;

	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private VarcharI18n dsValorExtenso;

	/** persistent field */
	private Short nrIsoCode;

	/** persistent field */
	private List<MoedaPais> moedaPais;

	/**
	 * Construtor padrão
	 */
	public Moeda() {
		
	}
	
	public Moeda(Long idMoeda) {
		this.idMoeda = idMoeda;
	}
	
	/**
	 * Construtor sem o campo intercionalizado
	 * 
	 * @author Diego Umpierre
	 * @since 18/08/2006
	 * 
	 * @param idMoeda
	 * @param dsMoeda
	 * @param sgMoeda
	 * @param dsSimbolo
	 * @param tpSituacao
	 */
	public Moeda(Long idMoeda, VarcharI18n dsMoeda, String sgMoeda,
			String dsSimbolo, DomainValue tpSituacao) {
		super();
		this.idMoeda = idMoeda;
		this.dsMoeda = dsMoeda;
		this.sgMoeda = sgMoeda;
		this.dsSimbolo = dsSimbolo;
		this.tpSituacao = tpSituacao;
	}

	public Long getIdMoeda() {
		return this.idMoeda;
	}

	public void setIdMoeda(Long idMoeda) {
		this.idMoeda = idMoeda;
	}

	public VarcharI18n getDsMoeda() {
		return dsMoeda;
	}

	public void setDsMoeda(VarcharI18n dsMoeda) {
		this.dsMoeda = dsMoeda;
	}

	public String getSgMoeda() {
		return this.sgMoeda;
	}

	public void setSgMoeda(String sgMoeda) {
		this.sgMoeda = sgMoeda;
	}

	public String getDsSimbolo() {
		return this.dsSimbolo;
	}

	public void setDsSimbolo(String dsSimbolo) {
		this.dsSimbolo = dsSimbolo;
	}

	/**
	 * Método adicionado. Não reflete diretamente um campo no banco de dados.
	 */
	public String getSiglaSimbolo() {
		if (sgMoeda != null || dsSimbolo != null) { 
			return sgMoeda + " " + dsSimbolo;
		} else {
			return null;
		}
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public VarcharI18n getDsValorExtenso() {
		return dsValorExtenso;
	}

	public void setDsValorExtenso(VarcharI18n dsValorExtenso) {
		this.dsValorExtenso = dsValorExtenso;
	}

	public Short getNrIsoCode() {
		return nrIsoCode;
	}

	public void setNrIsoCode(Short nrIsoCode) {
		this.nrIsoCode = nrIsoCode;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.MoedaPais.class)
	public List<MoedaPais> getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(List<MoedaPais> moedaPais) {
		this.moedaPais = moedaPais;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idMoeda", getIdMoeda())
			.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Moeda))
			return false;
		Moeda castOther = (Moeda) other;
		return new EqualsBuilder().append(this.getIdMoeda(),
				castOther.getIdMoeda()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdMoeda()).toHashCode();
	}

}
