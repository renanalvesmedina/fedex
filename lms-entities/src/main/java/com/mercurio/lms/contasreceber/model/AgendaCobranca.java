package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class AgendaCobranca implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAgendaCobranca;

    /** persistent field */
    private DateTime dhAgendaCobranca;

    /** persistent field */
    private String dsAgendaCobranca;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.LigacaoCobranca ligacaoCobranca;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Contato contato;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.CobrancaInadimplencia cobrancaInadimplencia;

    public Long getIdAgendaCobranca() {
        return this.idAgendaCobranca;
    }

    public void setIdAgendaCobranca(Long idAgendaCobranca) {
        this.idAgendaCobranca = idAgendaCobranca;
    }

    public DateTime getDhAgendaCobranca() {
        return this.dhAgendaCobranca;
    }

    public void setDhAgendaCobranca(DateTime dhAgendaCobranca) {
        this.dhAgendaCobranca = dhAgendaCobranca;
    }

    public String getDsAgendaCobranca() {
        return this.dsAgendaCobranca;
    }

    public void setDsAgendaCobranca(String dsAgendaCobranca) {
        this.dsAgendaCobranca = dsAgendaCobranca;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.contasreceber.model.LigacaoCobranca getLigacaoCobranca() {
        return this.ligacaoCobranca;
    }

	public void setLigacaoCobranca(
			com.mercurio.lms.contasreceber.model.LigacaoCobranca ligacaoCobranca) {
        this.ligacaoCobranca = ligacaoCobranca;
    }

    public com.mercurio.lms.configuracoes.model.Contato getContato() {
        return this.contato;
    }

    public void setContato(com.mercurio.lms.configuracoes.model.Contato contato) {
        this.contato = contato;
    }

    public com.mercurio.lms.contasreceber.model.CobrancaInadimplencia getCobrancaInadimplencia() {
        return this.cobrancaInadimplencia;
    }

	public void setCobrancaInadimplencia(
			com.mercurio.lms.contasreceber.model.CobrancaInadimplencia cobrancaInadimplencia) {
        this.cobrancaInadimplencia = cobrancaInadimplencia;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAgendaCobranca",
				getIdAgendaCobranca()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AgendaCobranca))
			return false;
        AgendaCobranca castOther = (AgendaCobranca) other;
		return new EqualsBuilder().append(this.getIdAgendaCobranca(),
				castOther.getIdAgendaCobranca()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAgendaCobranca()).toHashCode();
    }

}
