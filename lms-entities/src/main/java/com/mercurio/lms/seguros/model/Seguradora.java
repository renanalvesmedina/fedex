package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Seguradora implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSeguradora;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private String dsEnderecoWeb;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private List reguladoraSeguradoras;

    /** persistent field */
    private List apoliceSeguros;

    public Long getIdSeguradora() {
        return this.idSeguradora;
    }

    public void setIdSeguradora(Long idSeguradora) {
        this.idSeguradora = idSeguradora;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getDsEnderecoWeb() {
        return this.dsEnderecoWeb;
    }

    public void setDsEnderecoWeb(String dsEnderecoWeb) {
        this.dsEnderecoWeb = dsEnderecoWeb;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ReguladoraSeguradora.class)     
    public List getReguladoraSeguradoras() {
        return this.reguladoraSeguradoras;
    }

    public void setReguladoraSeguradoras(List reguladoraSeguradoras) {
        this.reguladoraSeguradoras = reguladoraSeguradoras;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PostoPassagemCc.class)     
    public List getApoliceSeguros() {
        return this.apoliceSeguros;
    }

    public void setApoliceSeguros(List apoliceSeguros) {
        this.apoliceSeguros = apoliceSeguros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSeguradora",
				getIdSeguradora()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Seguradora))
			return false;
        Seguradora castOther = (Seguradora) other;
		return new EqualsBuilder().append(this.getIdSeguradora(),
				castOther.getIdSeguradora()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSeguradora()).toHashCode();
    }

}
