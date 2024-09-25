package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateResponsavelEtiquetaDto implements Serializable {
    private Long idNotaFiscalEdi;
    private String nrIdentificacaoRemetente;

    public Long getIdNotaFiscalEdi() {
        return idNotaFiscalEdi;
    }

    public void setIdNotaFiscalEdi(Long idNotaFiscalEdi) {
        this.idNotaFiscalEdi = idNotaFiscalEdi;
    }

    public String getNrIdentificacaoRemetente() {
        return nrIdentificacaoRemetente;
    }

    public void setNrIdentificacaoRemetente(String nrIdentificacaoRemetente) {
        this.nrIdentificacaoRemetente = nrIdentificacaoRemetente;
    }
}
