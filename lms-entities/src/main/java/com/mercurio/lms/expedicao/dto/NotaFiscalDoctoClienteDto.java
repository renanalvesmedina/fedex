package com.mercurio.lms.expedicao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotaFiscalDoctoClienteDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<NotaFiscalEdiDto> notaFiscal;
    private String dataHora;

    public List<NotaFiscalEdiDto> getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(List<NotaFiscalEdiDto> notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }
}
