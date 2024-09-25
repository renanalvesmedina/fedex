package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;

public class GeneralidadeClienteLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long idGeneralidadeClienteLog;
	private GeneralidadeCliente generalidadeCliente;
	private ParametroCliente parametroCliente;
	private ParcelaPreco parcelaPreco;
	private DomainValue tpIndicador;
	private BigDecimal vlGeneralidade;
	private BigDecimal pcReajGeneralidade;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;

	public long getIdGeneralidadeClienteLog() {
		return idGeneralidadeClienteLog;
	}

	public void setIdGeneralidadeClienteLog(long idGeneralidadeClienteLog) {
		this.idGeneralidadeClienteLog = idGeneralidadeClienteLog;
	}

	public GeneralidadeCliente getGeneralidadeCliente() {
		return generalidadeCliente;
	}

	public void setGeneralidadeCliente(GeneralidadeCliente generalidadeCliente) {
		this.generalidadeCliente = generalidadeCliente;
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

	public DomainValue getTpIndicador() {
		return tpIndicador;
	}

	public void setTpIndicador(DomainValue tpIndicador) {
		this.tpIndicador = tpIndicador;
	}

	public BigDecimal getVlGeneralidade() {
		return vlGeneralidade;
	}

	public void setVlGeneralidade(BigDecimal vlGeneralidade) {
		this.vlGeneralidade = vlGeneralidade;
	}

	public BigDecimal getPcReajGeneralidade() {
		return pcReajGeneralidade;
	}

	public void setPcReajGeneralidade(BigDecimal pcReajGeneralidade) {
		this.pcReajGeneralidade = pcReajGeneralidade;
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
		return new ToStringBuilder(this).append("idGeneralidadeClienteLog",
				getIdGeneralidadeClienteLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof GeneralidadeClienteLog))
			return false;
		GeneralidadeClienteLog castOther = (GeneralidadeClienteLog) other;
		return new EqualsBuilder().append(this.getIdGeneralidadeClienteLog(),
				castOther.getIdGeneralidadeClienteLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdGeneralidadeClienteLog())
				.toHashCode();
	}
}