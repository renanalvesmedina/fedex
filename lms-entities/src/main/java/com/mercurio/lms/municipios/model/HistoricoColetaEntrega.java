package com.mercurio.lms.municipios.model;

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

import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;

@Entity
@Table(name = "HISTORICO_COLETA_ENTREGA")
@SequenceGenerator(name = "HISTORICO_COLETA_ENTREGA_SQ", sequenceName = "HISTORICO_COLETA_ENTREGA_SQ", allocationSize = 1)
public class HistoricoColetaEntrega implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_HISTORICO_COLETA_ENTREGA", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HISTORICO_COLETA_ENTREGA_SQ")
	private Long idHistoricoColetaEntrega;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	private Filial filial;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioADSM usuario;

	@Column(name = "NR_ROTA", length = 3, nullable = false)
	private Short nrRota;

	@Column(name = "DS_ROTA", length = 120, nullable = false)
	private String dsRota;

	@Column(name = "NR_KM", length = 6, nullable = false)
	private Integer nrKm;

	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DH_ATUALIZACAO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dhAtualizacao;

	@Column(name = "DT_VIGENCIA_FINAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;
	
	@Column(name = "DS_SAIDAS_PREVISTAS", length = 120, nullable = false)
	private String dsSaidasPrevistas;

	@Column(name = "NR_ROTA_ANT", length = 3, nullable = true)
	private Short nrRotaAnt;

	public Long getIdHistoricoColetaEntrega() {
		return idHistoricoColetaEntrega;
	}

	public void setIdHistoricoColetaEntrega(Long idHistoricoColetaEntrega) {
		this.idHistoricoColetaEntrega = idHistoricoColetaEntrega;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public UsuarioADSM getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioADSM usuario) {
		this.usuario = usuario;
	}

	public Short getNrRota() {
		return nrRota;
	}

	public void setNrRota(Short nrRota) {
		this.nrRota = nrRota;
	}

	public String getDsRota() {
		return dsRota;
	}

	public void setDsRota(String dsRota) {
		this.dsRota = dsRota;
	}

	public Integer getNrKm() {
		return nrKm;
	}

	public void setNrKm(Integer nrKm) {
		this.nrKm = nrKm;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDhAtualizacao() {
		return dhAtualizacao;
	}

	public void setDhAtualizacao(YearMonthDay dhAtualizacao) {
		this.dhAtualizacao = dhAtualizacao;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public String getDsSaidasPrevistas() {
		return dsSaidasPrevistas;
	}

	public void setDsSaidasPrevistas(String dsSaidasPrevistas) {
		this.dsSaidasPrevistas = dsSaidasPrevistas;
	}

	public void setNrRotaAnt(Short nrRotaAnt) {
		this.nrRotaAnt = nrRotaAnt;
	}

	public Short getNrRotaAnt() {
		return nrRotaAnt;
	}

}