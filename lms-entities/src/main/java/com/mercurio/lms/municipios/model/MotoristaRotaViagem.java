package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotoristaRotaViagem implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotoristaRotaViagem;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Motorista motorista;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaViagem rotaViagem;

    public Long getIdMotoristaRotaViagem() {
        return this.idMotoristaRotaViagem;
    }

    public void setIdMotoristaRotaViagem(Long idMotoristaRotaViagem) {
        this.idMotoristaRotaViagem = idMotoristaRotaViagem;
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

    public com.mercurio.lms.contratacaoveiculos.model.Motorista getMotorista() {
        return this.motorista;
    }

	public void setMotorista(
			com.mercurio.lms.contratacaoveiculos.model.Motorista motorista) {
        this.motorista = motorista;
    }

    public com.mercurio.lms.municipios.model.RotaViagem getRotaViagem() {
        return this.rotaViagem;
    }

	public void setRotaViagem(
			com.mercurio.lms.municipios.model.RotaViagem rotaViagem) {
        this.rotaViagem = rotaViagem;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotoristaRotaViagem",
				getIdMotoristaRotaViagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotoristaRotaViagem))
			return false;
        MotoristaRotaViagem castOther = (MotoristaRotaViagem) other;
		return new EqualsBuilder().append(this.getIdMotoristaRotaViagem(),
				castOther.getIdMotoristaRotaViagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotoristaRotaViagem())
            .toHashCode();
    }

}
