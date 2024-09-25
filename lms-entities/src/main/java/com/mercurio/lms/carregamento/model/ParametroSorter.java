package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.municipios.model.Filial;

public class ParametroSorter implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idParametroSorter;
	private Filial filial;
	private String nmAgendamento;
	private String nmAereo;
	
	public Long getIdParametroSorter() {
		return idParametroSorter;
	}

	public void setIdParametroSorter(Long idParametroSorter) {
		this.idParametroSorter = idParametroSorter;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public String getNmAgendamento() {
		return nmAgendamento;
	}

	public void setNmAgendamento(String nmAgendamento) {
		this.nmAgendamento = nmAgendamento;
	}

	public String getNmAereo() {
		return nmAereo;
	}

	public void setNmAereo(String nmAereo) {
		this.nmAereo = nmAereo;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idParametroSorter",
				getIdParametroSorter()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParametroSorter))
			return false;
		ParametroSorter castOther = (ParametroSorter) other;
		return new EqualsBuilder().append(this.getIdParametroSorter(),
				castOther.getIdParametroSorter()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdParametroSorter())
			.toHashCode();
	}
}
