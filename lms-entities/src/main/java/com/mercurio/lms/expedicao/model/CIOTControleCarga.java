package com.mercurio.lms.expedicao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.carregamento.model.ControleCarga;

@Entity
@Table(name = "CIOT_CONTROLE_CARGA")
@SequenceGenerator(name = "CIOT_CONTROLE_CARGA_SEQ", sequenceName = "CIOT_CONTROLE_CARGA_SEQ", allocationSize = 1)
public class CIOTControleCarga implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_CIOT_CONTROLE_CARGA", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CIOT_CONTROLE_CARGA_SEQ")
	private Long idCIOTControleCarga;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CIOT", nullable = false)
	private CIOT ciot;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CONTROLE_CARGA", nullable = false)
	private ControleCarga controleCarga;

	public String toString() {
		return new ToStringBuilder(this).append("idCIOTControleCarga", getIdCIOTControleCarga()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CIOTControleCarga))
			return false;
		CIOTControleCarga castOther = (CIOTControleCarga) other;
		return new EqualsBuilder().append(this.getIdCIOTControleCarga(), castOther.getIdCIOTControleCarga()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdCIOTControleCarga()).toHashCode();
	}

	public Long getIdCIOTControleCarga() {
		return idCIOTControleCarga;
	}

	public void setIdCIOTControleCarga(Long idCIOTControleCarga) {
		this.idCIOTControleCarga = idCIOTControleCarga;
	}

	public CIOT getCiot() {
		return ciot;
	}

	public void setCiot(CIOT ciot) {
		this.ciot = ciot;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

}
