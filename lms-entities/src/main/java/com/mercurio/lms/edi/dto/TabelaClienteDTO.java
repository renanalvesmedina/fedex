package com.mercurio.lms.edi.dto;

import java.io.Serializable;

public class TabelaClienteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idEdiTabelaCliente;

    private Long idEdiTabelaOcoren;

    private Long idCliente;

    private String nrIdentificacao;

    private String nomePessoa;

    public TabelaClienteDTO(Long idEdiTabelaCliente, Long idEdiTabelaOcoren, Long idCliente, String nrIdentificacao, String nomePessoa) {
        this.idEdiTabelaCliente = idEdiTabelaCliente;
        this.idEdiTabelaOcoren = idEdiTabelaOcoren;
        this.idCliente = idCliente;
        this.nrIdentificacao = nrIdentificacao;
        this.nomePessoa = nomePessoa;
    }

    public Long getIdEdiTabelaCliente() {
        return idEdiTabelaCliente;
    }

    public void setIdEdiTabelaCliente(Long idEdiTabelaCliente) {
        this.idEdiTabelaCliente = idEdiTabelaCliente;
    }

    public Long getIdEdiTabelaOcoren() {
        return idEdiTabelaOcoren;
    }

    public void setIdEdiTabelaOcoren(Long idEdiTabelaOcoren) {
        this.idEdiTabelaOcoren = idEdiTabelaOcoren;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNrIdentificacao() {
        return nrIdentificacao;
    }

    public void setNrIdentificacao(String nrIdentificacao) {
        this.nrIdentificacao = nrIdentificacao;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }
}
