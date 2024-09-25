package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.Arrays;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="ANEXO_AWB")
@SequenceGenerator(name="SQ_ANEXO_AWB", sequenceName="ANEXO_AWB_SQ", allocationSize=1)
public class AnexoAwb implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_ANEXO_AWB")
	@Column(name="ID_ANEXO_AWB", nullable=false)
	private Long idAnexoAwb;
	
	@ManyToOne
	@JoinColumn(name = "ID_AWB", nullable = false)
	private Awb awb;
	
	@Column(name="DS_ANEXO", length=200, nullable=false)
	private String dsAnexo;
	
	@Column(name="TP_ANEXO", length=1, nullable=false)
	private String tpAnexo;
	
	@Lob
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	@Column(name="DC_ANEXO", nullable=false)
	private byte[] dcAnexo;
	
	public AnexoAwb() {
		super();
	}
	
	public Long getIdAnexoAwb() {
		return idAnexoAwb;
	}
	public void setIdAnexoAwb(Long idAnexoAwb) {
		this.idAnexoAwb = idAnexoAwb;
	}
	public Awb getAwb() {
		return awb;
	}
	public void setAwb(Awb awb) {
		this.awb = awb;
	}
	public String getDsAnexo() {
		return dsAnexo;
	}
	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}
	public String getTpAnexo() {
		return tpAnexo;
	}
	public void setTpAnexo(String tpAnexo) {
		this.tpAnexo = tpAnexo;
	}
	public byte[] getDcAnexo() {
		return dcAnexo;
	}
	public void setDcAnexo(byte[] dcAnexo) {
		this.dcAnexo = dcAnexo;
	}
	
	@Override
	public String toString() {
		return "AnexoAwb [idAnexoAwb=" + idAnexoAwb + ", awb=" + awb
				+ ", dsAnexo=" + dsAnexo + ", tpAnexo=" + tpAnexo
				+ ", dcAnexo=" + Arrays.toString(dcAnexo) + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((awb == null) ? 0 : awb.hashCode());
		result = prime * result + Arrays.hashCode(dcAnexo);
		result = prime * result + ((dsAnexo == null) ? 0 : dsAnexo.hashCode());
		result = prime * result
				+ ((idAnexoAwb == null) ? 0 : idAnexoAwb.hashCode());
		result = prime * result + ((tpAnexo == null) ? 0 : tpAnexo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnexoAwb other = (AnexoAwb) obj;
		if (awb == null) {
			if (other.awb != null)
				return false;
		} else if (!awb.equals(other.awb))
			return false;
		if (!Arrays.equals(dcAnexo, other.dcAnexo))
			return false;
		if (dsAnexo == null) {
			if (other.dsAnexo != null)
				return false;
		} else if (!dsAnexo.equals(other.dsAnexo))
			return false;
		if (idAnexoAwb == null) {
			if (other.idAnexoAwb != null)
				return false;
		} else if (!idAnexoAwb.equals(other.idAnexoAwb))
			return false;
		if (tpAnexo == null) {
			if (other.tpAnexo != null)
				return false;
		} else if (!tpAnexo.equals(other.tpAnexo))
			return false;
		return true;
	}
}
