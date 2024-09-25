package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class AwbEmbalagem implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idAwbEmbalagem;

	/** nullable persistent field */
	private Integer qtEmbalagens;

	/** persistent field */
	private com.mercurio.lms.expedicao.model.Embalagem embalagem;

	/** persistent field */
	private com.mercurio.lms.expedicao.model.Awb awb;

	public Long getIdAwbEmbalagem() {
		return this.idAwbEmbalagem;
	}

	public void setIdAwbEmbalagem(Long idAwbEmbalagem) {
		this.idAwbEmbalagem = idAwbEmbalagem;
	}

	public Integer getQtEmbalagens() {
		return this.qtEmbalagens;
	}

	public void setQtEmbalagens(Integer qtEmbalagens) {
		this.qtEmbalagens = qtEmbalagens;
	}

	public com.mercurio.lms.expedicao.model.Embalagem getEmbalagem() {
		return this.embalagem;
	}

	public void setEmbalagem(
			com.mercurio.lms.expedicao.model.Embalagem embalagem) {
		this.embalagem = embalagem;
	}

	public com.mercurio.lms.expedicao.model.Awb getAwb() {
		return this.awb;
	}

	public void setAwb(com.mercurio.lms.expedicao.model.Awb awb) {
		this.awb = awb;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idAwbEmbalagem",
				getIdAwbEmbalagem()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AwbEmbalagem))
			return false;
		AwbEmbalagem castOther = (AwbEmbalagem) other;
		return new EqualsBuilder().append(this.getIdAwbEmbalagem(),
				castOther.getIdAwbEmbalagem()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAwbEmbalagem()).toHashCode();
	}

}
