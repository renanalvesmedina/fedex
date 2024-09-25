package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class PromotorCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idPromotorCliente;

	/** persistent field */
	private YearMonthDay dtPrimeiroPromotor;
	
	private YearMonthDay dtFimPromotor;

	/** persistent field */
	private YearMonthDay dtInicioPromotor;

	/** nullable persistent field */
	private BigDecimal pcRateioComissao;

	/** nullable persistent field */
	private DomainValue tpModal;

	/** nullable persistent field */
	private DomainValue tpAbrangencia;

	/** nullable persistent field */
	private YearMonthDay dtReconquista;

	/** persistent field */
	private Cliente cliente;

	/** persistent field */
	private Usuario usuario;
	
	/** persistent field */
	private YearMonthDay dtInclusao;

	/** nullable persistent field */
	private BigDecimal pcComissao;

	/** nullable persistent field */
	private BigDecimal pcReconquista;

	public Long getIdPromotorCliente() {
		return this.idPromotorCliente;
	}

	public void setIdPromotorCliente(Long idPromotorCliente) {
		this.idPromotorCliente = idPromotorCliente;
	}

	public YearMonthDay getDtPrimeiroPromotor() {
		return this.dtPrimeiroPromotor;
	}

	public void setDtPrimeiroPromotor(YearMonthDay dtPrimeiroPromotor) {
		this.dtPrimeiroPromotor = dtPrimeiroPromotor;
	}

	public YearMonthDay getDtInicioPromotor() {
		return this.dtInicioPromotor;
	}

	public void setDtInicioPromotor(YearMonthDay dtInicioPromotor) {
		this.dtInicioPromotor = dtInicioPromotor;
	}
	
	public YearMonthDay getDtFimPromotor() {
		return dtFimPromotor;
	}

	public void setDtFimPromotor(YearMonthDay dtFimPromotor) {
		this.dtFimPromotor = dtFimPromotor;
	}

	public BigDecimal getPcRateioComissao() {
		return this.pcRateioComissao;
	}

	public void setPcRateioComissao(BigDecimal pcRateioComissao) {
		this.pcRateioComissao = pcRateioComissao;
	}

	public DomainValue getTpModal() {
		return this.tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public DomainValue getTpAbrangencia() {
		return this.tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public YearMonthDay getDtReconquista() {
		return this.dtReconquista;
	}

	public void setDtReconquista(YearMonthDay dtReconquista) {
		this.dtReconquista = dtReconquista;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public YearMonthDay getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(YearMonthDay dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	public BigDecimal getPcComissao() {
		return pcComissao;
	}

	public void setPcComissao(BigDecimal pcComissao) {
		this.pcComissao = pcComissao;
	}

	public BigDecimal getPcReconquista() {
		return pcReconquista;
	}

	public void setPcReconquista(BigDecimal pcReconquista) {
		this.pcReconquista = pcReconquista;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idPromotorCliente",
				getIdPromotorCliente()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PromotorCliente))
			return false;
		PromotorCliente castOther = (PromotorCliente) other;
		return new EqualsBuilder().append(this.getIdPromotorCliente(),
				castOther.getIdPromotorCliente()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdPromotorCliente())
			.toHashCode();
	}

}
