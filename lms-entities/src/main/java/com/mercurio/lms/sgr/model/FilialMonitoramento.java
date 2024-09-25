package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

public class FilialMonitoramento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idFilialMonitoramento;
	private DomainValue tpTipoOperacao;
	private Filial filialByIdFilialMonitorada;
	private Filial filialByIdFilialResponsavel;

	public Long getIdFilialMonitoramento() {
		return idFilialMonitoramento;
	}

	public void setIdFilialMonitoramento(Long idFilialMonitoramento) {
		this.idFilialMonitoramento = idFilialMonitoramento;
	}

	public DomainValue getTpTipoOperacao() {
		return tpTipoOperacao;
	}

	public void setTpTipoOperacao(DomainValue tpTipoOperacao) {
		this.tpTipoOperacao = tpTipoOperacao;
	}

	public Filial getFilialByIdFilialMonitorada() {
		return filialByIdFilialMonitorada;
	}

	public void setFilialByIdFilialMonitorada(Filial filialByIdFilialMonitorada) {
		this.filialByIdFilialMonitorada = filialByIdFilialMonitorada;
	}

	public Filial getFilialByIdFilialResponsavel() {
		return filialByIdFilialResponsavel;
	}

	public void setFilialByIdFilialResponsavel(Filial filialByIdFilialResponsavel) {
		this.filialByIdFilialResponsavel = filialByIdFilialResponsavel;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idFilialMonitoramento)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof FilialMonitoramento)) {
			return false;
		}
		FilialMonitoramento cast = (FilialMonitoramento) other;
		return new EqualsBuilder()
				.append(idFilialMonitoramento, cast.idFilialMonitoramento)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idFilialMonitoramento)
				.toHashCode();
	}

}
