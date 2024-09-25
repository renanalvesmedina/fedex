package com.mercurio.lms.expedicao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InscricaoEstadualDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idInscricaoEstadual;
    private String nrInscricaoEstadual;
    private Boolean blIndicadorPadrao;

    public InscricaoEstadualDto(Long idInscricaoEstadual, String nrInscricaoEstadual, Boolean blIndicadorPadrao) {
        this.idInscricaoEstadual = idInscricaoEstadual;
        this.nrInscricaoEstadual = nrInscricaoEstadual;
        this.blIndicadorPadrao = blIndicadorPadrao;
    }

    public Long getIdInscricaoEstadual() {
        return idInscricaoEstadual;
    }

    public void setIdInscricaoEstadual(Long idInscricaoEstadual) {
        this.idInscricaoEstadual = idInscricaoEstadual;
    }

    public String getNrInscricaoEstadual() {
        return nrInscricaoEstadual;
    }

    public void setNrInscricaoEstadual(String nrInscricaoEstadual) {
        this.nrInscricaoEstadual = nrInscricaoEstadual;
    }

    public Boolean getBlIndicadorPadrao() {
        return blIndicadorPadrao;
    }

    public void setBlIndicadorPadrao(Boolean blIndicadorPadrao) {
        this.blIndicadorPadrao = blIndicadorPadrao;
    }
}

