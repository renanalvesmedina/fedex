package com.mercurio.lms.edi.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessamentoNotaEdiDTO implements Serializable {

    private String nomeClienteRemetente;
    private Integer nrNotaFiscal;
    private String dsMensagemErro;

    public String getNomeClienteRemetente() {
        return nomeClienteRemetente;
    }

    public void setNomeClienteRemetente(String nomeClienteRemetente) {
        this.nomeClienteRemetente = nomeClienteRemetente;
    }

    public Integer getNrNotaFiscal() {
        return nrNotaFiscal;
    }

    public void setNrNotaFiscal(Integer nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public String getDsMensagemErro() {
        return dsMensagemErro;
    }

    public void setDsMensagemErro(String dsMensagemErro) {
        this.dsMensagemErro = dsMensagemErro;
    }
}
