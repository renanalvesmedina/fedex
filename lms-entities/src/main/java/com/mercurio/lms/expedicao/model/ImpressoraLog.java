package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

public class ImpressoraLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idImpressoraLog;
	private Impressora impressora;
	private Filial filial;
	private String dsCheckIn;
	private String dsLocalizacao;
	private DomainValue tpImpressora;
	private String dsModelo;
	private String dsFabricante;
	private String dsMac;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdImpressoraLog() {
   
		return idImpressoraLog;
	}
   
	public void setIdImpressoraLog(long idImpressoraLog) {
   
		this.idImpressoraLog = idImpressoraLog;
	}
	
	public Impressora getImpressora() {
   
		return impressora;
	}
   
	public void setImpressora(Impressora impressora) {
   
		this.impressora = impressora;
	}
	
	public Filial getFilial() {
   
		return filial;
	}
   
	public void setFilial(Filial filial) {
   
		this.filial = filial;
	}
	
	public String getDsCheckIn() {
   
		return dsCheckIn;
	}
   
	public void setDsCheckIn(String dsCheckIn) {
   
		this.dsCheckIn = dsCheckIn;
	}
	
	public String getDsLocalizacao() {
   
		return dsLocalizacao;
	}
   
	public void setDsLocalizacao(String dsLocalizacao) {
   
		this.dsLocalizacao = dsLocalizacao;
	}
	
	public DomainValue getTpImpressora() {
   
		return tpImpressora;
	}
   
	public void setTpImpressora(DomainValue tpImpressora) {
   
		this.tpImpressora = tpImpressora;
	}
	
	public String getDsModelo() {
   
		return dsModelo;
	}
   
	public void setDsModelo(String dsModelo) {
   
		this.dsModelo = dsModelo;
	}
	
	public String getDsFabricante() {
   
		return dsFabricante;
	}
   
	public void setDsFabricante(String dsFabricante) {
   
		this.dsFabricante = dsFabricante;
	}
	
	public String getDsMac() {
   
		return dsMac;
	}
   
	public void setDsMac(String dsMac) {
   
		this.dsMac = dsMac;
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
		return new ToStringBuilder(this).append("idImpressoraLog",
				getIdImpressoraLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ImpressoraLog))
			return false;
		ImpressoraLog castOther = (ImpressoraLog) other;
		return new EqualsBuilder().append(this.getIdImpressoraLog(),
				castOther.getIdImpressoraLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdImpressoraLog()).toHashCode();
	}
} 