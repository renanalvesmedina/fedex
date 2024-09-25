package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolTiposUso implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTiposUso;

    /** persistent field */
    private String dsNome;

    /** nullable persistent field */
    private String obDescricao;

    /** persistent field */
    private List volEquipamentos;

    public Long getIdTiposUso() {
        return this.idTiposUso;
    }

    public void setIdTiposUso(Long idTiposUso) {
        this.idTiposUso = idTiposUso;
    }

    public String getDsNome() {
        return this.dsNome;
    }

    public void setDsNome(String dsNome) {
        this.dsNome = dsNome;
    }

    public String getObDescricao() {
        return this.obDescricao;
    }

    public void setObDescricao(String obDescricao) {
        this.obDescricao = obDescricao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolEquipamentos.class)     
    public List getVolEquipamentos() {
        return this.volEquipamentos;
    }

    public void setVolEquipamentos(List volEquipamentos) {
        this.volEquipamentos = volEquipamentos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTiposUso", getIdTiposUso())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolTiposUso))
			return false;
        VolTiposUso castOther = (VolTiposUso) other;
		return new EqualsBuilder().append(this.getIdTiposUso(),
				castOther.getIdTiposUso()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTiposUso()).toHashCode();
    }

}
