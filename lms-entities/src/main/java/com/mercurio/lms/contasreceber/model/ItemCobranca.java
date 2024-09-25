package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemCobranca implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer versao;

    /** identifier field */
    private Long idItemCobranca;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.CobrancaInadimplencia cobrancaInadimplencia;

    /** persistent field */
    private List itemLigacoes;

    public Long getIdItemCobranca() {
        return this.idItemCobranca;
    }

    public void setIdItemCobranca(Long idItemCobranca) {
        this.idItemCobranca = idItemCobranca;
    }

    public com.mercurio.lms.contasreceber.model.Fatura getFatura() {
        return this.fatura;
    }

    public void setFatura(com.mercurio.lms.contasreceber.model.Fatura fatura) {
        this.fatura = fatura;
    }

    public com.mercurio.lms.contasreceber.model.CobrancaInadimplencia getCobrancaInadimplencia() {
        return this.cobrancaInadimplencia;
    }

	public void setCobrancaInadimplencia(
			com.mercurio.lms.contasreceber.model.CobrancaInadimplencia cobrancaInadimplencia) {
        this.cobrancaInadimplencia = cobrancaInadimplencia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemLigacao.class)     
    public List getItemLigacoes() {
        return this.itemLigacoes;
    }

    public void setItemLigacoes(List itemLigacoes) {
        this.itemLigacoes = itemLigacoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idItemCobranca",
				getIdItemCobranca()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemCobranca))
			return false;
        ItemCobranca castOther = (ItemCobranca) other;
		return new EqualsBuilder().append(this.getIdItemCobranca(),
				castOther.getIdItemCobranca()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemCobranca()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
