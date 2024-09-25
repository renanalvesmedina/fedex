package com.mercurio.lms.edi.dto;

import java.io.Serializable;

public class TipoOcorrenciaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String valorDominio;
    private String descricaoDomino;

    public TipoOcorrenciaDTO(String valorDominio, String descricaoDomino) {
        this.valorDominio = valorDominio;
        this.descricaoDomino = descricaoDomino;
    }

    public String getValorDominio() {
        return valorDominio;
    }

    public void setValorDominio(String valorDominio) {
        this.valorDominio = valorDominio;
    }

    public String getDescricaoDomino() {
        return descricaoDomino;
    }

    public void setDescricaoDomino(String descricaoDomino) {
        this.descricaoDomino = descricaoDomino;
    }
}
