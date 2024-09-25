package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ValorCampoComplementar implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idValorCampoComplementar;

    /** persistent field */
    private String vlValor;

    /** persistent field */
    private com.mercurio.lms.vendas.model.CampoComplementar campoComplementar;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    public Long getIdValorCampoComplementar() {
        return this.idValorCampoComplementar;
    }

    public void setIdValorCampoComplementar(Long idValorCampoComplementar) {
        this.idValorCampoComplementar = idValorCampoComplementar;
    }

    public String getVlValor() {
        return this.vlValor;
    }

    public void setVlValor(String vlValor) {
        this.vlValor = vlValor;
    }

    public com.mercurio.lms.vendas.model.CampoComplementar getCampoComplementar() {
        return this.campoComplementar;
    }

	public void setCampoComplementar(
			com.mercurio.lms.vendas.model.CampoComplementar campoComplementar) {
        this.campoComplementar = campoComplementar;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idValorCampoComplementar",
				getIdValorCampoComplementar()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorCampoComplementar))
			return false;
        ValorCampoComplementar castOther = (ValorCampoComplementar) other;
		return new EqualsBuilder().append(this.getIdValorCampoComplementar(),
				castOther.getIdValorCampoComplementar()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorCampoComplementar())
            .toHashCode();
    }

}
