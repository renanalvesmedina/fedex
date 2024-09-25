package com.mercurio.lms.edi.dto;

import com.mercurio.lms.edi.enums.DsCampoLogErrosEDI;
import com.mercurio.lms.util.DataDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.Serializable;
import java.util.Date;

public class LogErrosEdiDTO implements Serializable {

    private Long idLogErrosEDI;
    private String dsTipoProcessamento;
    private DsCampoLogErrosEDI dsCampoLogErrosEDI;
    private String dsValorErrado;
    private String dsValorCorrigido;
    private Long idNotaFiscalEdi;
    private Integer nrNotaFiscal;
    @JsonDeserialize(using = DataDeserializer.class)
    private Date dataEmissaoNf;
    private Long cnpjReme;
    private String nomeDest;
    private Long nrProcessamento;
    private Long etiquetaInicial;
    private Long etiquetaFinal;

    public Long getIdLogErrosEDI() {
        return idLogErrosEDI;
    }

    public void setIdLogErrosEDI(Long idLogErrosEDI) {
        this.idLogErrosEDI = idLogErrosEDI;
    }

    public String getDsTipoProcessamento() {
        return dsTipoProcessamento;
    }

    public void setDsTipoProcessamento(String dsTipoProcessamento) {
        this.dsTipoProcessamento = dsTipoProcessamento;
    }

    public DsCampoLogErrosEDI getDsCampoLogErrosEDI() {
        return dsCampoLogErrosEDI;
    }

    public void setDsCampoLogErrosEDI(DsCampoLogErrosEDI dsCampoLogErrosEDI) {
        this.dsCampoLogErrosEDI = dsCampoLogErrosEDI;
    }

    public String getDsValorErrado() {
        return dsValorErrado;
    }

    public void setDsValorErrado(String dsValorErrado) {
        this.dsValorErrado = dsValorErrado;
    }

    public String getDsValorCorrigido() {
        return dsValorCorrigido;
    }

    public void setDsValorCorrigido(String dsValorCorrigido) {
        this.dsValorCorrigido = dsValorCorrigido;
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

    public Date getDataEmissaoNf() {
        return dataEmissaoNf;
    }

    public void setDataEmissaoNf(Date dataEmissaoNf) {
        this.dataEmissaoNf = dataEmissaoNf;
    }

    public Long getCnpjReme() {
        return cnpjReme;
    }

    public void setCnpjReme(Long cnpjReme) {
        this.cnpjReme = cnpjReme;
    }

    public String getNomeDest() {
        return nomeDest;
    }

    public void setNomeDest(String nomeDest) {
        this.nomeDest = nomeDest;
    }

    public Long getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    public Long getEtiquetaInicial() {
        return etiquetaInicial;
    }

    public void setEtiquetaInicial(Long etiquetaInicial) {
        this.etiquetaInicial = etiquetaInicial;
    }

    public Long getEtiquetaFinal() {
        return etiquetaFinal;
    }

    public void setEtiquetaFinal(Long etiquetaFinal) {
        this.etiquetaFinal = etiquetaFinal;
    }
}
