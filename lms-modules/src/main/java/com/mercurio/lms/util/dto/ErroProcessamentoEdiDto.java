package com.mercurio.lms.util.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ErroProcessamentoEdiDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String erro;
    private String description;
    private Long idProcessamentoEdi;
    private Long nrProcessamento;

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdProcessamentoEdi() {
        return idProcessamentoEdi;
    }

    public void setIdProcessamentoEdi(Long idProcessamentoEdi) {
        this.idProcessamentoEdi = idProcessamentoEdi;
    }

    public Long getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }
}
