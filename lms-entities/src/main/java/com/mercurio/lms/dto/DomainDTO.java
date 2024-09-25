package com.mercurio.lms.dto;

import java.io.Serializable;

public class DomainDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String valor;
    private String descricao;

    public DomainDTO(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
