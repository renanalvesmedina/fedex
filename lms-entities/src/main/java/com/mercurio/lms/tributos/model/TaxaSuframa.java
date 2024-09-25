package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TaxaSuframa implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTaxaSuframa;

    /** persistent field */
    private BigDecimal vlMercadoriaInicial;

    /** persistent field */
    private BigDecimal vlMercadoriaFinal;

    /** persistent field */
    private BigDecimal vlLiquido;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private DomainValue tpIndicadorCalculo;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    public Long getIdTaxaSuframa() {
        return this.idTaxaSuframa;
    }

    public void setIdTaxaSuframa(Long idTaxaSuframa) {
        this.idTaxaSuframa = idTaxaSuframa;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTaxaSuframa",
				getIdTaxaSuframa()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TaxaSuframa))
			return false;
        TaxaSuframa castOther = (TaxaSuframa) other;
		return new EqualsBuilder().append(this.getIdTaxaSuframa(),
				castOther.getIdTaxaSuframa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTaxaSuframa()).toHashCode();
    }

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public DomainValue getTpIndicadorCalculo() {
		return tpIndicadorCalculo;
	}

	public void setTpIndicadorCalculo(DomainValue tpIndicadorCalculo) {
		this.tpIndicadorCalculo = tpIndicadorCalculo;
	}

	public BigDecimal getVlLiquido() {
		return vlLiquido;
	}

	public void setVlLiquido(BigDecimal vlLiquido) {
		this.vlLiquido = vlLiquido;
	}

	public BigDecimal getVlMercadoriaFinal() {
		return vlMercadoriaFinal;
	}

	public void setVlMercadoriaFinal(BigDecimal vlMercadoriaFinal) {
		this.vlMercadoriaFinal = vlMercadoriaFinal;
	}

	public BigDecimal getVlMercadoriaInicial() {
		return vlMercadoriaInicial;
	}

	public void setVlMercadoriaInicial(BigDecimal vlMercadoriaInicial) {
		this.vlMercadoriaInicial = vlMercadoriaInicial;
	}

}
