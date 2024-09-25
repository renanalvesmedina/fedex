package com.mercurio.lms.vendas.model;

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

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "USUARIO_CLIENTE_RESPONSAVEL")
@SequenceGenerator(name = "SQ_USUARIO_CLIENTE_RESPONSAVEL", sequenceName = "USUARIO_CLIENTE_RESPONSAVEL_SQ", allocationSize = 1)
public class UsuarioClienteResponsavel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_USUARIO_CLIENTE_RESPONSAVEL")
	@Column(name = "ID_USUARIO_CLIENTE_RESPONSAVEL", nullable = false)
	private Long idUsuarioClienteResponsavel;

	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE", nullable = false)
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;

	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DT_VIGENCIA_FINAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;

	public String toString() {
		return new ToStringBuilder(this).append("idUsuarioClienteResponsavel",
				getIdUsuarioClienteResponsavel()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof UsuarioClienteResponsavel))
			return false;
		UsuarioClienteResponsavel castOther = (UsuarioClienteResponsavel) other;
		return new EqualsBuilder().append(
				this.getIdUsuarioClienteResponsavel(),
				castOther.getIdUsuarioClienteResponsavel()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdUsuarioClienteResponsavel())
				.toHashCode();
	}

	/**
	 * @return the idUsuarioClienteResponsavel
	 */
	public Long getIdUsuarioClienteResponsavel() {
		return idUsuarioClienteResponsavel;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @return the usuario
	 */
	public UsuarioLMS getUsuario() {
		return usuario;
	}

	/**
	 * @return the dtVigenciaInicial
	 */
	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	/**
	 * @return the dtVigenciaFinal
	 */
	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	/**
	 * @param idUsuarioClienteResponsavel the idUsuarioClienteResponsavel to set
	 */
	public void setIdUsuarioClienteResponsavel(Long idUsuarioClienteResponsavel) {
		this.idUsuarioClienteResponsavel = idUsuarioClienteResponsavel;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	/**
	 * @param dtVigenciaInicial the dtVigenciaInicial to set
	 */
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	/**
	 * @param dtVigenciaFinal the dtVigenciaFinal to set
	 */
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

}
