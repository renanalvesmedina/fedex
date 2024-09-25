package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class AliquotaIva implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAliquotaIva;

    /** persistent field */
    private BigDecimal pcAliquota;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;

    public Long getIdAliquotaIva() {
        return this.idAliquotaIva;
    }

    public void setIdAliquotaIva(Long idAliquotaIva) {
        this.idAliquotaIva = idAliquotaIva;
    }

    public BigDecimal getPcAliquota() {
        return this.pcAliquota;
    }

    public void setPcAliquota(BigDecimal pcAliquota) {
        this.pcAliquota = pcAliquota;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtInicioVigencia) {
        this.dtVigenciaInicial = dtInicioVigencia;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtFimVigencia) {
        this.dtVigenciaFinal = dtFimVigencia;
    }

    public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAliquotaIva",
				getIdAliquotaIva()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaIva))
			return false;
        AliquotaIva castOther = (AliquotaIva) other;
		return new EqualsBuilder().append(this.getIdAliquotaIva(),
				castOther.getIdAliquotaIva()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaIva()).toHashCode();
    }

}
