package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.util.List;

import com.mercurio.lms.municipios.model.Empresa;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TarifaPreco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTarifaPreco;

    /** persistent field */
    private String cdTarifaPreco;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private Long nrKmInicial;

    /** nullable persistent field */
    private Long nrKmFinal;

    /** nullable persistent field */
    private Long nrKmInicialAtual;
    
    /** nullable persistent field */
    private Long nrKmFinalAtual;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private List tarifaColetas;

    /** persistent field */
    private List mcdMunicipioFiliais;

    /** persistent field */
    private List tarifaPrecoRotas;

    /** persistent field */
    private List precoFretes;

    /** persistent field */
    private List valorFaixaProgressivas;
    
    public TarifaPreco() {
    	
    }

    public TarifaPreco(Long idTarifaPreco, String cdTarifaPreco, DomainValue tpSituacao, Long nrKmInicial, Long nrKmFinal, Long nrKmInicialAtual, Long nrKmFinalAtual, Empresa empresa, List tarifaColetas, List mcdMunicipioFiliais, List tarifaPrecoRotas, List precoFretes, List valorFaixaProgressivas) {
        this.idTarifaPreco = idTarifaPreco;
        this.cdTarifaPreco = cdTarifaPreco;
        this.tpSituacao = tpSituacao;
        this.nrKmInicial = nrKmInicial;
        this.nrKmFinal = nrKmFinal;
        this.nrKmInicialAtual = nrKmInicialAtual;
        this.nrKmFinalAtual = nrKmFinalAtual;
        this.empresa = empresa;
        this.tarifaColetas = tarifaColetas;
        this.mcdMunicipioFiliais = mcdMunicipioFiliais;
        this.tarifaPrecoRotas = tarifaPrecoRotas;
        this.precoFretes = precoFretes;
        this.valorFaixaProgressivas = valorFaixaProgressivas;
    }

    public TarifaPreco(Long id) {
    	this.idTarifaPreco = id;
    }

	public Long getIdTarifaPreco() {
        return this.idTarifaPreco;
    }

    public void setIdTarifaPreco(Long idTarifaPreco) {
        this.idTarifaPreco = idTarifaPreco;
    }

    public String getCdTarifaPreco() {
        return this.cdTarifaPreco;
    }

    public void setCdTarifaPreco(String cdTarifaPreco) {
        this.cdTarifaPreco = cdTarifaPreco;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Long getNrKmInicial() {
        return this.nrKmInicial;
    }

    public void setNrKmInicial(Long nrKmInicial) {
        this.nrKmInicial = nrKmInicial;
    }

    public Long getNrKmFinal() {
        return this.nrKmFinal;
    }

    public void setNrKmFinal(Long nrKmFinal) {
        this.nrKmFinal = nrKmFinal;
    }

    public Long getNrKmInicialAtual() {
    	return this.nrKmInicialAtual;
    }
    
    public void setNrKmInicialAtual(Long nrKmInicial) {
    	this.nrKmInicialAtual = nrKmInicial;
    }
    
    public Long getNrKmFinalAtual() {
    	return this.nrKmFinalAtual;
    }
    
    public void setNrKmFinalAtual(Long nrKmFinal) {
    	this.nrKmFinalAtual = nrKmFinal;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.TarifaColeta.class)     
    public List getTarifaColetas() {
        return this.tarifaColetas;
    }

    public void setTarifaColetas(List tarifaColetas) {
        this.tarifaColetas = tarifaColetas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.McdMunicipioFilial.class)     
    public List getMcdMunicipioFiliais() {
        return this.mcdMunicipioFiliais;
    }

    public void setMcdMunicipioFiliais(List mcdMunicipioFiliais) {
        this.mcdMunicipioFiliais = mcdMunicipioFiliais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota.class)     
    public List getTarifaPrecoRotas() {
        return this.tarifaPrecoRotas;
    }

    public void setTarifaPrecoRotas(List tarifaPrecoRotas) {
        this.tarifaPrecoRotas = tarifaPrecoRotas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.PrecoFrete.class)     
    public List getPrecoFretes() {
        return this.precoFretes;
    }

    public void setPrecoFretes(List precoFretes) {
        this.precoFretes = precoFretes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva.class)     
    public List getValorFaixaProgressivas() {
        return this.valorFaixaProgressivas;
    }

    public void setValorFaixaProgressivas(List valorFaixaProgressivas) {
        this.valorFaixaProgressivas = valorFaixaProgressivas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTarifaPreco",
				getIdTarifaPreco()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TarifaPreco))
			return false;
        TarifaPreco castOther = (TarifaPreco) other;
		return new EqualsBuilder().append(this.getIdTarifaPreco(),
				castOther.getIdTarifaPreco()).isEquals();
    }

    public boolean equals(TarifaPreco rp){
		return rp != null
				&& this.getIdTarifaPreco().equals(rp.getIdTarifaPreco());
    }
    
    public int hashCode() {
		return new HashCodeBuilder().append(getIdTarifaPreco()).toHashCode();
    }
}
