package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class MarcaMeioTransporte implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMarcaMeioTransporte;

    /** persistent field */
    private String dsMarcaMeioTransporte;

    /** persistent field */
    private DomainValue tpMeioTransporte;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List modeloMeioTransportes;

    public Long getIdMarcaMeioTransporte() {
        return this.idMarcaMeioTransporte;
    }

    public void setIdMarcaMeioTransporte(Long idMarcaMeioTransporte) {
        this.idMarcaMeioTransporte = idMarcaMeioTransporte;
    }

    public String getDsMarcaMeioTransporte() {
        return this.dsMarcaMeioTransporte;
    }

    public void setDsMarcaMeioTransporte(String dsMarcaMeioTransporte) {
        this.dsMarcaMeioTransporte = dsMarcaMeioTransporte;
    }

    public DomainValue getTpMeioTransporte() {
        return this.tpMeioTransporte;
    }

    public void setTpMeioTransporte(DomainValue tpMeioTransporte) {
        this.tpMeioTransporte = tpMeioTransporte;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte.class)     
    public List getModeloMeioTransportes() {
        return this.modeloMeioTransportes;
    }

    public void setModeloMeioTransportes(List modeloMeioTransportes) {
        this.modeloMeioTransportes = modeloMeioTransportes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMarcaMeioTransporte",
				getIdMarcaMeioTransporte()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MarcaMeioTransporte))
			return false;
        MarcaMeioTransporte castOther = (MarcaMeioTransporte) other;
		return new EqualsBuilder().append(this.getIdMarcaMeioTransporte(),
				castOther.getIdMarcaMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMarcaMeioTransporte())
            .toHashCode();
    }

}
