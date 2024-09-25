package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TpCombustTpMeioTransp implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTpCombustTpMeioTransp;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private BigDecimal qtConsumo;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel tipoCombustivel;

    public Long getIdTpCombustTpMeioTransp() {
        return this.idTpCombustTpMeioTransp;
    }

    public void setIdTpCombustTpMeioTransp(Long idTpCombustTpMeioTransp) {
        this.idTpCombustTpMeioTransp = idTpCombustTpMeioTransp;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public BigDecimal getQtConsumo() {
        return this.qtConsumo;
    }

    public void setQtConsumo(BigDecimal qtConsumo) {
        this.qtConsumo = qtConsumo;
    }

    public com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

	public void setTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    public com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel getTipoCombustivel() {
        return this.tipoCombustivel;
    }

	public void setTipoCombustivel(
			com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTpCombustTpMeioTransp",
				getIdTpCombustTpMeioTransp()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TpCombustTpMeioTransp))
			return false;
        TpCombustTpMeioTransp castOther = (TpCombustTpMeioTransp) other;
		return new EqualsBuilder().append(this.getIdTpCombustTpMeioTransp(),
				castOther.getIdTpCombustTpMeioTransp()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTpCombustTpMeioTransp())
            .toHashCode();
    }

}
