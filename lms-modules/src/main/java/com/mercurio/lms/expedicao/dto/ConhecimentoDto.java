package com.mercurio.lms.expedicao.dto;

import com.mercurio.lms.vendas.dto.ClienteDTO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ConhecimentoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean blGeraReceita;
    private String nrCepColeta;
    private String tpFrete;
    private String tpConhecimento;
    private Boolean blIndicadorEDI;
    private String dsEnderecoEntrega;
    private String dsBairroEntrega;
    private Boolean blEmitidoLMS;
    private Long NRoRDEMeMISSAOedi;
    private Boolean blProdutoPerigoso;
    private Boolean blProdutoControlado;
    private Boolean blOperacaoSpitFire;
    private String tpCalculoPreco;
    private Boolean blExecutarVerificacaoDocumentoManifesto;
    private Boolean blPaletizacao;
    private BigDecimal nrCubagemDeclarada;
    private ClienteDTO clienteByIdClienteRedespacho;
    private Boolean blProcessamentoTomador;

    public Boolean getBlGeraReceita() {
        return blGeraReceita;
    }

    public void setBlGeraReceita(Boolean blGeraReceita) {
        this.blGeraReceita = blGeraReceita;
    }

    public String getNrCepColeta() {
        return nrCepColeta;
    }

    public void setNrCepColeta(String nrCepColeta) {
        this.nrCepColeta = nrCepColeta;
    }

    public String getTpFrete() {
        return tpFrete;
    }

    public void setTpFrete(String tpFrete) {
        this.tpFrete = tpFrete;
    }

    public String getTpConhecimento() {
        return tpConhecimento;
    }

    public void setTpConhecimento(String tpConhecimento) {
        this.tpConhecimento = tpConhecimento;
    }

    public Boolean getBlIndicadorEDI() {
        return blIndicadorEDI;
    }

    public void setBlIndicadorEDI(Boolean blIndicadorEDI) {
        this.blIndicadorEDI = blIndicadorEDI;
    }

    public String getDsEnderecoEntrega() {
        return dsEnderecoEntrega;
    }

    public void setDsEnderecoEntrega(String dsEnderecoEntrega) {
        this.dsEnderecoEntrega = dsEnderecoEntrega;
    }

    public String getDsBairroEntrega() {
        return dsBairroEntrega;
    }

    public void setDsBairroEntrega(String dsBairroEntrega) {
        this.dsBairroEntrega = dsBairroEntrega;
    }

    public Boolean getBlEmitidoLMS() {
        return blEmitidoLMS;
    }

    public void setBlEmitidoLMS(Boolean blEmitidoLMS) {
        this.blEmitidoLMS = blEmitidoLMS;
    }

    public Long getNRoRDEMeMISSAOedi() {
        return NRoRDEMeMISSAOedi;
    }

    public void setNRoRDEMeMISSAOedi(Long NRoRDEMeMISSAOedi) {
        this.NRoRDEMeMISSAOedi = NRoRDEMeMISSAOedi;
    }

    public Boolean getBlProdutoPerigoso() {
        return blProdutoPerigoso;
    }

    public void setBlProdutoPerigoso(Boolean blProdutoPerigoso) {
        this.blProdutoPerigoso = blProdutoPerigoso;
    }

    public Boolean getBlProdutoControlado() {
        return blProdutoControlado;
    }

    public void setBlProdutoControlado(Boolean blProdutoControlado) {
        this.blProdutoControlado = blProdutoControlado;
    }

    public Boolean getBlOperacaoSpitFire() {
        return blOperacaoSpitFire;
    }

    public void setBlOperacaoSpitFire(Boolean blOperacaoSpitFire) {
        this.blOperacaoSpitFire = blOperacaoSpitFire;
    }

    public String getTpCalculoPreco() {
        return tpCalculoPreco;
    }

    public void setTpCalculoPreco(String tpCalculoPreco) {
        this.tpCalculoPreco = tpCalculoPreco;
    }

    public Boolean getBlExecutarVerificacaoDocumentoManifesto() {
        return blExecutarVerificacaoDocumentoManifesto;
    }

    public void setBlExecutarVerificacaoDocumentoManifesto(Boolean blExecutarVerificacaoDocumentoManifesto) {
        this.blExecutarVerificacaoDocumentoManifesto = blExecutarVerificacaoDocumentoManifesto;
    }

    public Boolean getBlPaletizacao() {
        return blPaletizacao;
    }

    public void setBlPaletizacao(Boolean blPaletizacao) {
        this.blPaletizacao = blPaletizacao;
    }

    public BigDecimal getNrCubagemDeclarada() {
        return nrCubagemDeclarada;
    }

    public void setNrCubagemDeclarada(BigDecimal nrCubagemDeclarada) {
        this.nrCubagemDeclarada = nrCubagemDeclarada;
    }

    public ClienteDTO getClienteByIdClienteRedespacho() {
        return clienteByIdClienteRedespacho;
    }

    public void setClienteByIdClienteRedespacho(ClienteDTO clienteByIdClienteRedespacho) {
        this.clienteByIdClienteRedespacho = clienteByIdClienteRedespacho;
    }

    public Boolean getBlProcessamentoTomador() {
        return blProcessamentoTomador;
    }

    public void setBlProcessamentoTomador(Boolean blProcessamentoTomador) {
        this.blProcessamentoTomador = blProcessamentoTomador;
    }
}
