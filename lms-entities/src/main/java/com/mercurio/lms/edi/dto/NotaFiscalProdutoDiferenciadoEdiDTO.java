package com.mercurio.lms.edi.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class NotaFiscalProdutoDiferenciadoEdiDTO implements Serializable {

    @JsonProperty("blControladoPoliciaCivil")
    private Boolean isControladoPoliciaCivil;
    @JsonProperty("blProdutoPerigoso")
    private Boolean isProdutoPerigoso;
    @JsonProperty("blControladoExercito")
    private Boolean isControladoExercito;
    @JsonProperty("blControladoPoliciaFederal")
    private Boolean isControladoPoliciaFederal;
    private Long cnpjDestinatario;
    private String razaoSocial;
    private Integer nrNotaFiscal;
    private Long idNotaFiscalEdi;
    private String nomeReme;
    private Long cnpjReme;
    private String ieReme;
    private String enderecoReme;
    private String bairroReme;
    private String municipioReme;
    private String ufReme;
    private Integer cepEnderReme;
    private Integer cepMuniReme;
    private String nomeDest;
    private Long cnpjDest;
    private String ieDest;
    private String enderecoDest;
    private String bairroDest;
    private String municipioDest;
    private String ufDest;
    private Integer cepEnderDest;
    private Integer cepMunicDest;
    private String nomeConsig;
    private Long cnpjConsig;
    private String ieConsig;
    private String enderecoConsig;
    private String bairroConsig;
    private String municipioConsig;
    private String ufConsig;
    private Integer cepEnderConsig;
    private Integer cepMunicConsig;
    private String nomeRedesp;
    private Long cnpjRedesp;
    private String ieRedesp;
    private String enderecoRedesp;
    private String bairroRedesp;
    private String municipioRedesp;
    private String ufRedesp;
    private Integer cepEnderRedesp;
    private Integer cepMunicRedesp;
    private String nomeTomador;
    private Long cnpjTomador;
    private String ieTomador;
    private String enderecoTomador;
    private String bairroTomador;
    private String municipioTomador;
    private String ufTomador;
    private Integer cepEnderTomador;
    private Integer cepMunicTomador;
    private String natureza;
    private String especie;
    private String tipoFrete;
    private String modalFrete;
    private String tipoTabela;
    private Short tarifa;
    private String serieNf;
    private Date dataEmissaoNf;

    private Date dataLog;

    private BigDecimal qtdeVolumes;
    private BigDecimal vlrTotalMerc;
    private BigDecimal pesoReal;
    private BigDecimal pesoCubado;
    private String chaveNfe;
    private BigDecimal vlrIcmsNf;
    private BigDecimal vlrIcmsStNf;
    private BigDecimal aliqNf;
    private BigDecimal vlrBaseCalcNf;
    private BigDecimal vlrBaseCalcStNf;
    private BigDecimal vlrTotProdutosNf;
    private Short cfopNf;
    private Integer pinSuframa;
    private Long sequenciaAgrupamento;
    private BigDecimal vlrFretePeso;
    private BigDecimal vlrFreteValor;
    private BigDecimal vlrCat;
    private BigDecimal vlrDespacho;
    private BigDecimal vlrItr;
    private BigDecimal vlrAdeme;
    private BigDecimal vlrPedagio;
    private BigDecimal vlrTaxas;
    private BigDecimal outrosValores;
    private BigDecimal vlrIcms;
    private BigDecimal vlrBaseCalcIcms;
    private BigDecimal aliqIcms;
    private BigDecimal vlrFreteLiquido;
    private BigDecimal vlrFreteTotal;
    private BigDecimal pesoRealTotal;
    private BigDecimal pesoCubadoTotal;
    private BigDecimal vlrTotalMercTotal;
    private String dsDivisaoCliente;
    private String nrCtrcSubcontratante;
    private BigDecimal qtVolumeInformado;
    private Boolean blPaletizacao;
    private Long nrEtiquetaInicial;
    private Long nrEtiquetaFinal;

    public Boolean getControladoPoliciaCivil() {
        return isControladoPoliciaCivil;
    }

    public void setControladoPoliciaCivil(Boolean controladoPoliciaCivil) {
        isControladoPoliciaCivil = controladoPoliciaCivil;
    }

    public Boolean getProdutoPerigoso() {
        return isProdutoPerigoso;
    }

    public void setProdutoPerigoso(Boolean produtoPerigoso) {
        isProdutoPerigoso = produtoPerigoso;
    }

    public Boolean getControladoExercito() {
        return isControladoExercito;
    }

    public void setControladoExercito(Boolean controladoExercito) {
        isControladoExercito = controladoExercito;
    }

    public Boolean getControladoPoliciaFederal() {
        return isControladoPoliciaFederal;
    }

    public void setControladoPoliciaFederal(Boolean controladoPoliciaFederal) {
        isControladoPoliciaFederal = controladoPoliciaFederal;
    }

    public Long getCnpjDestinatario() {
        return cnpjDestinatario;
    }

    public void setCnpjDestinatario(Long cnpjDestinatario) {
        this.cnpjDestinatario = cnpjDestinatario;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Integer getNrNotaFiscal() {
        return nrNotaFiscal;
    }

    public void setNrNotaFiscal(Integer nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public Long getIdNotaFiscalEdi() {
        return idNotaFiscalEdi;
    }

    public void setIdNotaFiscalEdi(Long idNotaFiscalEdi) {
        this.idNotaFiscalEdi = idNotaFiscalEdi;
    }

    public String getNomeReme() {
        return nomeReme;
    }

    public void setNomeReme(String nomeReme) {
        this.nomeReme = nomeReme;
    }

    public Long getCnpjReme() {
        return cnpjReme;
    }

    public void setCnpjReme(Long cnpjReme) {
        this.cnpjReme = cnpjReme;
    }

    public String getIeReme() {
        return ieReme;
    }

    public void setIeReme(String ieReme) {
        this.ieReme = ieReme;
    }

    public String getEnderecoReme() {
        return enderecoReme;
    }

    public void setEnderecoReme(String enderecoReme) {
        this.enderecoReme = enderecoReme;
    }

    public String getBairroReme() {
        return bairroReme;
    }

    public void setBairroReme(String bairroReme) {
        this.bairroReme = bairroReme;
    }

    public String getMunicipioReme() {
        return municipioReme;
    }

    public void setMunicipioReme(String municipioReme) {
        this.municipioReme = municipioReme;
    }

    public String getUfReme() {
        return ufReme;
    }

    public void setUfReme(String ufReme) {
        this.ufReme = ufReme;
    }

    public Integer getCepEnderReme() {
        return cepEnderReme;
    }

    public void setCepEnderReme(Integer cepEnderReme) {
        this.cepEnderReme = cepEnderReme;
    }

    public Integer getCepMuniReme() {
        return cepMuniReme;
    }

    public void setCepMuniReme(Integer cepMuniReme) {
        this.cepMuniReme = cepMuniReme;
    }

    public String getNomeDest() {
        return nomeDest;
    }

    public void setNomeDest(String nomeDest) {
        this.nomeDest = nomeDest;
    }

    public Long getCnpjDest() {
        return cnpjDest;
    }

    public void setCnpjDest(Long cnpjDest) {
        this.cnpjDest = cnpjDest;
    }

    public String getIeDest() {
        return ieDest;
    }

    public void setIeDest(String ieDest) {
        this.ieDest = ieDest;
    }

    public String getEnderecoDest() {
        return enderecoDest;
    }

    public void setEnderecoDest(String enderecoDest) {
        this.enderecoDest = enderecoDest;
    }

    public String getBairroDest() {
        return bairroDest;
    }

    public void setBairroDest(String bairroDest) {
        this.bairroDest = bairroDest;
    }

    public String getMunicipioDest() {
        return municipioDest;
    }

    public void setMunicipioDest(String municipioDest) {
        this.municipioDest = municipioDest;
    }

    public String getUfDest() {
        return ufDest;
    }

    public void setUfDest(String ufDest) {
        this.ufDest = ufDest;
    }

    public Integer getCepEnderDest() {
        return cepEnderDest;
    }

    public void setCepEnderDest(Integer cepEnderDest) {
        this.cepEnderDest = cepEnderDest;
    }

    public Integer getCepMunicDest() {
        return cepMunicDest;
    }

    public void setCepMunicDest(Integer cepMunicDest) {
        this.cepMunicDest = cepMunicDest;
    }

    public String getNomeConsig() {
        return nomeConsig;
    }

    public void setNomeConsig(String nomeConsig) {
        this.nomeConsig = nomeConsig;
    }

    public Long getCnpjConsig() {
        return cnpjConsig;
    }

    public void setCnpjConsig(Long cnpjConsig) {
        this.cnpjConsig = cnpjConsig;
    }

    public String getIeConsig() {
        return ieConsig;
    }

    public void setIeConsig(String ieConsig) {
        this.ieConsig = ieConsig;
    }

    public String getEnderecoConsig() {
        return enderecoConsig;
    }

    public void setEnderecoConsig(String enderecoConsig) {
        this.enderecoConsig = enderecoConsig;
    }

    public String getBairroConsig() {
        return bairroConsig;
    }

    public void setBairroConsig(String bairroConsig) {
        this.bairroConsig = bairroConsig;
    }

    public String getMunicipioConsig() {
        return municipioConsig;
    }

    public void setMunicipioConsig(String municipioConsig) {
        this.municipioConsig = municipioConsig;
    }

    public String getUfConsig() {
        return ufConsig;
    }

    public void setUfConsig(String ufConsig) {
        this.ufConsig = ufConsig;
    }

    public Integer getCepEnderConsig() {
        return cepEnderConsig;
    }

    public void setCepEnderConsig(Integer cepEnderConsig) {
        this.cepEnderConsig = cepEnderConsig;
    }

    public Integer getCepMunicConsig() {
        return cepMunicConsig;
    }

    public void setCepMunicConsig(Integer cepMunicConsig) {
        this.cepMunicConsig = cepMunicConsig;
    }

    public String getNomeRedesp() {
        return nomeRedesp;
    }

    public void setNomeRedesp(String nomeRedesp) {
        this.nomeRedesp = nomeRedesp;
    }

    public Long getCnpjRedesp() {
        return cnpjRedesp;
    }

    public void setCnpjRedesp(Long cnpjRedesp) {
        this.cnpjRedesp = cnpjRedesp;
    }

    public String getIeRedesp() {
        return ieRedesp;
    }

    public void setIeRedesp(String ieRedesp) {
        this.ieRedesp = ieRedesp;
    }

    public String getEnderecoRedesp() {
        return enderecoRedesp;
    }

    public void setEnderecoRedesp(String enderecoRedesp) {
        this.enderecoRedesp = enderecoRedesp;
    }

    public String getBairroRedesp() {
        return bairroRedesp;
    }

    public void setBairroRedesp(String bairroRedesp) {
        this.bairroRedesp = bairroRedesp;
    }

    public String getMunicipioRedesp() {
        return municipioRedesp;
    }

    public void setMunicipioRedesp(String municipioRedesp) {
        this.municipioRedesp = municipioRedesp;
    }

    public String getUfRedesp() {
        return ufRedesp;
    }

    public void setUfRedesp(String ufRedesp) {
        this.ufRedesp = ufRedesp;
    }

    public Integer getCepEnderRedesp() {
        return cepEnderRedesp;
    }

    public void setCepEnderRedesp(Integer cepEnderRedesp) {
        this.cepEnderRedesp = cepEnderRedesp;
    }

    public Integer getCepMunicRedesp() {
        return cepMunicRedesp;
    }

    public void setCepMunicRedesp(Integer cepMunicRedesp) {
        this.cepMunicRedesp = cepMunicRedesp;
    }

    public String getNomeTomador() {
        return nomeTomador;
    }

    public void setNomeTomador(String nomeTomador) {
        this.nomeTomador = nomeTomador;
    }

    public Long getCnpjTomador() {
        return cnpjTomador;
    }

    public void setCnpjTomador(Long cnpjTomador) {
        this.cnpjTomador = cnpjTomador;
    }

    public String getIeTomador() {
        return ieTomador;
    }

    public void setIeTomador(String ieTomador) {
        this.ieTomador = ieTomador;
    }

    public String getEnderecoTomador() {
        return enderecoTomador;
    }

    public void setEnderecoTomador(String enderecoTomador) {
        this.enderecoTomador = enderecoTomador;
    }

    public String getBairroTomador() {
        return bairroTomador;
    }

    public void setBairroTomador(String bairroTomador) {
        this.bairroTomador = bairroTomador;
    }

    public String getMunicipioTomador() {
        return municipioTomador;
    }

    public void setMunicipioTomador(String municipioTomador) {
        this.municipioTomador = municipioTomador;
    }

    public String getUfTomador() {
        return ufTomador;
    }

    public void setUfTomador(String ufTomador) {
        this.ufTomador = ufTomador;
    }

    public Integer getCepEnderTomador() {
        return cepEnderTomador;
    }

    public void setCepEnderTomador(Integer cepEnderTomador) {
        this.cepEnderTomador = cepEnderTomador;
    }

    public Integer getCepMunicTomador() {
        return cepMunicTomador;
    }

    public void setCepMunicTomador(Integer cepMunicTomador) {
        this.cepMunicTomador = cepMunicTomador;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getTipoFrete() {
        return tipoFrete;
    }

    public void setTipoFrete(String tipoFrete) {
        this.tipoFrete = tipoFrete;
    }

    public String getModalFrete() {
        return modalFrete;
    }

    public void setModalFrete(String modalFrete) {
        this.modalFrete = modalFrete;
    }

    public String getTipoTabela() {
        return tipoTabela;
    }

    public void setTipoTabela(String tipoTabela) {
        this.tipoTabela = tipoTabela;
    }

    public Short getTarifa() {
        return tarifa;
    }

    public void setTarifa(Short tarifa) {
        this.tarifa = tarifa;
    }

    public String getSerieNf() {
        return serieNf;
    }

    public void setSerieNf(String serieNf) {
        this.serieNf = serieNf;
    }

    public Date getDataEmissaoNf() {
        return dataEmissaoNf;
    }

    public void setDataEmissaoNf(Date dataEmissaoNf) {
        this.dataEmissaoNf = dataEmissaoNf;
    }

    public Date getDataLog() {
        return dataLog;
    }

    public void setDataLog(Date dataLog) {
        this.dataLog = dataLog;
    }

    public BigDecimal getQtdeVolumes() {
        return qtdeVolumes;
    }

    public void setQtdeVolumes(BigDecimal qtdeVolumes) {
        this.qtdeVolumes = qtdeVolumes;
    }

    public BigDecimal getVlrTotalMerc() {
        return vlrTotalMerc;
    }

    public void setVlrTotalMerc(BigDecimal vlrTotalMerc) {
        this.vlrTotalMerc = vlrTotalMerc;
    }

    public BigDecimal getPesoReal() {
        return pesoReal;
    }

    public void setPesoReal(BigDecimal pesoReal) {
        this.pesoReal = pesoReal;
    }

    public BigDecimal getPesoCubado() {
        return pesoCubado;
    }

    public void setPesoCubado(BigDecimal pesoCubado) {
        this.pesoCubado = pesoCubado;
    }

    public String getChaveNfe() {
        return chaveNfe;
    }

    public void setChaveNfe(String chaveNfe) {
        this.chaveNfe = chaveNfe;
    }

    public BigDecimal getVlrIcmsNf() {
        return vlrIcmsNf;
    }

    public void setVlrIcmsNf(BigDecimal vlrIcmsNf) {
        this.vlrIcmsNf = vlrIcmsNf;
    }

    public BigDecimal getVlrIcmsStNf() {
        return vlrIcmsStNf;
    }

    public void setVlrIcmsStNf(BigDecimal vlrIcmsStNf) {
        this.vlrIcmsStNf = vlrIcmsStNf;
    }

    public BigDecimal getAliqNf() {
        return aliqNf;
    }

    public void setAliqNf(BigDecimal aliqNf) {
        this.aliqNf = aliqNf;
    }

    public BigDecimal getVlrBaseCalcNf() {
        return vlrBaseCalcNf;
    }

    public void setVlrBaseCalcNf(BigDecimal vlrBaseCalcNf) {
        this.vlrBaseCalcNf = vlrBaseCalcNf;
    }

    public BigDecimal getVlrBaseCalcStNf() {
        return vlrBaseCalcStNf;
    }

    public void setVlrBaseCalcStNf(BigDecimal vlrBaseCalcStNf) {
        this.vlrBaseCalcStNf = vlrBaseCalcStNf;
    }

    public BigDecimal getVlrTotProdutosNf() {
        return vlrTotProdutosNf;
    }

    public void setVlrTotProdutosNf(BigDecimal vlrTotProdutosNf) {
        this.vlrTotProdutosNf = vlrTotProdutosNf;
    }

    public Short getCfopNf() {
        return cfopNf;
    }

    public void setCfopNf(Short cfopNf) {
        this.cfopNf = cfopNf;
    }

    public Integer getPinSuframa() {
        return pinSuframa;
    }

    public void setPinSuframa(Integer pinSuframa) {
        this.pinSuframa = pinSuframa;
    }

    public Long getSequenciaAgrupamento() {
        return sequenciaAgrupamento;
    }

    public void setSequenciaAgrupamento(Long sequenciaAgrupamento) {
        this.sequenciaAgrupamento = sequenciaAgrupamento;
    }

    public BigDecimal getVlrFretePeso() {
        return vlrFretePeso;
    }

    public void setVlrFretePeso(BigDecimal vlrFretePeso) {
        this.vlrFretePeso = vlrFretePeso;
    }

    public BigDecimal getVlrFreteValor() {
        return vlrFreteValor;
    }

    public void setVlrFreteValor(BigDecimal vlrFreteValor) {
        this.vlrFreteValor = vlrFreteValor;
    }

    public BigDecimal getVlrCat() {
        return vlrCat;
    }

    public void setVlrCat(BigDecimal vlrCat) {
        this.vlrCat = vlrCat;
    }

    public BigDecimal getVlrDespacho() {
        return vlrDespacho;
    }

    public void setVlrDespacho(BigDecimal vlrDespacho) {
        this.vlrDespacho = vlrDespacho;
    }

    public BigDecimal getVlrItr() {
        return vlrItr;
    }

    public void setVlrItr(BigDecimal vlrItr) {
        this.vlrItr = vlrItr;
    }

    public BigDecimal getVlrAdeme() {
        return vlrAdeme;
    }

    public void setVlrAdeme(BigDecimal vlrAdeme) {
        this.vlrAdeme = vlrAdeme;
    }

    public BigDecimal getVlrPedagio() {
        return vlrPedagio;
    }

    public void setVlrPedagio(BigDecimal vlrPedagio) {
        this.vlrPedagio = vlrPedagio;
    }

    public BigDecimal getVlrTaxas() {
        return vlrTaxas;
    }

    public void setVlrTaxas(BigDecimal vlrTaxas) {
        this.vlrTaxas = vlrTaxas;
    }

    public BigDecimal getOutrosValores() {
        return outrosValores;
    }

    public void setOutrosValores(BigDecimal outrosValores) {
        this.outrosValores = outrosValores;
    }

    public BigDecimal getVlrIcms() {
        return vlrIcms;
    }

    public void setVlrIcms(BigDecimal vlrIcms) {
        this.vlrIcms = vlrIcms;
    }

    public BigDecimal getVlrBaseCalcIcms() {
        return vlrBaseCalcIcms;
    }

    public void setVlrBaseCalcIcms(BigDecimal vlrBaseCalcIcms) {
        this.vlrBaseCalcIcms = vlrBaseCalcIcms;
    }

    public BigDecimal getAliqIcms() {
        return aliqIcms;
    }

    public void setAliqIcms(BigDecimal aliqIcms) {
        this.aliqIcms = aliqIcms;
    }

    public BigDecimal getVlrFreteLiquido() {
        return vlrFreteLiquido;
    }

    public void setVlrFreteLiquido(BigDecimal vlrFreteLiquido) {
        this.vlrFreteLiquido = vlrFreteLiquido;
    }

    public BigDecimal getVlrFreteTotal() {
        return vlrFreteTotal;
    }

    public void setVlrFreteTotal(BigDecimal vlrFreteTotal) {
        this.vlrFreteTotal = vlrFreteTotal;
    }

    public BigDecimal getPesoRealTotal() {
        return pesoRealTotal;
    }

    public void setPesoRealTotal(BigDecimal pesoRealTotal) {
        this.pesoRealTotal = pesoRealTotal;
    }

    public BigDecimal getPesoCubadoTotal() {
        return pesoCubadoTotal;
    }

    public void setPesoCubadoTotal(BigDecimal pesoCubadoTotal) {
        this.pesoCubadoTotal = pesoCubadoTotal;
    }

    public BigDecimal getVlrTotalMercTotal() {
        return vlrTotalMercTotal;
    }

    public void setVlrTotalMercTotal(BigDecimal vlrTotalMercTotal) {
        this.vlrTotalMercTotal = vlrTotalMercTotal;
    }

    public String getDsDivisaoCliente() {
        return dsDivisaoCliente;
    }

    public void setDsDivisaoCliente(String dsDivisaoCliente) {
        this.dsDivisaoCliente = dsDivisaoCliente;
    }

    public String getNrCtrcSubcontratante() {
        return nrCtrcSubcontratante;
    }

    public void setNrCtrcSubcontratante(String nrCtrcSubcontratante) {
        this.nrCtrcSubcontratante = nrCtrcSubcontratante;
    }

    public BigDecimal getQtVolumeInformado() {
        return qtVolumeInformado;
    }

    public void setQtVolumeInformado(BigDecimal qtVolumeInformado) {
        this.qtVolumeInformado = qtVolumeInformado;
    }

    public Boolean getBlPaletizacao() {
        return blPaletizacao;
    }

    public void setBlPaletizacao(Boolean blPaletizacao) {
        this.blPaletizacao = blPaletizacao;
    }

    public Long getNrEtiquetaInicial() {
        return nrEtiquetaInicial;
    }

    public void setNrEtiquetaInicial(Long nrEtiquetaInicial) {
        this.nrEtiquetaInicial = nrEtiquetaInicial;
    }

    public Long getNrEtiquetaFinal() {
        return nrEtiquetaFinal;
    }

    public void setNrEtiquetaFinal(Long nrEtiquetaFinal) {
        this.nrEtiquetaFinal = nrEtiquetaFinal;
    }
}
