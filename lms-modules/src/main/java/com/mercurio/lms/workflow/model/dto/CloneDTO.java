package com.mercurio.lms.workflow.model.dto;

import java.io.Serializable;

public class CloneDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String base;
    private String clone;
    private String situacao;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getClone() {
        return clone;
    }

    public void setClone(String clone) {
        this.clone = clone;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
