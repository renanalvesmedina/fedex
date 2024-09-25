package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.MotoristaControleCarga;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class LiberacaoReguladora implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idLiberacaoReguladora; 

	/** persistent field */
	private String nrLiberacao;

	/** nullable persistent field */
	private YearMonthDay dtLiberacao;

	/** nullable persistent field */
	private YearMonthDay dtVencimento;

	/** persistent field */
	private Usuario usuario;

	/** persistent field */
	private ReguladoraSeguro reguladoraSeguro;

	/** persistent field */
	private Motorista motorista;

	private MeioTransporte meioTransporte;

	/** persistent field */
	private Filial filial;

	/** persistent field */
	private Pendencia pendencia;

	/** persistent field */
	private DomainValue tpOperacao;

	/** persistent field */
	private List motoristaControleCargas;

	public Long getIdLiberacaoReguladora() {
		return this.idLiberacaoReguladora;
	}

	public void setIdLiberacaoReguladora(Long idLiberacaoReguladora) {
		this.idLiberacaoReguladora = idLiberacaoReguladora;
	}

	public String getNrLiberacao() {
		return this.nrLiberacao;
	}

	public void setNrLiberacao(String nrLiberacao) {
		this.nrLiberacao = nrLiberacao;
	}

	public YearMonthDay getDtLiberacao() {
		return this.dtLiberacao;
	}

	public void setDtLiberacao(YearMonthDay dtLiberacao) {
		this.dtLiberacao = dtLiberacao;
	}

	public YearMonthDay getDtVencimento() {
		return this.dtVencimento;
	}

	public void setDtVencimento(YearMonthDay dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public ReguladoraSeguro getReguladoraSeguro() {
		return this.reguladoraSeguro;
	}

	public void setReguladoraSeguro(ReguladoraSeguro reguladoraSeguro) {
		this.reguladoraSeguro = reguladoraSeguro;
	}

	public Motorista getMotorista() {
		return this.motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public Filial getFilial() {
		return this.filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	@ParametrizedAttribute(type = MotoristaControleCarga.class)
	public List getMotoristaControleCargas() {
		return motoristaControleCargas;
	}

	public void setMotoristaControleCargas(List motoristaControleCargas) {
		this.motoristaControleCargas = motoristaControleCargas;
	}

	public MeioTransporte getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idLiberacaoReguladora",
				getIdLiberacaoReguladora()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LiberacaoReguladora))
			return false;
		LiberacaoReguladora castOther = (LiberacaoReguladora) other;
		return new EqualsBuilder().append(this.getIdLiberacaoReguladora(),
				castOther.getIdLiberacaoReguladora()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdLiberacaoReguladora())
			.toHashCode();
	}
	
}
