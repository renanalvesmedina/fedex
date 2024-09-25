package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoPorNotaFiscalDto implements Serializable {

    private Long idNotaFiscalEdi;
    private Integer nrNotaFiscal;
    private BigDecimal qtdeVolumes;
    private String dataHora;
    private String cnpjDest;
    private String nomeDest;

    public DocumentoPorNotaFiscalDto(Long idNotaFiscalEdi, Integer nrNotaFiscal, BigDecimal qtdeVolumes, String dataHora, String cnpjDest, String nomeDest) {
        this.idNotaFiscalEdi = idNotaFiscalEdi;
        this.nrNotaFiscal = nrNotaFiscal;
        this.qtdeVolumes = qtdeVolumes;
        this.dataHora = dataHora;
        this.cnpjDest = cnpjDest;
        this.nomeDest = nomeDest;
    }

    public Long getIdNotaFiscalEdi() {
        return idNotaFiscalEdi;
    }

    public void setIdNotaFiscalEdi(Long idNotaFiscalEdi) {
        this.idNotaFiscalEdi = idNotaFiscalEdi;
    }

    public Integer getNrNotaFiscal() {
        return nrNotaFiscal;
    }

    public void setNrNotaFiscal(Integer nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public BigDecimal getQtdeVolumes() {
        return qtdeVolumes;
    }

    public void setQtdeVolumes(BigDecimal qtdeVolumes) {
        this.qtdeVolumes = qtdeVolumes;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getCnpjDest() {
        return cnpjDest;
    }

    public void setCnpjDest(String cnpjDest) {
        this.cnpjDest = cnpjDest;
    }

    public String getNomeDest() {
        return nomeDest;
    }

    public void setNomeDest(String nomeDest) {
        this.nomeDest = nomeDest;
    }
}
