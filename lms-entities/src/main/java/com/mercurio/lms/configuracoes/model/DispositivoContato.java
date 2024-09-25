package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

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
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name="DISPOSITIVO_CONTATO")
@SequenceGenerator(name = "DISPOSITIVO_CONTATO_SQ", sequenceName = "DISPOSITIVO_CONTATO_SQ")
public class DispositivoContato implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_DISPOSITIVO_CONTATO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DISPOSITIVO_CONTATO_SQ")
	private Long idDispositivoContato;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CONTATO", nullable = false, unique = true)
	private Contato contato;

	@Column(name = "DS_TOKEN", length = 255)
	private String dsToken;
	
	@Column(name = "NR_DDD", length = 5)
	private String nrDdd;
	
	@Column(name = "NR_TELEFONE", length = 10)
	private String nrTelefone;
	
	@Column(name = "TP_PLATAFORMA", length = 2)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_PLATAFORMA_DISPOSITIVO_CONTATO") })
	private DomainValue tpPlataforma;
	
	@Column(name = "DS_VERSAO", length = 60)
	private String dsVersao;

	public Long getIdDispositivoContato() {
		return idDispositivoContato;
	}

	public void setIdDispositivoContato(Long idDispositivoContato) {
		this.idDispositivoContato = idDispositivoContato;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public String getDsToken() {
		return dsToken;
	}

	public void setDsToken(String dsToken) {
		this.dsToken = dsToken;
	}

	public String getNrDdd() {
		return nrDdd;
	}

	public void setNrDdd(String nrDdd) {
		this.nrDdd = nrDdd;
	}

	public String getNrTelefone() {
		return nrTelefone;
	}

	public void setNrTelefone(String nrTelefone) {
		this.nrTelefone = nrTelefone;
	}

	public DomainValue getTpPlataforma() {
		return tpPlataforma;
	}

	public void setTpPlataforma(DomainValue tpPlataforma) {
		this.tpPlataforma = tpPlataforma;
	}

	public String getDsVersao() {
		return dsVersao;
	}

	public void setDsVersao(String dsVersao) {
		this.dsVersao = dsVersao;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idDispositivoContato",
				getIdDispositivoContato()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DispositivoContato))
			return false;
		DispositivoContato castOther = (DispositivoContato) other;
		return new EqualsBuilder().append(this.getIdDispositivoContato(),
				castOther.getIdDispositivoContato()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdDispositivoContato())
				.toHashCode();
	}

}
