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
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "FORNEC_ESCOLTA_IMPEDIDO")
@SequenceGenerator(name = "FORNEC_ESCOLTA_IMPEDIDO_SQ", sequenceName = "FORNEC_ESCOLTA_IMPEDIDO_SQ")
public class FornecedorEscoltaImpedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_FORNEC_ESCOLTA_IMPEDIDO", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORNEC_ESCOLTA_IMPEDIDO_SQ")
	private Long idFornecedorEscoltaImpedido;

	@ManyToOne
	@JoinColumn(name = "ID_FORNECEDOR_ESCOLTA", nullable = false)
	private FornecedorEscolta fornecedorEscolta;

	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE", nullable = false)
	private Cliente cliente;

	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DT_VIGENCIA_FINAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;

	public Long getIdFornecedorEscoltaImpedido() {
		return idFornecedorEscoltaImpedido;
	}

	public void setIdFornecedorEscoltaImpedido(Long idFornecedorEscoltaImpedido) {
		this.idFornecedorEscoltaImpedido = idFornecedorEscoltaImpedido;
	}

	public FornecedorEscolta getFornecedorEscolta() {
		return fornecedorEscolta;
	}

	public void setFornecedorEscolta(FornecedorEscolta fornecedorEscolta) {
		this.fornecedorEscolta = fornecedorEscolta;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(idFornecedorEscoltaImpedido)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof FornecedorEscoltaImpedido)) {
			return false;
		}
		FornecedorEscoltaImpedido cast = (FornecedorEscoltaImpedido) other;
		return new EqualsBuilder()
				.append(idFornecedorEscoltaImpedido, cast.idFornecedorEscoltaImpedido)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(idFornecedorEscoltaImpedido)
				.toString();
	}

}
