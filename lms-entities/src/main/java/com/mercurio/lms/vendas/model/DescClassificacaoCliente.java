package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class DescClassificacaoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDescClassificacaoCliente;
    
    private Integer versao;    

    /** persistent field */
    private VarcharI18n dsDescClassificacaoCliente;

    /** persistent field */
    private Boolean blPadrao;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.vendas.model.TipoClassificacaoCliente tipoClassificacaoCliente;

    /** persistent field */
    private List classificacaoClientes;

    public Long getIdDescClassificacaoCliente() {
        return this.idDescClassificacaoCliente;
    }

    public void setIdDescClassificacaoCliente(Long idDescClassificacaoCliente) {
        this.idDescClassificacaoCliente = idDescClassificacaoCliente;
    }

    public VarcharI18n getDsDescClassificacaoCliente() {
		return dsDescClassificacaoCliente;
    }

	public void setDsDescClassificacaoCliente(
			VarcharI18n dsDescClassificacaoCliente) {
        this.dsDescClassificacaoCliente = dsDescClassificacaoCliente;
    }

    public Boolean getBlPadrao() {
        return this.blPadrao;
    }

    public void setBlPadrao(Boolean blPadrao) {
        this.blPadrao = blPadrao;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.vendas.model.TipoClassificacaoCliente getTipoClassificacaoCliente() {
        return this.tipoClassificacaoCliente;
    }

	public void setTipoClassificacaoCliente(
			com.mercurio.lms.vendas.model.TipoClassificacaoCliente tipoClassificacaoCliente) {
        this.tipoClassificacaoCliente = tipoClassificacaoCliente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ClassificacaoCliente.class)     
    public List getClassificacaoClientes() {
        return this.classificacaoClientes;
    }

    public void setClassificacaoClientes(List classificacaoClientes) {
        this.classificacaoClientes = classificacaoClientes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDescClassificacaoCliente",
				getIdDescClassificacaoCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DescClassificacaoCliente))
			return false;
        DescClassificacaoCliente castOther = (DescClassificacaoCliente) other;
		return new EqualsBuilder().append(this.getIdDescClassificacaoCliente(),
				castOther.getIdDescClassificacaoCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDescClassificacaoCliente())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
