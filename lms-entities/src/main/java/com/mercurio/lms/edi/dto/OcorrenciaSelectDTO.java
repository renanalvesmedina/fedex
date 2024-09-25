package com.mercurio.lms.edi.dto;

import java.io.Serializable;

public class OcorrenciaSelectDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idLms;
    private String cdOcorrencia;
    private String dsOcorrencia;

    public OcorrenciaSelectDTO(Long idLms, String cdOcorrencia, String dsOcorrencia) {
        this.idLms = idLms;
        this.cdOcorrencia = cdOcorrencia;
        this.dsOcorrencia = dsOcorrencia;
    }

    public Long getIdLms() {
        return idLms;
    }

    public void setIdLms(Long idLms) {
        this.idLms = idLms;
    }

    public String getCdOcorrencia() {
        return cdOcorrencia;
    }

    public void setCdOcorrencia(String cdOcorrencia) {
        this.cdOcorrencia = cdOcorrencia;
    }

    public String getDsOcorrencia() {
        return dsOcorrencia;
    }

    public void setDsOcorrencia(String dsOcorrencia) {
        this.dsOcorrencia = dsOcorrencia;
    }
}
