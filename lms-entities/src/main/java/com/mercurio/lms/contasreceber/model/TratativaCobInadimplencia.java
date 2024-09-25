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
 * The persistent class for the TRATATIVA_COB_INADIMPLENCIA database table.
 * 
 */
@Entity
@Table(name="TRATATIVA_COB_INADIMPLENCIA")
public class TratativaCobInadimplencia implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_TRATATIVA_COB_INADIMPLENCIA")
    @SequenceGenerator(name = "TRATATIVA_COB_INADIMPLENCIA_SQ", sequenceName = "TRATATIVA_COB_INADIMPLENCIA_SQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRATATIVA_COB_INADIMPLENCIA_SQ")
	private Long idTratativaCobInadimplencia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_COBRANCA_INADIMPLENCIA", nullable = false)
	private CobrancaInadimplencia cobrancaInadimplencia;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_MOTIVO_INADIMPLENCIA", nullable = false)
	private MotivoInadimplencia motivoInadimplencia;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;
	
	@Columns(columns = { 
			@Column(name = "DH_TRATATIVA", nullable = false),
			@Column(name = "DH_TRATATIVA_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhTratativa;
	
	@Column(name="DS_PLANO_ACAO")
	private String dsPlanoAcao;

	@Column(name = "DT_PREV_SOLUCAO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtPrevSolucao;
	
	@Column(name="DS_PARECER_MATRIZ")
	private String dsParecerMatriz;

	public Long getIdTratativaCobInadimplencia() {
		return idTratativaCobInadimplencia;
	}

	public void setIdTratativaCobInadimplencia(Long idTratativaCobInadimplencia) {
		this.idTratativaCobInadimplencia = idTratativaCobInadimplencia;
	}

	public CobrancaInadimplencia getCobrancaInadimplencia() {
		return cobrancaInadimplencia;
	}

	public void setCobrancaInadimplencia(CobrancaInadimplencia cobrancaInadimplencia) {
		this.cobrancaInadimplencia = cobrancaInadimplencia;
	}

	public MotivoInadimplencia getMotivoInadimplencia() {
		return motivoInadimplencia;
	}

	public void setMotivoInadimplencia(MotivoInadimplencia motivoInadimplencia) {
		this.motivoInadimplencia = motivoInadimplencia;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public DateTime getDhTratativa() {
		return dhTratativa;
	}

	public void setDhTratativa(DateTime dhTratativa) {
		this.dhTratativa = dhTratativa;
	}

	public String getDsPlanoAcao() {
		return dsPlanoAcao;
	}

	public void setDsPlanoAcao(String dsPlanoAcao) {
		this.dsPlanoAcao = dsPlanoAcao;
	}

	public YearMonthDay getDtPrevSolucao() {
		return dtPrevSolucao;
	}

	public void setDtPrevSolucao(YearMonthDay dtPrevSolucao) {
		this.dtPrevSolucao = dtPrevSolucao;
	}

	public String getDsParecerMatriz() {
		return dsParecerMatriz;
	}

	public void setDsParecerMatriz(String dsParecerMatriz) {
		this.dsParecerMatriz = dsParecerMatriz;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}