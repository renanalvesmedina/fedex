package com.mercurio.lms.expedicao.model;

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
import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name = "REEMISSAO_ETIQUETA_VOLUME")
@SequenceGenerator(name = "SQ_REEMISSAO_ETIQUETA_VOLUME", sequenceName = "REEMISSAO_ETIQUETA_VOLUME_SQ", allocationSize = 1)
public class ReemissaoEtiquetaVolume implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long idReemissaoEtiquetaVolume;
	private VolumeNotaFiscal volumeNotaFiscal;
	private DateTime dhReemissao;
	private String dsMac;
	private UsuarioLMS usuario;
	private Filial filial;

	public ReemissaoEtiquetaVolume() {
		
	}

	@Columns(columns = { @Column(name = "DH_REEMISSAO", nullable = false), @Column(name = "DH_REEMISSAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhReemissao() {
		return dhReemissao;
	}

	@Column(name = "DS_MAC", nullable = false, length = 50)
	public String getDsMac() {
		return dsMac;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	public Filial getFilial() {
		return filial;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_REEMISSAO_ETIQUETA_VOLUME")
	@Column(name = "ID_REEMISSAO_ETIQUETA_VOLUME", nullable = false)
	public Long getIdReemissaoEtiquetaVolume() {
		return idReemissaoEtiquetaVolume;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	public UsuarioLMS getUsuario() {
		return usuario;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_VOLUME_NOTA_FISCAL", nullable = false)
	public VolumeNotaFiscal getVolumeNotaFiscal() {
		return volumeNotaFiscal;
	}

	public void setDhReemissao(DateTime dhReemissao) {
		this.dhReemissao = dhReemissao;
	}

	public void setDsMac(String dsMac) {
		this.dsMac = dsMac;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public void setIdReemissaoEtiquetaVolume(Long idReemissaoEtiquetaVolume) {
		this.idReemissaoEtiquetaVolume = idReemissaoEtiquetaVolume;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public void setVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal) {
		this.volumeNotaFiscal = volumeNotaFiscal;
	}
}
