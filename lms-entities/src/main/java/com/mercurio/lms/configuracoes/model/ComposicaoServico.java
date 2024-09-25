package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ComposicaoServico implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idComposicaoServico;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    public Long getIdComposicaoServico() {
        return this.idComposicaoServico;
    }

    public void setIdComposicaoServico(Long idComposicaoServico) {
        this.idComposicaoServico = idComposicaoServico;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.configuracoes.model.ServicoAdicional getServicoAdicional() {
        return this.servicoAdicional;
    }

	public void setServicoAdicional(
			com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional) {
        this.servicoAdicional = servicoAdicional;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idComposicaoServico",
				getIdComposicaoServico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ComposicaoServico))
			return false;
        ComposicaoServico castOther = (ComposicaoServico) other;
		return new EqualsBuilder().append(this.getIdComposicaoServico(),
				castOther.getIdComposicaoServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdComposicaoServico())
            .toHashCode();
    }

}
