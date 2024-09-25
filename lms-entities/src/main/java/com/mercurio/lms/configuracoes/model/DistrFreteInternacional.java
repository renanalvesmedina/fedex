package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class DistrFreteInternacional implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDistrFreteInternacional;

    /** persistent field */
    private Short cdPermisso;

    /** persistent field */
    private Integer distanciaKm;

    /** persistent field */
    private BigDecimal pcFreteExterno;

    /** persistent field */
    private BigDecimal pcFreteOrigem;

    /** persistent field */
    private BigDecimal pcFreteDestino;

    /** persistent field */
    private Filial filialOrigem;

    /** persistent field */
    private Filial filialDestino;

    /** nullable persistent field */
    private BigDecimal nrTempoViagem;
    
    private Integer versao;

    /** persistent field */
    private List tramoFreteInternacionais;

    public Long getIdDistrFreteInternacional() {
        return this.idDistrFreteInternacional;
    }

    public void setIdDistrFreteInternacional(Long idDistrFreteInternacional) {
        this.idDistrFreteInternacional = idDistrFreteInternacional;
    }

    public Short getCdPermisso() {
        return this.cdPermisso;
    }

    public void setCdPermisso(Short cdPermisso) {
        this.cdPermisso = cdPermisso;
    }

    public Integer getDistanciaKm() {
        return this.distanciaKm;
    }

    public void setDistanciaKm(Integer distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public BigDecimal getPcFreteExterno() {
        return this.pcFreteExterno;
    }

    public void setPcFreteExterno(BigDecimal pcFreteExterno) {
        this.pcFreteExterno = pcFreteExterno;
    }

    public BigDecimal getPcFreteOrigem() {
        return this.pcFreteOrigem;
    }

    public void setPcFreteOrigem(BigDecimal pcFreteOrigem) {
        this.pcFreteOrigem = pcFreteOrigem;
    }

    public BigDecimal getPcFreteDestino() {
        return this.pcFreteDestino;
    }

    public void setPcFreteDestino(BigDecimal pcFreteDestino) {
        this.pcFreteDestino = pcFreteDestino;
    }

    public Filial getFilialOrigem() {
        return this.filialOrigem;
    }

    public void setFilialOrigem(Filial filialOrigem) {
        this.filialOrigem = filialOrigem;
    }

    public Filial getFilialDestino() {
        return this.filialDestino;
    }

    public void setFilialDestino(Filial filialDestino) {
        this.filialDestino = filialDestino;
    }

    public BigDecimal getNrTempoViagem() {
        return this.nrTempoViagem;
    }

    public void setNrTempoViagem(BigDecimal nrTempoViagem) {
        this.nrTempoViagem = nrTempoViagem;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.TramoFreteInternacional.class)     
    public List getTramoFreteInternacionais() {
        return this.tramoFreteInternacionais;
    }

    public void setTramoFreteInternacionais(List tramoFreteInternacionais) {
        this.tramoFreteInternacionais = tramoFreteInternacionais;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDistrFreteInternacional",
				getIdDistrFreteInternacional()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DistrFreteInternacional))
			return false;
        DistrFreteInternacional castOther = (DistrFreteInternacional) other;
		return new EqualsBuilder().append(this.getIdDistrFreteInternacional(),
				castOther.getIdDistrFreteInternacional()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDistrFreteInternacional())
            .toHashCode();
    }

}
