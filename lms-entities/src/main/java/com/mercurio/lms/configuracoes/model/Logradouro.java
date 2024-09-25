package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Logradouro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLogradouro;

    /** persistent field */
    private String sgUf;

    /** persistent field */
    private String nmLogradouro;

    /** nullable persistent field */
    private String dsComplemento;

    /** persistent field */
    private String nrCep;

    /** persistent field */
    private String tpLogradouro;

    /** nullable persistent field */
    private String tpSituacao;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Bairro bairroByIdBairroInicial;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Bairro bairroByIdBairroFinal;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Localidade localidade;

    public Long getIdLogradouro() {
        return this.idLogradouro;
    }

    public void setIdLogradouro(Long idLogradouro) {
        this.idLogradouro = idLogradouro;
    }

    public String getSgUf() {
        return this.sgUf;
    }

    public void setSgUf(String sgUf) {
        this.sgUf = sgUf;
    }

    public String getNmLogradouro() {
        return this.nmLogradouro;
    }

    public void setNmLogradouro(String nmLogradouro) {
        this.nmLogradouro = nmLogradouro;
    }

    public String getDsComplemento() {
        return this.dsComplemento;
    }

    public void setDsComplemento(String dsComplemento) {
        this.dsComplemento = dsComplemento;
    }

    public String getNrCep() {
        return this.nrCep;
    }

    public void setNrCep(String nrCep) {
        this.nrCep = nrCep;
    }

    public String getTpLogradouro() {
        return this.tpLogradouro;
    }

    public void setTpLogradouro(String tpLogradouro) {
        this.tpLogradouro = tpLogradouro;
    }

    public String getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(String tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.configuracoes.model.Bairro getBairroByIdBairroInicial() {
        return this.bairroByIdBairroInicial;
    }

	public void setBairroByIdBairroInicial(
			com.mercurio.lms.configuracoes.model.Bairro bairroByIdBairroInicial) {
        this.bairroByIdBairroInicial = bairroByIdBairroInicial;
    }

    public com.mercurio.lms.configuracoes.model.Bairro getBairroByIdBairroFinal() {
        return this.bairroByIdBairroFinal;
    }

	public void setBairroByIdBairroFinal(
			com.mercurio.lms.configuracoes.model.Bairro bairroByIdBairroFinal) {
        this.bairroByIdBairroFinal = bairroByIdBairroFinal;
    }

    public com.mercurio.lms.configuracoes.model.Localidade getLocalidade() {
        return this.localidade;
    }

	public void setLocalidade(
			com.mercurio.lms.configuracoes.model.Localidade localidade) {
        this.localidade = localidade;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLogradouro",
				getIdLogradouro()).toString();
    }

}
