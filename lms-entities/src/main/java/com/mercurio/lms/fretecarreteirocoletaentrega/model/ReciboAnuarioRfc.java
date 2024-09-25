package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "RECIBO_ANUARIO_RFC")
@SequenceGenerator(name = "RECIBO_ANUARIO_RFC_SQ", sequenceName = "RECIBO_ANUARIO_RFC_SQ", allocationSize = 1)
public class ReciboAnuarioRfc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIBO_ANUARIO_RFC_SQ")
	@Column(name = "ID_RECIBO_ANUARIO_RFC", nullable = false)
	private Long idReciboAnuarioRfc;
	
	
	@Column(name="ID_RECIBO_FRETE_CARRETEIRO", nullable = false, length = 10)
	private Long idReciboFreteCarreteiro;
	
	@Column(name="ID_PROPRIETARIO", nullable = false, length = 10)
	private Long idProprietario;

	
	@Column(name="ID_USUARIO_INCLUSAO", nullable = true, length = 10)
	private Long idUsuarioInclusao;
	
	@Column(name="ID_USUARIO_ALTERACAO", nullable = true, length = 10)
	private Long idUsuarioAleteracao;
	
	@Column(name = "VL_ACUMULADO")
	private BigDecimal vlAcumulado;

	@Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = true),	@Column(name = "DH_INCLUSAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;
	
	@Columns(columns = { @Column(name = "DH_ATUALIZACAO", nullable = true),	@Column(name = "DH_ATUALIZACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAtualizacao;

	public Long getIdReciboAnuarioRfc() {
		return idReciboAnuarioRfc;
	}

	public void setIdReciboAnuarioRfc(Long idReciboAnuarioRfc) {
		this.idReciboAnuarioRfc = idReciboAnuarioRfc;
	}

	public Long getIdReciboFreteCarreteiro() {
		return idReciboFreteCarreteiro;
	}

	public void setIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		this.idReciboFreteCarreteiro = idReciboFreteCarreteiro;
	}

	public Long getIdProprietario() {
		return idProprietario;
	}

	public void setIdProprietario(Long idProprietario) {
		this.idProprietario = idProprietario;
	}

	public Long getIdUsuarioInclusao() {
		return idUsuarioInclusao;
	}

	public void setIdUsuarioInclusao(Long idUsuarioInclusao) {
		this.idUsuarioInclusao = idUsuarioInclusao;
	}

	public Long getIdUsuarioAleteracao() {
		return idUsuarioAleteracao;
	}

	public void setIdUsuarioAleteracao(Long idUsuarioAleteracao) {
		this.idUsuarioAleteracao = idUsuarioAleteracao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhAtualizacao() {
		return dhAtualizacao;
	}

	public void setDhAtualizacao(DateTime dhAtualizacao) {
		this.dhAtualizacao = dhAtualizacao;
	}

	public BigDecimal getVlAcumulado() {
		return vlAcumulado;
	}

	public void setVlAcumulado(BigDecimal vlAcumulado) {
		this.vlAcumulado = vlAcumulado;
	}
}