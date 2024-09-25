package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class StoreLogEdiDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idProcessamentoEdi;
    private Long nrNotaFiscal;
    private String mensagem;

    public Long getIdProcessamentoEdi() {
        return idProcessamentoEdi;
    }

    public void setIdProcessamentoEdi(Long idProcessamentoEdi) {
        this.idProcessamentoEdi = idProcessamentoEdi;
    }

    public Long getNrNotaFiscal() {
        return nrNotaFiscal;
    }

    public void setNrNotaFiscal(Long nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
