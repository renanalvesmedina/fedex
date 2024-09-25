package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class TarifaColeta implements Serializable,Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTarifaColeta;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.TarifaPreco tarifaPreco;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilial;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialColeta;
    
    private YearMonthDay dtVigenciaInicial;
    
    private YearMonthDay dtVigenciaFinal;

    public Long getIdTarifaColeta() {
        return this.idTarifaColeta;
    }

    public void setIdTarifaColeta(Long idTarifaColeta) {
        this.idTarifaColeta = idTarifaColeta;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.tabelaprecos.model.TarifaPreco getTarifaPreco() {
        return this.tarifaPreco;
    }

	public void setTarifaPreco(
			com.mercurio.lms.tabelaprecos.model.TarifaPreco tarifaPreco) {
        this.tarifaPreco = tarifaPreco;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilial() {
        return this.filialByIdFilial;
    }

	public void setFilialByIdFilial(
			com.mercurio.lms.municipios.model.Filial filialByIdFilial) {
        this.filialByIdFilial = filialByIdFilial;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialColeta() {
        return this.filialByIdFilialColeta;
    }

	public void setFilialByIdFilialColeta(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialColeta) {
        this.filialByIdFilialColeta = filialByIdFilialColeta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTarifaColeta",
				getIdTarifaColeta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TarifaColeta))
			return false;
        TarifaColeta castOther = (TarifaColeta) other;
		return new EqualsBuilder().append(this.getIdTarifaColeta(),
				castOther.getIdTarifaColeta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTarifaColeta()).toHashCode();
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

}
