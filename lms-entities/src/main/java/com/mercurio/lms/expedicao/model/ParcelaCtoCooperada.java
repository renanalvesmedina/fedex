package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParcelaCtoCooperada implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idParcelaCtoCooperada;

    /** persistent field */
    private BigDecimal vlParcela;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.ParcelaPreco parcelaPreco;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.CtoCtoCooperada ctoCtoCooperada;

    public Long getIdParcelaCtoCooperada() {
        return this.idParcelaCtoCooperada;
    }

    public void setIdParcelaCtoCooperada(Long idParcelaCtoCooperada) {
        this.idParcelaCtoCooperada = idParcelaCtoCooperada;
    }

    public BigDecimal getVlParcela() {
        return this.vlParcela;
    }

    public void setVlParcela(BigDecimal vlParcela) {
        this.vlParcela = vlParcela;
    }

    public com.mercurio.lms.tabelaprecos.model.ParcelaPreco getParcelaPreco() {
        return this.parcelaPreco;
    }

	public void setParcelaPreco(
			com.mercurio.lms.tabelaprecos.model.ParcelaPreco parcelaPreco) {
        this.parcelaPreco = parcelaPreco;
    }

    public com.mercurio.lms.expedicao.model.CtoCtoCooperada getCtoCtoCooperada() {
        return this.ctoCtoCooperada;
    }

	public void setCtoCtoCooperada(
			com.mercurio.lms.expedicao.model.CtoCtoCooperada ctoCtoCooperada) {
        this.ctoCtoCooperada = ctoCtoCooperada;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idParcelaCtoCooperada",
				getIdParcelaCtoCooperada()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParcelaCtoCooperada))
			return false;
        ParcelaCtoCooperada castOther = (ParcelaCtoCooperada) other;
		return new EqualsBuilder().append(this.getIdParcelaCtoCooperada(),
				castOther.getIdParcelaCtoCooperada()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParcelaCtoCooperada())
            .toHashCode();
    }

}
