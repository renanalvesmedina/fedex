package com.mercurio.lms.seguros.model;

import java.io.Serializable;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "AVERBACAO_ANEXO")
@SequenceGenerator(name = "AVERBACAO_ANEXO_SQ", sequenceName = "AVERBACAO_ANEXO_SQ")
public class AverbacaoAnexo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AVERBACAO_ANEXO_SQ")
	@Column(name = "ID_AVERBACAO_ANEXO", nullable = false)
	private Long idAverbacaoAnexo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_AVERBACAO", nullable = false)
	private Averbacao averbacao;
	
	@Lob
	@Column(name = "DC_ARQUIVO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	private byte[] dcArquivo;
	
	@Columns(columns = { @Column(name = "DH_CRIACAO", nullable = false),
			@Column(name = "DH_CRIACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhCriacao;
		
	@Column(name = "DS_ANEXO", length = 100, nullable = false)
	private String dsAnexo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "ID_USUARIO_CRIACAO", referencedColumnName="ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;	
		
	public Long getIdAverbacaoAnexo() {
		return idAverbacaoAnexo;
	}

	public void setIdAverbacaoAnexo(Long idAverbacaoAnexo) {
		this.idAverbacaoAnexo = idAverbacaoAnexo;
	}

	public Averbacao getAverbacao() {
		return averbacao;
	}

	public void setAverbacao(Averbacao averbacao) {
		this.averbacao = averbacao;
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

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AverbacaoAnexo))
			return false;
		AverbacaoAnexo castOther = (AverbacaoAnexo) other;
		return new EqualsBuilder().append(this.getIdAverbacaoAnexo(),
				castOther.getIdAverbacaoAnexo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAverbacaoAnexo()).toHashCode();
    }
}
