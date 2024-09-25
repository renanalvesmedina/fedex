package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DoctoServicoSeguros implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDoctoServicoSeguro;

    /** persistent field */
    private com.mercurio.lms.seguros.model.TipoSeguro tipoSeguro;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.Conhecimento conhecimento;

    public Long getIdDoctoServicoSeguro() {
        return this.idDoctoServicoSeguro;
    }

    public void setIdDoctoServicoSeguro(Long idDoctoServicoSeguro) {
        this.idDoctoServicoSeguro = idDoctoServicoSeguro;
    }

    public com.mercurio.lms.seguros.model.TipoSeguro getTipoSeguro() {
        return this.tipoSeguro;
    }

	public void setTipoSeguro(
			com.mercurio.lms.seguros.model.TipoSeguro tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public com.mercurio.lms.expedicao.model.Conhecimento getConhecimento() {
        return this.conhecimento;
    }

	public void setConhecimento(
			com.mercurio.lms.expedicao.model.Conhecimento conhecimento) {
        this.conhecimento = conhecimento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDoctoServicoSeguro",
				getIdDoctoServicoSeguro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DoctoServicoSeguros))
			return false;
        DoctoServicoSeguros castOther = (DoctoServicoSeguros) other;
		return new EqualsBuilder().append(this.getIdDoctoServicoSeguro(),
				castOther.getIdDoctoServicoSeguro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDoctoServicoSeguro())
            .toHashCode();
    }

}
