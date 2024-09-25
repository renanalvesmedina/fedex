package com.mercurio.lms.edi.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FiltroProcessamentoNotasEdiDTO implements Serializable {

    private Long idFilial;
    private String situacao;
    private Long idUsuario;
    private Long idClienteProcessamento;
    private DateTime dhProcessamento;
    private Long nrNotaFiscal;

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdClienteProcessamento() {
        return idClienteProcessamento;
    }

    public void setIdClienteProcessamento(Long idClienteProcessamento) {
        this.idClienteProcessamento = idClienteProcessamento;
    }

    public DateTime getDhProcessamento() {
        return dhProcessamento;
    }

    public void setDhProcessamento(DateTime dhProcessamento) {
        this.dhProcessamento = dhProcessamento;
    }

    public Long getNrNotaFiscal() {
        return nrNotaFiscal;
    }

    public void setNrNotaFiscal(Long nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }
}
