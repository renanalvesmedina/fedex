package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author Hibernate CodeGenerator */
public class IntervaloCepUF implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idIntervaloCepUF;

    /** persistent field */
    private String nrCepInicial;

    /** persistent field */
    private String nrCepFinal;

    /** persistent field */
    private DomainValue tpSituacao;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;
	
    public Long getIdIntervaloCepUF() {
		return idIntervaloCepUF;
	}

	public void setIdIntervaloCepUF(Long idIntervaloCepUF) {
		this.idIntervaloCepUF = idIntervaloCepUF;
	}

	public String getNrCepFinal() {
		return nrCepFinal;
	}

	public void setNrCepFinal(String nrCepFinal) {
		this.nrCepFinal = nrCepFinal;
	}

	public String getNrCepInicial() {
		return nrCepInicial;
	}

	public void setNrCepInicial(String nrCepInicial) {
		this.nrCepInicial = nrCepInicial;
	}

	public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idIntervaloCepUF",
				getIdIntervaloCepUF()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof IntervaloCepUF))
			return false;
        IntervaloCepUF castOther = (IntervaloCepUF) other;
		return new EqualsBuilder().append(this.getIdIntervaloCepUF(),
				castOther.getIdIntervaloCepUF()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdIntervaloCepUF()).toHashCode();
    }

}
