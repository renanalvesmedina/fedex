package com.mercurio.lms.contasreceber.model;

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

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "FATURA_CLOUD")
@SequenceGenerator(name = "FATURA_CLOUD_SEQ", sequenceName = "FATURA_CLOUD_SQ", allocationSize = 1)
public class FaturaCloud implements Serializable {
	private static final long serialVersionUID = 8332191180294698820L;

	@Id
	@Column(name = "ID_FATURA_CLOUD", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FATURA_CLOUD_SEQ")
	private Long idFaturaCloud;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FATURA", nullable = false)
	private Fatura fatura;
	
	@Column(name = "DS_LINK", nullable = false)
	private String dsLink;
	
	@Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = false), @Column(name = "DH_INCLUSAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;
	
	@Columns(columns = { @Column(name = "DH_ALTERACAO", nullable = false), @Column(name = "DH_ALTERACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO", nullable = false)
	private UsuarioLMS usuarioInclusao;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO", nullable = false)
	private UsuarioLMS usuarioAlteracao;


	/**
	 * 
	 */
	public FaturaCloud() {
		super();
	}

	public FaturaCloud(Long idFaturaCloud, Fatura fatura, String dsLink, DateTime dhInclusao, DateTime dhAlteracao,
			UsuarioLMS usuarioInclusao, UsuarioLMS usuarioAlteracao) {
		super();
		this.idFaturaCloud = idFaturaCloud;
		this.fatura = fatura;
		this.dsLink = dsLink;
		this.dhInclusao = dhInclusao;
		this.dhAlteracao = dhAlteracao;
		this.usuarioInclusao = usuarioInclusao;
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Long getIdFaturaCloud() {
		return idFaturaCloud;
	}

	public void setIdFaturaCloud(Long idFaturaCloud) {
		this.idFaturaCloud = idFaturaCloud;
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public String getDsLink() {
		return dsLink;
	}

	public void setDsLink(String dsLink) {
		this.dsLink = dsLink;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public UsuarioLMS getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(UsuarioLMS usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public UsuarioLMS getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioLMS usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idFaturaCloud == null) ? 0 : idFaturaCloud.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
			
		}
		if (!(obj instanceof FaturaCloud)) {
			return false;
			
		}
		FaturaCloud oFc = (FaturaCloud) obj;
		if (!this.idFaturaCloud.equals(oFc.idFaturaCloud)) {
			return false;
			
		}
		return true;

	}

}
