package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.municipios.dto.MunicipioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.TipoLocalizacaoMunicipioDTO;
import com.mercurio.lms.rest.municipios.dto.AeroportoSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.PaisDTO;
import com.mercurio.lms.rest.municipios.dto.UnidadeFederativaDTO;
import com.mercurio.lms.rest.municipios.dto.ZonaDTO;
import com.mercurio.lms.rest.tabeladeprecos.GrupoRegiaoDTO;
import com.mercurio.lms.rest.tabeladeprecos.ServicoSuggestDTO;
import com.mercurio.lms.rest.tabeladeprecos.TabelaPrecoSuggestDTO;

public class ParametroPropostaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	// Campos cabeçalho
	private DomainValue tpGeracaoProposta;
	private Long idSimulacao;
	private ClienteSuggestDTO cliente;
	private DivisaoClienteSuggestDTO divisaoCliente;
	private TabelaPrecoSuggestDTO tabelaPreco;
	private ServicoSuggestDTO servico;
	private String dsRotaOrigem;
	private String dsRotaDestino;
	// Fim Campos cabeçalho
	
	// Aba Parametros
	private String dsEspecificacaoRota;
	// Aba Parametros Frete Peso
	private DomainValue tpIndicadorMinFretePeso;
	private BigDecimal vlMinFretePeso;
	private DomainValue tpIndicadorPercMinimoProgr;
	private BigDecimal vlPercMinimoProgr;
	private DomainValue tpIndicadorFretePeso;
	private BigDecimal vlFretePeso;
	private BigDecimal vlMinimoFreteQuilo;
	private boolean blPagaPesoExcedente;
	private DomainValue tpTarifaMinima;
	private BigDecimal vlTarifaMinima;
	private BigDecimal vlFreteVolume;
	private DomainValue tpIndicVlrTblEspecifica;
	private BigDecimal vlTblEspecifica;
	// Aba Parametros Frete Valor
	private DomainValue tpIndicadorAdvalorem;
	private BigDecimal vlAdvalorem;
	private DomainValue tpIndicadorAdvalorem2;
	private BigDecimal vlAdvalorem2;
	private DomainValue tpIndicadorValorReferencia;
	private BigDecimal vlValorReferencia;
	// Aba Parametros Frete Percentual
	private BigDecimal pcFretePercentual;
	private BigDecimal vlMinimoFretePercentual;
	private BigDecimal vlToneladaFretePercentual;
	private BigDecimal psFretePercentual;
	// Aba Parametros GRIS
	private DomainValue tpIndicadorPercentualGris;
	private BigDecimal vlPercentualGris;
	private DomainValue tpIndicadorMinimoGris;
	private BigDecimal vlMinimoGris;
	// Aba Parametros Pedagio
	private DomainValue tpIndicadorPedagio;
	private BigDecimal vlPedagio;
	// Aba Parametros TRT
	private DomainValue tpIndicadorPercentualTrt;
	private BigDecimal vlPercentualTrt;
	private DomainValue tpIndicadorMinimoTrt;
	private BigDecimal vlMinimoTrt;
	// Aba Parametros TDE
	private DomainValue tpIndicadorPercentualTde;
	private BigDecimal vlPercentualTde;
	private DomainValue tpIndicadorMinimoTde;
	private BigDecimal vlMinimoTde;
	// Aba Parametros Total Frete
	private BigDecimal pcDescontoFreteTotal;
	private BigDecimal pcCobrancaReentrega;
	private BigDecimal pcCobrancaDevolucoes;
	
	// Fim Aba Parametros
	
	
	//Aba rota - origem
	private AeroportoSuggestDTO rotaOrigemAeroporto;
	private FilialSuggestDTO rotaOrigemFilial;
	private ZonaDTO rotaOrigemZona;
	private PaisDTO rotaOrigemPais;
	private UnidadeFederativaDTO rotaOrigemUf;
	private MunicipioDTO rotaOrigemMunicipio;
	private TipoLocalizacaoMunicipioDTO rotaOrigemTipoLocalizacao;
	private GrupoRegiaoDTO rotaOrigemGrupoRegiao;
	
	//Aba rota - origem
	private AeroportoSuggestDTO rotaDestinoAeroporto;
	private FilialSuggestDTO rotaDestinoFilial;
	private ZonaDTO rotaDestinoZona;
	private PaisDTO rotaDestinoPais;
	private UnidadeFederativaDTO rotaDestinoUf;
	private MunicipioDTO rotaDestinoMunicipio;
	private TipoLocalizacaoMunicipioDTO rotaDestinoTipoLocalizacao;
	private GrupoRegiaoDTO rotaDestinoGrupoRegiao;
	
	public Long getIdSimulacao() {
		return idSimulacao;
	}
	public void setIdSimulacao(Long idSimulacao) {
		this.idSimulacao = idSimulacao;
	}
	public ClienteSuggestDTO getCliente() {
		return cliente;
	}
	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}
	public DivisaoClienteSuggestDTO getDivisaoCliente() {
		return divisaoCliente;
	}
	public void setDivisaoCliente(DivisaoClienteSuggestDTO divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}
	public TabelaPrecoSuggestDTO getTabelaPreco() {
		return tabelaPreco;
	}
	public void setTabelaPreco(TabelaPrecoSuggestDTO tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}
	public ServicoSuggestDTO getServico() {
		return servico;
	}
	public void setServico(ServicoSuggestDTO servico) {
		this.servico = servico;
	}
	public String getDsRotaOrigem() {
		return dsRotaOrigem;
	}
	public void setDsRotaOrigem(String dsRotaOrigem) {
		this.dsRotaOrigem = dsRotaOrigem;
	}
	public DomainValue getTpGeracaoProposta() {
		return tpGeracaoProposta;
	}
	public void setTpGeracaoProposta(DomainValue tpGeracaoProposta) {
		this.tpGeracaoProposta = tpGeracaoProposta;
	}
	public String getDsRotaDestino() {
		return dsRotaDestino;
	}
	public void setDsRotaDestino(String dsRotaDestino) {
		this.dsRotaDestino = dsRotaDestino;
	}
	public String getDsEspecificacaoRota() {
		return dsEspecificacaoRota;
	}
	public void setDsEspecificacaoRota(String dsEspecificacaoRota) {
		this.dsEspecificacaoRota = dsEspecificacaoRota;
	}
	public AeroportoSuggestDTO getRotaOrigemAeroporto() {
		return rotaOrigemAeroporto;
	}
	public void setRotaOrigemAeroporto(AeroportoSuggestDTO rotaOrigemAeroporto) {
		this.rotaOrigemAeroporto = rotaOrigemAeroporto;
	}
	public FilialSuggestDTO getRotaOrigemFilial() {
		return rotaOrigemFilial;
	}
	public void setRotaOrigemFilial(FilialSuggestDTO rotaOrigemFilial) {
		this.rotaOrigemFilial = rotaOrigemFilial;
	}
	public ZonaDTO getRotaOrigemZona() {
		return rotaOrigemZona;
	}
	public void setRotaOrigemZona(ZonaDTO rotaOrigemZona) {
		this.rotaOrigemZona = rotaOrigemZona;
	}
	public PaisDTO getRotaOrigemPais() {
		return rotaOrigemPais;
	}
	public void setRotaOrigemPais(PaisDTO rotaOrigemPais) {
		this.rotaOrigemPais = rotaOrigemPais;
	}
	public UnidadeFederativaDTO getRotaOrigemUf() {
		return rotaOrigemUf;
	}
	public void setRotaOrigemUf(UnidadeFederativaDTO rotaOrigemUf) {
		this.rotaOrigemUf = rotaOrigemUf;
	}
	public AeroportoSuggestDTO getRotaDestinoAeroporto() {
		return rotaDestinoAeroporto;
	}
	public void setRotaDestinoAeroporto(AeroportoSuggestDTO rotaDestinoAeroporto) {
		this.rotaDestinoAeroporto = rotaDestinoAeroporto;
	}
	public FilialSuggestDTO getRotaDestinoFilial() {
		return rotaDestinoFilial;
	}
	public void setRotaDestinoFilial(FilialSuggestDTO rotaDestinoFilial) {
		this.rotaDestinoFilial = rotaDestinoFilial;
	}
	public ZonaDTO getRotaDestinoZona() {
		return rotaDestinoZona;
	}
	public void setRotaDestinoZona(ZonaDTO rotaDestinoZona) {
		this.rotaDestinoZona = rotaDestinoZona;
	}
	public PaisDTO getRotaDestinoPais() {
		return rotaDestinoPais;
	}
	public void setRotaDestinoPais(PaisDTO rotaDestinoPais) {
		this.rotaDestinoPais = rotaDestinoPais;
	}
	public UnidadeFederativaDTO getRotaDestinoUf() {
		return rotaDestinoUf;
	}
	public void setRotaDestinoUf(UnidadeFederativaDTO rotaDestinoUf) {
		this.rotaDestinoUf = rotaDestinoUf;
	}
	public MunicipioDTO getRotaDestinoMunicipio() {
		return rotaDestinoMunicipio;
	}
	public void setRotaDestinoMunicipio(MunicipioDTO rotaDestinoMunicipio) {
		this.rotaDestinoMunicipio = rotaDestinoMunicipio;
	}
	public MunicipioDTO getRotaOrigemMunicipio() {
		return rotaOrigemMunicipio;
	}
	public void setRotaOrigemMunicipio(MunicipioDTO rotaOrigemMunicipio) {
		this.rotaOrigemMunicipio = rotaOrigemMunicipio;
	}
	public TipoLocalizacaoMunicipioDTO getRotaOrigemTipoLocalizacao() {
		return rotaOrigemTipoLocalizacao;
	}
	public void setRotaOrigemTipoLocalizacao(
			TipoLocalizacaoMunicipioDTO rotaOrigemTipoLocalizacao) {
		this.rotaOrigemTipoLocalizacao = rotaOrigemTipoLocalizacao;
	}
	public GrupoRegiaoDTO getRotaOrigemGrupoRegiao() {
		return rotaOrigemGrupoRegiao;
	}
	public void setRotaOrigemGrupoRegiao(GrupoRegiaoDTO rotaOrigemGrupoRegiao) {
		this.rotaOrigemGrupoRegiao = rotaOrigemGrupoRegiao;
	}
	public TipoLocalizacaoMunicipioDTO getRotaDestinoTipoLocalizacao() {
		return rotaDestinoTipoLocalizacao;
	}
	public void setRotaDestinoTipoLocalizacao(
			TipoLocalizacaoMunicipioDTO rotaDestinoTipoLocalizacao) {
		this.rotaDestinoTipoLocalizacao = rotaDestinoTipoLocalizacao;
	}
	public GrupoRegiaoDTO getRotaDestinoGrupoRegiao() {
		return rotaDestinoGrupoRegiao;
	}
	public void setRotaDestinoGrupoRegiao(GrupoRegiaoDTO rotaDestinoGrupoRegiao) {
		this.rotaDestinoGrupoRegiao = rotaDestinoGrupoRegiao;
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
	public void setTpIndicadorPercMinimoProgr(DomainValue tpIndicadorPercMinimoProgr) {
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
	public BigDecimal getVlMinimoFreteQuilo() {
		return vlMinimoFreteQuilo;
	}
	public void setVlMinimoFreteQuilo(BigDecimal vlMinimoFreteQuilo) {
		this.vlMinimoFreteQuilo = vlMinimoFreteQuilo;
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
	public BigDecimal getVlFreteVolume() {
		return vlFreteVolume;
	}
	public void setVlFreteVolume(BigDecimal vlFreteVolume) {
		this.vlFreteVolume = vlFreteVolume;
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
	public void setTpIndicadorValorReferencia(DomainValue tpIndicadorValorReferencia) {
		this.tpIndicadorValorReferencia = tpIndicadorValorReferencia;
	}
	public BigDecimal getVlValorReferencia() {
		return vlValorReferencia;
	}
	public void setVlValorReferencia(BigDecimal vlValorReferencia) {
		this.vlValorReferencia = vlValorReferencia;
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
	public void setVlToneladaFretePercentual(BigDecimal vlToneladaFretePercentual) {
		this.vlToneladaFretePercentual = vlToneladaFretePercentual;
	}
	public BigDecimal getPsFretePercentual() {
		return psFretePercentual;
	}
	public void setPsFretePercentual(BigDecimal psFretePercentual) {
		this.psFretePercentual = psFretePercentual;
	}
	public DomainValue getTpIndicadorPercentualGris() {
		return tpIndicadorPercentualGris;
	}
	public void setTpIndicadorPercentualGris(DomainValue tpIndicadorPercentualGris) {
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
	public BigDecimal getPcDescontoFreteTotal() {
		return pcDescontoFreteTotal;
	}
	public void setPcDescontoFreteTotal(BigDecimal pcDescontoFreteTotal) {
		this.pcDescontoFreteTotal = pcDescontoFreteTotal;
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
	
}
