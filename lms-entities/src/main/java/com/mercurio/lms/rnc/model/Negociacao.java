package com.mercurio.lms.rnc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class Negociacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNegociacao;

    /** persistent field */
    private DateTime dhNegociacao;

    /** persistent field */
    private String dsNegociacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdNegociacao() {
        return this.idNegociacao;
    }

    public void setIdNegociacao(Long idNegociacao) {
        this.idNegociacao = idNegociacao;
    }

    public DateTime getDhNegociacao() {
        return this.dhNegociacao;
    }

    public void setDhNegociacao(DateTime dhNegociacao) {
        this.dhNegociacao = dhNegociacao;
    }

    public String getDsNegociacao() {
        return this.dsNegociacao;
    }

    public void setDsNegociacao(String dsNegociacao) {
        this.dsNegociacao = dsNegociacao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade getOcorrenciaNaoConformidade() {
        return this.ocorrenciaNaoConformidade;
    }

	public void setOcorrenciaNaoConformidade(
			com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade) {
        this.ocorrenciaNaoConformidade = ocorrenciaNaoConformidade;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNegociacao",
				getIdNegociacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Negociacao))
			return false;
        Negociacao castOther = (Negociacao) other;
		return new EqualsBuilder().append(this.getIdNegociacao(),
				castOther.getIdNegociacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNegociacao()).toHashCode();
    }

}
