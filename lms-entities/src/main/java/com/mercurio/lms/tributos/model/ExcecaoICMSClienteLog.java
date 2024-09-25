package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

public class ExcecaoICMSClienteLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idExcecaoICMSClienteLog;
	private ExcecaoICMSCliente excecaoIcmsCliente;
	private TipoTributacaoIcms tipoTributacaoIcms;
	private UnidadeFederativa unidadeFederativa;
	private YearMonthDay dtVigenciaInicial;
	private DomainValue tpFrete;
	private boolean blSubcontratacao;
	private YearMonthDay dtVigenciaFinal;
	private boolean blObrigaCtrcSubcontratante;
	private String nrCnpjParcialDev;
	private String dsRegimeEspecial;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdExcecaoICMSClienteLog() {
   
		return idExcecaoICMSClienteLog;
	}
   
	public void setIdExcecaoICMSClienteLog(long idExcecaoICMSClienteLog) {
   
		this.idExcecaoICMSClienteLog = idExcecaoICMSClienteLog;
	}
	
	public ExcecaoICMSCliente getExcecaoIcmsCliente() {
   
		return excecaoIcmsCliente;
	}
   
	public void setExcecaoIcmsCliente(ExcecaoICMSCliente excecaoIcmsCliente) {
   
		this.excecaoIcmsCliente = excecaoIcmsCliente;
	}
	
	public TipoTributacaoIcms getTipoTributacaoIcms() {
   
		return tipoTributacaoIcms;
	}
   
	public void setTipoTributacaoIcms(TipoTributacaoIcms tipoTributacaoIcms) {
   
		this.tipoTributacaoIcms = tipoTributacaoIcms;
	}
	
	public UnidadeFederativa getUnidadeFederativa() {
   
		return unidadeFederativa;
	}
   
	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
   
		this.unidadeFederativa = unidadeFederativa;
	}
	
	public YearMonthDay getDtVigenciaInicial() {
   
		return dtVigenciaInicial;
	}
   
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
   
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public DomainValue getTpFrete() {
   
		return tpFrete;
	}
   
	public void setTpFrete(DomainValue tpFrete) {
   
		this.tpFrete = tpFrete;
	}
	
	public boolean isBlSubcontratacao() {
   
		return blSubcontratacao;
	}
   
	public void setBlSubcontratacao(boolean blSubcontratacao) {
   
		this.blSubcontratacao = blSubcontratacao;
	}
	
	public YearMonthDay getDtVigenciaFinal() {
   
		return dtVigenciaFinal;
	}
   
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
   
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	
	public boolean isBlObrigaCtrcSubcontratante() {
   
		return blObrigaCtrcSubcontratante;
	}
   
	public void setBlObrigaCtrcSubcontratante(boolean blObrigaCtrcSubcontratante) {
   
		this.blObrigaCtrcSubcontratante = blObrigaCtrcSubcontratante;
	}
	
	public String getNrCnpjParcialDev() {
   
		return nrCnpjParcialDev;
	}
   
	public void setNrCnpjParcialDev(String nrCnpjParcialDev) {
   
		this.nrCnpjParcialDev = nrCnpjParcialDev;
	}
	
	public String getDsRegimeEspecial() {
   
		return dsRegimeEspecial;
	}
   
	public void setDsRegimeEspecial(String dsRegimeEspecial) {
   
		this.dsRegimeEspecial = dsRegimeEspecial;
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
		return new ToStringBuilder(this).append("idExcecaoICMSClienteLog",
				getIdExcecaoICMSClienteLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ExcecaoICMSClienteLog))
			return false;
		ExcecaoICMSClienteLog castOther = (ExcecaoICMSClienteLog) other;
		return new EqualsBuilder().append(this.getIdExcecaoICMSClienteLog(),
				castOther.getIdExcecaoICMSClienteLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdExcecaoICMSClienteLog())
			.toHashCode();
	}
} 