package com.mercurio.lms.coleta.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class LocalidadeEspecial implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLocalidadeEspecial;

    /** persistent field */
    private VarcharI18n dsLocalidade;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;
    
    /** persistent field */
    private List detalheColetas;

    public Long getIdLocalidadeEspecial() {
        return this.idLocalidadeEspecial;
    }

    public void setIdLocalidadeEspecial(Long idLocalidadeEspecial) {
        this.idLocalidadeEspecial = idLocalidadeEspecial;
    }

    public VarcharI18n getDsLocalidade() {
		return dsLocalidade;
    }

	public void setDsLocalidade(VarcharI18n dsLocalidade) {
        this.dsLocalidade = dsLocalidade;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
		return this.unidadeFederativa;
	}

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.DetalheColeta.class)     
    public List getDetalheColetas() {
        return this.detalheColetas;
    }

    public void setDetalheColetas(List detalheColetas) {
        this.detalheColetas = detalheColetas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLocalidadeEspecial",
				getIdLocalidadeEspecial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LocalidadeEspecial))
			return false;
        LocalidadeEspecial castOther = (LocalidadeEspecial) other;
		return new EqualsBuilder().append(this.getIdLocalidadeEspecial(),
				castOther.getIdLocalidadeEspecial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLocalidadeEspecial())
            .toHashCode();
    }

}
