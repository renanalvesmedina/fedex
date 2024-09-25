package com.mercurio.lms.expedicao.model;

import java.math.BigDecimal;

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
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;

@Entity
@Table(name = "CIOT")
@SequenceGenerator(name = "CIOT_SEQ", sequenceName = "CIOT_SQ", allocationSize = 1)
public class CIOT implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_CIOT", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CIOT_SEQ")
	private Long idCIOT;

	//FIXME REMOVER APÓS TESTES
	@Transient
	private ControleCarga controleCarga;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MEIO_TRANSPORTE", nullable = false)
	private MeioTransporte meioTransporte;

	@Column(name = "NR_CIOT", nullable = true)
	private Long nrCIOT;

	@Column(name = "TP_SITUACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_CIOT") })
	private DomainValue tpSituacao;

	@Column(name = "NR_COD_VERIFICADOR", nullable = true)
	private String nrCodigoVerificador;

	@Column(name = "VL_FRETE", nullable = true)
	private BigDecimal vlFrete;

	@Columns(columns = { @Column(name = "DH_GERACAO", nullable = true), @Column(name = "DH_GERACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhGeracao;

	@Columns(columns = { @Column(name = "DH_ENCERRAMENTO", nullable = true), @Column(name = "DH_ENCERRAMENTO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEncerramento;

	@Columns(columns = { @Column(name = "DH_CANCELAMENTO", nullable = true), @Column(name = "DH_CANCELAMENTO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhCancelamento;

	@Columns(columns = { @Column(name = "DH_ALTERACAO", nullable = true), @Column(name = "DH_ALTERACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;

	@Column(name = "DS_OBSERVACAO", nullable = true)
	private String dsObservacao;
	
	@Column(name = "NR_PROTOCOLO_ENCER", nullable = true)
	private String nrProtocoloEncerramento;
	
	@Column(name = "NR_PROTOCOLO_CANCEL", nullable = true)
	private String nrProtocoloCancelamento;

	public String toString() {
		return new ToStringBuilder(this).append("idCIOT", getIdCIOT()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CIOT))
			return false;
		CIOT castOther = (CIOT) other;
		return new EqualsBuilder().append(this.getIdCIOT(), castOther.getIdCIOT()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdCIOT()).toHashCode();
	}

	public Long getIdCIOT() {
		return idCIOT;
	}

	public void setIdCIOT(Long idCIOT) {
		this.idCIOT = idCIOT;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public MeioTransporte getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public Long getNrCIOT() {
		return nrCIOT;
	}

	public void setNrCIOT(Long nrCIOT) {
		this.nrCIOT = nrCIOT;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public String getNrCodigoVerificador() {
		return nrCodigoVerificador;
	}

	public void setNrCodigoVerificador(String nrCodigoVerificador) {
		this.nrCodigoVerificador = nrCodigoVerificador;
	}

	public BigDecimal getVlFrete() {
		return vlFrete;
	}

	public void setVlFrete(BigDecimal vlFrete) {
		this.vlFrete = vlFrete;
	}

	public DateTime getDhGeracao() {
		return dhGeracao;
	}

	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public DateTime getDhEncerramento() {
		return dhEncerramento;
	}

	public void setDhEncerramento(DateTime dhEncerramento) {
		this.dhEncerramento = dhEncerramento;
	}

	public DateTime getDhCancelamento() {
		return dhCancelamento;
	}

	public void setDhCancelamento(DateTime dhCancelamento) {
		this.dhCancelamento = dhCancelamento;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public String getNrProtocoloEncerramento() {
		return nrProtocoloEncerramento;
	}

	public void setNrProtocoloEncerramento(String nrProtocoloEncerramento) {
		this.nrProtocoloEncerramento = nrProtocoloEncerramento;
	}

	public String getNrProtocoloCancelamento() {
		return nrProtocoloCancelamento;
	}

	public void setNrProtocoloCancelamento(String nrProtocoloCancelamento) {
		this.nrProtocoloCancelamento = nrProtocoloCancelamento;
	}

}
