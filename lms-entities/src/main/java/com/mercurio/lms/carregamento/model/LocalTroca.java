package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class LocalTroca implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLocalTroca;

    /** nullable persistent field */
    private Integer nrKmRodoviaTroca;

    /** nullable persistent field */
    private String dsTroca;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleTrecho controleTrecho;

    /** persistent field */
    private com.mercurio.lms.municipios.model.PontoParada pontoParada;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Rodovia rodovia;

    /** persistent field */
    private List semiReboqueCcs;

    /** persistent field */
    private List veiculoControleCargas;

    /** persistent field */
    private List motoristaControleCargas;

    public Long getIdLocalTroca() {
        return this.idLocalTroca;
    }

    public void setIdLocalTroca(Long idLocalTroca) {
        this.idLocalTroca = idLocalTroca;
    }

    public Integer getNrKmRodoviaTroca() {
        return this.nrKmRodoviaTroca;
    }

    public void setNrKmRodoviaTroca(Integer nrKmRodoviaTroca) {
        this.nrKmRodoviaTroca = nrKmRodoviaTroca;
    }

    public String getDsTroca() {
        return this.dsTroca;
    }

    public void setDsTroca(String dsTroca) {
        this.dsTroca = dsTroca;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.carregamento.model.ControleTrecho getControleTrecho() {
        return this.controleTrecho;
    }

	public void setControleTrecho(
			com.mercurio.lms.carregamento.model.ControleTrecho controleTrecho) {
        this.controleTrecho = controleTrecho;
    }

    public com.mercurio.lms.municipios.model.PontoParada getPontoParada() {
        return this.pontoParada;
    }

	public void setPontoParada(
			com.mercurio.lms.municipios.model.PontoParada pontoParada) {
        this.pontoParada = pontoParada;
    }

    public com.mercurio.lms.municipios.model.Rodovia getRodovia() {
        return this.rodovia;
    }

    public void setRodovia(com.mercurio.lms.municipios.model.Rodovia rodovia) {
        this.rodovia = rodovia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.SemiReboqueCc.class)     
    public List getSemiReboqueCcs() {
        return this.semiReboqueCcs;
    }

    public void setSemiReboqueCcs(List semiReboqueCcs) {
        this.semiReboqueCcs = semiReboqueCcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.VeiculoControleCarga.class)     
    public List getVeiculoControleCargas() {
        return this.veiculoControleCargas;
    }

    public void setVeiculoControleCargas(List veiculoControleCargas) {
        this.veiculoControleCargas = veiculoControleCargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.MotoristaControleCarga.class)     
    public List getMotoristaControleCargas() {
        return this.motoristaControleCargas;
    }

    public void setMotoristaControleCargas(List motoristaControleCargas) {
        this.motoristaControleCargas = motoristaControleCargas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLocalTroca",
				getIdLocalTroca()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LocalTroca))
			return false;
        LocalTroca castOther = (LocalTroca) other;
		return new EqualsBuilder().append(this.getIdLocalTroca(),
				castOther.getIdLocalTroca()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLocalTroca()).toHashCode();
    }

}
