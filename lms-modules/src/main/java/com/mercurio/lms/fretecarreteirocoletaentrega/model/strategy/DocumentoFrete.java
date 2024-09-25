package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocumentoFrete;

public class DocumentoFrete<T extends NotaCreditoDocumentoFrete> {

    private T documento;
    private String dsEndereco;
    private BigDecimal peso;
    private BigDecimal pesoAcumulado = BigDecimal.ZERO;
    private BigDecimal valor = BigDecimal.ZERO;

    public DocumentoFrete() {}

    public T getDocumento() {
        return documento;
    }

    public void setDocumento(T documento) {
        this.documento = documento;
    }

    public String getDsEndereco() {
        return dsEndereco;
    }

    public void setDsEndereco(String dsEndereco) {
        this.dsEndereco = dsEndereco;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getPesoAcumulado() {
        return pesoAcumulado;
    }

    public void addPesoAcumulado(BigDecimal pesoAcumulado) {
    	this.pesoAcumulado = this.pesoAcumulado.add(pesoAcumulado);
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean comparaPorEndereco(DocumentoFrete<?> doc) {
        return compareEnderecoDocumentosFrete(doc);
    }

    private boolean compareEnderecoDocumentosFrete(DocumentoFrete<?> doc) {
        return (getDsEndereco() != null && doc.getDsEndereco() != null)
                && getDsEndereco().equals(doc.getDsEndereco());
    }

}
