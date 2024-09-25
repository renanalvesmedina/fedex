package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Pessoa;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolOperadorasTelefonia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOperadora;

    /** persistent field */
    private String dsUrlSms;

    /** nullable persistent field */
    private String dsSenha;

    /** nullable persistent field */
    private String dsCorpoSms;

    /** nullable persistent field */
    private String dsUsuario;

    /** nullable persistent field */
    private Pessoa pessoa;

    /** persistent field */
    private Contato contato;

    /** persistent field */
    private List volEquipamentos;
    
    private DomainValue tpSituacao;
    
    public Long getIdOperadora() {
        return this.idOperadora;
    }

    public void setIdOperadora(Long idOperadora) {
        this.idOperadora = idOperadora;
    }

    public String getDsUrlSms() {
        return this.dsUrlSms;
    }

    public void setDsUrlSms(String dsUrlSms) {
        this.dsUrlSms = dsUrlSms;
    }

    public String getDsSenha() {
        return this.dsSenha;
    }

    public void setDsSenha(String dsSenha) {
        this.dsSenha = dsSenha;
    }

    public String getDsCorpoSms() {
        return this.dsCorpoSms;
    }

    public void setDsCorpoSms(String dsCorpoSms) {
        this.dsCorpoSms = dsCorpoSms;
    }

    public String getDsUsuario() {
        return this.dsUsuario;
    }

    public void setDsUsuario(String dsUsuario) {
        this.dsUsuario = dsUsuario;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Contato getContato() {
        return this.contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolEquipamentos.class)     
    public List getVolEquipamentos() {
        return this.volEquipamentos;
    }

    public void setVolEquipamentos(List volEquipamentos) {
        this.volEquipamentos = volEquipamentos;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idOperadora", getIdOperadora()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolOperadorasTelefonia))
			return false;
        VolOperadorasTelefonia castOther = (VolOperadorasTelefonia) other;
		return new EqualsBuilder().append(this.getIdOperadora(),
				castOther.getIdOperadora()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOperadora()).toHashCode();
    }

    public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
}
