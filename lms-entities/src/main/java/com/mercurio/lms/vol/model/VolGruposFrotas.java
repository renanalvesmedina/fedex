package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolGruposFrotas implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idGrupoFrota;

    /** persistent field */
    private String dsNome;

    /** persistent field */
    private Filial filial;

    /** persistent field */
    private List volGrfsVeiculos;

    /** persistent field */
    private List volGrfrFuncionarios;

    public Long getIdGrupoFrota() {
        return this.idGrupoFrota;
    }

    public void setIdGrupoFrota(Long idGrupoFrota) {
        this.idGrupoFrota = idGrupoFrota;
    }

    public String getDsNome() {
        return this.dsNome;
    }

    public void setDsNome(String dsNome) {
        this.dsNome = dsNome;
    }

    public Filial getFilial() {
        return this.filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolGrfsVeiculos.class)     
    public List getVolGrfsVeiculos() {
        return this.volGrfsVeiculos;
    }

    public void setVolGrfsVeiculos(List volGrfsVeiculos) {
        this.volGrfsVeiculos = volGrfsVeiculos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolGrfrFuncionarios.class)     
    public List getVolGrfrFuncionarios() {
        return this.volGrfrFuncionarios;
    }

    public void setVolGrfrFuncionarios(List volGrfrFuncionarios) {
        this.volGrfrFuncionarios = volGrfrFuncionarios;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idGrupoFrota",
				getIdGrupoFrota()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolGruposFrotas))
			return false;
        VolGruposFrotas castOther = (VolGruposFrotas) other;
		return new EqualsBuilder().append(this.getIdGrupoFrota(),
				castOther.getIdGrupoFrota()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdGrupoFrota()).toHashCode();
    }

}
