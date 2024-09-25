package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;

@Entity
@Table(name="VOLUME_SOBRA")
@SequenceGenerator(name="VOLUME_SOBRA_SQ", sequenceName="VOLUME_SOBRA_SQ", allocationSize=1)
public class VolumeSobra implements Serializable {
	private static final long serialVersionUID = 1L;                                                                                                                                                                                 
	
	private Long idVolumeSobra;
	private CarregamentoDescarga carregamentoDescarga;
	private VolumeNotaFiscal volumeNotaFiscal;
	private UsuarioLMS usuario;
	private DateTime dhOperacao;

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOLUME_SOBRA_SQ")
	@Column(name = "ID_VOLUME_SOBRA", nullable = false)
	public Long getIdVolumeSobra() {
		return idVolumeSobra;
	}

	public void setIdVolumeSobra(Long idVolumeSobra) {
		this.idVolumeSobra = idVolumeSobra;
	}
	
	@OneToOne
	@JoinColumn(name = "ID_CARREGAMENTO_DESCARGA", nullable = false)
	public CarregamentoDescarga getCarregamentoDescarga() {
		return carregamentoDescarga;
	}

	public void setCarregamentoDescarga(CarregamentoDescarga carregamentoDescarga) {
		this.carregamentoDescarga = carregamentoDescarga;
	}

	@OneToOne
	@JoinColumn(name = "ID_VOLUME_NOTA_FISCAL", nullable = false)
	public VolumeNotaFiscal getVolumeNotaFiscal() {
		return volumeNotaFiscal;
	}

	public void setVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal) {
		this.volumeNotaFiscal = volumeNotaFiscal;
	}

	@OneToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}
	
	@Columns(columns = { @Column(name = "DH_OPERACAO", nullable = false),
			@Column(name = "DH_OPERACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhOperacao() {
		return dhOperacao;
	}
	public void setDhOperacao(DateTime dhOperacao) {
		this.dhOperacao = dhOperacao;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
