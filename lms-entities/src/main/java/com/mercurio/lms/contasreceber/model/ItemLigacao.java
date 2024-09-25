package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemLigacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemLigacao;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.ItemCobranca itemCobranca;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.LigacaoCobranca ligacaoCobranca;

    public Long getIdItemLigacao() {
        return this.idItemLigacao;
    }

    public void setIdItemLigacao(Long idItemLigacao) {
        this.idItemLigacao = idItemLigacao;
    }

    public com.mercurio.lms.contasreceber.model.ItemCobranca getItemCobranca() {
        return this.itemCobranca;
    }

	public void setItemCobranca(
			com.mercurio.lms.contasreceber.model.ItemCobranca itemCobranca) {
        this.itemCobranca = itemCobranca;
    }

    public com.mercurio.lms.contasreceber.model.LigacaoCobranca getLigacaoCobranca() {
        return this.ligacaoCobranca;
    }

	public void setLigacaoCobranca(
			com.mercurio.lms.contasreceber.model.LigacaoCobranca ligacaoCobranca) {
        this.ligacaoCobranca = ligacaoCobranca;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idItemLigacao",
				getIdItemLigacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemLigacao))
			return false;
        ItemLigacao castOther = (ItemLigacao) other;
		return new EqualsBuilder().append(this.getIdItemLigacao(),
				castOther.getIdItemLigacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemLigacao()).toHashCode();
    }

}
