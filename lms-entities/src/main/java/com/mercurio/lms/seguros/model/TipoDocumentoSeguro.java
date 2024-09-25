package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoDocumentoSeguro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoDocumentoSeguro;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private String dsTipo;

    /** persistent field */
    private List doctoProcessoSinistros;

    public Long getIdTipoDocumentoSeguro() {
        return this.idTipoDocumentoSeguro;
    }

    public void setIdTipoDocumentoSeguro(Long idTipoDocumentoSeguro) {
        this.idTipoDocumentoSeguro = idTipoDocumentoSeguro;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getDsTipo() {
        return this.dsTipo;
    }

    public void setDsTipo(String dsTipo) {
        this.dsTipo = dsTipo;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.DoctoProcessoSinistro.class)     
    public List getDoctoProcessoSinistros() {
        return this.doctoProcessoSinistros;
    }

    public void setDoctoProcessoSinistros(List doctoProcessoSinistros) {
        this.doctoProcessoSinistros = doctoProcessoSinistros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoDocumentoSeguro",
				getIdTipoDocumentoSeguro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoDocumentoSeguro))
			return false;
        TipoDocumentoSeguro castOther = (TipoDocumentoSeguro) other;
		return new EqualsBuilder().append(this.getIdTipoDocumentoSeguro(),
				castOther.getIdTipoDocumentoSeguro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoDocumentoSeguro())
            .toHashCode();
    }

}
