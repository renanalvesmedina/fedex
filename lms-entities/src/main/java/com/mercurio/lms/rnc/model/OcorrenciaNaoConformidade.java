package com.mercurio.lms.rnc.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class OcorrenciaNaoConformidade implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOcorrenciaNaoConformidade;

    /** persistent field */
    private Integer nrOcorrenciaNc;

    /** persistent field */
    private String dsOcorrenciaNc;

    /** nullable persistent field */
    private String dsCausaNc;

    /** persistent field */
    private DateTime dhInclusao;

    /** persistent field */
    private Integer qtVolumes;

    /** persistent field */
    private Boolean blCaixaReaproveitada;

    /** persistent field */
    private DomainValue tpStatusOcorrenciaNc;

    /** nullable persistent field */
    private BigDecimal vlOcorrenciaNc;
    
    /** nullable persistent field */
    private String dsCaixaReaproveitada;
    
    /** persistent field */
    private Integer nrRncLegado;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.rnc.model.MotivoAberturaNc motivoAberturaNc;

    /** persistent field */
    private com.mercurio.lms.rnc.model.CausaNaoConformidade causaNaoConformidade;

    /** persistent field */
    private com.mercurio.lms.rnc.model.DescricaoPadraoNc descricaoPadraoNc;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.Manifesto manifesto;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialResponsavel;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialAbertura;

    /** persistent field */
    private com.mercurio.lms.rnc.model.NaoConformidade naoConformidade;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialLegado;
    
    /** persistent field */
    private List notaOcorrenciaNcs;

    /** persistent field */
    private List negociacoes;

    /** persistent field */
    private List disposicoes;
    
    /** persistent field */
    private List fotoOcorrencias;

    /** persistent field */
    private List caractProdutoOcorrencias;

    private List itemOcorrencias;

    public Long getIdOcorrenciaNaoConformidade() {
        return this.idOcorrenciaNaoConformidade;
    }

    public void setIdOcorrenciaNaoConformidade(Long idOcorrenciaNaoConformidade) {
        this.idOcorrenciaNaoConformidade = idOcorrenciaNaoConformidade;
    }

    public Integer getNrOcorrenciaNc() {
        return this.nrOcorrenciaNc;
    }

    public void setNrOcorrenciaNc(Integer nrOcorrenciaNc) {
        this.nrOcorrenciaNc = nrOcorrenciaNc;
    }

    public String getDsOcorrenciaNc() {
        return this.dsOcorrenciaNc;
    }

    public void setDsOcorrenciaNc(String dsOcorrenciaNc) {
        this.dsOcorrenciaNc = dsOcorrenciaNc;
    }

    public String getDsCausaNc() {
        return this.dsCausaNc;
    }

    public void setDsCausaNc(String dsCausaNc) {
        this.dsCausaNc = dsCausaNc;
    }

    public DateTime getDhInclusao() {
        return this.dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    public Integer getQtVolumes() {
        return this.qtVolumes;
    }

    public void setQtVolumes(Integer qtVolumes) {
        this.qtVolumes = qtVolumes;
    }

    public Boolean getBlCaixaReaproveitada() {
        return this.blCaixaReaproveitada;
    }

    public void setBlCaixaReaproveitada(Boolean blCaixaReaproveitada) {
        this.blCaixaReaproveitada = blCaixaReaproveitada;
    }

    public DomainValue getTpStatusOcorrenciaNc() {
        return this.tpStatusOcorrenciaNc;
    }

    public void setTpStatusOcorrenciaNc(DomainValue tpStatusOcorrenciaNc) {
        this.tpStatusOcorrenciaNc = tpStatusOcorrenciaNc;
    }

    public BigDecimal getVlOcorrenciaNc() {
        return this.vlOcorrenciaNc;
    }

    public void setVlOcorrenciaNc(BigDecimal vlOcorrenciaNc) {
        this.vlOcorrenciaNc = vlOcorrenciaNc;
    }

    public String getDsCaixaReaproveitada() {
		return dsCaixaReaproveitada;
	}

	public void setDsCaixaReaproveitada(String dsCaixaReaproveitada) {
		this.dsCaixaReaproveitada = dsCaixaReaproveitada;
	}

	public Integer getNrRncLegado() {
		return nrRncLegado;
	}

	public void setNrRncLegado(Integer nrRncLegado) {
		this.nrRncLegado = nrRncLegado;
	}

	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.rnc.model.MotivoAberturaNc getMotivoAberturaNc() {
        return this.motivoAberturaNc;
    }

	public void setMotivoAberturaNc(
			com.mercurio.lms.rnc.model.MotivoAberturaNc motivoAberturaNc) {
        this.motivoAberturaNc = motivoAberturaNc;
    }

    public com.mercurio.lms.rnc.model.CausaNaoConformidade getCausaNaoConformidade() {
        return this.causaNaoConformidade;
    }

	public void setCausaNaoConformidade(
			com.mercurio.lms.rnc.model.CausaNaoConformidade causaNaoConformidade) {
        this.causaNaoConformidade = causaNaoConformidade;
    }

    public com.mercurio.lms.rnc.model.DescricaoPadraoNc getDescricaoPadraoNc() {
        return this.descricaoPadraoNc;
    }

	public void setDescricaoPadraoNc(
			com.mercurio.lms.rnc.model.DescricaoPadraoNc descricaoPadraoNc) {
        this.descricaoPadraoNc = descricaoPadraoNc;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.carregamento.model.Manifesto getManifesto() {
        return this.manifesto;
    }

	public void setManifesto(
			com.mercurio.lms.carregamento.model.Manifesto manifesto) {
        this.manifesto = manifesto;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialResponsavel() {
        return this.filialByIdFilialResponsavel;
    }

	public void setFilialByIdFilialResponsavel(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialResponsavel) {
        this.filialByIdFilialResponsavel = filialByIdFilialResponsavel;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialAbertura() {
        return this.filialByIdFilialAbertura;
    }

	public void setFilialByIdFilialAbertura(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialAbertura) {
        this.filialByIdFilialAbertura = filialByIdFilialAbertura;
    }

    public com.mercurio.lms.rnc.model.NaoConformidade getNaoConformidade() {
        return this.naoConformidade;
    }

	public void setNaoConformidade(
			com.mercurio.lms.rnc.model.NaoConformidade naoConformidade) {
        this.naoConformidade = naoConformidade;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialLegado() {
		return filialByIdFilialLegado;
	}

	public void setFilialByIdFilialLegado(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialLegado) {
		this.filialByIdFilialLegado = filialByIdFilialLegado;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.rnc.model.NotaOcorrenciaNc.class)     
    public List getNotaOcorrenciaNcs() {
        return this.notaOcorrenciaNcs;
    }

    public void setNotaOcorrenciaNcs(List notaOcorrenciaNcs) {
        this.notaOcorrenciaNcs = notaOcorrenciaNcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.Negociacao.class)     
    public List getNegociacoes() {
        return this.negociacoes;
    }

    public void setNegociacoes(List negociacoes) {
        this.negociacoes = negociacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.FotoOcorrencia.class)     
    public List getFotoOcorrencias() {
        return this.fotoOcorrencias;
    }

    public void setFotoOcorrencias(List fotoOcorrencias) {
        this.fotoOcorrencias = fotoOcorrencias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.CaractProdutoOcorrencia.class)     
    public List getCaractProdutoOcorrencias() {
        return this.caractProdutoOcorrencias;
    }

    public void setCaractProdutoOcorrencias(List caractProdutoOcorrencias) {
        this.caractProdutoOcorrencias = caractProdutoOcorrencias;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.Disposicao.class)     
    public List getDisposicoes() {
        return disposicoes;
    }
    
    public void setDisposicoes(List disposicoes) {
        this.disposicoes = disposicoes;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idOcorrenciaNaoConformidade",
				getIdOcorrenciaNaoConformidade()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaNaoConformidade))
			return false;
        OcorrenciaNaoConformidade castOther = (OcorrenciaNaoConformidade) other;
		return new EqualsBuilder().append(
				this.getIdOcorrenciaNaoConformidade(),
				castOther.getIdOcorrenciaNaoConformidade()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaNaoConformidade())
            .toHashCode();
    }

	public List getItemOcorrencias() {
		return itemOcorrencias;
	}

	public void setItemOcorrencias(List itemOcorrencias) {
		this.itemOcorrencias = itemOcorrencias;
	}
}