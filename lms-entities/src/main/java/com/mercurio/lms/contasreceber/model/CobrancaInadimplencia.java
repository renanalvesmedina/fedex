package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class CobrancaInadimplencia implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer versao;

    /** identifier field */
    private Long idCobrancaInadimplencia;

    /** persistent field */
    private String dsCobrancaInadimplencia;

    /** persistent field */
    private Boolean blCobrancaEncerrada;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private List ligacaoCobrancas;

    /** persistent field */
    private List itemCobrancas;

    /** persistent field */
    private List agendaCobrancas;

    public Long getIdCobrancaInadimplencia() {
        return this.idCobrancaInadimplencia;
    }

    public void setIdCobrancaInadimplencia(Long idCobrancaInadimplencia) {
        this.idCobrancaInadimplencia = idCobrancaInadimplencia;
    }

    public String getDsCobrancaInadimplencia() {
        return this.dsCobrancaInadimplencia;
    }

    public void setDsCobrancaInadimplencia(String dsCobrancaInadimplencia) {
        this.dsCobrancaInadimplencia = dsCobrancaInadimplencia;
    }

    public Boolean getBlCobrancaEncerrada() {
        return this.blCobrancaEncerrada;
    }

    public void setBlCobrancaEncerrada(Boolean blCobrancaEncerrada) {
        this.blCobrancaEncerrada = blCobrancaEncerrada;
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

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.LigacaoCobranca.class)     
    public List getLigacaoCobrancas() {
        return this.ligacaoCobrancas;
    }

    public void setLigacaoCobrancas(List ligacaoCobrancas) {
        this.ligacaoCobrancas = ligacaoCobrancas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemCobranca.class)     
    public List getItemCobrancas() {
        return this.itemCobrancas;
    }

    public void setItemCobrancas(List itemCobrancas) {
        this.itemCobrancas = itemCobrancas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.AgendaCobranca.class)     
    public List getAgendaCobrancas() {
        return this.agendaCobrancas;
    }

    public void setAgendaCobrancas(List agendaCobrancas) {
        this.agendaCobrancas = agendaCobrancas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCobrancaInadimplencia",
				getIdCobrancaInadimplencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CobrancaInadimplencia))
			return false;
        CobrancaInadimplencia castOther = (CobrancaInadimplencia) other;
		return new EqualsBuilder().append(this.getIdCobrancaInadimplencia(),
				castOther.getIdCobrancaInadimplencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCobrancaInadimplencia())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
