package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class Servico implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SERVICO_PADRAO = "SERVICO_PADRAO";
	
    /** identifier field */
    private Long idServico;

    /** persistent field */
    private VarcharI18n dsServico;

    /** persistent field */
    private DomainValue tpModal;

    /** persistent field */
    private DomainValue tpAbrangencia;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.TipoServico tipoServico;

    private Boolean blGeraMcd;
    
    /** persistent field */
    private List zonaServicos;

    /** persistent field */
    private List simulacoes;

    /** persistent field */
    private List boxFinalidades;

    /** persistent field */
    private List restricaoColetas;

    /** persistent field */
    private List tabelaDivisaoClientes;

    /** persistent field */
    private List servicoOferecidos;

    /** persistent field */
    private List detalheColetas;

    /** persistent field */
    private List prazoEntregaClientes;

    /** persistent field */
    private List contatos;

    /** persistent field */
    private List configuracaoComunicacoes;

    /** persistent field */
    private List mcdMunicipioFiliais;

    /** persistent field */
    private List substAtendimentoFiliais;

    /** persistent field */
    private List doctoServicos;

    /** persistent field */
    private List filialServicos;

    /** persistent field */
    private List servicoRotaViagems;

    /** persistent field */
    private List fluxoFiliais;

    /** persistent field */
    private List composicaoServicos;

    /** persistent field */
    private List operacaoServicoLocalizas;

    /** persistent field */
    private List tipoTabelaPrecos;

    /** persistent field */
    private List cotacoes;

    /** persistent field */
    private List milkRemetentes;
    
    /** persistent field */ 
    private String sgServico;

    public Long getIdServico() {
        return this.idServico;
    }

    public void setIdServico(Long idServico) {
        this.idServico = idServico;
    }

    public VarcharI18n getDsServico() {
		return dsServico;
    }

	public void setDsServico(VarcharI18n dsServico) {
        this.dsServico = dsServico;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }

    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.configuracoes.model.TipoServico getTipoServico() {
        return this.tipoServico;
    }

	public void setTipoServico(
			com.mercurio.lms.configuracoes.model.TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.ZonaServico.class)     
    public List getZonaServicos() {
        return this.zonaServicos;
    }

    public void setZonaServicos(List zonaServicos) {
        this.zonaServicos = zonaServicos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Simulacao.class)     
    public List getSimulacoes() {
        return this.simulacoes;
    }

    public void setSimulacoes(List simulacoes) {
        this.simulacoes = simulacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.BoxFinalidade.class)     
    public List getBoxFinalidades() {
        return this.boxFinalidades;
    }

    public void setBoxFinalidades(List boxFinalidades) {
        this.boxFinalidades = boxFinalidades;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.RestricaoColeta.class)     
    public List getRestricaoColetas() {
        return this.restricaoColetas;
    }

    public void setRestricaoColetas(List restricaoColetas) {
        this.restricaoColetas = restricaoColetas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.TabelaDivisaoCliente.class)     
    public List getTabelaDivisaoClientes() {
        return this.tabelaDivisaoClientes;
    }

    public void setTabelaDivisaoClientes(List tabelaDivisaoClientes) {
        this.tabelaDivisaoClientes = tabelaDivisaoClientes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ServicoOferecido.class)     
    public List getServicoOferecidos() {
        return this.servicoOferecidos;
    }

    public void setServicoOferecidos(List servicoOferecidos) {
        this.servicoOferecidos = servicoOferecidos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.DetalheColeta.class)     
    public List getDetalheColetas() {
        return this.detalheColetas;
    }

    public void setDetalheColetas(List detalheColetas) {
        this.detalheColetas = detalheColetas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.PrazoEntregaCliente.class)     
    public List getPrazoEntregaClientes() {
        return this.prazoEntregaClientes;
    }

    public void setPrazoEntregaClientes(List prazoEntregaClientes) {
        this.prazoEntregaClientes = prazoEntregaClientes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.Contato.class)     
    public List getContatos() {
        return this.contatos;
    }

    public void setContatos(List contatos) {
        this.contatos = contatos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sim.model.ConfiguracaoComunicacao.class)     
    public List getConfiguracaoComunicacoes() {
        return this.configuracaoComunicacoes;
    }

    public void setConfiguracaoComunicacoes(List configuracaoComunicacoes) {
        this.configuracaoComunicacoes = configuracaoComunicacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.McdMunicipioFilial.class)     
    public List getMcdMunicipioFiliais() {
        return this.mcdMunicipioFiliais;
    }

    public void setMcdMunicipioFiliais(List mcdMunicipioFiliais) {
        this.mcdMunicipioFiliais = mcdMunicipioFiliais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.SubstAtendimentoFilial.class)     
    public List getSubstAtendimentoFiliais() {
        return this.substAtendimentoFiliais;
    }

    public void setSubstAtendimentoFiliais(List substAtendimentoFiliais) {
        this.substAtendimentoFiliais = substAtendimentoFiliais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DoctoServico.class)     
    public List getDoctoServicos() {
        return this.doctoServicos;
    }

    public void setDoctoServicos(List doctoServicos) {
        this.doctoServicos = doctoServicos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.FilialServico.class)     
    public List getFilialServicos() {
        return this.filialServicos;
    }

    public void setFilialServicos(List filialServicos) {
        this.filialServicos = filialServicos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.ServicoRotaViagem.class)     
    public List getServicoRotaViagems() {
        return this.servicoRotaViagems;
    }

    public void setServicoRotaViagems(List servicoRotaViagems) {
        this.servicoRotaViagems = servicoRotaViagems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.FluxoFilial.class)     
    public List getFluxoFiliais() {
        return this.fluxoFiliais;
    }

    public void setFluxoFiliais(List fluxoFiliais) {
        this.fluxoFiliais = fluxoFiliais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ComposicaoServico.class)     
    public List getComposicaoServicos() {
        return this.composicaoServicos;
    }

    public void setComposicaoServicos(List composicaoServicos) {
        this.composicaoServicos = composicaoServicos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.OperacaoServicoLocaliza.class)     
    public List getOperacaoServicoLocalizas() {
        return this.operacaoServicoLocalizas;
    }

    public void setOperacaoServicoLocalizas(List operacaoServicoLocalizas) {
        this.operacaoServicoLocalizas = operacaoServicoLocalizas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco.class)     
    public List getTipoTabelaPrecos() {
        return this.tipoTabelaPrecos;
    }

    public void setTipoTabelaPrecos(List tipoTabelaPrecos) {
        this.tipoTabelaPrecos = tipoTabelaPrecos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Cotacao.class)     
    public List getCotacoes() {
        return this.cotacoes;
    }

    public void setCotacoes(List cotacoes) {
        this.cotacoes = cotacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.MilkRemetente.class)     
    public List getMilkRemetentes() {
        return milkRemetentes;
    }
    
    public void setMilkRemetentes(List milkRemetentes) {
        this.milkRemetentes = milkRemetentes;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idServico", getIdServico())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Servico))
			return false;
        Servico castOther = (Servico) other;
		return new EqualsBuilder().append(this.getIdServico(),
				castOther.getIdServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdServico()).toHashCode();
    }

	/**
	 * @return Returns the blGeraMcd.
	 */
	public Boolean getBlGeraMcd() {
		return blGeraMcd;
	}

	/**
	 * @param blGeraMcd
	 *            The blGeraMcd to set.
	 */
	public void setBlGeraMcd(Boolean blGeraMcd) {
		this.blGeraMcd = blGeraMcd;
	}

	public String getSgServico() {
		return sgServico;
	}

	public void setSgServico(String sgServico) {
		this.sgServico = sgServico;
	}
	
	public boolean hasModal(String modal) {
		return 
			getTpModal() != null && 
			getTpModal().getValue() != null &&
			getTpModal().getValue().equalsIgnoreCase(modal);
	}

	public String getNomeServico() {
		return getDsServico() == null ? StringUtils.EMPTY : getDsServico().toString();
	}

}
