package com.mercurio.lms.prestcontasciaaerea.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "FATURA_CIA_AEREA_ANEXO")
@SequenceGenerator(name = "FATURA_CIA_AEREA_ANEXO_SEQ", sequenceName = "FATURA_CIA_AEREA_ANEXO_SQ", allocationSize = 1)
public class FaturaCiaAereaAnexo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_FATURA_CIA_AEREA_ANEXO", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FATURA_CIA_AEREA_ANEXO_SEQ")
	private Long idFaturaCiaAereaAnexo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FATURA_CIA_AEREA", nullable = false)
	private FaturaCiaAerea faturaCiaAerea;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;
	
	@Column(name = "DS_ANEXO", nullable = false, length = 100)
	private String dsAnexo;
	
	@Lob
	@Column(name="DC_ARQUIVO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	private byte[] dcArquivo;
	
	@Columns(columns = { @Column(name = "DH_CRIACAO", nullable = true), @Column(name = "DH_CRIACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhCriacao;
	
	@Transient
	private String nmArquivo;
	
	@Transient
	private String nmUsuario;
	
	public String toString() {
		return new ToStringBuilder(this).append("idFaturaCiaAereaAnexo", getIdFaturaCiaAereaAnexo()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FaturaCiaAereaAnexo))
			return false;
		FaturaCiaAereaAnexo castOther = (FaturaCiaAereaAnexo) other;
		return new EqualsBuilder().append(this.getIdFaturaCiaAereaAnexo(), castOther.getIdFaturaCiaAereaAnexo()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdFaturaCiaAereaAnexo()).toHashCode();
	}

	public Long getIdFaturaCiaAereaAnexo() {
		return idFaturaCiaAereaAnexo;
	}

	public void setIdFaturaCiaAereaAnexo(Long idFaturaCiaAereaAnexo) {
		this.idFaturaCiaAereaAnexo = idFaturaCiaAereaAnexo;
	}

	public FaturaCiaAerea getFaturaCiaAerea() {
		return faturaCiaAerea;
	}

	public void setFaturaCiaAerea(FaturaCiaAerea faturaCiaAerea) {
		this.faturaCiaAerea = faturaCiaAerea;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}

	public String getNmUsuario() {
		return nmUsuario;
	}

	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}
}
