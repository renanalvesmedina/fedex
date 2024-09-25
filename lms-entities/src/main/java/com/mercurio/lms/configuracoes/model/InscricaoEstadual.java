package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;

/** @author LMS Custom Hibernate CodeGenerator */
public class InscricaoEstadual implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idInscricaoEstadual;

    /** persistent field */
    private String nrInscricaoEstadual;

    /** persistent field */
    private Boolean blIndicadorPadrao;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;
     
    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;

    /** persistent field */
    private List filiais;

    /** persistent field */
    private List filialCiaAereas;

    /** persistent field */
    private List concessionarias;

    /** persistent field */
    private List descrTributIcmsPessoas;

    /** persistent field */
    private List empresas;

    /** persistent field */
    private List<TipoTributacaoIE> tiposTributacaoIe;

    public Long getIdInscricaoEstadual() {
        return this.idInscricaoEstadual;
    }

    public void setIdInscricaoEstadual(Long idInscricaoEstadual) {
        this.idInscricaoEstadual = idInscricaoEstadual;
    }

    public String getNrInscricaoEstadual() {
        return this.nrInscricaoEstadual;
    }

    public void setNrInscricaoEstadual(String nrInscricaoEstadual) {
        this.nrInscricaoEstadual = nrInscricaoEstadual;
    }

    public Boolean getBlIndicadorPadrao() {
        return this.blIndicadorPadrao;
    }

    public void setBlIndicadorPadrao(Boolean blIndicadorPadrao) {
        this.blIndicadorPadrao = blIndicadorPadrao;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Filial.class)     
    public List getFiliais() {
        return this.filiais;
    }

    public void setFiliais(List filiais) {
        this.filiais = filiais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.FilialCiaAerea.class)     
    public List getFilialCiaAereas() {
        return this.filialCiaAereas;
    }

    public void setFilialCiaAereas(List filialCiaAereas) {
        this.filialCiaAereas = filialCiaAereas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Concessionaria.class)     
    public List getConcessionarias() {
        return this.concessionarias;
    }

    public void setConcessionarias(List concessionarias) {
        this.concessionarias = concessionarias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.DescrTributIcmsPessoa.class)     
    public List getDescrTributIcmsPessoas() {
        return this.descrTributIcmsPessoas;
    }

    public void setDescrTributIcmsPessoas(List descrTributIcmsPessoas) {
        this.descrTributIcmsPessoas = descrTributIcmsPessoas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Empresa.class)     
    public List getEmpresas() {
        return this.empresas;
    }

    public void setEmpresas(List empresas) {
        this.empresas = empresas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idInscricaoEstadual",
				getIdInscricaoEstadual()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof InscricaoEstadual))
			return false;
        InscricaoEstadual castOther = (InscricaoEstadual) other;
		return new EqualsBuilder().append(this.getIdInscricaoEstadual(),
				castOther.getIdInscricaoEstadual()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdInscricaoEstadual())
            .toHashCode();
    }

	public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	/**
	 * @return the tiposTributacaoIe
	 */
	public List<TipoTributacaoIE> getTiposTributacaoIe() {
		return tiposTributacaoIe;
}

	/**
	 * @param tiposTributacaoIe
	 *            the tiposTributacaoIe to set
	 */
	public void setTiposTributacaoIe(List<TipoTributacaoIE> tiposTributacaoIe) {
		this.tiposTributacaoIe = tiposTributacaoIe;
	}

}
