package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "ANEXO_DESCONTO_RFC")
@SequenceGenerator(name = "ANEXO_DESCONTO_RFC_SQ", sequenceName = "ANEXO_DESCONTO_RFC_SQ", allocationSize = 1)
public class AnexoDescontoRfc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANEXO_DESCONTO_RFC_SQ")
	@Column(name = "ID_ANEXO_DESCONTO_RFC", nullable = false)
	private Long idAnexoDescontoRfc;

	@ManyToOne
	@JoinColumn(name = "ID_DESCONTO_RFC", nullable = false)
	private DescontoRfc descontoRfc;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuarioLMS;

	@Column(name = "DS_ANEXO", nullable = false, length = 250)
	private String dsAnexo;

	@Formula("TRIM(UTL_RAW.CAST_TO_VARCHAR2(DBMS_LOB.SUBSTR(dc_arquivo, 1024)))")
	private String nmArquivo;

	@Columns(columns = { @Column(name = "DH_CRIACAO", nullable = false),
						 @Column(name = "DH_CRIACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhCriacao;

	@Column(name = "DH_CRIACAO_TZR", nullable = false, insertable = false, updatable = false)
	private String dhCriacaoTimeZone;

	@Lob
	@Column(name = "DC_ARQUIVO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	private byte[] dcArquivo;

	public Long getIdAnexoDescontoRfc() {
		return idAnexoDescontoRfc;
	}

	public void setIdAnexoDescontoRfc(Long idAnexoDescontoRfc) {
		this.idAnexoDescontoRfc = idAnexoDescontoRfc;
	}

	public DescontoRfc getDescontoRfc() {
		return descontoRfc;
	}

	public void setDescontoRfc(DescontoRfc descontoRfc) {
		this.descontoRfc = descontoRfc;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}

	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}

	public String getDhCriacaoTimeZone() {
		return dhCriacaoTimeZone;
	}

	public void setDhCriacaoTimeZone(String dhCriacaoTimeZone) {
		this.dhCriacaoTimeZone = dhCriacaoTimeZone;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idAnexoDescontoRfc",
				getIdAnexoDescontoRfc()).toString();
	}

	public UsuarioLMS getUsuarioLMS() {
		return usuarioLMS;
	}

	public void setUsuarioLMS(UsuarioLMS usuarioLMS) {
		this.usuarioLMS = usuarioLMS;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idAnexoDescontoRfc == null) ? 0 : idAnexoDescontoRfc
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AnexoDescontoRfc)) {
			return false;
		}

		AnexoDescontoRfc castOther = (AnexoDescontoRfc) obj;

		return new EqualsBuilder().append(this.getIdAnexoDescontoRfc(),
				castOther.getIdAnexoDescontoRfc()).isEquals();
	}
}