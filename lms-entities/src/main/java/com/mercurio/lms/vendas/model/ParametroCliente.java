package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.Zona;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParametroCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idParametroCliente;

	/** persistent field */
	private YearMonthDay dtVigenciaInicial;

	/** persistent field */
	private DomainValue tpIndicadorPercentualGris;

	/** persistent field */
	private BigDecimal vlPercentualGris;

	/** persistent field */
	private DomainValue tpIndicadorMinimoGris;

	/** persistent field */
	private BigDecimal vlMinimoGris;

	/** persistent field */
	private DomainValue tpIndicadorPercentualTrt;

	/** persistent field */
	private BigDecimal vlPercentualTrt;

	/** persistent field */
	private DomainValue tpIndicadorMinimoTrt;

	/** persistent field */
	private BigDecimal vlMinimoTrt;

	/** persistent field */
	private DomainValue tpIndicadorPedagio;

	/** persistent field */
	private BigDecimal vlPedagio;

	/** persistent field */
	private DomainValue tpIndicadorMinFretePeso;

	/** persistent field */
	private BigDecimal vlMinFretePeso;

	/** persistent field */
	private DomainValue tpIndicadorPercMinimoProgr;

	/** persistent field */
	private BigDecimal vlPercMinimoProgr;

	/** persistent field */
	private DomainValue tpIndicadorFretePeso;

	/** persistent field */
	private BigDecimal vlFretePeso;

	/** persistent field */
	private DomainValue tpIndicadorAdvalorem;

	/** persistent field */
	private BigDecimal vlAdvalorem;

	/** persistent field */
	private DomainValue tpIndicadorAdvalorem2;

	/** persistent field */
	private BigDecimal vlAdvalorem2;

	/** persistent field */
	private DomainValue tpIndicadorValorReferencia;

	/** persistent field */
	private BigDecimal vlValorReferencia;

	/** persistent field */
	private BigDecimal vlMinimoFreteQuilo;

	/** persistent field */
	private BigDecimal pcFretePercentual;

	/** persistent field */
	private BigDecimal vlMinimoFretePercentual;

	/** persistent field */
	private BigDecimal vlToneladaFretePercentual;

	/** persistent field */
	private BigDecimal psFretePercentual;

	/** persistent field */
	private BigDecimal pcDescontoFreteTotal;

	/** persistent field */
	private DomainValue tpIndicVlrTblEspecifica;

	/** persistent field */
	private BigDecimal vlTblEspecifica;

	/** persistent field */
	private BigDecimal vlFreteVolume;

	/** persistent field */
	private Boolean blPagaCubagem;

	/** persistent field */
	private BigDecimal pcPagaCubagem;

	/** persistent field */
	private Boolean blPagaPesoExcedente;

	/** persistent field */
	private DomainValue tpTarifaMinima;

	/** persistent field */
	private BigDecimal vlTarifaMinima;

	/** persistent field */
	private BigDecimal pcCobrancaReentrega;

	/** persistent field */
	private BigDecimal pcCobrancaDevolucoes;

	/** persistent field */
	private DomainValue tpSituacaoParametro;

	/** nullable persistent field */
	private BigDecimal pcReajFretePeso;

	/** nullable persistent field */
	private BigDecimal pcReajVlMinimoFreteQuilo;

	/** nullable persistent field */
	private BigDecimal pcReajVlFreteVolume;

	/** nullable persistent field */
	private BigDecimal pcReajTarifaMinima;

	/** nullable persistent field */
	private BigDecimal pcReajVlTarifaEspecifica;

	/** nullable persistent field */
	private BigDecimal pcReajAdvalorem;

	/** nullable persistent field */
	private BigDecimal pcReajAdvalorem2;

	/** nullable persistent field */
	private BigDecimal pcReajVlMinimoFretePercen;

	/** nullable persistent field */
	private BigDecimal pcReajVlToneladaFretePerc;

	/** nullable persistent field */
	private BigDecimal pcReajMinimoGris;

	/** nullable persistent field */
	private BigDecimal pcReajMinimoTrt;

	/** nullable persistent field */
	private BigDecimal pcReajPedagio;

	/** nullable persistent field */
	private YearMonthDay dtVigenciaFinal;

	/** nullable persistent field */
	private String dsEspecificacaoRota;

	/** persistent field */
	private DomainValue tpIndicadorPercentualTde;

	/** persistent field */
	private BigDecimal vlPercentualTde;

	/** persistent field */
	private DomainValue tpIndicadorMinimoTde;

	/** persistent field */
	private BigDecimal vlMinimoTde;

	/** nullable persistent field */
	private BigDecimal pcReajMinimoTde;

	/** nullable persistent field */
	private Boolean blEfetivaProposta;

	/** persistent field */
	private UnidadeFederativa unidadeFederativaByIdUfOrigem;

	/** persistent field */
	private UnidadeFederativa unidadeFederativaByIdUfDestino;

	/** persistent field */
	private Municipio municipioByIdMunicipioOrigem;

	/** persistent field */
	private Municipio municipioByIdMunicipioDestino;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoOrigem;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoDestino;

	/** persistent field */
	private Zona zonaByIdZonaDestino;

	/** persistent field */
	private Zona zonaByIdZonaOrigem;

	/** persistent field */
	private Simulacao simulacao;

	/** persistent field */
	private TabelaDivisaoCliente tabelaDivisaoCliente;

	/** persistent field */
	private TabelaPreco tabelaPreco;

	/** persistent field */
	private Cotacao cotacao;

	/** persistent field */
	private Proposta proposta;
	
	/** persistent field */
	private TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;

	/** persistent field */
	private TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;

	/** persistent field */
	private Cliente clienteByIdClienteRedespacho;

	/** persistent field */
	private Pais paisByIdPaisDestino;

	/** persistent field */
	private Pais paisByIdPaisOrigem;

	/** persistent field */
	private Filial filialByIdFilialOrigem;

	/** persistent field */
	private Filial filialByIdFilialMercurioRedespacho;

	/** persistent field */
	private GrupoRegiao grupoRegiaoOrigem;
	
	/** persistent field */
	private GrupoRegiao grupoRegiaoDestino;

	/** persistent field */
	private Filial filialByIdFilialDestino;

	/** persistent field */
	private List generalidadeClientes;

	/** persistent field */
	private List taxaClientes;
	
	private List valoresFaixaProgressivaProposta;

	private Integer indexRotaOrigem;
	
	private Integer indexRotaDestino;	
	
	private BigDecimal pcReajPercentualTRT;
	
	private BigDecimal pcReajPercentualTDE;
	
	private BigDecimal pcReajPercentualGris;
	
	private BigDecimal pcReajReferencia;
	
	private BigDecimal pcReajVlMinimoFretePeso;
	
	private BigDecimal pcReajVlMinimoProg;

	public ParametroCliente() {
	}

	public ParametroCliente(Long idParametroCliente, YearMonthDay dtVigenciaInicial, DomainValue tpIndicadorPercentualGris, BigDecimal vlPercentualGris, DomainValue tpIndicadorMinimoGris, BigDecimal vlMinimoGris, DomainValue tpIndicadorPercentualTrt, BigDecimal vlPercentualTrt, DomainValue tpIndicadorMinimoTrt, BigDecimal vlMinimoTrt, DomainValue tpIndicadorPedagio, BigDecimal vlPedagio, DomainValue tpIndicadorMinFretePeso, BigDecimal vlMinFretePeso, DomainValue tpIndicadorPercMinimoProgr, BigDecimal vlPercMinimoProgr, DomainValue tpIndicadorFretePeso, BigDecimal vlFretePeso, DomainValue tpIndicadorAdvalorem, BigDecimal vlAdvalorem, DomainValue tpIndicadorAdvalorem2, BigDecimal vlAdvalorem2, DomainValue tpIndicadorValorReferencia, BigDecimal vlValorReferencia, BigDecimal vlMinimoFreteQuilo, BigDecimal pcFretePercentual, BigDecimal vlMinimoFretePercentual, BigDecimal vlToneladaFretePercentual, BigDecimal psFretePercentual, BigDecimal pcDescontoFreteTotal, DomainValue tpIndicVlrTblEspecifica, BigDecimal vlTblEspecifica, BigDecimal vlFreteVolume, Boolean blPagaCubagem, BigDecimal pcPagaCubagem, Boolean blPagaPesoExcedente, DomainValue tpTarifaMinima, BigDecimal vlTarifaMinima, BigDecimal pcCobrancaReentrega, BigDecimal pcCobrancaDevolucoes, DomainValue tpSituacaoParametro, BigDecimal pcReajFretePeso, BigDecimal pcReajVlMinimoFreteQuilo, BigDecimal pcReajVlFreteVolume, BigDecimal pcReajTarifaMinima, BigDecimal pcReajVlTarifaEspecifica, BigDecimal pcReajAdvalorem, BigDecimal pcReajAdvalorem2, BigDecimal pcReajVlMinimoFretePercen, BigDecimal pcReajVlToneladaFretePerc, BigDecimal pcReajMinimoGris, BigDecimal pcReajMinimoTrt, BigDecimal pcReajPedagio, YearMonthDay dtVigenciaFinal, String dsEspecificacaoRota, DomainValue tpIndicadorPercentualTde, BigDecimal vlPercentualTde, DomainValue tpIndicadorMinimoTde, BigDecimal vlMinimoTde, BigDecimal pcReajMinimoTde, Boolean blEfetivaProposta, UnidadeFederativa unidadeFederativaByIdUfOrigem, UnidadeFederativa unidadeFederativaByIdUfDestino, Municipio municipioByIdMunicipioOrigem, Municipio municipioByIdMunicipioDestino, Aeroporto aeroportoByIdAeroportoOrigem, Aeroporto aeroportoByIdAeroportoDestino, Zona zonaByIdZonaDestino, Zona zonaByIdZonaOrigem, Simulacao simulacao, TabelaDivisaoCliente tabelaDivisaoCliente, TabelaPreco tabelaPreco, Cotacao cotacao, Proposta proposta, TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem, TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino, Cliente clienteByIdClienteRedespacho, Pais paisByIdPaisDestino, Pais paisByIdPaisOrigem, Filial filialByIdFilialOrigem, Filial filialByIdFilialMercurioRedespacho, GrupoRegiao grupoRegiaoOrigem, GrupoRegiao grupoRegiaoDestino, Filial filialByIdFilialDestino, List generalidadeClientes, List taxaClientes, List valoresFaixaProgressivaProposta, Integer indexRotaOrigem, Integer indexRotaDestino, BigDecimal pcReajPercentualTRT, BigDecimal pcReajPercentualTDE, BigDecimal pcReajPercentualGris, BigDecimal pcReajReferencia, BigDecimal pcReajVlMinimoFretePeso, BigDecimal pcReajVlMinimoProg) {
		this.idParametroCliente = idParametroCliente;
		this.dtVigenciaInicial = dtVigenciaInicial;
		this.tpIndicadorPercentualGris = tpIndicadorPercentualGris;
		this.vlPercentualGris = vlPercentualGris;
		this.tpIndicadorMinimoGris = tpIndicadorMinimoGris;
		this.vlMinimoGris = vlMinimoGris;
		this.tpIndicadorPercentualTrt = tpIndicadorPercentualTrt;
		this.vlPercentualTrt = vlPercentualTrt;
		this.tpIndicadorMinimoTrt = tpIndicadorMinimoTrt;
		this.vlMinimoTrt = vlMinimoTrt;
		this.tpIndicadorPedagio = tpIndicadorPedagio;
		this.vlPedagio = vlPedagio;
		this.tpIndicadorMinFretePeso = tpIndicadorMinFretePeso;
		this.vlMinFretePeso = vlMinFretePeso;
		this.tpIndicadorPercMinimoProgr = tpIndicadorPercMinimoProgr;
		this.vlPercMinimoProgr = vlPercMinimoProgr;
		this.tpIndicadorFretePeso = tpIndicadorFretePeso;
		this.vlFretePeso = vlFretePeso;
		this.tpIndicadorAdvalorem = tpIndicadorAdvalorem;
		this.vlAdvalorem = vlAdvalorem;
		this.tpIndicadorAdvalorem2 = tpIndicadorAdvalorem2;
		this.vlAdvalorem2 = vlAdvalorem2;
		this.tpIndicadorValorReferencia = tpIndicadorValorReferencia;
		this.vlValorReferencia = vlValorReferencia;
		this.vlMinimoFreteQuilo = vlMinimoFreteQuilo;
		this.pcFretePercentual = pcFretePercentual;
		this.vlMinimoFretePercentual = vlMinimoFretePercentual;
		this.vlToneladaFretePercentual = vlToneladaFretePercentual;
		this.psFretePercentual = psFretePercentual;
		this.pcDescontoFreteTotal = pcDescontoFreteTotal;
		this.tpIndicVlrTblEspecifica = tpIndicVlrTblEspecifica;
		this.vlTblEspecifica = vlTblEspecifica;
		this.vlFreteVolume = vlFreteVolume;
		this.blPagaCubagem = blPagaCubagem;
		this.pcPagaCubagem = pcPagaCubagem;
		this.blPagaPesoExcedente = blPagaPesoExcedente;
		this.tpTarifaMinima = tpTarifaMinima;
		this.vlTarifaMinima = vlTarifaMinima;
		this.pcCobrancaReentrega = pcCobrancaReentrega;
		this.pcCobrancaDevolucoes = pcCobrancaDevolucoes;
		this.tpSituacaoParametro = tpSituacaoParametro;
		this.pcReajFretePeso = pcReajFretePeso;
		this.pcReajVlMinimoFreteQuilo = pcReajVlMinimoFreteQuilo;
		this.pcReajVlFreteVolume = pcReajVlFreteVolume;
		this.pcReajTarifaMinima = pcReajTarifaMinima;
		this.pcReajVlTarifaEspecifica = pcReajVlTarifaEspecifica;
		this.pcReajAdvalorem = pcReajAdvalorem;
		this.pcReajAdvalorem2 = pcReajAdvalorem2;
		this.pcReajVlMinimoFretePercen = pcReajVlMinimoFretePercen;
		this.pcReajVlToneladaFretePerc = pcReajVlToneladaFretePerc;
		this.pcReajMinimoGris = pcReajMinimoGris;
		this.pcReajMinimoTrt = pcReajMinimoTrt;
		this.pcReajPedagio = pcReajPedagio;
		this.dtVigenciaFinal = dtVigenciaFinal;
		this.dsEspecificacaoRota = dsEspecificacaoRota;
		this.tpIndicadorPercentualTde = tpIndicadorPercentualTde;
		this.vlPercentualTde = vlPercentualTde;
		this.tpIndicadorMinimoTde = tpIndicadorMinimoTde;
		this.vlMinimoTde = vlMinimoTde;
		this.pcReajMinimoTde = pcReajMinimoTde;
		this.blEfetivaProposta = blEfetivaProposta;
		this.unidadeFederativaByIdUfOrigem = unidadeFederativaByIdUfOrigem;
		this.unidadeFederativaByIdUfDestino = unidadeFederativaByIdUfDestino;
		this.municipioByIdMunicipioOrigem = municipioByIdMunicipioOrigem;
		this.municipioByIdMunicipioDestino = municipioByIdMunicipioDestino;
		this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
		this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
		this.zonaByIdZonaDestino = zonaByIdZonaDestino;
		this.zonaByIdZonaOrigem = zonaByIdZonaOrigem;
		this.simulacao = simulacao;
		this.tabelaDivisaoCliente = tabelaDivisaoCliente;
		this.tabelaPreco = tabelaPreco;
		this.cotacao = cotacao;
		this.proposta = proposta;
		this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem = tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;
		this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino = tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;
		this.clienteByIdClienteRedespacho = clienteByIdClienteRedespacho;
		this.paisByIdPaisDestino = paisByIdPaisDestino;
		this.paisByIdPaisOrigem = paisByIdPaisOrigem;
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
		this.filialByIdFilialMercurioRedespacho = filialByIdFilialMercurioRedespacho;
		this.grupoRegiaoOrigem = grupoRegiaoOrigem;
		this.grupoRegiaoDestino = grupoRegiaoDestino;
		this.filialByIdFilialDestino = filialByIdFilialDestino;
		this.generalidadeClientes = generalidadeClientes;
		this.taxaClientes = taxaClientes;
		this.valoresFaixaProgressivaProposta = valoresFaixaProgressivaProposta;
		this.indexRotaOrigem = indexRotaOrigem;
		this.indexRotaDestino = indexRotaDestino;
		this.pcReajPercentualTRT = pcReajPercentualTRT;
		this.pcReajPercentualTDE = pcReajPercentualTDE;
		this.pcReajPercentualGris = pcReajPercentualGris;
		this.pcReajReferencia = pcReajReferencia;
		this.pcReajVlMinimoFretePeso = pcReajVlMinimoFretePeso;
		this.pcReajVlMinimoProg = pcReajVlMinimoProg;
	}

	public Long getIdParametroCliente() {
		return this.idParametroCliente;
	}

	public void setIdParametroCliente(Long idParametroCliente) {
		this.idParametroCliente = idParametroCliente;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return this.dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public DomainValue getTpIndicadorPercentualGris() {
		return this.tpIndicadorPercentualGris;
	}

	public void setTpIndicadorPercentualGris(
			DomainValue tpIndicadorPercentualGris) {
		this.tpIndicadorPercentualGris = tpIndicadorPercentualGris;
	}

	public BigDecimal getVlPercentualGris() {
		return this.vlPercentualGris;
	}

	public void setVlPercentualGris(BigDecimal vlPercentualGris) {
		this.vlPercentualGris = vlPercentualGris;
	}

	public DomainValue getTpIndicadorMinimoGris() {
		return this.tpIndicadorMinimoGris;
	}

	public void setTpIndicadorMinimoGris(DomainValue tpIndicadorMinimoGris) {
		this.tpIndicadorMinimoGris = tpIndicadorMinimoGris;
	}

	public BigDecimal getVlMinimoGris() {
		return this.vlMinimoGris;
	}

	public void setVlMinimoGris(BigDecimal vlMinimoGris) {
		this.vlMinimoGris = vlMinimoGris;
	}

	public DomainValue getTpIndicadorPercentualTrt() {
		return tpIndicadorPercentualTrt;
	}

	public void setTpIndicadorPercentualTrt(DomainValue tpIndicadorPercentualTrt) {
		this.tpIndicadorPercentualTrt = tpIndicadorPercentualTrt;
	}
	
	public BigDecimal getVlPercentualTrt() {
		return vlPercentualTrt;
	}

	public void setVlPercentualTrt(BigDecimal vlPercentualTrt) {
		this.vlPercentualTrt = vlPercentualTrt;
	}
	
	public DomainValue getTpIndicadorMinimoTrt() {
		return tpIndicadorMinimoTrt;
	}

	public void setTpIndicadorMinimoTrt(DomainValue tpIndicadorMinimoTrt) {
		this.tpIndicadorMinimoTrt = tpIndicadorMinimoTrt;
	}
	
	public BigDecimal getVlMinimoTrt() {
		return vlMinimoTrt;
	}

	public void setVlMinimoTrt(BigDecimal vlMinimoTrt) {
		this.vlMinimoTrt = vlMinimoTrt;
	}

	public DomainValue getTpIndicadorPedagio() {
		return this.tpIndicadorPedagio;
	}

	public void setTpIndicadorPedagio(DomainValue tpIndicadorPedagio) {
		this.tpIndicadorPedagio = tpIndicadorPedagio;
	}

	public BigDecimal getVlPedagio() {
		return this.vlPedagio;
	}

	public void setVlPedagio(BigDecimal vlPedagio) {
		this.vlPedagio = vlPedagio;
	}

	public DomainValue getTpIndicadorMinFretePeso() {
		return this.tpIndicadorMinFretePeso;
	}

	public void setTpIndicadorMinFretePeso(DomainValue tpIndicadorMinFretePeso) {
		this.tpIndicadorMinFretePeso = tpIndicadorMinFretePeso;
	}

	public BigDecimal getVlMinFretePeso() {
		return this.vlMinFretePeso;
	}

	public void setVlMinFretePeso(BigDecimal vlMinFretePeso) {
		this.vlMinFretePeso = vlMinFretePeso;
	}

	public DomainValue getTpIndicadorPercMinimoProgr() {
		return this.tpIndicadorPercMinimoProgr;
	}

	public void setTpIndicadorPercMinimoProgr(
			DomainValue tpIndicadorPercMinimoProgr) {
		this.tpIndicadorPercMinimoProgr = tpIndicadorPercMinimoProgr;
	}

	public BigDecimal getVlPercMinimoProgr() {
		return this.vlPercMinimoProgr;
	}

	public void setVlPercMinimoProgr(BigDecimal vlPercMinimoProgr) {
		this.vlPercMinimoProgr = vlPercMinimoProgr;
	}

	public DomainValue getTpIndicadorFretePeso() {
		return this.tpIndicadorFretePeso;
	}

	public void setTpIndicadorFretePeso(DomainValue tpIndicadorFretePeso) {
		this.tpIndicadorFretePeso = tpIndicadorFretePeso;
	}

	public BigDecimal getVlFretePeso() {
		return this.vlFretePeso;
	}

	public void setVlFretePeso(BigDecimal vlFretePeso) {
		this.vlFretePeso = vlFretePeso;
	}

	public DomainValue getTpIndicadorAdvalorem() {
		return this.tpIndicadorAdvalorem;
	}

	public void setTpIndicadorAdvalorem(DomainValue tpIndicadorAdvalorem) {
		this.tpIndicadorAdvalorem = tpIndicadorAdvalorem;
	}

	public BigDecimal getVlAdvalorem() {
		return this.vlAdvalorem;
	}

	public void setVlAdvalorem(BigDecimal vlAdvalorem) {
		this.vlAdvalorem = vlAdvalorem;
	}

	public DomainValue getTpIndicadorAdvalorem2() {
		return this.tpIndicadorAdvalorem2;
	}

	public void setTpIndicadorAdvalorem2(DomainValue tpIndicadorAdvalorem2) {
		this.tpIndicadorAdvalorem2 = tpIndicadorAdvalorem2;
	}

	public BigDecimal getVlAdvalorem2() {
		return this.vlAdvalorem2;
	}

	public void setVlAdvalorem2(BigDecimal vlAdvalorem2) {
		this.vlAdvalorem2 = vlAdvalorem2;
	}

	public DomainValue getTpIndicadorValorReferencia() {
		return this.tpIndicadorValorReferencia;
	}

	public void setTpIndicadorValorReferencia(
			DomainValue tpIndicadorValorReferencia) {
		this.tpIndicadorValorReferencia = tpIndicadorValorReferencia;
	}

	public BigDecimal getVlValorReferencia() {
		return this.vlValorReferencia;
	}

	public void setVlValorReferencia(BigDecimal vlValorReferencia) {
		this.vlValorReferencia = vlValorReferencia;
	}

	public BigDecimal getVlMinimoFreteQuilo() {
		return this.vlMinimoFreteQuilo;
	}

	public void setVlMinimoFreteQuilo(BigDecimal vlMinimoFreteQuilo) {
		this.vlMinimoFreteQuilo = vlMinimoFreteQuilo;
	}

	public BigDecimal getPcFretePercentual() {
		return this.pcFretePercentual;
	}

	public void setPcFretePercentual(BigDecimal pcFretePercentual) {
		this.pcFretePercentual = pcFretePercentual;
	}

	public BigDecimal getVlMinimoFretePercentual() {
		return this.vlMinimoFretePercentual;
	}

	public void setVlMinimoFretePercentual(BigDecimal vlMinimoFretePercentual) {
		this.vlMinimoFretePercentual = vlMinimoFretePercentual;
	}

	public BigDecimal getVlToneladaFretePercentual() {
		return this.vlToneladaFretePercentual;
	}

	public void setVlToneladaFretePercentual(
			BigDecimal vlToneladaFretePercentual) {
		this.vlToneladaFretePercentual = vlToneladaFretePercentual;
	}

	public BigDecimal getPsFretePercentual() {
		return this.psFretePercentual;
	}

	public void setPsFretePercentual(BigDecimal psFretePercentual) {
		this.psFretePercentual = psFretePercentual;
	}

	public BigDecimal getPcDescontoFreteTotal() {
		return this.pcDescontoFreteTotal;
	}

	public void setPcDescontoFreteTotal(BigDecimal pcDescontoFreteTotal) {
		this.pcDescontoFreteTotal = pcDescontoFreteTotal;
	}

	public DomainValue getTpIndicVlrTblEspecifica() {
		return this.tpIndicVlrTblEspecifica;
	}

	public void setTpIndicVlrTblEspecifica(DomainValue tpIndicVlrTblEspecifica) {
		this.tpIndicVlrTblEspecifica = tpIndicVlrTblEspecifica;
	}

	public BigDecimal getVlTblEspecifica() {
		return this.vlTblEspecifica;
	}

	public void setVlTblEspecifica(BigDecimal vlTblEspecifica) {
		this.vlTblEspecifica = vlTblEspecifica;
	}

	public BigDecimal getVlFreteVolume() {
		return this.vlFreteVolume;
	}

	public void setVlFreteVolume(BigDecimal vlFreteVolume) {
		this.vlFreteVolume = vlFreteVolume;
	}

	public Boolean getBlPagaCubagem() {
		return this.blPagaCubagem;
	}

	public void setBlPagaCubagem(Boolean blPagaCubagem) {
		this.blPagaCubagem = blPagaCubagem;
	}

	public BigDecimal getPcPagaCubagem() {
		return this.pcPagaCubagem;
	}

	public void setPcPagaCubagem(BigDecimal pcPagaCubagem) {
		this.pcPagaCubagem = pcPagaCubagem;
	}

	public Boolean getBlPagaPesoExcedente() {
		return this.blPagaPesoExcedente;
	}

	public void setBlPagaPesoExcedente(Boolean blPagaPesoExcedente) {
		this.blPagaPesoExcedente = blPagaPesoExcedente;
	}

	public DomainValue getTpTarifaMinima() {
		return this.tpTarifaMinima;
	}

	public void setTpTarifaMinima(DomainValue tpTarifaMinima) {
		this.tpTarifaMinima = tpTarifaMinima;
	}

	public BigDecimal getVlTarifaMinima() {
		return this.vlTarifaMinima;
	}

	public void setVlTarifaMinima(BigDecimal vlTarifaMinima) {
		this.vlTarifaMinima = vlTarifaMinima;
	}

	public BigDecimal getPcCobrancaReentrega() {
		return this.pcCobrancaReentrega;
	}

	public void setPcCobrancaReentrega(BigDecimal pcCobrancaReentrega) {
		this.pcCobrancaReentrega = pcCobrancaReentrega;
	}

	public BigDecimal getPcCobrancaDevolucoes() {
		return this.pcCobrancaDevolucoes;
	}

	public void setPcCobrancaDevolucoes(BigDecimal pcCobrancaDevolucoes) {
		this.pcCobrancaDevolucoes = pcCobrancaDevolucoes;
	}

	public DomainValue getTpSituacaoParametro() {
		return this.tpSituacaoParametro;
	}

	public void setTpSituacaoParametro(DomainValue tpSituacaoParametro) {
		this.tpSituacaoParametro = tpSituacaoParametro;
	}

	public BigDecimal getPcReajFretePeso() {
		return this.pcReajFretePeso;
	}

	public void setPcReajFretePeso(BigDecimal pcReajFretePeso) {
		this.pcReajFretePeso = pcReajFretePeso;
	}

	public BigDecimal getPcReajVlMinimoFreteQuilo() {
		return this.pcReajVlMinimoFreteQuilo;
	}

	public void setPcReajVlMinimoFreteQuilo(BigDecimal pcReajVlMinimoFreteQuilo) {
		this.pcReajVlMinimoFreteQuilo = pcReajVlMinimoFreteQuilo;
	}

	public BigDecimal getPcReajVlFreteVolume() {
		return this.pcReajVlFreteVolume;
	}

	public void setPcReajVlFreteVolume(BigDecimal pcReajVlFreteVolume) {
		this.pcReajVlFreteVolume = pcReajVlFreteVolume;
	}

	public BigDecimal getPcReajTarifaMinima() {
		return this.pcReajTarifaMinima;
	}

	public void setPcReajTarifaMinima(BigDecimal pcReajTarifaMinima) {
		this.pcReajTarifaMinima = pcReajTarifaMinima;
	}

	public BigDecimal getPcReajVlTarifaEspecifica() {
		return this.pcReajVlTarifaEspecifica;
	}

	public void setPcReajVlTarifaEspecifica(BigDecimal pcReajVlTarifaEspecifica) {
		this.pcReajVlTarifaEspecifica = pcReajVlTarifaEspecifica;
	}

	public BigDecimal getPcReajAdvalorem() {
		return this.pcReajAdvalorem;
	}

	public void setPcReajAdvalorem(BigDecimal pcReajAdvalorem) {
		this.pcReajAdvalorem = pcReajAdvalorem;
	}

	public BigDecimal getPcReajAdvalorem2() {
		return this.pcReajAdvalorem2;
	}

	public void setPcReajAdvalorem2(BigDecimal pcReajAdvalorem2) {
		this.pcReajAdvalorem2 = pcReajAdvalorem2;
	}

	public BigDecimal getPcReajVlMinimoFretePercen() {
		return this.pcReajVlMinimoFretePercen;
	}

	public void setPcReajVlMinimoFretePercen(
			BigDecimal pcReajVlMinimoFretePercen) {
		this.pcReajVlMinimoFretePercen = pcReajVlMinimoFretePercen;
	}

	public BigDecimal getPcReajVlToneladaFretePerc() {
		return this.pcReajVlToneladaFretePerc;
	}

	public void setPcReajVlToneladaFretePerc(
			BigDecimal pcReajVlToneladaFretePerc) {
		this.pcReajVlToneladaFretePerc = pcReajVlToneladaFretePerc;
	}

	public BigDecimal getPcReajMinimoGris() {
		return this.pcReajMinimoGris;
	}

	public void setPcReajMinimoGris(BigDecimal pcReajMinimoGris) {
		this.pcReajMinimoGris = pcReajMinimoGris;
	}

	public BigDecimal getPcReajMinimoTrt() {
		return pcReajMinimoTrt;
	}

	public void setPcReajMinimoTrt(BigDecimal pcReajMinimoTrt) {
		this.pcReajMinimoTrt = pcReajMinimoTrt;
	}

	public BigDecimal getPcReajPedagio() {
		return this.pcReajPedagio;
	}

	public void setPcReajPedagio(BigDecimal pcReajPedagio) {
		this.pcReajPedagio = pcReajPedagio;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return this.dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public String getDsEspecificacaoRota() {
		return this.dsEspecificacaoRota;
	}

	public void setDsEspecificacaoRota(String dsEspecificacaoRota) {
		this.dsEspecificacaoRota = dsEspecificacaoRota;
	}

	public DomainValue getTpIndicadorPercentualTde() {
		return this.tpIndicadorPercentualTde;
	}

	public void setTpIndicadorPercentualTde(DomainValue tpIndicadorPercentualTde) {
		this.tpIndicadorPercentualTde = tpIndicadorPercentualTde;
	}

	public BigDecimal getVlPercentualTde() {
		return this.vlPercentualTde;
	}

	public void setVlPercentualTde(BigDecimal vlPercentualTde) {
		this.vlPercentualTde = vlPercentualTde;
	}

	public DomainValue getTpIndicadorMinimoTde() {
		return this.tpIndicadorMinimoTde;
	}

	public void setTpIndicadorMinimoTde(DomainValue tpIndicadorMinimoTde) {
		this.tpIndicadorMinimoTde = tpIndicadorMinimoTde;
	}

	public BigDecimal getVlMinimoTde() {
		return this.vlMinimoTde;
	}

	public void setVlMinimoTde(BigDecimal vlMinimoTde) {
		this.vlMinimoTde = vlMinimoTde;
	}

	public BigDecimal getPcReajMinimoTde() {
		return this.pcReajMinimoTde;
	}

	public void setPcReajMinimoTde(BigDecimal pcReajMinimoTde) {
		this.pcReajMinimoTde = pcReajMinimoTde;
	}

	public UnidadeFederativa getUnidadeFederativaByIdUfOrigem() {
		return this.unidadeFederativaByIdUfOrigem;
	}

	public void setUnidadeFederativaByIdUfOrigem(
			UnidadeFederativa unidadeFederativaByIdUfOrigem) {
		this.unidadeFederativaByIdUfOrigem = unidadeFederativaByIdUfOrigem;
	}

	public UnidadeFederativa getUnidadeFederativaByIdUfDestino() {
		return this.unidadeFederativaByIdUfDestino;
	}

	public void setUnidadeFederativaByIdUfDestino(
			UnidadeFederativa unidadeFederativaByIdUfDestino) {
		this.unidadeFederativaByIdUfDestino = unidadeFederativaByIdUfDestino;
	}

	public Municipio getMunicipioByIdMunicipioOrigem() {
		return this.municipioByIdMunicipioOrigem;
	}

	public void setMunicipioByIdMunicipioOrigem(
			Municipio municipioByIdMunicipioOrigem) {
		this.municipioByIdMunicipioOrigem = municipioByIdMunicipioOrigem;
	}

	public Municipio getMunicipioByIdMunicipioDestino() {
		return this.municipioByIdMunicipioDestino;
	}

	public void setMunicipioByIdMunicipioDestino(
			Municipio municipioByIdMunicipioDestino) {
		this.municipioByIdMunicipioDestino = municipioByIdMunicipioDestino;
	}

	public Aeroporto getAeroportoByIdAeroportoOrigem() {
		return this.aeroportoByIdAeroportoOrigem;
	}

	public void setAeroportoByIdAeroportoOrigem(
			Aeroporto aeroportoByIdAeroportoOrigem) {
		this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
	}

	public Aeroporto getAeroportoByIdAeroportoDestino() {
		return this.aeroportoByIdAeroportoDestino;
	}

	public void setAeroportoByIdAeroportoDestino(
			Aeroporto aeroportoByIdAeroportoDestino) {
		this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
	}

	public Zona getZonaByIdZonaDestino() {
		return this.zonaByIdZonaDestino;
	}

	public void setZonaByIdZonaDestino(Zona zonaByIdZonaDestino) {
		this.zonaByIdZonaDestino = zonaByIdZonaDestino;
	}

	public Zona getZonaByIdZonaOrigem() {
		return this.zonaByIdZonaOrigem;
	}

	public void setZonaByIdZonaOrigem(Zona zonaByIdZonaOrigem) {
		this.zonaByIdZonaOrigem = zonaByIdZonaOrigem;
	}

	public Simulacao getSimulacao() {
		return this.simulacao;
	}

	public void setSimulacao(Simulacao simulacao) {
		this.simulacao = simulacao;
	}

	public TabelaDivisaoCliente getTabelaDivisaoCliente() {
		return this.tabelaDivisaoCliente;
	}

	public void setTabelaDivisaoCliente(
			TabelaDivisaoCliente tabelaDivisaoCliente) {
		this.tabelaDivisaoCliente = tabelaDivisaoCliente;
	}

	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public Cotacao getCotacao() {
		return this.cotacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public Proposta getProposta() {
		return proposta;
	}

	public void setProposta(Proposta proposta) {
		this.proposta = proposta;
	}

	public TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() {
		return this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;
	}

	public void setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocMunicipioOrigem) {
		this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem = tipoLocalizacaoMunicipioByIdTipoLocMunicipioOrigem;
	}

	public TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() {
		return this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;
	}

	public void setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocMunicipioDestino) {
		this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino = tipoLocalizacaoMunicipioByIdTipoLocMunicipioDestino;
	}

	public Cliente getClienteByIdClienteRedespacho() {
		return this.clienteByIdClienteRedespacho;
	}

	public void setClienteByIdClienteRedespacho(
			Cliente clienteByIdClienteRedespacho) {
		this.clienteByIdClienteRedespacho = clienteByIdClienteRedespacho;
	}

	public Pais getPaisByIdPaisDestino() {
		return this.paisByIdPaisDestino;
	}

	public void setPaisByIdPaisDestino(Pais paisByIdPaisDestino) {
		this.paisByIdPaisDestino = paisByIdPaisDestino;
	}

	public Pais getPaisByIdPaisOrigem() {
		return this.paisByIdPaisOrigem;
	}

	public void setPaisByIdPaisOrigem(Pais paisByIdPaisOrigem) {
		this.paisByIdPaisOrigem = paisByIdPaisOrigem;
	}

	public Filial getFilialByIdFilialOrigem() {
		return this.filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	public Filial getFilialByIdFilialMercurioRedespacho() {
		return this.filialByIdFilialMercurioRedespacho;
	}

	public void setFilialByIdFilialMercurioRedespacho(
			Filial filialByIdFilialMercurioRedespacho) {
		this.filialByIdFilialMercurioRedespacho = filialByIdFilialMercurioRedespacho;
	}

	public Filial getFilialByIdFilialDestino() {
		return this.filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.GeneralidadeCliente.class) 
	public List getGeneralidadeClientes() {
		return this.generalidadeClientes;
	}

	public void setGeneralidadeClientes(List generalidadeClientes) {
		this.generalidadeClientes = generalidadeClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.TaxaCliente.class) 
	public List getTaxaClientes() {
		return this.taxaClientes;
	}

	public void setTaxaClientes(List taxaClientes) {
		this.taxaClientes = taxaClientes;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idParametroCliente",
				getIdParametroCliente()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParametroCliente))
			return false;
		ParametroCliente castOther = (ParametroCliente) other;
		return new EqualsBuilder().append(this.getIdParametroCliente(),
				castOther.getIdParametroCliente()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdParametroCliente())
			.toHashCode();
	}

	public GrupoRegiao getGrupoRegiaoOrigem() {
		return grupoRegiaoOrigem;
	}

	public void setGrupoRegiaoOrigem(GrupoRegiao grupoRegiaoOrigem) {
		this.grupoRegiaoOrigem = grupoRegiaoOrigem;
	}

	public GrupoRegiao getGrupoRegiaoDestino() {
		return grupoRegiaoDestino;
	}

	public void setGrupoRegiaoDestino(GrupoRegiao grupoRegiaoDestino) {
		this.grupoRegiaoDestino = grupoRegiaoDestino;
	}

	public Boolean getBlEfetivaProposta() {
		return blEfetivaProposta;
	}

	public void setBlEfetivaProposta(Boolean blEfetivaProposta) {
		this.blEfetivaProposta = blEfetivaProposta;
	}

	@Override
	public ParametroCliente clone(){
		try {
			return (ParametroCliente)super.clone();
		} catch (CloneNotSupportedException e) {
		
}
		return null;
	}
	
	public Integer getIndexRotaOrigem() {
		
		indexRotaOrigem = RestricaoRota.indexRota(getZonaByIdZonaOrigem(), 
				getPaisByIdPaisOrigem(), getFilialByIdFilialOrigem(),
				getUnidadeFederativaByIdUfOrigem(),
				getMunicipioByIdMunicipioOrigem(), getGrupoRegiaoOrigem(), 
				getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(), 
				getAeroportoByIdAeroportoOrigem());		
		
		return indexRotaOrigem;
}

	public void setIndexRotaOrigem(Integer indexRotaOrigem) {
		this.indexRotaOrigem = indexRotaOrigem;
	}
	
	public Integer getIndexRotaDestino() {
		
		indexRotaDestino = RestricaoRota.indexRota(getZonaByIdZonaDestino(), 
				getPaisByIdPaisDestino(), getFilialByIdFilialDestino(),
				getUnidadeFederativaByIdUfDestino(),
				getMunicipioByIdMunicipioDestino(), getGrupoRegiaoDestino(), 
				getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(), 
				getAeroportoByIdAeroportoDestino());		
		
		return indexRotaDestino;
	}

	public void setIndexRotaDestino(Integer indexRotaDestino) {
		this.indexRotaDestino = indexRotaDestino;
	}

	public BigDecimal getPcReajPercentualTRT() {
		return pcReajPercentualTRT;
	}

	public void setPcReajPercentualTRT(BigDecimal pcReajPercentualTRT) {
		this.pcReajPercentualTRT = pcReajPercentualTRT;
	}

	public BigDecimal getPcReajPercentualTDE() {
		return pcReajPercentualTDE;
	}

	public void setPcReajPercentualTDE(BigDecimal pcReajPercentualTDE) {
		this.pcReajPercentualTDE = pcReajPercentualTDE;
	}

	public BigDecimal getPcReajPercentualGris() {
		return pcReajPercentualGris;
	}

	public void setPcReajPercentualGris(BigDecimal pcReajPercentualGris) {
		this.pcReajPercentualGris = pcReajPercentualGris;
	}

	public BigDecimal getPcReajReferencia() {
		return pcReajReferencia;
	}

	public void setPcReajReferencia(BigDecimal pcReajReferencia) {
		this.pcReajReferencia = pcReajReferencia;
	}

	public BigDecimal getPcReajVlMinimoFretePeso() {
		return pcReajVlMinimoFretePeso;
	}

	public void setPcReajVlMinimoFretePeso(BigDecimal pcReajVlMinimoFretePeso) {
		this.pcReajVlMinimoFretePeso = pcReajVlMinimoFretePeso;
	}

	public BigDecimal getPcReajVlMinimoProg() {
		return pcReajVlMinimoProg;
	}

	public void setPcReajVlMinimoProg(BigDecimal pcReajVlMinimoProg) {
		this.pcReajVlMinimoProg = pcReajVlMinimoProg;
	}

    public List getValoresFaixaProgressivaProposta() {
        return valoresFaixaProgressivaProposta;
    }

    public void setValoresFaixaProgressivaProposta(List valoresFaixaProgressivaProposta) {
        this.valoresFaixaProgressivaProposta = valoresFaixaProgressivaProposta;
    }
}
