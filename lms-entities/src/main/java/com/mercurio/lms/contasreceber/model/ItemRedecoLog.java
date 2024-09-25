package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class ItemRedecoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idItemRedecoLog;
	private ItemRedeco itemRedeco;
	private Redeco redeco;
	private Fatura fatura;
	private BigDecimal vlTarifa;
	private BigDecimal vlJuros;
	private Recibo recibo;
	private String obItemRedeco;
	private Long nrVersao;
	private BigDecimal vlDiferencaCambialCotacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdItemRedecoLog() {
   
		return idItemRedecoLog;
	}
   
	public void setIdItemRedecoLog(long idItemRedecoLog) {
   
		this.idItemRedecoLog = idItemRedecoLog;
	}
	
	public ItemRedeco getItemRedeco() {
   
		return itemRedeco;
	}
   
	public void setItemRedeco(ItemRedeco itemRedeco) {
   
		this.itemRedeco = itemRedeco;
	}
	
	public Redeco getRedeco() {
   
		return redeco;
	}
   
	public void setRedeco(Redeco redeco) {
   
		this.redeco = redeco;
	}
	
	public Fatura getFatura() {
   
		return fatura;
	}
   
	public void setFatura(Fatura fatura) {
   
		this.fatura = fatura;
	}
	
	public BigDecimal getVlTarifa() {
   
		return vlTarifa;
	}
   
	public void setVlTarifa(BigDecimal vlTarifa) {
   
		this.vlTarifa = vlTarifa;
	}
	
	public BigDecimal getVlJuros() {
   
		return vlJuros;
	}
   
	public void setVlJuros(BigDecimal vlJuros) {
   
		this.vlJuros = vlJuros;
	}
	
	public Recibo getRecibo() {
   
		return recibo;
	}
   
	public void setRecibo(Recibo recibo) {
   
		this.recibo = recibo;
	}
	
	public String getObItemRedeco() {
   
		return obItemRedeco;
	}
   
	public void setObItemRedeco(String obItemRedeco) {
   
		this.obItemRedeco = obItemRedeco;
	}
	
	public Long getNrVersao() {
   
		return nrVersao;
	}
   
	public void setNrVersao(Long nrVersao) {
   
		this.nrVersao = nrVersao;
	}
	
	public BigDecimal getVlDiferencaCambialCotacao() {
   
		return vlDiferencaCambialCotacao;
	}
   
	public void setVlDiferencaCambialCotacao(
			BigDecimal vlDiferencaCambialCotacao) {
   
		this.vlDiferencaCambialCotacao = vlDiferencaCambialCotacao;
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
		return new ToStringBuilder(this).append("idItemRedecoLog",
				getIdItemRedecoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemRedecoLog))
			return false;
		ItemRedecoLog castOther = (ItemRedecoLog) other;
		return new EqualsBuilder().append(this.getIdItemRedecoLog(),
				castOther.getIdItemRedecoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdItemRedecoLog()).toHashCode();
	}
} 