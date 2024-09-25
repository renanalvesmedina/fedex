package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.coleta.model.AwbColeta;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.prestcontasciaaerea.model.ItemFaturaCiaAerea;
import com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaSpot;
import com.mercurio.lms.vendas.model.Cliente;

/** @author LMS Custom Hibernate CodeGenerator */
public class Awb implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idAwb;

	/** persistent field */
	private Integer qtVolumes;

	/** persistent field */
	private BigDecimal psTotal;

	/** persistent field */
	private DateTime dhDigitacao;

	/** persistent field */
	private Long nrAwb;

	private Integer dvAwb;

	/** persistent field */
	private DomainValue tpFrete;

	/** nullable persistent field */
	private BigDecimal psCubado;

	/** nullable persistent field */
	private BigDecimal vlFrete;

	/** nullable persistent field */
	private BigDecimal vlFretePeso;
	
	/** nullable persistent field */
	private BigDecimal vlFreteCalculado;

	/** nullable persistent field */
	private BigDecimal vlTaxaTerrestre;

	/** nullable persistent field */
	private BigDecimal vlTaxaCombustivel;

	/** nullable persistent field */
	private BigDecimal pcAliquotaICMS;

	/** nullable persistent field */
	private BigDecimal vlICMS;

	/** nullable persistent field */
	private BigDecimal vlQuiloTarifaSpot;

	/** nullable persistent field */
	private DateTime dhPrevistaChegada;

	/** nullable persistent field */
	private DateTime dhEmissao;
	
	/** nullable persistent field */
	private DateTime dhCancelamento;

	/** nullable persistent field */
	private DateTime dhPrevistaSaida;

	/** nullable persistent field */
	private String obAwb;

	/** nullable persistent field */
	private String obColetaCliente;

	/** nullable persistent field */
	private DomainValue tpStatusAwb;

	/** nullable persistent field */
	private Boolean blColetaCliente;

	/** nullable persistent field */
	private String dsVooPrevisto;

	/** nullable persistent field */
	private DomainValue tpLocalEmissao;

	/** nullable persistent field */
	private BigDecimal vlBaseCalcImposto;

	/** nullable persistent field */
	private String dsSerie;

	/** persistent field */
	private Cliente clienteByIdClienteExpedidor;

	/** persistent field */
	private InscricaoEstadual inscricaoEstadualExpedidor;

	/** persistent field */
	private Cliente clienteByIdClienteTomador;

	/** persistent field */
	private InscricaoEstadual inscricaoEstadualTomador;
	
	/** persistent field */
	private Cliente clienteByIdClienteDestinatario;

	/** persistent field */
	private InscricaoEstadual inscricaoEstadualDestinatario;

	/** persistent field */
	private PrestacaoConta prestacaoConta;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoEscala;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoDestino;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoOrigem;

	/** persistent field */
	private NaturezaProduto naturezaProduto;

	/** persistent field */
	private Moeda moeda;

	/** persistent field */
	private CiaFilialMercurio ciaFilialMercurio;

	/** persistent field */
	private Filial filialByIdFilialDestino;

	/** persistent field */
	private Filial filialByIdFilialOrigem;

	/** persistent field */
	private TarifaSpot tarifaSpot;

	/** persistent field */
	private ProdutoEspecifico produtoEspecifico;

	private DomainValue tpAwb;

	/** persistent field */
	private List<CtoAwb> ctoAwbs;

	/** persistent field */
	private List<PreAlerta> preAlertas;

	/** persistent field */
	private List<AwbEmbalagem> awbEmbalagems;

	/** persistent field */
	private List<Dimensao> dimensoes;

	private List<ItemFaturaCiaAerea> itensFaturaCiaAerea;

	private String nrChave;

	private ApoliceSeguro apoliceSeguro;
	
	private TabelaPreco tabelaPreco;
	
	private Long nrCcTomadorServico;
	
	private String nrLvSeguro;
	
	private List<PreManifestoDocumento> preManifestoDocumentos;
	
	private List<ManifestoEntregaDocumento> manifestoEntregaDocumentos;
	
	private List<AwbColeta> awbColetas;
	
	private DomainValue tpLocalizacao;
	
	private Boolean blConferido;
	
	private Awb awbSubstituido;
	
	private DomainValue tpMotivoCancelamento;
	
	private String dsMotivoCancelamento;
	
	private String dsJustificativaPrejuizo;
	
	private Usuario usuarioJustificativa;
	
	private Usuario usuarioInclusao;
	
	private Usuario usuarioCancelamento;
	
	private List<LiberaAWBComplementar> liberaAWBComplementars;
	
	private List<String> listNrChaveCteAnt;
	
	private List<String> nrChaveInfNfe;
	
	/*Novos campos para inserção do AWB de forma automática*/
	private String dsProddominante;
	
	private String dsOutCaracteristicas;
	
	private BigDecimal vlTotalMercadoria;
	
	private BigDecimal psBaseCalc;
	
	private BigDecimal vlCubagem;
	
	private String nmResponsavelApolice;
	
	private BigDecimal vlTotalServico;
	
	private BigDecimal vlReceber;
	
	private String dsSituacaoTributaria;
	
	private String dsInfManuseio;
	
	private String cdCargaEspecial;
	
	private String dsCaracAdicServ;
	
	private String nrOperacionalAereo;
	
	private YearMonthDay dtPrevistaEntrega;
	
	private String dsClasse;
	
	private BigDecimal vlTarifa;
	
	private String dsIdentificacaoTomador;
	
	private String dsIdentificacaoEmissor;
	
	private Boolean blRetira;
	
	private String dsUsoEmissor;
	
	private List<ParcelaAwb> parcelasAwb;
	
	private String dsModeloDacte;
	
	private Long nrSerieDacte;
	
	private Long nrDacte;
	
	private String tpCte;
	
	private String tpServico;
	
	private String dsNaturezaPrestacao;
	
	private String dsAutorizacaoUso;

	private String cdTarifa;
	
	private String nrMinuta;

	private String nrApoliceSeguro;
	
	private String tpCaracteristicaServico;

	public Awb() {
	}

	public Awb(Long idAwb, Integer qtVolumes, BigDecimal psTotal, DateTime dhDigitacao, Long nrAwb, Integer dvAwb, DomainValue tpFrete, BigDecimal psCubado, BigDecimal vlFrete, BigDecimal vlFretePeso, BigDecimal vlFreteCalculado, BigDecimal vlTaxaTerrestre, BigDecimal vlTaxaCombustivel, BigDecimal pcAliquotaICMS, BigDecimal vlICMS, BigDecimal vlQuiloTarifaSpot, DateTime dhPrevistaChegada, DateTime dhEmissao, DateTime dhCancelamento, DateTime dhPrevistaSaida, String obAwb, String obColetaCliente, DomainValue tpStatusAwb, Boolean blColetaCliente, String dsVooPrevisto, DomainValue tpLocalEmissao, BigDecimal vlBaseCalcImposto, String dsSerie, Cliente clienteByIdClienteExpedidor, InscricaoEstadual inscricaoEstadualExpedidor, Cliente clienteByIdClienteTomador, InscricaoEstadual inscricaoEstadualTomador, Cliente clienteByIdClienteDestinatario, InscricaoEstadual inscricaoEstadualDestinatario, PrestacaoConta prestacaoConta, Aeroporto aeroportoByIdAeroportoEscala, Aeroporto aeroportoByIdAeroportoDestino, Aeroporto aeroportoByIdAeroportoOrigem, NaturezaProduto naturezaProduto, Moeda moeda, CiaFilialMercurio ciaFilialMercurio, Filial filialByIdFilialDestino, Filial filialByIdFilialOrigem, TarifaSpot tarifaSpot, ProdutoEspecifico produtoEspecifico, DomainValue tpAwb, List<CtoAwb> ctoAwbs, List<PreAlerta> preAlertas, List<AwbEmbalagem> awbEmbalagems, List<Dimensao> dimensoes, List<ItemFaturaCiaAerea> itensFaturaCiaAerea, String nrChave, ApoliceSeguro apoliceSeguro, TabelaPreco tabelaPreco, Long nrCcTomadorServico, String nrLvSeguro, List<PreManifestoDocumento> preManifestoDocumentos, List<ManifestoEntregaDocumento> manifestoEntregaDocumentos, List<AwbColeta> awbColetas, DomainValue tpLocalizacao, Boolean blConferido, Awb awbSubstituido, DomainValue tpMotivoCancelamento, String dsMotivoCancelamento, String dsJustificativaPrejuizo, Usuario usuarioJustificativa, Usuario usuarioInclusao, Usuario usuarioCancelamento, List<LiberaAWBComplementar> liberaAWBComplementars, List<String> listNrChaveCteAnt, List<String> nrChaveInfNfe, String dsProddominante, String dsOutCaracteristicas, BigDecimal vlTotalMercadoria, BigDecimal psBaseCalc, BigDecimal vlCubagem, String nmResponsavelApolice, BigDecimal vlTotalServico, BigDecimal vlReceber, String dsSituacaoTributaria, String dsInfManuseio, String cdCargaEspecial, String dsCaracAdicServ, String nrOperacionalAereo, YearMonthDay dtPrevistaEntrega, String dsClasse, BigDecimal vlTarifa, String dsIdentificacaoTomador, String dsIdentificacaoEmissor, Boolean blRetira, String dsUsoEmissor, List<ParcelaAwb> parcelasAwb, String dsModeloDacte, Long nrSerieDacte, Long nrDacte, String tpCte, String tpServico, String dsNaturezaPrestacao, String dsAutorizacaoUso, String cdTarifa, String nrMinuta, String nrApoliceSeguro, String tpCaracteristicaServico) {
		this.idAwb = idAwb;
		this.qtVolumes = qtVolumes;
		this.psTotal = psTotal;
		this.dhDigitacao = dhDigitacao;
		this.nrAwb = nrAwb;
		this.dvAwb = dvAwb;
		this.tpFrete = tpFrete;
		this.psCubado = psCubado;
		this.vlFrete = vlFrete;
		this.vlFretePeso = vlFretePeso;
		this.vlFreteCalculado = vlFreteCalculado;
		this.vlTaxaTerrestre = vlTaxaTerrestre;
		this.vlTaxaCombustivel = vlTaxaCombustivel;
		this.pcAliquotaICMS = pcAliquotaICMS;
		this.vlICMS = vlICMS;
		this.vlQuiloTarifaSpot = vlQuiloTarifaSpot;
		this.dhPrevistaChegada = dhPrevistaChegada;
		this.dhEmissao = dhEmissao;
		this.dhCancelamento = dhCancelamento;
		this.dhPrevistaSaida = dhPrevistaSaida;
		this.obAwb = obAwb;
		this.obColetaCliente = obColetaCliente;
		this.tpStatusAwb = tpStatusAwb;
		this.blColetaCliente = blColetaCliente;
		this.dsVooPrevisto = dsVooPrevisto;
		this.tpLocalEmissao = tpLocalEmissao;
		this.vlBaseCalcImposto = vlBaseCalcImposto;
		this.dsSerie = dsSerie;
		this.clienteByIdClienteExpedidor = clienteByIdClienteExpedidor;
		this.inscricaoEstadualExpedidor = inscricaoEstadualExpedidor;
		this.clienteByIdClienteTomador = clienteByIdClienteTomador;
		this.inscricaoEstadualTomador = inscricaoEstadualTomador;
		this.clienteByIdClienteDestinatario = clienteByIdClienteDestinatario;
		this.inscricaoEstadualDestinatario = inscricaoEstadualDestinatario;
		this.prestacaoConta = prestacaoConta;
		this.aeroportoByIdAeroportoEscala = aeroportoByIdAeroportoEscala;
		this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
		this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
		this.naturezaProduto = naturezaProduto;
		this.moeda = moeda;
		this.ciaFilialMercurio = ciaFilialMercurio;
		this.filialByIdFilialDestino = filialByIdFilialDestino;
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
		this.tarifaSpot = tarifaSpot;
		this.produtoEspecifico = produtoEspecifico;
		this.tpAwb = tpAwb;
		this.ctoAwbs = ctoAwbs;
		this.preAlertas = preAlertas;
		this.awbEmbalagems = awbEmbalagems;
		this.dimensoes = dimensoes;
		this.itensFaturaCiaAerea = itensFaturaCiaAerea;
		this.nrChave = nrChave;
		this.apoliceSeguro = apoliceSeguro;
		this.tabelaPreco = tabelaPreco;
		this.nrCcTomadorServico = nrCcTomadorServico;
		this.nrLvSeguro = nrLvSeguro;
		this.preManifestoDocumentos = preManifestoDocumentos;
		this.manifestoEntregaDocumentos = manifestoEntregaDocumentos;
		this.awbColetas = awbColetas;
		this.tpLocalizacao = tpLocalizacao;
		this.blConferido = blConferido;
		this.awbSubstituido = awbSubstituido;
		this.tpMotivoCancelamento = tpMotivoCancelamento;
		this.dsMotivoCancelamento = dsMotivoCancelamento;
		this.dsJustificativaPrejuizo = dsJustificativaPrejuizo;
		this.usuarioJustificativa = usuarioJustificativa;
		this.usuarioInclusao = usuarioInclusao;
		this.usuarioCancelamento = usuarioCancelamento;
		this.liberaAWBComplementars = liberaAWBComplementars;
		this.listNrChaveCteAnt = listNrChaveCteAnt;
		this.nrChaveInfNfe = nrChaveInfNfe;
		this.dsProddominante = dsProddominante;
		this.dsOutCaracteristicas = dsOutCaracteristicas;
		this.vlTotalMercadoria = vlTotalMercadoria;
		this.psBaseCalc = psBaseCalc;
		this.vlCubagem = vlCubagem;
		this.nmResponsavelApolice = nmResponsavelApolice;
		this.vlTotalServico = vlTotalServico;
		this.vlReceber = vlReceber;
		this.dsSituacaoTributaria = dsSituacaoTributaria;
		this.dsInfManuseio = dsInfManuseio;
		this.cdCargaEspecial = cdCargaEspecial;
		this.dsCaracAdicServ = dsCaracAdicServ;
		this.nrOperacionalAereo = nrOperacionalAereo;
		this.dtPrevistaEntrega = dtPrevistaEntrega;
		this.dsClasse = dsClasse;
		this.vlTarifa = vlTarifa;
		this.dsIdentificacaoTomador = dsIdentificacaoTomador;
		this.dsIdentificacaoEmissor = dsIdentificacaoEmissor;
		this.blRetira = blRetira;
		this.dsUsoEmissor = dsUsoEmissor;
		this.parcelasAwb = parcelasAwb;
		this.dsModeloDacte = dsModeloDacte;
		this.nrSerieDacte = nrSerieDacte;
		this.nrDacte = nrDacte;
		this.tpCte = tpCte;
		this.tpServico = tpServico;
		this.dsNaturezaPrestacao = dsNaturezaPrestacao;
		this.dsAutorizacaoUso = dsAutorizacaoUso;
		this.cdTarifa = cdTarifa;
		this.nrMinuta = nrMinuta;
		this.nrApoliceSeguro = nrApoliceSeguro;
		this.tpCaracteristicaServico = tpCaracteristicaServico;
	}

	public List<ItemFaturaCiaAerea> getItensFaturaCiaAerea() {
		return itensFaturaCiaAerea;
	}

	public void setItensFaturaCiaAerea(List<ItemFaturaCiaAerea> itensFaturaCiaAerea) {
		this.itensFaturaCiaAerea = itensFaturaCiaAerea;
	}

	public Long getIdAwb() {
		return this.idAwb;
	}

	public void setIdAwb(Long idAwb) {
		this.idAwb = idAwb;
	}

	public Integer getQtVolumes() {
		return this.qtVolumes;
	}

	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public BigDecimal getPsTotal() {
		return this.psTotal;
	}

	public void setPsTotal(BigDecimal psTotal) {
		this.psTotal = psTotal;
	}

	public DateTime getDhDigitacao() {
		return this.dhDigitacao;
	}

	public void setDhDigitacao(DateTime dhDigitacao) {
		this.dhDigitacao = dhDigitacao;
	}

	public Long getNrAwb() {
		return nrAwb;
	}

	public void setNrAwb(Long nrAwb) {
		this.nrAwb = nrAwb;
	}

	public Integer getDvAwb() {
		return dvAwb;
	}

	public void setDvAwb(Integer dvAwb) {
		this.dvAwb = dvAwb;
	}

	public DomainValue getTpFrete() {
		return this.tpFrete;
	}

	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}

	public BigDecimal getPsCubado() {
		return this.psCubado;
	}

	public void setPsCubado(BigDecimal psCubado) {
		this.psCubado = psCubado;
	}

	public BigDecimal getVlFrete() {
		return this.vlFrete;
	}

	public void setVlFrete(BigDecimal vlFrete) {
		this.vlFrete = vlFrete;
	}

	public BigDecimal getVlFretePeso() {
		return vlFretePeso;
	}

	public void setVlFretePeso(BigDecimal vlFretePeso) {
		this.vlFretePeso = vlFretePeso;
	}

	public BigDecimal getVlFreteCalculado() {
		return vlFreteCalculado;
	}

	public void setVlFreteCalculado(BigDecimal vlFreteCalculado) {
		this.vlFreteCalculado = vlFreteCalculado;
	}

	public BigDecimal getVlTaxaCombustivel() {
		return vlTaxaCombustivel;
	}

	public void setVlTaxaCombustivel(BigDecimal vlTaxaCombustivel) {
		this.vlTaxaCombustivel = vlTaxaCombustivel;
	}

	public BigDecimal getPcAliquotaICMS() {
		return pcAliquotaICMS;
	}

	public void setPcAliquotaICMS(BigDecimal pcAliquotaICMS) {
		this.pcAliquotaICMS = pcAliquotaICMS;
	}

	public BigDecimal getVlICMS() {
		return vlICMS;
	}

	public void setVlICMS(BigDecimal vlICMS) {
		this.vlICMS = vlICMS;
	}

	public BigDecimal getVlTaxaTerrestre() {
		return vlTaxaTerrestre;
	}

	public void setVlTaxaTerrestre(BigDecimal vlTaxaTerrestre) {
		this.vlTaxaTerrestre = vlTaxaTerrestre;
	}

	public BigDecimal getVlTaxa() {
		if (vlTaxaCombustivel == null && vlTaxaTerrestre == null) {
			return null;
		}

		if (vlTaxaCombustivel != null && vlTaxaTerrestre != null) {
			return vlTaxaCombustivel.add(vlTaxaTerrestre);
		}

		if (vlTaxaCombustivel != null) {
			return vlTaxaCombustivel;
		}

		return vlTaxaTerrestre;
	}

	public BigDecimal getVlQuiloTarifaSpot() {
		return this.vlQuiloTarifaSpot;
	}

	public void setVlQuiloTarifaSpot(BigDecimal vlQuiloTarifaSpot) {
		this.vlQuiloTarifaSpot = vlQuiloTarifaSpot;
	}

	public DateTime getDhPrevistaChegada() {
		return this.dhPrevistaChegada;
	}

	public void setDhPrevistaChegada(DateTime dhPrevistaChegada) {
		this.dhPrevistaChegada = dhPrevistaChegada;
	}

	public DateTime getDhEmissao() {
		return this.dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public DateTime getDhPrevistaSaida() {
		return this.dhPrevistaSaida;
	}

	public void setDhPrevistaSaida(DateTime dhPrevistaSaida) {
		this.dhPrevistaSaida = dhPrevistaSaida;
	}

	public String getObAwb() {
		return this.obAwb;
	}

	public void setObAwb(String obAwb) {
		this.obAwb = obAwb;
	}

	public String getObColetaCliente() {
		return this.obColetaCliente;
	}

	public void setObColetaCliente(String obColetaCliente) {
		this.obColetaCliente = obColetaCliente;
	}

	public DomainValue getTpStatusAwb() {
		return this.tpStatusAwb;
	}

	public void setTpStatusAwb(DomainValue tpStatusAwb) {
		this.tpStatusAwb = tpStatusAwb;
	}

	public Boolean getBlColetaCliente() {
		return this.blColetaCliente;
	}

	public void setBlColetaCliente(Boolean blColetaCliente) {
		this.blColetaCliente = blColetaCliente;
	}

	public String getDsVooPrevisto() {
		return this.dsVooPrevisto;
	}

	public void setDsVooPrevisto(String dsVooPrevisto) {
		this.dsVooPrevisto = dsVooPrevisto;
	}

	public DomainValue getTpLocalEmissao() {
		return this.tpLocalEmissao;
	}

	public void setTpLocalEmissao(DomainValue tpLocalEmissao) {
		this.tpLocalEmissao = tpLocalEmissao;
	}

	public BigDecimal getVlBaseCalcImposto() {
		return this.vlBaseCalcImposto;
	}

	public void setVlBaseCalcImposto(BigDecimal vlBaseCalcImposto) {
		this.vlBaseCalcImposto = vlBaseCalcImposto;
	}

	public String getDsSerie() {
		return this.dsSerie;
	}

	public void setDsSerie(String dsSerie) {
		this.dsSerie = dsSerie;
	}

	public Cliente getClienteByIdClienteExpedidor() {
		return this.clienteByIdClienteExpedidor;
	}

	public void setClienteByIdClienteExpedidor(
			Cliente clienteByIdClienteExpedidor) {
		this.clienteByIdClienteExpedidor = clienteByIdClienteExpedidor;
	}

	public InscricaoEstadual getInscricaoEstadualExpedidor() {
		return inscricaoEstadualExpedidor;
	}

	public void setInscricaoEstadualExpedidor(
			InscricaoEstadual inscricaoEstadualExpedidor) {
		this.inscricaoEstadualExpedidor = inscricaoEstadualExpedidor;
	}

	public Cliente getClienteByIdClienteDestinatario() {
		return this.clienteByIdClienteDestinatario;
	}

	public void setClienteByIdClienteDestinatario(
			Cliente clienteByIdClienteDestinatario) {
		this.clienteByIdClienteDestinatario = clienteByIdClienteDestinatario;
	}

	public InscricaoEstadual getInscricaoEstadualDestinatario() {
		return inscricaoEstadualDestinatario;
	}

	public void setInscricaoEstadualDestinatario(
			InscricaoEstadual inscricaoEstadualDestinatario) {
		this.inscricaoEstadualDestinatario = inscricaoEstadualDestinatario;
	}

	public PrestacaoConta getPrestacaoConta() {
		return this.prestacaoConta;
	}

	public void setPrestacaoConta(PrestacaoConta prestacaoConta) {
		this.prestacaoConta = prestacaoConta;
	}

	public Aeroporto getAeroportoByIdAeroportoEscala() {
		return this.aeroportoByIdAeroportoEscala;
	}

	public void setAeroportoByIdAeroportoEscala(
			Aeroporto aeroportoByIdAeroportoEscala) {
		this.aeroportoByIdAeroportoEscala = aeroportoByIdAeroportoEscala;
	}

	public Aeroporto getAeroportoByIdAeroportoDestino() {
		return this.aeroportoByIdAeroportoDestino;
	}

	public void setAeroportoByIdAeroportoDestino(
			Aeroporto aeroportoByIdAeroportoDestino) {
		this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
	}

	public Aeroporto getAeroportoByIdAeroportoOrigem() {
		return this.aeroportoByIdAeroportoOrigem;
	}

	public void setAeroportoByIdAeroportoOrigem(
			Aeroporto aeroportoByIdAeroportoOrigem) {
		this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
	}

	public NaturezaProduto getNaturezaProduto() {
		return this.naturezaProduto;
	}

	public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}

	public Moeda getMoeda() {
		return this.moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public CiaFilialMercurio getCiaFilialMercurio() {
		return this.ciaFilialMercurio;
	}

	public void setCiaFilialMercurio(CiaFilialMercurio ciaFilialMercurio) {
		this.ciaFilialMercurio = ciaFilialMercurio;
	}

	public Filial getFilialByIdFilialDestino() {
		return this.filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	public Filial getFilialByIdFilialOrigem() {
		return this.filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	public TarifaSpot getTarifaSpot() {
		return this.tarifaSpot;
	}

	public void setTarifaSpot(TarifaSpot tarifaSpot) {
		this.tarifaSpot = tarifaSpot;
	}

	public ProdutoEspecifico getProdutoEspecifico() {
		return this.produtoEspecifico;
	}

	public void setProdutoEspecifico(ProdutoEspecifico produtoEspecifico) {
		this.produtoEspecifico = produtoEspecifico;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoAwb.class) 
	public List<CtoAwb> getCtoAwbs() {
		return this.ctoAwbs;
	}

	public void setCtoAwbs(List<CtoAwb> ctoAwbs) {
		this.ctoAwbs = ctoAwbs;
	}

	public void addCtoAwb(CtoAwb ctoAwb) {
		if(this.ctoAwbs == null) {
			ctoAwbs = new ArrayList<CtoAwb>();
		}
		this.ctoAwbs.add(ctoAwb);
	}

	public boolean removeCtoAwb(CtoAwb ctoAwb) {
		if(this.ctoAwbs != null) {
			return this.ctoAwbs.remove(ctoAwb);
		}
		return false;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.PreAlerta.class) 
	public List<PreAlerta> getPreAlertas() {
		return this.preAlertas;
	}

	public void setPreAlertas(List<PreAlerta> preAlertas) {
		this.preAlertas = preAlertas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.AwbEmbalagem.class) 
	public List<AwbEmbalagem> getAwbEmbalagems() {
		return this.awbEmbalagems;
	}

	public void setAwbEmbalagems(List<AwbEmbalagem> awbEmbalagems) {
		this.awbEmbalagems = awbEmbalagems;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Dimensao.class) 
	public List<Dimensao> getDimensoes() {
		return this.dimensoes;
	}

	public void setDimensoes(List<Dimensao> dimensoes) {
		this.dimensoes = dimensoes;
	}

	public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

	public DomainValue getTpAwb() {
		return tpAwb;
	}

	public void setTpAwb(DomainValue tpAwb) {
		this.tpAwb = tpAwb;
	}

	public void addDimensao(Dimensao dimensao) {
		if(this.dimensoes == null) {
			this.dimensoes = new ArrayList<Dimensao>();
		}
		this.dimensoes.add(dimensao);
	}

	public boolean removeDimensao(Dimensao dimensao){
		if (this.dimensoes != null){
			return this.dimensoes.remove(dimensao);
		}
		return false;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idAwb", getIdAwb()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other){
			return true;
		}
		if (!(other instanceof Awb)){
			return false;
		}
		Awb castOther = (Awb) other;
		return new EqualsBuilder()
				.append(this.getIdAwb(), castOther.getIdAwb()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdAwb()).toHashCode();
	}

	public ApoliceSeguro getApoliceSeguro() {
		return apoliceSeguro;
	}

	public void setApoliceSeguro(ApoliceSeguro apoliceSeguro) {
		this.apoliceSeguro = apoliceSeguro;
	}

	public Long getNrCcTomadorServico() {
		return nrCcTomadorServico;
	}

	public void setNrCcTomadorServico(Long nrCcTomadorServico) {
		this.nrCcTomadorServico = nrCcTomadorServico;
	}

	public String getNrLvSeguro() {
		return nrLvSeguro;
	}

	public void setNrLvSeguro(String nrLvSeguro) {
		this.nrLvSeguro = nrLvSeguro;
	}

	public List<PreManifestoDocumento> getPreManifestoDocumentos() {
		return preManifestoDocumentos;
	}

	public void setPreManifestoDocumentos(
			List<PreManifestoDocumento> preManifestoDocumentos) {
		this.preManifestoDocumentos = preManifestoDocumentos;
	}

	public List<ManifestoEntregaDocumento> getManifestoEntregaDocumentos() {
		return manifestoEntregaDocumentos;
	}

	public void setManifestoEntregaDocumentos(
			List<ManifestoEntregaDocumento> manifestoEntregaDocumentos) {
		this.manifestoEntregaDocumentos = manifestoEntregaDocumentos;
	}

	public DomainValue getTpLocalizacao() {
		return tpLocalizacao;
	}

	public void setTpLocalizacao(DomainValue tpLocalizacao) {
		this.tpLocalizacao = tpLocalizacao;
	}

	public List<AwbColeta> getAwbColetas() {
		return awbColetas;
	}

	public void setAwbColetas(List<AwbColeta> awbColetas) {
		this.awbColetas = awbColetas;
	}

	public Boolean getBlConferido() {
		return blConferido;
	}

	public void setBlConferido(Boolean blConferido) {
		this.blConferido = blConferido;
	}
	
	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public Awb getAwbSubstituido() {
		return awbSubstituido;
	}

	public void setAwbSubstituido(Awb awbSubstituido) {
		this.awbSubstituido = awbSubstituido;
	}

	public DomainValue getTpMotivoCancelamento() {
		return tpMotivoCancelamento;
	}

	public void setTpMotivoCancelamento(DomainValue tpMotivoCancelamento) {
		this.tpMotivoCancelamento = tpMotivoCancelamento;
	}

	public String getDsMotivoCancelamento() {
		return dsMotivoCancelamento;
	}

	public void setDsMotivoCancelamento(String dsMotivoCancelamento) {
		this.dsMotivoCancelamento = dsMotivoCancelamento;
	}

	public String getDsJustificativaPrejuizo() {
		return dsJustificativaPrejuizo;
	}

	public void setDsJustificativaPrejuizo(String dsJustificativaPrejuizo) {
		this.dsJustificativaPrejuizo = dsJustificativaPrejuizo;
	}

	public Usuario getUsuarioJustificativa() {
		return usuarioJustificativa;
	}

	public void setUsuarioJustificativa(Usuario usuarioJustificativa) {
		this.usuarioJustificativa = usuarioJustificativa;
	}

	public List<LiberaAWBComplementar> getLiberaAWBComplementars() {
		return liberaAWBComplementars;
	}

	public void setLiberaAWBComplementars(
			List<LiberaAWBComplementar> liberaAWBComplementars) {
		this.liberaAWBComplementars = liberaAWBComplementars;
	}

	public List<String> getListNrChaveCteAnt() {
		return listNrChaveCteAnt;
	}

	public void setListNrChaveCteAnt(List<String> listNrChaveCteAnt) {
		this.listNrChaveCteAnt = listNrChaveCteAnt;
	}

	public List<String> getNrChaveInfNfe() {
		return nrChaveInfNfe;
	}

	public void setNrChaveInfNfe(List<String> nrChaveInfNfe) {
		this.nrChaveInfNfe = nrChaveInfNfe;
	}

	public String getDsProddominante() {
		return dsProddominante;
	}

	public void setDsProddominante(String dsProddominante) {
		this.dsProddominante = dsProddominante;
	}

	public String getDsOutCaracteristicas() {
		return dsOutCaracteristicas;
	}

	public void setDsOutCaracteristicas(String dsOutCaracteristicas) {
		this.dsOutCaracteristicas = dsOutCaracteristicas;
	}

	public BigDecimal getVlTotalMercadoria() {
		return vlTotalMercadoria;
	}

	public void setVlTotalMercadoria(BigDecimal vlTotalMercadoria) {
		this.vlTotalMercadoria = vlTotalMercadoria;
	}

	public BigDecimal getPsBaseCalc() {
		return psBaseCalc;
	}

	public void setPsBaseCalc(BigDecimal psBaseCalc) {
		this.psBaseCalc = psBaseCalc;
	}

	public BigDecimal getVlCubagem() {
		return vlCubagem;
	}

	public void setVlCubagem(BigDecimal vlCubagem) {
		this.vlCubagem = vlCubagem;
	}

	public String getNmResponsavelApolice() {
		return nmResponsavelApolice;
	}

	public void setNmResponsavelApolice(String nmResponsavelApolice) {
		this.nmResponsavelApolice = nmResponsavelApolice;
	}

	public BigDecimal getVlTotalServico() {
		return vlTotalServico;
	}

	public void setVlTotalServico(BigDecimal vlTotalServico) {
		this.vlTotalServico = vlTotalServico;
	}

	public BigDecimal getVlReceber() {
		return vlReceber;
	}

	public void setVlReceber(BigDecimal vlReceber) {
		this.vlReceber = vlReceber;
	}

	public String getDsSituacaoTributaria() {
		return dsSituacaoTributaria;
	}

	public void setDsSituacaoTributaria(String dsSituacaoTributaria) {
		this.dsSituacaoTributaria = dsSituacaoTributaria;
	}

	public String getDsInfManuseio() {
		return dsInfManuseio;
	}

	public void setDsInfManuseio(String dsInfManuseio) {
		this.dsInfManuseio = dsInfManuseio;
	}

	public String getCdCargaEspecial() {
		return cdCargaEspecial;
	}

	public void setCdCargaEspecial(String cdCargaEspecial) {
		this.cdCargaEspecial = cdCargaEspecial;
	}

	public String getDsCaracAdicServ() {
		return dsCaracAdicServ;
	}

	public void setDsCaracAdicServ(String dsCaracAdicServ) {
		this.dsCaracAdicServ = dsCaracAdicServ;
	}

	public String getNrOperacionalAereo() {
		return nrOperacionalAereo;
	}

	public void setNrOperacionalAereo(String nrOperacionalAereo) {
		this.nrOperacionalAereo = nrOperacionalAereo;
	}

	public YearMonthDay getDtPrevistaEntrega() {
		return dtPrevistaEntrega;
	}

	public void setDtPrevistaEntrega(YearMonthDay dtPrevistaEntrega) {
		this.dtPrevistaEntrega = dtPrevistaEntrega;
	}

	public String getDsClasse() {
		return dsClasse;
	}

	public void setDsClasse(String dsClasse) {
		this.dsClasse = dsClasse;
	}

	public BigDecimal getVlTarifa() {
		return vlTarifa;
	}

	public void setVlTarifa(BigDecimal vlTarifa) {
		this.vlTarifa = vlTarifa;
	}

	public String getDsIdentificacaoTomador() {
		return dsIdentificacaoTomador;
	}

	public void setDsIdentificacaoTomador(String dsIdentificacaoTomador) {
		this.dsIdentificacaoTomador = dsIdentificacaoTomador;
	}

	public String getDsIdentificacaoEmissor() {
		return dsIdentificacaoEmissor;
	}

	public void setDsIdentificacaoEmissor(String dsIdentificacaoEmissor) {
		this.dsIdentificacaoEmissor = dsIdentificacaoEmissor;
	}

	public Boolean getBlRetira() {
		return blRetira;
	}

	public void setBlRetira(Boolean blRetira) {
		this.blRetira = blRetira;
	}

	public String getDsUsoEmissor() {
		return dsUsoEmissor;
	}

	public void setDsUsoEmissor(String dsUsoEmissor) {
		this.dsUsoEmissor = dsUsoEmissor;
	}

	public List<ParcelaAwb> getParcelasAwb() {
		return parcelasAwb;
	}

	public void setParcelasAwb(List<ParcelaAwb> parcelasAwb) {
		this.parcelasAwb = parcelasAwb;
	}

	public String getDsModeloDacte() {
		return dsModeloDacte;
	}

	public void setDsModeloDacte(String dsModeloDacte) {
		this.dsModeloDacte = dsModeloDacte;
	}

	public Long getNrSerieDacte() {
		return nrSerieDacte;
	}

	public void setNrSerieDacte(Long nrSerieDacte) {
		this.nrSerieDacte = nrSerieDacte;
	}

	public Long getNrDacte() {
		return nrDacte;
	}

	public void setNrDacte(Long nrDacte) {
		this.nrDacte = nrDacte;
	}

	public String getTpCte() {
		return tpCte;
	}

	public void setTpCte(String tpCte) {
		this.tpCte = tpCte;
	}

	public String getTpServico() {
		return tpServico;
	}

	public void setTpServico(String tpServico) {
		this.tpServico = tpServico;
	}

	public String getDsNaturezaPrestacao() {
		return dsNaturezaPrestacao;
	}

	public void setDsNaturezaPrestacao(String dsNaturezaPrestacao) {
		this.dsNaturezaPrestacao = dsNaturezaPrestacao;
	}

	public String getDsAutorizacaoUso() {
		return dsAutorizacaoUso;
	}

	public void setDsAutorizacaoUso(String dsAutorizacaoUso) {
		this.dsAutorizacaoUso = dsAutorizacaoUso;
	}

	public String getCdTarifa() {
		return cdTarifa;
	}

	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setCdTarifa(String cdTarifa) {
		this.cdTarifa = cdTarifa;
	}

	public String getNrMinuta() {
		return nrMinuta;
	}

	public void setNrMinuta(String nrMinuta) {
		this.nrMinuta = nrMinuta;
	}

	public String getNrApoliceSeguro() {
		return nrApoliceSeguro;
	}

	public void setNrApoliceSeguro(String nrApoliceSeguro) {
		this.nrApoliceSeguro = nrApoliceSeguro;
	}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public Usuario getUsuarioCancelamento() {
		return usuarioCancelamento;
	}

	public void setUsuarioCancelamento(Usuario usuarioCancelamento) {
		this.usuarioCancelamento = usuarioCancelamento;
	}

	public DateTime getDhCancelamento() {
		return dhCancelamento;
	}

	public void setDhCancelamento(DateTime dhCancelamento) {
		this.dhCancelamento = dhCancelamento;
	}

	public String getTpCaracteristicaServico() {
		return tpCaracteristicaServico;
	}

	public void setTpCaracteristicaServico(String tpCaracteristicaServico) {
		this.tpCaracteristicaServico = tpCaracteristicaServico;
	}

	public Cliente getClienteByIdClienteTomador() {
		return clienteByIdClienteTomador;
}

	public void setClienteByIdClienteTomador(Cliente clienteByIdClienteTomador) {
		this.clienteByIdClienteTomador = clienteByIdClienteTomador;
	}

	public InscricaoEstadual getInscricaoEstadualTomador() {
		return inscricaoEstadualTomador;
	}

	public void setInscricaoEstadualTomador(InscricaoEstadual inscricaoEstadualTomador) {
		this.inscricaoEstadualTomador = inscricaoEstadualTomador;
	}
}
