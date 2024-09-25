package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import com.mercurio.lms.municipios.model.Filial;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Impressora implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idImpressora;

	/** persistent field */
	private String dsCheckIn;

	/** nullable persistent field */
	private String dsLocalizacao;

	/** nullable persistent field */
	private DomainValue tpImpressora;

	/** nullable persistent field */
	private String dsModelo;

	/** nullable persistent field */
	private String dsFabricante;

	/** nullable persistent field */
	private Long nrIp;

	/** nullable persistent field */
	private Integer nrPort;

	/** persistent field */
	private com.mercurio.lms.municipios.model.Filial filial;

	/** persistent field */
	private List impressoraFormularios;

	private Boolean blEtiquetaNova;

	/** persistent field */
	private com.mercurio.lms.expedicao.model.Balanca balanca;

	public Impressora() {
	}

	public Impressora(Long idImpressora, String dsCheckIn, String dsLocalizacao, DomainValue tpImpressora, String dsModelo, String dsFabricante, Long nrIp, Integer nrPort, Filial filial, List impressoraFormularios, Boolean blEtiquetaNova, Balanca balanca) {
		this.idImpressora = idImpressora;
		this.dsCheckIn = dsCheckIn;
		this.dsLocalizacao = dsLocalizacao;
		this.tpImpressora = tpImpressora;
		this.dsModelo = dsModelo;
		this.dsFabricante = dsFabricante;
		this.nrIp = nrIp;
		this.nrPort = nrPort;
		this.filial = filial;
		this.impressoraFormularios = impressoraFormularios;
		this.blEtiquetaNova = blEtiquetaNova;
		this.balanca = balanca;
	}

	/**
	 * @return the nrIp
	 */
	public Long getNrIp() {
		return nrIp;
	}

	/**
	 * @param nrIp
	 *            the nrIp to set
	 */
	public void setNrIp(Long nrIp) {
		this.nrIp = nrIp;
	}

	/**
	 * @return the port
	 */
	public Integer getNrPort() {
		return nrPort;
	}

	/**
	 * @param nrPort
	 *            the port to set
	 */
	public void setNrPort(Integer nrPort) {
		this.nrPort = nrPort;
	}

	/**
	 * @return dsCheckin + dsLocalizacao
	 */
	public String getCheckinLocalizacao(){
		return new StringBuffer().append(this.getDsCheckIn()).append(" - ")
				.append(this.getDsLocalizacao()).toString();
	}

	public Long getIdImpressora() {
		return this.idImpressora;
	}

	public void setIdImpressora(Long idImpressora) {
		this.idImpressora = idImpressora;
	}

	public String getDsCheckIn() {
		return this.dsCheckIn;
	}

	public void setDsCheckIn(String dsCheckIn) {
		this.dsCheckIn = dsCheckIn;
	}

	public String getDsLocalizacao() {
		return this.dsLocalizacao;
	}

	public void setDsLocalizacao(String dsLocalizacao) {
		this.dsLocalizacao = dsLocalizacao;
	}

	public DomainValue getTpImpressora() {
		return this.tpImpressora;
	}

	public void setTpImpressora(DomainValue tpImpressora) {
		this.tpImpressora = tpImpressora;
	}

	public String getDsModelo() {
		return this.dsModelo;
	}

	public void setDsModelo(String dsModelo) {
		this.dsModelo = dsModelo;
	}

	public String getDsFabricante() {
		return this.dsFabricante;
	}

	public void setDsFabricante(String dsFabricante) {
		this.dsFabricante = dsFabricante;
	}

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return this.filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ImpressoraFormulario.class) 
	public List getImpressoraFormularios() {
		return this.impressoraFormularios;
	}

	public void setImpressoraFormularios(List impressoraFormularios) {
		this.impressoraFormularios = impressoraFormularios;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idImpressora",
				getIdImpressora()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Impressora))
			return false;
		Impressora castOther = (Impressora) other;
		return new EqualsBuilder().append(this.getIdImpressora(),
				castOther.getIdImpressora()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdImpressora()).toHashCode();
	}

	public Boolean getBlEtiquetaNova() {
		return blEtiquetaNova;
}

	public void setBlEtiquetaNova(Boolean blEtiquetaNova) {
		this.blEtiquetaNova = blEtiquetaNova;
	}

	public Balanca getBalanca() {
		return balanca;
}

	public void setBalanca(Balanca balanca) {
		this.balanca = balanca;
	}

}
