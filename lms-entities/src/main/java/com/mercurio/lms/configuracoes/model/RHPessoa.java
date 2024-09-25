package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.sql.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class RHPessoa implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long codigo;

    /** persistent field */
    private String nome;

    /** persistent field */
    private String apelido;
    
    private Date dtNascimento;
    
    private String cartMotorista;
    
    private String tipoCartHabilit;
    
    private Date dtVencHabilit;

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getCartMotorista() {
        return cartMotorista;
    }

    public void setCartMotorista(String cartMotorista) {
        this.cartMotorista = cartMotorista;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public Date getDtVencHabilit() {
        return dtVencHabilit;
    }

    public void setDtVencHabilit(Date dtVencHabilit) {
        this.dtVencHabilit = dtVencHabilit;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoCartHabilit() {
        return tipoCartHabilit;
    }

    public void setTipoCartHabilit(String tipoCartHabilit) {
        this.tipoCartHabilit = tipoCartHabilit;
    }

    public String toString() {
		return new ToStringBuilder(this).append("codigo", getCodigo())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RHPessoa))
			return false;
        RHPessoa castOther = (RHPessoa) other;
		return new EqualsBuilder().append(this.getCodigo(),
				castOther.getCodigo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getCodigo()).toHashCode();
    }

}
