package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class MunicipioFilialCliOrigem implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMunicipioFilialCliOrigem;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial;

    public Long getIdMunicipioFilialCliOrigem() {
        return this.idMunicipioFilialCliOrigem;
    }

    public void setIdMunicipioFilialCliOrigem(Long idMunicipioFilialCliOrigem) {
        this.idMunicipioFilialCliOrigem = idMunicipioFilialCliOrigem;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.municipios.model.MunicipioFilial getMunicipioFilial() {
        return this.municipioFilial;
    }

	public void setMunicipioFilial(
			com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial) {
        this.municipioFilial = municipioFilial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMunicipioFilialCliOrigem",
				getIdMunicipioFilialCliOrigem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MunicipioFilialCliOrigem))
			return false;
        MunicipioFilialCliOrigem castOther = (MunicipioFilialCliOrigem) other;
		return new EqualsBuilder().append(this.getIdMunicipioFilialCliOrigem(),
				castOther.getIdMunicipioFilialCliOrigem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipioFilialCliOrigem())
            .toHashCode();
    }

}
