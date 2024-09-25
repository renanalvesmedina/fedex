package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class AnexoDoctoServicoLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long idAnexoDoctoServicoLog;
	private AnexoDoctoServico anexoDoctoServico;
	private String dsAnexoDoctoServico;
	private DomainValue tpSituacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;

	public long getIdAnexoDoctoServicoLog() {
		return idAnexoDoctoServicoLog;
	}

	public void setIdAnexoDoctoServicoLog(long idAnexoDoctoServicoLog) {
		this.idAnexoDoctoServicoLog = idAnexoDoctoServicoLog;
	}

	public AnexoDoctoServico getAnexoDoctoServico() {
		return anexoDoctoServico;
	}

	public void setAnexoDoctoServico(AnexoDoctoServico anexoDoctoServico) {
		this.anexoDoctoServico = anexoDoctoServico;
	}

	public String getDsAnexoDoctoServico() {
		return dsAnexoDoctoServico;
	}

	public void setDsAnexoDoctoServico(String dsAnexoDoctoServico) {
		this.dsAnexoDoctoServico = dsAnexoDoctoServico;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
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
		return new ToStringBuilder(this).append("idAnexoDoctoServicoLog",
				getIdAnexoDoctoServicoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AnexoDoctoServicoLog))
			return false;
		AnexoDoctoServicoLog castOther = (AnexoDoctoServicoLog) other;
		return new EqualsBuilder().append(this.getIdAnexoDoctoServicoLog(),
				castOther.getIdAnexoDoctoServicoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAnexoDoctoServicoLog())
				.toHashCode();
	}
}