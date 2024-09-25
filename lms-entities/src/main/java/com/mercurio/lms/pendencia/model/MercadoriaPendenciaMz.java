package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class MercadoriaPendenciaMz implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMercadoriaPendenciaMz;

    /** persistent field */
    private String dsProduto;

    /** persistent field */
    private Integer qtUnidadesPorVolume;

    /** persistent field */
    private BigDecimal vlProduto;

    /** persistent field */
    private DomainValue tpMotivoArmazenagem;

    /** persistent field */
    private DomainValue tpDisposicaoPendencia;

    /** nullable persistent field */
    private String cdProduto;

    /** nullable persistent field */
    private YearMonthDay dtVencimentoProduto;


    /* persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;


    /* persistent field */
    private com.mercurio.lms.pendencia.model.UnidadeProduto unidadeProdutoByIdUnidadeProduto;

    /* persistent field */
    private com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto;

    /* persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /* persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /* persistent field */
    private com.mercurio.lms.pendencia.model.EntradaPendenciaMatriz entradaPendenciaMatriz;

    /* persistent field */
    private com.mercurio.lms.configuracoes.model.Foto foto;

    /* persistent field */
    private List mercadoriaLotePendencias;

    public Long getIdMercadoriaPendenciaMz() {
        return this.idMercadoriaPendenciaMz;
    }

    public void setIdMercadoriaPendenciaMz(Long idMercadoriaPendenciaMz) {
        this.idMercadoriaPendenciaMz = idMercadoriaPendenciaMz;
    }

    public String getDsProduto() {
        return this.dsProduto;
    }

    public void setDsProduto(String dsProduto) {
        this.dsProduto = dsProduto;
    }

    public Integer getQtUnidadesPorVolume() {
        return this.qtUnidadesPorVolume;
    }

    public void setQtUnidadesPorVolume(Integer qtUnidadesPorVolume) {
        this.qtUnidadesPorVolume = qtUnidadesPorVolume;
    }

    public BigDecimal getVlProduto() {
        return this.vlProduto;
    }

    public void setVlProduto(BigDecimal vlProduto) {
        this.vlProduto = vlProduto;
    }

    public DomainValue getTpMotivoArmazenagem() {
        return this.tpMotivoArmazenagem;
    }

    public void setTpMotivoArmazenagem(DomainValue tpMotivoArmazenagem) {
        this.tpMotivoArmazenagem = tpMotivoArmazenagem;
    }

    public DomainValue getTpDisposicaoPendencia() {
        return this.tpDisposicaoPendencia;
    }

    public void setTpDisposicaoPendencia(DomainValue tpDisposicaoPendencia) {
        this.tpDisposicaoPendencia = tpDisposicaoPendencia;
    }

    public String getCdProduto() {
        return this.cdProduto;
    }

    public void setCdProduto(String cdProduto) {
        this.cdProduto = cdProduto;
    }

    public YearMonthDay getDtVencimentoProduto() {
        return this.dtVencimentoProduto;
    }

    public void setDtVencimentoProduto(YearMonthDay dtVencimentoProduto) {
        this.dtVencimentoProduto = dtVencimentoProduto;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.pendencia.model.UnidadeProduto getUnidadeProdutoByIdUnidadeProduto() {
        return this.unidadeProdutoByIdUnidadeProduto;
    }

	public void setUnidadeProdutoByIdUnidadeProduto(
			com.mercurio.lms.pendencia.model.UnidadeProduto unidadeProdutoByIdUnidadeProduto) {
        this.unidadeProdutoByIdUnidadeProduto = unidadeProdutoByIdUnidadeProduto;
    }

    public com.mercurio.lms.expedicao.model.NaturezaProduto getNaturezaProduto() {
        return this.naturezaProduto;
    }

	public void setNaturezaProduto(
			com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto) {
        this.naturezaProduto = naturezaProduto;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public com.mercurio.lms.pendencia.model.EntradaPendenciaMatriz getEntradaPendenciaMatriz() {
        return this.entradaPendenciaMatriz;
    }

	public void setEntradaPendenciaMatriz(
			com.mercurio.lms.pendencia.model.EntradaPendenciaMatriz entradaPendenciaMatriz) {
        this.entradaPendenciaMatriz = entradaPendenciaMatriz;
    }

    public com.mercurio.lms.configuracoes.model.Foto getFoto() {
        return this.foto;
    }

    public void setFoto(com.mercurio.lms.configuracoes.model.Foto foto) {
        this.foto = foto;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.MercadoriaLotePendencia.class)     
    public List getMercadoriaLotePendencias() {
        return this.mercadoriaLotePendencias;
    }

    public void setMercadoriaLotePendencias(List mercadoriaLotePendencias) {
        this.mercadoriaLotePendencias = mercadoriaLotePendencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMercadoriaPendenciaMz",
				getIdMercadoriaPendenciaMz()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MercadoriaPendenciaMz))
			return false;
        MercadoriaPendenciaMz castOther = (MercadoriaPendenciaMz) other;
		return new EqualsBuilder().append(this.getIdMercadoriaPendenciaMz(),
				castOther.getIdMercadoriaPendenciaMz()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMercadoriaPendenciaMz())
            .toHashCode();
    }

}
