package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ColetaAutomaticaCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idColetaAutomaticaCliente;

    /** persistent field */
    private TimeOfDay hrChegada;

    /** nullable persistent field */
    private TimeOfDay hrSaida;

    /** nullable persistent field */
    private DomainValue tpDiaSemana;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;
    
    /** persistent field */
    private com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto;

    public Long getIdColetaAutomaticaCliente() {
        return this.idColetaAutomaticaCliente;
    }

    public void setIdColetaAutomaticaCliente(Long idColetaAutomaticaCliente) {
        this.idColetaAutomaticaCliente = idColetaAutomaticaCliente;
    }

    public TimeOfDay getHrChegada() {
        return this.hrChegada;
    }

    public void setHrChegada(TimeOfDay hrChegada) {
        this.hrChegada = hrChegada;
    }

    public TimeOfDay getHrSaida() {
        return this.hrSaida;
    }

    public void setHrSaida(TimeOfDay hrSaida) {
        this.hrSaida = hrSaida;
    }

    public DomainValue getTpDiaSemana() {
        return this.tpDiaSemana;
    }

    public void setTpDiaSemana(DomainValue tpDiaSemana) {
        this.tpDiaSemana = tpDiaSemana;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoPessoa() {
        return this.enderecoPessoa;
    }

	public void setEnderecoPessoa(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa) {
        this.enderecoPessoa = enderecoPessoa;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idColetaAutomaticaCliente",
				getIdColetaAutomaticaCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ColetaAutomaticaCliente))
			return false;
        ColetaAutomaticaCliente castOther = (ColetaAutomaticaCliente) other;
		return new EqualsBuilder().append(this.getIdColetaAutomaticaCliente(),
				castOther.getIdColetaAutomaticaCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdColetaAutomaticaCliente())
            .toHashCode();
    }

    /**
     * @return Returns the servico.
     */
    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return servico;
    }

    /**
	 * @param servico
	 *            The servico to set.
     */
    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    /**
     * @return Returns the naturezaProduto.
     */
    public com.mercurio.lms.expedicao.model.NaturezaProduto getNaturezaProduto() {
        return naturezaProduto;
    }

    /**
	 * @param naturezaProduto
	 *            The naturezaProduto to set.
     */
    public void setNaturezaProduto(
            com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto) {
        this.naturezaProduto = naturezaProduto;
    }

}
