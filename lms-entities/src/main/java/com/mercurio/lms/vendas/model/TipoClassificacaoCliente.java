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
public class TipoClassificacaoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoClassificacaoCliente;
    
    private Integer versao;    

    /** persistent field */
    private VarcharI18n dsTipoClassificacaoCliente;

    /** persistent field */
    private Boolean blOpcional;

    /** persistent field */
    private Boolean blEspecial;

    /** persistent field */
    private Boolean blEventual;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private List descClassificacaoClientes;

    public Long getIdTipoClassificacaoCliente() {
        return this.idTipoClassificacaoCliente;
    }

    public void setIdTipoClassificacaoCliente(Long idTipoClassificacaoCliente) {
        this.idTipoClassificacaoCliente = idTipoClassificacaoCliente;
    }

    public VarcharI18n getDsTipoClassificacaoCliente() {
		return dsTipoClassificacaoCliente;
    }

	public void setDsTipoClassificacaoCliente(
			VarcharI18n dsTipoClassificacaoCliente) {
        this.dsTipoClassificacaoCliente = dsTipoClassificacaoCliente;
    }

    public Boolean getBlOpcional() {
        return this.blOpcional;
    }

    public void setBlOpcional(Boolean blOpcional) {
        this.blOpcional = blOpcional;
    }

    public Boolean getBlEspecial() {
        return this.blEspecial;
    }

    public void setBlEspecial(Boolean blEspecial) {
        this.blEspecial = blEspecial;
    }

    public Boolean getBlEventual() {
        return this.blEventual;
    }

    public void setBlEventual(Boolean blEventual) {
        this.blEventual = blEventual;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DescClassificacaoCliente.class)     
    public List getDescClassificacaoClientes() {
        return this.descClassificacaoClientes;
    }

    public void setDescClassificacaoClientes(List descClassificacaoClientes) {
        this.descClassificacaoClientes = descClassificacaoClientes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoClassificacaoCliente",
				getIdTipoClassificacaoCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoClassificacaoCliente))
			return false;
        TipoClassificacaoCliente castOther = (TipoClassificacaoCliente) other;
		return new EqualsBuilder().append(this.getIdTipoClassificacaoCliente(),
				castOther.getIdTipoClassificacaoCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoClassificacaoCliente())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
