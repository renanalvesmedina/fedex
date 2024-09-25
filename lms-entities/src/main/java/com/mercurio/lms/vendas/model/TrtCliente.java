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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.workflow.model.Pendencia;

@Entity
@Table(name="TRT_CLIENTE")
@SequenceGenerator(name="SQ_TRT_CLIENTE", sequenceName="TRT_CLIENTE_SQ", allocationSize=1)
public class TrtCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_TRT_CLIENTE")
	@Column(name="ID_TRT_CLIENTE", nullable=false)
	private Long idTrtCliente;	

	@ManyToOne
	@JoinColumn(name="ID_CLIENTE", nullable=true)
	private Cliente cliente;
	
	@Column(name="ID_TABELA_PRECO", nullable=true)
	private Long idTabelaPreco;

	@Column(name = "DT_VIGENCIA_INICIAL", nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;
	
	@Column(name = "DT_VIGENCIA_INICIAL_SOLICITADA")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicialSolicitada;

	@Column(name = "DT_VIGENCIA_FINAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;
	
	@Column(name = "DT_VIGENCIA_FINAL_SOLICITADA")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinalSolicitada;	
	
	@Column(name = "TP_SITUACAO_APROVACAO ", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_STATUS_WORKFLOW") })
	private DomainValue tpSituacaoAprovacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TRT_CLIENTE_ORIGINAL")
	private TrtCliente trtClienteOriginal;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PENDENCIA")
	private Pendencia pendencia;
	
	@Transient
	private String dsMotivoSolicitacao;

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idTrtCliente",
				getIdTrtCliente()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TrtCliente))
			return false;
		TrtCliente castOther = (TrtCliente) other;
		return new EqualsBuilder().append(this.getIdTrtCliente(),
				castOther.getIdTrtCliente()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdTrtCliente()).toHashCode();
	}

	public Long getIdTrtCliente() {
		return idTrtCliente;
	}

	public void setIdTrtCliente(Long idTrtCliente) {
		this.idTrtCliente = idTrtCliente;
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

	public YearMonthDay getDtVigenciaInicialSolicitada() {
		return dtVigenciaInicialSolicitada;
	}

	public void setDtVigenciaInicialSolicitada(YearMonthDay dtVigenciaInicialSolicitada) {
		this.dtVigenciaInicialSolicitada = dtVigenciaInicialSolicitada;
	}

	public YearMonthDay getDtVigenciaFinalSolicitada() {
		return dtVigenciaFinalSolicitada;
	}

	public void setDtVigenciaFinalSolicitada(YearMonthDay dtVigenciaFinalSolicitada) {
		this.dtVigenciaFinalSolicitada = dtVigenciaFinalSolicitada;
	}

	public String getDsMotivoSolicitacao() {
		return dsMotivoSolicitacao;
	}

	public void setDsMotivoSolicitacao(String dsMotivoSolicitacao) {
		this.dsMotivoSolicitacao = dsMotivoSolicitacao;
	}

	public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public TrtCliente getTrtClienteOriginal() {
		return trtClienteOriginal;
	}

	public void setTrtClienteOriginal(TrtCliente trtClienteOriginal) {
		this.trtClienteOriginal = trtClienteOriginal;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public Long getIdTabelaPreco() {
		return idTabelaPreco;
	}

	public void setIdTabelaPreco(Long idTabelaPreco) {
		this.idTabelaPreco = idTabelaPreco;
	}
}
