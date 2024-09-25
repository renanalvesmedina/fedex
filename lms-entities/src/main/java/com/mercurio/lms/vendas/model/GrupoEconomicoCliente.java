package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name = "GRUPO_ECONOMICO_CLIENTE")
@SequenceGenerator(name = "GRUPO_ECONOMICO_CLIENTE_SEQ", sequenceName = "GRUPO_ECONOMICO_CLIENTE_SQ", allocationSize = 1)
public class GrupoEconomicoCliente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRUPO_ECONOMICO_CLIENTE_SEQ")
	@Column(name = "ID_GRUPO_ECONOMICO_CLIENTE", nullable = false)
	private Long idGrupoEconomicoCliente;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPO_ECONOMICO", nullable = false)
	private GrupoEconomico grupoEconomico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE_GRUPO",  nullable = false)
	private Cliente cliente;
	
	public GrupoEconomicoCliente() {
	}
	
	public GrupoEconomicoCliente(GrupoEconomico grupoEconomico, Cliente cliente) {
		this.grupoEconomico = grupoEconomico;
		this.cliente = cliente;
	}

	public Long getIdGrupoEconomicoCliente() {
		return idGrupoEconomicoCliente;
	}

	public void setIdGrupoEconomicoCliente(Long idGrupoEconomicoCliente) {
		this.idGrupoEconomicoCliente = idGrupoEconomicoCliente;
	}

	public GrupoEconomico getGrupoEconomico() {
		return grupoEconomico;
	}

	public void setGrupoEconomico(GrupoEconomico grupoEconomico) {
		this.grupoEconomico = grupoEconomico;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof GrupoEconomicoCliente))
			return false;
		GrupoEconomicoCliente castOther = (GrupoEconomicoCliente) other;
		return new EqualsBuilder().append(this.getIdGrupoEconomicoCliente(), castOther.getIdGrupoEconomicoCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdGrupoEconomicoCliente()).toHashCode();
    }
}
