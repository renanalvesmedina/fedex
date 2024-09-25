package com.mercurio.lms.expedicao.dto;

import java.io.Serializable;

public class VolumeNotaFiscalDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nrVolumeColeta;
    private String cdBarraPostoAvancado;

    public String getNrVolumeColeta() {
        return nrVolumeColeta;
    }

    public void setNrVolumeColeta(String nrVolumeColeta) {
        this.nrVolumeColeta = nrVolumeColeta;
    }

    public String getCdBarraPostoAvancado() {
        return cdBarraPostoAvancado;
    }

    public void setCdBarraPostoAvancado(String cdBarraPostoAvancado) {
        this.cdBarraPostoAvancado = cdBarraPostoAvancado;
    }
}
