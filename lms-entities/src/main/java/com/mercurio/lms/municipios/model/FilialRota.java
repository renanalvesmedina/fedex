package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class FilialRota implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFilialRota;

    /** persistent field */
    private Byte nrOrdem;
    
    private Boolean blDestinoRota;
    
    private Boolean blOrigemRota;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Rota rota;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List trechoRotaIdaVoltasByIdFilialRotaOrigem;

    /** persistent field */
    private List trechoRotaIdaVoltasByIdFilialRotaDetino;

    public Long getIdFilialRota() {
        return this.idFilialRota;
    }

    public void setIdFilialRota(Long idFilialRota) {
        this.idFilialRota = idFilialRota;
    }

    public Byte getNrOrdem() {
        return this.nrOrdem;
    }

    public void setNrOrdem(Byte nrOrdem) {
        this.nrOrdem = nrOrdem;
    }

    public com.mercurio.lms.municipios.model.Rota getRota() {
        return this.rota;
    }

    public void setRota(com.mercurio.lms.municipios.model.Rota rota) {
        this.rota = rota;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.TrechoRotaIdaVolta.class)     
    public List getTrechoRotaIdaVoltasByIdFilialRotaOrigem() {
        return this.trechoRotaIdaVoltasByIdFilialRotaOrigem;
    }

	public void setTrechoRotaIdaVoltasByIdFilialRotaOrigem(
			List trechoRotaIdaVoltasByIdFilialRotaOrigem) {
        this.trechoRotaIdaVoltasByIdFilialRotaOrigem = trechoRotaIdaVoltasByIdFilialRotaOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.TrechoRotaIdaVolta.class)     
    public List getTrechoRotaIdaVoltasByIdFilialRotaDetino() {
        return this.trechoRotaIdaVoltasByIdFilialRotaDetino;
    }

	public void setTrechoRotaIdaVoltasByIdFilialRotaDetino(
			List trechoRotaIdaVoltasByIdFilialRotaDetino) {
        this.trechoRotaIdaVoltasByIdFilialRotaDetino = trechoRotaIdaVoltasByIdFilialRotaDetino;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFilialRota",
				getIdFilialRota()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FilialRota))
			return false;
        FilialRota castOther = (FilialRota) other;
		return new EqualsBuilder().append(this.getIdFilialRota(),
				castOther.getIdFilialRota()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFilialRota()).toHashCode();
    }

	public Boolean getBlDestinoRota() {
		return blDestinoRota;
	}

	public void setBlDestinoRota(Boolean blDestinoRota) {
		this.blDestinoRota = blDestinoRota;
	}

	public Boolean getBlOrigemRota() {
		return blOrigemRota;
	}

	public void setBlOrigemRota(Boolean blOrigemRota) {
		this.blOrigemRota = blOrigemRota;
	}

}
