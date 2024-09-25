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
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

/**
 * Bloqueios de faturamento
 *
 * @author Inacio G Klassmann
 * @since 27/10/11
 */

@Entity
@Table(name = "BLOQUEIO_FATURAMENTO")
@SequenceGenerator(name = "BLOQUEIO_FATURAMENTO_SEQ", sequenceName = "BLOQUEIO_FATURAMENTO_SQ")
public class BloqueioFaturamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BLOQUEIO_FATURAMENTO_SEQ")
	@Column(name = "ID_BLOQUEIO_FATURAMENTO", nullable = false)
	private Long idBloqueioFaturamento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DEVEDOR_DOC_SERV_FAT", nullable = false)
	private DevedorDocServFat devedorDocServFat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_DESBLOQUEIO", nullable = true)
	private UsuarioLMS usuarioDesbloqueio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_BLOQUEIO", nullable = false)
	private UsuarioLMS usuarioBloqueio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MOTIVO_OCORRENCIA", nullable = false)
	private MotivoOcorrencia motivoOcorrencia;

	@Column(name = "DS_BLOQUEIO", length = 500, nullable = true)
	private String dsBloqueio;

	@Columns(columns = { @Column(name = "DH_BLOQUEIO", nullable = false),
			@Column(name = "DH_BLOQUEIO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhBloqueio;

	@Columns(columns = { @Column(name = "DH_DESBLOQUEIO", nullable = true),
			@Column(name = "DH_DESBLOQUEIO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhDesbloqueio;

	@Column(name = "DT_PREVISAO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtPrevisao;

	public Long getIdBloqueioFaturamento() {
		return idBloqueioFaturamento;
	}

	public void setIdBloqueioFaturamento(Long idBloqueioFaturamento) {
		this.idBloqueioFaturamento = idBloqueioFaturamento;
	}

	public DevedorDocServFat getDevedorDocServFat() {
		return devedorDocServFat;
	}

	public void setDevedorDocServFat(DevedorDocServFat devedorDocServFat) {
		this.devedorDocServFat = devedorDocServFat;
	}

	public UsuarioLMS getUsuarioDesbloqueio() {
		return usuarioDesbloqueio;
	}

	public void setUsuarioDesbloqueio(UsuarioLMS usuarioDesbloqueio) {
		this.usuarioDesbloqueio = usuarioDesbloqueio;
	}

	public UsuarioLMS getUsuarioBloqueio() {
		return usuarioBloqueio;
	}

	public void setUsuarioBloqueio(UsuarioLMS usuarioBloqueio) {
		this.usuarioBloqueio = usuarioBloqueio;
	}

	public MotivoOcorrencia getMotivoOcorrencia() {
		return motivoOcorrencia;
	}

	public void setMotivoOcorrencia(MotivoOcorrencia motivoOcorrencia) {
		this.motivoOcorrencia = motivoOcorrencia;
	}

	public String getDsBloqueio() {
		return dsBloqueio;
	}

	public void setDsBloqueio(String dsBloqueio) {
		this.dsBloqueio = dsBloqueio;
	}

	public DateTime getDhBloqueio() {
		return dhBloqueio;
	}

	public void setDhBloqueio(DateTime dhBloqueio) {
		this.dhBloqueio = dhBloqueio;
	}

	public DateTime getDhDesbloqueio() {
		return dhDesbloqueio;
	}

	public void setDhDesbloqueio(DateTime dhDesbloqueio) {
		this.dhDesbloqueio = dhDesbloqueio;
	}

	public YearMonthDay getDtPrevisao() {
		return dtPrevisao;
	}

	public void setDtPrevisao(YearMonthDay dtPrevisao) {
		this.dtPrevisao = dtPrevisao;
	}
}
