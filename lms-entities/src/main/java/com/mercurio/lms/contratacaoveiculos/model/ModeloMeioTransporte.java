package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ModeloMeioTransporte implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idModeloMeioTransporte;

    /** persistent field */
    private String dsModeloMeioTransporte;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private MarcaMeioTransporte marcaMeioTransporte;

    /** persistent field */
    private List meioTransportes;

    /** persistent field */
    private List meioTransporteTerceiros;
    
    private DomainValue tpMeioTransporte;

    public DomainValue getTpMeioTransporte() {
		return tpMeioTransporte;
	}

	public void setTpMeioTransporte(DomainValue tpMeioTransporte) {
		this.tpMeioTransporte = tpMeioTransporte;
	}

    public Long getIdModeloMeioTransporte() {
        return this.idModeloMeioTransporte;
    }

    public void setIdModeloMeioTransporte(Long idModeloMeioTransporte) {
        this.idModeloMeioTransporte = idModeloMeioTransporte;
    }

    public String getDsModeloMeioTransporte() {
        return this.dsModeloMeioTransporte;
    }

    public void setDsModeloMeioTransporte(String dsModeloMeioTransporte) {
        this.dsModeloMeioTransporte = dsModeloMeioTransporte;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

    public void setTipoMeioTransporte(TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    public MarcaMeioTransporte getMarcaMeioTransporte() {
        return this.marcaMeioTransporte;
    }

    public void setMarcaMeioTransporte(MarcaMeioTransporte marcaMeioTransporte) {
        this.marcaMeioTransporte = marcaMeioTransporte;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTransporte.class)     
    public List getMeioTransportes() {
        return this.meioTransportes;
    }

    public void setMeioTransportes(List meioTransportes) {
        this.meioTransportes = meioTransportes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.MeioTransporteTerceiro.class)     
    public List getMeioTransporteTerceiros() {
        return this.meioTransporteTerceiros;
    }

    public void setMeioTransporteTerceiros(List meioTransporteTerceiros) {
        this.meioTransporteTerceiros = meioTransporteTerceiros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idModeloMeioTransporte",
				getIdModeloMeioTransporte()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ModeloMeioTransporte))
			return false;
        ModeloMeioTransporte castOther = (ModeloMeioTransporte) other;
		return new EqualsBuilder().append(this.getIdModeloMeioTransporte(),
				castOther.getIdModeloMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdModeloMeioTransporte())
            .toHashCode();
    }

}
