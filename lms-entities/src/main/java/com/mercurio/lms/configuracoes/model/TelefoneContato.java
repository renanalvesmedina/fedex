package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class TelefoneContato implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTelefoneContato;

    /** nullable persistent field */
    private String nrRamal;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Contato contato;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.TelefoneEndereco telefoneEndereco;
    
    /** persistent field */
    private List ligacaoCobrancas;

    public Long getIdTelefoneContato() {
        return this.idTelefoneContato;
    }

    public void setIdTelefoneContato(Long idTelefoneContato) {
        this.idTelefoneContato = idTelefoneContato;
    }

    public String getNrRamal() {
        return this.nrRamal;
    }

    public void setNrRamal(String nrRamal) {
        this.nrRamal = nrRamal;
    }

    public com.mercurio.lms.configuracoes.model.Contato getContato() {
        return this.contato;
    }

    public void setContato(com.mercurio.lms.configuracoes.model.Contato contato) {
        this.contato = contato;
    }

    public com.mercurio.lms.configuracoes.model.TelefoneEndereco getTelefoneEndereco() {
        return this.telefoneEndereco;
    }

	public void setTelefoneEndereco(
			com.mercurio.lms.configuracoes.model.TelefoneEndereco telefoneEndereco) {
        this.telefoneEndereco = telefoneEndereco;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.LigacaoCobranca.class)     
    public List getLigacaoCobrancas() {
        return this.ligacaoCobrancas;
    }

    public void setLigacaoCobrancas(List ligacaoCobrancas) {
        this.ligacaoCobrancas = ligacaoCobrancas;
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TelefoneContato))
			return false;
        TelefoneContato castOther = (TelefoneContato) other;
		return new EqualsBuilder().append(this.getIdTelefoneContato(),
				castOther.getIdTelefoneContato()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTelefoneContato())
            .toHashCode();
    }

}
