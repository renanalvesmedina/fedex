package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author Hibernate CodeGenerator */
public class MotivoParada implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoParada;

    /** persistent field */
    private VarcharI18n  dsMotivoParada;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List motivoParadaPontoTrechos;

    public Long getIdMotivoParada() {
        return this.idMotivoParada;
    }

    public void setIdMotivoParada(Long idMotivoParada) {
        this.idMotivoParada = idMotivoParada;
    }

    public VarcharI18n getDsMotivoParada() {
		return dsMotivoParada;
    }

	public void setDsMotivoParada(VarcharI18n dsMotivoParada) {
        this.dsMotivoParada = dsMotivoParada;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MotivoParadaPontoTrecho.class)     
    public List getMotivoParadaPontoTrechos() {
        return this.motivoParadaPontoTrechos;
    }

    public void setMotivoParadaPontoTrechos(List motivoParadaPontoTrechos) {
        this.motivoParadaPontoTrechos = motivoParadaPontoTrechos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoParada",
				getIdMotivoParada()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoParada))
			return false;
        MotivoParada castOther = (MotivoParada) other;
		return new EqualsBuilder().append(this.getIdMotivoParada(),
				castOther.getIdMotivoParada()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoParada()).toHashCode();
    }
}
