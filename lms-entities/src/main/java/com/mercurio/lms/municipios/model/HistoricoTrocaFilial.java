package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class HistoricoTrocaFilial implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idHistoricoTrocaFilial;

    /** persistent field */
    private YearMonthDay dtInclusao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MunicipioFilial municipioFilialByIdMunicipioFilial;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MunicipioFilial municipioFilialByIdMunicipioFilialTroca;

    /** persistent field */
    private List histTrocaFilialClientes;

    public Long getIdHistoricoTrocaFilial() {
        return this.idHistoricoTrocaFilial;
    }

    public void setIdHistoricoTrocaFilial(Long idHistoricoTrocaFilial) {
        this.idHistoricoTrocaFilial = idHistoricoTrocaFilial;
    }

    public YearMonthDay getDtInclusao() {
        return this.dtInclusao;
    }

    public void setDtInclusao(YearMonthDay dtInclusao) {
        this.dtInclusao = dtInclusao;
    }

    public com.mercurio.lms.municipios.model.MunicipioFilial getMunicipioFilialByIdMunicipioFilial() {
        return this.municipioFilialByIdMunicipioFilial;
    }

	public void setMunicipioFilialByIdMunicipioFilial(
			com.mercurio.lms.municipios.model.MunicipioFilial municipioFilialByIdMunicipioFilial) {
        this.municipioFilialByIdMunicipioFilial = municipioFilialByIdMunicipioFilial;
    }

    public com.mercurio.lms.municipios.model.MunicipioFilial getMunicipioFilialByIdMunicipioFilialTroca() {
        return this.municipioFilialByIdMunicipioFilialTroca;
    }

	public void setMunicipioFilialByIdMunicipioFilialTroca(
			com.mercurio.lms.municipios.model.MunicipioFilial municipioFilialByIdMunicipioFilialTroca) {
        this.municipioFilialByIdMunicipioFilialTroca = municipioFilialByIdMunicipioFilialTroca;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.HistTrocaFilialCliente.class)     
    public List getHistTrocaFilialClientes() {
        return this.histTrocaFilialClientes;
    }

    public void setHistTrocaFilialClientes(List histTrocaFilialClientes) {
        this.histTrocaFilialClientes = histTrocaFilialClientes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idHistoricoTrocaFilial",
				getIdHistoricoTrocaFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HistoricoTrocaFilial))
			return false;
        HistoricoTrocaFilial castOther = (HistoricoTrocaFilial) other;
		return new EqualsBuilder().append(this.getIdHistoricoTrocaFilial(),
				castOther.getIdHistoricoTrocaFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdHistoricoTrocaFilial())
            .toHashCode();
    }

}
