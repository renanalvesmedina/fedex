package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class CarteiraVendasCliente implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long idCarteiraVendasCliente;
	private Cliente cliente;
	private CarteiraVendas carteiraVendas;
	private DomainValue tpModal;
	private DomainValue tpAbrangencia;
	private DomainValue tpComissao;
	private YearMonthDay dtPromotor;
	
	public Long getIdCarteiraVendasCliente() {
		return idCarteiraVendasCliente;
	}

	public void setIdCarteiraVendasCliente(Long idCarteiraVendasCliente) {
		this.idCarteiraVendasCliente = idCarteiraVendasCliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public CarteiraVendas getCarteiraVendas() {
		return carteiraVendas;
	}

	public void setCarteiraVendas(CarteiraVendas carteiraVendas) {
		this.carteiraVendas = carteiraVendas;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public YearMonthDay getDtPromotor() {
		return dtPromotor;
	}

	public void setDtPromotor(YearMonthDay dtPromotor) {
		this.dtPromotor = dtPromotor;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CarteiraVendasCliente))
			return false;
		CarteiraVendasCliente cast = (CarteiraVendasCliente) obj;
		return new EqualsBuilder().append(this.getIdCarteiraVendasCliente(),
				cast.getIdCarteiraVendasCliente()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getIdCarteiraVendasCliente())
		.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idCateiraVendasCliente",
				this.getIdCarteiraVendasCliente()).toString();
	}

	public DomainValue getTpComissao() {
		return tpComissao;
	}

	public void setTpComissao(DomainValue tpComissao) {
		this.tpComissao = tpComissao;
	}

}
