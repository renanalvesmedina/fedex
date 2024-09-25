package com.mercurio.lms.vol.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name="UPTIME_CONEXAO")
@SequenceGenerator(name="UPTIME_CONEXAO_SEQ", sequenceName="UPTIME_CONEXAO_SQ", allocationSize=1)
public class VolUptimeConexao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long idUptimeConexao;
	private MeioTransporte meioTransporte;
	private Filial filial;
	private VolEquipamentos volEquipamentos;
	private DateTime dhInicioChamada;
	private Long httpCode;
	private Long tempoExecucao;
	private String latitude;
	private String longitude;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UPTIME_CONEXAO_SEQ")
	@Column(name="ID_UPTIME_CONEXAO", nullable=false)
	public Long getIdUptimeConexao() {
		return idUptimeConexao;
	}
	public void setIdUptimeConexao(Long idUptimeConexao) {
		this.idUptimeConexao = idUptimeConexao;
	}
	
	@OneToOne
	@JoinColumn(name="ID_MEIO_TRANSPORTE", nullable=false)
	public MeioTransporte getMeioTransporte() {
		return meioTransporte;
	}
	public void setMeioTransporte(MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
	}
	
	@OneToOne
	@JoinColumn(name="ID_FILIAL", nullable=false)
	public Filial getFilial() {
		return filial;
	}
	public void setFilial(Filial filial) {
		this.filial = filial;
	}
	
	@OneToOne
	@JoinColumn(name="ID_EQUIPAMENTO", nullable=false)
	public VolEquipamentos getVolEquipamentos() {
		return volEquipamentos;
	}
	public void setVolEquipamentos(VolEquipamentos volEquipamentos) {
		this.volEquipamentos = volEquipamentos;
	}
	
	@Columns(columns = { @Column(name = "DH_INICIO_CHAMADA", nullable = false),
			@Column(name = "DH_INICIO_CHAMADA_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhInicioChamada() {
		return dhInicioChamada;
	}
	public void setDhInicioChamada(DateTime dhInicioChamada) {
		this.dhInicioChamada = dhInicioChamada;
	}
	
	@Column(name="HTTP_CODE", nullable=false)
	public Long getHttpCode() {
		return httpCode;
	}
	public void setHttpCode(Long httpCode) {
		this.httpCode = httpCode;
	}
	
	@Column(name="TEMPO_EXECUCAO", nullable=false)
	public Long getTempoExecucao() {
		return tempoExecucao;
	}
	public void setTempoExecucao(Long tempoExecucao) {
		this.tempoExecucao = tempoExecucao;
	}
	@Column(name="LATITUDE")
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	@Column(name="LONGITUDE")
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
}
