package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class CarteiraVendas implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long idCarteiraVendas;
	private com.mercurio.lms.configuracoes.model.Usuario usuario;
	private com.mercurio.lms.workflow.model.Pendencia pendencia;
	private DomainValue tpSituacaoAprovacao;
	private Boolean blEfetivadoNivel1;
	private Boolean blEfetivadoNivel2;
	private YearMonthDay dtInicioLote;
	private YearMonthDay dtFimLote;
	
	private List<CarteiraVendasCliente> carteiraVendasClientes; 
	
	public List<CarteiraVendasCliente> getCarteiraVendasClientes() {
		return carteiraVendasClientes;
	}

	public void setCarteiraVendasClientes(
			List<CarteiraVendasCliente> carteiraVendasClientes) {
		this.carteiraVendasClientes = carteiraVendasClientes;
	}

	public Long getIdCarteiraVendas() {
		return idCarteiraVendas;
	}

	public void setIdCarteiraVendas(Long idCarteiraVendas) {
		this.idCarteiraVendas = idCarteiraVendas;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

	public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public Boolean getBlEfetivadoNivel1() {
		return blEfetivadoNivel1;
	}

	public void setBlEfetivadoNivel1(Boolean blEfetivadoNivel1) {
		this.blEfetivadoNivel1 = blEfetivadoNivel1;
	}

	public Boolean getBlEfetivadoNivel2() {
		return blEfetivadoNivel2;
	}

	public void setBlEfetivadoNivel2(Boolean blEfetivadoNivel2) {
		this.blEfetivadoNivel2 = blEfetivadoNivel2;
	}

	public YearMonthDay getDtInicioLote() {
		return dtInicioLote;
	}

	public void setDtInicioLote(YearMonthDay dtInicioLote) {
		this.dtInicioLote = dtInicioLote;
	}

	public YearMonthDay getDtFimLote() {
		return dtFimLote;
	}

	public void setDtFimLote(YearMonthDay dtFimLote) {
		this.dtFimLote = dtFimLote;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CarteiraVendas))
			return false;
		CarteiraVendas cast = (CarteiraVendas) obj;
		return new EqualsBuilder().append(this.getIdCarteiraVendas(),
				cast.getIdCarteiraVendas()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getIdCarteiraVendas())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idCateiraVendas",
				this.getIdCarteiraVendas()).toString();
	}
}
