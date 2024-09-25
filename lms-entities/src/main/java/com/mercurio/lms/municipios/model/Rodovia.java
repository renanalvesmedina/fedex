package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author Hibernate CodeGenerator */
public class Rodovia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRodovia;

    /** persistent field */
    private String sgRodovia;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private String dsRodovia;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;

    /** persistent field */
    private List processoSinistros;

    /** persistent field */
    private List localTrocas;

    /** persistent field */
    private List postoPassagems;

    /** persistent field */
    private List pontoParadas;

    /** persistent field */
    private List postoControles;

    public Long getIdRodovia() {
        return this.idRodovia;
    }

    public void setIdRodovia(Long idRodovia) {
        this.idRodovia = idRodovia;
    }

    public String getSgRodovia() {
        return this.sgRodovia;
    }

    public void setSgRodovia(String sgRodovia) {
        this.sgRodovia = sgRodovia;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getDsRodovia() {
        return this.dsRodovia;
    }

    public void setDsRodovia(String dsRodovia) {
        this.dsRodovia = dsRodovia;
    }
    
    public String getSgDsRodovia() {
    	StringBuffer sbRodovia = new StringBuffer();
		sbRodovia.append(this.sgRodovia != null ? this.sgRodovia : "").append(
				this.dsRodovia != null ? " - " + this.dsRodovia : "");
        return sbRodovia.toString();
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
        return this.unidadeFederativa;
    }

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ProcessoSinistro.class)     
    public List getProcessoSinistros() {
        return this.processoSinistros;
    }

    public void setProcessoSinistros(List processoSinistros) {
        this.processoSinistros = processoSinistros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.LocalTroca.class)     
    public List getLocalTrocas() {
        return this.localTrocas;
    } 

    public void setLocalTrocas(List localTrocas) {
        this.localTrocas = localTrocas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PostoPassagem.class)     
    public List getPostoPassagems() {
        return this.postoPassagems;
    }

    public void setPostoPassagems(List postoPassagems) {
        this.postoPassagems = postoPassagems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PontoParada.class)     
    public List getPontoParadas() {
        return this.pontoParadas;
    }

    public void setPontoParadas(List pontoParadas) {
        this.pontoParadas = pontoParadas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.PostoControle.class)     
    public List getPostoControles() {
        return this.postoControles;
    }

    public void setPostoControles(List postoControles) {
        this.postoControles = postoControles;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRodovia", getIdRodovia())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Rodovia))
			return false;
        Rodovia castOther = (Rodovia) other;
		return new EqualsBuilder().append(this.getIdRodovia(),
				castOther.getIdRodovia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRodovia()).toHashCode();
    }

}
