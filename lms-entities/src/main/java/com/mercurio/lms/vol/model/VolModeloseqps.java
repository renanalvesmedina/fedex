package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolModeloseqps implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idModeloeqp;

    /** persistent field */
    private Boolean blHomologado;

    /** persistent field */
    private String dsNome;

    /** persistent field */
    private VolFabricante volFabricante;

    /** persistent field */
    private VolTiposEqpto volTiposEqpto;
    
    /** nullable persistent field */
    private String obHistorico;

    /** persistent field */
    private List volEquipamentos;

    public Long getIdModeloeqp() {
        return this.idModeloeqp;
    }

    public void setIdModeloeqp(Long idModeloeqp) {
        this.idModeloeqp = idModeloeqp;
    }

    public Boolean getBlHomologado() {
        return this.blHomologado;
    }

    public void setBlHomologado(Boolean blHomologado) {
        this.blHomologado = blHomologado;
    }

    public String getDsNome() {
        return this.dsNome;
    }

    public void setDsNome(String dsNome) {
        this.dsNome = dsNome;
    }

    public VolFabricante getVolFabricante() {
        return this.volFabricante;
    }

    public void setVolFabricante(VolFabricante volFabricante) {
        this.volFabricante = volFabricante;
    }

    public VolTiposEqpto getVolTiposEqpto() {
        return this.volTiposEqpto;
    }

    public void setVolTiposEqpto(VolTiposEqpto volTiposEqpto) {
        this.volTiposEqpto = volTiposEqpto;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolEquipamentos.class)     
    public List getVolEquipamentos() {
        return this.volEquipamentos;
    }

    public void setVolEquipamentos(List volEquipamentos) {
        this.volEquipamentos = volEquipamentos;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idModeloeqp", getIdModeloeqp()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolModeloseqps))
			return false;
        VolModeloseqps castOther = (VolModeloseqps) other;
		return new EqualsBuilder().append(this.getIdModeloeqp(),
				castOther.getIdModeloeqp()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdModeloeqp()).toHashCode();
    }

	public String getObHistorico() {
		return obHistorico;
	}

	public void setObHistorico(String obHistorico) {
		this.obHistorico = obHistorico;
	}

}
