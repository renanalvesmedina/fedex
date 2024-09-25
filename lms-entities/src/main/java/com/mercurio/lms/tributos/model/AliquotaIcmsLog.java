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

public class AliquotaIcmsLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idAliquotaIcmsLog;
	private AliquotaIcms aliquotaIcms;
	private BigDecimal pcAliquota;
	private BigDecimal pcEmbute;
	private UnidadeFederativa unidadeFederativaOrigem;
	private UnidadeFederativa unidadeFederativaDestino;
	private TipoTributacaoIcms tipoTributacaoIcms;
	private YearMonthDay dtVigenciaInicial;
	private DomainValue tpSituacaoTribRemetente;
	private DomainValue tpSituacaoTribDestinatario;
	private DomainValue tpTipoFrete;
	private YearMonthDay dtVigenciaFinal;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdAliquotaIcmsLog() {
   
		return idAliquotaIcmsLog;
	}
   
	public void setIdAliquotaIcmsLog(long idAliquotaIcmsLog) {
   
		this.idAliquotaIcmsLog = idAliquotaIcmsLog;
	}
	
	public AliquotaIcms getAliquotaIcms() {
   
		return aliquotaIcms;
	}
   
	public void setAliquotaIcms(AliquotaIcms aliquotaIcms) {
   
		this.aliquotaIcms = aliquotaIcms;
	}
	
	public BigDecimal getPcAliquota() {
   
		return pcAliquota;
	}
   
	public void setPcAliquota(BigDecimal pcAliquota) {
   
		this.pcAliquota = pcAliquota;
	}
	
	public BigDecimal getPcEmbute() {
   
		return pcEmbute;
	}
   
	public void setPcEmbute(BigDecimal pcEmbute) {
   
		this.pcEmbute = pcEmbute;
	}
	
	public UnidadeFederativa getUnidadeFederativaOrigem() {
   
		return unidadeFederativaOrigem;
	}
   
	public void setUnidadeFederativaOrigem(
			UnidadeFederativa unidadeFederativaOrigem) {
   
		this.unidadeFederativaOrigem = unidadeFederativaOrigem;
	}
	
	public UnidadeFederativa getUnidadeFederativaDestino() {
   
		return unidadeFederativaDestino;
	}
   
	public void setUnidadeFederativaDestino(
			UnidadeFederativa unidadeFederativaDestino) {
   
		this.unidadeFederativaDestino = unidadeFederativaDestino;
	}
	
	public TipoTributacaoIcms getTipoTributacaoIcms() {
   
		return tipoTributacaoIcms;
	}
   
	public void setTipoTributacaoIcms(TipoTributacaoIcms tipoTributacaoIcms) {
   
		this.tipoTributacaoIcms = tipoTributacaoIcms;
	}
	
	public YearMonthDay getDtVigenciaInicial() {
   
		return dtVigenciaInicial;
	}
   
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
   
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public DomainValue getTpSituacaoTribRemetente() {
   
		return tpSituacaoTribRemetente;
	}
   
	public void setTpSituacaoTribRemetente(DomainValue tpSituacaoTribRemetente) {
   
		this.tpSituacaoTribRemetente = tpSituacaoTribRemetente;
	}
	
	public DomainValue getTpSituacaoTribDestinatario() {
   
		return tpSituacaoTribDestinatario;
	}
   
	public void setTpSituacaoTribDestinatario(
			DomainValue tpSituacaoTribDestinatario) {
   
		this.tpSituacaoTribDestinatario = tpSituacaoTribDestinatario;
	}
	
	public DomainValue getTpTipoFrete() {
   
		return tpTipoFrete;
	}
   
	public void setTpTipoFrete(DomainValue tpTipoFrete) {
   
		this.tpTipoFrete = tpTipoFrete;
	}
	
	public YearMonthDay getDtVigenciaFinal() {
   
		return dtVigenciaFinal;
	}
   
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
   
		this.dtVigenciaFinal = dtVigenciaFinal;
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
		return new ToStringBuilder(this).append("idAliquotaIcmsLog",
				getIdAliquotaIcmsLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaIcmsLog))
			return false;
		AliquotaIcmsLog castOther = (AliquotaIcmsLog) other;
		return new EqualsBuilder().append(this.getIdAliquotaIcmsLog(),
				castOther.getIdAliquotaIcmsLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaIcmsLog())
			.toHashCode();
	}
} 