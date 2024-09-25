package com.mercurio.lms.vol.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolRetiradasEqptos implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRetiradaEqp;

    /** persistent field */
    private DateTime dhRetirada;
  
    /** nullable persistent field */
    private DateTime dhDevolucao;

    /** persistent field */
    private VolEquipamentos volEquipamento;
    
    /** persistent field */
    private MeioTransporte meioTransporte;

    private Motorista motorista;
    
    public Long getIdRetiradaEqp() {
        return this.idRetiradaEqp;
    }

    public void setIdRetiradaEqp(Long idRetiradaEqp) {
        this.idRetiradaEqp = idRetiradaEqp;
    }

    public DateTime getDhRetirada() {
        return this.dhRetirada;
    }

    public void setDhRetirada(DateTime dhRetirada) {
        this.dhRetirada = dhRetirada;
    }

    public DateTime getDhDevolucao() {
        return this.dhDevolucao;
    }

    public void setDhDevolucao(DateTime dhDevolucao) {
        this.dhDevolucao = dhDevolucao;
    }

    public VolEquipamentos getVolEquipamento() {
        return this.volEquipamento;
    }

    public void setVolEquipamento(VolEquipamentos volEquipamento) {
        this.volEquipamento = volEquipamento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRetiradaEqp",
				getIdRetiradaEqp()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolRetiradasEqptos))
			return false;
        VolRetiradasEqptos castOther = (VolRetiradasEqptos) other;
		return new EqualsBuilder().append(this.getIdRetiradaEqp(),
				castOther.getIdRetiradaEqp()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRetiradaEqp()).toHashCode();
    }

	public MeioTransporte getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public Motorista getMotorista() {
		return motorista;
}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

}
