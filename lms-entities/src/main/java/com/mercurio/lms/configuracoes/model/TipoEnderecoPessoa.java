package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoEnderecoPessoa implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoEnderecoPessoa;

    /** persistent field */
    private DomainValue tpEndereco;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa;
   
    public Long getIdTipoEnderecoPessoa() {
        return this.idTipoEnderecoPessoa;
    }

    public void setIdTipoEnderecoPessoa(Long idTipoEnderecoPessoa) {
        this.idTipoEnderecoPessoa = idTipoEnderecoPessoa;
    }

    public DomainValue getTpEndereco() {
        return this.tpEndereco;
    }

    public void setTpEndereco(DomainValue tpEndereco) {
        this.tpEndereco = tpEndereco;
    }

    public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoPessoa() {
        return this.enderecoPessoa;
    }

	public void setEnderecoPessoa(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa) {
        this.enderecoPessoa = enderecoPessoa;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoEnderecoPessoa",
				getIdTipoEnderecoPessoa()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoEnderecoPessoa))
			return false;
        TipoEnderecoPessoa castOther = (TipoEnderecoPessoa) other;
		return new EqualsBuilder().append(this.getIdTipoEnderecoPessoa(),
				castOther.getIdTipoEnderecoPessoa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoEnderecoPessoa())
            .toHashCode();
    }

}
