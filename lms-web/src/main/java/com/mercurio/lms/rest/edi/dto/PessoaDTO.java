package com.mercurio.lms.rest.edi.dto;

import java.io.Serializable;

public class PessoaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idCliente;

    private String nomePossoa;

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomePossoa() {
        return nomePossoa;
    }

    public void setNomePossoa(String nomePossoa) {
        this.nomePossoa = nomePossoa;
    }
}
