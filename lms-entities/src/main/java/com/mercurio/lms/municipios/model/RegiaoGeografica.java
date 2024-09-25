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
public class RegiaoGeografica implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRegiaoGeografica;

    /** persistent field */
    private VarcharI18n dsRegiaoGeografica;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List unidadeFederativas;

    public Long getIdRegiaoGeografica() {
        return this.idRegiaoGeografica;
    }

    public void setIdRegiaoGeografica(Long idRegiaoGeografica) {
        this.idRegiaoGeografica = idRegiaoGeografica;
    }

    public VarcharI18n getDsRegiaoGeografica() {
		return dsRegiaoGeografica;
    }

	public void setDsRegiaoGeografica(VarcharI18n dsRegiaoGeografica) {
        this.dsRegiaoGeografica = dsRegiaoGeografica;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.UnidadeFederativa.class)     
    public List getUnidadeFederativas() {
        return this.unidadeFederativas;
    }

    public void setUnidadeFederativas(List unidadeFederativas) {
        this.unidadeFederativas = unidadeFederativas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRegiaoGeografica",
				getIdRegiaoGeografica()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RegiaoGeografica))
			return false;
        RegiaoGeografica castOther = (RegiaoGeografica) other;
		return new EqualsBuilder().append(this.getIdRegiaoGeografica(),
				castOther.getIdRegiaoGeografica()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRegiaoGeografica())
            .toHashCode();
    }

}
