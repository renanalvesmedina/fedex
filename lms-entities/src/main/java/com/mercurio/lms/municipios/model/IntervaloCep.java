package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author Hibernate CodeGenerator */
public class IntervaloCep implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idIntervaloCep;

    /** persistent field */
    private String nrCepInicial;

    /** persistent field */
    private String nrCepFinal;

    /** persistent field */
    private DomainValue tpSituacao;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** full constructor */
	public IntervaloCep(String nrCepInicial, String nrCepFinal,
			DomainValue tpSituacao,
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.nrCepInicial = nrCepInicial;
        this.nrCepFinal = nrCepFinal;
        this.municipio = municipio;
        this.tpSituacao = tpSituacao;
    }

    /** default constructor */
    public IntervaloCep() {
    }

    public Long getIdIntervaloCep() {
        return this.idIntervaloCep;
    }

    public void setIdIntervaloCep(Long idIntervaloCep) {
        this.idIntervaloCep = idIntervaloCep;
    }

    public String getNrCepInicial() {
        return this.nrCepInicial;
    }

    public void setNrCepInicial(String nrCepInicial) {
        this.nrCepInicial = nrCepInicial;
    }

    public String getNrCepFinal() {
        return this.nrCepFinal;
    }

    public void setNrCepFinal(String nrCepFinal) {
        this.nrCepFinal = nrCepFinal;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }
	
    public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idIntervaloCep",
				getIdIntervaloCep()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof IntervaloCep))
			return false;
        IntervaloCep castOther = (IntervaloCep) other;
		return new EqualsBuilder().append(this.getIdIntervaloCep(),
				castOther.getIdIntervaloCep()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdIntervaloCep()).toHashCode();
    }

}
