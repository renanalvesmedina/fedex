package com.mercurio.lms.tributos.model;

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

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "INSCRICAO_ESTADUAL_COLETIVA")
public class InscricaoEstadualColetiva implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "INSCRICAO_ESTADUAL_COLETIVA_SEQ", sequenceName = "INSCRICAO_ESTADUAL_COLETIVA_SQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSCRICAO_ESTADUAL_COLETIVA_SEQ")
	@Column(name = "ID_INSCRICAO_ESTADUAL_COLETIVA", nullable = false)
	private Long idInscricaoEstadualColetiva;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE", nullable = false)
	private Cliente cliente;                    
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UNIDADE_FEDERATIVA", nullable = false)
	private UnidadeFederativa unidadeFederativa;
	
	@Column(name = "NR_INSCRICAO_ESTADUAL_COLETIVA", length = 20, nullable = true)
	private String nrInscricaoEstadualColetiva;
	
	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayNotNullUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DT_VIGENCIA_FINAL", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayNotNullUserType")
	private YearMonthDay dtVigenciaFinal;

	public Long getIdInscricaoEstadualColetiva() {
		return idInscricaoEstadualColetiva;
	}

	public void setIdInscricaoEstadualColetiva(Long idInscricaoEstadualColetiva) {
		this.idInscricaoEstadualColetiva = idInscricaoEstadualColetiva;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public String getNrInscricaoEstadualColetiva() {
		return nrInscricaoEstadualColetiva;
	}

	public void setNrInscricaoEstadualColetiva(String nrInscricaoEstadualColetiva) {
		this.nrInscricaoEstadualColetiva = nrInscricaoEstadualColetiva;
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

	public InscricaoEstadualColetiva fillByMap(TypedFlatMap data) {
		this.setIdInscricaoEstadualColetiva(data.getLong("idInscricaoEstadualColetiva"));
		Long idCliente = data.getLong("idCliente");
		this.setCliente(null);
		if(idCliente != null) {
			Cliente cliente = new Cliente();
			cliente.setIdCliente(idCliente);
			this.setCliente(cliente);
		}
		this.setUnidadeFederativa(null);
		Long idUnidadeFederativa = data.getLong("idUnidadeFederativa");
		if(idUnidadeFederativa != null) {
			UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
			unidadeFederativa.setIdUnidadeFederativa(idUnidadeFederativa);
			this.setUnidadeFederativa(unidadeFederativa);
		}
		this.setNrInscricaoEstadualColetiva(data.getString("nrInscricaoEstadual"));
		this.setDtVigenciaInicial(data.getYearMonthDay("periodoInicial"));
		this.setDtVigenciaFinal(data.getYearMonthDay("periodoFinal"));
		return this;
	}

}
