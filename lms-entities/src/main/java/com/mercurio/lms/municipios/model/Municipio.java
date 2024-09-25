package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author Hibernate CodeGenerator */
public class Municipio implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMunicipio;

    /** persistent field */
    private String nmMunicipio;

    /** persistent field */
    private String nrCep;

    /** persistent field */
    private Integer cdIbge;

    /** persistent field */
    private Integer cdSiafi;
    
    /** persistent field */
    private Integer nrPopulacao;

    /** persistent field */
    private Boolean blDistrito;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private Integer cdEstadual;

    /** persistent field */
    private UnidadeFederativa unidadeFederativa;

    /** persistent field */
    private Municipio municipioDistrito;

    private Integer nrDistanciaCapital;

	public Long getIdMunicipio() {
        return this.idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getNmMunicipio() {
        return this.nmMunicipio;
    }

    public void setNmMunicipio(String nmMunicipio) {
        this.nmMunicipio = nmMunicipio;
    }

    public String getNrCep() {
        return this.nrCep;
    }

    public void setNrCep(String nrCep) {
        this.nrCep = nrCep;
    }

    public Integer getCdIbge() {
        return this.cdIbge;
    }

    public void setCdIbge(Integer cdIbge) {
        this.cdIbge = cdIbge;
    }

    public Integer getNrPopulacao() {
        return this.nrPopulacao;
    }

    public void setNrPopulacao(Integer nrPopulacao) {
        this.nrPopulacao = nrPopulacao;
    }

    public Boolean getBlDistrito() {
        return this.blDistrito;
    }

    public void setBlDistrito(Boolean blDistrito) {
        this.blDistrito = blDistrito;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Integer getCdEstadual() {
        return this.cdEstadual;
    }

    public void setCdEstadual(Integer cdEstadual) {
        this.cdEstadual = cdEstadual;
    }

    public UnidadeFederativa getUnidadeFederativa() {
        return this.unidadeFederativa;
    }

    public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public Municipio getMunicipioDistrito() {
        return this.municipioDistrito;
    }

    public void setMunicipioDistrito(Municipio municipioDistrito) {
        this.municipioDistrito = municipioDistrito;
    }

    /**
	 * Retorna o nome do município concatenado com a sigla do distrito a que
	 * pertence. Exemplo: Porto Alegre - RS
	 * 
     * @return
     */
    public String getNmMunicipioAndSgUnidadeFederativa(){
    	String retorno = null;
		if (this.getUnidadeFederativa() != null
				&& Hibernate.isInitialized(this.unidadeFederativa)
				&& this.getUnidadeFederativa().getSgUnidadeFederativa() != null) {
			retorno = this.getNmMunicipio() + " - "
					+ this.getUnidadeFederativa().getSgUnidadeFederativa();
    	} else {
    		retorno = this.getNmMunicipio();
    	}    	
    	return retorno;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idMunicipio", getIdMunicipio()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Municipio))
			return false;
        Municipio castOther = (Municipio) other;
		return new EqualsBuilder().append(this.getIdMunicipio(),
				castOther.getIdMunicipio()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipio()).toHashCode();
    }

	public Integer getCdSiafi() {
		return cdSiafi;
}

	public void setCdSiafi(Integer cdSiafi) {
		this.cdSiafi = cdSiafi;
	}

    public Integer getNrDistanciaCapital() {
        return nrDistanciaCapital;
    }

    public void setNrDistanciaCapital(Integer nrDistanciaCapital) {
        this.nrDistanciaCapital = nrDistanciaCapital;
    }
}
