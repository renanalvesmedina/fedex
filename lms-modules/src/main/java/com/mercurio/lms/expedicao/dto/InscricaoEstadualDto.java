package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
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

