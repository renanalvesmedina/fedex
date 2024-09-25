package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ServicoOferecido implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idServicoOferecido;

    /** nullable persistent field */
    private String obServicoOferecido;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private com.mercurio.lms.vendas.model.EtapaVisita etapaVisita;

    public Long getIdServicoOferecido() {
        return this.idServicoOferecido;
    }

    public void setIdServicoOferecido(Long idServicoOferecido) {
        this.idServicoOferecido = idServicoOferecido;
    }

    public String getObServicoOferecido() {
        return this.obServicoOferecido;
    }

    public void setObServicoOferecido(String obServicoOferecido) {
        this.obServicoOferecido = obServicoOferecido;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public com.mercurio.lms.vendas.model.EtapaVisita getEtapaVisita() {
        return this.etapaVisita;
    }

	public void setEtapaVisita(
			com.mercurio.lms.vendas.model.EtapaVisita etapaVisita) {
        this.etapaVisita = etapaVisita;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idServicoOferecido",
				getIdServicoOferecido()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ServicoOferecido))
			return false;
        ServicoOferecido castOther = (ServicoOferecido) other;
		return new EqualsBuilder().append(this.getIdServicoOferecido(),
				castOther.getIdServicoOferecido()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdServicoOferecido())
            .toHashCode();
    }

}
