package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.configuracoes.model.TelefoneContato;

/** @author LMS Custom Hibernate CodeGenerator */
public class LigacaoCobranca implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLigacaoCobranca;

    /** persistent field */
    private DateTime dhLigacaoCobranca;

    /** persistent field */
    private String dsLigacaoCobranca;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private TelefoneContato telefoneContato;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.CobrancaInadimplencia cobrancaInadimplencia;

    /** persistent field */
    private List agendaCobrancas;

    /** persistent field */
    private List itemLigacoes;

    public Long getIdLigacaoCobranca() {
        return this.idLigacaoCobranca;
    }

    public void setIdLigacaoCobranca(Long idLigacaoCobranca) {
        this.idLigacaoCobranca = idLigacaoCobranca;
    }

    public DateTime getDhLigacaoCobranca() {
        return this.dhLigacaoCobranca;
    }

    public void setDhLigacaoCobranca(DateTime dhLigacaoCobranca) {
        this.dhLigacaoCobranca = dhLigacaoCobranca;
    }

    public String getDsLigacaoCobranca() {
        return this.dsLigacaoCobranca;
    }

    public void setDsLigacaoCobranca(String dsLigacaoCobranca) {
        this.dsLigacaoCobranca = dsLigacaoCobranca;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

   public TelefoneContato getTelefoneContato() {
		return telefoneContato;
	}

	public void setTelefoneContato(TelefoneContato telefoneContato) {
		this.telefoneContato = telefoneContato;
	}

	public com.mercurio.lms.contasreceber.model.CobrancaInadimplencia getCobrancaInadimplencia() {
        return this.cobrancaInadimplencia;
    }

	public void setCobrancaInadimplencia(
			com.mercurio.lms.contasreceber.model.CobrancaInadimplencia cobrancaInadimplencia) {
        this.cobrancaInadimplencia = cobrancaInadimplencia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.AgendaCobranca.class)     
    public List getAgendaCobrancas() {
        return this.agendaCobrancas;
    }

    public void setAgendaCobrancas(List agendaCobrancas) {
        this.agendaCobrancas = agendaCobrancas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemLigacao.class)     
    public List getItemLigacoes() {
        return this.itemLigacoes;
    }

    public void setItemLigacoes(List itemLigacoes) {
        this.itemLigacoes = itemLigacoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLigacaoCobranca",
				getIdLigacaoCobranca()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LigacaoCobranca))
			return false;
        LigacaoCobranca castOther = (LigacaoCobranca) other;
		return new EqualsBuilder().append(this.getIdLigacaoCobranca(),
				castOther.getIdLigacaoCobranca()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLigacaoCobranca())
            .toHashCode();
    }

}
