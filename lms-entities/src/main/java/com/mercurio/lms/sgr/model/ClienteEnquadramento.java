package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.vendas.model.Cliente;

public class ClienteEnquadramento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idClienteEnquadramento;
	private Cliente cliente;
	@JsonIgnore
	private EnquadramentoRegra enquadramentoRegra;

	// LMS-7253
	private DomainValue tpIntegranteFrete;

	public Long getIdClienteEnquadramento() {
		return idClienteEnquadramento;
	}

	public void setIdClienteEnquadramento(Long idClienteEnquadramento) {
		this.idClienteEnquadramento = idClienteEnquadramento;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public EnquadramentoRegra getEnquadramentoRegra() {
		return enquadramentoRegra;
	}

	public void setEnquadramentoRegra(EnquadramentoRegra enquadramentoRegra) {
		this.enquadramentoRegra = enquadramentoRegra;
	}

	public DomainValue getTpIntegranteFrete() {
		return tpIntegranteFrete;
	}

	public void setTpIntegranteFrete(DomainValue tpIntegranteFrete) {
		this.tpIntegranteFrete = tpIntegranteFrete;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idClienteEnquadramento)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof ClienteEnquadramento)) {
			return false;
		}
		ClienteEnquadramento cast = (ClienteEnquadramento) other;
		return new EqualsBuilder()
				.append(idClienteEnquadramento, cast.idClienteEnquadramento)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idClienteEnquadramento)
				.toHashCode();
	}

}
