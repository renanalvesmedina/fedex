package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Municipio;

public class LiberacaoEmbarqueLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idLiberacaoEmbarqueLog;
	private LiberacaoEmbarque liberacaoEmbarque;
	private Cliente cliente;
	private Municipio municipio;
	private DomainValue tpModal;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdLiberacaoEmbarqueLog() {
   
		return idLiberacaoEmbarqueLog;
	}
   
	public void setIdLiberacaoEmbarqueLog(long idLiberacaoEmbarqueLog) {
   
		this.idLiberacaoEmbarqueLog = idLiberacaoEmbarqueLog;
	}
	
	public LiberacaoEmbarque getLiberacaoEmbarque() {
   
		return liberacaoEmbarque;
	}
   
	public void setLiberacaoEmbarque(LiberacaoEmbarque liberacaoEmbarque) {
   
		this.liberacaoEmbarque = liberacaoEmbarque;
	}
	
	public Cliente getCliente() {
   
		return cliente;
	}
   
	public void setCliente(Cliente cliente) {
   
		this.cliente = cliente;
	}
	
	public Municipio getMunicipio() {
   
		return municipio;
	}
   
	public void setMunicipio(Municipio municipio) {
   
		this.municipio = municipio;
	}
	
	public DomainValue getTpModal() {
   
		return tpModal;
	}
   
	public void setTpModal(DomainValue tpModal) {
   
		this.tpModal = tpModal;
	}
	
	public DomainValue getTpOrigemLog() {
   
		return tpOrigemLog;
	}
   
	public void setTpOrigemLog(DomainValue tpOrigemLog) {
   
		this.tpOrigemLog = tpOrigemLog;
	}
	
	public String getLoginLog() {
   
		return loginLog;
	}
   
	public void setLoginLog(String loginLog) {
   
		this.loginLog = loginLog;
	}
	
	public DateTime getDhLog() {
   
		return dhLog;
	}
   
	public void setDhLog(DateTime dhLog) {
   
		this.dhLog = dhLog;
	}
	
	public DomainValue getOpLog() {
   
		return opLog;
	}
   
	public void setOpLog(DomainValue opLog) {
   
		this.opLog = opLog;
	}
	
   	public String toString() {
		return new ToStringBuilder(this).append("idLiberacaoEmbarqueLog",
				getIdLiberacaoEmbarqueLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LiberacaoEmbarqueLog))
			return false;
		LiberacaoEmbarqueLog castOther = (LiberacaoEmbarqueLog) other;
		return new EqualsBuilder().append(this.getIdLiberacaoEmbarqueLog(),
				castOther.getIdLiberacaoEmbarqueLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdLiberacaoEmbarqueLog())
			.toHashCode();
	}
} 