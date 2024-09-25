package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class MercadoriaLotePendencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMercadoriaLotePendencia;

    /** nullable persistent field */
    private BigDecimal vlPago;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz mercadoriaPendenciaMz;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.LotePendencia lotePendencia;

    public Long getIdMercadoriaLotePendencia() {
        return this.idMercadoriaLotePendencia;
    }

    public void setIdMercadoriaLotePendencia(Long idMercadoriaLotePendencia) {
        this.idMercadoriaLotePendencia = idMercadoriaLotePendencia;
    }

    public BigDecimal getVlPago() {
        return this.vlPago;
    }

    public void setVlPago(BigDecimal vlPago) {
        this.vlPago = vlPago;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz getMercadoriaPendenciaMz() {
        return this.mercadoriaPendenciaMz;
    }

	public void setMercadoriaPendenciaMz(
			com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz mercadoriaPendenciaMz) {
        this.mercadoriaPendenciaMz = mercadoriaPendenciaMz;
    }

    public com.mercurio.lms.pendencia.model.LotePendencia getLotePendencia() {
        return this.lotePendencia;
    }

	public void setLotePendencia(
			com.mercurio.lms.pendencia.model.LotePendencia lotePendencia) {
        this.lotePendencia = lotePendencia;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMercadoriaLotePendencia",
				getIdMercadoriaLotePendencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MercadoriaLotePendencia))
			return false;
        MercadoriaLotePendencia castOther = (MercadoriaLotePendencia) other;
		return new EqualsBuilder().append(this.getIdMercadoriaLotePendencia(),
				castOther.getIdMercadoriaLotePendencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMercadoriaLotePendencia())
            .toHashCode();
    }

}
