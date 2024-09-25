package com.mercurio.lms.portaria.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class MeioTransporteTerceiro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMeioTransporteTerceiro;

    /** persistent field */
    private String nrIdentificao;

    /** nullable persistent field */
    private Short nrAno;

    /** nullable persistent field */
    private String nmTransportadora;
    
    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte modeloMeioTransporte;

    /** persistent field */
    private List controleEntSaidaTerceiros;

    public Long getIdMeioTransporteTerceiro() {
        return this.idMeioTransporteTerceiro;
    }

    public void setIdMeioTransporteTerceiro(Long idMeioTransporteTerceiro) {
        this.idMeioTransporteTerceiro = idMeioTransporteTerceiro;
    }

    public String getNrIdentificao() {
        return this.nrIdentificao;
    }

    public void setNrIdentificao(String nrIdentificao) {
        this.nrIdentificao = nrIdentificao;
    }

    public Short getNrAno() {
        return this.nrAno;
    }

    public void setNrAno(Short nrAno) {
        this.nrAno = nrAno;
    }

    public String getNmTransportadora() {
        return this.nmTransportadora;
    }

    public void setNmTransportadora(String nmTransportadora) {
        this.nmTransportadora = nmTransportadora;
    }

    public com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte getModeloMeioTransporte() {
        return this.modeloMeioTransporte;
    }

	public void setModeloMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte modeloMeioTransporte) {
        this.modeloMeioTransporte = modeloMeioTransporte;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.ControleEntSaidaTerceiro.class)     
    public List getControleEntSaidaTerceiros() {
        return this.controleEntSaidaTerceiros;
    }

    public void setControleEntSaidaTerceiros(List controleEntSaidaTerceiros) {
        this.controleEntSaidaTerceiros = controleEntSaidaTerceiros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMeioTransporteTerceiro",
				getIdMeioTransporteTerceiro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTransporteTerceiro))
			return false;
        MeioTransporteTerceiro castOther = (MeioTransporteTerceiro) other;
		return new EqualsBuilder().append(this.getIdMeioTransporteTerceiro(),
				castOther.getIdMeioTransporteTerceiro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTransporteTerceiro())
            .toHashCode();
    }

}
