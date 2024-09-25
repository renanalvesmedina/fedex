package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.Zona;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;

public class ParametroClienteLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idParametroClienteLog;
	private ParametroCliente parametroCliente;
	private YearMonthDay dtVigenciaInicial;
	private DomainValue tpIndicadorPercentualGris;
	private BigDecimal vlPercentualGris;
	private DomainValue tpIndicadorMinimoGris;
	private BigDecimal vlMinimoGris;
	private DomainValue tpIndicadorPedagio;
	private BigDecimal vlPedagio;
	private DomainValue tpIndicadorMinFretePeso;
	private BigDecimal vlMinFretePeso;
	private DomainValue tpIndicadorPercMinimoProgr;
	private BigDecimal vlPercMinimoProgr;
	private DomainValue tpIndicadorFretePeso;
	private BigDecimal vlFretePeso;
	private DomainValue tpIndicadorAdvalorem;
	private BigDecimal vlAdvalorem;
	private DomainValue tpIndicadorAdvalorem2;
	private BigDecimal vlAdvalorem2;
	private DomainValue tpIndicadorValorReferencia;
	private BigDecimal vlValorReferencia;
	private BigDecimal vlMinimoFreteQuilo;
	private BigDecimal pcFretePercentual;
	private BigDecimal vlMinimoFretePercentual;
	private BigDecimal vlToneladaFretePercentual;
	private String psFretePercentual;
	private BigDecimal pcDescontoFreteTotal;
	private DomainValue tpIndicVlrTblEspecifica;
	private BigDecimal vlTblEspecifica;
	private BigDecimal vlFreteVolume;
	private boolean blPagaCubagem;
	private BigDecimal pcPagaCubagem;
	private boolean blPagaPesoExcedente;
	private DomainValue tpTarifaMinima;
	private BigDecimal vlTarifaMinima;
	private BigDecimal pcCobrancaReentrega;
	private BigDecimal pcCobrancaDevolucoes;
	private DomainValue tpSituacaoParametro;
	private TabelaDivisaoCliente tabelaDivisaoCliente;
	private Cliente clienteRedespacho;
	private Filial filialMercurioRedespacho;
	private Filial filialOrigem;
	private Filial filialDestino;
	private Zona zonaOrigem;
	private Zona zonaDestino;
	private Pais paisOrigem;
	private Pais paisDestino;
	private TipoLocalizacaoMunicipio tipoLocMunicipioOrigem;
	private TipoLocalizacaoMunicipio tipoLocMunicipioDestino;
	private UnidadeFederativa ufOrigem;
	private UnidadeFederativa ufDestino;
	private Aeroporto aeroportoOrigem;
	private Aeroporto aeroportoDestino;
	private Municipio municipioOrigem;
	private Municipio municipioDestino;
	private Simulacao simulacao;
	private Cotacao cotacao;
	private BigDecimal pcReajFretePeso;
	private BigDecimal pcReajVlMinimoFreteQuilo;
	private BigDecimal pcReajVlFreteVolume;
	private BigDecimal pcReajTarifaMinima;
	private BigDecimal pcReajVlTarifaEspecifica;
	private BigDecimal pcReajAdvalorem;
	private BigDecimal pcReajAdvalorem2;
	private BigDecimal pcReajVlMinimoFretePercen;
	private BigDecimal pcReajVlToneladaFretePerc;
	private BigDecimal pcReajMinimoGris;
	private YearMonthDay dtVigenciaFinal;
	private String dsEspecificacaoRota;
	private BigDecimal pcReajPedagio;
	private DomainValue tpIndicadorPercentualTde;
	private BigDecimal vlPercentualTde;
	private DomainValue tpIndicadorMinimoTde;
	private BigDecimal vlMinimoTde;
	private BigDecimal pcReajMinimoTde;
	private TabelaPreco tabelaPreco;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdParametroClienteLog() {
   
		return idParametroClienteLog;
	}
   
	public void setIdParametroClienteLog(long idParametroClienteLog) {
   
		this.idParametroClienteLog = idParametroClienteLog;
	}
	
	public ParametroCliente getParametroCliente() {
   
		return parametroCliente;
	}
   
	public void setParametroCliente(ParametroCliente parametroCliente) {
   
		this.parametroCliente = parametroCliente;
	}
	
	public YearMonthDay getDtVigenciaInicial() {
   
		return dtVigenciaInicial;
	}
   
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
   
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public DomainValue getTpIndicadorPercentualGris() {
   
		return tpIndicadorPercentualGris;
	}
   
	public void setTpIndicadorPercentualGris(
			DomainValue tpIndicadorPercentualGris) {
   
		this.tpIndicadorPercentualGris = tpIndicadorPercentualGris;
	}
	
	public BigDecimal getVlPercentualGris() {
   
		return vlPercentualGris;
	}
   
	public void setVlPercentualGris(BigDecimal vlPercentualGris) {
   
		this.vlPercentualGris = vlPercentualGris;
	}
	
	public DomainValue getTpIndicadorMinimoGris() {
   
		return tpIndicadorMinimoGris;
	}
   
	public void setTpIndicadorMinimoGris(DomainValue tpIndicadorMinimoGris) {
   
		this.tpIndicadorMinimoGris = tpIndicadorMinimoGris;
	}
	
	public BigDecimal getVlMinimoGris() {
   
		return vlMinimoGris;
	}
   
	public void setVlMinimoGris(BigDecimal vlMinimoGris) {
   
		this.vlMinimoGris = vlMinimoGris;
	}
	
	public DomainValue getTpIndicadorPedagio() {
   
		return tpIndicadorPedagio;
	}
   
	public void setTpIndicadorPedagio(DomainValue tpIndicadorPedagio) {
   
		this.tpIndicadorPedagio = tpIndicadorPedagio;
	}
	
	public BigDecimal getVlPedagio() {
   
		return vlPedagio;
	}
   
	public void setVlPedagio(BigDecimal vlPedagio) {
   
		this.vlPedagio = vlPedagio;
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
	
	public DomainValue getTpIndicadorPercMinimoProgr() {
   
		return tpIndicadorPercMinimoProgr;
	}
   
	public void setTpIndicadorPercMinimoProgr(
			DomainValue tpIndicadorPercMinimoProgr) {
   
		this.tpIndicadorPercMinimoProgr = tpIndicadorPercMinimoProgr;
	}
	
	public BigDecimal getVlPercMinimoProgr() {
   
		return vlPercMinimoProgr;
	}
   
	public void setVlPercMinimoProgr(BigDecimal vlPercMinimoProgr) {
   
		this.vlPercMinimoProgr = vlPercMinimoProgr;
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
	
	public DomainValue getTpIndicadorValorReferencia() {
   
		return tpIndicadorValorReferencia;
	}
   
	public void setTpIndicadorValorReferencia(
			DomainValue tpIndicadorValorReferencia) {
   
		this.tpIndicadorValorReferencia = tpIndicadorValorReferencia;
	}
	
	public BigDecimal getVlValorReferencia() {
   
		return vlValorReferencia;
	}
   
	public void setVlValorReferencia(BigDecimal vlValorReferencia) {
   
		this.vlValorReferencia = vlValorReferencia;
	}
	
	public BigDecimal getVlMinimoFreteQuilo() {
   
		return vlMinimoFreteQuilo;
	}
   
	public void setVlMinimoFreteQuilo(BigDecimal vlMinimoFreteQuilo) {
   
		this.vlMinimoFreteQuilo = vlMinimoFreteQuilo;
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
	
	public String getPsFretePercentual() {
   
		return psFretePercentual;
	}
   
	public void setPsFretePercentual(String psFretePercentual) {
   
		this.psFretePercentual = psFretePercentual;
	}
	
	public BigDecimal getPcDescontoFreteTotal() {
   
		return pcDescontoFreteTotal;
	}
   
	public void setPcDescontoFreteTotal(BigDecimal pcDescontoFreteTotal) {
   
		this.pcDescontoFreteTotal = pcDescontoFreteTotal;
	}
	
	public DomainValue getTpIndicVlrTblEspecifica() {
   
		return tpIndicVlrTblEspecifica;
	}
   
	public void setTpIndicVlrTblEspecifica(DomainValue tpIndicVlrTblEspecifica) {
   
		this.tpIndicVlrTblEspecifica = tpIndicVlrTblEspecifica;
	}
	
	public BigDecimal getVlTblEspecifica() {
   
		return vlTblEspecifica;
	}
   
	public void setVlTblEspecifica(BigDecimal vlTblEspecifica) {
   
		this.vlTblEspecifica = vlTblEspecifica;
	}
	
	public BigDecimal getVlFreteVolume() {
   
		return vlFreteVolume;
	}
   
	public void setVlFreteVolume(BigDecimal vlFreteVolume) {
   
		this.vlFreteVolume = vlFreteVolume;
	}
	
	public boolean isBlPagaCubagem() {
   
		return blPagaCubagem;
	}
   
	public void setBlPagaCubagem(boolean blPagaCubagem) {
   
		this.blPagaCubagem = blPagaCubagem;
	}
	
	public BigDecimal getPcPagaCubagem() {
   
		return pcPagaCubagem;
	}
   
	public void setPcPagaCubagem(BigDecimal pcPagaCubagem) {
   
		this.pcPagaCubagem = pcPagaCubagem;
	}
	
	public boolean isBlPagaPesoExcedente() {
   
		return blPagaPesoExcedente;
	}
   
	public void setBlPagaPesoExcedente(boolean blPagaPesoExcedente) {
   
		this.blPagaPesoExcedente = blPagaPesoExcedente;
	}
	
	public DomainValue getTpTarifaMinima() {
   
		return tpTarifaMinima;
	}
   
	public void setTpTarifaMinima(DomainValue tpTarifaMinima) {
   
		this.tpTarifaMinima = tpTarifaMinima;
	}
	
	public BigDecimal getVlTarifaMinima() {
   
		return vlTarifaMinima;
	}
   
	public void setVlTarifaMinima(BigDecimal vlTarifaMinima) {
   
		this.vlTarifaMinima = vlTarifaMinima;
	}
	
	public BigDecimal getPcCobrancaReentrega() {
   
		return pcCobrancaReentrega;
	}
   
	public void setPcCobrancaReentrega(BigDecimal pcCobrancaReentrega) {
   
		this.pcCobrancaReentrega = pcCobrancaReentrega;
	}
	
	public BigDecimal getPcCobrancaDevolucoes() {
   
		return pcCobrancaDevolucoes;
	}
   
	public void setPcCobrancaDevolucoes(BigDecimal pcCobrancaDevolucoes) {
   
		this.pcCobrancaDevolucoes = pcCobrancaDevolucoes;
	}
	
	public DomainValue getTpSituacaoParametro() {
   
		return tpSituacaoParametro;
	}
   
	public void setTpSituacaoParametro(DomainValue tpSituacaoParametro) {
   
		this.tpSituacaoParametro = tpSituacaoParametro;
	}
	
	public TabelaDivisaoCliente getTabelaDivisaoCliente() {
   
		return tabelaDivisaoCliente;
	}
   
	public void setTabelaDivisaoCliente(
			TabelaDivisaoCliente tabelaDivisaoCliente) {
   
		this.tabelaDivisaoCliente = tabelaDivisaoCliente;
	}
	
	public Cliente getClienteRedespacho() {
   
		return clienteRedespacho;
	}
   
	public void setClienteRedespacho(Cliente clienteRedespacho) {
   
		this.clienteRedespacho = clienteRedespacho;
	}
	
	public Filial getFilialMercurioRedespacho() {
   
		return filialMercurioRedespacho;
	}
   
	public void setFilialMercurioRedespacho(Filial filialMercurioRedespacho) {
   
		this.filialMercurioRedespacho = filialMercurioRedespacho;
	}
	
	public Filial getFilialOrigem() {
   
		return filialOrigem;
	}
   
	public void setFilialOrigem(Filial filialOrigem) {
   
		this.filialOrigem = filialOrigem;
	}
	
	public Filial getFilialDestino() {
   
		return filialDestino;
	}
   
	public void setFilialDestino(Filial filialDestino) {
   
		this.filialDestino = filialDestino;
	}
	
	public Zona getZonaOrigem() {
   
		return zonaOrigem;
	}
   
	public void setZonaOrigem(Zona zonaOrigem) {
   
		this.zonaOrigem = zonaOrigem;
	}
	
	public Zona getZonaDestino() {
   
		return zonaDestino;
	}
   
	public void setZonaDestino(Zona zonaDestino) {
   
		this.zonaDestino = zonaDestino;
	}
	
	public Pais getPaisOrigem() {
   
		return paisOrigem;
	}
   
	public void setPaisOrigem(Pais paisOrigem) {
   
		this.paisOrigem = paisOrigem;
	}
	
	public Pais getPaisDestino() {
   
		return paisDestino;
	}
   
	public void setPaisDestino(Pais paisDestino) {
   
		this.paisDestino = paisDestino;
	}
	
	public TipoLocalizacaoMunicipio getTipoLocMunicipioOrigem() {
   
		return tipoLocMunicipioOrigem;
	}
   
	public void setTipoLocMunicipioOrigem(
			TipoLocalizacaoMunicipio tipoLocMunicipioOrigem) {
   
		this.tipoLocMunicipioOrigem = tipoLocMunicipioOrigem;
	}
	
	public TipoLocalizacaoMunicipio getTipoLocMunicipioDestino() {
   
		return tipoLocMunicipioDestino;
	}
   
	public void setTipoLocMunicipioDestino(
			TipoLocalizacaoMunicipio tipoLocMunicipioDestino) {
   
		this.tipoLocMunicipioDestino = tipoLocMunicipioDestino;
	}
	
	public UnidadeFederativa getUfOrigem() {
   
		return ufOrigem;
	}
   
	public void setUfOrigem(UnidadeFederativa ufOrigem) {
   
		this.ufOrigem = ufOrigem;
	}
	
	public UnidadeFederativa getUfDestino() {
   
		return ufDestino;
	}
   
	public void setUfDestino(UnidadeFederativa ufDestino) {
   
		this.ufDestino = ufDestino;
	}
	
	public Aeroporto getAeroportoOrigem() {
   
		return aeroportoOrigem;
	}
   
	public void setAeroportoOrigem(Aeroporto aeroportoOrigem) {
   
		this.aeroportoOrigem = aeroportoOrigem;
	}
	
	public Aeroporto getAeroportoDestino() {
   
		return aeroportoDestino;
	}
   
	public void setAeroportoDestino(Aeroporto aeroportoDestino) {
   
		this.aeroportoDestino = aeroportoDestino;
	}
	
	public Municipio getMunicipioOrigem() {
   
		return municipioOrigem;
	}
   
	public void setMunicipioOrigem(Municipio municipioOrigem) {
   
		this.municipioOrigem = municipioOrigem;
	}
	
	public Municipio getMunicipioDestino() {
   
		return municipioDestino;
	}
   
	public void setMunicipioDestino(Municipio municipioDestino) {
   
		this.municipioDestino = municipioDestino;
	}
	
	public Simulacao getSimulacao() {
   
		return simulacao;
	}
   
	public void setSimulacao(Simulacao simulacao) {
   
		this.simulacao = simulacao;
	}
	
	public Cotacao getCotacao() {
   
		return cotacao;
	}
   
	public void setCotacao(Cotacao cotacao) {
   
		this.cotacao = cotacao;
	}
	
	public BigDecimal getPcReajFretePeso() {
   
		return pcReajFretePeso;
	}
   
	public void setPcReajFretePeso(BigDecimal pcReajFretePeso) {
   
		this.pcReajFretePeso = pcReajFretePeso;
	}
	
	public BigDecimal getPcReajVlMinimoFreteQuilo() {
   
		return pcReajVlMinimoFreteQuilo;
	}
   
	public void setPcReajVlMinimoFreteQuilo(BigDecimal pcReajVlMinimoFreteQuilo) {
   
		this.pcReajVlMinimoFreteQuilo = pcReajVlMinimoFreteQuilo;
	}
	
	public BigDecimal getPcReajVlFreteVolume() {
   
		return pcReajVlFreteVolume;
	}
   
	public void setPcReajVlFreteVolume(BigDecimal pcReajVlFreteVolume) {
   
		this.pcReajVlFreteVolume = pcReajVlFreteVolume;
	}
	
	public BigDecimal getPcReajTarifaMinima() {
   
		return pcReajTarifaMinima;
	}
   
	public void setPcReajTarifaMinima(BigDecimal pcReajTarifaMinima) {
   
		this.pcReajTarifaMinima = pcReajTarifaMinima;
	}
	
	public BigDecimal getPcReajVlTarifaEspecifica() {
   
		return pcReajVlTarifaEspecifica;
	}
   
	public void setPcReajVlTarifaEspecifica(BigDecimal pcReajVlTarifaEspecifica) {
   
		this.pcReajVlTarifaEspecifica = pcReajVlTarifaEspecifica;
	}
	
	public BigDecimal getPcReajAdvalorem() {
   
		return pcReajAdvalorem;
	}
   
	public void setPcReajAdvalorem(BigDecimal pcReajAdvalorem) {
   
		this.pcReajAdvalorem = pcReajAdvalorem;
	}
	
	public BigDecimal getPcReajAdvalorem2() {
   
		return pcReajAdvalorem2;
	}
   
	public void setPcReajAdvalorem2(BigDecimal pcReajAdvalorem2) {
   
		this.pcReajAdvalorem2 = pcReajAdvalorem2;
	}
	
	public BigDecimal getPcReajVlMinimoFretePercen() {
   
		return pcReajVlMinimoFretePercen;
	}
   
	public void setPcReajVlMinimoFretePercen(
			BigDecimal pcReajVlMinimoFretePercen) {
   
		this.pcReajVlMinimoFretePercen = pcReajVlMinimoFretePercen;
	}
	
	public BigDecimal getPcReajVlToneladaFretePerc() {
   
		return pcReajVlToneladaFretePerc;
	}
   
	public void setPcReajVlToneladaFretePerc(
			BigDecimal pcReajVlToneladaFretePerc) {
   
		this.pcReajVlToneladaFretePerc = pcReajVlToneladaFretePerc;
	}
	
	public BigDecimal getPcReajMinimoGris() {
   
		return pcReajMinimoGris;
	}
   
	public void setPcReajMinimoGris(BigDecimal pcReajMinimoGris) {
   
		this.pcReajMinimoGris = pcReajMinimoGris;
	}
	
	public YearMonthDay getDtVigenciaFinal() {
   
		return dtVigenciaFinal;
	}
   
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
   
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	
	public String getDsEspecificacaoRota() {
   
		return dsEspecificacaoRota;
	}
   
	public void setDsEspecificacaoRota(String dsEspecificacaoRota) {
   
		this.dsEspecificacaoRota = dsEspecificacaoRota;
	}
	
	public BigDecimal getPcReajPedagio() {
   
		return pcReajPedagio;
	}
   
	public void setPcReajPedagio(BigDecimal pcReajPedagio) {
   
		this.pcReajPedagio = pcReajPedagio;
	}
	
	public DomainValue getTpIndicadorPercentualTde() {
   
		return tpIndicadorPercentualTde;
	}
   
	public void setTpIndicadorPercentualTde(DomainValue tpIndicadorPercentualTde) {
   
		this.tpIndicadorPercentualTde = tpIndicadorPercentualTde;
	}
	
	public BigDecimal getVlPercentualTde() {
   
		return vlPercentualTde;
	}
   
	public void setVlPercentualTde(BigDecimal vlPercentualTde) {
   
		this.vlPercentualTde = vlPercentualTde;
	}
	
	public DomainValue getTpIndicadorMinimoTde() {
   
		return tpIndicadorMinimoTde;
	}
   
	public void setTpIndicadorMinimoTde(DomainValue tpIndicadorMinimoTde) {
   
		this.tpIndicadorMinimoTde = tpIndicadorMinimoTde;
	}
	
	public BigDecimal getVlMinimoTde() {
   
		return vlMinimoTde;
	}
   
	public void setVlMinimoTde(BigDecimal vlMinimoTde) {
   
		this.vlMinimoTde = vlMinimoTde;
	}
	
	public BigDecimal getPcReajMinimoTde() {
   
		return pcReajMinimoTde;
	}
   
	public void setPcReajMinimoTde(BigDecimal pcReajMinimoTde) {
   
		this.pcReajMinimoTde = pcReajMinimoTde;
	}
	
	public TabelaPreco getTabelaPreco() {
   
		return tabelaPreco;
	}
   
	public void setTabelaPreco(TabelaPreco tabelaPreco) {
   
		this.tabelaPreco = tabelaPreco;
	}
	
	public DomainValue getTpOrigemLog() {
   
		return tpOrigemLog;
	}
   
	public void setTpOrigemLog(DomainValue tpOrigemLog) {
   
		this.tpOrigemLog = tpOrigemLog;
	}
	
	public String getLoginLog() {
   
		return loginLog;
	}
   
	public void setLoginLog(String loginLog) {
   
		this.loginLog = loginLog;
	}
	
	public DateTime getDhLog() {
   
		return dhLog;
	}
   
	public void setDhLog(DateTime dhLog) {
   
		this.dhLog = dhLog;
	}
	
	public DomainValue getOpLog() {
   
		return opLog;
	}
   
	public void setOpLog(DomainValue opLog) {
   
		this.opLog = opLog;
	}
	
   	public String toString() {
		return new ToStringBuilder(this).append("idParametroClienteLog",
				getIdParametroClienteLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParametroClienteLog))
			return false;
		ParametroClienteLog castOther = (ParametroClienteLog) other;
		return new EqualsBuilder().append(this.getIdParametroClienteLog(),
				castOther.getIdParametroClienteLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdParametroClienteLog())
			.toHashCode();
	}
} 