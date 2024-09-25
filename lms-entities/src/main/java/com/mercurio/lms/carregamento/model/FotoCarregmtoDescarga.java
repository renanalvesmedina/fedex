package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class FotoCarregmtoDescarga implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFotoCarregmtoDescarga;

    /** persistent field */
    private String dsFoto;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Foto foto;
    
    private Integer versao;

    public Long getIdFotoCarregmtoDescarga() {
        return this.idFotoCarregmtoDescarga;
    }

    public void setIdFotoCarregmtoDescarga(Long idFotoCarregmtoDescarga) {
        this.idFotoCarregmtoDescarga = idFotoCarregmtoDescarga;
    }

    public String getDsFoto() {
        return this.dsFoto;
    }

    public void setDsFoto(String dsFoto) {
        this.dsFoto = dsFoto;
    }

    public com.mercurio.lms.carregamento.model.CarregamentoDescarga getCarregamentoDescarga() {
        return this.carregamentoDescarga;
    }

	public void setCarregamentoDescarga(
			com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga) {
        this.carregamentoDescarga = carregamentoDescarga;
    }

    public com.mercurio.lms.configuracoes.model.Foto getFoto() {
        return this.foto;
    }

    public void setFoto(com.mercurio.lms.configuracoes.model.Foto foto) {
        this.foto = foto;
    }
    
	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}    

    public String toString() {
		return new ToStringBuilder(this).append("idFotoCarregmtoDescarga",
				getIdFotoCarregmtoDescarga()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FotoCarregmtoDescarga))
			return false;
        FotoCarregmtoDescarga castOther = (FotoCarregmtoDescarga) other;
		return new EqualsBuilder().append(this.getIdFotoCarregmtoDescarga(),
				castOther.getIdFotoCarregmtoDescarga()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFotoCarregmtoDescarga())
            .toHashCode();
    }

}
