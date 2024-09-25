package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Localidade implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLocalidade;

    /** persistent field */
    private String sgUf;

    /** persistent field */
    private String nmLocalidade;

    /** nullable persistent field */
    private String nrCep;

    /** nullable persistent field */
    private String tpSituacao;

    /** nullable persistent field */
    private String tpLocalidade;

    /** nullable persistent field */
    private Long nrSub;
    
    private List bairros;
    
    private List logradouros;

    public Long getIdLocalidade() {
        return this.idLocalidade;
    }

    public void setIdLocalidade(Long idLocalidade) {
        this.idLocalidade = idLocalidade;
    }

    public String getSgUf() {
        return this.sgUf;
    }

    public void setSgUf(String sgUf) {
        this.sgUf = sgUf;
    }

    public String getNmLocalidade() {
        return this.nmLocalidade;
    }

    public void setNmLocalidade(String nmLocalidade) {
        this.nmLocalidade = nmLocalidade;
    }

    public String getNrCep() {
        return this.nrCep;
    }

    public void setNrCep(String nrCep) {
        this.nrCep = nrCep;
    }

    public String getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(String tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getTpLocalidade() {
        return this.tpLocalidade;
    }

    public void setTpLocalidade(String tpLocalidade) {
        this.tpLocalidade = tpLocalidade;
    }

    public Long getNrSub() {
        return this.nrSub;
    }

    public void setNrSub(Long nrSub) {
        this.nrSub = nrSub;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLocalidade",
				getIdLocalidade()).toString();
    }

	public List getBairros() {
		return bairros;
	}

	public void setBairros(List bairros) {
		this.bairros = bairros;
	}

	public List getLogradouros() {
		return logradouros;
	}

	public void setLogradouros(List logradouros) {
		this.logradouros = logradouros;
	}

}
