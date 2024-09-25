package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

public class ParametroSubstituicaoTribLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idParametroSubstituicaoTribLog;
	private ParametroSubstituicaoTrib parametroSubstituicaoTrib;
	private UnidadeFederativa unidadeFederativa;
	private BigDecimal pcRetencao;
	private YearMonthDay dtVigenciaInicial;
	private boolean blEmbuteIcmsParcelas;
	private boolean blImpDadosCalcCtrc;
	private YearMonthDay dtVigenciaFinal;
	private boolean blAplicarClientesEspeciais;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdParametroSubstituicaoTribLog() {
   
		return idParametroSubstituicaoTribLog;
	}
   
	public void setIdParametroSubstituicaoTribLog(
			long idParametroSubstituicaoTribLog) {
   
		this.idParametroSubstituicaoTribLog = idParametroSubstituicaoTribLog;
	}
	
	public ParametroSubstituicaoTrib getParametroSubstituicaoTrib() {
   
		return parametroSubstituicaoTrib;
	}
   
	public void setParametroSubstituicaoTrib(
			ParametroSubstituicaoTrib parametroSubstituicaoTrib) {
   
		this.parametroSubstituicaoTrib = parametroSubstituicaoTrib;
	}
	
	public UnidadeFederativa getUnidadeFederativa() {
   
		return unidadeFederativa;
	}
   
	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
   
		this.unidadeFederativa = unidadeFederativa;
	}
	
	public BigDecimal getPcRetencao() {
   
		return pcRetencao;
	}
   
	public void setPcRetencao(BigDecimal pcRetencao) {
   
		this.pcRetencao = pcRetencao;
	}
	
	public YearMonthDay getDtVigenciaInicial() {
   
		return dtVigenciaInicial;
	}
   
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
   
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public boolean isBlEmbuteIcmsParcelas() {
   
		return blEmbuteIcmsParcelas;
	}
   
	public void setBlEmbuteIcmsParcelas(boolean blEmbuteIcmsParcelas) {
   
		this.blEmbuteIcmsParcelas = blEmbuteIcmsParcelas;
	}
	
	public boolean isBlImpDadosCalcCtrc() {
   
		return blImpDadosCalcCtrc;
	}
   
	public void setBlImpDadosCalcCtrc(boolean blImpDadosCalcCtrc) {
   
		this.blImpDadosCalcCtrc = blImpDadosCalcCtrc;
	}
	
	public YearMonthDay getDtVigenciaFinal() {
   
		return dtVigenciaFinal;
	}
   
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
   
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	
	public boolean isBlAplicarClientesEspeciais() {
   
		return blAplicarClientesEspeciais;
	}
   
	public void setBlAplicarClientesEspeciais(boolean blAplicarClientesEspeciais) {
   
		this.blAplicarClientesEspeciais = blAplicarClientesEspeciais;
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
		return new ToStringBuilder(this).append(
				"idParametroSubstituicaoTribLog",
				getIdParametroSubstituicaoTribLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParametroSubstituicaoTribLog))
			return false;
		ParametroSubstituicaoTribLog castOther = (ParametroSubstituicaoTribLog) other;
		return new EqualsBuilder().append(
				this.getIdParametroSubstituicaoTribLog(),
				castOther.getIdParametroSubstituicaoTribLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(getIdParametroSubstituicaoTribLog()).toHashCode();
	}
} 