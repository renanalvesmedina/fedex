package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;

public class AliquotaContribuicaoServLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idAliquotaContribuicaoServLog;
	private AliquotaContribuicaoServ aliquotaContribuicaoServ;
	private YearMonthDay dtVigenciaInicial;
	private BigDecimal pcBcalcReduzida;
	private BigDecimal pcAliquota;
	private BigDecimal vlPiso;
	private DomainValue tpImposto;
	private YearMonthDay dtVigenciaFinal;
	private Pessoa pessoa;
	private Long idServicoAdicional;
	private ServicoTributo servicoTributo;
	private String obAliquotaContribuicaoServ;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdAliquotaContribuicaoServLog() {
   
		return idAliquotaContribuicaoServLog;
	}
   
	public void setIdAliquotaContribuicaoServLog(
			long idAliquotaContribuicaoServLog) {
   
		this.idAliquotaContribuicaoServLog = idAliquotaContribuicaoServLog;
	}
	
	public AliquotaContribuicaoServ getAliquotaContribuicaoServ() {
   
		return aliquotaContribuicaoServ;
	}
   
	public void setAliquotaContribuicaoServ(
			AliquotaContribuicaoServ aliquotaContribuicaoServ) {
   
		this.aliquotaContribuicaoServ = aliquotaContribuicaoServ;
	}
	
	public YearMonthDay getDtVigenciaInicial() {
   
		return dtVigenciaInicial;
	}
   
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
   
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public BigDecimal getPcBcalcReduzida() {
   
		return pcBcalcReduzida;
	}
   
	public void setPcBcalcReduzida(BigDecimal pcBcalcReduzida) {
   
		this.pcBcalcReduzida = pcBcalcReduzida;
	}
	
	public BigDecimal getPcAliquota() {
   
		return pcAliquota;
	}
   
	public void setPcAliquota(BigDecimal pcAliquota) {
   
		this.pcAliquota = pcAliquota;
	}
	
	public BigDecimal getVlPiso() {
   
		return vlPiso;
	}
   
	public void setVlPiso(BigDecimal vlPiso) {
   
		this.vlPiso = vlPiso;
	}
	
	public DomainValue getTpImposto() {
   
		return tpImposto;
	}
   
	public void setTpImposto(DomainValue tpImposto) {
   
		this.tpImposto = tpImposto;
	}
	
	public YearMonthDay getDtVigenciaFinal() {
   
		return dtVigenciaFinal;
	}
   
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
   
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	
	public Pessoa getPessoa() {
   
		return pessoa;
	}
   
	public void setPessoa(Pessoa pessoa) {
   
		this.pessoa = pessoa;
	}
	
	public Long getIdServicoAdicional() {
   
		return idServicoAdicional;
	}
   
	public void setIdServicoAdicional(Long idServicoAdicional) {
   
		this.idServicoAdicional = idServicoAdicional;
	}
	
	public ServicoTributo getServicoTributo() {
   
		return servicoTributo;
	}
   
	public void setServicoTributo(ServicoTributo servicoTributo) {
   
		this.servicoTributo = servicoTributo;
	}
	
	public String getObAliquotaContribuicaoServ() {
   
		return obAliquotaContribuicaoServ;
	}
   
	public void setObAliquotaContribuicaoServ(String obAliquotaContribuicaoServ) {
   
		this.obAliquotaContribuicaoServ = obAliquotaContribuicaoServ;
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
				"idAliquotaContribuicaoServLog",
				getIdAliquotaContribuicaoServLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaContribuicaoServLog))
			return false;
		AliquotaContribuicaoServLog castOther = (AliquotaContribuicaoServLog) other;
		return new EqualsBuilder().append(
				this.getIdAliquotaContribuicaoServLog(),
				castOther.getIdAliquotaContribuicaoServLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaContribuicaoServLog())
			.toHashCode();
	}
} 