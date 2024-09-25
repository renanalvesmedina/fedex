package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Bairro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idBairro;

    /** persistent field */
    private String sgUf;

    /** persistent field */
    private String nmBairro;

    /** nullable persistent field */
    private String nmBairroAbreviado;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Localidade localidade;

    public Long getIdBairro() {
        return this.idBairro;
    }

    public void setIdBairro(Long idBairro) {
        this.idBairro = idBairro;
    }

    public String getSgUf() {
        return this.sgUf;
    }

    public void setSgUf(String sgUf) {
        this.sgUf = sgUf;
    }

    public String getNmBairro() {
        return this.nmBairro;
    }

    public void setNmBairro(String nmBairro) {
        this.nmBairro = nmBairro;
    }

    public String getNmBairroAbreviado() {
        return this.nmBairroAbreviado;
    }

    public void setNmBairroAbreviado(String nmBairroAbreviado) {
        this.nmBairroAbreviado = nmBairroAbreviado;
    }

    public com.mercurio.lms.configuracoes.model.Localidade getLocalidade() {
        return this.localidade;
    }

	public void setLocalidade(
			com.mercurio.lms.configuracoes.model.Localidade localidade) {
        this.localidade = localidade;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idBairro", getIdBairro())
            .toString();
    }

}
