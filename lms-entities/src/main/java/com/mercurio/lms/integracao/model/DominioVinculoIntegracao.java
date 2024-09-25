package com.mercurio.lms.integracao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DominioVinculoIntegracao implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idDominioVinculoIntegracao;

    /** persistent field */
    private String dsValorLms;

    /** persistent field */
    private String dsValorCorporativo;

    /** persistent field */
    private String dsValorClipper;
    
    /** persistent field */
    private Boolean blLmsCorporativo;
    
    /** persistent field */
    private Boolean blLmsClipper;

    /** persistent field */
    private String dsSignificadoIntegracao;
	
    /** persistent field */
	private com.mercurio.lms.integracao.model.DominioNomeIntegracao dominioNomeIntegracao;

	public Long getIdDominioVinculoIntegracao() {
		return idDominioVinculoIntegracao;
	}

	public void setIdDominioVinculoIntegracao(Long idDominioVinculoIntegracao) {
		this.idDominioVinculoIntegracao = idDominioVinculoIntegracao;
	}

	public String getDsValorLms() {
		return dsValorLms;
	}

	public void setDsValorLms(String dsValorLms) {
		this.dsValorLms = dsValorLms;
	}

	public String getDsValorCorporativo() {
		return dsValorCorporativo;
	}

	public void setDsValorCorporativo(String dsValorCorporativo) {
		this.dsValorCorporativo = dsValorCorporativo;
	}

	public String getDsValorClipper() {
		return dsValorClipper;
	}

	public void setDsValorClipper(String dsValorClipper) {
		this.dsValorClipper = dsValorClipper;
	}

	public Boolean getBlLmsClipper() {
		return blLmsClipper;
	}

	public void setBlLmsClipper(Boolean blLmsClipper) {
		this.blLmsClipper = blLmsClipper;
	}

	public Boolean getBlLmsCorporativo() {
		return blLmsCorporativo;
	}

	public void setBlLmsCorporativo(Boolean blLmsCorporativo) {
		this.blLmsCorporativo = blLmsCorporativo;
	}

	public String getDsSignificadoIntegracao() {
		return dsSignificadoIntegracao;
	}

	public void setDsSignificadoIntegracao(String dsSignificadoIntegracao) {
		this.dsSignificadoIntegracao = dsSignificadoIntegracao;
	}

	public com.mercurio.lms.integracao.model.DominioNomeIntegracao getDominioNomeIntegracao() {
		return dominioNomeIntegracao;
	}

	public void setDominioNomeIntegracao(
			com.mercurio.lms.integracao.model.DominioNomeIntegracao dominioNomeIntegracao) {
		this.dominioNomeIntegracao = dominioNomeIntegracao;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idDominioVinculoIntegracao",
				getIdDominioVinculoIntegracao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DominioVinculoIntegracao))
			return false;
        DominioVinculoIntegracao castOther = (DominioVinculoIntegracao) other;
		return new EqualsBuilder().append(this.getIdDominioVinculoIntegracao(),
				castOther.getIdDominioVinculoIntegracao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDominioVinculoIntegracao())
            .toHashCode();
    }
	
}
