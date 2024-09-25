package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoLogradouro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoLogradouro;

    /** persistent field */
    private VarcharI18n dsTipoLogradouro;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List enderecoPessoas;

    public Long getIdTipoLogradouro() {
        return this.idTipoLogradouro;
    }

    public void setIdTipoLogradouro(Long idTipoLogradouro) {
        this.idTipoLogradouro = idTipoLogradouro;
    }

    public VarcharI18n getDsTipoLogradouro() {
		return dsTipoLogradouro;
    }

	public void setDsTipoLogradouro(VarcharI18n dsTipoLogradouro) {
        this.dsTipoLogradouro = dsTipoLogradouro;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.EnderecoPessoa.class)     
    public List getEnderecoPessoas() {
        return this.enderecoPessoas;
    }

    public void setEnderecoPessoas(List enderecoPessoas) {
        this.enderecoPessoas = enderecoPessoas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoLogradouro",
				getIdTipoLogradouro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoLogradouro))
			return false;
        TipoLogradouro castOther = (TipoLogradouro) other;
		return new EqualsBuilder().append(this.getIdTipoLogradouro(),
				castOther.getIdTipoLogradouro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoLogradouro()).toHashCode();
    }

}
