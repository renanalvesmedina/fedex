package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TelefoneEndereco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTelefoneEndereco;

    /** persistent field */
    private DomainValue tpTelefone;

    /** persistent field */
    private DomainValue tpUso;

    /** persistent field */
    private String nrDdd;

    /** persistent field */
    private String nrTelefone;

    /** nullable persistent field */
    private String nrDdi;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private List telefoneContatos;

    public Long getIdTelefoneEndereco() {
        return this.idTelefoneEndereco;
    }

    public void setIdTelefoneEndereco(Long idTelefoneEndereco) {
        this.idTelefoneEndereco = idTelefoneEndereco;
    }

    public DomainValue getTpTelefone() {
        return this.tpTelefone;
    }

    public void setTpTelefone(DomainValue tpTelefone) {
        this.tpTelefone = tpTelefone;
    }

    public DomainValue getTpUso() {
        return this.tpUso;
    }

    public void setTpUso(DomainValue tpUso) {
        this.tpUso = tpUso;
    }

    public String getNrDdd() {
        return this.nrDdd;
    }

    public void setNrDdd(String nrDdd) {
        this.nrDdd = nrDdd;
    }

    public String getNrTelefone() {
        return this.nrTelefone;
    }

    public void setNrTelefone(String nrTelefone) {
        this.nrTelefone = nrTelefone;
    }

    public String getNrDdi() {
        return this.nrDdi;
    }

    public void setNrDdi(String nrDdi) {
        this.nrDdi = nrDdi;
    }

    public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoPessoa() {
        return this.enderecoPessoa;
    }

	public void setEnderecoPessoa(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa) {
        this.enderecoPessoa = enderecoPessoa;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    /**
     * Retorna o DDD e o número do telefone no formato : (DDD) NR_TELEFONE
	 * 
     * @return
     */
    public String getDddTelefone(){
    	return "("+getNrDdd()+") " + getNrTelefone();
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.TelefoneContato.class)     
    public List getTelefoneContatos() {
        return this.telefoneContatos;
    }

    public void setTelefoneContatos(List telefoneContatos) {
        this.telefoneContatos = telefoneContatos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTelefoneEndereco",
				getIdTelefoneEndereco()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TelefoneEndereco))
			return false;
        TelefoneEndereco castOther = (TelefoneEndereco) other;
		return new EqualsBuilder().append(this.getIdTelefoneEndereco(),
				castOther.getIdTelefoneEndereco()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTelefoneEndereco())
            .toHashCode();
    }

}
