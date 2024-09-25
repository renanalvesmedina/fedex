package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class AliquotaIssMunicipioServLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idAliquotaIssMunicipioServLog;
	private AliquotaIssMunicipioServ aliquotaIssMunicipioServ;
	private IssMunicipioServico issMunicipioServico;
	private BigDecimal pcAliquota;
	private BigDecimal pcEmbute;
	private YearMonthDay dtVigenciaInicial;
	private boolean blEmiteNfServico;
	private YearMonthDay dtVigenciaFinal;
	private boolean blRetencaoTomadorServico;
	private String obAliquotaIssMunicipioServ;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdAliquotaIssMunicipioServLog() {
   
		return idAliquotaIssMunicipioServLog;
	}
   
	public void setIdAliquotaIssMunicipioServLog(
			long idAliquotaIssMunicipioServLog) {
   
		this.idAliquotaIssMunicipioServLog = idAliquotaIssMunicipioServLog;
	}
	
	public AliquotaIssMunicipioServ getAliquotaIssMunicipioServ() {
   
		return aliquotaIssMunicipioServ;
	}
   
	public void setAliquotaIssMunicipioServ(
			AliquotaIssMunicipioServ aliquotaIssMunicipioServ) {
   
		this.aliquotaIssMunicipioServ = aliquotaIssMunicipioServ;
	}
	
	public IssMunicipioServico getIssMunicipioServico() {
   
		return issMunicipioServico;
	}
   
	public void setIssMunicipioServico(IssMunicipioServico issMunicipioServico) {
   
		this.issMunicipioServico = issMunicipioServico;
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
	
	public YearMonthDay getDtVigenciaInicial() {
   
		return dtVigenciaInicial;
	}
   
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
   
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public boolean isBlEmiteNfServico() {
   
		return blEmiteNfServico;
	}
   
	public void setBlEmiteNfServico(boolean blEmiteNfServico) {
   
		this.blEmiteNfServico = blEmiteNfServico;
	}
	
	public YearMonthDay getDtVigenciaFinal() {
   
		return dtVigenciaFinal;
	}
   
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
   
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	
	public boolean isBlRetencaoTomadorServico() {
   
		return blRetencaoTomadorServico;
	}
   
	public void setBlRetencaoTomadorServico(boolean blRetencaoTomadorServico) {
   
		this.blRetencaoTomadorServico = blRetencaoTomadorServico;
	}
	
	public String getObAliquotaIssMunicipioServ() {
   
		return obAliquotaIssMunicipioServ;
	}
   
	public void setObAliquotaIssMunicipioServ(String obAliquotaIssMunicipioServ) {
   
		this.obAliquotaIssMunicipioServ = obAliquotaIssMunicipioServ;
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
				"idAliquotaIssMunicipioServLog",
				getIdAliquotaIssMunicipioServLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaIssMunicipioServLog))
			return false;
		AliquotaIssMunicipioServLog castOther = (AliquotaIssMunicipioServLog) other;
		return new EqualsBuilder().append(
				this.getIdAliquotaIssMunicipioServLog(),
				castOther.getIdAliquotaIssMunicipioServLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaIssMunicipioServLog())
			.toHashCode();
	}
} 