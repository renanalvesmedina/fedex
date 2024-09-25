package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReciboPostoPassagem implements Serializable{
	
	private static final long serialVersionUID = 1L;

	 /** identifier field */
	private Long idReciboPostoPassagem;
	
	 /** identifier field */
	private Long nrReciboPostoPassagem;
	
	/** identifier field */
	private BigDecimal vlBruto;
	
	/** identifier field */
	private DomainValue tpStatusRecibo;
	
	/** identifier field */
	private DateTime dhEmissao;
	
	 /** identifier field */
	private com.mercurio.lms.municipios.model.Filial filial;
	
	 /** identifier field */
	private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;
	
	 /** identifier field */
	private com.mercurio.lms.carregamento.model.PagtoPedagioCc pagtoPedagioCc;
	
	 /** identifier field */
	private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte  meioTransporte;
	
	 /** identifier field */
	private com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario;
	
	 /** identifier field */
	private com.mercurio.lms.contratacaoveiculos.model.Motorista motorista;
	
	 /** identifier field */
	private com.mercurio.lms.configuracoes.model.Moeda moeda;
	
	 /** identifier field */
	private com.mercurio.lms.configuracoes.model.Usuario usuario;
	
	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

	public Long getIdReciboPostoPassagem() {
		return idReciboPostoPassagem;
	}

	public void setIdReciboPostoPassagem(Long idReciboPostoPassagem) {
		this.idReciboPostoPassagem = idReciboPostoPassagem;
	}

	public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
		this.moeda = moeda;
	}

	public com.mercurio.lms.contratacaoveiculos.model.Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(
			com.mercurio.lms.contratacaoveiculos.model.Motorista motorista) {
		this.motorista = motorista;
	}

	public Long getNrReciboPostoPassagem() {
		return nrReciboPostoPassagem;
	}

	public void setNrReciboPostoPassagem(Long nrReciboPostoPassagem) {
		this.nrReciboPostoPassagem = nrReciboPostoPassagem;
	}

	public com.mercurio.lms.carregamento.model.PagtoPedagioCc getPagtoPedagioCc() {
		return pagtoPedagioCc;
	}

	public void setPagtoPedagioCc(
			com.mercurio.lms.carregamento.model.PagtoPedagioCc pagtoPedagioCc) {
		this.pagtoPedagioCc = pagtoPedagioCc;
	}

	public com.mercurio.lms.contratacaoveiculos.model.Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(
			com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public DomainValue getTpStatusRecibo() {
		return tpStatusRecibo;
	}

	public void setTpStatusRecibo(DomainValue tpStatusRecibo) {
		this.tpStatusRecibo = tpStatusRecibo;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getVlBruto() {
		return vlBruto;
	}

	public void setVlBruto(BigDecimal vlBruto) {
		this.vlBruto = vlBruto;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idReciboPostoPassagem",
				getIdReciboPostoPassagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReciboPostoPassagem))
			return false;
        ReciboPostoPassagem castOther = (ReciboPostoPassagem) other;
		return new EqualsBuilder().append(this.getIdReciboPostoPassagem(),
				castOther.getIdReciboPostoPassagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdReciboPostoPassagem())
            .toHashCode();
    }
}
