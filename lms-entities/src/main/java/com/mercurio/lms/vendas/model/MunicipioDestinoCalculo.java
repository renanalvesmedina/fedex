package com.mercurio.lms.vendas.model;

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

import com.mercurio.lms.municipios.model.Municipio;

/**
 * Desvio para município de destino
 *
 * @author Inacio G Klassmann
 * @since 29/11/11
 */

@Entity
@Table(name = "MUNICIPIO_DESTINO_CALCULO")
@SequenceGenerator(name = "MUNICIPIO_DESTINO_CALCULO_SEQ", sequenceName = "MUNICIPIO_DESTINO_CALCULO_SQ")
public class MunicipioDestinoCalculo implements Serializable {

	private static final long serialVersionUID = -689536174915831195L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MUNICIPIO_DESTINO_CALCULO_SEQ")
	@Column(name = "ID_MUNICIPIO_DESTINO_CALCULO", nullable = false)
	private Long idMunicipioDestinoCalculo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_MUNICIPIO_DESTINO", nullable = false)
	private Municipio municipioDestino;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_MUNICIPIO_ORIGINAL", nullable = false)
	private Municipio municipioOriginal;

	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DT_VIGENCIA_FINAL", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;


	public Long getIdMunicipioDestinoCalculo() {
		return idMunicipioDestinoCalculo;
	}

	public void setIdMunicipioDestinoCalculo(Long idMunicipioDestinoCalculo) {
		this.idMunicipioDestinoCalculo = idMunicipioDestinoCalculo;
	}

	public Municipio getMunicipioDestino() {
		return municipioDestino;
	}

	public void setMunicipioDestino(Municipio municipioDestino) {
		this.municipioDestino = municipioDestino;
	}

	public Municipio getMunicipioOriginal() {
		return municipioOriginal;
	}

	public void setMunicipioOriginal(Municipio municipioOriginal) {
		this.municipioOriginal = municipioOriginal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

}
