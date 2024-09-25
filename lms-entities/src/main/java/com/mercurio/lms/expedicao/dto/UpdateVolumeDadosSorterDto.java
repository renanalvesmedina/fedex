package com.mercurio.lms.expedicao.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class UpdateVolumeDadosSorterDto {

    private Long idVolumeNotaFiscal;
    private Integer nrDimensao1Cm;
    private Integer nrDimensao2Cm; 
    private Integer nrDimensao3Cm; 
    private BigDecimal psAferido;
    private DateTime dhPesagem;
    private Integer nrDimensao1Sorter;
    private Integer nrDimensao2Sorter;
    private Integer nrDimensao3Sorter;
    private BigDecimal psAferidoSorter;

    public Long getIdVolumeNotaFiscal() {
        return idVolumeNotaFiscal;
    }
    public void setIdVolumeNotaFiscal(Long idVolumeNotaFiscal) {
        this.idVolumeNotaFiscal = idVolumeNotaFiscal;
    }
    public Integer getNrDimensao1Cm() {
        return nrDimensao1Cm;
    }
    public void setNrDimensao1Cm(Integer nrDimensao1Cm) {
        this.nrDimensao1Cm = nrDimensao1Cm;
    }
    public Integer getNrDimensao2Cm() {
        return nrDimensao2Cm;
    }
    public void setNrDimensao2Cm(Integer nrDimensao2Cm) {
        this.nrDimensao2Cm = nrDimensao2Cm;
    }
    public Integer getNrDimensao3Cm() {
        return nrDimensao3Cm;
    }
    public void setNrDimensao3Cm(Integer nrDimensao3Cm) {
        this.nrDimensao3Cm = nrDimensao3Cm;
    }
    public BigDecimal getPsAferido() {
        return psAferido;
    }
    public void setPsAferido(BigDecimal psAferido) {
        this.psAferido = psAferido;
    }
    public DateTime getDhPesagem() {
        return dhPesagem;
    }
    public void setDhPesagem(DateTime dhPesagem) {
        this.dhPesagem = dhPesagem;
    }
    public Integer getNrDimensao1Sorter() {
        return nrDimensao1Sorter;
    }
    public void setNrDimensao1Sorter(Integer nrDimensao1Sorter) {
        this.nrDimensao1Sorter = nrDimensao1Sorter;
    }
    public Integer getNrDimensao2Sorter() {
        return nrDimensao2Sorter;
    }
    public void setNrDimensao2Sorter(Integer nrDimensao2Sorter) {
        this.nrDimensao2Sorter = nrDimensao2Sorter;
    }
    public Integer getNrDimensao3Sorter() {
        return nrDimensao3Sorter;
    }
    public void setNrDimensao3Sorter(Integer nrDimensao3Sorter) {
        this.nrDimensao3Sorter = nrDimensao3Sorter;
    }
    public BigDecimal getPsAferidoSorter() {
        return psAferidoSorter;
    }
    public void setPsAferidoSorter(BigDecimal psAferidoSorter) {
        this.psAferidoSorter = psAferidoSorter;
    }
    
}
