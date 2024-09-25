package com.mercurio.lms.seguros.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class FotoProcessoSinistro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFotoProcessoSinistro;

    /** persistent field */
    private String dsFoto;

    /** persistent field */
    private com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Foto foto;

    public Long getIdFotoProcessoSinistro() {
        return this.idFotoProcessoSinistro;
    }

    public void setIdFotoProcessoSinistro(Long idFotoProcessoSinistro) {
        this.idFotoProcessoSinistro = idFotoProcessoSinistro;
    }

    public String getDsFoto() {
        return this.dsFoto;
    }

    public void setDsFoto(String dsFoto) {
        this.dsFoto = dsFoto;
    }

    public com.mercurio.lms.seguros.model.ProcessoSinistro getProcessoSinistro() {
        return this.processoSinistro;
    }

	public void setProcessoSinistro(
			com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro) {
        this.processoSinistro = processoSinistro;
    }

    public com.mercurio.lms.configuracoes.model.Foto getFoto() {
        return this.foto;
    }

    public void setFoto(com.mercurio.lms.configuracoes.model.Foto foto) {
        this.foto = foto;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFotoProcessoSinistro",
				getIdFotoProcessoSinistro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FotoProcessoSinistro))
			return false;
        FotoProcessoSinistro castOther = (FotoProcessoSinistro) other;
		return new EqualsBuilder().append(this.getIdFotoProcessoSinistro(),
				castOther.getIdFotoProcessoSinistro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFotoProcessoSinistro())
            .toHashCode();
    }

}
