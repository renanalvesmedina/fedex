package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolTiposEventos implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoEvento;

    /** persistent field */
    private String dsNome;

    /** persistent field */
    private DomainValue tpTipoEvento;

    /** persistent field */
    private Long nmCodigo;

    /** persistent field */
    private List volEventosCelulars;

    public Long getIdTipoEvento() {
        return this.idTipoEvento;
    }

    public void setIdTipoEvento(Long idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public String getDsNome() {
        return this.dsNome;
    }

    public void setDsNome(String dsNome) {
        this.dsNome = dsNome;
    }

    public DomainValue getTpTipoEvento() {
        return this.tpTipoEvento;
    }

    public void setTpTipoEvento(DomainValue tpTipoEvento) {
        this.tpTipoEvento = tpTipoEvento;
    }

    public Long getNmCodigo() {
        return this.nmCodigo;
    }

    public void setNmCodigo(Long nmCodigo) {
        this.nmCodigo = nmCodigo;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolEventosCelular.class)     
    public List getVolEventosCelulars() {
        return this.volEventosCelulars;
    }

    public void setVolEventosCelulars(List volEventosCelulars) {
        this.volEventosCelulars = volEventosCelulars;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoEvento",
				getIdTipoEvento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolTiposEventos))
			return false;
        VolTiposEventos castOther = (VolTiposEventos) other;
		return new EqualsBuilder().append(this.getIdTipoEvento(),
				castOther.getIdTipoEvento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoEvento()).toHashCode();
    }

}
