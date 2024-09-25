package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import com.mercurio.lms.municipios.model.Municipio;

@Entity
@Table(name="MUNICIPIO_TRT_CLIENTE")
@SequenceGenerator(name="SQ_MUNICIPIO_TRT_CLIENTE", sequenceName="MUNICIPIO_TRT_CLIENTE_SQ", allocationSize=1)
public class MunicipioTrtCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_MUNICIPIO_TRT_CLIENTE")
	@Column(name="ID_MUNICIPIO_TRT_CLIENTE", nullable=false)
	private Long idMunicipioTrtCliente;

	@ManyToOne
	@JoinColumn(name="ID_TRT_CLIENTE", nullable=false)
	private TrtCliente trtCliente;

	@ManyToOne
	@JoinColumn(name="ID_MUNICIPIO", nullable=false)
	private Municipio municipio;

	@Column(name = "BL_COBRA_TRT", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blCobraTrt;	

	public String toString() {
		return new ToStringBuilder(this).append("idMunicipioTrtCliente",
				getIdMunicipioTrtCliente()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MunicipioTrtCliente))
			return false;
		MunicipioTrtCliente castOther = (MunicipioTrtCliente) other;
		return new EqualsBuilder().append(this.getIdMunicipioTrtCliente(),
				castOther.getIdMunicipioTrtCliente()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipioTrtCliente()).toHashCode();
	}	

	public Long getIdMunicipioTrtCliente() {
		return idMunicipioTrtCliente;
	}

	public void setIdMunicipioTrtCliente(Long idMunicipioTrtCliente) {
		this.idMunicipioTrtCliente = idMunicipioTrtCliente;
	}

	public Boolean getBlCobraTrt() {
		return blCobraTrt;
	}

	public void setBlCobraTrt(Boolean blCobraTrt) {
		this.blCobraTrt = blCobraTrt;
	}

	public TrtCliente getTrtCliente() {
		return trtCliente;
	}

	public void setTrtCliente(TrtCliente trtCliente) {
		this.trtCliente = trtCliente;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
}
