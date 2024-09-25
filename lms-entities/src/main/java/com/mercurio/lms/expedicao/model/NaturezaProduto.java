package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class NaturezaProduto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNaturezaProduto;

    /** persistent field */
    private VarcharI18n dsNaturezaProduto;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List produtos;

    /** persistent field */
    private List substAtendimentoFiliais;

    /** persistent field */
    private List enquadramentoRegras;

    /** persistent field */
    private List conhecimentos;

    /** persistent field */
    private List itemMdas;

    /** persistent field */
    private List divisaoProdutos;

    /** persistent field */
    private List ctoCtoCooperadas;

    /** persistent field */
    private List awbs;

    /** persistent field */
    private List detalheColetas;

    /** persistent field */
    private List cotacoes;

    /** persistent field */
    private List mercadoriaPendenciaMzs;
    
    /** persistent field */
    private List milkRemetentes;

    public Long getIdNaturezaProduto() {
        return this.idNaturezaProduto;
    }

    public void setIdNaturezaProduto(Long idNaturezaProduto) {
        this.idNaturezaProduto = idNaturezaProduto;
    }

    public VarcharI18n getDsNaturezaProduto() {
		return dsNaturezaProduto;
    }

	public void setDsNaturezaProduto(VarcharI18n dsNaturezaProduto) {
        this.dsNaturezaProduto = dsNaturezaProduto;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Produto.class)     
    public List getProdutos() {
        return this.produtos;
    }

    public void setProdutos(List produtos) {
        this.produtos = produtos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.SubstAtendimentoFilial.class)     
    public List getSubstAtendimentoFiliais() {
        return this.substAtendimentoFiliais;
    }

    public void setSubstAtendimentoFiliais(List substAtendimentoFiliais) {
        this.substAtendimentoFiliais = substAtendimentoFiliais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.EnquadramentoRegra.class)     
    public List getEnquadramentoRegras() {
        return this.enquadramentoRegras;
    }

    public void setEnquadramentoRegras(List enquadramentoRegras) {
        this.enquadramentoRegras = enquadramentoRegras;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Conhecimento.class)     
    public List getConhecimentos() {
        return this.conhecimentos;
    }

    public void setConhecimentos(List conhecimentos) {
        this.conhecimentos = conhecimentos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.ItemMda.class)     
    public List getItemMdas() {
        return this.itemMdas;
    }

    public void setItemMdas(List itemMdas) {
        this.itemMdas = itemMdas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DivisaoProduto.class)     
    public List getDivisaoProdutos() {
        return this.divisaoProdutos;
    }

    public void setDivisaoProdutos(List divisaoProdutos) {
        this.divisaoProdutos = divisaoProdutos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoCtoCooperada.class)     
    public List getCtoCtoCooperadas() {
        return this.ctoCtoCooperadas;
    }

    public void setCtoCtoCooperadas(List ctoCtoCooperadas) {
        this.ctoCtoCooperadas = ctoCtoCooperadas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Awb.class)     
    public List getAwbs() {
        return this.awbs;
    }

    public void setAwbs(List awbs) {
        this.awbs = awbs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.DetalheColeta.class)     
    public List getDetalheColetas() {
        return this.detalheColetas;
    }

    public void setDetalheColetas(List detalheColetas) {
        this.detalheColetas = detalheColetas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Cotacao.class)     
    public List getCotacoes() {
        return this.cotacoes;
    }

    public void setCotacoes(List cotacoes) {
        this.cotacoes = cotacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz.class)     
    public List getMercadoriaPendenciaMzs() {
        return this.mercadoriaPendenciaMzs;
    }

    public void setMercadoriaPendenciaMzs(List mercadoriaPendenciaMzs) {
        this.mercadoriaPendenciaMzs = mercadoriaPendenciaMzs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.MilkRemetente.class)     
    public List getMilkRemetentes() {
        return milkRemetentes;
    }
    
    public void setMilkRemetentes(List milkRemetentes) {
        this.milkRemetentes = milkRemetentes;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idNaturezaProduto",
				getIdNaturezaProduto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NaturezaProduto))
			return false;
        NaturezaProduto castOther = (NaturezaProduto) other;
		return new EqualsBuilder().append(this.getIdNaturezaProduto(),
				castOther.getIdNaturezaProduto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNaturezaProduto())
            .toHashCode();
    }

}
