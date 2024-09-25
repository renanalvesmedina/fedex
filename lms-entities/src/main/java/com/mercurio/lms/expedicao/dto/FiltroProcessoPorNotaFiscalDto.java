package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FiltroProcessoPorNotaFiscalDto implements Serializable {

    private String nrIdentificacao;
    @JsonProperty(value = "nrNotaFiscal")
    private Integer nrNotaFiscalInicial;
    private Integer nrNotaFiscalFinal;
    private String nrChave;
    private Long idFilial;

    public String getNrIdentificacao() {
        return nrIdentificacao;
    }

    public void setNrIdentificacao(String nrIdentificacao) {
        this.nrIdentificacao = nrIdentificacao;
    }

    public Integer getNrNotaFiscalInicial() {
        return nrNotaFiscalInicial;
    }

    public void setNrNotaFiscalInicial(Integer nrNotaFiscalInicial) {
        this.nrNotaFiscalInicial = nrNotaFiscalInicial;
    }

    public String getNrChave() {
        return nrChave;
    }

    public void setNrChave(String nrChave) {
        this.nrChave = nrChave;
    }

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }

    public Integer getNrNotaFiscalFinal() {
        return nrNotaFiscalFinal;
    }

    public void setNrNotaFiscalFinal(Integer nrNotaFiscalFinal) {
        this.nrNotaFiscalFinal = nrNotaFiscalFinal;
    }
}
