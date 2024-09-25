package com.mercurio.lms.tributos.model;

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

import com.mercurio.lms.municipios.model.Municipio;

@Entity
@Table(name = "ALIQUOTA_CONTRIB_SERV_MUNIC")
@SequenceGenerator(name = "ALIQUOTA_CONTRIB_SERV_MUNIC_SEQ", sequenceName = "ALIQUOTA_CONTRIB_SERV_MUNIC_SQ", allocationSize = 1)
public class AliquotaContribuicaoServMunic implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_ALIQUOTA_CONTRIB_SERV_MUNIC", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALIQUOTA_CONTRIB_SERV_MUNIC_SEQ")
	private Long idAliquotaContribuicaoServMunic;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ALIQUOTA_CONTRIBUICAO_SERV", nullable = false)
	private AliquotaContribuicaoServ aliquotaContribuicaoServ;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_MUNICIPIO", nullable = false)
	private Municipio municipio;

	public String toString() {
		return new ToStringBuilder(this).append("idAliquotaContribuicaoServMunic", getIdAliquotaContribuicaoServMunic()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaContribuicaoServMunic))
			return false;
		AliquotaContribuicaoServMunic castOther = (AliquotaContribuicaoServMunic) other;
		return new EqualsBuilder().append(this.getIdAliquotaContribuicaoServMunic(), castOther.getIdAliquotaContribuicaoServMunic()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaContribuicaoServMunic()).toHashCode();
	}

	public Long getIdAliquotaContribuicaoServMunic() {
		return idAliquotaContribuicaoServMunic;
	}

	public void setIdAliquotaContribuicaoServMunic(Long idAliquotaContribuicaoServMunic) {
		this.idAliquotaContribuicaoServMunic = idAliquotaContribuicaoServMunic;
	}

	public AliquotaContribuicaoServ getAliquotaContribuicaoServ() {
		return aliquotaContribuicaoServ;
	}

	public void setAliquotaContribuicaoServ(AliquotaContribuicaoServ aliquotaContribuicaoServ) {
		this.aliquotaContribuicaoServ = aliquotaContribuicaoServ;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

}
