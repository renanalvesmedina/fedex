package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;

/** @author LMS Custom Hibernate CodeGenerator */
public class DestinoProposta implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idDestinoProposta;

	/** persistent field */
	private DomainValue tpIndicadorFreteMinimo;

	/** persistent field */
	private BigDecimal vlFreteMinimo;

	/** persistent field */
	private DomainValue tpIndicadorFretePeso;

	/** persistent field */
	private BigDecimal vlFretePeso;

	/** persistent field */
	private DomainValue tpIndicadorAdvalorem;

	/** persistent field */
	private DomainValue tpDiferencaAdvalorem;	

	/** persistent field */
	private BigDecimal vlAdvalorem;

	/** persistent field */
	private BigDecimal pcDiferencaFretePeso;

	/** persistent field */
	private BigDecimal pcDiferencaAdvalorem;

	/** persistent field */
	private BigDecimal pcFretePercentual;
	
	/** persistent field */
	private BigDecimal vlMinimoFretePercentual;
	
	/** persistent field */
	private BigDecimal vlToneladaFretePercentual;
	
	/** persistent field */
	private BigDecimal psFretePercentual;
		
	/** persistent field */
	private UnidadeFederativa unidadeFederativa;

	/** persistent field */
	private TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio;

	/** persistent field */
	private Proposta proposta;

	/** persistent field */
	private GrupoRegiao grupoRegiao;

	private Boolean marcadoParaExclusao;
	private List<ValorFaixaProgressivaProposta> listaValorFaixaProgressivaProposta;
	
	private DomainValue tpIndicadorProdutoEspecifico;
	private BigDecimal vlProdutoEspecifico;
	private BigDecimal pcDiferencaProdutoEspecifico;
	
	private DomainValue tpIndicadorTaxaColetaUrbanaEmergencial;
	private BigDecimal vlTaxaColetaUrbanaEmergencial;
	private BigDecimal psMinimoTaxaColetaUrbanaEmergencial;
	private BigDecimal vlExcedenteTaxaColetaUrbanaEmergencial;
	
	private DomainValue tpIndicadorTaxaEntregaUrbanaConvencional;
	private BigDecimal vlTaxaEntregaUrbanaConvencional;
	private BigDecimal psMinimoTaxaEntregaUrbanaConvencional;
	private BigDecimal vlExcedenteTaxaEntregaUrbanaConvencional;
	
	private DomainValue tpIndicadorTaxaEntregaUrbanaEmergencial;
	private BigDecimal vlTaxaEntregaUrbanaEmergencial;
	private BigDecimal psMinimoTaxaEntregaUrbanaEmergencial;
	private BigDecimal vlExcedenteTaxaEntregaUrbanaEmergencial;
	
	private DomainValue tpIndicadorTaxaColetaInteriorConvencial;
	private BigDecimal vlTaxaColetaInteriorConvencional;
	private BigDecimal psMinimoTaxaColetaInteriorConvencional;
	private BigDecimal vlExcedenteTaxaColetaInteriorConvencional;
	
	private DomainValue tpIndicadorTaxaColetaInteriorEmergencial;
	private BigDecimal vlTaxaColetaInteriorEmergencial;
	private BigDecimal psMinimoTaxaColetaInteriorEmergencial;
	private BigDecimal vlExcedenteTaxaColetaInteriorEmergencial;
	
	private DomainValue tpIndicadorTaxaEntregaInteriorConvencional;
	private BigDecimal vlTaxaEntregaInteriorConvencional;	
	private BigDecimal psMinimoTaxaEntregaInteriorConvencional;	
	private BigDecimal vlExcedenteTaxaEntregaInteriorConvencional;	
	
	private DomainValue tpIndicadorTaxaEntregaInteriorEmergencial;
	private BigDecimal vlTaxaEntregaInteriorEmergencial;	
	private BigDecimal psMinimoTaxaEntregaInteriorEmergencial;	
	private BigDecimal vlExcedenteTaxaEntregaInteriorEmergencial;	
	
	private DomainValue tpIndicadorTaxaColetaUrbanaConvencional;
	private BigDecimal vlTaxaColetaUrbanaConvencional;	
	private BigDecimal psMinimoTaxaColetaUrbanaConvencional;	
	private BigDecimal vlExcedenteTaxaColetaUrbanaConvencional;	
	
	private Aeroporto aeroportoByIdAeroporto;
	
	private DomainValue tpIndicadorAdvalorem2;
	private BigDecimal vlAdvalorem2;
	private BigDecimal psMinimoFretePeso;
	
	private RotaPreco rotaPreco;
	
	public Long getIdDestinoProposta() {
		return this.idDestinoProposta;
	}

	public void setIdDestinoProposta(Long idDestinoProposta) {
		this.idDestinoProposta = idDestinoProposta;
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

	public BigDecimal getPcDiferencaFretePeso() {
		return pcDiferencaFretePeso;
	}

	public void setPcDiferencaFretePeso(BigDecimal pcDiferencaFretePeso) {
		this.pcDiferencaFretePeso = pcDiferencaFretePeso;
	}

	public BigDecimal getPcDiferencaAdvalorem() {
		return pcDiferencaAdvalorem;
	}

	public void setPcDiferencaAdvalorem(BigDecimal pcDiferencaAdvalorem) {
		this.pcDiferencaAdvalorem = pcDiferencaAdvalorem;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipio() {
		return tipoLocalizacaoMunicipio;
	}

	public void setTipoLocalizacaoMunicipio(
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio) {
		this.tipoLocalizacaoMunicipio = tipoLocalizacaoMunicipio;
	}

	public Proposta getProposta() {
		return proposta;
	}

	public void setProposta(Proposta proposta) {
		this.proposta = proposta;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idDestinoProposta",
				getIdDestinoProposta()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DestinoProposta))
			return false;
		DestinoProposta castOther = (DestinoProposta) other;
		return new EqualsBuilder().append(this.getIdDestinoProposta(),
				castOther.getIdDestinoProposta()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdDestinoProposta())
			.toHashCode();
	}

	public DomainValue getTpDiferencaAdvalorem() {
		return tpDiferencaAdvalorem;
	}

	public void setTpDiferencaAdvalorem(DomainValue tpDiferencaAdvalorem) {
		this.tpDiferencaAdvalorem = tpDiferencaAdvalorem;
	}

	public BigDecimal getPcFretePercentual() {
		return pcFretePercentual;
	}

	public void setPcFretePercentual(BigDecimal pcFretePercentual) {
		this.pcFretePercentual = pcFretePercentual;
	}

	public BigDecimal getVlMinimoFretePercentual() {
		return vlMinimoFretePercentual;
	}

	public void setVlMinimoFretePercentual(BigDecimal vlMinimoFretePercentual) {
		this.vlMinimoFretePercentual = vlMinimoFretePercentual;
	}

	public BigDecimal getVlToneladaFretePercentual() {
		return vlToneladaFretePercentual;
	}

	public void setVlToneladaFretePercentual(
			BigDecimal vlToneladaFretePercentual) {
		this.vlToneladaFretePercentual = vlToneladaFretePercentual;
	}

	public BigDecimal getPsFretePercentual() {
		return psFretePercentual;
	}

	public void setPsFretePercentual(BigDecimal psFretePercentual) {
		this.psFretePercentual = psFretePercentual;
	}

	public GrupoRegiao getGrupoRegiao() {
		return grupoRegiao;
	}

	public void setGrupoRegiao(GrupoRegiao grupoRegiao) {
		this.grupoRegiao = grupoRegiao;
	}

	public DomainValue getTpIndicadorProdutoEspecifico() {
		return tpIndicadorProdutoEspecifico;
	}

	public void setTpIndicadorProdutoEspecifico(DomainValue tpIndicadorProdutoEspecifico) {
		this.tpIndicadorProdutoEspecifico = tpIndicadorProdutoEspecifico;
	}

	public BigDecimal getVlProdutoEspecifico() {
		return vlProdutoEspecifico;
	}

	public void setVlProdutoEspecifico(BigDecimal vlProdutoEspecifico) {
		this.vlProdutoEspecifico = vlProdutoEspecifico;
	}

	public BigDecimal getPcDiferencaProdutoEspecifico() {
		return pcDiferencaProdutoEspecifico;
	}

	public void setPcDiferencaProdutoEspecifico(BigDecimal pcDiferencaProdutoEspecifico) {
		this.pcDiferencaProdutoEspecifico = pcDiferencaProdutoEspecifico;
	}

	public DomainValue getTpIndicadorTaxaColetaUrbanaEmergencial() {
		return tpIndicadorTaxaColetaUrbanaEmergencial;
	}

	public void setTpIndicadorTaxaColetaUrbanaEmergencial(DomainValue tpIndicadorTaxaColetaUrbanaEmergencial) {
		this.tpIndicadorTaxaColetaUrbanaEmergencial = tpIndicadorTaxaColetaUrbanaEmergencial;
	}

	public BigDecimal getVlTaxaColetaUrbanaEmergencial() {
		return vlTaxaColetaUrbanaEmergencial;
	}

	public void setVlTaxaColetaUrbanaEmergencial(BigDecimal vlTaxaColetaUrbanaEmergencial) {
		this.vlTaxaColetaUrbanaEmergencial = vlTaxaColetaUrbanaEmergencial;
	}

	public DomainValue getTpIndicadorTaxaEntregaUrbanaConvencional() {
		return tpIndicadorTaxaEntregaUrbanaConvencional;
	}

	public void setTpIndicadorTaxaEntregaUrbanaConvencional(DomainValue tpIndicadorTaxaEntregaUrbanaConvencional) {
		this.tpIndicadorTaxaEntregaUrbanaConvencional = tpIndicadorTaxaEntregaUrbanaConvencional;
	}

	public BigDecimal getVlTaxaEntregaUrbanaConvencional() {
		return vlTaxaEntregaUrbanaConvencional;
	}

	public void setVlTaxaEntregaUrbanaConvencional(BigDecimal vlTaxaEntregaUrbanaConvencional) {
		this.vlTaxaEntregaUrbanaConvencional = vlTaxaEntregaUrbanaConvencional;
	}

	public DomainValue getTpIndicadorTaxaEntregaUrbanaEmergencial() {
		return tpIndicadorTaxaEntregaUrbanaEmergencial;
	}

	public void setTpIndicadorTaxaEntregaUrbanaEmergencial(DomainValue tpIndicadorTaxaEntregaUrbanaEmergencial) {
		this.tpIndicadorTaxaEntregaUrbanaEmergencial = tpIndicadorTaxaEntregaUrbanaEmergencial;
	}

	public BigDecimal getVlTaxaEntregaUrbanaEmergencial() {
		return vlTaxaEntregaUrbanaEmergencial;
	}

	public void setVlTaxaEntregaUrbanaEmergencial(BigDecimal vlTaxaEntregaUrbanaEmergencial) {
		this.vlTaxaEntregaUrbanaEmergencial = vlTaxaEntregaUrbanaEmergencial;
	}

	public DomainValue getTpIndicadorTaxaColetaInteriorConvencial() {
		return tpIndicadorTaxaColetaInteriorConvencial;
	}

	public void setTpIndicadorTaxaColetaInteriorConvencial(DomainValue tpIndicadorTaxaColetaInteriorConvencial) {
		this.tpIndicadorTaxaColetaInteriorConvencial = tpIndicadorTaxaColetaInteriorConvencial;
	}

	public BigDecimal getVlTaxaColetaInteriorConvencional() {
		return vlTaxaColetaInteriorConvencional;
	}

	public void setVlTaxaColetaInteriorConvencional(BigDecimal vlTaxaColetaInteriorConvencional) {
		this.vlTaxaColetaInteriorConvencional = vlTaxaColetaInteriorConvencional;
	}

	public DomainValue getTpIndicadorTaxaColetaInteriorEmergencial() {
		return tpIndicadorTaxaColetaInteriorEmergencial;
	}

	public void setTpIndicadorTaxaColetaInteriorEmergencial(DomainValue tpIndicadorTaxaColetaInteriorEmergencial) {
		this.tpIndicadorTaxaColetaInteriorEmergencial = tpIndicadorTaxaColetaInteriorEmergencial;
	}

	public BigDecimal getVlTaxaColetaInteriorEmergencial() {
		return vlTaxaColetaInteriorEmergencial;
	}

	public void setVlTaxaColetaInteriorEmergencial(BigDecimal vlTaxaColetaInteriorEmergencial) {
		this.vlTaxaColetaInteriorEmergencial = vlTaxaColetaInteriorEmergencial;
	}

	public DomainValue getTpIndicadorTaxaEntregaInteriorConvencional() {
		return tpIndicadorTaxaEntregaInteriorConvencional;
	}

	public void setTpIndicadorTaxaEntregaInteriorConvencional(DomainValue tpIndicadorTaxaEntregaInteriorConvencional) {
		this.tpIndicadorTaxaEntregaInteriorConvencional = tpIndicadorTaxaEntregaInteriorConvencional;
	}

	public BigDecimal getVlTaxaEntregaInteriorConvencional() {
		return vlTaxaEntregaInteriorConvencional;
	}

	public void setVlTaxaEntregaInteriorConvencional(BigDecimal vlTaxaEntregaInteriorConvencional) {
		this.vlTaxaEntregaInteriorConvencional = vlTaxaEntregaInteriorConvencional;
	}

	public DomainValue getTpIndicadorTaxaEntregaInteriorEmergencial() {
		return tpIndicadorTaxaEntregaInteriorEmergencial;
	}

	public void setTpIndicadorTaxaEntregaInteriorEmergencial(DomainValue tpIndicadorTaxaEntregaInteriorEmergencial) {
		this.tpIndicadorTaxaEntregaInteriorEmergencial = tpIndicadorTaxaEntregaInteriorEmergencial;
	}

	public BigDecimal getVlTaxaEntregaInteriorEmergencial() {
		return vlTaxaEntregaInteriorEmergencial;
	}

	public void setVlTaxaEntregaInteriorEmergencial(BigDecimal vlTaxaEntregaInteriorEmergencial) {
		this.vlTaxaEntregaInteriorEmergencial = vlTaxaEntregaInteriorEmergencial;
	}

	public DomainValue getTpIndicadorTaxaColetaUrbanaConvencional() {
		return tpIndicadorTaxaColetaUrbanaConvencional;
	}

	public void setTpIndicadorTaxaColetaUrbanaConvencional(DomainValue tpIndicadorTaxaColetaUrbanaConvencional) {
		this.tpIndicadorTaxaColetaUrbanaConvencional = tpIndicadorTaxaColetaUrbanaConvencional;
	}

	public BigDecimal getVlTaxaColetaUrbanaConvencional() {
		return vlTaxaColetaUrbanaConvencional;
	}

	public void setVlTaxaColetaUrbanaConvencional(BigDecimal vlTaxaColetaUrbanaConvencional) {
		this.vlTaxaColetaUrbanaConvencional = vlTaxaColetaUrbanaConvencional;
	}

	public Aeroporto getAeroportoByIdAeroporto() {
		return aeroportoByIdAeroporto;
	}

	public void setAeroportoByIdAeroporto(Aeroporto aeroportoByIdAeroporto) {
		this.aeroportoByIdAeroporto = aeroportoByIdAeroporto;
	}

    public DomainValue getTpIndicadorAdvalorem2() {
        return tpIndicadorAdvalorem2;
    }

    public void setTpIndicadorAdvalorem2(DomainValue tpIndicadorAdvalorem2) {
        this.tpIndicadorAdvalorem2 = tpIndicadorAdvalorem2;
    }

    public BigDecimal getVlAdvalorem2() {
        return vlAdvalorem2;
    }

    public void setVlAdvalorem2(BigDecimal vlAdvalorem2) {
        this.vlAdvalorem2 = vlAdvalorem2;
    }

    public BigDecimal getPsMinimoFretePeso() {
        return psMinimoFretePeso;
    }

    public void setPsMinimoFretePeso(BigDecimal psMinimoFretePeso) {
        this.psMinimoFretePeso = psMinimoFretePeso;
    }

	public BigDecimal getPsMinimoTaxaColetaUrbanaEmergencial() {
		return psMinimoTaxaColetaUrbanaEmergencial;
	}

	public void setPsMinimoTaxaColetaUrbanaEmergencial(BigDecimal psMinimoTaxaColetaUrbanaEmergencial) {
		this.psMinimoTaxaColetaUrbanaEmergencial = psMinimoTaxaColetaUrbanaEmergencial;
	}

	public BigDecimal getVlExcedenteTaxaColetaUrbanaEmergencial() {
		return vlExcedenteTaxaColetaUrbanaEmergencial;
	}

	public void setVlExcedenteTaxaColetaUrbanaEmergencial(BigDecimal vlExcedenteTaxaColetaUrbanaEmergencial) {
		this.vlExcedenteTaxaColetaUrbanaEmergencial = vlExcedenteTaxaColetaUrbanaEmergencial;
	}

	public BigDecimal getPsMinimoTaxaEntregaUrbanaConvencional() {
		return psMinimoTaxaEntregaUrbanaConvencional;
	}

	public void setPsMinimoTaxaEntregaUrbanaConvencional(BigDecimal psMinimoTaxaEntregaUrbanaConvencional) {
		this.psMinimoTaxaEntregaUrbanaConvencional = psMinimoTaxaEntregaUrbanaConvencional;
	}

	public BigDecimal getVlExcedenteTaxaEntregaUrbanaConvencional() {
		return vlExcedenteTaxaEntregaUrbanaConvencional;
	}

	public void setVlExcedenteTaxaEntregaUrbanaConvencional(BigDecimal vlExcedenteTaxaEntregaUrbanaConvencional) {
		this.vlExcedenteTaxaEntregaUrbanaConvencional = vlExcedenteTaxaEntregaUrbanaConvencional;
	}

	public BigDecimal getPsMinimoTaxaEntregaUrbanaEmergencial() {
		return psMinimoTaxaEntregaUrbanaEmergencial;
	}

	public void setPsMinimoTaxaEntregaUrbanaEmergencial(BigDecimal psMinimoTaxaEntregaUrbanaEmergencial) {
		this.psMinimoTaxaEntregaUrbanaEmergencial = psMinimoTaxaEntregaUrbanaEmergencial;
	}

	public BigDecimal getVlExcedenteTaxaEntregaUrbanaEmergencial() {
		return vlExcedenteTaxaEntregaUrbanaEmergencial;
	}

	public void setVlExcedenteTaxaEntregaUrbanaEmergencial(BigDecimal vlExcedenteTaxaEntregaUrbanaEmergencial) {
		this.vlExcedenteTaxaEntregaUrbanaEmergencial = vlExcedenteTaxaEntregaUrbanaEmergencial;
	}

	public BigDecimal getPsMinimoTaxaColetaInteriorConvencional() {
		return psMinimoTaxaColetaInteriorConvencional;
	}

	public void setPsMinimoTaxaColetaInteriorConvencional(BigDecimal psMinimoTaxaColetaInteriorConvencional) {
		this.psMinimoTaxaColetaInteriorConvencional = psMinimoTaxaColetaInteriorConvencional;
	}

	public BigDecimal getVlExcedenteTaxaColetaInteriorConvencional() {
		return vlExcedenteTaxaColetaInteriorConvencional;
	}

	public void setVlExcedenteTaxaColetaInteriorConvencional(BigDecimal vlExcedenteTaxaColetaInteriorConvencional) {
		this.vlExcedenteTaxaColetaInteriorConvencional = vlExcedenteTaxaColetaInteriorConvencional;
	}

	public BigDecimal getPsMinimoTaxaColetaInteriorEmergencial() {
		return psMinimoTaxaColetaInteriorEmergencial;
	}

	public void setPsMinimoTaxaColetaInteriorEmergencial(BigDecimal psMinimoTaxaColetaInteriorEmergencial) {
		this.psMinimoTaxaColetaInteriorEmergencial = psMinimoTaxaColetaInteriorEmergencial;
	}

	public BigDecimal getVlExcedenteTaxaColetaInteriorEmergencial() {
		return vlExcedenteTaxaColetaInteriorEmergencial;
	}

	public void setVlExcedenteTaxaColetaInteriorEmergencial(BigDecimal vlExcedenteTaxaColetaInteriorEmergencial) {
		this.vlExcedenteTaxaColetaInteriorEmergencial = vlExcedenteTaxaColetaInteriorEmergencial;
	}

	public BigDecimal getPsMinimoTaxaEntregaInteriorConvencional() {
		return psMinimoTaxaEntregaInteriorConvencional;
	}

	public void setPsMinimoTaxaEntregaInteriorConvencional(BigDecimal psMinimoTaxaEntregaInteriorConvencional) {
		this.psMinimoTaxaEntregaInteriorConvencional = psMinimoTaxaEntregaInteriorConvencional;
	}

	public BigDecimal getVlExcedenteTaxaEntregaInteriorConvencional() {
		return vlExcedenteTaxaEntregaInteriorConvencional;
	}

	public void setVlExcedenteTaxaEntregaInteriorConvencional(BigDecimal vlExcedenteTaxaEntregaInteriorConvencional) {
		this.vlExcedenteTaxaEntregaInteriorConvencional = vlExcedenteTaxaEntregaInteriorConvencional;
	}

	public BigDecimal getPsMinimoTaxaEntregaInteriorEmergencial() {
		return psMinimoTaxaEntregaInteriorEmergencial;
	}

	public void setPsMinimoTaxaEntregaInteriorEmergencial(BigDecimal psMinimoTaxaEntregaInteriorEmergencial) {
		this.psMinimoTaxaEntregaInteriorEmergencial = psMinimoTaxaEntregaInteriorEmergencial;
	}

	public BigDecimal getVlExcedenteTaxaEntregaInteriorEmergencial() {
		return vlExcedenteTaxaEntregaInteriorEmergencial;
	}

	public void setVlExcedenteTaxaEntregaInteriorEmergencial(BigDecimal vlExcedenteTaxaEntregaInteriorEmergencial) {
		this.vlExcedenteTaxaEntregaInteriorEmergencial = vlExcedenteTaxaEntregaInteriorEmergencial;
	}

	public BigDecimal getPsMinimoTaxaColetaUrbanaConvencional() {
		return psMinimoTaxaColetaUrbanaConvencional;
	}

	public void setPsMinimoTaxaColetaUrbanaConvencional(BigDecimal psMinimoTaxaColetaUrbanaConvencional) {
		this.psMinimoTaxaColetaUrbanaConvencional = psMinimoTaxaColetaUrbanaConvencional;
	}

	public BigDecimal getVlExcedenteTaxaColetaUrbanaConvencional() {
		return vlExcedenteTaxaColetaUrbanaConvencional;
	}

	public void setVlExcedenteTaxaColetaUrbanaConvencional(BigDecimal vlExcedenteTaxaColetaUrbanaConvencional) {
		this.vlExcedenteTaxaColetaUrbanaConvencional = vlExcedenteTaxaColetaUrbanaConvencional;
	}

	public Boolean getMarcadoParaExclusao() {
		return marcadoParaExclusao;
	}

	public void setMarcadoParaExclusao(Boolean marcadoParaExclusao) {
		this.marcadoParaExclusao = marcadoParaExclusao;
	}

	public List<ValorFaixaProgressivaProposta> getListaValorFaixaProgressivaProposta() {
		return listaValorFaixaProgressivaProposta;
	}

	public void setListaValorFaixaProgressivaProposta(List<ValorFaixaProgressivaProposta> listaValorFaixaProgressivaProposta) {
		this.listaValorFaixaProgressivaProposta = listaValorFaixaProgressivaProposta;
	}

    public RotaPreco getRotaPreco() {
        return rotaPreco;
    }

    public void setRotaPreco(RotaPreco rotaPreco) {
        this.rotaPreco = rotaPreco;
    }

}
