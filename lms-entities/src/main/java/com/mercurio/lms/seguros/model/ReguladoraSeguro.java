package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReguladoraSeguro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idReguladora;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private String nmServicoLiberacaoPrestado;

    /** nullable persistent field */
    private String dsEnderecoWeb;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private List reguladoraSeguradoras;

    /** persistent field */
    private List liberacaoReguladoras;

    /** persistent field */
    private List regraLiberacaoReguladoras;

    /** persistent field */
    private List processoSinistros;

    /** persistent field */
    private List apoliceSeguros;

    /** persistent field */
    private List enquadramentoRegras;

    /** persistent field */
    private List seguroClientes;

    /** persistent field */
    private List escoltaReguladoras;

    /** persistent field */
    private List postoControles;

    public Long getIdReguladora() {
        return this.idReguladora;
    }

    public void setIdReguladora(Long idReguladora) {
        this.idReguladora = idReguladora;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getNmServicoLiberacaoPrestado() {
        return this.nmServicoLiberacaoPrestado;
    }

    public void setNmServicoLiberacaoPrestado(String nmServicoLiberacaoPrestado) {
        this.nmServicoLiberacaoPrestado = nmServicoLiberacaoPrestado;
    }

    public String getDsEnderecoWeb() {
        return this.dsEnderecoWeb;
    }

    public void setDsEnderecoWeb(String dsEnderecoWeb) {
        this.dsEnderecoWeb = dsEnderecoWeb;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ReguladoraSeguradora.class)     
    public List getReguladoraSeguradoras() {
        return this.reguladoraSeguradoras;
    }

    public void setReguladoraSeguradoras(List reguladoraSeguradoras) {
        this.reguladoraSeguradoras = reguladoraSeguradoras;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora.class)     
    public List getLiberacaoReguladoras() {
        return this.liberacaoReguladoras;
    }

    public void setLiberacaoReguladoras(List liberacaoReguladoras) {
        this.liberacaoReguladoras = liberacaoReguladoras;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.RegraLiberacaoReguladora.class)     
    public List getRegraLiberacaoReguladoras() {
        return this.regraLiberacaoReguladoras;
    }

    public void setRegraLiberacaoReguladoras(List regraLiberacaoReguladoras) {
        this.regraLiberacaoReguladoras = regraLiberacaoReguladoras;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ProcessoSinistro.class)     
    public List getProcessoSinistros() {
        return this.processoSinistros;
    }

    public void setProcessoSinistros(List processoSinistros) {
        this.processoSinistros = processoSinistros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PostoPassagemCc.class)     
    public List getApoliceSeguros() {
        return this.apoliceSeguros;
    }

    public void setApoliceSeguros(List apoliceSeguros) {
        this.apoliceSeguros = apoliceSeguros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.EnquadramentoRegra.class)     
    public List getEnquadramentoRegras() {
        return this.enquadramentoRegras;
    }

    public void setEnquadramentoRegras(List enquadramentoRegras) {
        this.enquadramentoRegras = enquadramentoRegras;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.SeguroCliente.class)     
    public List getSeguroClientes() {
        return this.seguroClientes;
    }

    public void setSeguroClientes(List seguroClientes) {
        this.seguroClientes = seguroClientes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.EscoltaReguladora.class)     
    public List getEscoltaReguladoras() {
        return this.escoltaReguladoras;
    }

    public void setEscoltaReguladoras(List escoltaReguladoras) {
        this.escoltaReguladoras = escoltaReguladoras;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.PostoControle.class)     
    public List getPostoControles() {
        return this.postoControles;
    }

    public void setPostoControles(List postoControles) {
        this.postoControles = postoControles;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idReguladora",
				getIdReguladora()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReguladoraSeguro))
			return false;
        ReguladoraSeguro castOther = (ReguladoraSeguro) other;
		return new EqualsBuilder().append(this.getIdReguladora(),
				castOther.getIdReguladora()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdReguladora()).toHashCode();
    }

}
