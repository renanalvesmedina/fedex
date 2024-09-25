package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DestinoSimulacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDestinoSimulacao;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Simulacao simulacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdDestinoSimulacao() {
        return this.idDestinoSimulacao;
    }

    public void setIdDestinoSimulacao(Long idDestinoSimulacao) {
        this.idDestinoSimulacao = idDestinoSimulacao;
    }

    public com.mercurio.lms.vendas.model.Simulacao getSimulacao() {
        return this.simulacao;
    }

    public void setSimulacao(com.mercurio.lms.vendas.model.Simulacao simulacao) {
        this.simulacao = simulacao;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDestinoSimulacao",
				getIdDestinoSimulacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DestinoSimulacao))
			return false;
        DestinoSimulacao castOther = (DestinoSimulacao) other;
		return new EqualsBuilder().append(this.getIdDestinoSimulacao(),
				castOther.getIdDestinoSimulacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDestinoSimulacao())
            .toHashCode();
    }

}
