package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class TrechoCorporativo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTrechoCorporativo;

    /** nullable persistent field */
    private Integer codigo;
    
    /** nullable persistent field */
    private TimeOfDay hrSaida;

    /** nullable persistent field */
    private Integer qtdHorasPrev;
    
    /** persistent field */
    private BigDecimal vlFaixa1;

    /** persistent field */
    private BigDecimal vlFaixa2;

    /** persistent field */
    private BigDecimal vlFaixa3;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;

	public Long getIdTrechoCorporativo() {
		return idTrechoCorporativo;
	}

	public void setIdTrechoCorporativo(Long idTrechoCorporativo) {
		this.idTrechoCorporativo = idTrechoCorporativo;
	}

    public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TimeOfDay getHrSaida() {
		return hrSaida;
	}

	public void setHrSaida(TimeOfDay hrSaida) {
		this.hrSaida = hrSaida;
	}

	public Integer getQtdHorasPrev() {
		return qtdHorasPrev;
	}

	public void setQtdHorasPrev(Integer qtdHorasPrev) {
		this.qtdHorasPrev = qtdHorasPrev;
	}

	public BigDecimal getVlFaixa1() {
		return vlFaixa1;
	}

	public void setVlFaixa1(BigDecimal vlFaixa1) {
		this.vlFaixa1 = vlFaixa1;
	}

	public BigDecimal getVlFaixa2() {
		return vlFaixa2;
	}

	public void setVlFaixa2(BigDecimal vlFaixa2) {
		this.vlFaixa2 = vlFaixa2;
	}

	public BigDecimal getVlFaixa3() {
		return vlFaixa3;
	}

	public void setVlFaixa3(BigDecimal vlFaixa3) {
		this.vlFaixa3 = vlFaixa3;
	}

	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialDestino() {
		return filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialOrigem() {
		return filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idTrechoCorporativo",
				getIdTrechoCorporativo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TrechoCorporativo))
			return false;
        TrechoCorporativo castOther = (TrechoCorporativo) other;
		return new EqualsBuilder().append(this.getIdTrechoCorporativo(),
				castOther.getIdTrechoCorporativo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTrechoCorporativo())
            .toHashCode();
    }
}