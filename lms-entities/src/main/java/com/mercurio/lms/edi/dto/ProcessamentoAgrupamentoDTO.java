package com.mercurio.lms.edi.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProcessamentoAgrupamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nrProcessamento;

    public ProcessamentoAgrupamentoDTO() {
    }

    public ProcessamentoAgrupamentoDTO(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    public Long getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }
}
