package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class RodizioVeiculoMunicipio implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRodizioVeiculoMunicipio;

    /** persistent field */
    private Integer nrFinalPlaca;

    /** nullable persistent field */
    private TimeOfDay hrRodizioInicial;

    /** nullable persistent field */
    private TimeOfDay hrRodizioFinal;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** nullable persistent field */
    private DomainValue diaSemana;
    
    private Integer acaoVigenciaAtual;

    public Integer getAcaoVigenciaAtual() {
		return acaoVigenciaAtual;
	}

	public void setAcaoVigenciaAtual(Integer acaoVigenciaAtual) {
		this.acaoVigenciaAtual = acaoVigenciaAtual;
	}

    public Long getIdRodizioVeiculoMunicipio() {
        return this.idRodizioVeiculoMunicipio;
    }

    public void setIdRodizioVeiculoMunicipio(Long idRodizioVeiculoMunicipio) {
        this.idRodizioVeiculoMunicipio = idRodizioVeiculoMunicipio;
    }

    public Integer getNrFinalPlaca() {
        return this.nrFinalPlaca;
    }

    public void setNrFinalPlaca(Integer nrFinalPlaca) {
        this.nrFinalPlaca = nrFinalPlaca;
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

    public TimeOfDay getHrRodizioFinal() {
		return hrRodizioFinal;
	}

	public void setHrRodizioFinal(TimeOfDay hrRodizioFinal) {
		this.hrRodizioFinal = hrRodizioFinal;
	}

	public TimeOfDay getHrRodizioInicial() {
		return hrRodizioInicial;
	}

	public void setHrRodizioInicial(TimeOfDay hrRodizioInicial) {
		this.hrRodizioInicial = hrRodizioInicial;
	}

	public DomainValue getDiaSemana() {
        return this.diaSemana;
    }

    public void setDiaSemana(DomainValue diaSemana) {
        this.diaSemana = diaSemana;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRodizioVeiculoMunicipio",
				getIdRodizioVeiculoMunicipio()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RodizioVeiculoMunicipio))
			return false;
        RodizioVeiculoMunicipio castOther = (RodizioVeiculoMunicipio) other;
		return new EqualsBuilder().append(this.getIdRodizioVeiculoMunicipio(),
				castOther.getIdRodizioVeiculoMunicipio()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRodizioVeiculoMunicipio())
            .toHashCode();
    }

}
