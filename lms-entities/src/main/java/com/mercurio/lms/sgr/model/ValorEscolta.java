package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.municipios.model.Filial;

public class ValorEscolta implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idValorEscolta;
	private BigDecimal vlEscolta;
	private ControleCarga controleCarga;
	private Moeda moeda;
	private Escolta escolta;
	private Filial filialByIdFilialOrigem;
	private Filial filialByIdFilialDestino;

	public Long getIdValorEscolta() {
		return idValorEscolta;
	}

	public void setIdValorEscolta(Long idValorEscolta) {
		this.idValorEscolta = idValorEscolta;
	}

	public BigDecimal getVlEscolta() {
		return vlEscolta;
	}

	public void setVlEscolta(BigDecimal vlEscolta) {
		this.vlEscolta = vlEscolta;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public Escolta getEscolta() {
		return escolta;
	}

	public void setEscolta(Escolta escolta) {
		this.escolta = escolta;
	}

	public Filial getFilialByIdFilialOrigem() {
		return filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	public Filial getFilialByIdFilialDestino() {
		return filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idValorEscolta)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof ValorEscolta)) {
			return false;
		}
		ValorEscolta cast = (ValorEscolta) other;
		return new EqualsBuilder()
				.append(idValorEscolta, cast.idValorEscolta)
				.isEquals();	
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idValorEscolta)
				.toHashCode();
	}

}
