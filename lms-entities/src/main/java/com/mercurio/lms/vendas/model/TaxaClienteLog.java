package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;

public class TaxaClienteLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idTaxaClienteLog;
	private TaxaCliente taxaCliente;
	private ParametroCliente parametroCliente;
	private ParcelaPreco parcelaPreco;
	private DomainValue tpTaxaIndicador;
	private BigDecimal vlTaxa;
	private String psMinimo;
	private BigDecimal vlExcedente;
	private BigDecimal pcReajTaxa;
	private BigDecimal pcReajVlExcedente;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdTaxaClienteLog() {
   
		return idTaxaClienteLog;
	}
   
	public void setIdTaxaClienteLog(long idTaxaClienteLog) {
   
		this.idTaxaClienteLog = idTaxaClienteLog;
	}
	
	public TaxaCliente getTaxaCliente() {
   
		return taxaCliente;
	}
   
	public void setTaxaCliente(TaxaCliente taxaCliente) {
   
		this.taxaCliente = taxaCliente;
	}
	
	public ParametroCliente getParametroCliente() {
   
		return parametroCliente;
	}
   
	public void setParametroCliente(ParametroCliente parametroCliente) {
   
		this.parametroCliente = parametroCliente;
	}
	
	public ParcelaPreco getParcelaPreco() {
   
		return parcelaPreco;
	}
   
	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
   
		this.parcelaPreco = parcelaPreco;
	}
	
	public DomainValue getTpTaxaIndicador() {
   
		return tpTaxaIndicador;
	}
   
	public void setTpTaxaIndicador(DomainValue tpTaxaIndicador) {
   
		this.tpTaxaIndicador = tpTaxaIndicador;
	}
	
	public BigDecimal getVlTaxa() {
   
		return vlTaxa;
	}
   
	public void setVlTaxa(BigDecimal vlTaxa) {
   
		this.vlTaxa = vlTaxa;
	}
	
	public String getPsMinimo() {
   
		return psMinimo;
	}
   
	public void setPsMinimo(String psMinimo) {
   
		this.psMinimo = psMinimo;
	}
	
	public BigDecimal getVlExcedente() {
   
		return vlExcedente;
	}
   
	public void setVlExcedente(BigDecimal vlExcedente) {
   
		this.vlExcedente = vlExcedente;
	}
	
	public BigDecimal getPcReajTaxa() {
   
		return pcReajTaxa;
	}
   
	public void setPcReajTaxa(BigDecimal pcReajTaxa) {
   
		this.pcReajTaxa = pcReajTaxa;
	}
	
	public BigDecimal getPcReajVlExcedente() {
   
		return pcReajVlExcedente;
	}
   
	public void setPcReajVlExcedente(BigDecimal pcReajVlExcedente) {
   
		this.pcReajVlExcedente = pcReajVlExcedente;
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
		return new ToStringBuilder(this).append("idTaxaClienteLog",
				getIdTaxaClienteLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TaxaClienteLog))
			return false;
		TaxaClienteLog castOther = (TaxaClienteLog) other;
		return new EqualsBuilder().append(this.getIdTaxaClienteLog(),
				castOther.getIdTaxaClienteLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTaxaClienteLog()).toHashCode();
	}
} 