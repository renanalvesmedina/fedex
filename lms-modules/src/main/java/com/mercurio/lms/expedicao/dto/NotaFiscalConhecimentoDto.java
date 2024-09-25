package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class NotaFiscalConhecimentoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idNotaFiscalEdi;
    private Integer nrNotaFiscal;
    private String dsSerie;
    private BigDecimal vlBaseCalculo;
    private BigDecimal vlPedagio;
    private String dtEmissao;
    private BigDecimal vlIcms;
    private Short qtVolumes;
    private BigDecimal vlIcmsSt;
    private BigDecimal vlTotal;
    private BigDecimal vlTotalProdutos;
    private Integer nrPinSuframa;
    private BigDecimal vlBaseCalculoSt;
    private BigDecimal psMercadoria;
    private Boolean blProdutoPerigoso;
    private Boolean blControladoPoliciaCivil;
    private Boolean blControladoPoliciaFederal;
    private Boolean blControladoExercito;
    private BigDecimal psCubadoNotfis;
    private BigDecimal vlAdeme;
    private BigDecimal vlFreteValor;
    private BigDecimal vlItr;
    private BigDecimal vlDespacho;
    private BigDecimal vlFretePeso;
    private BigDecimal vlCat;
    private BigDecimal psCubado;
    private BigDecimal vlTaxasEOutrosValores;
    private String nrChave;
    private Integer nrCfop;
    private String tpDocumento;
    private BigDecimal psAferido;
    private List<VolumeNotaFiscalDto> volumeNotaFiscal;

    public Integer getNrNotaFiscal() {
        return nrNotaFiscal;
    }

    public void setNrNotaFiscal(Integer nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public String getDsSerie() {
        return dsSerie;
    }

    public void setDsSerie(String dsSerie) {
        this.dsSerie = dsSerie;
    }

    public BigDecimal getVlBaseCalculo() {
        return vlBaseCalculo;
    }

    public void setVlBaseCalculo(BigDecimal vlBaseCalculo) {
        this.vlBaseCalculo = vlBaseCalculo;
    }

    public BigDecimal getVlPedagio() {
        return vlPedagio;
    }

    public void setVlPedagio(BigDecimal vlPedagio) {
        this.vlPedagio = vlPedagio;
    }

    public String getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(String dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public BigDecimal getVlIcms() {
        return vlIcms;
    }

    public void setVlIcms(BigDecimal vlIcms) {
        this.vlIcms = vlIcms;
    }

    public Short getQtVolumes() {
        return qtVolumes;
    }

    public void setQtVolumes(Short qtVolumes) {
        this.qtVolumes = qtVolumes;
    }

    public BigDecimal getVlIcmsSt() {
        return vlIcmsSt;
    }

    public void setVlIcmsSt(BigDecimal vlIcmsSt) {
        this.vlIcmsSt = vlIcmsSt;
    }

    public BigDecimal getVlTotal() {
        return vlTotal;
    }

    public void setVlTotal(BigDecimal vlTotal) {
        this.vlTotal = vlTotal;
    }

    public BigDecimal getVlTotalProdutos() {
        return vlTotalProdutos;
    }

    public void setVlTotalProdutos(BigDecimal vlTotalProdutos) {
        this.vlTotalProdutos = vlTotalProdutos;
    }

    public Integer getNrPinSuframa() {
        return nrPinSuframa;
    }

    public void setNrPinSuframa(Integer nrPinSuframa) {
        this.nrPinSuframa = nrPinSuframa;
    }

    public BigDecimal getVlBaseCalculoSt() {
        return vlBaseCalculoSt;
    }

    public void setVlBaseCalculoSt(BigDecimal vlBaseCalculoSt) {
        this.vlBaseCalculoSt = vlBaseCalculoSt;
    }

    public BigDecimal getPsMercadoria() {
        return psMercadoria;
    }

    public void setPsMercadoria(BigDecimal psMercadoria) {
        this.psMercadoria = psMercadoria;
    }

    public Boolean getBlProdutoPerigoso() {
        return blProdutoPerigoso;
    }

    public void setBlProdutoPerigoso(Boolean blProdutoPerigoso) {
        this.blProdutoPerigoso = blProdutoPerigoso;
    }

    public Boolean getBlControladoPoliciaCivil() {
        return blControladoPoliciaCivil;
    }

    public void setBlControladoPoliciaCivil(Boolean blControladoPoliciaCivil) {
        this.blControladoPoliciaCivil = blControladoPoliciaCivil;
    }

    public Boolean getBlControladoPoliciaFederal() {
        return blControladoPoliciaFederal;
    }

    public void setBlControladoPoliciaFederal(Boolean blControladoPoliciaFederal) {
        this.blControladoPoliciaFederal = blControladoPoliciaFederal;
    }

    public Boolean getBlControladoExercito() {
        return blControladoExercito;
    }

    public void setBlControladoExercito(Boolean blControladoExercito) {
        this.blControladoExercito = blControladoExercito;
    }

    public BigDecimal getPsCubadoNotfis() {
        return psCubadoNotfis;
    }

    public void setPsCubadoNotfis(BigDecimal psCubadoNotfis) {
        this.psCubadoNotfis = psCubadoNotfis;
    }

    public BigDecimal getVlAdeme() {
        return vlAdeme;
    }

    public void setVlAdeme(BigDecimal vlAdeme) {
        this.vlAdeme = vlAdeme;
    }

    public BigDecimal getVlFreteValor() {
        return vlFreteValor;
    }

    public void setVlFreteValor(BigDecimal vlFreteValor) {
        this.vlFreteValor = vlFreteValor;
    }

    public BigDecimal getVlItr() {
        return vlItr;
    }

    public void setVlItr(BigDecimal vlItr) {
        this.vlItr = vlItr;
    }

    public BigDecimal getVlDespacho() {
        return vlDespacho;
    }

    public void setVlDespacho(BigDecimal vlDespacho) {
        this.vlDespacho = vlDespacho;
    }

    public BigDecimal getVlFretePeso() {
        return vlFretePeso;
    }

    public void setVlFretePeso(BigDecimal vlFretePeso) {
        this.vlFretePeso = vlFretePeso;
    }

    public BigDecimal getVlCat() {
        return vlCat;
    }

    public void setVlCat(BigDecimal vlCat) {
        this.vlCat = vlCat;
    }

    public BigDecimal getPsCubado() {
        return psCubado;
    }

    public void setPsCubado(BigDecimal psCubado) {
        this.psCubado = psCubado;
    }

    public BigDecimal getVlTaxasEOutrosValores() {
        return vlTaxasEOutrosValores;
    }

    public void setVlTaxasEOutrosValores(BigDecimal vlTaxasEOutrosValores) {
        this.vlTaxasEOutrosValores = vlTaxasEOutrosValores;
    }

    public String getNrChave() {
        return nrChave;
    }

    public void setNrChave(String nrChave) {
        this.nrChave = nrChave;
    }

    public Integer getNrCfop() {
        return nrCfop;
    }

    public void setNrCfop(Integer nrCfop) {
        this.nrCfop = nrCfop;
    }

    public String getTpDocumento() {
        return tpDocumento;
    }

    public void setTpDocumento(String tpDocumento) {
        this.tpDocumento = tpDocumento;
    }

    public List<VolumeNotaFiscalDto> getVolumeNotaFiscal() {
        return volumeNotaFiscal;
    }

    public void setVolumeNotaFiscal(List<VolumeNotaFiscalDto> volumeNotaFiscal) {
        this.volumeNotaFiscal = volumeNotaFiscal;
    }

    public BigDecimal getPsAferido() {
        return psAferido;
    }

    public void setPsAferido(BigDecimal psAferido) {
        this.psAferido = psAferido;
    }

    public Long getIdNotaFiscalEdi() {
        return idNotaFiscalEdi;
    }

    public void setIdNotaFiscalEdi(Long idNotaFiscalEdi) {
        this.idNotaFiscalEdi = idNotaFiscalEdi;
    }
}
