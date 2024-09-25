package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class HistoricoPce implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idHistoricoPce;

    /** persistent field */
    private DateTime dhOcorrencia;

    /** persistent field */
    private com.mercurio.lms.vendas.model.DescritivoPce descritivoPce;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdHistoricoPce() {
        return this.idHistoricoPce;
    }

    public void setIdHistoricoPce(Long idHistoricoPce) {
        this.idHistoricoPce = idHistoricoPce;
    }

    public DateTime getDhOcorrencia() {
        return this.dhOcorrencia;
    }

    public void setDhOcorrencia(DateTime dhOcorrencia) {
        this.dhOcorrencia = dhOcorrencia;
    }

    public com.mercurio.lms.vendas.model.DescritivoPce getDescritivoPce() {
        return this.descritivoPce;
    }

	public void setDescritivoPce(
			com.mercurio.lms.vendas.model.DescritivoPce descritivoPce) {
        this.descritivoPce = descritivoPce;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idHistoricoPce",
				getIdHistoricoPce()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HistoricoPce))
			return false;
        HistoricoPce castOther = (HistoricoPce) other;
		return new EqualsBuilder().append(this.getIdHistoricoPce(),
				castOther.getIdHistoricoPce()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdHistoricoPce()).toHashCode();
    }

}
