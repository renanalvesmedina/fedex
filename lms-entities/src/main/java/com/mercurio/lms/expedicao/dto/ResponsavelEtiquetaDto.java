package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsavelEtiquetaDto implements Serializable {
    private Boolean blEtiquetaPorVolume;
    private Boolean BlPaleteFechado;

    public Boolean getBlEtiquetaPorVolume() {
        return blEtiquetaPorVolume;
    }

    public void setBlEtiquetaPorVolume(Boolean blEtiquetaPorVolume) {
        this.blEtiquetaPorVolume = blEtiquetaPorVolume;
    }

    public Boolean getBlPaleteFechado() {
        return BlPaleteFechado;
    }

    public void setBlPaleteFechado(Boolean blPaleteFechado) {
        BlPaleteFechado = blPaleteFechado;
    }

}
