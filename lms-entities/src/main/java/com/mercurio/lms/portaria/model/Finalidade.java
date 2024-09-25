package com.mercurio.lms.portaria.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class Finalidade implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFinalidade;

    /** persistent field */
    private VarcharI18n dsFinalidade;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private DomainValue tpControleCarga;

    /** persistent field */
    private Boolean blDescarga;

    /** persistent field */
    private List boxFinalidades;

    public Long getIdFinalidade() {
        return this.idFinalidade;
    }

    public void setIdFinalidade(Long idFinalidade) {
        this.idFinalidade = idFinalidade;
    }

    public VarcharI18n getDsFinalidade() {
		return dsFinalidade;
    }

	public void setDsFinalidade(VarcharI18n dsFinalidade) {
        this.dsFinalidade = dsFinalidade;
    }

    public DomainValue getTpControleCarga() {
        return this.tpControleCarga;
    }

    public void setTpControleCarga(DomainValue tpControleCarga) {
        this.tpControleCarga = tpControleCarga;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.BoxFinalidade.class)     
    public List getBoxFinalidades() {
        return this.boxFinalidades;
    }

    public void setBoxFinalidades(List boxFinalidades) {
        this.boxFinalidades = boxFinalidades;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFinalidade",
				getIdFinalidade()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Finalidade))
			return false;
        Finalidade castOther = (Finalidade) other;
		return new EqualsBuilder().append(this.getIdFinalidade(),
				castOther.getIdFinalidade()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFinalidade()).toHashCode();
    }

	/**
	 * @return Returns the tpSituacao.
	 */
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	/**
	 * @param tpSituacao
	 *            The tpSituacao to set.
	 */
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	/**
	 * @return Returns the blDescarga.
	 */
	public Boolean getBlDescarga() {
		return blDescarga;
	}

	/**
	 * @param blDescarga
	 *            The blDescarga to set.
	 */
	public void setBlDescarga(Boolean blDescarga) {
		this.blDescarga = blDescarga;
	}

}
