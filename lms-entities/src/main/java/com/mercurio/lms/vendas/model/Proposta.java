package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

/** @author LMS Custom Hibernate CodeGenerator */
public class Proposta implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idProposta;

	/** persistent field */
	private DomainValue tpIndicadorMinFretePeso;

	/** persistent field */
	private BigDecimal vlMinFretePeso;

	/** persistent field */
	private DomainValue tpIndicadorFreteMinimo;

	/** persistent field */
	private BigDecimal vlFreteMinimo;

	/** persistent field */
	private DomainValue tpIndicadorFretePeso;

	/** persistent field */
	private BigDecimal vlFretePeso;

	/** persistent field */
	private Boolean blPagaPesoExcedente;

	/** persistent field */
	private Boolean blPagaCubagem;

	/** persistent field */
	private BigDecimal pcPagaCubagem;

	/** persistent field */
	private BigDecimal pcDiferencaFretePeso;

	/** persistent field */
	private DomainValue tpIndicadorAdvalorem;

	/** persistent field */
	private BigDecimal vlAdvalorem;

	/** persistent field */
	private BigDecimal pcDiferencaAdvalorem;

	/** persistent field */
	private UnidadeFederativa unidadeFederativaByIdUfOrigem;

	/** persistent field */
	private TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;

	/** persistent field */
	private Simulacao simulacao;

	/** persistent field */
	private Boolean blFreteExpedido;

	/** persistent field */
	private Boolean blFreteRecebido;

	/** persistent field */
	private ParametroCliente parametroCliente;
	
	private Aeroporto aeroportoReferencia;

	public Long getIdProposta() {
		return this.idProposta;
	}

	public void setIdProposta(Long idProposta) {
		this.idProposta = idProposta;
	}

	public DomainValue getTpIndicadorMinFretePeso() {
		return tpIndicadorMinFretePeso;
	}

	public void setTpIndicadorMinFretePeso(DomainValue tpIndicadorMinFretePeso) {
		this.tpIndicadorMinFretePeso = tpIndicadorMinFretePeso;
	}

	public BigDecimal getVlMinFretePeso() {
		return vlMinFretePeso;
	}

	public void setVlMinFretePeso(BigDecimal vlMinFretePeso) {
		this.vlMinFretePeso = vlMinFretePeso;
	}

	public DomainValue getTpIndicadorFreteMinimo() {
		return tpIndicadorFreteMinimo;
	}

	public void setTpIndicadorFreteMinimo(DomainValue tpIndicadorFreteMinimo) {
		this.tpIndicadorFreteMinimo = tpIndicadorFreteMinimo;
	}

	public BigDecimal getVlFreteMinimo() {
		return vlFreteMinimo;
	}

	public void setVlFreteMinimo(BigDecimal vlFreteMinimo) {
		this.vlFreteMinimo = vlFreteMinimo;
	}

	public DomainValue getTpIndicadorFretePeso() {
		return tpIndicadorFretePeso;
	}

	public void setTpIndicadorFretePeso(DomainValue tpIndicadorFretePeso) {
		this.tpIndicadorFretePeso = tpIndicadorFretePeso;
	}

	public BigDecimal getVlFretePeso() {
		return vlFretePeso;
	}

	public void setVlFretePeso(BigDecimal vlFretePeso) {
		this.vlFretePeso = vlFretePeso;
	}

	public Boolean getBlPagaPesoExcedente() {
		return blPagaPesoExcedente;
	}

	public void setBlPagaPesoExcedente(Boolean blPagaPesoExcedente) {
		this.blPagaPesoExcedente = blPagaPesoExcedente;
	}

	public Boolean getBlPagaCubagem() {
		return blPagaCubagem;
	}

	public void setBlPagaCubagem(Boolean blPagaCubagem) {
		this.blPagaCubagem = blPagaCubagem;
	}

	public BigDecimal getPcPagaCubagem() {
		return pcPagaCubagem;
	}

	public void setPcPagaCubagem(BigDecimal pcPagaCubagem) {
		this.pcPagaCubagem = pcPagaCubagem;
	}

	public BigDecimal getPcDiferencaFretePeso() {
		return pcDiferencaFretePeso;
	}

	public void setPcDiferencaFretePeso(BigDecimal pcDiferencaFretePeso) {
		this.pcDiferencaFretePeso = pcDiferencaFretePeso;
	}

	public DomainValue getTpIndicadorAdvalorem() {
		return tpIndicadorAdvalorem;
	}

	public void setTpIndicadorAdvalorem(DomainValue tpIndicadorAdvalorem) {
		this.tpIndicadorAdvalorem = tpIndicadorAdvalorem;
	}

	public BigDecimal getVlAdvalorem() {
		return vlAdvalorem;
	}

	public void setVlAdvalorem(BigDecimal vlAdvalorem) {
		this.vlAdvalorem = vlAdvalorem;
	}

	public BigDecimal getPcDiferencaAdvalorem() {
		return pcDiferencaAdvalorem;
	}

	public void setPcDiferencaAdvalorem(BigDecimal pcDiferencaAdvalorem) {
		this.pcDiferencaAdvalorem = pcDiferencaAdvalorem;
	}

	public UnidadeFederativa getUnidadeFederativaByIdUfOrigem() {
		return unidadeFederativaByIdUfOrigem;
	}

	public void setUnidadeFederativaByIdUfOrigem(
			UnidadeFederativa unidadeFederativaByIdUfOrigem) {
		this.unidadeFederativaByIdUfOrigem = unidadeFederativaByIdUfOrigem;
	}

	public TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() {
		return tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;
	}

	public void setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem) {
		this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem = tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;
	}

	public Simulacao getSimulacao() {
		return simulacao;
	}

	public void setSimulacao(Simulacao simulacao) {
		this.simulacao = simulacao;
	}

	public Boolean getBlFreteExpedido() {
		return blFreteExpedido;
	}

	public void setBlFreteExpedido(Boolean blFreteExpedido) {
		this.blFreteExpedido = blFreteExpedido;
	}

	public Boolean getBlFreteRecebido() {
		return blFreteRecebido;
	}

	public void setBlFreteRecebido(Boolean blFreteRecebido) {
		this.blFreteRecebido = blFreteRecebido;
	}

	public ParametroCliente getParametroCliente() {
		return parametroCliente;
	}

	public void setParametroCliente(ParametroCliente parametroCliente) {
		this.parametroCliente = parametroCliente;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idProposta", getIdProposta())
			.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Proposta))
			return false;
		Proposta castOther = (Proposta) other;
		return new EqualsBuilder().append(this.getIdProposta(),
				castOther.getIdProposta()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdProposta()).toHashCode();
	}

    public Aeroporto getAeroportoReferencia() {
        return aeroportoReferencia;
    }

    public void setAeroportoReferencia(Aeroporto aeroportoReferencia) {
        this.aeroportoReferencia = aeroportoReferencia;
    }

}
