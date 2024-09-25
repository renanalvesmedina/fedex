package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;


/**
 * LMS-6885 - Entidade para Solicitação de Viatura da Escolta.
 * 
 * @author romulo.panassolo@cwi.com.br (Rômulo da Silva Panassolo)
 *
 */

@Entity
@Table(name = "SOLIC_ESCOLTA_VIATURA")
@SequenceGenerator(name = "SOLIC_ESCOLTA_VIATURA_SQ", sequenceName = "SOLIC_ESCOLTA_VIATURA_SQ")
public class SolicEscoltaViatura implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "ID_SOLIC_ESCOLTA_VIATURA", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOLIC_ESCOLTA_VIATURA_SQ")
	private Long idSolicEscoltaViatura;
		
	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_ESCOLTA")
	private SolicitacaoEscolta solicitacaoEscolta;
	
	@Column(name = "DS_PLACA_VIATURA", length=20)
	private String dsPlacaViatura;
	
	@Column(name = "NM_AGENTE_ESCOLTA_1", length=100)
	private String nmAgenteEscolta1;
	
	@Column(name = "NM_AGENTE_ESCOLTA_2", length=100)
	private String nmAgenteEscolta2;
	
	@Column(name = "DS_CONTATO_AGENTE", length=50)
	private String dsContatoAgente;
	
	@Column(name = "NR_KM_INICIAL_HODOMETRO")
	private Long nrKmInicialHodometro;
	
	@Column(name = "NR_KM_FINAL_HODOMETRO")
	private Long nrKmFinalHodometro;
	
	@Column(name = "NR_KM_REAL")
	private Long nrKmReal;
	
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	@Columns(columns = {
			@Column(name = "DH_REAL_CHEGADA", nullable = false),
			@Column(name = "DH_REAL_CHEGADA_TZR", nullable = false)
	})	
	private DateTime dhRealChegada;
	
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	@Columns(columns = {
			@Column(name = "DH_REAL_INICIO_MISSAO", nullable = false),
			@Column(name = "DH_REAL_INICIO_MISSAO_TZR", nullable = false)
	})	
	private DateTime dhRealInicioMissao;
	
	
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	@Columns(columns = {
			@Column(name = "DH_REAL_FIM_MISSAO", nullable = false),
			@Column(name = "DH_REAL_FIM_MISSAO_TZR", nullable = false)
	})	
	private DateTime dhRealFimMissao;
	
	@Column(name = "NR_TEMPO_VIAGEM_REAL_MIN")
	private Long nrTempoViagemRealMin;
	
	public Long getIdSolicEscoltaViatura() {
		return idSolicEscoltaViatura;
	}

	public void setIdSolicEscoltaViatura(Long idSolicEscoltaViatura) {
		this.idSolicEscoltaViatura = idSolicEscoltaViatura;
	}

	public SolicitacaoEscolta getSolicitacaoEscolta() {
		return solicitacaoEscolta;
	}

	public void setSolicitacaoEscolta(SolicitacaoEscolta solicitacaoEscolta) {
		this.solicitacaoEscolta = solicitacaoEscolta;
	}

	public String getDsPlacaViatura() {
		return dsPlacaViatura;
	}

	public void setDsPlacaViatura(String dsPlacaViatura) {
		this.dsPlacaViatura = dsPlacaViatura;
	}

	public String getNmAgenteEscolta1() {
		return nmAgenteEscolta1;
	}

	public void setNmAgenteEscolta1(String nmAgenteEscolta1) {
		this.nmAgenteEscolta1 = nmAgenteEscolta1;
	}

	public String getNmAgenteEscolta2() {
		return nmAgenteEscolta2;
	}

	public void setNmAgenteEscolta2(String nmAgenteEscolta2) {
		this.nmAgenteEscolta2 = nmAgenteEscolta2;
	}

	public String getDsContatoAgente() {
		return dsContatoAgente;
	}

	public void setDsContatoAgente(String dsContatoAgente) {
		this.dsContatoAgente = dsContatoAgente;
	}

	public Long getNrKmInicialHodometro() {
		return nrKmInicialHodometro;
	}

	public void setNrKmInicialHodometro(Long nrKmInicialHodometro) {
		this.nrKmInicialHodometro = nrKmInicialHodometro;
	}

	public Long getNrKmFinalHodometro() {
		return nrKmFinalHodometro;
	}

	public void setNrKmFinalHodometro(Long nrKmFinalHodometro) {
		this.nrKmFinalHodometro = nrKmFinalHodometro;
	}

	public Long getNrKmReal() {
		return nrKmReal;
	}

	public void setNrKmReal(Long nrKmReal) {
		this.nrKmReal = nrKmReal;
	}
	
	public DateTime getDhRealChegada() {
		return dhRealChegada;
	}

	public void setDhRealChegada(DateTime dhRealChegada) {
		this.dhRealChegada = dhRealChegada;
	}

	public DateTime getDhRealInicioMissao() {
		return dhRealInicioMissao;
	}

	public void setDhRealInicioMissao(DateTime dhRealInicioMissao) {
		this.dhRealInicioMissao = dhRealInicioMissao;
	}

	public DateTime getDhRealFimMissao() {
		return dhRealFimMissao;
	}

	public void setDhRealFimMissao(DateTime dhRealFimMissao) {
		this.dhRealFimMissao = dhRealFimMissao;
	}

	public Long getNrTempoViagemRealMin() {
		return nrTempoViagemRealMin;
	}

	public void setNrTempoViagemRealMin(Long nrTempoViagemRealMin) {
		this.nrTempoViagemRealMin = nrTempoViagemRealMin;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idSolicEscoltaViatura)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof SolicEscoltaViatura)) {
			return false;
		}
		SolicEscoltaViatura cast = (SolicEscoltaViatura) other;
		return new EqualsBuilder()
				.append(idSolicEscoltaViatura, cast.idSolicEscoltaViatura)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idSolicEscoltaViatura)
				.toHashCode();
	}

}
