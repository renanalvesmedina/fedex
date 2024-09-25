package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author Hibernate CodeGenerator */
public class UnidadeFederativa implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idUnidadeFederativa;

    /** persistent field */
    private String sgUnidadeFederativa;

    /** persistent field */
    private String nmUnidadeFederativa;

    /** persistent field */
    private Boolean blIcmsPedagio;

    /** persistent field */
    private Boolean blFronteiraRapida;

    /** persistent field */
    private Boolean blIncideIss;
    
    private Boolean blCobraTas;
    
    /** persistent field */
    private Boolean blCobraSuframa;
    
    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private Long nrIbge;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaSefazVirtual;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RegiaoGeografica regiaoGeografica;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;

    /** persistent field */
    private List rotaPrecosByIdUfDestino;

    /** persistent field */
    private List rotaPrecosByIdUfOrigem;

    /** persistent field */
    private List simulacoesByIdUfDestino;

    /** persistent field */
    private List simulacoesByIdUfOrigem;

    /** persistent field */
    private List municipioRegionalClientes;

    /** persistent field */
    private List rodovias;

    /** persistent field */
    private List municipioFilialUfOrigems;

    /** persistent field */
    private List parametroClientesByIdUfOrigem;

    /** persistent field */
    private List parametroClientesByIdUfDestino;

    /** persistent field */
    private List horarioCorteClientes;

    /** persistent field */
    private List prazoEntregaClientesByIdUfOrigem;

    /** persistent field */
    private List prazoEntregaClientesByIdUfDestino;

    /** persistent field */
    private List feriados;

    /** persistent field */
    private List descricaoTributacaoIcms;

    /** persistent field */
    private List referenciaFreteCarreteirosByIdUnidadeDestino;

    /** persistent field */
    private List referenciaFreteCarreteirosByIdUnidadeFederativaOrigem;

    /** persistent field */
    private List potencialComercialClientesByIdUfOrigem;

    /** persistent field */
    private List potencialComercialClientesByIdUfDestino;

    /** persistent field */
    private List substAtendimentoFiliais;

    /** persistent field */
    private List aliquotasIcmsUFDestino;

    /** persistent field */
    private List aliquotasIcmsUFOrigem;

    /** persistent field */
    private List ufDispositivoLegais;

    /** persistent field */
    private List municipios;
    
    /** persistent field */
    private Long nrPrazoCancCte;

	public Long getIdUnidadeFederativa() {
        return this.idUnidadeFederativa;
    }

    public void setIdUnidadeFederativa(Long idUnidadeFederativa) {
        this.idUnidadeFederativa = idUnidadeFederativa;
    }

    public String getSgUnidadeFederativa() {
        return this.sgUnidadeFederativa;
    }

    public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
        this.sgUnidadeFederativa = sgUnidadeFederativa;
    }

    public String getNmUnidadeFederativa() {
        return this.nmUnidadeFederativa;
    }

    public void setNmUnidadeFederativa(String nmUnidadeFederativa) {
        this.nmUnidadeFederativa = nmUnidadeFederativa;
    }

    public Boolean getBlIcmsPedagio() {
        return this.blIcmsPedagio;
    }

    public void setBlIcmsPedagio(Boolean blIcmsPedagio) {
        this.blIcmsPedagio = blIcmsPedagio;
    }

    public Boolean getBlFronteiraRapida() {
        return this.blFronteiraRapida;
    }

    public void setBlFronteiraRapida(Boolean blFronteiraRapida) {
        this.blFronteiraRapida = blFronteiraRapida;
    }

    public Boolean getBlIncideIss() {
        return this.blIncideIss;
    }

    public void setBlIncideIss(Boolean blIncideIss) {
        this.blIncideIss = blIncideIss;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.municipios.model.RegiaoGeografica getRegiaoGeografica() {
        return this.regiaoGeografica;
    }

	public void setRegiaoGeografica(
			com.mercurio.lms.municipios.model.RegiaoGeografica regiaoGeografica) {
        this.regiaoGeografica = regiaoGeografica;
    }

    public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.RotaPreco.class)     
    public List getRotaPrecosByIdUfDestino() {
        return this.rotaPrecosByIdUfDestino;
    }

    public void setRotaPrecosByIdUfDestino(List rotaPrecosByIdUfDestino) {
        this.rotaPrecosByIdUfDestino = rotaPrecosByIdUfDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.RotaPreco.class)     
    public List getRotaPrecosByIdUfOrigem() {
        return this.rotaPrecosByIdUfOrigem;
    }

    public void setRotaPrecosByIdUfOrigem(List rotaPrecosByIdUfOrigem) {
        this.rotaPrecosByIdUfOrigem = rotaPrecosByIdUfOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Simulacao.class)     
    public List getSimulacoesByIdUfDestino() {
        return this.simulacoesByIdUfDestino;
    }

    public void setSimulacoesByIdUfDestino(List simulacoesByIdUfDestino) {
        this.simulacoesByIdUfDestino = simulacoesByIdUfDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Simulacao.class)     
    public List getSimulacoesByIdUfOrigem() {
        return this.simulacoesByIdUfOrigem;
    }

    public void setSimulacoesByIdUfOrigem(List simulacoesByIdUfOrigem) {
        this.simulacoesByIdUfOrigem = simulacoesByIdUfOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.MunicipioRegionalCliente.class)     
    public List getMunicipioRegionalClientes() {
        return this.municipioRegionalClientes;
    }

    public void setMunicipioRegionalClientes(List municipioRegionalClientes) {
        this.municipioRegionalClientes = municipioRegionalClientes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Rodovia.class)     
    public List getRodovias() {
        return this.rodovias;
    }

    public void setRodovias(List rodovias) {
        this.rodovias = rodovias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MunicipioFilialUFOrigem.class)     
    public List getMunicipioFilialUfOrigems() {
        return this.municipioFilialUfOrigems;
    }

    public void setMunicipioFilialUfOrigems(List municipioFilialUfOrigems) {
        this.municipioFilialUfOrigems = municipioFilialUfOrigems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ParametroCliente.class)     
    public List getParametroClientesByIdUfOrigem() {
        return this.parametroClientesByIdUfOrigem;
    }

	public void setParametroClientesByIdUfOrigem(
			List parametroClientesByIdUfOrigem) {
        this.parametroClientesByIdUfOrigem = parametroClientesByIdUfOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ParametroCliente.class)     
    public List getParametroClientesByIdUfDestino() {
        return this.parametroClientesByIdUfDestino;
    }

	public void setParametroClientesByIdUfDestino(
			List parametroClientesByIdUfDestino) {
        this.parametroClientesByIdUfDestino = parametroClientesByIdUfDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.HorarioCorteCliente.class)     
    public List getHorarioCorteClientes() {
        return this.horarioCorteClientes;
    }

    public void setHorarioCorteClientes(List horarioCorteClientes) {
        this.horarioCorteClientes = horarioCorteClientes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.PrazoEntregaCliente.class)     
    public List getPrazoEntregaClientesByIdUfOrigem() {
        return this.prazoEntregaClientesByIdUfOrigem;
    }

	public void setPrazoEntregaClientesByIdUfOrigem(
			List prazoEntregaClientesByIdUfOrigem) {
        this.prazoEntregaClientesByIdUfOrigem = prazoEntregaClientesByIdUfOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.PrazoEntregaCliente.class)     
    public List getPrazoEntregaClientesByIdUfDestino() {
        return this.prazoEntregaClientesByIdUfDestino;
    }

	public void setPrazoEntregaClientesByIdUfDestino(
			List prazoEntregaClientesByIdUfDestino) {
        this.prazoEntregaClientesByIdUfDestino = prazoEntregaClientesByIdUfDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Feriado.class)     
    public List getFeriados() {
        return this.feriados;
    }

    public void setFeriados(List feriados) {
        this.feriados = feriados;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.DescricaoTributacaoIcms.class)     
    public List getDescricaoTributacaoIcms() {
        return this.descricaoTributacaoIcms;
    }

    public void setDescricaoTributacaoIcms(List descricaoTributacaoIcms) {
        this.descricaoTributacaoIcms = descricaoTributacaoIcms;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaFreteCarreteiro.class)     
    public List getReferenciaFreteCarreteirosByIdUnidadeDestino() {
        return this.referenciaFreteCarreteirosByIdUnidadeDestino;
    }

	public void setReferenciaFreteCarreteirosByIdUnidadeDestino(
			List referenciaFreteCarreteirosByIdUnidadeDestino) {
        this.referenciaFreteCarreteirosByIdUnidadeDestino = referenciaFreteCarreteirosByIdUnidadeDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaFreteCarreteiro.class)     
    public List getReferenciaFreteCarreteirosByIdUnidadeFederativaOrigem() {
        return this.referenciaFreteCarreteirosByIdUnidadeFederativaOrigem;
    }

	public void setReferenciaFreteCarreteirosByIdUnidadeFederativaOrigem(
			List referenciaFreteCarreteirosByIdUnidadeFederativaOrigem) {
        this.referenciaFreteCarreteirosByIdUnidadeFederativaOrigem = referenciaFreteCarreteirosByIdUnidadeFederativaOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.PotencialComercialCliente.class)     
    public List getPotencialComercialClientesByIdUfOrigem() {
        return this.potencialComercialClientesByIdUfOrigem;
    }

	public void setPotencialComercialClientesByIdUfOrigem(
			List potencialComercialClientesByIdUfOrigem) {
        this.potencialComercialClientesByIdUfOrigem = potencialComercialClientesByIdUfOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.PotencialComercialCliente.class)     
    public List getPotencialComercialClientesByIdUfDestino() {
        return this.potencialComercialClientesByIdUfDestino;
    }

	public void setPotencialComercialClientesByIdUfDestino(
			List potencialComercialClientesByIdUfDestino) {
        this.potencialComercialClientesByIdUfDestino = potencialComercialClientesByIdUfDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.SubstAtendimentoFilial.class)     
    public List getSubstAtendimentoFiliais() {
        return this.substAtendimentoFiliais;
    }

    public void setSubstAtendimentoFiliais(List substAtendimentoFiliais) {
        this.substAtendimentoFiliais = substAtendimentoFiliais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.AliquotaIcms.class)     
    public List getAliquotasIcmsUFDestino() {
		return aliquotasIcmsUFDestino;
	}

	public void setAliquotasIcmsUFDestino(List aliquotasIcmsUFDestino) {
		this.aliquotasIcmsUFDestino = aliquotasIcmsUFDestino;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.tributos.model.AliquotaIcms.class)     
	public List getAliquotasIcmsUFOrigem() {
		return aliquotasIcmsUFOrigem;
	}

	public void setAliquotasIcmsUFOrigem(List aliquotasIcmsUFOrigem) {
		this.aliquotasIcmsUFOrigem = aliquotasIcmsUFOrigem;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.UfDispositivoLegal.class)     
    public List getUfDispositivoLegais() {
        return this.ufDispositivoLegais;
    }

    public void setUfDispositivoLegais(List ufDispositivoLegais) {
        this.ufDispositivoLegais = ufDispositivoLegais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Municipio.class)     
    public List getMunicipios() {
        return this.municipios;
    }

    public void setMunicipios(List municipios) {
        this.municipios = municipios;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idUnidadeFederativa",
				getIdUnidadeFederativa()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof UnidadeFederativa))
			return false;
        UnidadeFederativa castOther = (UnidadeFederativa) other;
		return new EqualsBuilder().append(this.getIdUnidadeFederativa(),
				castOther.getIdUnidadeFederativa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdUnidadeFederativa())
            .toHashCode();
    }
    
    public String getSiglaDescricao() {
    	StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(this.sgUnidadeFederativa)
				&& StringUtils.isNotBlank(this.nmUnidadeFederativa)) {
    		sb.append(this.sgUnidadeFederativa);
    		sb.append(" - ");
    		sb.append(this.nmUnidadeFederativa);
    	}
    	
        return sb.toString();
    }

	public Boolean getBlCobraTas() {
		return blCobraTas;
	}

	public void setBlCobraTas(Boolean blCobraTas) {
		this.blCobraTas = blCobraTas;
	}
	
	public Boolean getBlCobraSuframa() {
        return blCobraSuframa;
    }
 
    public void setBlCobraSuframa(Boolean blCobraSuframa) {
        this.blCobraSuframa = blCobraSuframa;
    }

	public Long getNrIbge() {
		return nrIbge;
}

	public void setNrIbge(Long nrIbge) {
		this.nrIbge = nrIbge;
	}

	public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaSefazVirtual() {
		return unidadeFederativaSefazVirtual;
	}

	public void setUnidadeFederativaSefazVirtual(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaSefazVirtual) {
		this.unidadeFederativaSefazVirtual = unidadeFederativaSefazVirtual;
	}

	public Long getNrPrazoCancCte() {
		return nrPrazoCancCte;
	}

	public void setNrPrazoCancCte(Long nrPrazoCancCte) {
		this.nrPrazoCancCte = nrPrazoCancCte;
	}
	
}