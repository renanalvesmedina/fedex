package com.mercurio.lms.expedicao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FiltroNotaFiscalDoctoClienteDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idFilial;
    private Long idCliente;
   // private String valorComplemento;
    private String nrDoctoCliente;
    private String tpProcessamento;

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNrDoctoCliente() {
        return nrDoctoCliente;
    }

    public void setNrDoctoCliente(String nrDoctoCliente) {
        this.nrDoctoCliente = nrDoctoCliente;
    }

    public String getTpProcessamento() {
        return tpProcessamento;
    }

    public void setTpProcessamento(String tpProcessamento) {
        this.tpProcessamento = tpProcessamento;
    }
}
